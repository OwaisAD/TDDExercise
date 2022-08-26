import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTestClass {

    private DBConnector dbConnector;

    @BeforeEach
    void setUp() {
        System.out.println("!!!!TESTING!!!!");
        Connection con = null;
        try {
            con = DBConnector.connection();

            String clearTable = "DROP TABLE IF EXISTS usertable";
            con.prepareStatement(clearTable).executeUpdate();

            String createTable = "CREATE TABLE IF NOT EXISTS `startcode_test`.`usertable` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `fname` VARCHAR(45) NULL,\n" +
                    "  `lname` VARCHAR(45) NULL,\n" +
                    "  `pw` VARCHAR(45) NULL,\n" +
                    "  `phone` VARCHAR(45) NULL,\n" +
                    "  `address` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`id`));";
            con.prepareStatement(createTable).executeUpdate();



            String SQL = "INSERT INTO startcode_test.usertable (fname, lname, pw, phone, address) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "Hans");
            ps.setString(2, "Hansen");
            ps.setString(3, "Hemmelig123");
            ps.setString(4, "40404040");
            ps.setString(5,"Rolighedsvej 3");
            ps.executeUpdate();

            ps.setString(1, "Elon");
            ps.setString(2, "Musk");
            ps.setString(3, "Hollywood");
            ps.setString(4, "12345678");
            ps.setString(5,"A Blvd. 123");
            ps.executeUpdate();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void asAUserICanSeeAListOfAllUsersOnTheSystemByTheirNamesOnly() {
        System.out.println("Testing if a user can get a list of names for all users in the system");
        UserMapper userMapper = new UserMapper(dbConnector);
        ArrayList<String> actual = userMapper.listUsersByNames();
        //ArrayList<String> expected = (ArrayList<String>) Arrays.asList("Hans");
        assertEquals("Hans", actual.get(0));
        assertEquals("Elon", actual.get(1));
        assertEquals(2, actual.size());
    }

    @Test
    public void getSpecificUserDetails() {
        System.out.println("Testing seeing if a user can get a specific user data");
        UserMapper userMapper = new UserMapper(dbConnector);
        User actual = userMapper.getUser(2);
        User expected = new User("Elon", "Musk", "Hollywood", "12345678","A Blvd. 123");
        assertEquals(expected, actual);
    }

    @Test
    public void testingEditingAUserData() {
        UserMapper userMapper = new UserMapper(dbConnector);
        User actual = userMapper.editUserData(2, "phone", "12121212");
        User expected = new User("Elon", "Musk", "Hollywood", "12121212","A Blvd. 123");
        assertEquals(expected, actual);
    }
}
