package com.example.filestorage.Service;

import com.example.filestorage.DTO.FileSaveDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageService {
    private static final Path STORAGE_ROOT = Path.of("/opt/jobcircle");
    private static final String PUBLIC_ROOT = "/jobcircle";

    public FileSaveDetails saveFile(MultipartFile file, String path) throws IOException {
        String normalizedPath = normalizeRelativePath(path);
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        if (!StringUtils.hasText(originalFileName) || originalFileName.contains("..")) {
            throw new IllegalArgumentException("File name is invalid");
        }

        String storedFileName = UUID.randomUUID() + "-" + originalFileName;
        Path targetDirectory = STORAGE_ROOT.resolve(normalizedPath).normalize();
        if (!targetDirectory.startsWith(STORAGE_ROOT)) {
            throw new IllegalArgumentException("Project name is invalid");
        }
        System.out.println("1");
        Files.createDirectories(targetDirectory);
        System.out.println("2");
        Path targetFile = targetDirectory.resolve(storedFileName).normalize();
        System.out.println("3");
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println("4");
        FileSaveDetails fileSaveDetails = new FileSaveDetails();
        System.out.println("5");
        fileSaveDetails.setPath(PUBLIC_ROOT + "/" + normalizedPath + "/" + storedFileName);
        return fileSaveDetails;
    }

    private String normalizeRelativePath(String path) {
        if (!StringUtils.hasText(path)) {
            throw new IllegalArgumentException("Project name is required");
        }

        String normalizedPath = Arrays.stream(path.replace("\\", "/").split("/"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .peek(segment -> {
                    if (".".equals(segment) || "..".equals(segment) || segment.contains("..")) {
                        throw new IllegalArgumentException("Project name is invalid");
                    }
                })
                .collect(Collectors.joining("/"));

        if (!StringUtils.hasText(normalizedPath)) {
            throw new IllegalArgumentException("Project name is invalid");
        }
        return normalizedPath;
    }
}
