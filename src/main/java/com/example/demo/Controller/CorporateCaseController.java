package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.CorporateCaseDAO;
import com.example.demo.model.Client;
import com.example.demo.model.CorporateCase;

@Controller
@RequestMapping("/corporateCase")
public class CorporateCaseController {

    @Autowired
    private CorporateCaseDAO corporateCaseDAO; // Inject CorporateCaseDAO

    @Autowired
    private JdbcTemplate jdbcTemplate; // Kept only for client lookup; ideally, this should also be moved to a DAO

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("corporateCase", new CorporateCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "corporateCase"; // Name of your Thymeleaf template file
    }

    @PostMapping("/register")
    public String registerCorporateCase(@ModelAttribute("corporateCase") CorporateCase corporateCase,
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
            return "corporateCase"; // Return to the registration form
        }

        // Set the client in the corporate case
        corporateCase.setClientID(clients.get(0).getClientId()); // Set client ID in corporateCase

        // Save the corporate case via DAO
        corporateCaseDAO.saveCorporateCase(corporateCase);

        // Redirect to the list of corporate cases or another appropriate page
        return "redirect:/corporateCase/all"; // Adjust as needed
    }

    @GetMapping("/all")
    public String listCorporateCases(Model model) {
        // Fetch all corporate cases via DAO
        List<CorporateCase> corporateCases = corporateCaseDAO.listCorporateCases();
        model.addAttribute("corporateCases", corporateCases);
        model.addAttribute("x",1);
        return "corporateCaseList"; // Thymeleaf template name
    }
}
