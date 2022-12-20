DROP TABLE IF EXISTS users, items, bookings, requests, comments;

CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name     VARCHAR(40) NOT NULL,
    email    VARCHAR(60) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name     VARCHAR(40) NOT NULL,
    description    VARCHAR(200) NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT,
    request_id BIGINT,
    CONSTRAINT items_pk PRIMARY KEY (item_id),
    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL ,
    booker_id BIGINT NOT NULL,
    approved VARCHAR(10) NOT NULL,
    CONSTRAINT booking_pk PRIMARY KEY (booking_id),
    CONSTRAINT fk_booker_id FOREIGN KEY (booker_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    description    VARCHAR(200) NOT NULL,
    requestor_id BIGINT NOT NULL,
    CONSTRAINT request_pk PRIMARY KEY (request_id),
    CONSTRAINT fk_requestor_id FOREIGN KEY (requestor_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    text VARCHAR(200) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY (comment_id),
    CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES items (item_id),
    CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users (user_id)
);


