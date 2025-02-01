create table if not exists expense
(
    id                  uuid         not null
        primary key,
    name                varchar(150) not null,
    amount              double precision,
    currency            varchar(3) not null,
    type                varchar(50) not null,
    payment_period_type varchar(50) not null,
    payment_day         integer,
    payment_date        timestamp,
    expiry_date         timestamp,
    created_at          timestamp default CURRENT_TIMESTAMP not null
);


CREATE TABLE if not exists expense_detail (
                                id UUID not null
                                    PRIMARY KEY DEFAULT gen_random_uuid() ,
                                expense_id          UUID not null ,
                                payment_period_type varchar(50) not null ,
                                name                varchar(150) not null ,
                                amount              DOUBLE PRECISION not null ,
                                currency            varchar(3) not null ,
                                plan_payment_date   TIMESTAMP not null ,
                                fact_payment_date   TIMESTAMP not null ,
                                paid boolean not null ,
                                FOREIGN KEY (expense_id) REFERENCES expense(id)
);

create table if not exists expense_detail_attachment
(
    id                     uuid         not null
        constraint expense_detail_attachment_pk
            primary key,
    expense_detail_id                uuid         not null
        constraint twin_attachment_twin_id_fk
            references expense_detail
            on update cascade on delete cascade,
    storage_link           varchar(255) not null,
    created_by_user_id     uuid,
    created_at             timestamp default CURRENT_TIMESTAMP,
    title                  varchar,
    description            varchar
);

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       currency VARCHAR(10) NOT NULL
);






