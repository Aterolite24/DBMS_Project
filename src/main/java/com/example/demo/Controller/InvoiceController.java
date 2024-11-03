package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CategoryDAO categoryDAO; 

    @GetMapping("/add")
    public String showAddInvoiceForm(@RequestParam("caseId") int caseId, @RequestParam("catId") int catId, Model model) {
        // Create a query to fetch the appropriate case based on the catId
        String sql = "";
        String type = categoryDAO.getCategoryTypeById(catId);
    
                sql =  "SELECT * FROM "+type+"Case WHERE "+type+"CaseID = ?";
        // switch (catId) {
        //     case 1: // Assuming 1 is for CorporateCase
        //         sql = "SELECT * FROM CorporateCase WHERE CorporateCaseID = ?";
        //         break;
        //     case 2: // Assuming 2 is for CriminalCase
        //         sql = "SELECT * FROM CriminalCase WHERE CriminalCaseID = ?";
        //         break;
        //     case 3: // Assuming 3 is for CivilCase
        //         sql = "SELECT * FROM CivilCase WHERE CivilCaseID = ?";
        //         break;
        //     case 4: // Assuming 4 is for MatrimonialCase
        //         sql = "SELECT * FROM MatrimonialCase WHERE MatrimonialCaseID = ?";
        //         break;
        //     default:
        //         return "error";  // Handle error for unknown case type
        // }
    
        List<Map<String, Object>> cases = jdbcTemplate.queryForList(sql, caseId);
        
        if (cases.isEmpty()) {
            return "error";  // Handle error when case is not found
        }
    
        model.addAttribute("caseDetails", cases.get(0)); // Add case details to the model
        // No need to add catId again, it can be accessed directly from the method parameter
        model.addAttribute("caseId", caseId);
    model.addAttribute("catId", catId);
        return "addinvoCorp";  // Thymeleaf template to add the invoice
    }
    

// Handle form submission for adding a new invoice
@PostMapping("/save")
public String saveInvoice(@RequestParam int caseId,
                          @RequestParam int catId,
                          @RequestParam double amount,
                          @RequestParam String dueDate,
                          @RequestParam String status) {
    // Determine the table to insert based on the catId
    String query = "INSERT INTO Invoice (CaseID, CatID, InvoiceDate, Amount, DueDate, Status) VALUES (?, ?, ?, ?, ?, ?)";
    
    // Insert the new invoice using caseId and catId
    jdbcTemplate.update(query, caseId, catId, LocalDate.now(), amount, dueDate, status);
    return "redirect:/invoice/list"; // Redirect to the invoice list after saving
}

@GetMapping("/list")
public String listInvoices(Model model) {
    // Retrieve all categories from the Category table
    List<Category> categories = jdbcTemplate.query("SELECT CatID, caseType FROM Category", 
        new BeanPropertyRowMapper<>(Category.class));

    // List to hold all invoice results
    List<Map<String, Object>> invoices = new ArrayList<>();

    // Loop through each category to construct the invoice query dynamically
    for (Category category : categories) {
        String caseType = category.getCaseType();
        int catID = category.getCatID();

        // Dynamically build the invoice query for each category type
        String invoiceQuery = "SELECT i.InvoiceID, i.Amount, i.InvoiceDate, i.Status, i.CaseID, i.CatID, c.CaseType, " +
                              "'" + caseType + "Case' AS CaseCategory, " +
                              "cc.CaseDesc AS CaseDesc " +
                              "FROM Invoice i " +
                              "JOIN Category c ON i.CatID = c.CatID " +
                              "LEFT JOIN " + caseType + "Case cc ON i.CaseID = cc." + caseType + "CaseID " +
                              "WHERE i.CatID = ?";

        // Execute the query for this category and add to the invoices list
        invoices.addAll(jdbcTemplate.queryForList(invoiceQuery, catID));
    }

    // Query to fetch cases without invoices across all categories
    List<Map<String, Object>> casesWithoutInvoices = new ArrayList<>();

    for (Category category : categories) {
        String caseType = category.getCaseType();
        int catID = category.getCatID();

        // Dynamically build the query to find cases without invoices for each category
        String caseWithoutInvoiceQuery = "SELECT cc." + caseType + "CaseID AS CaseID, cc.CaseDesc, " +
                                         "'" + caseType + "Case' AS CaseCategory, " + catID + " AS CatID " +
                                         "FROM " + caseType + "Case cc " +
                                         "LEFT JOIN Invoice i ON cc." + caseType + "CaseID = i.CaseID AND i.CatID = ? " +
                                         "WHERE i.InvoiceID IS NULL";

        // Execute the query for cases without invoices for this category and add to the list
        casesWithoutInvoices.addAll(jdbcTemplate.queryForList(caseWithoutInvoiceQuery, catID));
    }

    // Add both lists to the model
    model.addAttribute("invoices", invoices);
    model.addAttribute("casesWithoutInvoices", casesWithoutInvoices);

    // Return the Thymeleaf template name
    return "invoicelist";  // Template to display invoices
}




    @PostMapping("/updateStatus")
public String updateInvoiceStatus(@RequestParam("invoiceID") int invoiceID, 
                                  @RequestParam("status") String status) {
    String sql = "UPDATE Invoice SET Status = ? WHERE InvoiceID = ?";
    jdbcTemplate.update(sql, status, invoiceID);
    return "redirect:/invoice/list";  // Redirect back to the invoice list
}
}
