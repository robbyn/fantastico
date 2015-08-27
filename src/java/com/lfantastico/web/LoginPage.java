package com.lfantastico.web;

import java.io.IOException;
import javax.servlet.ServletException;

public class LoginPage extends BasePage {

    public void error() throws ServletException, IOException {
        addError("", "failed");
        forward();
    }
}
