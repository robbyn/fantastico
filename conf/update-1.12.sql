charset utf8

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
     select ID,'already-exists','We already have an account with this e-mail address' from pageversions
     where NAME='reg' and SITEVERSION_ID
         in (select ID from siteversions where language='en');

insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE)
     select ID,'already-exists','Nous avons déjà un compte avec cette adresse e-mail' from pageversions
     where NAME='reg' and SITEVERSION_ID
         in (select ID from siteversions where language='fr');
