package pl.coderslab.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {
    private Integer id = 0;
    private String title = "";
    private String description = "";

    public void saveToDB(Connection conn) throws SQLException {        //create and update
        if (this.id == 0) {
            String sql = "INSERT INTO exercises(title, description) VALUES (?, ?)";
            String[] generatedColumns = {"id"};
            PreparedStatement prepStat = conn.prepareStatement(sql, generatedColumns);
            prepStat.setString(1, this.title);
            prepStat.setString(2, this.description);
            prepStat.executeUpdate();
            ResultSet rs = prepStat.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE exercises SET title=?, description=? where id = ?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, this.title);
            prepStat.setString(2, this.description);
            prepStat.setInt(3, this.id);
            prepStat.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM exercises WHERE id=?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setInt(1, this.id);
            prepStat.executeUpdate();
            this.id = 0;
        }
    }

    static public Exercise loadExerciseById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM exercises where id=?";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(1, id);
        ResultSet resultSet = prepStat.executeQuery();
        if (resultSet.next()) {
            Exercise loadedExercise = new Exercise();
            loadedExercise.id = resultSet.getInt("id");
            loadedExercise.title = resultSet.getString("title");
            loadedExercise.description = resultSet.getString("description");
            return loadedExercise;
        }
        return null;
    }

    static public Exercise[] loadAllExercises(Connection conn) throws SQLException {
        String sql = "SELECT * FROM exercises";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        return loadService(prepStat);
    }

    static public Exercise[] loadMissingUserExercises(Connection conn, Integer user_id) throws SQLException {
        String sql = "SELECT * FROM exercises\n" +
                "WHERE id NOT IN (SELECT exercise_id FROM solutions WHERE user_id = ?)";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(1, user_id);
        return loadService(prepStat);
    }

    static private Exercise[] loadService(PreparedStatement prepStat) throws SQLException {
        ArrayList<Exercise> exercises = new ArrayList<>();
        ResultSet resultSet = prepStat.executeQuery();
        while (resultSet.next()) {
            Exercise loadedExercise = new Exercise();
            loadedExercise.id = resultSet.getInt("id");
            loadedExercise.title = resultSet.getString("title");
            loadedExercise.description = resultSet.getString("description");
            exercises.add(loadedExercise);
        }
        Exercise[] eArray = new Exercise[exercises.size()];
        eArray = exercises.toArray(eArray);
        return eArray;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
