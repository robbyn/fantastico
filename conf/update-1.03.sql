charset utf8

-- Application page

update pageversions set NAME='apply' where name='book';
update pageversions_strings set value='Apply'
where NAME='book.label' and PAGEVERSION_ID=(
    select p.ID from pageversions p,siteversions s
    where s.ID=p.SITEVERSION_ID and p.name='main' and s.language='en');

-- Applied page

insert into pageversions(SITEVERSION_ID,NAME) select max(ID),'applied' from siteversions where language='en';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'title','Thank You for your application' from pageversions;

insert into pageversions(SITEVERSION_ID,NAME) select max(ID),'applied' from siteversions where language='fr';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'title','Merci pour votre inscription' from pageversions;
