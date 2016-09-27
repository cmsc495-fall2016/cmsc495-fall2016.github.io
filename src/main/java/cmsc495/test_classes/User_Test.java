package cmsc495.test_classes;

import cmsc495.database.User;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tests the User entity as a standalone. Requires users arrive in a .csv. Support is provided for the 3 fields
 * of our test input.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */
public class User_Test {

    File user_test_data = null;

    private class User_CSV_Entry{
        int id = -1;
        String first_name = null;
        String last_name = null;
        String email = null;
        String username = null;
    }

    private ArrayList<User_CSV_Entry> testData = new ArrayList<>();

    /**
     * Reads User information from a test file, populating our testData ArrayList
     * @param file          File containing User information
     * @throws IOException  Standard IOException
     */
    private void populateTestData(File file) throws IOException {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] user;
            reader.readNext(); // discard header line
            while ((user = reader.readNext()) != null) {
                User_CSV_Entry entry = new User_CSV_Entry();
                entry.id = Integer.parseInt(user[0]);
                entry.first_name = user[1];
                entry.last_name = user[2];
                entry.email = user[3];
                entry.username = user[4];
                testData.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Commit our test data to the Ingredients table, deleting all prior Ingredients first, as
     * we are in testing mode and want only this data in the table.
     * @throws SQLException Standard SQL Exception
     */
    private void updateIngredientTable() throws SQLException {
        System.out.println("[!] Deleting all prior ingredient entries (we are in test mode!)");
        User userDelete = new User();
        userDelete.clearUserTable();
        for (User_CSV_Entry entry : testData){
            User u = new User();
            u.createUser(entry.first_name, entry.last_name, entry.email, entry.username);
        }
    }

    /**
     * Reads data from a .csv & populates the appropriate table in our database.
     * @param args          Standard cmdline arguments
     * @throws IOException  Standard IO Exception
     * @throws SQLException Standard SQL Exception
     */
    public static void main( String[] args ) throws IOException, SQLException {
        User_Test ut = new User_Test();
        System.out.println("[!] Begin ingestion of User test_classes data.");
        ut.populateTestData(new File("src/main/java/cmsc495/test_data/user_data.csv"));
        System.out.println("[!] Test data read in; attempting to write to table");
        ut.updateIngredientTable();
        System.out.println("[!] If no stacktrace, assume db user table is populated.");
    }


}
