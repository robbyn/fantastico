charset utf8

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

    create table events_keywords (
        EVENT_ID integer not null,
        KEYWORD_ID integer not null,
        primary key (EVENT_ID, KEYWORD_ID)
    );

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

insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.keywords.label','Keywords' from siteversions where LANGUAGE='en';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.add-keyword.label','Add keyword...' from siteversions where LANGUAGE='en';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.save-keyword.label','Save' from siteversions where LANGUAGE='en';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.delete-keyword.label','Delete' from siteversions where LANGUAGE='en';

insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.keywords.label','Mots-clé' from siteversions where LANGUAGE='fr';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.add-keyword.label','Ajouter un mot-clé...' from siteversions where LANGUAGE='fr';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.save-keyword.label','Enregistrer' from siteversions where LANGUAGE='fr';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.delete-keyword.label','Supprimer' from siteversions where LANGUAGE='fr';
