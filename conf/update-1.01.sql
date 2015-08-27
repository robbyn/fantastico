charset utf8

    drop table if exists mailtemplates_bccs;

    drop table if exists mailtemplates_recipients;

    drop table if exists mailtemplates;

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

-- Email page

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select p.ID,'from.label','From' from pageversions p,siteversions s where s.ID=p.SITEVERSION_ID and name='email' and language='en';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select p.ID,'to.label','To' from pageversions p,siteversions s where s.ID=p.SITEVERSION_ID and name='email' and language='en';

-- Email page

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select p.ID,'from.label','De' from pageversions p,siteversions s where s.ID=p.SITEVERSION_ID and name='email' and language='fr';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select p.ID,'to.label','Pour' from pageversions p,siteversions s where s.ID=p.SITEVERSION_ID and name='email' and language='fr';
