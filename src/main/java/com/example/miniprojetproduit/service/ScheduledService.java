package com.example.miniprojetproduit.service;

import com.example.miniprojetproduit.entity.Produit;
import com.example.miniprojetproduit.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduledService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private EmailService emailService;


     // Suppression automatique des produits alimentaires expirés
     // Exécuté tous les jours à 2h du matin

    @Scheduled(cron = "0 0 9 * * ?")
    public void archiveExpiredProducts() {
        System.out.println("=== DÉBUT ARCHIVAGE PRODUITS EXPIRÉS - " + LocalDate.now() + " ===");

        try {
            List<Produit> expiredProducts = produitRepository.findExpiredAlimentaireProducts(LocalDate.now());

            if (!expiredProducts.isEmpty()) {
                System.out.println("Produits expirés trouvés : " + expiredProducts.size());

                // Supprimer les produits expirés
                for (Produit product : expiredProducts) {
                    System.out.println("Suppression produit expiré : " + product.getNom() + " (expiré le " + product.getDateExpiration() + ")");
                    produitRepository.deleteById(product.getId());
                }

                // Envoyer email de notification
                sendExpiredProductsEmail(expiredProducts);

                System.out.println("Archivage terminé avec succès : " + expiredProducts.size() + " produit(s) supprimé(s)");
            } else {
                System.out.println("Aucun produit expiré à archiver");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'archivage : " + e.getMessage());
            emailService.sendErrorNotification("Archivage produits expirés", e.getMessage());
        }

        System.out.println("=== FIN ARCHIVAGE PRODUITS EXPIRÉS ===");
    }


      // Envoi du récapitulatif quotidien par email
      // Exécuté tous les jours à 8h du matin

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailySummary() {
        System.out.println("=== DÉBUT RÉCAPITULATIF QUOTIDIEN - " + LocalDate.now() + " ===");

        try {
            // Statistiques des produits
            List<Object[]> categoryStats = produitRepository.countProductsByCategory();
            long totalProducts = produitRepository.count();

            // Produits expirant bientôt (dans les 7 jours)
            List<Produit> expiringSoon = produitRepository.findAlimentaireProductsExpiringSoon(
                    LocalDate.now(),
                    LocalDate.now().plusDays(7)
            );

            // Envoyer le récapitulatif
            sendDailySummaryEmail(categoryStats, totalProducts, expiringSoon);

            System.out.println("Récapitulatif quotidien envoyé - Total produits : " + totalProducts + ", Expirent bientôt : " + expiringSoon.size());

        } catch (Exception e) {
            System.err.println("Erreur lors du récapitulatif quotidien : " + e.getMessage());
            emailService.sendErrorNotification("Récapitulatif quotidien", e.getMessage());
        }

        System.out.println("=== FIN RÉCAPITULATIF QUOTIDIEN ===");
    }


     // Alerte pour les produits qui expirent dans les 3 jours
     // Exécuté tous les jours à 9h du matin

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendExpirationWarning() {
        System.out.println("=== DÉBUT ALERTE EXPIRATION - " + LocalDate.now() + " ===");

        try {
            List<Produit> productsSoonToExpire = produitRepository.findAlimentaireProductsExpiringSoon(
                    LocalDate.now().minusDays(1),  // c'est pour hier
                    LocalDate.now().plusDays(7)    // c'est dans 7 jours
            );

            if (!productsSoonToExpire.isEmpty()) {
                System.out.println("Produits expirant dans 3 jours : " + productsSoonToExpire.size());
                sendExpirationWarningEmail(productsSoonToExpire);
                System.out.println("Alerte expiration envoyée avec succès");
            } else {
                System.out.println("Aucun produit n'expire dans les 3 jours");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'alerte expiration : " + e.getMessage());
            emailService.sendErrorNotification("Alerte expiration produits", e.getMessage());
        }

        System.out.println("=== FIN ALERTE EXPIRATION ===");
    }


     // Test manuel - exécuté toutes les minutes pour vérifier que le scheduler fonctionne

    @Scheduled(fixedRate = 60000) // Toutes les 60 secondes
    public void testScheduler() {
        System.out.println("SCHEDULER TEST - " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - Scheduler actif !");
    }

    // Méthodes privées pour l'envoi d'emails

    private void sendExpiredProductsEmail(List<Produit> expiredProducts) {
        StringBuilder content = new StringBuilder();
        content.append("PRODUITS EXPIRÉS SUPPRIMÉS AUTOMATIQUEMENT\n\n");
        content.append("Date : ").append(LocalDate.now()).append("\n");
        content.append("Nombre de produits supprimés : ").append(expiredProducts.size()).append("\n\n");
        content.append("Liste des produits :\n");

        for (Produit product : expiredProducts) {
            content.append("- ").append(product.getNom())
                    .append(" (").append(product.getPrix()).append("€)")
                    .append(" - Expiré le : ").append(product.getDateExpiration())
                    .append("\n");
        }

        emailService.sendErrorNotification("Produits expirés supprimés", content.toString());
    }

    private void sendDailySummaryEmail(List<Object[]> categoryStats, long totalProducts, List<Produit> expiringSoon) {
        StringBuilder content = new StringBuilder();
        content.append("RÉCAPITULATIF QUOTIDIEN - ").append(LocalDate.now()).append("\n\n");
        content.append("STATISTIQUES :\n");
        content.append("- Total produits : ").append(totalProducts).append("\n");

        content.append("\nRépartition par catégorie :\n");
        for (Object[] stat : categoryStats) {
            String category = (String) stat[0];
            Long count = (Long) stat[1];
            content.append("- ").append(category).append(" : ").append(count).append(" produit(s)\n");
        }

        content.append("\nPRODUITS EXPIRANT BIENTÔT (7 jours) : ").append(expiringSoon.size()).append("\n");
        for (Produit product : expiringSoon) {
            long daysLeft = product.getDateExpiration().toEpochDay() - LocalDate.now().toEpochDay();
            content.append("- ").append(product.getNom()).append(" expire dans ").append(daysLeft).append(" jour(s)\n");
        }

        emailService.sendErrorNotification("Récapitulatif quotidien", content.toString());
    }

    private void sendExpirationWarningEmail(List<Produit> products) {
        StringBuilder content = new StringBuilder();
        content.append("ALERTE : PRODUITS EXPIRANT DANS 3 JOURS\n\n");
        content.append("Date : ").append(LocalDate.now()).append("\n");
        content.append("Nombre de produits concernés : ").append(products.size()).append("\n\n");

        for (Produit product : products) {
            long daysLeft = product.getDateExpiration().toEpochDay() - LocalDate.now().toEpochDay();            content.append("- ").append(product.getNom())
                    .append(" (").append(product.getPrix()).append("€)")
                    .append(" - Expire dans ").append(daysLeft).append(" jour(s)")
                    .append(" (").append(product.getDateExpiration()).append(")\n");
        }

        emailService.sendErrorNotification("Alerte expiration produits", content.toString());
    }
}