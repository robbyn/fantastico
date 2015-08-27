package com.lfantastico.web;

import com.lfantastico.domain.User;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.Request;

public class PhotoPage extends BasePage {
    public void get(@Param("user") String email)
            throws IOException, ServletException {
        Helper helper = Helper.current();
        User user = email == null ? helper.getUser() : helper.getUser(email);
        byte data[] = user == null ? null : user.getPhoto();
        if (data == null) {
            if (user != null) {
                String image = user.getSex() == User.Sex.FEMALE
                        ? "images/female.png" : "images/male.png";
                Request.current().forward(image);
            }
        } else {
            OutputStream out = Request.current().getOutputStream("image/jpeg");
            try {
                out.write(data);
            } finally {
                out.close();
            }
        }
    }
}
