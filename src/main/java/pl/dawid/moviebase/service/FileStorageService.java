package pl.dawid.moviebase.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path postersDir = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize()
                    .resolve("posters");

            Files.createDirectories(postersDir);

            String originalFilename = file.getOriginalFilename();
            String safeFilename = originalFilename == null
                    ? "poster"
                    : originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

            String filename = UUID.randomUUID() + "_" + safeFilename;

            Path targetPath = postersDir.resolve(filename).normalize();

            file.transferTo(targetPath.toFile());

            return "/uploads/posters/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Nie udało się zapisać pliku: " + file.getOriginalFilename(), e);
        }
    }
}