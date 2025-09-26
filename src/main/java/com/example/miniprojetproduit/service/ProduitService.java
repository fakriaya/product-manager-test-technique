package com.example.miniprojetproduit.service;

import com.example.miniprojetproduit.dto.ProduitDTO;
import com.example.miniprojetproduit.entity.Produit;
import com.example.miniprojetproduit.form.ProduitForm;
import com.example.miniprojetproduit.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;

    public ProduitService(ProduitRepository produitRepository, FileStorageService fileStorageService, EmailService emailService) {
        this.produitRepository = produitRepository;
        this.fileStorageService = fileStorageService;
        this.emailService = emailService;
    }

    // Méthode pour JSON (sans fichier)
    public Produit createFromDto(ProduitDTO dto) {
        // Validation simple ajoutée
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire");
        }
        if (dto.getPrix() == null || dto.getPrix() <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }

        Produit p = new Produit();
        p.setNom(dto.getNom());
        p.setPrix(dto.getPrix());
        p.setCategorie(dto.getCategorie());
        p.setReference(dto.getReference());
        p.setMatricule(dto.getMatricule());
        p.setDateExpiration(dto.getDateExpiration());

        Produit savedProduit = produitRepository.save(p);

        // Envoi email de notification
        emailService.sendProductCreatedNotification(savedProduit);

        return savedProduit;
    }

    // Méthode pour multipart (avec fichier)
    public Produit createFromForm(ProduitForm form) throws IOException {
        // Validation du fichier ajoutée
        if (form.getFichier() != null && !form.getFichier().isEmpty()) {
            if (form.getFichier().getSize() > 10_000_000) {
                throw new IllegalArgumentException("Fichier trop volumineux (maximum 10MB)");
            }
        }

        Produit p = new Produit();
        p.setNom(form.getNom());
        p.setPrix(form.getPrix());
        p.setCategorie(form.getCategorie());
        p.setReference(form.getReference());
        p.setMatricule(form.getMatricule());
        p.setDateExpiration(form.getDateExpiration());

        if (form.getFichier() != null && !form.getFichier().isEmpty()) {
            String stored = fileStorageService.storeFile(form.getFichier());
            p.setFichier(stored);
        }

        Produit savedProduit = produitRepository.save(p);

        // Envoi email de notification
        emailService.sendProductCreatedNotification(savedProduit);

        return savedProduit;
    }

    @Transactional
    public Optional<Produit> updateFromForm(Long id, ProduitForm form) throws IOException {
        return produitRepository.findById(id).map(p -> {
            p.setNom(form.getNom());
            p.setPrix(form.getPrix());
            p.setCategorie(form.getCategorie());
            p.setReference(form.getReference());
            p.setMatricule(form.getMatricule());
            p.setDateExpiration(form.getDateExpiration());

            try {
                if (form.getFichier() != null && !form.getFichier().isEmpty()) {
                    // Validation du fichier ajoutée
                    if (form.getFichier().getSize() > 10_000_000) {
                        throw new IllegalArgumentException("Fichier trop volumineux (maximum 10MB)");
                    }
                    String stored = fileStorageService.storeFile(form.getFichier());
                    p.setFichier(stored);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Produit savedProduit = produitRepository.save(p);

            // Envoi email de modification
            emailService.sendProductUpdatedNotification(savedProduit);

            return savedProduit;
        });
    }
}