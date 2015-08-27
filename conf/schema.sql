
    DROP VIEW user_roles;

    DROP VIEW active_users;

    alter table attendances 
        drop 
        foreign key FKB1E47AAAFEC4B6B;

    alter table attendances 
        drop 
        foreign key FKB1E47AAAD6347FC9;

    alter table countries_labels 
        drop 
        foreign key FK6D130FEAFDE861BB;

    alter table events 
        drop 
        foreign key FKB307E1195747D43B;

    alter table events_keywords 
        drop 
        foreign key FK51051C10BAE61CCB;

    alter table events_keywords 
        drop 
        foreign key FK51051C10FEC4B6B;

    alter table keywords 
        drop 
        foreign key FK1F2E9FAA5C23FE49;

    alter table keywords_texts 
        drop 
        foreign key FK4D0D7C11BAE61CCB;

    alter table languages_labels 
        drop 
        foreign key FKFA7D40A35747D43B;

    alter table mailtemplates 
        drop 
        foreign key FKF013B629526BF2B;

    alter table mailtemplates_bccs 
        drop 
        foreign key FK842F572E130A7140;

    alter table mailtemplates_recipients 
        drop 
        foreign key FK45A85A77130A7140;

    alter table pageversions 
        drop 
        foreign key FK1A9AE22A9526BF2B;

    alter table pageversions_strings 
        drop 
        foreign key FKBA39616D2EFC842B;

    alter table pageversions_texts 
        drop 
        foreign key FKD5DDDE912EFC842B;

    alter table payments 
        drop 
        foreign key FK526A0F2DFEC4B6B;

    alter table payments 
        drop 
        foreign key FK526A0F2DD6347FC9;

    alter table payments 
        drop 
        foreign key FK526A0F2D29EEC287;

    alter table siteversions 
        drop 
        foreign key FK409026E25C23FE49;

    alter table siteversions_strings 
        drop 
        foreign key FKD9DA6E259526BF2B;

    alter table siteversions_texts 
        drop 
        foreign key FK1D4AB9499526BF2B;

    alter table users 
        drop 
        foreign key FK6A68E086CBB881F;

    alter table users_languages 
        drop 
        foreign key FKF33376A4D6347FC9;

    alter table users_languages 
        drop 
        foreign key FKF33376A45747D43B;

    drop table if exists attendances;

    drop table if exists countries;

    drop table if exists countries_labels;

    drop table if exists events;

    drop table if exists events_keywords;

    drop table if exists keywords;

    drop table if exists keywords_texts;

    drop table if exists languages;

    drop table if exists languages_labels;

    drop table if exists mailtemplates;

    drop table if exists mailtemplates_bccs;

    drop table if exists mailtemplates_recipients;

    drop table if exists pageversions;

    drop table if exists pageversions_strings;

    drop table if exists pageversions_texts;

    drop table if exists payments;

    drop table if exists sites;

    drop table if exists siteversions;

    drop table if exists siteversions_strings;

    drop table if exists siteversions_texts;

    drop table if exists users;

    drop table if exists users_languages;

    create table attendances (
        EVENT_ID integer not null,
        STATE varchar(10) not null,
        NUMBER integer not null,
        USER_ID integer not null,
        primary key (EVENT_ID, USER_ID)
    );

    create table countries (
        CODE varchar(2) not null,
        primary key (CODE)
    );

    create table countries_labels (
        LANGUAGE_CODE varchar(2) not null,
        LABEL varchar(255),
        LANGUAGE varchar(2) not null,
        primary key (LANGUAGE_CODE, LANGUAGE)
    );

    create table events (
        ID integer not null auto_increment,
        CREATION datetime not null,
        DATETIME datetime not null,
        LOCATION varchar(255),
        STATE varchar(10) not null,
        ORIENTATION varchar(6) not null,
        RELATIONSHIP varchar(8),
        LANGUAGE_CODE varchar(2) not null,
        AGE_MIN integer,
        AGE_MAX integer,
        ADDRESS longtext,
        PRICE decimal(15,2) not null,
        MAX_ATTENDEES integer not null,
        primary key (ID)
    );

    create table events_keywords (
        EVENT_ID integer not null,
        KEYWORD_ID integer not null,
        primary key (EVENT_ID, KEYWORD_ID)
    );

    create table keywords (
        ID integer not null auto_increment,
        SITE_ID integer,
        primary key (ID)
    );

    create table keywords_texts (
        KEYWORD_ID integer not null,
        VALUE varchar(255),
        LANGUAGE varchar(2) not null,
        primary key (KEYWORD_ID, LANGUAGE)
    );

    create table languages (
        CODE varchar(2) not null,
        primary key (CODE)
    );

    create table languages_labels (
        LANGUAGE_CODE varchar(2) not null,
        LABEL varchar(255),
        LANGUAGE varchar(2) not null,
        primary key (LANGUAGE_CODE, LANGUAGE)
    );

    create table mailtemplates (
        ID integer not null auto_increment,
        SUBTYPE varchar(16),
        SENDER varchar(255),
        SUBJECT varchar(255),
        BODY longtext,
        SITEVERSION_ID integer not null,
        NAME varchar(32),
        primary key (ID)
    );

    create table mailtemplates_bccs (
        TEMPLATE_ID integer not null,
        ADDRESS varchar(255) not null,
        primary key (TEMPLATE_ID, ADDRESS)
    );

    create table mailtemplates_recipients (
        TEMPLATE_ID integer not null,
        ADDRESS varchar(255) not null,
        primary key (TEMPLATE_ID, ADDRESS)
    );

    create table pageversions (
        ID integer not null auto_increment,
        SITEVERSION_ID integer not null,
        NAME varchar(16),
        primary key (ID)
    );

    create table pageversions_strings (
        PAGEVERSION_ID integer not null,
        VALUE varchar(255),
        NAME varchar(32) not null,
        primary key (PAGEVERSION_ID, NAME)
    );

    create table pageversions_texts (
        PAGEVERSION_ID integer not null,
        VALUE longtext,
        NAME varchar(32) not null,
        primary key (PAGEVERSION_ID, NAME)
    );

    create table payments (
        ID varchar(24) not null,
        PARENT_ID varchar(24),
        USER_ID integer,
        EVENT_ID integer,
        DATETIME datetime not null,
        AMOUNT decimal(15,2) not null,
        DETAILS longtext,
        primary key (ID)
    );

    create table sites (
        ID integer not null auto_increment,
        HOSTNAME varchar(64) unique,
        primary key (ID)
    );

    create table siteversions (
        ID integer not null auto_increment,
        SITE_ID integer,
        LANGUAGE varchar(2),
        primary key (ID)
    );

    create table siteversions_strings (
        SITEVERSION_ID integer not null,
        VALUE varchar(255),
        NAME varchar(48) not null,
        primary key (SITEVERSION_ID, NAME)
    );

    create table siteversions_texts (
        SITEVERSION_ID integer not null,
        VALUE longtext,
        NAME varchar(48) not null,
        primary key (SITEVERSION_ID, NAME)
    );

    create table users (
        ID integer not null auto_increment,
        STATUS varchar(10) not null,
        PASSWORD varchar(32) not null,
        EMAIL varchar(255) not null unique,
        FIRSTNAME varchar(32),
        LASTNAME varchar(32),
        ADDRESS1 varchar(255),
        ADDRESS2 varchar(255),
        ZIP varchar(12),
        CITY varchar(32),
        STATE varchar(32),
        COUNTRY varchar(4),
        PHONE varchar(32),
        FAX varchar(32),
        SEX varchar(6) not null,
        ORIENTATION varchar(7) not null,
        RELATIONSHIP varchar(8),
        DATEOFBIRTH date not null,
        REGISTEREDON datetime not null,
        FAVORITELANGUAGE_CODE varchar(2) not null,
        ADMIN bit not null,
        ACTIVATIONKEY varchar(16),
        PHOTO longblob,
        CREDIT decimal(15,2) not null,
        ABOUTME text,
        primary key (ID)
    );

    create table users_languages (
        USER_ID integer not null,
        LANGUAGE_CODE varchar(2) not null,
        primary key (USER_ID, LANGUAGE_CODE)
    );

    alter table attendances 
        add index FKB1E47AAAFEC4B6B (EVENT_ID), 
        add constraint FKB1E47AAAFEC4B6B 
        foreign key (EVENT_ID) 
        references events (ID);

    alter table attendances 
        add index FKB1E47AAAD6347FC9 (USER_ID), 
        add constraint FKB1E47AAAD6347FC9 
        foreign key (USER_ID) 
        references users (ID);

    alter table countries_labels 
        add index FK6D130FEAFDE861BB (LANGUAGE_CODE), 
        add constraint FK6D130FEAFDE861BB 
        foreign key (LANGUAGE_CODE) 
        references countries (CODE);

    create index creation on events (CREATION);

    create index dateTime on events (DATETIME);

    alter table events 
        add index FKB307E1195747D43B (LANGUAGE_CODE), 
        add constraint FKB307E1195747D43B 
        foreign key (LANGUAGE_CODE) 
        references languages (CODE);

    alter table events_keywords 
        add index FK51051C10BAE61CCB (KEYWORD_ID), 
        add constraint FK51051C10BAE61CCB 
        foreign key (KEYWORD_ID) 
        references keywords (ID);

    alter table events_keywords 
        add index FK51051C10FEC4B6B (EVENT_ID), 
        add constraint FK51051C10FEC4B6B 
        foreign key (EVENT_ID) 
        references events (ID);

    alter table keywords 
        add index FK1F2E9FAA5C23FE49 (SITE_ID), 
        add constraint FK1F2E9FAA5C23FE49 
        foreign key (SITE_ID) 
        references sites (ID);

    alter table keywords_texts 
        add index FK4D0D7C11BAE61CCB (KEYWORD_ID), 
        add constraint FK4D0D7C11BAE61CCB 
        foreign key (KEYWORD_ID) 
        references keywords (ID);

    alter table languages_labels 
        add index FKFA7D40A35747D43B (LANGUAGE_CODE), 
        add constraint FKFA7D40A35747D43B 
        foreign key (LANGUAGE_CODE) 
        references languages (CODE);

    alter table mailtemplates 
        add index FKF013B629526BF2B (SITEVERSION_ID), 
        add constraint FKF013B629526BF2B 
        foreign key (SITEVERSION_ID) 
        references siteversions (ID);

    alter table mailtemplates_bccs 
        add index FK842F572E130A7140 (TEMPLATE_ID), 
        add constraint FK842F572E130A7140 
        foreign key (TEMPLATE_ID) 
        references mailtemplates (ID);

    alter table mailtemplates_recipients 
        add index FK45A85A77130A7140 (TEMPLATE_ID), 
        add constraint FK45A85A77130A7140 
        foreign key (TEMPLATE_ID) 
        references mailtemplates (ID);

    alter table pageversions 
        add index FK1A9AE22A9526BF2B (SITEVERSION_ID), 
        add constraint FK1A9AE22A9526BF2B 
        foreign key (SITEVERSION_ID) 
        references siteversions (ID);

    alter table pageversions_strings 
        add index FKBA39616D2EFC842B (PAGEVERSION_ID), 
        add constraint FKBA39616D2EFC842B 
        foreign key (PAGEVERSION_ID) 
        references pageversions (ID);

    alter table pageversions_texts 
        add index FKD5DDDE912EFC842B (PAGEVERSION_ID), 
        add constraint FKD5DDDE912EFC842B 
        foreign key (PAGEVERSION_ID) 
        references pageversions (ID);

    alter table payments 
        add index FK526A0F2DFEC4B6B (EVENT_ID), 
        add constraint FK526A0F2DFEC4B6B 
        foreign key (EVENT_ID) 
        references events (ID);

    alter table payments 
        add index FK526A0F2DD6347FC9 (USER_ID), 
        add constraint FK526A0F2DD6347FC9 
        foreign key (USER_ID) 
        references users (ID);

    alter table payments 
        add index FK526A0F2D29EEC287 (PARENT_ID), 
        add constraint FK526A0F2D29EEC287 
        foreign key (PARENT_ID) 
        references payments (ID);

    alter table siteversions 
        add index FK409026E25C23FE49 (SITE_ID), 
        add constraint FK409026E25C23FE49 
        foreign key (SITE_ID) 
        references sites (ID);

    alter table siteversions_strings 
        add index FKD9DA6E259526BF2B (SITEVERSION_ID), 
        add constraint FKD9DA6E259526BF2B 
        foreign key (SITEVERSION_ID) 
        references siteversions (ID);

    alter table siteversions_texts 
        add index FK1D4AB9499526BF2B (SITEVERSION_ID), 
        add constraint FK1D4AB9499526BF2B 
        foreign key (SITEVERSION_ID) 
        references siteversions (ID);

    alter table users 
        add index FK6A68E086CBB881F (FAVORITELANGUAGE_CODE), 
        add constraint FK6A68E086CBB881F 
        foreign key (FAVORITELANGUAGE_CODE) 
        references languages (CODE);

    alter table users_languages 
        add index FKF33376A4D6347FC9 (USER_ID), 
        add constraint FKF33376A4D6347FC9 
        foreign key (USER_ID) 
        references users (ID);

    alter table users_languages 
        add index FKF33376A45747D43B (LANGUAGE_CODE), 
        add constraint FKF33376A45747D43B 
        foreign key (LANGUAGE_CODE) 
        references languages (CODE);

    CREATE VIEW active_users(USERNAME,PASSWORD) AS SELECT EMAIL,PASSWORD FROM users WHERE STATUS<>'DISABLED';

    CREATE VIEW user_roles(USERNAME,ROLE) AS SELECT EMAIL, 'user' FROM users WHERE STATUS<>'DISABLED' UNION SELECT EMAIL, 'enabled' FROM users WHERE STATUS='ENABLED' UNION SELECT EMAIL, 'admin' FROM users WHERE STATUS='ENABLED' AND ADMIN;
