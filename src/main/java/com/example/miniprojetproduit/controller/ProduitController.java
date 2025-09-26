package com.example.miniprojetproduit.controller;

import com.example.miniprojetproduit.dto.ProduitDTO;
import com.example.miniprojetproduit.entity.Produit;
import com.example.miniprojetproduit.form.ProduitForm;
import com.example.miniprojetproduit.repository.ProduitRepository;
import com.example.miniprojetproduit.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:4200")
public class ProduitController {

    private final ProduitRepository produitRepository;
    private final ProduitService produitService;

    public ProduitController(ProduitRepository produitRepository, ProduitService produitService) {
        this.produitRepository = produitRepository;
        this.produitService = produitService;
    }

    @GetMapping
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Création du produit pour JSON (sans fichier)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produit> createProduitJson(@Valid @RequestBody ProduitDTO produitDTO) {
        Produit produit = produitService.createFromDto(produitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produit);
    }

    // Création du produit pour multipart (avec fichier)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduitMultipart(@ModelAttribute @Valid ProduitForm form) throws IOException {
        Produit created = produitService.createFromForm(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Update du produit
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduit(@PathVariable Long id, @ModelAttribute @Valid ProduitForm form) throws IOException {
        Optional<Produit> updated = produitService.updateFromForm(id, form);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Produit non trouvé"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable Long id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Produit supprimé avec succès"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Produit non trouvé"));
    }
}