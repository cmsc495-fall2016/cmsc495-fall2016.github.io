package sharkbread.test.classes;

import com.opencsv.CSVReader;

import sharkbread.database.User;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tests the User entity as a standalone. Requires users arrive in a .csv. Support is provided for
 * the 3 fields of our test input.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */
public class UserTest {

  File userTestData = null;

  private class UserCsvEntry {
    int id = -1;
    String firstName = null;
    String lastName = null;
    String email = null;
    String username = null;
  }

  private ArrayList<UserCsvEntry> testData = new ArrayList<>();

  /**
   * Reads User information from a test file, populating our testData ArrayList.
   * 
   * @param file File containing User information
   * @throws IOException Standard IOException
   */
  private void populateTestData(File file) throws IOException {
    CSVReader reader;
    try {
      reader = new CSVReader(new FileReader(file));
      String[] user;
      reader.readNext(); // discard header line
      while ((user = reader.readNext()) != null) {
        UserCsvEntry entry = new UserCsvEntry();
        entry.id = Integer.parseInt(user[0]);
        entry.firstName = user[1];
        entry.lastName = user[2];
        entry.email = user[3];
        entry.username = user[4];
        testData.add(entry);
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void populateTestData(InputStream inputStream) throws IOException {
    CSVReader reader;
    try {
      reader = new CSVReader(new InputStreamReader(inputStream));
      String[] user;
      reader.readNext(); // discard header line
      while ((user = reader.readNext()) != null) {
        UserCsvEntry entry = new UserCsvEntry();
        entry.id = Integer.parseInt(user[0]);
        entry.firstName = user[1];
        entry.lastName = user[2];
        entry.email = user[3];
        entry.username = user[4];
        testData.add(entry);
      }
      reader.close();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
  
  /**
   * Commit our test data to the Ingredients table, deleting all prior Ingredients first, as we are
   * in testing mode and want only this data in the table.
   * 
   * @throws SQLException Standard SQL Exception
   */
  private void updateIngredientTable() throws SQLException {
    System.out.println("[!] Deleting all prior user entries (we are in test mode!)");
    User user = new User();
    user.clearUserTable();
    for (UserCsvEntry entry : testData) {
      user.createUser(entry.firstName, entry.lastName, entry.email, entry.username);
    }
  }

  /**
   * Reads data from a .csv & populates the appropriate table in our database.
   * 
   * @param args Standard cmdline arguments
   * @throws IOException Standard IO Exception
   * @throws SQLException Standard SQL Exception
   */
  public static void main(String[] args) throws IOException, SQLException {
    UserTest ut = new UserTest();
    System.out.println("[!] Begin ingestion of User test_classes data.");
    //ut.populateTestData(new File("src/main/java/sharkbread/test/data/user_data.csv"));
    ut.populateTestData(ut.getClass().getResourceAsStream("/user_data.csv"));
    System.out.println("[!] Test data read in; attempting to write to table");
    ut.updateIngredientTable();
    System.out.println("[!] If no stacktrace, assume db user table is populated.");
  }


}
