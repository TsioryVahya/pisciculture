package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nourriture")
public class Nourriture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(name = "prix_achat_par_kg")
    private BigDecimal prixAchatParKg;

    @Column(name = "pourcentage_apport_proteine")
    private BigDecimal pourcentageApportProteine;

    @Column(name = "pourcentage_apport_glucide")
    private BigDecimal pourcentageApportGlucide;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPrixAchatParKg() { return prixAchatParKg; }
    public void setPrixAchatParKg(BigDecimal prixAchatParKg) { this.prixAchatParKg = prixAchatParKg; }

    public BigDecimal getPourcentageApportProteine() { return pourcentageApportProteine; }
    public void setPourcentageApportProteine(BigDecimal pourcentageApportProteine) { this.pourcentageApportProteine = pourcentageApportProteine; }

    public BigDecimal getPourcentageApportGlucide() { return pourcentageApportGlucide; }
    public void setPourcentageApportGlucide(BigDecimal pourcentageApportGlucide) { this.pourcentageApportGlucide = pourcentageApportGlucide; }
}
