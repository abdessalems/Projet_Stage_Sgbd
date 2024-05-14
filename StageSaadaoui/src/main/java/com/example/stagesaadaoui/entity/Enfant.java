package com.example.stagesaadaoui.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import java.util.Set;

@Entity
public class Enfant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Le nom doit contenir uniquement des lettres")
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Le prénom doit contenir uniquement des lettres")
    private String prenom;

    @Enumerated(EnumType.STRING)
    private Gender sexe;

    @NotNull(message = "La date de naissance est requise")
    private LocalDate dateNaiss;

    @OneToMany(mappedBy = "enfant")
    private Set<Inscription> inscriptions;

    // Constructor
    public Enfant() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Gender getSexe() {
        return sexe;
    }

    public void setSexe(Gender sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
}