package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.CaseWitDAO;
import com.example.demo.dao.CategoryDAO;
import com.example.demo.dao.CivilCaseDAO;
import com.example.demo.dao.ClientDAO;
import com.example.demo.dao.CorporateCaseDAO;
import com.example.demo.dao.CriminalCaseDAO;
import com.example.demo.dao.LawyerDAO;
import com.example.demo.dao.MatrimonialCaseDAO;
import com.example.demo.dao.ParalegalDAO;
import com.example.demo.dao.WitAndEviDAO;
import com.example.demo.model.CaseWit;
import com.example.demo.model.Category;
import com.example.demo.model.WitAndEvi;

@Controller
@RequestMapping("/witnessEvi")
public class WitAndEviController {

    @Autowired
    private WitAndEviDAO witAndEviDAO;

    @Autowired
    private ClientDAO clientDAO; // DAO for clients
    @Autowired
    private LawyerDAO lawyerDAO; // DAO for lawyers

    @Autowired
    private ParalegalDAO paralegalDAO; 

    @Autowired
    private CorporateCaseDAO corporateCaseDAO; // DAO for corporate cases
    @Autowired
    private CivilCaseDAO civilCaseDAO;
    @Autowired
    private MatrimonialCaseDAO matrimonialCaseDAO;
    @Autowired
    private CriminalCaseDAO criminalCaseDAO;

    @Autowired
    private CategoryDAO categoryDAO; // DAO for categories

    @Autowired
    private CaseWitDAO caseWitDAO;

    // Display form
    @GetMapping("/form") // works
    public String showWitnessForm(Model model) {
        model.addAttribute("witnessEvi", new WitAndEvi());
        return "witandeviform";  // The Thymeleaf template (HTML page) name
    }

    // Save new witness and evidence data
    @PostMapping("/save") //works
    public String saveWitness(@jakarta.validation.Valid @ModelAttribute("witnessEvi") WitAndEvi witAndEvi, 
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "witandeviform";  // Return to form if validation errors
        }

        // Save the witness and evidence data using DAO
        witAndEviDAO.saveWitAndEvi(witAndEvi);

        model.addAttribute("message", "Witness and evidence details saved successfully!");

        return "redirect:/witnessEvi/list";  // Redirect to list view after successful save
    }

    // List all witness and evidence records
    @GetMapping("/list") // works
    public String listWitnessEvidence(Model model) {
        List<WitAndEvi> witnessEvidenceList = witAndEviDAO.getAllWitAndEvi();
        model.addAttribute("witnessEvidenceList", witnessEvidenceList);
        
        if (witnessEvidenceList.isEmpty()) {
            model.addAttribute("message", "No witness and evidence records found.");
        }
        
        return "witlist";  // The Thymeleaf template to display the list
    }
    

    // Edit a specific witness and evidence record
    @GetMapping("/edit/{eviID}") //works
    public String editWitness(@PathVariable("eviID") int eviID, Model model) {
        WitAndEvi witnessEvi = witAndEviDAO.getWitAndEviById(eviID);
        if (witnessEvi != null) {
            model.addAttribute("witnessEvi", witnessEvi);
            return "witandeviform";  // Return to form with populated data
        } else {
            return "redirect:/witnessEvi/list";  // Redirect to list if not found
        }
    }

    // Delete a specific witness and evidence record
    @GetMapping("/delete/{eviID}") // works
    public String deleteWitness(@PathVariable("eviID") int eviID) {
        witAndEviDAO.deleteWitAndEvi(eviID);
        return "redirect:/witnessEvi/list";  // Redirect to list after deletion
    }

    @GetMapping("/categories") 
    public String showCategories(@RequestParam("eviID") int eviID, Model model) {
        List<Category> categories = categoryDAO.listCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("eviID", eviID); // Add eviID to the model
        return "witcat"; // Name of the Thymeleaf template
    }
    
    
    // Handle the category selection
    @PostMapping("/selCategory")
    public String selCategory(@RequestParam("caseType") String caseType, @RequestParam("eviID") int eviID, Model model) {
    
    // Fetch the CatID from the database using the caseType
    int catID = categoryDAO.getCatIdByCaseType(caseType);
    
    // Fetch case IDs based on catID
    List<Integer> caseIds = new ArrayList<>();
    switch (catID) {
        case 1:
            caseIds = corporateCaseDAO.getCorporateCaseIds();
            break;
        case 2:
            caseIds = matrimonialCaseDAO.getMatrimonialCaseIds();
            break;
        case 3:
            caseIds = civilCaseDAO.getCivilCaseIds();
            break;
        case 4:
            caseIds = criminalCaseDAO.getCriminalCaseIds();
            break;
        default:
            caseIds = new ArrayList<>();
            break;
    }
    
    // Add attributes to the model
    model.addAttribute("catID", catID);
    model.addAttribute("caseIds", caseIds);
    model.addAttribute("eviID", eviID);
    
    return "chooscase"; // This is the Thymeleaf view name
}   
    @PostMapping("/assignCase")
        public String assignCase(
        @RequestParam("eviID") int eviID, 
        @RequestParam("catID") int catID, 
        @RequestParam("caseID") int caseID, 
        Model model) {
    
    // Logic to assign the case to the witness
    // You might want to create a new CaseWit object here if needed
    CaseWit caseWit = new CaseWit();
    caseWit.setEviID(eviID); // Set the witness ID
    caseWit.setCatID(catID); // Set the category ID
    caseWit.setCaseID(caseID); // Set the case ID
    
    // Directly using DAO to save the CaseWit assignment
    caseWitDAO.save(caseWit); // Assuming you have a method to save in your DAO

    // Optionally, you can add a success message or redirect to another page
    model.addAttribute("message", "Case assigned successfully!");
    
    // Redirect to the witness list or any other relevant page
    return "redirect:/witnessEvi/list"; // Adjust the redirect path as needed
}


}
