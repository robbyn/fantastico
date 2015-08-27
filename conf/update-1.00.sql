charset utf8

    create table mailtemplates (
        ID integer not null auto_increment,
        SUBJECT varchar(255),
        BODY longtext,
        SITEVERSION_ID integer not null,
        NAME varchar(32),
        primary key (ID)
    );

    create table mailtemplates_bccs (
        TEMPLATE_ID integer not null,
        BCC varchar(255) not null,
        primary key (TEMPLATE_ID, BCC)
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


-- Email page

insert into pageversions(SITEVERSION_ID,NAME) select max(ID),'email' from siteversions where language='en';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'title','E-mail templates' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'type.label','Type' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'bcc.label','BCC' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'subject.label','Subject' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'body.label','Message' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'submit.label','Save' from pageversions;

-- Email page

insert into pageversions(SITEVERSION_ID,NAME) select max(ID),'email' from siteversions where language='fr';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'title','Modèles d\'e-mails' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'type.label','Type' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'bcc.label','Copie cachée à' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'subject.label','Objet' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'body.label','Message' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'submit.label','Enregistrer' from pageversions;
