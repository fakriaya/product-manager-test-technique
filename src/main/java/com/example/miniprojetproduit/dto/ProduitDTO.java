package com.example.miniprojetproduit.dto;

import com.example.miniprojetproduit.validation.ValidProduit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@ValidProduit
public class ProduitDTO {

    private Long id;

    @NotBlank(message = "Le nom est requis")
    private String nom;

    @NotNull(message = "Le prix est requis")
    private Double prix;

    @NotBlank(message = "La catégorie est requise")
    private String categorie;

    // Requis ssi catégorie => Informatique
    private String reference;

    // Requis ssi catégorie => Véhicule
    private String matricule;

    // Requis ssi catégorie => Alimentaire
    private LocalDate dateExpiration;

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

    @Override
    public String toString() {
        return "ProduitDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", categorie='" + categorie + '\'' +
                ", reference='" + reference + '\'' +
                ", matricule='" + matricule + '\'' +
                ", dateExpiration=" + dateExpiration +
                '}';
    }
}
