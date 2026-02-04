-- Script de création de la base de données Pisciculture (PostgreSQL)

-- Suppression des tables si elles existent pour réinitialisation
DROP TABLE IF EXISTS evolution_historique CASCADE;
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
    poids_max DECIMAL(10, 2),
    capacite_augmentation_poids DECIMAL(10, 2), -- en grammes par jour
    besoin_proteine DECIMAL(10, 2), -- en grammes
    besoin_glucide DECIMAL(10, 2) -- en grammes
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
    prix_achat_par_kg DECIMAL(15, 2),
    pourcentage_apport_proteine DECIMAL(5, 2),
    pourcentage_apport_glucide DECIMAL(5, 2)
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
    poids_initial DECIMAL(10, 2),
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
    quantite_kg DECIMAL(10, 2)
);

-- Table evolution_historique
CREATE TABLE evolution_historique (
    id BIGSERIAL PRIMARY KEY,
    idpoisson BIGINT REFERENCES poissons(id),
    poids DECIMAL(10, 2),
    idalimentation BIGINT REFERENCES alimentation(id),
    proteineObtenue DECIMAL(10, 2),
    glucideObtenue DECIMAL(10, 2),
    dateHeure TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion de données de base
INSERT INTO typePrix (nom) VALUES ('achat'), ('vente');

-- Insertion des statuts initiaux pour les poissons
INSERT INTO statut (nom) VALUES ('Vivant'), ('Vendu'), ('Mort');
