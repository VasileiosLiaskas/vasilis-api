CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS business (
    id INT NOT NULL AUTO_INCREMENT,
    date DATETIME NULL,
    date_to DATETIME NULL,
    type VARCHAR(255) NULL,
    who VARCHAR(255) NULL,
    area VARCHAR(255) NULL,
    details VARCHAR(255) NULL,
    costs DOUBLE NULL,
    fee DOUBLE NULL,
    advance_payment DOUBLE NULL,
    remaining_money DOUBLE NULL,
    payout BIT NULL,
    files_completed BIT NULL,
    files_delivered BIT NULL,
    comments VARCHAR(255) NULL,
    google_calendar_id VARCHAR(255) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS invoice (
    id INT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NULL,
    invoice_number VARCHAR(255) NULL,
    description VARCHAR(255) NULL,
    date DATETIME NULL,
    invoice_date DATETIME NULL,
    enum VARCHAR(255) NULL,
    business_id INT NULL,
    file_data LONGBLOB NULL,
    file_type VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_invoice_business
        FOREIGN KEY (business_id)
        REFERENCES business (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comment (
    id INT NOT NULL AUTO_INCREMENT,
    text TEXT NOT NULL,
    author_id INT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comment_author
        FOREIGN KEY (author_id)
        REFERENCES users (id)
) ENGINE=InnoDB;


