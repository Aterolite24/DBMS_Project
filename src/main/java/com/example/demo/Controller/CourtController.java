package com.example.demo.Controller;

import com.example.demo.dao.CaseCourtDAO;
import com.example.demo.dao.CourtDAO;
import com.example.demo.model.CaseCourt;
import com.example.demo.model.Court;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/court")
public class CourtController {

    @Autowired
    private CourtDAO courtDAO;

    // Display all courts
    @GetMapping("/list")
    public String listCourts(Model model) {
        List<Court> courts = courtDAO.listCourts();
        model.addAttribute("courts", courts);
        return "CourtList";
    }

    // Show form to add a new court
    @GetMapping("/add")
    public String showAddCourtForm(Model model) {
        model.addAttribute("court", new Court());
        return "Court";
    }

    // Save a new court
    @PostMapping("/save")
    public String saveCourt(@ModelAttribute Court court) {
        courtDAO.saveCourt(court);
        return "redirect:/court/list";
    }

    // Show form to edit an existing court
    @GetMapping("/edit/{id}")
    public String showEditCourtForm(@PathVariable Integer id, Model model) {
        Court court = courtDAO.getCourtById(id);
        model.addAttribute("court", court);
        return "editcourt";
    }

    // Update an existing court
    @PostMapping("/update")
    public String updateCourt(@ModelAttribute Court court) {
        courtDAO.updateCourt(court);
        return "redirect:/court/list";
    }

    // Delete a court
    @GetMapping("/delete/{id}")
    public String deleteCourt(@PathVariable Integer id) {
        courtDAO.deleteCourt(id);
        return "redirect:/court/list";
    }

    @Autowired
    private CaseCourtDAO caseCourtDao;

    // Display the court assignment page
    @GetMapping("/assignCourt")
    public String showAssignCourtPage(@RequestParam("caseId") int caseId,
                                      @RequestParam("x") String x, Model model) {
        List<Court> courts = courtDAO.listCourts();
        model.addAttribute("courts", courts);
        model.addAttribute("caseId", caseId);
        model.addAttribute("x", x);
        return "assignCourt";
    }

    // Handle court assignment
    @PostMapping("/assignCourt/save")
    public String assignCourts(@RequestParam("caseId") int caseId,
                               @RequestParam(value = "courtIds", required = false) List<Integer> courtIds,
                               @RequestParam("x") int x) {
        if (courtIds != null && !courtIds.isEmpty()) {
            for (Integer courtId : courtIds) {
                CaseCourt caseCourt = new CaseCourt();
                caseCourt.setCaseID(caseId);
                caseCourt.setCourtID(courtId);
                caseCourt.setCatID(x);

                caseCourtDao.save(caseCourt);
            }
        }
        return "redirect:/corporateCase/all";
    }
}
