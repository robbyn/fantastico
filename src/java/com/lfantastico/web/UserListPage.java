package com.lfantastico.web;

import com.lfantastico.domain.User;
import java.io.IOException;
import java.util.List;
import org.tastefuljava.mvc.Param;

public class UserListPage extends BasePage {
    private String nameFilter = "";

    public String getNameFilter() {
        return nameFilter;
    }

    public List<User> getQueryUsers() {
        return Helper.current().getQueryUsers(nameFilter);
    }

    public void changeFilter(@Param("nameFilter") String newValue)
            throws IOException {
        nameFilter = newValue;
        redirect();
    }

    public void activate(@Param("user") String email,
            @Param("activate") boolean activate) throws IOException {
        Helper helper = Helper.current();
        User user = helper.getUser(email);
        if (user != null) {
            user.setStatus(activate
                    ? User.Status.ENABLED : User.Status.DISABLED);
            helper.commit();
        }
        redirect();
    }
}
