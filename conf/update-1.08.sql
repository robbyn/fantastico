charset utf8

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'photo.label','Photo' from pageversions
    where NAME='applied' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'send-photo.label','Send photo' from pageversions
    where NAME='applied' and SITEVERSION_ID in (select ID from siteversions where language='en');

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'photo.label','Photo' from pageversions
    where NAME='applied' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'send-photo.label','Envoyer la photo' from pageversions
    where NAME='applied' and SITEVERSION_ID in (select ID from siteversions where language='fr');

alter table events add CREATION datetime not null default 0;
update events set CREATION=DATETIME;

create index creation on events (CREATION);
create index dateTime on events (DATETIME);
