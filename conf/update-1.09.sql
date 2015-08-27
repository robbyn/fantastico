charset utf8

insert into pageversions(SITEVERSION_ID,NAME) select ID,'event-list' from siteversions;

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'cancel.label','Cancel' from pageversions
    where NAME='event-list' and SITEVERSION_ID in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'validate.label','Validate' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='en');

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'cancel.label','Annuler' from pageversions
    where NAME='event-list' and SITEVERSION_ID in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
    select ID,'validate.label','Valider' from pageversions
    where NAME='place' and SITEVERSION_ID in (select ID from siteversions where language='fr');
