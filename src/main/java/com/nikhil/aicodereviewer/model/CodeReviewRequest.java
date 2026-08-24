package com.nikhil.aicodereviewer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CodeReviewRequest {

    @NotBlank(message = "Code cannot be empty")
    @Size(
            max = 10000,
            message = "Code cannot exceed 10,000 characters"
    )
    private String code;

    @NotBlank(
            message = "Programming language is required"
    )
    private String language;

    public CodeReviewRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}