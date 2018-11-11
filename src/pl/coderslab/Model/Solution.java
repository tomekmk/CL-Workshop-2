package pl.coderslab.Model;

import java.sql.*;
import java.util.ArrayList;

public class Solution {
    private Integer id = 0;
    private Timestamp created = null;
    private Timestamp updated = null;
    private String description = "";
    private Integer exercise_id = 0;
    private Integer user_id = 0;

    public void saveToDB(Connection conn) throws SQLException {        //create and update
        if (this.id == 0) {
            String sql = "INSERT INTO solutions(description, exercise_id, user_id) VALUES (?, ?, ?)";
            String[] generatedColumns = {"id"};
            PreparedStatement prepStat = conn.prepareStatement(sql, generatedColumns);
            prepStat.setString(1, this.description);
            prepStat.setInt(2, this.exercise_id);
            prepStat.setInt(3, this.user_id);
            prepStat.executeUpdate();
            ResultSet rs = prepStat.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE solutions SET updated=current_timestamp, description=?, exercise_id=?, user_id=? where id = ?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, this.description);
            prepStat.setInt(2, this.exercise_id);
            prepStat.setInt(3, this.user_id);
            prepStat.setInt(4, this.id);
            prepStat.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM solutions WHERE id=?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setInt(1, this.id);
            prepStat.executeUpdate();
            this.id = 0;
        }
    }

    static public Solution loadSolutionById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM solutions where id=?";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(1, id);
        ResultSet resultSet = prepStat.executeQuery();
        if (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exercise_id = resultSet.getInt("exercise_id");
            loadedSolution.user_id = resultSet.getInt("user_id");
            return loadedSolution;
        }
        return null;
    }

    static public Solution[] loadAllSolutions(Connection conn, Integer user_id) throws SQLException {
        String sql = "";
        if (user_id == null)
            sql = "SELECT * FROM solutions ORDER BY updated DESC";
        else
            sql = "SELECT * FROM solutions WHERE user_id = ? ORDER BY updated DESC";

        ArrayList<Solution> solutions = new ArrayList<>();
        PreparedStatement prepStat = conn.prepareStatement(sql);
        if (user_id != null)
            prepStat.setInt(1, user_id);
        ResultSet resultSet = prepStat.executeQuery();

        while (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exercise_id = resultSet.getInt("exercise_id");
            loadedSolution.user_id = resultSet.getInt("user_id");
            solutions.add(loadedSolution);
        }
        Solution[] sArray = new Solution[solutions.size()];
        sArray = solutions.toArray(sArray);
        return sArray;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExercise_id(Integer exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }


    public Integer getId() {
        return id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public String getDescription() {
        return description;
    }

    public Integer getExercise_id() {
        return exercise_id;
    }

    public Integer getUser_id() {
        return user_id;
    }
}

