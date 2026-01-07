-- =========================
-- USER & ROLE
-- =========================
--DROP TABLE IF EXISTS users;

CREATE TABLE users (
                      user_id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      email VARCHAR(100) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      phone VARCHAR(20),
                      role VARCHAR(20),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--DROP TABLE IF EXISTS role;

CREATE TABLE role (
                      role_id INT AUTO_INCREMENT PRIMARY KEY,
                      emri VARCHAR(50) NOT NULL,
                      description TEXT,
                      permissions TEXT,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role (
                           user_id INT,
                           role_id INT,
                           assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (user_id, role_id),
                           FOREIGN KEY (user_id) references users(user_id) ON DELETE CASCADE,
                           FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE
);

-- =========================
-- BIZNES
-- =========================
--DROP TABLE IF EXISTS biznes;
CREATE TABLE biznes (
                        biznes_id INT AUTO_INCREMENT PRIMARY KEY,
                        emri VARCHAR(100) NOT NULL,
                        pershkrim TEXT,
                        kategori VARCHAR(50),
                        nipt VARCHAR(40) NOT NULL,
                        license VARCHAR(50),
                        telefon VARCHAR(20),
                        email VARCHAR(100),
                        website VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--DROP TABLE IF EXISTS biznes_imazhe;
CREATE TABLE biznes_imazhe (
                               imazh_id INT AUTO_INCREMENT PRIMARY KEY,
                               biznes_id INT NOT NULL,
                               url VARCHAR(500) NOT NULL,
                               pershkrim VARCHAR(255),
                               is_primary BOOLEAN DEFAULT FALSE,
                               renditja INT DEFAULT 0,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (biznes_id) REFERENCES biznes(biznes_id) ON DELETE CASCADE
);

-- =========================
-- KATEGORI
-- =========================
--DROP TABLE IF EXISTS kategori;
CREATE TABLE kategori (
                          kategori_id INT AUTO_INCREMENT PRIMARY KEY,
                          emri VARCHAR(100) NOT NULL,
                          ikona VARCHAR(50),
                          pershkrim TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--DROP TABLE IF EXISTS biznes_kategori;
CREATE TABLE biznes_kategori (
                                 biznes_id INT,
                                 kategori_id INT,
                                 PRIMARY KEY (biznes_id, kategori_id),
                                 FOREIGN KEY (biznes_id) REFERENCES biznes(biznes_id) ON DELETE CASCADE,
                                 FOREIGN KEY (kategori_id) REFERENCES kategori(kategori_id) ON DELETE CASCADE
);

-- =========================
-- LOKACION
-- =========================
--DROP TABLE IF EXISTS lokacion;
CREATE TABLE lokacion (
                          lokacion_id INT AUTO_INCREMENT PRIMARY KEY,
                          qyteti VARCHAR(100) NOT NULL,
                          adresa VARCHAR(255),
                          rruga VARCHAR(100),
                          numri VARCHAR(20),
                          zip_code VARCHAR(10),
                          latitude DECIMAL(10,8),
                          longitude DECIMAL(11,8),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--DROP TABLE IF EXISTS biznes_lokacion;
CREATE TABLE biznes_lokacion (
                                 biznes_id INT,
                                 lokacion_id INT,
                                 PRIMARY KEY (biznes_id, lokacion_id),
                                 FOREIGN KEY (biznes_id) REFERENCES biznes(biznes_id) ON DELETE CASCADE,
                                 FOREIGN KEY (lokacion_id) REFERENCES lokacion(lokacion_id) ON DELETE CASCADE
);

-- =========================
-- INVENTARI
-- =========================
--DROP TABLE IF EXISTS inventari;
CREATE TABLE inventari (
                           inventar_id INT AUTO_INCREMENT PRIMARY KEY,
                           biznes_id INT NOT NULL,
                           emer_produkt VARCHAR(100) NOT NULL,
                           pershkrim TEXT,
                           sasi INT DEFAULT 0,
                           cmimi DECIMAL(10,2),
                           njesia VARCHAR(20),
                           kategoria VARCHAR(50),
                           is_active BOOLEAN DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (biznes_id) REFERENCES biznes(biznes_id) ON DELETE CASCADE
);

-- =========================
-- FAQ
-- =========================

--DROP TABLE IF EXISTS faq;
CREATE TABLE faq (
                     faq_id INT AUTO_INCREMENT PRIMARY KEY,
                     pyetje TEXT NOT NULL,
                     pergjigje TEXT NOT NULL,
                     renditja INT DEFAULT 0,
                     is_active BOOLEAN DEFAULT TRUE,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--DROP TABLE IF EXISTS faqja_kategori;
CREATE TABLE faqja_kategori (
                                kategori_id INT AUTO_INCREMENT PRIMARY KEY,
                                emri VARCHAR(100) NOT NULL,
                                pershkrim TEXT,
                                renditja INT DEFAULT 0,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--DROP TABLE IF EXISTS faq_kategori;
CREATE TABLE faq_kategori (
                              faq_id INT,
                              kategori_id INT,
                              PRIMARY KEY (faq_id, kategori_id),
                              FOREIGN KEY (faq_id) REFERENCES faq(faq_id) ON DELETE CASCADE,
                              FOREIGN KEY (kategori_id) REFERENCES faqja_kategori(kategori_id) ON DELETE CASCADE
);

-- =========================
-- REZERVIM & PAGESA
-- =========================
--DROP TABLE IF EXISTS rezervim;
CREATE TABLE rezervim (
                          rezervim_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT NOT NULL,
                          biznes_id INT NOT NULL,
                          inventar_id INT,
                          data TIMESTAMP NOT NULL,
                          numri_personave INT DEFAULT 1,
                          shenime TEXT,
                          status VARCHAR(20),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) references users(user_id),
                          FOREIGN KEY (biznes_id) REFERENCES biznes(biznes_id),
                          FOREIGN KEY (inventar_id) REFERENCES inventari(inventar_id)
);
--DROP TABLE IF EXISTS pagesat;
CREATE TABLE pagesat (
                         pagesa_id INT AUTO_INCREMENT PRIMARY KEY,
                         rezervim_id INT NOT NULL,
                         shuma DECIMAL(10,2) NOT NULL,
                         metoda VARCHAR(20),
                         status VARCHAR(20),
                         transaction_id VARCHAR(100),
                         payment_date TIMESTAMP,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (rezervim_id) REFERENCES rezervim(rezervim_id)
);
--DROP TABLE IF EXISTS pagesat_detaje;
CREATE TABLE pagesat_detaje (
                                detaje_id INT AUTO_INCREMENT PRIMARY KEY,
                                pagesa_id INT NOT NULL,
                                reference VARCHAR(100),
                                card_last_four VARCHAR(4),
                                card_type VARCHAR(20),
                                payment_gateway VARCHAR(50),
                                gateway_response TEXT,
                                ip_address VARCHAR(45),
                                user_agent TEXT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (pagesa_id) REFERENCES pagesat(pagesa_id) ON DELETE CASCADE
);
--DROP TABLE IF EXISTS pagesat_historik;
CREATE TABLE pagesat_historik (
                                  historik_id INT AUTO_INCREMENT PRIMARY KEY,
                                  pagesa_id INT NOT NULL,
                                  status VARCHAR(50),
                                  mesazh TEXT,
                                  data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (pagesa_id) REFERENCES pagesat(pagesa_id) ON DELETE CASCADE
);

-- =========================
-- PREFERENCA, NOTIFIKIME, KONTAKT
-- =========================
--DROP TABLE IF EXISTS preferenca;
CREATE TABLE preferenca (
                            preferenca_id INT AUTO_INCREMENT PRIMARY KEY,
                            user_id INT NOT NULL,
                            njoftime_aktive BOOLEAN DEFAULT TRUE,
                            gjuha VARCHAR(10),
                            tema VARCHAR(20),
                            email_notifications BOOLEAN DEFAULT TRUE,
                            sms_notifications BOOLEAN DEFAULT FALSE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) references users(user_id) ON DELETE CASCADE
);
--DROP TABLE IF EXISTS notifikime;
CREATE TABLE notifikime (
                            njoftim_id INT AUTO_INCREMENT PRIMARY KEY,
                            user_id INT NOT NULL,
                            titulli VARCHAR(100),
                            mesazh TEXT,
                            tipi VARCHAR(20),
                            lexuar BOOLEAN DEFAULT FALSE,
                            data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) references users(user_id)
);
--DROP TABLE IF EXISTS kontakt;
CREATE TABLE kontakt (
                         kontakt_id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id INT NOT NULL,
                         email VARCHAR(100),
                         subjekti VARCHAR(200),
                         mesazh TEXT,
                         status VARCHAR(20),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) references users(user_id)
);

-- =========================
-- VLERESIM
-- =========================
--DROP TABLE IF EXISTS vleresim;
CREATE TABLE vleresim (
                          vleresim_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT NOT NULL,
                          biznes_id INT NOT NULL,
                          rating INT,
                          koment TEXT,
                          is_approved BOOLEAN DEFAULT FALSE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (user_id) references users(user_id),
                          FOREIGN KEY (biznes_id) REFERENCES biznes(biznes_id)
);
