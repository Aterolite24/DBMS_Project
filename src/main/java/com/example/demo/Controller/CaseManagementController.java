package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.model.Category;
import com.example.demo.model.CivilCase;
import com.example.demo.model.CorporateCase;
import com.example.demo.model.CriminalCase;
import com.example.demo.model.MatrimonialCase;


@Controller
public class CaseManagementController {
    @Autowired
    private CategoryDAO categoryDAO; 

    @GetMapping("/addNewCase")
    public String addNewCase(Model model) {
        List<Category> categories = categoryDAO.listCategories();
        model.addAttribute("categories", categories);
        return "addNewCasePage"; // Thymeleaf will look for addNewCasePage.html
    }

    @GetMapping("/viewCurrentCases")
    public String viewCurrentCases() {
        return "viewCurrentCasesPage"; // Thymeleaf will look for viewCurrentCasesPage.html
    }

    @PostMapping("/selCategory")
    public ModelAndView handleCategorySelection(@RequestParam("caseType") int caseType) {
        ModelAndView modelAndView = new ModelAndView();
        
        // Redirect to a different view or URL based on caseType
        switch (caseType) {
            case 1 -> modelAndView.setViewName("redirect:/corp_client");
            case 2 -> modelAndView.setViewName("redirect:/matri_client");
            case 3 -> modelAndView.setViewName("redirect:/civil_client");
            case 4 -> modelAndView.setViewName("redirect:/criminal_client");
            default -> modelAndView.setViewName("error");
        }

        // Optionally pass the eviID as a parameter to the redirected URLs
        
        return modelAndView;
    }

    @GetMapping("/corp_client")
    public String showRegistrationFormCorp(Model model) {
        model.addAttribute("corporateCase", new CorporateCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "corporateCaseClient"; // Name of your Thymeleaf template file
    }

    @GetMapping("/matri_client")
    public String showRegistrationFormMatri(Model model) {
        model.addAttribute("matrimonialCase", new MatrimonialCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "matrimonialCaseClient"; // Name of your Thymeleaf template file
    }

    @GetMapping("/civil_client")
    public String showRegistrationFormCivil(Model model) {
        model.addAttribute("civilCase", new CivilCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "civilCaseClient"; // Name of your Thymeleaf template file
    }

    @GetMapping("/criminal_client")
    public String showRegistrationFormCriminal(Model model) {
        model.addAttribute("criminalCase", new CriminalCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "criminalCaseClient"; // Name of your Thymeleaf template file
    }




}
