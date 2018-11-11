package pl.coderslab.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
    private Integer id = 0;
    private String name = "";

    public void saveToDB(Connection conn) throws SQLException {        //create and update
        if (this.id == 0) {
            String sql = "INSERT INTO groups(name) VALUES (?)";
            String[] generatedColumns = {"id"};
            PreparedStatement prepStat = conn.prepareStatement(sql, generatedColumns);
            prepStat.setString(1, this.name);
            prepStat.executeUpdate();
            ResultSet rs = prepStat.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE groups SET name=? where id = ?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, this.name);
            prepStat.setInt(2, this.id);
            prepStat.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM groups WHERE id=?";
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setInt(1, this.id);
            prepStat.executeUpdate();
            this.id = 0;
        }
    }

    static public Group loadGroupById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM groups where id=?";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setInt(1, id);
        ResultSet resultSet = prepStat.executeQuery();
        if (resultSet.next()) {
            Group loadedGroup = new Group();
            loadedGroup.id = resultSet.getInt("id");
            loadedGroup.name = resultSet.getString("name");
            return loadedGroup;
        }
        return null;
    }

    static public Group[] loadAllGroups(Connection conn) throws SQLException {
        ArrayList<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM groups";
        PreparedStatement prepStat = conn.prepareStatement(sql);
        ResultSet resultSet = prepStat.executeQuery();
        while (resultSet.next()) {
            Group loadedGroup = new Group();
            loadedGroup.id = resultSet.getInt("id");
            loadedGroup.name = resultSet.getString("name");
            groups.add(loadedGroup);
        }
        Group[] gArray = new Group[groups.size()];
        gArray = groups.toArray(gArray);
        return gArray;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
