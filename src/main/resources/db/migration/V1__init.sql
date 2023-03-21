CREATE TABLE public.user (
    id uuid,
    email varchar(255) NOT NULL,
    password char(60) NOT NULL,
    active bool NOT NULL,
    banned bool NOT NULL,
    verify_token varchar DEFAULT NULL,
    registration_timestamp timestamp NOT NULL,
    restore_token varchar DEFAULT NULL,
    restore_timestamp timestamp DEFAULT NULL,
    UNIQUE (email),
    PRIMARY KEY (id)
);

CREATE TABLE public.version (
    id int GENERATED ALWAYS AS IDENTITY,
    type varchar(255) NOT NULL,
    protocol int NOT NULL,
    name varchar(255) NOT NULL,
    UNIQUE (name),
    PRIMARY KEY (id)
);

CREATE TABLE public.server (
    id uuid,
    user_id uuid NOT NULL,
    host varchar(255) NOT NULL,
    ip varchar(18) NOT NULL,
    port int NOT NULL,
    name varchar(64) NOT NULL,
    online bool NOT NULL,
    online_players int NOT NULL,
    max_players int NOT NULL,
    creation_timestamp timestamp NOT NULL,
    favicon varchar DEFAULT NULL,
    version_id int DEFAULT NULL,
    FOREIGN KEY (version_id) REFERENCES public.version(id),
    FOREIGN KEY (user_id) REFERENCES public.user(id),
    PRIMARY KEY (id)
);

INSERT INTO public.version (name, type, protocol) VALUES
    ('1.7.10', 'java', 5),
    ('1.8.8', 'java', 47),
    ('1.9', 'java', 107),
    ('1.9.1', 'java', 108),
    ('1.9.4', 'java', 110),
    ('1.10.2', 'java', 210),
    ('1.11', 'java', 315),
    ('1.11.2', 'java', 316),
    ('1.12', 'java', 335),
    ('1.12.1', 'java', 338),
    ('1.12.2', 'java', 340),
    ('1.13', 'java', 393),
    ('1.13.1', 'java', 401),
    ('1.13.2', 'java', 404),
    ('1.14', 'java', 477),
    ('1.14.1', 'java', 480),
    ('1.14.2', 'java', 485),
    ('1.14.3', 'java', 490),
    ('1.14.4', 'java', 498),
    ('1.15', 'java', 573),
    ('1.15.1', 'java', 575),
    ('1.15.2', 'java', 578),
    ('1.16', 'java', 735),
    ('1.16.1', 'java', 736),
    ('1.16.2', 'java', 751),
    ('1.16.3', 'java', 753),
    ('1.16.5', 'java', 754),
    ('1.17', 'java', 755),
    ('1.17.1', 'java', 756),
    ('1.18', 'java', 757),
    ('1.18.2', 'java', 758),
    ('1.19', 'java', 759),
    ('1.19.2', 'java', 760),
    ('1.19.3', 'java', 761);