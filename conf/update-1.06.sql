charset utf8

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'change-photo.label','Change photo...' from pageversions
    where NAME='profile' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'send-photo.label','Submit' from pageversions
    where NAME='profile' and SITEVERSION_ID in (select ID from siteversions where language='en');

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'change-photo.label','Changer de photo...' from pageversions
    where NAME='profile' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'send-photo.label','Envoyer' from pageversions
    where NAME='profile' and SITEVERSION_ID in (select ID from siteversions where language='fr');
