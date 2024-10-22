package com.example.demo.Controller;

import java.util.ArrayList; // Assuming Task model is created
import java.util.List; // Assuming Category model is created

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // DAO for tasks
import org.springframework.web.bind.annotation.PathVariable; // DAO for clients
import org.springframework.web.bind.annotation.PostMapping; // DAO for lawyers
import org.springframework.web.bind.annotation.RequestMapping; // DAO for corporate cases
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.dao.CivilCaseDAO;
import com.example.demo.dao.ClientDAO;
import com.example.demo.dao.CorporateCaseDAO; // DAO for categories
import com.example.demo.dao.CriminalCaseDAO;
import com.example.demo.dao.LawyerDAO;
import com.example.demo.dao.MatrimonialCaseDAO;
import com.example.demo.dao.ParalegalDAO;
import com.example.demo.dao.TaskDAO;
import com.example.demo.model.Category;
import com.example.demo.model.Lawyer;
import com.example.demo.model.Paralegal;
import com.example.demo.model.Task;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskDAO taskDAO; // DAO for tasks

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

    // Display the category selection page
    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Category> categories = categoryDAO.listCategories();
        model.addAttribute("categories", categories);
        return "cattask"; // Name of the Thymeleaf template
    }

    // Handle the category selection
    @PostMapping("/selCategory")
    public String selectCategory(@RequestParam("caseType") String caseType, Model model) {
        // Fetch the CatID from the database using the caseType
        int catID = categoryDAO.getCatIdByCaseType(caseType);
    
        // Create a new task and set the category ID
        Task task = new Task();
        task.setCatId(catID); // Set the catID
    
        // Fetch case IDs based on catID
        List<Integer> caseIds = new ArrayList<>();
        switch (catID) {
            case 1: // Corporate Case
                caseIds = corporateCaseDAO.getCorporateCaseIds();
                break;
            case 3: // Civil Case
                caseIds = civilCaseDAO.getCivilCaseIds();
                break;
            case 4: // Criminal Case
                caseIds = criminalCaseDAO.getCriminalCaseIds();
                break;
            case 2: // Matrimonial Case
                caseIds = matrimonialCaseDAO.getMatrimonialCaseIds();
                break;
            default:
                // Handle default case or invalid catID
                caseIds = new ArrayList<>();
                break;
        }
    
        // Add attributes to the model
        model.addAttribute("catID", catID);
        model.addAttribute("caseIds", caseIds);
        model.addAttribute("task", task); // Add the task object for the form

        // Return the task form view
        return "task"; // Name of the Thymeleaf template for displaying task form
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task") Task task, Model model) {
        // Save the task using DAO
        taskDAO.saveTask(task); // Assuming you have a saveTask method in TaskDAO
        
        return "redirect:/task/all"; // Redirect to the list of tasks
    }

    // List all tasks
    @GetMapping("/all")
    public String listTasks(Model model) {
        List<Task> tasks = taskDAO.listTasks(); // Get all tasks
        model.addAttribute("tasks", tasks);
        return "taskList"; // Thymeleaf template to list tasks
    }

    // Show form to edit an existing task
    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable int id, Model model) {
        Task task = taskDAO.getTaskById(id); // Get task by ID
        model.addAttribute("task", task);

        // Fetch all categories and case IDs to populate dropdowns
        List<Category> categories = categoryDAO.listCategories(); // Method to get categories
        model.addAttribute("categories", categories);
        
        // Fetch case IDs based on the current task's catID
        List<Integer> caseIds = new ArrayList<>();
        switch (task.getCatId()) {
            case 1: // Corporate Case
                caseIds = corporateCaseDAO.getCorporateCaseIds();
                break;
            case 3: // Civil Case
                caseIds = civilCaseDAO.getCivilCaseIds();
                break;
            case 4: // Criminal Case
                caseIds = criminalCaseDAO.getCriminalCaseIds();
                break;
            case 2: // Matrimonial Case
                caseIds = matrimonialCaseDAO.getMatrimonialCaseIds();
                break;
            default:
                caseIds = new ArrayList<>();
                break;
        }

        model.addAttribute("caseIds", caseIds);

        return "editTask"; // Thymeleaf template for editing task
    }

    // Update an existing task
    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task) {
        taskDAO.updateTask(task); // Update task using DAO
        return "redirect:/task/all"; // Redirect to the list of tasks
    }

    // Delete a task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskDAO.deleteTask(id); // Delete task using DAO
        return "redirect:/task/all"; // Redirect to the list of tasks
    }

    @GetMapping("/assignLawyers/{taskId}")
    public String assignLawyers(@PathVariable int taskId, Model model) {
        Task task = taskDAO.getTaskById(taskId); // Fetch the task by ID
        List<Lawyer> lawyers = lawyerDAO.listLawyers(); // Fetch all lawyers
        model.addAttribute("task", task);
        model.addAttribute("lawyers", lawyers);
        return "assignLawyers"; // Thymeleaf template for assigning lawyers
    }

    // Handle the assignment of multiple lawyers to a task
    @PostMapping("/assignLawyers/{taskId}")
    public String saveAssignedLawyers(@PathVariable int taskId, @RequestParam("lawyerIds[]") List<Integer> lawyerIds) {
        // Save the assignment of lawyers to the task
        taskDAO.assignLawyersToTask(taskId, lawyerIds); 
        return "redirect:/task/all"; // Redirect to the task list
    }

    // Show the form to assign paralegals to a task
    @GetMapping("/assignParalegals/{taskId}")
public String assignParalegals(@PathVariable int taskId, Model model) {
    Task task = taskDAO.getTaskById(taskId); // Fetch the task by ID
    List<Paralegal> paralegals = paralegalDAO.listParalegals(); // Fetch all paralegals
    model.addAttribute("task", task);
    model.addAttribute("paralegals", paralegals);
    return "assignParalegals"; // Thymeleaf template for assigning paralegals
}

// Handle the assignment of multiple paralegals to a task
@PostMapping("/assignParalegals/{taskId}")
public String saveAssignedParalegals(@PathVariable int taskId, @RequestParam("paralegalIds[]") List<Integer> paralegalIds) {
    // Save the assignment of paralegals to the task
    taskDAO.assignParalegalsToTask(taskId, paralegalIds); 
    return "redirect:/task/all"; // Redirect to the task list
}
}
