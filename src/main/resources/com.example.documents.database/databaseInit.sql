create table if not exists payment_request
(
    id              serial      primary key,
    number          varchar     not null,
    date            date        not null,
    "user"          varchar     not null,
    counterpart     varchar     not null,
    amount          decimal     not null,
    currency        varchar     not null,
    currencyrate    decimal     not null,
    commission      decimal     not null
);

create table if not exists payment
(
    id          serial     primary key,
    number      varchar    not null,
    date        date       not null,
    "user"      varchar    not null,
    amount      decimal    not null,
    employee    varchar    not null
);

create table if not exists waybill
(
    id              serial              primary key,
    number          varchar             not null,
    date            date                not null,
    "user"          varchar             not null,
    amount          decimal             not null,
    currency        varchar             not null,
    currencyrate    decimal             not null,
    item            varchar             not null,
    quantity        double precision    not null
);
