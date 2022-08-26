import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserMapper {

    DBConnector dbConnector;

    public UserMapper(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public ArrayList<String> listUsersByNames() {
        ArrayList<String> list = new ArrayList<>();

        String sql = "SELECT fname FROM usertable";

        try (Connection connection = dbConnector.connection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    String name = rs.getString("fname");
                    list.add(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getUser(int id) {
        User user = null;

        String sql = "SELECT fname, lname, phone, address FROM usertable WHERE id = ?";
        try (Connection connection = dbConnector.connection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    user = new User(fname, lname, phone, address);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User editUserData(int id, String property, String newValue) {
        User user = null;
        String sql = "";

        if(property.equals("password")) {
            sql = "UPDATE usertable SET pw = ? WHERE id = ?";
        } else if (property.equals("phone")) {
            sql = "UPDATE usertable SET phone = ? WHERE id = ?";
        } else if (property.equals("address")) {
            sql = "UPDATE usertable SET address = ? WHERE id = ?";
        }
        try (Connection connection = dbConnector.connection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, newValue);
                ps.setInt(2, id);
                ps.executeUpdate();

                user = getUser(id);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
}
