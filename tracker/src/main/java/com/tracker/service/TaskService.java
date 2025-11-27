package com.tracker.service;

import java.util.List;

import com.tracker.dao.TaskDAO;
import com.tracker.model.TaskModel;

public class TaskService {
    private TaskDAO dao = new TaskDAO();
    public int createTask(String title,String description) throws Exception {
        if(title == null || title.trim().isEmpty()){
            throw new Exception("Title Should not be null");
        }
        TaskModel task = new TaskModel(title,description);
        return dao.createTask(task);
    }

    public List<TaskModel> getAllTasks(){
        return dao.getAllTasks();
    }

    public List<TaskModel> getTasksByStatus(String status) throws Exception{
        return dao.getTasksByStatus(status);
    }

    public TaskModel getById(int id) throws Exception {
        return dao.getByID(id);
    }

    public boolean updateTask(int id,String status) throws Exception{
        return dao.updateTask(id,status);
    }

    public boolean deleteTask(int id) throws Exception{
        return dao.deleteTask(id);
    }
}
