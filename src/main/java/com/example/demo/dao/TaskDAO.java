package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Task;

import java.sql.Date;
import java.util.List;

@Repository
public class TaskDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Save a new task
    public void saveTask(Task task) {
        String query = "INSERT INTO Task (TaskID, CatID, CaseID, TaskDesc, Status, DueDate) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query,
            task.getTaskId(),
            task.getCatId(),
            task.getCaseId(),
            task.getTaskDesc(),
            task.getStatus(),
            Date.valueOf(task.getDueDate().toLocalDate()) // Assuming dueDate is a LocalDate
        );
    }

    // Get a list of all tasks
    public List<Task> listTasks() {
        String query = "SELECT * FROM Task";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskId(rs.getInt("TaskID"));
            task.setCatId(rs.getInt("CatID"));
            task.setCaseId(rs.getInt("CaseID"));
            task.setTaskDesc(rs.getString("TaskDesc"));
            task.setStatus(rs.getString("Status"));
            task.setDueDate(rs.getDate("DueDate"));
            return task;
        });
    }

    // Get a task by its TaskID
    public Task getTaskById(int taskId) {
        String query = "SELECT * FROM Task WHERE TaskID = ?";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskId(rs.getInt("TaskID"));
            task.setCatId(rs.getInt("CatID"));
            task.setCaseId(rs.getInt("CaseID"));
            task.setTaskDesc(rs.getString("TaskDesc"));
            task.setStatus(rs.getString("Status"));
            task.setDueDate(rs.getDate("DueDate"));
            return task;
        }, taskId);
    }

    // Update a task
    public void updateTask(Task task) {
        String query = "UPDATE Task SET TaskDesc = ?, Status = ?, DueDate = ? WHERE TaskID = ?";
        jdbcTemplate.update(query,
            task.getTaskDesc(),
            task.getStatus(),
            Date.valueOf(task.getDueDate().toLocalDate()), // Assuming dueDate is a LocalDate
            task.getTaskId()
        );
    }

    // Delete a task
    public void deleteTask(int taskId) {
        String query = "DELETE FROM Task WHERE TaskID = ?";
        jdbcTemplate.update(query, taskId);
    }

    // Search for tasks by description
    public List<Task> searchTasks(String keyword) {
        String query = "SELECT * FROM Task WHERE TaskDesc LIKE ?";
        String searchKeyword = "%" + keyword + "%"; // Add wildcards for partial matching
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Task task = new Task();
            task.setTaskId(rs.getInt("TaskID"));
            task.setCatId(rs.getInt("CatID"));
            task.setCaseId(rs.getInt("CaseID"));
            task.setTaskDesc(rs.getString("TaskDesc"));
            task.setStatus(rs.getString("Status"));
            task.setDueDate(rs.getDate("DueDate"));
            return task;
        }, searchKeyword);
    }

    public void assignLawyersToTask(int taskId, List<Integer> lawyerIds) {
        // Remove existing lawyer assignments
        String deleteLawyersQuery = "DELETE FROM Tasklawassigned WHERE TaskID = ?";
        jdbcTemplate.update(deleteLawyersQuery, taskId);

        // Insert new lawyer assignments
        String insertLawyerQuery = "INSERT INTO Tasklawassigned(TaskID,EmpID) VALUES (?,?)";
        for (Integer lawyerId : lawyerIds) {
            jdbcTemplate.update(insertLawyerQuery, taskId,lawyerId);
        }
    }

    // Assign multiple paralegals to a task
    public void assignParalegalsToTask(int taskId, List<Integer> paralegalIds) {
        // Remove existing paralegal assignments
        // String deleteParalegalsQuery = "DELETE FROM Taskassigned WHERE TaskID = ? AND EmployeeType = 'Paralegal'";
        String deleteParalegalsQuery = "DELETE FROM Taskparassigned WHERE TaskID = ?";

        jdbcTemplate.update(deleteParalegalsQuery, taskId);

        // Insert new paralegal assignments
        String insertParalegalQuery = "INSERT INTO Taskparassigned (TaskID, EmpID) VALUES (?,?)";
        for (Integer paralegalId : paralegalIds) {
            jdbcTemplate.update(insertParalegalQuery, taskId, paralegalId);
        }
    }
}
