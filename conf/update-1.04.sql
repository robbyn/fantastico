charset utf8

create table payments (
    ID varchar(24) not null,
    PARENT_ID varchar(24),
    USER_ID integer,
    EVENT_ID integer,
    DATETIME datetime not null,
    AMOUNT decimal(15,2) not null,
    DETAILS longtext,
    primary key (ID)
);

alter table payments
    add index FK526A0F2DFEC4B6B (EVENT_ID),
    add constraint FK526A0F2DFEC4B6B
    foreign key (EVENT_ID)
    references events (ID);

alter table payments
    add index FK526A0F2DD6347FC9 (USER_ID),
    add constraint FK526A0F2DD6347FC9
    foreign key (USER_ID)
    references users (ID);

alter table payments
    add index FK526A0F2D29EEC287 (PARENT_ID),
    add constraint FK526A0F2D29EEC287
    foreign key (PARENT_ID)
    references payments (ID);
