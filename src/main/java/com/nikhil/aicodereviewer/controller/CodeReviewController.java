package com.nikhil.aicodereviewer.controller;

import com.nikhil.aicodereviewer.model.AiReviewResult;
import com.nikhil.aicodereviewer.model.CodeReviewRequest;
import com.nikhil.aicodereviewer.service.CodeReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:5173")
public class CodeReviewController {

    private final CodeReviewService codeReviewService;

    public CodeReviewController(
            CodeReviewService codeReviewService) {

        this.codeReviewService =
                codeReviewService;
    }

    @PostMapping
    public AiReviewResult reviewCode(
            @Valid @RequestBody CodeReviewRequest request) {

        return codeReviewService.reviewCode(
                request
        );
    }
}