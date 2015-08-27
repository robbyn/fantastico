charset utf8

alter table events add MAX_ATTENDEES integer not null default 16;
alter table attendances add NUMBER integer not null default 0;

insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.attendees.label','Number of attendees' from siteversions where LANGUAGE='en';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event-list.place.label','Placement' from siteversions where LANGUAGE='en';
insert into pageversions(SITEVERSION_ID,NAME) select ID,'place' from siteversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'title','Placement' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'available','Applicants' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'females','Women' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'males','Men' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'groupa','Group A' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'groupb','Group B' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');

insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event.attendees.label','Nombre de participants' from siteversions where LANGUAGE='fr';
insert into siteversions_strings(SITEVERSION_ID,NAME,VALUE) select ID,'event-list.place.label','Placement' from siteversions where LANGUAGE='fr';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'title','Placement' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'available','Inscrits' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'females','Femmes' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'males','Hommes' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'groupa','Groupe A' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'groupb','Groupe B' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
