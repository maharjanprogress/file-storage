package com.example.filestorage.Controller;

import com.example.filestorage.DTO.FileSaveDetails;
import com.example.filestorage.DTO.ResponseDTO;
import com.example.filestorage.Properties.FileStorage;
import com.example.filestorage.Service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StorageController {

    private final FileStorage fileStorage;
    private final StorageService storageService;

    public StorageController(FileStorage fileStorage, StorageService storageService) {
        this.fileStorage = fileStorage;
        this.storageService = storageService;
    }

    @PostMapping("/model/post")
    public ResponseEntity<ResponseDTO> postFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectName") String projectName,
            @RequestParam("password") String password) {
        if (!StringUtils.hasText(fileStorage.getKey())) {
            return new ResponseEntity<>(ResponseDTO.internalError("File storage API key is not configured"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!fileStorage.getKey().equals(password)) {
            return new ResponseEntity<>(ResponseDTO.unauthorized("Invalid password"), HttpStatus.UNAUTHORIZED);
        }
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(ResponseDTO.badRequest("File is required"), HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.hasText(projectName)) {
            return new ResponseEntity<>(ResponseDTO.badRequest("Project name is required"), HttpStatus.BAD_REQUEST);
        }

        try {
            FileSaveDetails fileSaveDetails = storageService.saveFile(file, projectName);
            fileSaveDetails.setPath(buildPublicPath(fileSaveDetails.getPath()));
            return new ResponseEntity<>(ResponseDTO.success("File uploaded successfully", fileSaveDetails), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(ResponseDTO.badRequest(exception.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return new ResponseEntity<>(ResponseDTO.internalError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildPublicPath(String storedPath) {
        if (!StringUtils.hasText(fileStorage.getPath())) {
            return storedPath;
        }
        String normalizedBasePath = fileStorage.getPath().endsWith("/")
                ? fileStorage.getPath().substring(0, fileStorage.getPath().length() - 1)
                : fileStorage.getPath();
        return normalizedBasePath + storedPath;
    }
}
