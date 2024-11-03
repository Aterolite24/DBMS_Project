package com.example.demo.Controller;

import com.example.demo.dao.CriminalCaseDAO; // Import the DAO for CriminalCase
import com.example.demo.model.Client;
import com.example.demo.model.CriminalCase; // Import the CriminalCase model
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/criminalCase")
public class CriminalCaseController {

    @Autowired
    private CriminalCaseDAO criminalCaseDAO; // Inject CriminalCaseDAO

    @Autowired
    private JdbcTemplate jdbcTemplate; // For client lookup

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("criminalCase", new CriminalCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "criminalCase"; // Name of Thymeleaf template for Criminal Case registration
    }

    @PostMapping("/register")
    public String registerCriminalCase(@ModelAttribute("criminalCase") CriminalCase criminalCase,
                                        @RequestParam Integer clientID, 
                                        Model model) {

        // Fetch client based on the provided clientID
        String sql = "SELECT * FROM Client WHERE ClientID = ?";
        List<Client> clients = jdbcTemplate.query(sql, ps -> ps.setInt(1, clientID), (rs, rowNum) -> {
            Client client = new Client();
            client.setClientId(rs.getInt("ClientID"));
            return client;
        });

        // Check if the client exists
        if (clients.isEmpty()) {
            model.addAttribute("clientNotFound", true);
            return "criminalCase"; // Return to the registration form if client not found
        }

        // Set the client in the criminal case
        criminalCase.setClientID(clients.get(0).getClientId()); // Set client ID in criminalCase

        // Save the criminal case via DAO
        criminalCaseDAO.saveCriminalCase(criminalCase);

        // Redirect to the list of criminal cases or another appropriate page
        return "redirect:/criminalCase/all"; // Adjust as needed
    }

    @GetMapping("/all")
    public String listCriminalCases(Model model) {
        // Fetch all criminal cases via DAO
        List<CriminalCase> criminalCases = criminalCaseDAO.listCriminalCases();
        model.addAttribute("criminalCases", criminalCases);
        model.addAttribute("x", 4);
        return "criminalCaseList"; // Thymeleaf template name for Criminal Case list
    }
}
