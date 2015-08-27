package com.lfantastico.domain;

import com.lfantastico.util.Util;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {
    private static final Logger LOG = LoggerFactory.getLogger(User.class);

    private int id = -1;
    private Status status;
    private String passwordHash;
    private String email;
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String zip;
    private String city;
    private String state;
    private String country = "CH";
    private String phoneNumber;
    private String faxNumber;
    private Sex sex;
    private Orientation orientation = Orientation.HETERO;
    private Relationship relationship;
    private Date dateOfBirth;
    private Date registeredOn;
    private Language favoriteLanguage;
    private Set<Language> languages = new HashSet<Language>();
    private boolean admin;
    private String activationKey;
    private byte photo[];
    private BigDecimal credit = BigDecimal.ZERO;
    private String aboutMe;

    public enum Status {
        REGISTERED, ENABLED, DISABLED;

        public String getName() {
            return name();
        }
    }

    public enum Sex {
        MALE, FEMALE;

        public String getName() {
            return name();
        }

        public Sex getOpposite() {
            switch (this) {
                case FEMALE:
                    return MALE;
                default:
                    return FEMALE;
            }
        }
    }

    public enum Orientation {
        HETERO, HOMO, BI;

        public String getName() {
            return name();
        }
    }

    public User() {
    }

    public User(User other) {
        id = other.getId();
        status = other.getStatus();
        passwordHash = other.getPasswordHash();
        email = other.getEmail();
        firstName = other.getFirstName();
        lastName = other.getLastName();
        address1 = other.getAddress1();
        address2 = other.getAddress2();
        zip = other.getZip();
        city = other.getCity();
        state = other.getState();
        country = other.getCountry();
        phoneNumber = other.getPhoneNumber();
        faxNumber = other.getFaxNumber();
        sex = other.getSex();
        orientation = other.getOrientation();
        relationship = other.getRelationship();
        dateOfBirth = other.getDateOfBirth();
        registeredOn = other.getRegisteredOn();
        favoriteLanguage = other.getFavoriteLanguage();
        for (Language lng: other.getLanguages()) {
            languages.add(lng);
        }
        admin = other.isAdmin();
        activationKey = other.getActivationKey();
        photo = other.getPhoto();
        credit = other.getCredit();
        aboutMe = other.getAboutMe();
    }

    public int getId() {
        return id;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String newValue) {
        phoneNumber = newValue;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public void setPassword(String newValue) {
        passwordHash = Util.hash(newValue);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean hasPassword() {
        return passwordHash != null;
    }

    public boolean checkPassword(String password) {
        if (passwordHash == null) {
            throw new IllegalStateException("User password is not set");
        }
        if (password == null) {
            throw new IllegalArgumentException(
                "Null password passed to User.checkPassword");
        }
        String hash = Util.hash(password);
        return hash.equals(passwordHash);
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(Date registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status state) {
        this.status = state;
    }

    public Language getFavoriteLanguage() {
        return favoriteLanguage;
    }

    public void setFavoriteLanguage(Language language) {
        this.favoriteLanguage = language;
        if (language != null) {
            languages.add(language);
        }
    }

    public Set<Language> getLanguages() {
        return Collections.unmodifiableSet(languages);
    }

    public void setLanguages(Collection<Language> newLanguages) {
        languages.clear();
        if (newLanguages != null) {
            languages.addAll(newLanguages);
        }
        if (favoriteLanguage != null) {
            languages.add(favoriteLanguage);
        }
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public boolean getHasPhoto() {
        return photo != null;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean activate(String key) {
        if (!key.equals(activationKey)) {
            return false;
        } else if (status == Status.REGISTERED) {
            status = Status.ENABLED;
        }
        return true;
    }

    public void generateActivationKey() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte data[] = new byte[8];
            random.nextBytes(data);
            activationKey = Util.hex(data);
        } catch (NoSuchAlgorithmException ex) {
            LOG.error("Error generating activation key", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public int getAgeOn(Date date) {
        Calendar cal = Calendar.getInstance(
                TimeZone.getTimeZone("Europe/Zurich"));
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        cal.setTime(dateOfBirth);
        int birthYear = cal.get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        Date birthDay = cal.getTime();
        int age = year-birthYear;
        if (date.before(birthDay)) {
            age -= 1;
        }
        return age;
    }

    public int getAgeNow() {
        return getAgeOn(new Date());
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public void addCredit(BigDecimal amount) {
        credit = credit.add(amount);
    }

    public void subtractCredit(BigDecimal amount) {
        credit = credit.subtract(amount);
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
