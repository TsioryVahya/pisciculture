package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "etat_nutrition_jour")
public class EtatNutritionJour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_poisson")
    private Poisson poisson;

    @Column(name = "date_jour")
    private LocalDate dateJour;

    @Column(name = "prot_stock")
    private BigDecimal protStock = BigDecimal.ZERO;

    @Column(name = "gluc_stock")
    private BigDecimal glucStock = BigDecimal.ZERO;

    @Column(name = "cycles_complets")
    private Integer cyclesComplets = 0;

    @Column(name = "demi_cycle_applique")
    private Boolean demiCycleApplique = false;

    @Column(name = "poids")
    private BigDecimal poids;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Poisson getPoisson() { return poisson; }
    public void setPoisson(Poisson poisson) { this.poisson = poisson; }

    public LocalDate getDateJour() { return dateJour; }
    public void setDateJour(LocalDate dateJour) { this.dateJour = dateJour; }

    public BigDecimal getProtStock() { return protStock; }
    public void setProtStock(BigDecimal protStock) { this.protStock = protStock; }

    public BigDecimal getGlucStock() { return glucStock; }
    public void setGlucStock(BigDecimal glucStock) { this.glucStock = glucStock; }

    public Integer getCyclesComplets() { return cyclesComplets; }
    public void setCyclesComplets(Integer cyclesComplets) { this.cyclesComplets = cyclesComplets; }

    public Boolean getDemiCycleApplique() { return demiCycleApplique; }
    public void setDemiCycleApplique(Boolean demiCycleApplique) { this.demiCycleApplique = demiCycleApplique; }

    public BigDecimal getPoids() { return poids; }
    public void setPoids(BigDecimal poids) { this.poids = poids; }
}
