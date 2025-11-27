package com.tracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tracker.model.TaskModel;
import com.tracker.util.DBConnection;
import com.tracker.util.Log;

public class TaskDAO {
    private Log log=new Log();
    public int createTask(TaskModel task){

        String sql = "INSERT INTO TASKS (title,description,status) VALUES (?,?,?)";

        try(
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());

            ps.executeUpdate();

            try(ResultSet rs=ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<TaskModel> getAllTasks(){
        ArrayList<TaskModel> tasks = new ArrayList<>();

        String sql = "SELECT id,title,description,status from tasks";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ){
            while(rs.next()){
                TaskModel task = new TaskModel(
                    rs.getInt("id"),rs.getString("title"),
                    rs.getString("description"),rs.getString("status")
                );
                tasks.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<TaskModel> getTasksByStatus(String status) throws Exception{
        String sql ="SELECT id,title,description,status FROM TASKS WHERE status=?";
        List<TaskModel> list = new ArrayList<>();
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, status);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    TaskModel t= new TaskModel(
                        rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getString("status")
                    );
                    list.add(t);
                }
            }
        } 
        return list;
    }

    public TaskModel getByID(int id) throws Exception{

        String sql = "SELECT id,title,description,status FROM TASKS WHERE ID=?";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()){
                    return new TaskModel(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status")
                    );
                }
            }
        }

        return null;
    }

    public boolean deleteTask(int id) throws Exception {
        String sql = "DELETE FROM TASKS WHERE id=?";

        try( Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setInt(1, id);
            int num = ps.executeUpdate();
            return (num>0);
        }
    }

    public boolean updateTask(int id,String status)throws Exception{
        String sql = "UPDATE TASKS SET status=? WHERE id=?";
        try( Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, status);ps.setInt(2, id);
            int row = ps.executeUpdate();
            return row>0;
        }
    }
}
