charset utf8

-- Application page

insert into pageversions(SITEVERSION_ID,NAME) select max(ID),'book' from siteversions where language='en';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'title','Apply for an event' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'payment-button','Pay with PayPal' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'apply','Apply' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'credit','Your credit (CHF)' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'to-pay','Amount to pay (CHF)' from pageversions;

insert into pageversions(SITEVERSION_ID,NAME) select max(ID),'book' from siteversions where language='fr';
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'title','Inscription à un événement' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'payment-button','Payer sur PayPal' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'apply','S\'inscrire' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'credit','Votre credit (CHF)' from pageversions;
insert into pageversions_strings(PAGEVERSION_ID,NAME,VALUE) select max(ID),'to-pay','Montant à payer (CHF)' from pageversions;
