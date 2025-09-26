package com.example.miniprojetproduit.repository;

import com.example.miniprojetproduit.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Trouver produits alimentaires expirés
    @Query("SELECT p FROM Produit p WHERE p.categorie = 'Alimentaire' AND p.dateExpiration < :date")
    List<Produit> findExpiredAlimentaireProducts(@Param("date") LocalDate date);

    // Trouver produits alimentaires qui expirent bientot
    @Query("SELECT p FROM Produit p WHERE p.categorie = 'Alimentaire' AND p.dateExpiration BETWEEN :today AND :futureDate")
    List<Produit> findAlimentaireProductsExpiringSoon(@Param("today") LocalDate today,
                                                      @Param("futureDate") LocalDate futureDate);

    // Compter produits par categorie
    @Query("SELECT p.categorie, COUNT(p) FROM Produit p GROUP BY p.categorie")
    List<Object[]> countProductsByCategory();

}