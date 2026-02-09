-- Script de création de la base de données Pisciculture (PostgreSQL)
-- Table d'état nutritionnel journalier par poisson

DROP TABLE IF EXISTS historique_poids CASCADE;
DROP TABLE IF EXISTS poisson_nutriment_stock CASCADE;
DROP TABLE IF EXISTS nutriments_nourritures CASCADE;
DROP TABLE IF EXISTS nutriments_races CASCADE;
DROP TABLE IF EXISTS nutriments CASCADE;
DROP TABLE IF EXISTS etat_nutrition_jour CASCADE;

DROP TABLE IF EXISTS alimentation_detaille CASCADE;
DROP TABLE IF EXISTS alimentation CASCADE;
DROP TABLE IF EXISTS poissons_statut CASCADE;
DROP TABLE IF EXISTS statut CASCADE;
DROP TABLE IF EXISTS etang_poisson CASCADE;
DROP TABLE IF EXISTS poissons CASCADE;
DROP TABLE IF EXISTS etang CASCADE;
DROP TABLE IF EXISTS nourriture CASCADE;
DROP TABLE IF EXISTS tarifPoisson CASCADE;
DROP TABLE IF EXISTS typePrix CASCADE;
DROP TABLE IF EXISTS race CASCADE;


-- Table race
CREATE TABLE race (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix_achat_par_kg DECIMAL(15, 2),
    prix_vente_par_kg DECIMAL(15, 2),
    poids_max DECIMAL(10, 6),
    capacite_augmentation_poids DECIMAL(10, 2) -- en grammes par jour
);

-- Table nutriments
CREATE TABLE nutriments (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

-- Table nutriments_races
CREATE TABLE nutriments_races (
    id BIGSERIAL PRIMARY KEY,
    idrace BIGINT REFERENCES race(id),
    idnutriment BIGINT REFERENCES nutriments(id),
    besoin_nutriment DECIMAL(10, 2) NOT NULL
);


-- Table typePrix (achat, vente)
CREATE TABLE typePrix (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL -- 'achat', 'vente'
);

-- Table tarifPoisson
CREATE TABLE tarifPoisson (
    id BIGSERIAL PRIMARY KEY,
    idRace BIGINT REFERENCES race(id),
    idTypeprix BIGINT REFERENCES typePrix(id),
    montant DECIMAL(15, 2),
    date DATE DEFAULT CURRENT_DATE
);

-- Table nourriture
CREATE TABLE nourriture (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix_achat_par_kg DECIMAL(15, 2)
);

-- Table nutriments_nourritures
CREATE TABLE nutriments_nourritures (
    id BIGSERIAL PRIMARY KEY,
    idnourriture BIGINT REFERENCES nourriture(id),
    idnutriment BIGINT REFERENCES nutriments(id),
    pourcentage_apport_nutriment DECIMAL(10, 2) NOT NULL
);

-- Table etang
CREATE TABLE etang (
    id BIGSERIAL PRIMARY KEY,
    longueur DECIMAL(10, 2),
    largeur DECIMAL(10, 2),
    capacite INTEGER NULL -- nombre max de poissons ou volume?
);

-- Table poissons
CREATE TABLE poissons (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100),
    poids_initial DECIMAL(10, 6),
    idRace BIGINT REFERENCES race(id)
);

-- Table etang_poisson (mouvement ou affectation)
CREATE TABLE etang_poisson (
    id BIGSERIAL PRIMARY KEY,
    id_poisson BIGINT REFERENCES poissons(id),
    id_etang BIGINT REFERENCES etang(id),
    date DATE DEFAULT CURRENT_DATE
);

-- Table statut
CREATE TABLE statut (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

-- Table poissons_statut
CREATE TABLE poissons_statut (
    id BIGSERIAL PRIMARY KEY,
    idpoisson BIGINT REFERENCES poissons(id),
    idstatut BIGINT REFERENCES statut(id),
    date_changement TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table alimentation
CREATE TABLE alimentation (
    id BIGSERIAL PRIMARY KEY,
    idetang BIGINT REFERENCES etang(id),
    dateHeure TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table alimentation_detaille
CREATE TABLE alimentation_detaille (
    id BIGSERIAL PRIMARY KEY,
    idalimentation BIGINT REFERENCES alimentation(id),
    idnourriture BIGINT REFERENCES nourriture(id),
    quantite_kg DECIMAL(10, 6),
    prixKiloNourriture DECIMAL(15, 2)
);



-- Table etat_nutrition_jour (stock prot/gluc et cycles par jour et par poisson)
CREATE TABLE etat_nutrition_jour (
    id BIGSERIAL PRIMARY KEY,
    id_poisson BIGINT REFERENCES poissons(id),
    date_jour DATE NOT NULL,
    cycles_complets INTEGER DEFAULT 0,
    demi_cycles INTEGER DEFAULT 0,
    poids DECIMAL(10, 6),
    cout_alimentation_cumule DECIMAL(15, 2) DEFAULT 0
);



-- Insertion de données de base
INSERT INTO typePrix (nom) VALUES ('achat'), ('vente');

-- Insertion des statuts initiaux pour les poissons
INSERT INTO statut (nom) VALUES ('Vivant'), ('Vendu'), ('Mort');

-- Insertion de 4 races de poissons avec leurs caractéristiques
INSERT INTO race (nom, prix_achat_par_kg, prix_vente_par_kg, poids_max, capacite_augmentation_poids) VALUES 
('Carpe', 1000, 10000, 4.00, 10.0),
('Tilapia', 1000, 9600, 2.5, 15.0),
('Truite', 1000, 7600, 9.0, 20.0),
('Silure', 1000, 8700, 5.00, 25.0);

-- Insertion de tarifs initiaux dans tarifPoisson
-- idTypeprix 1 = achat, 2 = ventea
INSERT INTO tarifPoisson (idRace, idTypeprix, montant, date) VALUES 
(1, 1, 1000, CURRENT_DATE), (1, 2, 10000, CURRENT_DATE),
(2, 1, 1000, CURRENT_DATE), (2, 2, 9600, CURRENT_DATE),
(3, 1, 1000, CURRENT_DATE), (3, 2, 7600, CURRENT_DATE),
(4, 1, 1000, CURRENT_DATE), (4, 2, 8700, CURRENT_DATE);

-- Insertion de 3 étangs avec longueur, largeur et capacité
INSERT INTO etang (longueur, largeur) VALUES (10.0, 5.0), (10.0, 10.0), (15.0, 10.0);

-- Insertion de nutriments de base
INSERT INTO nutriments (nom) VALUES ('Protéines'), ('Glucides'), ('Lipides'), ('Vitamines');

-- Insertion de besoins nutritionnels pour les races
-- Exemple pour la Carpe (id 1)
INSERT INTO nutriments_races (idrace, idnutriment, besoin_nutriment) VALUES 
(1, 1, 2.0), -- Protéines
(1, 2, 4.0), -- Glucides
(1, 3, 1.0); -- Lipides

-- Exemple pour le Tilapia (id 2)
INSERT INTO nutriments_races (idrace, idnutriment, besoin_nutriment) VALUES 
(2, 1, 3.0), -- Protéines
(2, 2, 5.0), -- Glucides
(2, 3, 1.5); -- Lipides

INSERT INTO nourriture (nom, prix_achat_par_kg) VALUES 
('Granulés Croissance', 500),
('Farine de Poisson', 600),
('Mélange Végétal', 700),
('Complément Vitaminé', 1000);

-- Insertion d'apports nutritionnels pour les nourritures
-- Exemple pour Granulés Croissance (id 1)
INSERT INTO nutriments_nourritures (idnourriture, idnutriment, pourcentage_apport_nutriment) VALUES 
(1, 1, 45.0), -- Protéines
(1, 2, 20.0), -- Glucides
(1, 3, 5.0);  -- Lipides

-- Exemple pour Farine de Poisson (id 2)
INSERT INTO nutriments_nourritures (idnourriture, idnutriment, pourcentage_apport_nutriment) VALUES 
(2, 1, 65.0), -- Protéines
(2, 2, 5.0),  -- Glucides
(2, 3, 10.0); -- Lipides

-- Insertion de données de test pour les poissons
INSERT INTO poissons (nom, poids_initial, idRace) VALUES 
('Poisson 1', 0.200, 2),    -- Tilapia
('Poisson 2', 0.150, 2),
('Poisson 3', 0.250, 2), -- Tilapia
('Poisson 4', 0.100, 2); -- Tilapia
 -- Tilapia


-- Insertion des statuts initiaux pour les poissons (idstatut 1 = Vivant)
INSERT INTO poissons_statut (idpoisson, idstatut) VALUES 
(1, 1),
(2, 1),
(3, 1),
(4, 1);

-- Affectation des poissons aux étangs (id_etang 1)
INSERT INTO etang_poisson (id_poisson, id_etang) VALUES 
(1, 1),
(2, 1),
(3, 2),
(4, 2);

-- Table pour stocker l'historique de l'évolution du poids des poissons
CREATE TABLE historique_poids (
    id BIGSERIAL PRIMARY KEY,
    id_poisson BIGINT REFERENCES poissons(id),
    date_mesure DATE NOT NULL,
    poids DECIMAL(10, 6) NOT NULL
);

-- Table pour stocker les stocks de nutriments par poisson et par jour
CREATE TABLE poisson_nutriment_stock (
    id BIGSERIAL PRIMARY KEY,
    id_poisson BIGINT REFERENCES poissons(id),
    id_nutriment BIGINT REFERENCES nutriments(id),
    date_jour DATE NOT NULL,
    stock DECIMAL(15, 6) DEFAULT 0
);
