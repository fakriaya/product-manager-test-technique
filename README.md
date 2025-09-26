<img width="1503" height="650" alt="image" src="https://github.com/user-attachments/assets/49d1d650-624d-4975-b143-5512e7221459" />
<img width="1920" height="536" alt="image" src="https://github.com/user-attachments/assets/0bc0093b-f597-4296-a939-8777c60b1400" />
# Product Manager - Application Full Stack Java/Angular

Application complète de gestion de produits avec validation dynamique, upload de fichiers, notifications email et tâches planifiées.

## Architecture du Projet
miniprojet-produit/
├── src/main/java/com/example/miniprojetproduit/
│   ├── controller/          # Contrôleurs REST API
│   ├── entity/              # Entités JPA
│   ├── repository/          # Repositories Spring Data
│   ├── service/             # Services métier
│   ├── dto/                 # Data Transfer Objects
│   ├── form/                # Formulaires multipart
│   ├── exception/           # Gestion globale des erreurs
│   └── validation/          # Validateurs personnalisés
├── product-frontend/        # Application Angular
│   ├── src/app/
│   │   ├── models/          # Modèles TypeScript
│   │   ├── product-form/    # Composant formulaire
│   │   ├── product-list/    # Composant liste
│   │   └── services/        # Services Angular
│   └── ...
├── uploads/                 # Fichiers uploadés (auto-généré)
└── target/                  # Build Maven

## Technologies Utilisées

### Backend
- Java 17 - Langage principal
- Spring Boot 3.2.0 - Framework principal
- Spring Data JPA - Persistence des données
- H2 Database - Base de données en mémoire
- Spring Mail - Notifications email
- Maven - Gestionnaire de dépendances

### Frontend
- Angular 17 - Framework frontend
- Bootstrap 5 - Framework CSS
- RxJS - Programmation réactive
- TypeScript - Langage typé

## Installation et Exécution

### Prérequis
- Java 17+
- Node.js 16+
- Maven 3.6+
- Git

### 1. Cloner le projet
```bash
git clone https://github.com/VOTRE_USERNAME/product-manager-test-technique.git
cd product-manager-test-technique
2. Configuration Backend
Copier le fichier de configuration :
application.properties.example src/main/resources/application.properties
Configurer l'email dans application.properties :
properties# Configuration email Gmail
spring.mail.username=votre.email@gmail.com
spring.mail.password=votre_mot_de_passe_application_16_caracteres
app.admin.email=admin@votreentreprise.com
Lancer le backend :
bashmvn spring-boot:run
API disponible sur : http://localhost:8081
3. Configuration Frontend
bashcd product-frontend
npm install
ng serve
Interface disponible sur : http://localhost:4200
Fonctionnalités Principales
Validation Dynamique des Produits
CatégorieChamps ObligatoiresValidation SpécifiqueInformatiquenom, prix, referenceFormat : INF-XXXXVéhiculenom, prix, matriculeFormat : XXX-ABC-XXAlimentairenom, prix, dateExpirationDate > aujourd'hui
Gestion des Fichiers

Formats acceptés : Images (JPG, PNG), PDF, DOC, TXT
Taille maximale : 10MB
Stockage sécurisé : Noms UUID
Prévisualisation : Images dans l'interface
Nettoyage automatique : Suppression des fichiers orphelins

Notifications Email Automatiques

Création produit : Email HTML avec détails du produit
Modification : Notification des changements
Erreurs critiques : Alertes administrateur
Templates responsive : Emails lisibles sur mobile

Tâches Planifiées (Cron Jobs)
TâcheFréquenceDescriptionArchivage produitsQuotidien 2hSupprime produits alimentaires expirésRécapitulatif quotidienQuotidien 8hEmail statistiques du catalogueAlerte expirationQuotidien 9hPrévient des produits expirant bientôt
API REST Endpoints
Produits
MéthodeEndpointDescription
GET/api/produitsListe tous les produits
GET/api/produits/{id}Détails d'un produit
POST/api/produitsCréer un produit
PUT/api/produits/{id}Modifier un produit
DELETE/api/produits/{id}Supprimer un produit
GET/api/produits/healthStatut de l'API
Exemples d'utilisation
Créer un produit Informatique :
bashcurl -X POST http://localhost:8081/api/produits \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "MacBook Pro",
    "prix": 2499.99,
    "categorie": "Informatique",
    "reference": "INF-APPLE001"
  }'
Créer un produit avec fichier :
bashcurl -X POST http://localhost:8081/api/produits \
  -F "nom=Ordinateur Portable" \
  -F "prix=999.99" \
  -F "categorie=Informatique" \
  -F "reference=INF-LAPTOP001" \
  -F "fichier=@/chemin/vers/image.jpg"
Configuration Email Gmail

Activer l'authentification 2FA sur votre compte Google
Générer un mot de passe d'application :

Google Account → Sécurité → Mots de passe d'application
Sélectionner "Autre" → "Product Manager"
Copier le mot de passe généré 


Configurer application.properties avec le mot de passe généré

Base de Données
Console H2 (développement)

URL : http://localhost:8081/h2-console
JDBC URL : jdbc:h2:mem:testdb
Username : sa
Password : password

Modèle de données
sqlCREATE TABLE produits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prix DECIMAL(10,2) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    reference VARCHAR(50),        -- Requis si Informatique
    matricule VARCHAR(50),        -- Requis si Véhicule
    date_expiration DATE,         -- Requis si Alimentaire
    fichier VARCHAR(255)
);
Tests
Tests Backend :
bashmvn test
Tests Frontend :
bashcd product-frontend
ng test
Structure du Code
Patterns Utilisés

Repository Pattern : Accès aux données
Service Layer : Logique métier
DTO Pattern : Transfert de données
Observer Pattern : État réactif (RxJS)
Template Method : Emails HTML

Bonnes Pratiques

Injection par constructeur
Gestion globale des exceptions
Validation côté client ET serveur
État réactif avec BehaviorSubject
Noms de méthodes explicites

Résolution de Problèmes
Erreur "Port 8081 already in use"
bash# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8081
kill -9 <PID>
Erreur email "Authentication failed"

Vérifier l'authentification 2FA activée
Utiliser un mot de passe d'application (pas le mot de passe habituel)
Vérifier les paramètres SMTP

Problème CORS
Le backend accepte les requêtes de http://localhost:4200 par défaut.

Test Technique Java/Angular - Application full-stack démontrant les compétences en développement moderne
Application développée dans le cadre d'un test technique pour démontrer les compétences full-stack Java/Angular


