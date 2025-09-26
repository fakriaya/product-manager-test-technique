package com.example.miniprojetproduit.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Double prix;
    private String categorie;

    // Requis ssi catégorie => Informatique
    private String reference;

    // Requis ssi catégorie => Véhicule
    private String matricule;

    // Requis ssi catégorie => Alimentaire
    private LocalDate dateExpiration;

    private String fichier;

    public Produit() {}

    public Produit(String nom, Double prix, String categorie) {
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public String getFichier() { return fichier; }
    public void setFichier(String fichier) { this.fichier = fichier; }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", categorie='" + categorie + '\'' +
                ", reference='" + reference + '\'' +
                ", matricule='" + matricule + '\'' +
                ", dateExpiration=" + dateExpiration +
                ", fichier='" + fichier + '\'' +
                '}';
    }
}