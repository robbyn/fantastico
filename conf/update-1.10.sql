charset utf8
alter table users add ABOUTME text;

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
     select ID,'about-me.label','About me' from pageversions
     where NAME='profile' and SITEVERSION_ID
         in (select ID from siteversions where language='en');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
     select ID,'about-me.label','About me' from pageversions
     where NAME='reg' and SITEVERSION_ID
         in (select ID from siteversions where language='en');

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
     select ID,'about-me.label','A propos de moi' from pageversions
     where NAME='profile' and SITEVERSION_ID
         in (select ID from siteversions where language='fr');
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
     select ID,'about-me.label','A propos de moi' from pageversions
     where NAME='reg' and SITEVERSION_ID
         in (select ID from siteversions where language='fr');
