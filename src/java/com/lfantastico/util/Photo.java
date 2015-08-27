package com.lfantastico.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Photo {
    private static Logger LOG = LoggerFactory.getLogger(Photo.class);

    private SharedImage image;
    private byte[][] exif;
    private byte[][] iptc;
    private Date timestamp;
    private int rotation;

    public static Photo load(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            return load(in, new Date(file.lastModified()));
        } finally {
            in.close();
        }
    }

    public static Photo load(URL url) throws IOException {
        InputStream in = url.openStream();
        try {
            return load(in, new Date());
        } finally {
            in.close();
        }
    }

    public static Photo load(InputStream in, Date timestamp)
            throws IOException {
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
        BufferedImage img = decoder.decodeAsBufferedImage();
        JPEGDecodeParam dparm = decoder.getJPEGDecodeParam();
        byte exif[][] = dparm.getMarkerData(Exif.EXIF_MARKER);
        byte iptc[][] = dparm.getMarkerData(Exif.IPTC_MARKER);
        int rotation = 0;
        if (exif != null && exif.length > 0 && exif[0] != null) {
            Exif reader = new Exif(exif[0]);
            Exif.Directory root = reader.getRootDirectory();
            Exif.Directory dir = root.getDirectory(Exif.EXIF_IFD);
            switch (root.getInt(Exif.ORIENTATION, 0)) {
                case 6:
                    rotation = 90;
                    break;
                case 8:
                    rotation = 270;
                    break;
            }
            Date datetime = dir.getDateTime(Exif.DATETIME_ORIGINAL);
            if (datetime != null) {
                timestamp = datetime;
            }
        }
        return new Photo(new SharedImage(img), exif, iptc, timestamp, rotation);
    }

    public Photo(Photo other) {
        this(other.image, other.exif, other.iptc, other.timestamp,
                other.rotation);
    }

    private Photo(SharedImage image, byte[][] exif, byte[][] iptc,
            Date timestamp, int rotation) {
        this.image = image.addRef();
        this.exif = exif;
        this.iptc = iptc;
        this.timestamp = timestamp;
        this.rotation = rotation;
    }

    public boolean isVertical() {
        return getWidth() < getHeight();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getWidth() {
        return (rotation == 90 || rotation == 270)
                ? image.getHeight() : image.getWidth();
    }

    public int getHeight() {
        return (rotation == 90 || rotation == 270)
                ? image.getWidth() : image.getHeight();
    }

    public Photo derive(BufferedImage img) {
        return new Photo(new SharedImage(img), exif, iptc, timestamp, rotation);
    }

    public void flush() {
        image.release();
        image = null;
    }

    public void convertToGrayScale() {
        long tm = System.currentTimeMillis();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                hints);
        setImage(image.filter(op));
        tm = System.currentTimeMillis()-tm;
        LOG.debug("Conversion to grayscale in " + tm + "ms");
    }

    public void overrideRotation(int newValue) {
        rotation = normalizeAngle(newValue);
    }

    public void rotate(int angle) {
        rotation = normalizeAngle(rotation + angle);
    }

    public void crop(int x, int y, int w, int h) {
        applyRotation();
        if (x+w > image.getWidth()) {
            x = image.getWidth()-w;
        }
        if (y+h > image.getHeight()) {
            y = image.getHeight()-h;
        }
        if (x < 0) {
            w += x;
            x = 0;
        }
        if (y < 0) {
            h += y;
            y = 0;
        }
        BufferedImage img = new BufferedImage(w, h, image.getType());
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(image.getImage(), 0, 0, w, h, x, y, x+w, y+h, null);
        } finally {
            g.dispose();
        }
        setImage(img);
    }

    public void resize(int width, int height) {
        applyRotation();
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        int x = 0;
        int y = 0;
        int w = width;
        int h = height;
        if (imgWidth*height < imgHeight*width) {
            w = imgWidth*height/imgHeight;
            x = (width-w)/2;
        } else {
            h = imgHeight*width/imgWidth;
            y = (height-h)/2;
        }
        BufferedImage img = new BufferedImage(width, height, image.getType());
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(image.getImage(), x, y, w, h, null);
        } finally {
            g.dispose();
        }
        setImage(img);
    }

    public BufferedImage render() {
        applyRotation();
        return image.getImage();
    }

    public void store(float quality, File file) throws IOException {
        applyRotation();
        OutputStream out = new FileOutputStream(file);
        try {
            store(image.getImage(), exif, iptc, quality, out);
        } finally {
            out.close();
        }
        if (timestamp != null) {
            file.setLastModified(timestamp.getTime());
        }
    }

    public void store(float quality, OutputStream out) throws IOException {
        applyRotation();
        store(image.getImage(), exif, iptc, quality, out);
    }

    private void setImage(SharedImage newImage) {
        if (newImage != image) {
            SharedImage oldImage = image;
            if (newImage != null) {
                newImage.addRef();
            }
            image = newImage;
            if (oldImage != null) {
                oldImage.release();
            }
        }
    }

    private void setImage(BufferedImage img) {
        setImage(new SharedImage(img));
    }

    private static void store(BufferedImage img, byte exif[][], byte iptc[][],
            float quality, OutputStream out) throws IOException {
        if (exif != null) {
            // change the orientation
            Exif reader = new Exif(exif[0]);
            Exif.Directory root = reader.getRootDirectory();
            root.setInt(Exif.ORIENTATION, 1);
            root.setInt(Exif.IMAGE_WIDTH, img.getWidth());
            root.setInt(Exif.IMAGE_HEIGHT, img.getHeight());
        }
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam eparm = encoder.getDefaultJPEGEncodeParam(img);
        if (exif != null) {
            eparm.setMarkerData(Exif.EXIF_MARKER, exif);
        }
        if (iptc != null) {
            eparm.setMarkerData(Exif.IPTC_MARKER, iptc);
        }
        eparm.setQuality(quality, true);
        encoder.setJPEGEncodeParam(eparm);
        encoder.encode(img);
    }

    private void applyRotation() {
        if (rotation != 0) {
            AffineTransform trans;
            BufferedImage newImg;
            int w = image.getWidth();
            int h = image.getHeight();
            if (rotation == 90) {
                newImg = new BufferedImage(h, w, image.getType());
                trans = new AffineTransform(0.0, 1.0, -1.0, 0.0, h, 0.0);
            } else if (rotation == 180) {
                newImg = new BufferedImage(w, h, image.getType());
                trans = new AffineTransform(-1.0, 0.0, 0.0, -1.0, w, h);
            } else if (rotation == 270) {
                newImg = new BufferedImage(h, w, image.getType());
                trans = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, w);
            } else {
                throw new RuntimeException("Invalid angle " + rotation);
            }
            Graphics2D g = newImg.createGraphics();
            try {
                g.transform(trans);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.drawImage(image.getImage(), 0, 0, w, h, null);
            } finally {
                g.dispose();
            }
            setImage(newImg);
            rotation = 0;
        }
    }

    private static int normalizeAngle(int angle) {
        while (angle < 0) {
            angle += 360;
        }
        return angle % 360;
    }

    boolean isRotated() {
        return rotation != 0;
    }

    private static class SharedImage {
        private BufferedImage image;
        private int refCount;

        private SharedImage(BufferedImage image) {
            this.image = image;
            this.refCount = 0;
        }

        private BufferedImage getImage() {
            return image;
        }

        private int getWidth() {
            return image.getWidth();
        }

        private int getHeight() {
            return image.getHeight();
        }

        private ColorModel getColorModel() {
            return image.getColorModel();
        }

        private SharedImage filter(BufferedImageOp op) {
            return new SharedImage(op.filter(image, null));
        }

        private SharedImage addRef() {
            ++refCount;
            return this;
        }

        private void release() {
            if (--refCount == 0) {
                image.flush();
                image = null;
                System.gc();
            }
        }

        @Override
        @SuppressWarnings("FinalizeDeclaration")
        protected void finalize() throws Throwable {
            if (image == null) {
                throw new RuntimeException(
                        "A shared image was not released properly");
            }
            image.flush();
            image = null;
            super.finalize();
        }

        private int getType() {
            return image.getType();
        }
    }
}
