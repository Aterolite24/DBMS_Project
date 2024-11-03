package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;

public class CaseNoteWithDetailDAO {

    @Autowired
    private CorporateCaseDAO cC;

    @Autowired
    private CivilCaseDAO cc;

    @Autowired
    private CriminalCaseDAO cr;
    @Autowired
    private MatrimonialCaseDAO ma;

    @Autowired
    private CategoryDAO category;
    
    private String getCaseNameById(int caseID, int catID) {
        String caseName = "";
    
        switch (catID) {
            case 1: // Corporate
                caseName = cC.getCorporateCaseById(caseID).getCaseDesc();
                break;
            case 2: // Matrimonial
                caseName = ma.getMatrimonialCaseById(caseID).getCaseDesc();
                break;
            case 3: // Civil
                caseName = cc.getCivilCaseById(caseID).getCaseDesc();
                break;
            case 4: // Criminal
                caseName = cr.getCriminalCaseById(caseID).getCaseDesc();
                break;
            default:
                caseName = "Unknown Case";
        }
    
        return caseName;
    }
    
}
