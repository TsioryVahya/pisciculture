package com.pisciculture.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "poisson_nutriment_stock")
public class PoissonNutrimentStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_poisson")
    private Poisson poisson;

    @ManyToOne
    @JoinColumn(name = "id_nutriment")
    private Nutriment nutriment;

    @Column(name = "date_jour")
    private LocalDate dateJour;

    @Column(name = "stock", precision = 15, scale = 6)
    private BigDecimal stock = BigDecimal.ZERO;

    public PoissonNutrimentStock() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Poisson getPoisson() { return poisson; }
    public void setPoisson(Poisson poisson) { this.poisson = poisson; }

    public Nutriment getNutriment() { return nutriment; }
    public void setNutriment(Nutriment nutriment) { this.nutriment = nutriment; }

    public LocalDate getDateJour() { return dateJour; }
    public void setDateJour(LocalDate dateJour) { this.dateJour = dateJour; }

    public BigDecimal getStock() { return stock; }
    public void setStock(BigDecimal stock) { this.stock = stock; }
}
