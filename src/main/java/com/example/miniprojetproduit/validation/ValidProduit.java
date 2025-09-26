package com.example.miniprojetproduit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProduitValidator.class)
@Documented
public @interface ValidProduit {
    String message() default "Produit invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
