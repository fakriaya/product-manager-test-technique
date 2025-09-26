package com.example.miniprojetproduit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MiniprojetProduitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniprojetProduitApplication.class, args);
        System.out.println("Application démarrée avec tâches planifiées activées !");

    }

}
