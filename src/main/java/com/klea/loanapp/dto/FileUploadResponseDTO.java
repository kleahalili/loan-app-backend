
package com.klea.loanapp.dto;

public class FileUploadResponseDTO {
    private String message;
    private String fileName;

    public FileUploadResponseDTO(String message, String fileName) {
        this.message = message;
        this.fileName = fileName;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
