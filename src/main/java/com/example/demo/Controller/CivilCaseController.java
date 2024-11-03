package com.example.demo.Controller;

import com.example.demo.dao.CivilCaseDAO;
import com.example.demo.model.Client;
import com.example.demo.model.CivilCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/civilCase")
public class CivilCaseController {

    @Autowired
    private CivilCaseDAO civilCaseDAO; // Inject CivilCaseDAO

    @Autowired
    private JdbcTemplate jdbcTemplate; // For client lookup

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("civilCase", new CivilCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "civilCase"; // Name of Thymeleaf template for Civil Case registration
    }

    @PostMapping("/register")
    public String registerCivilCase(@ModelAttribute("civilCase") CivilCase civilCase,
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
            return "civilCase"; // Return to the registration form if client not found
        }

        // Set the client in the civil case
        civilCase.setClientID(clients.get(0).getClientId()); // Set client ID in civilCase

        // Save the civil case via DAO
        civilCaseDAO.saveCivilCase(civilCase);

        // Redirect to the list of civil cases or another appropriate page
        return "redirect:/civilCase/all"; // Adjust as needed
    }

    @GetMapping("/all")
    public String listCivilCases(Model model) {
        // Fetch all civil cases via DAO
        List<CivilCase> civilCases = civilCaseDAO.listCivilCases();
        model.addAttribute("civilCases", civilCases);
        model.addAttribute("x", 3);
        return "civilCaseList"; // Thymeleaf template name for Civil Case list
    }
}
