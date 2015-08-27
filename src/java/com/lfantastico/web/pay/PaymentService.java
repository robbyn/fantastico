package com.lfantastico.web.pay;

import com.lfantastico.util.Slf4jLogChute;
import com.lfantastico.util.Util;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tastefuljava.mvc.Request;

public class PaymentService {
    private static final Logger LOG
            = LoggerFactory.getLogger(PaymentService.class);
    private static final String PROP_FILE_NAME = "paypal.properties";
    private static final Map<String,String> PAYPAL_LANGUAGES;
    private static final String PDT_PATH = "/cgi-bin/webscr";
    private static final String IPN_PATH = "/cgi-bin/webscr";
    private static final Pattern CONTENT_TYPE_PATTERN
            = Pattern.compile("^[a-zA-Z_0-9+-]+/[a-zA-Z_0-9+-]+\\s*"
                + "(?:;\\s*charset=([a-zA-Z_0-9+-]+))$");

    private String baseURL;
    private String business;
    private String certId;
    private PrivateKey ownKey;
    private X509Certificate ownCert;
    private X509Certificate paypalCert;
    private String identityToken;
    private int connectTimeout;
    private int readTimeout;

    public static PaymentService getInstance() throws IOException {
        return new PaymentService(loadProps());
    }

    private PaymentService(Properties props) throws IOException {
        try {
            baseURL = props.getProperty("base-url");
            business = props.getProperty("business");
            certId = props.getProperty("cert-id");
            identityToken = props.getProperty("identity-token");
            connectTimeout = Integer.parseInt(
                    props.getProperty("connect-timeout"));
            readTimeout = Integer.parseInt(
                    props.getProperty("read-timeout"));
            KeyStore ks = KeyStore.getInstance("JKS");
            URL url = getResource(props.getProperty("key-store"));
            InputStream in = url.openStream();
            try {
                ks.load(in, null);
            } finally {
                in.close();
            }
            String keyAlias = props.getProperty("key-alias");
            String keyPwd = props.getProperty("key-password");
            ownKey = (PrivateKey)ks.getKey(keyAlias, keyPwd.toCharArray());
            ownCert = (X509Certificate)ks.getCertificate(keyAlias);
            String ppCertAlias = props.getProperty("paypal-cert-alias");
            paypalCert = (X509Certificate)ks.getCertificate(ppCertAlias);
        } catch (CertificateException e) {
            LOG.error("Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (UnrecoverableKeyException e) {
            LOG.error("Error loading key store", e);
            throw new IOException(e.getMessage());
        } catch (KeyStoreException e) {
            LOG.error("Error loading key store", e);
            throw new IOException(e.getMessage());
        }
    }

    public PaymentButton createButton() {
        return new PaymentButton(this);
    }

    public PaymentInfo getPaymentInfo(String txid) throws IOException {
        URL uploadUrl = new URL(baseURL + PDT_PATH);
        HttpURLConnection con = (HttpURLConnection)uploadUrl.openConnection();
        try {
            StringBuilder buf = new StringBuilder();
            buf.append("cmd=_notify-synch&tx=");
            buf.append(txid);
            buf.append("&at=");
            buf.append(identityToken);
            byte[] content = buf.toString().getBytes("ASCII");
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            con.setFixedLengthStreamingMode(content.length);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            OutputStream out = con.getOutputStream();
            try {
                out.write(content);
            } finally {
                out.close();
            }
            int code = con.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                String msg = con.getResponseMessage();
                throw new IOException("HTTP Error " + code + ": " + msg);
            }
            if (LOG.isDebugEnabled()) {
                dumpHeaderFields(con.getHeaderFields());
            }
            String enc = "UTF-8";
            String type = con.getHeaderField("Content-Type");
            if (type != null) {
                Matcher matcher = CONTENT_TYPE_PATTERN.matcher(type);
                if (matcher.matches()) {
                    enc = matcher.group(1);
                }
            }
            InputStream stream = con.getInputStream();
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(stream, enc));
                String s = in.readLine();
                if (!"SUCCESS".equals(s)) {
                    throw new IOException("Transaction failed " + txid);
                }
                Map<String,String> attrs = new HashMap<String,String>();
                while (true) {
                    s = in.readLine();
                    if (s == null) {
                        break;
                    }
                    int pos = s.indexOf('=');
                    String name = s.substring(0, pos);
                    String val = URLDecoder.decode(s.substring(pos+1), enc);
                    LOG.debug("    " + name + "=" + val);
                    attrs.put(name, val);
                }
                return createPaymentInfo(attrs);
            } finally {
                stream.close();
            }
        } finally {
            con.disconnect();
        }
    }

    public PaymentInfo createPaymentInfo(Map<String,String> attrs) {
        PaymentInfo pi = new PaymentInfo();
        for (Map.Entry<String,String> entry: attrs.entrySet()) {
            putInfo(pi, entry.getKey(), entry.getValue());
        }
        return pi;
    }

    public PaymentInfo processNotification(Request req) throws IOException {
        Map<String,String> attrs = new HashMap<String,String>();
        StringBuilder buf = new StringBuilder("cmd=_notify-validate");
        for (String name: req.getParameterNames()) {
            String value = req.getParameter(name);
            attrs.put(name, value);
            buf.append('&');
            buf.append(name);
            buf.append('=');
            buf.append(URLEncoder.encode(value, "UTF-8"));
        }
        byte[] content = buf.toString().getBytes("UTF-8");
        URL uploadUrl = new URL(baseURL + PDT_PATH);
        HttpURLConnection con = (HttpURLConnection)uploadUrl.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            con.setFixedLengthStreamingMode(content.length);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            OutputStream out = con.getOutputStream();
            try {
                out.write(content);
            } finally {
                out.close();
            }
            int code = con.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                String msg = con.getResponseMessage();
                throw new IOException("HTTP Error " + code + ": " + msg);
            }
            String res;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            try {
                res = in.readLine();
            } finally {
                in.close();
            }
            if (res.equals("VERIFIED")) {
                return createPaymentInfo(attrs);
            } else {
                throw new IOException("Payment verification failed");
            }
        } finally {
            con.disconnect();
        }
    }

    private void putInfo(PaymentInfo pi, String name, String val) {
        if ("txn_id".equals(name)) {
            pi.setTransactionId(val);
        } else if ("payment_date".equals(name)) {
            pi.setDate(Util.parseDateTime(val, "HH:mm:ss MMM dd, yyyy z"));
        } else if ("custom".equals(name)) {
            pi.setCustomerId(val);
        } else if ("item_number".equals(name)) {
            pi.setItemCode(val);
        } else if ("mc_gross".equals(name)) {
            pi.setPrice(Util.parseDecimal(val));
        } else if ("mc_currency".equals(name)) {
            pi.setCurrency(val);
        }
        String s = pi.getDetails();
        if (s == null) {
            s = "";
        }
        pi.setDetails(s + name + '=' + val + '\n');
    }

    void renderButton(PaymentButton button, Writer out) {
        try {
            String params = buttonParams(button);
            String encrypted = encrypt(params.getBytes("UTF-8"));
            VelocityContext context = new VelocityContext();
            context.put("button", button);
            context.put("baseURL", baseURL);
            context.put("encrypted", encrypted);
            VelocityEngine engine = new VelocityEngine();
            engine.setProperty("resource.loader", "classpath");
            engine.setProperty("classpath.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader."
                    + "ClasspathResourceLoader");
            engine.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM,
                    new Slf4jLogChute(LOG));
            engine.init();
            engine.mergeTemplate(
                    getPackagePath() + "/button.vm", "UTF-8", context, out);
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String encrypt(byte[] data)
            throws IOException, GeneralSecurityException {
        try {
            CMSSignedDataGenerator sg = new CMSSignedDataGenerator();
            sg.addSigner(ownKey, ownCert, CMSSignedDataGenerator.DIGEST_SHA1);
            ArrayList<X509Certificate> certs = new ArrayList<X509Certificate>();
            certs.add(ownCert);
            CertStore certStore = CertStore.getInstance("Collection",
                    new CollectionCertStoreParameters(certs));
            sg.addCertificatesAndCRLs(certStore);
            CMSProcessableByteArray cmsba = new CMSProcessableByteArray(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cmsba.write(baos);
            CMSSignedData signedData = sg.generate(cmsba, true, "BC");
            byte[] signed = signedData.getEncoded();
            CMSEnvelopedDataGenerator eg = new CMSEnvelopedDataGenerator();
            eg.addKeyTransRecipient(paypalCert);
            CMSEnvelopedData envData = eg.generate(
                    new CMSProcessableByteArray(signed),
                    CMSEnvelopedDataGenerator.DES_EDE3_CBC, "BC");
            byte[] bytes = envData.getEncoded();
            String encoded = new String(Base64.encode(bytes), "ASCII");
            return "-----BEGIN PKCS7-----" + encoded + "-----END PKCS7-----";
        } catch (CMSException e) {
            LOG.error("Error encrypting button data", e);
            throw new GeneralSecurityException(e.getMessage());
        }
    }

    private static Properties loadProps() throws IOException {
        Properties props = new Properties();
        loadDefaultProps(props);
        URL url = getConfResource(PROP_FILE_NAME);
        if (url != null) {
            InputStream in = url.openStream();
            try {
                props.load(in);
            } finally {
                in.close();
            }
        }
        return props;
    }

    private static void loadDefaultProps(Properties props) throws IOException {
        Class thisClass = PaymentService.class;
        InputStream in = getClassPathResource(PROP_FILE_NAME).openStream();
        try {
            props.load(in);
        } finally {
            in.close();
        }
    }

    private static URL getResource(String fileName) throws IOException {
        URL url = getConfResource(fileName);
        if (url == null) {
            url = getClassPathResource(fileName);
        }
        return url;
    }

    private static URL getConfResource(String fileName) throws IOException {
        String base = System.getProperty("catalina.base");
        if (base == null) {
            return null;
        }
        File dir = new File(base, "conf");
        if (!dir.isDirectory()) {
            return null;
        }
        File file = new File(dir, PROP_FILE_NAME);
        if (!file.isFile() || !file.canRead()) {
            return null;
        }
        return file.toURI().toURL();
    }

    private static URL getClassPathResource(String fileName) {
        Class thisClass = PaymentService.class;
        return thisClass.getResource(fileName);
    }

    private static String getPackagePath() {
        Class thisClass = PaymentService.class;
        return thisClass.getPackage().getName().replace('.', '/');
    }

    private String buttonParams(PaymentButton button) {
        StringBuilder buf = new StringBuilder();
        appendParam("charset", "UTF-8", buf);
        appendParam("business", business, buf);
        appendParam("cert_id", certId, buf);
        appendParam("cmd", "_xclick", buf);
        appendParam("no_note", "1", buf);
        appendParam("custom", button.getCustomerId(), buf);
        appendParam("item_number", button.getItemCode(), buf);
        appendParam("item_name", button.getItemLabel(), buf);
        appendParam("amount", Util.formatDecimal(button.getPrice()), buf);
        appendParam("currency_code", button.getCurrency(), buf);
        appendParam("lc", paypalLanguage(button.getLanguage()), buf);
        appendParam("notify_url", button.getNotifyUrl(), buf);
        appendParam("return", button.getReturnUrl(), buf);
        appendParam("cancel_return", button.getCancelUrl(), buf);
        appendParam("address_override","1", buf);
        appendParam("email", button.getEmail(), buf);
        appendParam("first_name", button.getFirstName(), buf);
        appendParam("last_name", button.getLastName(), buf);
        appendParam("address1", button.getAddress1(), buf);
        appendParam("address2", button.getAddress2(), buf);
        appendParam("zip", button.getZip(), buf);
        appendParam("city", button.getCity(), buf);
        if ("US".equals(button.getCountry())) {
            appendParam("state", button.getState(), buf);
        }
        appendParam("country", button.getCountry(), buf);
        return buf.toString();
    }

    private static void appendParam(String name, String value,
            StringBuilder buf) {
        buf.append(name);
        buf.append('=');
        buf.append(value);
        buf.append('\n');
    }

    private static String paypalLanguage(String language) {
        String result = language == null
                ? null : PAYPAL_LANGUAGES.get(language);
        return result == null ? "US" : result;
    }

    private void dumpHeaderFields(Map<String, List<String>> fields) {
        LOG.debug("Header fields:");
        String names[] = fields.keySet().toArray(new String[fields.size()]);
        for (String name: names) {
            LOG.debug("    " + name + ": " + fields.get(name));
        }
    }

    public static void initialize() {
        LOG.info("Registering bouncy castle");
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void terminate() {
        LOG.info("Unregistering bouncy castle");
        Security.removeProvider("BC");
    }

    static {
        PAYPAL_LANGUAGES = new HashMap<String,String>();
        PAYPAL_LANGUAGES.put("en", "US");
        PAYPAL_LANGUAGES.put("fr", "FR");
        PAYPAL_LANGUAGES.put("it", "IT");
        PAYPAL_LANGUAGES.put("de", "DE");
    }
}
