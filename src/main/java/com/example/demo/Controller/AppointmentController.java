package com.example.demo.Controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.Category; // Assuming Category model is created
import com.example.demo.model.Client;
import com.example.demo.model.Lawyer;
import com.example.demo.dao.AppointmentDAO;
import org.springframework.dao.EmptyResultDataAccessException;
import com.example.demo.dao.ClientDAO; // DAO for clients
import com.example.demo.dao.LawyerDAO; // DAO for lawyers
import com.example.demo.dao.CorporateCaseDAO; // DAO for corporate cases
import com.example.demo.dao.CivilCaseDAO;
import com.example.demo.dao.CriminalCaseDAO;
import com.example.demo.dao.MatrimonialCaseDAO;
import com.example.demo.dao.CategoryDAO; // DAO for categories
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private AppointmentDAO appointmentDAO;

    @Autowired
    private ClientDAO clientDAO; // DAO for clients
    @Autowired
    private LawyerDAO lawyerDAO; // DAO for lawyers
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


    // Display the category selection page
    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = categoryDAO.listCategories();
        model.addAttribute("categories", categories);
        return "category"; // Name of the Thymeleaf template
    }

    // Handle the category selection
    @PostMapping("/selectCategory")
    public String selectCategory(@RequestParam("caseType") String caseType, Model model) {
        // Fetch the CatID from the database using the caseType
        int catID = categoryDAO.getCatIdByCaseType(caseType);
    
        // Create a new appointment and set the category ID
        Appointment appointment = new Appointment();
        appointment.setCatID(catID); // Set the catID
    
        // Fetch clients and lawyers
        List<Lawyer> lawyers = lawyerDAO.listLawyers();

        List<Integer> caseIds = new ArrayList<>();

    // Fetch case IDs based on catID
    switch (catID) {
        case 1:
            // Fetch from CorporateCase table
            caseIds = corporateCaseDAO.getCorporateCaseIds();
            break;
        case 3:
            // Fetch from CivilCase table
            caseIds = civilCaseDAO.getCivilCaseIds();
            break;
        case 4:
            // Fetch from CriminalCase table
            caseIds = criminalCaseDAO.getCriminalCaseIds();
            break;
        case 2:
            // Fetch from FamilyCase table
            caseIds = matrimonialCaseDAO.getMatrimonialCaseIds();
            break;
        default:
            // Handle default case or invalid catID
            caseIds = new ArrayList<>(); // or throw an exception, or log an error
            break;
    }
    
    
        // Add attributes to the model
        model.addAttribute("catID", catID);
        model.addAttribute("lawyers", lawyers);
        model.addAttribute("appointment", appointment); // Pass the appointment object
        model.addAttribute("caseIds", caseIds);
    
        // Return the appointment form view
        return "appointform"; // Name of the Thymeleaf template for displaying appointment form
    }


    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute("appointment") Appointment appointment, Model model) {
        Integer clientId = null;

        int caseId = appointment.getCatID();
        String type = categoryDAO.getCategoryTypeById(caseId);
        String Query = "SELECT c.ClientID FROM Client c JOIN " + type+"Case cc ON c.ClientID = cc.ClientID WHERE cc."+type+"CaseID = ?";
                try {
                    clientId = jdbcTemplate.queryForObject(Query, Integer.class, appointment.getCaseID());
                } catch (EmptyResultDataAccessException e) {
                    // Log and handle the case when no result is found
                    System.out.println("No client found for "+type+"CaseID: " + appointment.getCaseID());
                }       

        // Check if clientId is found
        if (clientId != null) {
            appointment.setClientID(clientId); // Set the found ClientID in the appointment
            appointmentDAO.saveAppointment(appointment); // Save the appointment
        } else {
            // Handle case where clientId is null (e.g., show an error message)
            model.addAttribute("errorMessage", "Client ID not found for the selected case.");
            return "errorPage"; // Return to an error page or redirect accordingly
        }

        return "redirect:/appointment/all"; // Redirect to the list of appointments
    }
    
    
    

    // List all appointments
    @GetMapping("/all")
    public String listAppointments(Model model) {
        List<Appointment> appointments = appointmentDAO.listAppointments(); // Get all appointments
        model.addAttribute("appointments", appointments);
        return "appointlist"; // Thymeleaf template to list appointments
    }

    // Show form to edit an existing appointment
    @GetMapping("/edit/{id}")
    public String showEditAppointmentForm(@PathVariable int id, Model model) {
        Appointment appointment = appointmentDAO.getAppointmentById(id); // Get appointment by ID
        model.addAttribute("appointment", appointment);

        // Fetch all clients, lawyers, and categories to populate dropdowns
        List<Client> clients = clientDAO.listClients();
        List<Lawyer> lawyers = lawyerDAO.listLawyers();
        List<Category> categories = categoryDAO.listCategories(); // Method to get categories

        model.addAttribute("clients", clients);
        model.addAttribute("lawyers", lawyers);
        model.addAttribute("categories", categories);

        return "editAppointment"; // Thymeleaf template for editing appointment
    }

    // Update an existing appointment
    @PostMapping("/update")
    public String updateAppointment(@ModelAttribute Appointment appointment) {
        appointmentDAO.updateAppointment(appointment); // Update appointment using DAO
        return "redirect:/appointment/all"; // Redirect to the list of appointments
    }

    // Delete an appointment
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable int id) {
        appointmentDAO.deleteAppointment(id); // Delete appointment using DAO
        return "redirect:/appointment/all"; // Redirect to the list of appointments
    }
}
