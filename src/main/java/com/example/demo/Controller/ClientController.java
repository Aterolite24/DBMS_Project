package com.example.demo.Controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.ClientDAO;
import com.example.demo.model.Client;



@Controller
public class ClientController {

    @Autowired
    private ClientDAO clientDAO;

    @GetMapping("/register")
    public String showForm(Model model) {
        Client client = new Client();
        model.addAttribute("client", client);
        return "register_form";
    }

    @PostMapping("/register")
    public String submitForm(@ModelAttribute("client") Client client,
                             @RequestParam String phoneNumber1,
                             @RequestParam(required = false) String phoneNumber2,
                             @RequestParam String email1,
                             @RequestParam(required = false) String email2) {
        // Save the client first to get the ID
        clientDAO.saveClient(client);

        // After saving, retrieve the generated ID
        Integer clientId = clientDAO.getLastInsertId();

        // Save phone numbers if provided
        if (phoneNumber1 != null && !phoneNumber1.isEmpty()) {
            clientDAO.saveClientPhone(clientId, phoneNumber1);
        }
        if (phoneNumber2 != null && !phoneNumber2.isEmpty()) {
            clientDAO.saveClientPhone(clientId, phoneNumber2);
        }

        // Save emails if provided
        if (email1 != null && !email1.isEmpty()) {
            clientDAO.saveClientEmail(clientId, email1);
        }
        if (email2 != null && !email2.isEmpty()) {
            clientDAO.saveClientEmail(clientId, email2);
        }

        return "redirect:/clients"; // Redirect to success page
    }

    @GetMapping("/clients")
    public String listClients(Model model) {
        List<Client> clients = clientDAO.listClients();
        model.addAttribute("clients", clients);
        return "client_list"; // Return a view name for displaying the client list
    }


    @GetMapping("/clients/edit/{id}")
public String showEditForm(@PathVariable("id") Integer id, Model model) {
    try {
        // Fetch the client by ID
        Client client = clientDAO.getClientById(id);
        model.addAttribute("client", client);

        // Fetch associated phone numbers and emails
        List<String> clientPhones = clientDAO.getClientPhones(id); // List of phone numbers as Strings
        List<String> clientEmails = clientDAO.getClientEmails(id); // List of ClientEmail objects
        
        model.addAttribute("clientPhones", clientPhones);
        model.addAttribute("clientEmails", clientEmails);

        // Safely retrieve phone numbers and emails
        String phoneNumber1 = clientPhones.size() > 0 ? clientPhones.get(0) : ""; // Correctly get the phone number
        String phoneNumber2 = clientPhones.size() > 1 ? clientPhones.get(1) : ""; // Correctly get the second phone number
        String email1 = clientEmails.size() > 0 ? clientEmails.get(0) : ""; // Correctly get the first email
        String email2 = clientEmails.size() > 1 ? clientEmails.get(1) : ""; // Correctly get the second email


        // Add the phone numbers and emails to the model
        model.addAttribute("phoneNumber1", phoneNumber1);
        model.addAttribute("phoneNumber2", phoneNumber2);
        model.addAttribute("email1", email1);
        model.addAttribute("email2", email2);
    } catch (EmptyResultDataAccessException e) {
        model.addAttribute("error", "Client not found");
        return "error_page"; // Redirect to an error page or handle accordingly
    } catch (Exception e) {
        model.addAttribute("error", "An error occurred while retrieving the client data");
        return "error_page"; // Redirect to an error page for other exceptions
    }
    
    return "edit_client"; // Render the edit client form
}


    @PostMapping("/clients/update")
public String updateClient(@ModelAttribute Client client, @RequestParam String phoneNumber1, 
                           @RequestParam String phoneNumber2, @RequestParam String email1,
                           @RequestParam String email2) {
    // Update the client information
    clientDAO.updateClient(client);

    // Update phone numbers and emails
    List<String> newPhoneNumbers = new ArrayList<>();
    if (!phoneNumber1.isEmpty()) newPhoneNumbers.add(phoneNumber1);
    if (!phoneNumber2.isEmpty()) newPhoneNumbers.add(phoneNumber2);
    clientDAO.updateClientPhones(client.getClientId(), newPhoneNumbers);

    List<String> newEmails = new ArrayList<>();
    if (!email1.isEmpty()) newEmails.add(email1);
    if (!email2.isEmpty()) newEmails.add(email2);
    clientDAO.updateClientEmails(client.getClientId(), newEmails);

    return "redirect:/clients"; // Redirect to the client list after updating
}


    @PostMapping("/clients/delete/{id}")
    public String deleteClient(@PathVariable("id") Integer id) {
        clientDAO.deleteClient(id);
        clientDAO.deleteClientPhone(id); // Delete associated phone numbers
        clientDAO.deleteClientEmail(id); // Delete associated emails
        return "redirect:/clients"; // Redirect to the client list after deletion
    }


    @GetMapping("/clients/search")
public String searchClients(@RequestParam("query") String query, Model model) {
    List<Client> clients = clientDAO.searchClients(query);
    model.addAttribute("clients", clients);
    model.addAttribute("searchQuery", query); // Add the search query to the model to show it on the view
    return "client_list"; // Return the view name for displaying the client list
}

}
