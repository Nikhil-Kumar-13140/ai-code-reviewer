package com.nikhil.aicodereviewer.model;

import java.util.ArrayList;
import java.util.List;

public class AiReviewResult {

    private String summary;

    private List<String> bugs =
            new ArrayList<>();

    private List<String> securityIssues =
            new ArrayList<>();

    private List<String> codeQuality =
            new ArrayList<>();

    private List<String> performance =
            new ArrayList<>();

    private String improvedCode;

    private List<String> improvementReasons =
            new ArrayList<>();

    public AiReviewResult() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getBugs() {
        return bugs;
    }

    public void setBugs(List<String> bugs) {
        this.bugs =
                bugs != null
                        ? bugs
                        : new ArrayList<>();
    }

    public List<String> getSecurityIssues() {
        return securityIssues;
    }

    public void setSecurityIssues(
            List<String> securityIssues) {

        this.securityIssues =
                securityIssues != null
                        ? securityIssues
                        : new ArrayList<>();
    }

    public List<String> getCodeQuality() {
        return codeQuality;
    }

    public void setCodeQuality(
            List<String> codeQuality) {

        this.codeQuality =
                codeQuality != null
                        ? codeQuality
                        : new ArrayList<>();
    }

    public List<String> getPerformance() {
        return performance;
    }

    public void setPerformance(
            List<String> performance) {

        this.performance =
                performance != null
                        ? performance
                        : new ArrayList<>();
    }

    public String getImprovedCode() {
        return improvedCode;
    }

    public void setImprovedCode(
            String improvedCode) {

        this.improvedCode =
                improvedCode;
    }

    public List<String> getImprovementReasons() {
        return improvementReasons;
    }

    public void setImprovementReasons(
            List<String> improvementReasons) {

        this.improvementReasons =
                improvementReasons != null
                        ? improvementReasons
                        : new ArrayList<>();
    }
}