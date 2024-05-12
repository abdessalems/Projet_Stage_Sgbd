package com.example.stagesaadaoui.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Set;

import java.util.Date;

@Entity
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String denom;

    @NotNull
    @Positive
    private Integer ageMin;

    @NotNull
    @Positive
    private Integer ageMax;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDeb;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFin;
    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private Set<Inscription> inscriptions;

    // Constructor
    public Stage() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenom() {
        return denom;
    }

    public void setDenom(String denom) {
        this.denom = denom;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public Date getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(Date dateDeb) {
        this.dateDeb = dateDeb;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
}
