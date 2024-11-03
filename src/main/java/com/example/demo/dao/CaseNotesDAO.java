package com.example.demo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CaseNotes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.RowMapper;


@Repository
public class CaseNotesDAO {

    private final JdbcTemplate jdbcTemplate;

    public CaseNotesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CaseNotes> getAllCaseNotes() {
        String sql = "SELECT * FROM CaseNotes"; // Modify to include necessary joins if required
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        
        List<CaseNotes> caseNotesList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            CaseNotes caseNote = new CaseNotes();
            caseNote.setCaseNoteID((Integer) row.get("CaseNoteID"));
            caseNote.setNoteText((String) row.get("NoteText"));
            caseNote.setRelevance((String) row.get("Relevance"));
            caseNote.setDateCreated((Date) row.get("DateCreated"));
            caseNote.setDateModified((Date) row.get("DateModified"));
            caseNote.setCaseID((Integer) row.get("CaseID")); // Retrieved from the CASE statement
            caseNote.setCatID((Integer) row.get("CatID")); // Include category ID if necessary
            caseNote.setLawyerID((Integer) row.get("LawyerID"));
            caseNotesList.add(caseNote);
        }
        
        return caseNotesList;
    }

    // public List<CaseNotes> getAllCaseNotesForLawyer(int lawyerId) {
    //     String sql = "SELECT cn.CaseNoteID, cn.NoteText, cn.Relevance, cn.DateCreated, cn.DateModified, " +
    //                  "CASE " +
    //                  "    WHEN t.CatID = 1 THEN c.CorporateCaseID " +
    //                  "    WHEN t.CatID = 2 THEN m.MatrimonialCaseID " +
    //                  "    WHEN t.CatID = 3 THEN ci.CivilCaseID " +
    //                  "    WHEN t.CatID = 4 THEN cr.CriminalCaseID " +
    //                  "END AS CaseID, " +
    //                  "COALESCE(c.CaseDesc, m.CaseDesc, ci.CaseDesc, cr.CaseDesc) AS CaseDesc, " +
    //                  "ct.CaseType, l.LawyerID ,l.fName" +
    //                  "FROM CaseNotes cn " +
    //                  "JOIN Task t ON cn.CaseID = t.CaseID AND cn.CatID = t.CatID " +
    //                  "LEFT JOIN CorporateCase c ON t.CaseID = c.CorporateCaseID AND t.CatID = 1 " +
    //                  "LEFT JOIN MatrimonialCase m ON t.CaseID = m.MatrimonialCaseID AND t.CatID = 2 " +
    //                  "LEFT JOIN CivilCase ci ON t.CaseID = ci.CivilCaseID AND t.CatID = 3 " +
    //                  "LEFT JOIN CriminalCase cr ON t.CaseID = cr.CriminalCaseID AND t.CatID = 4 " +
    //                  "JOIN Category ct ON t.CatID = ct.CatID " +
    //                  "JOIN Lawyer l ON cn.LawyerID = l.LawyerID " +
    //                  "WHERE cn.LawyerID = ?";
        
    //     List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, lawyerId);
    
    //     List<CaseNotes> caseNotesList = new ArrayList<>();
    //     for (Map<String, Object> row : rows) {
    //         CaseNotes caseNote = new CaseNotes();
    //         caseNote.setCaseNoteID((Integer) row.get("CaseNoteID"));
    //         caseNote.setNoteText((String) row.get("NoteText"));
    //         caseNote.setRelevance((String) row.get("Relevance"));
    //         caseNote.setDateCreated((Date) row.get("DateCreated"));
    //         caseNote.setDateModified((Date) row.get("DateModified"));
    //         caseNote.setCaseID((Integer) row.get("CaseID")); // Retrieved from the CASE statement
    //         caseNote.setLawyerID((Integer) row.get("LawyerID"));
    //         caseNotesList.add(caseNote);
    //     }
    //     return caseNotesList;
    // }
    public List<CaseNotes> getAllCaseNotesForLawyer(int lawyerId) {
        String sql = "SELECT cn.CaseNoteID, cn.NoteText, cn.Relevance, cn.DateCreated, cn.DateModified, " +
                     "cn.CaseID, cn.CatID, cn.LawyerID " +
                     "FROM CaseNotes cn " +
                     "WHERE cn.LawyerID = ?";
    
        // Query the database for case notes belonging to the lawyer
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, lawyerId);
    
        List<CaseNotes> caseNotesList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            CaseNotes caseNote = new CaseNotes();
            caseNote.setCaseNoteID((Integer) row.get("CaseNoteID"));
            caseNote.setNoteText((String) row.get("NoteText"));
            caseNote.setRelevance((String) row.get("Relevance"));
            caseNote.setDateCreated((Date) row.get("DateCreated"));
            caseNote.setDateModified((Date) row.get("DateModified"));
            caseNote.setCaseID((Integer) row.get("CaseID")); // Directly from CaseNotes
            caseNote.setCatID((Integer) row.get("CatID"));   // Category ID from CaseNotes
            caseNote.setLawyerID((Integer) row.get("LawyerID")); // Lawyer ID from CaseNotes
            caseNotesList.add(caseNote);
        }
        return caseNotesList;
    }
    
    
    public void saveCaseNote(CaseNotes caseNote) {
        String sql = "INSERT INTO CaseNotes (NoteText, Relevance, DateCreated, DateModified, CaseID, CatID, LawyerID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
                            caseNote.getNoteText(), 
                            caseNote.getRelevance(), 
                            caseNote.getDateCreated(), 
                            caseNote.getDateModified(), 
                            caseNote.getCaseID(), 
                            caseNote.getCatID(), 
                            caseNote.getLawyerID());
    }

    // public CaseNotes getCaseNoteById(int caseNoteID, int caseID, int catID) {
    //     String sql = "SELECT * FROM CaseNotes WHERE CaseNoteID = ? AND CaseID = ? AND CatID = ?";
    
    //     return jdbcTemplate.query(sql, new Object[]{caseNoteID, caseID, catID}, new RowMapper<CaseNotes>() {
    //         @Override
    //         public CaseNotes mapRow(ResultSet rs, int rowNum) throws SQLException {
    //             CaseNotes caseNote = new CaseNotes();
    //             caseNote.setCaseNoteID(rs.getInt("CaseNoteID"));
    //             caseNote.setNoteText(rs.getString("NoteText"));
    //             caseNote.setRelevance(rs.getString("Relevance"));
    //             caseNote.setDateCreated(rs.getDate("DateCreated"));
    //             caseNote.setDateModified(rs.getDate("DateModified"));
    //             caseNote.setCaseID(rs.getInt("CaseID"));
    //             caseNote.setCatID(rs.getInt("CatID"));
    //             caseNote.setLawyerID(rs.getInt("LawyerID"));
    //             return caseNote;
    //         }
    //     }).stream().findFirst().orElse(null); // Return the first CaseNotes object or null if not found
    // }
    

    public void updateCaseNote(CaseNotes caseNote) {
        String sql = "UPDATE CaseNotes SET NoteText = ?, Relevance = ?, DateModified = ? " +
                     "WHERE CaseNoteID = ? AND CaseID = ? AND CatID = ?";
        jdbcTemplate.update(sql, 
                            caseNote.getNoteText(), 
                            caseNote.getRelevance(), 
                            caseNote.getDateModified(), 
                            caseNote.getCaseNoteID(), 
                            caseNote.getCaseID(), 
                            caseNote.getCatID());
    }

    public void deleteCaseNote(int caseNoteID, int caseID, int catID) {
        String sql = "DELETE FROM CaseNotes WHERE CaseNoteID = ? AND CaseID = ? AND CatID = ?";
        jdbcTemplate.update(sql, caseNoteID, caseID, catID);
    }
}
