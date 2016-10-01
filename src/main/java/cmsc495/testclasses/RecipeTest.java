package cmsc495.testclasses;

import cmsc495.database.Recipe;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tests the Recipe entity as a standalone. Does not integrate Ingredient testing, also does not
 * populate the 'uses' table. Requires recipes arrive in a .csv. Support is provided for the 10
 * fields used by our test_classes inputs.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */
public class RecipeTest {

  File recipe_test_data = null;


  private class Recipe_CSV_Entry {
    int id = -1;
    String name = null;
    int serves = -1;
    String author = null;
    String prep_time_as_String = null;
    int prep_time = 0;
    String cook_time_as_String = null;
    int cook_time = 0;
    int difficulty = 0;
    String procedures = null;
    String description = null;
    String source = null;
  }

  private ArrayList<Recipe_CSV_Entry> testData = new ArrayList<>();

  /**
   * Utility method used to convert times to minutes for standardization. Admittedly janky
   * algorithm.
   * 
   * @param time A string containing units of time, e.g. 2.5 hrs, 12 minutes
   * @return an integer expressing the given time in minutes
   */
  private int ensureTimeInMinutes(String time) {
    String[] time_elements;

    if (time.contentEquals("")) {
      return 0;
    }
    if (time.matches(".+(?<=\\d)(?=\\D).+")) { // WHY DOESN'T THIS MATCH ON "10minutes"
      time_elements = time.split("(?<=\\d)(?=\\D)|(?=\\d)(?<=\\D)");
      System.out.println(time_elements[0] + " | " + time_elements[1]);
    } else {
      time_elements = time.split(" ");
      System.out.println("DEBUG: " + time_elements[0]);
      System.out.println(time_elements[0] + " | " + time_elements[1]);
    }

    int minutes = Integer.parseInt(time_elements[0]);
    if (time.toLowerCase().contains("min")) {
      minutes *= 60;
    }
    return minutes;
  }

  /**
   * Get test_classes data from the .csv; normalize prep / cooking times, create our ArrayList of
   * Recipe elements.
   * 
   * @param file File containing our Recipe test data
   * @throws IOException Standard IOException
   */
  private void populateTestData(File file) throws IOException {

    CSVReader reader;
    try {
      reader = new CSVReader(new FileReader(file));
      String[] recipe;
      reader.readNext(); // Get rid of the header line
      while ((recipe = reader.readNext()) != null) {
        Recipe_CSV_Entry entry = new Recipe_CSV_Entry();
        entry.id = Integer.parseInt(recipe[0]);
        entry.name = recipe[1];
        entry.serves = Integer.parseInt(recipe[2].split(" ")[0]); // in case our Serves field has
                                                                  // non-nummeric data.
        entry.author = recipe[3];
        entry.prep_time_as_String = recipe[4];
        entry.prep_time = ensureTimeInMinutes(entry.prep_time_as_String);
        entry.cook_time_as_String = recipe[5];
        entry.cook_time = ensureTimeInMinutes(entry.cook_time_as_String);
        if (recipe[6].contentEquals("")) {
          entry.difficulty = 0;
        } else {
          entry.difficulty = Integer.parseInt(recipe[6]);
        }
        entry.procedures = recipe[7];
        entry.description = recipe[8];
        entry.source = recipe[9];

        testData.add(entry);
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }


  /**
   * Store our test_classes data into the Recipe table of our database.
   * 
   * @throws SQLException Standard SQL Exception
   */
  private void updateRecipeTable() throws SQLException {
    System.out.println("[!] Deleting all prior recipe entries (we are in test mode!)");
    Recipe recipeDelete = new Recipe();
    recipeDelete.clearRecipeTable();
    for (Recipe_CSV_Entry entry : testData) {
      Recipe r = new Recipe();
      r.createRecipe(entry.name, entry.serves, entry.author, entry.prep_time, entry.cook_time, entry.difficulty,
          entry.procedures, entry.description, entry.source, null);
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
    RecipeTest rt = new RecipeTest();
    System.out.println("[!] Begin ingestion of Recipe test_classes data.");
    rt.populateTestData(new File("src/main/java/cmsc495/test_data/recipe_data_optionals_removed.csv"));
    System.out.println("[!] Test data read in; attempting to write to table");
    rt.updateRecipeTable();
    System.out.println("[!] If no stacktrace, assume db recipe table is populated.");
  }
}
