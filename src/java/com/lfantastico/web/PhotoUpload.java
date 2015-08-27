package com.lfantastico.web;

import com.lfantastico.domain.User;
import com.lfantastico.util.Photo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.apache.commons.fileupload.FileItemStream;
import org.tastefuljava.mvc.Request;

public class PhotoUpload {
    private PhotoUpload() {
    }

    public static void changePhoto(User usr) throws IOException {
        Helper helper = Helper.current();
        usr.setPhoto(null);
        if (usr != null) {
            Request req = Request.current();
            for (FileItemStream item: req.getItems()) {
                if (!item.isFormField()) {
                    String name = item.getName();
                    if (name != null && name.length() > 0) {
                        usr.setPhoto(getPhotoBytes(item));
                    }
                }
            }
        }
        helper.commit();
    }

    private static byte[] getPhotoBytes(FileItemStream item)
            throws IOException {
        Photo photo;
        InputStream in = item.openStream();
        try {
            photo = Photo.load(in, new Date());
        } finally {
            in.close();
        }
        photo.resize(175, 225);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        photo.store(0.80f, out);
        out.close();
        return out.toByteArray();
    }
}
