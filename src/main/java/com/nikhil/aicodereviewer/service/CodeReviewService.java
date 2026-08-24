package com.nikhil.aicodereviewer.service;

import com.nikhil.aicodereviewer.model.AiReviewResult;
import com.nikhil.aicodereviewer.model.CodeReviewRequest;
import com.nikhil.aicodereviewer.model.ValidationResult;
import org.springframework.stereotype.Service;

@Service
public class CodeReviewService {

    private final GeminiService geminiService;
    private final CodeValidationService codeValidationService;

    public CodeReviewService(
            GeminiService geminiService,
            CodeValidationService codeValidationService) {

        this.geminiService = geminiService;
        this.codeValidationService = codeValidationService;
    }

    public AiReviewResult reviewCode(CodeReviewRequest request) {

        ValidationResult validationResult =
                codeValidationService.validate(
                        request.getCode(),
                        request.getLanguage()
                );

        return geminiService.reviewCode(
                request.getCode(),
                request.getLanguage(),
                validationResult
        );
    }
}