package com.example.miniprojetproduit.validation;

import com.example.miniprojetproduit.dto.ProduitDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProduitValidator implements ConstraintValidator<ValidProduit, ProduitDTO> {

    @Override
    public boolean isValid(ProduitDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        String cat = dto.getCategorie();
        if (cat != null) {
            if ("Informatique".equalsIgnoreCase(cat) && (dto.getReference() == null || dto.getReference().isBlank())) {
                context.buildConstraintViolationWithTemplate("La référence est requise pour la catégorie Informatique")
                        .addPropertyNode("reference")
                        .addConstraintViolation();
                valid = false;
            }
            if ("Véhicule".equalsIgnoreCase(cat) && (dto.getMatricule() == null || dto.getMatricule().isBlank())) {
                context.buildConstraintViolationWithTemplate("Le matricule est requis pour la catégorie Véhicule")
                        .addPropertyNode("matricule")
                        .addConstraintViolation();
                valid = false;
            }
            if ("Alimentaire".equalsIgnoreCase(cat) && dto.getDateExpiration() == null) {
                context.buildConstraintViolationWithTemplate("La date d'expiration est requise pour la catégorie Alimentaire")
                        .addPropertyNode("dateExpiration")
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}
