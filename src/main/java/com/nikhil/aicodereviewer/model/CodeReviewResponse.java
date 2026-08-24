package com.nikhil.aicodereviewer.model;

public class CodeReviewResponse {

    private String language;
    private String summary;
    private String issues;
    private String suggestions;

    public CodeReviewResponse(
            String language,
            String summary,
            String issues,
            String suggestions) {

        this.language = language;
        this.summary = summary;
        this.issues = issues;
        this.suggestions = suggestions;
    }

    public String getLanguage() {
        return language;
    }

    public String getSummary() {
        return summary;
    }

    public String getIssues() {
        return issues;
    }

    public String getSuggestions() {
        return suggestions;
    }
}