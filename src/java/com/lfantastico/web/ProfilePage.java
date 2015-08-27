package com.lfantastico.web;

import com.lfantastico.domain.Relationship;
import com.lfantastico.domain.User;
import com.lfantastico.domain.User.Orientation;
import com.lfantastico.domain.User.Sex;
import com.lfantastico.util.Util;
import java.io.IOException;
import java.util.Date;
import org.tastefuljava.mvc.Param;
import org.tastefuljava.mvc.Request;

public class ProfilePage extends BasePage {
    private int userId = -1;
    private String email = "";
    private String password = "";
    private String verification = "";
    private User.Sex sex = User.Sex.FEMALE;
    private User.Orientation orientation = User.Orientation.HETERO;
    private String relationship = "any";
    private String dateOfBirth = "";
    private String lastName = "";
    private String firstName = "";
    private String address1 = "";
    private String address2 = "";
    private String zip = "";
    private String city = "";
    private String state = "";
    private String country = "";
    private String phone = "";
    private String userLanguage = "";
    private Boolean admin = false;
    private String credit = "";
    private String aboutMe = "";

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCredit() {
        return credit;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getRelationship() {
        return relationship;
    }

    public Sex getSex() {
        return sex;
    }

    public String getState() {
        return state;
    }

    public int getUserId() {
        return userId;
    }

    public String getVerification() {
        return verification;
    }

    public String getZip() {
        return zip;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void create() throws IOException {
        userId = -1;
        email = "";
        password = "";
        verification = "";
        sex = User.Sex.FEMALE;
        orientation = User.Orientation.HETERO;
        relationship = "any";
        dateOfBirth = "";
        lastName = "";
        firstName = "";
        address1 = "";
        address2 = "";
        zip = "";
        city = "";
        state = "";
        country = "";
        phone = "";
        userLanguage = "";
        admin = false;
        credit = "";
        aboutMe = "";
        redirect();
    }

    public void edit(@Param("user") String email) throws IOException {
        Helper helper = Helper.current();
        User user = helper.getUser(email);
        if (user == null) {
            setUser(new User());
        } else {
            setUser(user);
        }
        redirect();
    }

    private void setUser(User user) {
        userId = user.getId();
        this.email = user.getEmail();
        password = "";
        verification = "";
        sex = user.getSex();
        orientation = user.getOrientation();
        relationship = user.getRelationship() == null
                ? "any" : user.getRelationship().name();
        dateOfBirth = Util.formatDate(user.getDateOfBirth());
        lastName = user.getLastName();
        firstName = user.getFirstName();
        address1 = user.getAddress1();
        address2 = user.getAddress2();
        zip = user.getZip();
        city = user.getCity();
        state = user.getState();
        country = user.getCountry();
        phone = user.getPhoneNumber();
        userLanguage = user.getFavoriteLanguage() == null
                ? "" : user.getFavoriteLanguage().getCode();
        admin = user.isAdmin();
        credit = Util.formatDecimal(user.getCredit());
        aboutMe = Util.null2blank(user.getAboutMe());
    }

    public void myProfile() throws IOException {
        Helper helper = Helper.current();
        User user = helper.getUser();
        if (user == null) {
            setUser(new User());
        } else {
            setUser(user);
        }
        redirect();
    }

    public void changePhoto() throws IOException {
        Helper helper = Helper.current();
        PhotoUpload.changePhoto(helper.getUserById(userId));
        redirect();
    }

    public void save() throws IOException {
        Request req = Request.current();
        req.save(this);
        Helper helper = Helper.current();
        User user;
        if (userId >= 0) {
            user = helper.getUserById(userId);
            if (!email.equals(user.getEmail())
                    && helper.getUser(email) != null) {
                addError("email", "already-exists");
            }
        } else {
            if (helper.getUser(email) != null) {
                addError("email", "already-exists");
            }
            user = new User();
            user.setStatus(User.Status.ENABLED);
        }
        user.setEmail(email);
        if (!Util.eq(password, verification)) {
            addError("verification", "verification");
        } else if (!Util.isBlank(password)) {
            user.setPassword(password);
        } else if (!user.hasPassword()) {
            addError("password", "verification");
        }
        user.setSex(sex);
        user.setOrientation(orientation);
        user.setRelationship(relationship == null || "any".equals(relationship)
                ? null : Relationship.valueOf(relationship));
        try {
            user.setDateOfBirth(Util.parseDate(dateOfBirth));
        } catch (NumberFormatException e) {
            addError("dateOfBirth", "invalid-date", dateOfBirth);
        }
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setAddress1(address1);
        user.setAddress2(address2);
        user.setZip(zip);
        user.setCity(city);
        user.setState(state);
        user.setCountry(country);
        user.setPhoneNumber(phone);
        user.setFavoriteLanguage(helper.getLanguage(userLanguage));
        user.setAboutMe(Util.blank2null(aboutMe));
        if (req.hasRole("admin")) {
            user.setAdmin(admin == null ? false : admin);
            try {
                user.setCredit(Util.parseDecimal(credit));
            } catch (NumberFormatException e) {
                addError("credit", "invalid-number", credit);
            }
        }
        if (user.getSex() == null) {
            addError("sex", "required.sex");
        }
        if (user.getDateOfBirth() == null) {
            addError("dateOfBirth", "required.dateOfBirth");
        }
        if (Util.isBlank(user.getEmail())) {
            addError("email", "required.email");
        }
        if (Util.isBlank(user.getFirstName())) {
            addError("email", "required.firstName");
        }
        if (Util.isBlank(user.getLastName())) {
            addError("email", "required.lastName");
        }
        if (user.getId() < 0) {
            user.generateActivationKey();
            user.setRegisteredOn(new Date());
        }
        if (!hasErrors()) {
            if (user.getId() < 0) {
                helper.save(user);
                userId = user.getId();
            }
            helper.commit();
            afterSave(password);
        } else {
            redirect();
        }
    }

    protected void afterSave(String password) throws IOException {
        redirect();
    }
}
