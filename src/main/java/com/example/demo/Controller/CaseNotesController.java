package com.example.demo.Controller;

import com.example.demo.dao.CaseNotesDAO;
import com.example.demo.dao.LawyerDAO;
import com.example.demo.model.CaseNotes;
import com.example.demo.model.Lawyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/caseNotes")
public class CaseNotesController {

    @Autowired
    private CaseNotesDAO caseNotesDAO;

    @Autowired
    @Qualifier("customUserDetailsService") // Specify the bean to use
    private UserDetailsService userDetailsService;

    @Autowired
    private LawyerDAO lawyerDAO;

    @GetMapping("/create")
    public String showCreateCaseNotePage(@RequestParam int caseID, @RequestParam int catID, Model model) {
        model.addAttribute("caseID", caseID);
        model.addAttribute("catID", catID);
        return "casenote"; // Return the name of the Thymeleaf template
    }

    @PostMapping("/create")
    public String createCaseNote(@RequestParam int caseID, 
                                  @RequestParam int catID, 
                                  @RequestParam String noteText, 
                                  @RequestParam String relevance,
                                  Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        Lawyer lawyer = lawyerDAO.getLawyerByEmail(userDetails.getUsername()).get(0); // Fetch lawyer details

        // Prepare the case note entity
        CaseNotes caseNote = new CaseNotes();
        caseNote.setNoteText(noteText);
        caseNote.setRelevance(relevance);

        // Set creation and modification dates to the current date
        Date currentDate = new Date(System.currentTimeMillis()); // Get current date
        caseNote.setDateCreated(currentDate); // Set creation date
        caseNote.setDateModified(currentDate); // Set modification date
        
        caseNote.setCaseID(caseID);
        caseNote.setCatID(catID);
        caseNote.setLawyerID(lawyer.getLawyerID());

        // Save the case note using your DAO
        caseNotesDAO.saveCaseNote(caseNote); // Assume you have a DAO method for saving case notes

        return "redirect:/dashboard"; // Redirect back to the dashboard or wherever you want
    }

    // Display a form to create a new CaseNote (GET method)
    @GetMapping("/new")
    public String showNewCaseNoteForm(Model model) {
        model.addAttribute("caseNote", new CaseNotes());
        return "newCaseNote"; // Thymeleaf template name for the new CaseNote form
    }

    // Handle the form submission for creating a new CaseNote (POST method)
    @PostMapping("/save")
    public String saveCaseNote(@ModelAttribute CaseNotes caseNotes) {
        caseNotesDAO.saveCaseNote(caseNotes); // Implement save logic in DAO
        return "redirect:/caseNotes/list";
    }

    // List all CaseNotes
    @GetMapping("/list")
    public String listCaseNotes(Model model) {
        List<CaseNotes> caseNotesList = caseNotesDAO.getAllCaseNotes();
        model.addAttribute("caseNotesList", caseNotesList);
        return "casenotelist"; // Thymeleaf template name for listing CaseNotes
    }

    // Delete a CaseNote
    @GetMapping("/delete/{caseNoteID}/{caseID}/{catID}")
    public String deleteCaseNote(@PathVariable("caseNoteID") Integer caseNoteID,
                                 @PathVariable("caseID") Integer caseID,
                                 @PathVariable("catID") Integer catID) {
        caseNotesDAO.deleteCaseNote(caseNoteID, caseID, catID);
        return "redirect:/caseNotes/list"; // Redirect after deletion
    }

    
    // Handle updating an existing CaseNote
    @PostMapping("/update")
    public String updateCaseNote(@ModelAttribute CaseNotes caseNotes) {
        caseNotesDAO.updateCaseNote(caseNotes); // Implement update logic in DAO
        return "redirect:/caseNotes/list";
    }
}
