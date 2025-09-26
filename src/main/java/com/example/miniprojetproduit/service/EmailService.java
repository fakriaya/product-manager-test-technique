package com.example.miniprojetproduit.service;

import com.example.miniprojetproduit.entity.Produit;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.admin.email}")
    private String adminEmail;

    public void sendProductCreatedNotification(Produit produit) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(adminEmail);
            helper.setSubject(" Nouveau produit créé - " + produit.getNom());

            String emailContent = buildProductCreatedEmail(produit);
            helper.setText(emailContent, true);

            mailSender.send(message);
            System.out.println("Email de création envoyé avec succès pour : " + produit.getNom());

        } catch (Exception e) {
            System.err.println("Erreur envoi email création: " + e.getMessage());
            sendErrorNotification("Erreur envoi email création", e.getMessage());
        }
    }

    public void sendProductUpdatedNotification(Produit produit) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(adminEmail);
            helper.setSubject("️ Produit modifié - " + produit.getNom());

            String emailContent = buildProductUpdatedEmail(produit);
            helper.setText(emailContent, true);

            mailSender.send(message);
            System.out.println("Email de modification envoyé avec succès pour : " + produit.getNom());

        } catch (Exception e) {
            System.err.println("Erreur envoi email modification: " + e.getMessage());
            sendErrorNotification("Erreur envoi email modification", e.getMessage());
        }
    }

    public void sendErrorNotification(String errorType, String errorMessage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(adminEmail);
            helper.setSubject(" ERREUR CRITIQUE - " + errorType);

            String content = "Une erreur critique s'est produite:\n\n" +
                    "Type: " + errorType + "\n" +
                    "Message: " + errorMessage + "\n" +
                    "Timestamp: " + java.time.LocalDateTime.now();

            helper.setText(content);
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("Erreur critique: impossible d'envoyer l'email d'erreur: " + e.getMessage());
        }
    }

    private String buildProductCreatedEmail(Produit produit) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'></head><body>");
        html.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        html.append("<h2 style='color: #28a745;'> Nouveau produit créé</h2>");
        html.append("<div style='background-color: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0;'>");
        html.append("<h3>").append(produit.getNom()).append("</h3>");
        html.append("<p><strong>Catégorie:</strong> ").append(produit.getCategorie()).append("</p>");
        html.append("<p><strong>Prix:</strong> ").append(produit.getPrix()).append(" €</p>");

        if (produit.getReference() != null) {
            html.append("<p><strong>Référence:</strong> ").append(produit.getReference()).append("</p>");
        }
        if (produit.getMatricule() != null) {
            html.append("<p><strong>Matricule:</strong> ").append(produit.getMatricule()).append("</p>");
        }
        if (produit.getDateExpiration() != null) {
            html.append("<p><strong>Date d'expiration:</strong> ").append(produit.getDateExpiration()).append("</p>");
        }
        if (produit.getFichier() != null) {
            html.append("<p><strong>Fichier:</strong> ").append(produit.getFichier()).append("</p>");
        }

        html.append("</div>");
        html.append("<p style='color: #6c757d;'>Email envoyé automatiquement le ").append(java.time.LocalDateTime.now()).append("</p>");
        html.append("</div></body></html>");

        return html.toString();
    }

    private String buildProductUpdatedEmail(Produit produit) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'></head><body>");
        html.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        html.append("<h2 style='color: #ffc107;'> Produit modifié</h2>");
        html.append("<div style='background-color: #fff3cd; padding: 20px; border-radius: 8px; margin: 20px 0;'>");
        html.append("<h3>").append(produit.getNom()).append("</h3>");
        html.append("<p><strong>Catégorie:</strong> ").append(produit.getCategorie()).append("</p>");
        html.append("<p><strong>Prix:</strong> ").append(produit.getPrix()).append(" €</p>");

        if (produit.getReference() != null) {
            html.append("<p><strong>Référence:</strong> ").append(produit.getReference()).append("</p>");
        }
        if (produit.getMatricule() != null) {
            html.append("<p><strong>Matricule:</strong> ").append(produit.getMatricule()).append("</p>");
        }
        if (produit.getDateExpiration() != null) {
            html.append("<p><strong>Date d'expiration:</strong> ").append(produit.getDateExpiration()).append("</p>");
        }

        html.append("</div>");
        html.append("<p style='color: #6c757d;'>Email envoyé automatiquement le ").append(java.time.LocalDateTime.now()).append("</p>");
        html.append("</div></body></html>");

        return html.toString();
    }
}