CREATE TABLE expenseEntity (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255),
                         type VARCHAR(50),
                         currency VARCHAR(3),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expense_detail (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                expense_id UUID,
                                amount DOUBLE PRECISION,
                                payment_date TIMESTAMP,
                                FOREIGN KEY (expense_id) REFERENCES expenseEntity(id)
);

alter table expense_detail
    add paid boolean not null default false;

CREATE TABLE users (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                currency VARCHAR(10) NOT NULL
);

alter table expense_detail
    add currency VARCHAR(50);

create table expense_detail_attachment
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






