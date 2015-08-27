package com.lfantastico.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.util.Streams;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.Request;

public class BrowserPage extends BasePage {
    private String editor;
    private int funcNumber;
    private String fileName;

    public String getEditor() {
        return editor;
    }

    public int getFuncNumber() {
        return funcNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public String[] getFileNames() {
        File dir = new File(System.getProperty("catalina.base"), "images");
        if (!dir.isDirectory()) {
            return new String[0];
        }
        return dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.startsWith(".")) {
                    return false;
                }
                return new File(dir, name).isFile();
            }
        });
    }

    public void browse(@Param("CKEditor") String editor,
            @Param("CKEditorFuncNum") int funcNumber)
            throws IOException {
        this.editor = editor;
        this.funcNumber = funcNumber;
        redirect();
    }

    public void upload(@Param("CKEditor") String editor,
            @Param("CKEditorFuncNum") Integer funcNumber)
            throws IOException, ServletException {
        if (editor != null) {
            this.editor = editor;
        }
        if (funcNumber != null) {
            this.funcNumber = funcNumber;
        }
        fileName = null;
        File dir = new File(System.getProperty("catalina.base"), "images");
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        Request req = Request.current();
        for (FileItemStream item: req.getItems()) {
            if (item.isFormField()) {
                if ("CKEditor".equals(item.getFieldName())) {
                    this.editor = Streams.asString(item.openStream());
                } else if ("CKEditorFuncNum".equals(item.getFieldName())) {
                    String s = Streams.asString(item.openStream());
                    this.funcNumber = Integer.parseInt(s);
                }
            } else {
                String name = item.getName();
                if (name != null && name.length() > 0) {
                    int pos = Math.max(name.lastIndexOf('\\'),
                            name.lastIndexOf('/'));
                    if (pos >= 0) {
                        name = name.substring(pos+1);
                    }
                    fileName = uniqueName(dir, name);
                    File file = new File(dir, fileName);
                    InputStream in = item.openStream();
                    try {
                        OutputStream out = new FileOutputStream(file);
                        try {
                            copy(in, out);
                        } finally {
                            out.close();
                        }
                    } finally {
                        in.close();
                    }
                }
            }
        }
        if (fileName == null) {
            redirect();
        } else {
            forward("upload.jsp");
        }
    }

    public void file(@Param("name") String name) throws IOException {
        File dir = new File(System.getProperty("catalina.base"), "images");
        File file = new File(dir, name);
        Request req = Request.current();
        String mimeType = req.getMimeType(name);
        OutputStream out = req.getOutputStream(mimeType);
        try {
            InputStream in = new FileInputStream(file);
            try {
                copy(in, out);
            } finally {
                in.close();
            }
        } finally {
            out.close();
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte data[] = new byte[4096];
        for (int n = in.read(data); n > 0; n = in.read(data)) {
            out.write(data, 0, n);
        }
    }

    private String uniqueName(File dir, String name) {
        File file = new File(dir, name);
        if (!file.exists()) {
            return name;
        }
        int extPos = name.lastIndexOf('.');
        String ext = "";
        if (extPos >= 0) {
            ext = name.substring(extPos);
            name = name.substring(0, extPos);
        }
        int n = 1;
        do {
            file = new File(dir, name + "(" + n + ")" + ext);
            ++n;
        } while (file.exists());
        return file.getName();
    }
}
