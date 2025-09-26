package com.example.miniprojetproduit.form;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class ProduitForm {
    private Long id;
    private String nom;
    private Double prix;
    private String categorie;
    private String reference;
    private String matricule;
    private LocalDate dateExpiration;
    private MultipartFile fichier;

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

    public MultipartFile getFichier() { return fichier; }
    public void setFichier(MultipartFile fichier) { this.fichier = fichier; }
}
