package com.example.miniprojetproduit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le répertoire d'uploads : " + this.uploadDir, e);
        }
    }

    /**
     * Sauvegarde le fichier et renvoie le nom de fichier stocké (UUID + extension).
     */
    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int i = original.lastIndexOf('.');
        if (i > 0) {
            ext = original.substring(i);
        }

        String filename = UUID.randomUUID().toString() + ext;
        Path target = this.uploadDir.resolve(filename).normalize();
        if (!target.getParent().equals(this.uploadDir)) {
            throw new IOException("Chemin de stockage invalide.");
        }
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

}
