package com.pisciculture.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alimentation")
public class Alimentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idetang")
    private Etang etang;

    @Column(name = "dateheure")
    private LocalDateTime dateHeure;

    @OneToMany(mappedBy = "alimentation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlimentationDetaille> details = new ArrayList<>();

    public Alimentation() {
        this.dateHeure = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Etang getEtang() { return etang; }
    public void setEtang(Etang etang) { this.etang = etang; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public List<AlimentationDetaille> getDetails() { return details; }
    public void setDetails(List<AlimentationDetaille> details) { this.details = details; }
}
