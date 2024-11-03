package com.example.demo.model;

public class CaseNoteWithDetails {
    private CaseNotes caseNote;
    private String categoryName;
    private String caseName;

    public CaseNoteWithDetails(CaseNotes caseNote, String categoryName, String caseName) {
        this.caseNote = caseNote;
        this.categoryName = categoryName;
        this.caseName = caseName;
    }

    // Getters
    public CaseNotes getCaseNote() {
        return caseNote;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCaseName() {
        return caseName;
    }
}
