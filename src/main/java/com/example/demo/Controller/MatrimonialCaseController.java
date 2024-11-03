package com.example.demo.Controller;

import com.example.demo.dao.MatrimonialCaseDAO; // Import the new DAO for MatrimonialCase
import com.example.demo.model.Client;
import com.example.demo.model.MatrimonialCase; // Import the MatrimonialCase model
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matrimonialCase")
public class MatrimonialCaseController {

    @Autowired
    private MatrimonialCaseDAO matrimonialCaseDAO; // Inject MatrimonialCaseDAO

    @Autowired
    private JdbcTemplate jdbcTemplate; // For client lookup

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("matrimonialCase", new MatrimonialCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "matrimonialCase"; // Name of Thymeleaf template for Matrimonial Case registration
    }

    @PostMapping("/register")
    public String registerMatrimonialCase(@ModelAttribute("matrimonialCase") MatrimonialCase matrimonialCase,
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
            return "matrimonialCase"; // Return to the registration form if client not found
        }

        // Set the client in the matrimonial case
        matrimonialCase.setClientID(clients.get(0).getClientId()); // Set client ID in matrimonialCase

        // Save the matrimonial case via DAO
        matrimonialCaseDAO.saveMatrimonialCase(matrimonialCase);

        // Redirect to the list of matrimonial cases or another appropriate page
        return "redirect:/matrimonialCase/all"; // Adjust as needed
    }

    @GetMapping("/all")
    public String listMatrimonialCases(Model model) {
        // Fetch all matrimonial cases via DAO
        List<MatrimonialCase> matrimonialCases = matrimonialCaseDAO.listMatrimonialCases();
        model.addAttribute("matrimonialCases", matrimonialCases);
        model.addAttribute("x", 2);
        return "matrimonialCaseList"; // Thymeleaf template name for Matrimonial Case list
    }
}
