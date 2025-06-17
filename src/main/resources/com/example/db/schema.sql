-- Account table
CREATE TABLE account (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('user', 'admin')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admin information table
CREATE TABLE admin_info (
    account_id INTEGER PRIMARY KEY REFERENCES account(user_id) ON DELETE CASCADE,
    admin_id VARCHAR(10) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL
);

-- Application table
CREATE TABLE passport_application (
    user_id INT PRIMARY KEY,
    status VARCHAR(20) CHECK (status IN ('Pending', 'Accepted', 'Denied')) DEFAULT 'Pending',
    feedback TEXT,
    reference_id VARCHAR(50) UNIQUE,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Personal information table
CREATE TABLE user_info (
    user_id INT PRIMARY KEY,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    birth_place VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female')) NOT NULL,
    civil_status VARCHAR(20) CHECK (civil_status IN ('Single', 'Married', 'Widow', 'Legally Separated', 'Annulled')) NOT NULL,
    current_address TEXT NOT NULL,
    acquired_citizenship VARCHAR(20) CHECK (acquired_citizenship IN ('Birth', 'Election', 'Marriage', 'Naturalization', 'R.A. 9225')),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Contact information table
CREATE TABLE user_contact (
    contact_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    mobile_number VARCHAR(20),
    telephone_number VARCHAR(20),
    email_address VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Employment information table
CREATE TABLE user_work (
    work_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    occupation VARCHAR(100),
    work_address TEXT,
    work_telephone_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Foreign passport information table
CREATE TABLE user_foreign_passport (
    user_id INT PRIMARY KEY,
    has_foreign_passport BOOLEAN DEFAULT FALSE,
    issuing_country VARCHAR(50),
    foreign_passport_number VARCHAR(50) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Spouse information table
CREATE TABLE user_spouse (
    user_id INT PRIMARY KEY,
    spouse_full_name VARCHAR(100),
    spouse_citizenship VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Parents information table
CREATE TABLE user_parents (
    user_id INT PRIMARY KEY,
    father_full_name VARCHAR(100),
    father_citizenship VARCHAR(50),
    mother_maiden_name VARCHAR(100),
    mother_citizenship VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Philippine passport information table
CREATE TABLE user_philippine_passport (
    user_id INT PRIMARY KEY,
    has_philippine_passport BOOLEAN DEFAULT FALSE,
    philippine_passport_number VARCHAR(50) UNIQUE,
    issue_date DATE,
    expiry_date DATE,
    issue_place VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Minor applicant information table
CREATE TABLE user_minor_info (
    user_id INT PRIMARY KEY,
    is_minor BOOLEAN DEFAULT FALSE,
    companion_full_name VARCHAR(100),
    companion_relationship VARCHAR(50),
    companion_contact_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);

-- Images table for user uploads
CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    filename VARCHAR(255),
    file_type VARCHAR(20) CHECK (file_type IN ('Valid ID', 'Birth Certificate')) NOT NULL,
    supabase_url TEXT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES account(user_id) ON DELETE CASCADE
);