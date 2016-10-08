package sharkbread.test.classes;

import com.opencsv.CSVReader;

import sharkbread.database.Recipe;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

  File recipeTestData = null;


  private class RecipeCsvEntry {
    int id = -1;
    String name = null;
    int serves = -1;
    String author = null;
    String prepTimeAsString = null;
    int prepTime = 0;
    String cookTimeAsString = null;
    int cookTime = 0;
    int difficulty = 0;
    String procedures = null;
    String description = null;
    String source = null;
  }

  private ArrayList<RecipeCsvEntry> testData = new ArrayList<>();

  /**
   * Utility method used to convert times to minutes for standardization. Admittedly janky
   * algorithm.
   * 
   * @param time A string containing units of time, e.g. 2.5 hrs, 12 minutes
   * @return an integer expressing the given time in minutes
   */
  private int ensureTimeInMinutes(String time) {
    String[] timeElements;

    if (time.contentEquals("")) {
      return 0;
    }
    if (time.matches(".+(?<=\\d)(?=\\D).+")) { // WHY DOESN'T THIS MATCH ON "10minutes"
      timeElements = time.split("(?<=\\d)(?=\\D)|(?=\\d)(?<=\\D)");
      System.out.println(timeElements[0] + " | " + timeElements[1]);
    } else {
      timeElements = time.split(" ");
      System.out.println("DEBUG: " + timeElements[0]);
      System.out.println(timeElements[0] + " | " + timeElements[1]);
    }

    int minutes = Integer.parseInt(timeElements[0]);
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
  public void populateTestData(File file) throws IOException {

    CSVReader reader;
    try {
      reader = new CSVReader(new FileReader(file));
      String[] recipe;
      reader.readNext(); // Get rid of the header line
      while ((recipe = reader.readNext()) != null) {
        RecipeCsvEntry entry = new RecipeCsvEntry();
        entry.id = Integer.parseInt(recipe[0]);
        entry.name = recipe[1];
        
        // in case our Serves field has non-nummeric data.
        entry.serves = Integer.parseInt(recipe[2].split(" ")[0]);
        entry.author = recipe[3];
        entry.prepTimeAsString = recipe[4];
        entry.prepTime = ensureTimeInMinutes(entry.prepTimeAsString);
        entry.cookTimeAsString = recipe[5];
        entry.cookTime = ensureTimeInMinutes(entry.cookTimeAsString);
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
  
  public void populateTestData(InputStream inputStream) {
    
    CSVReader reader;
    try {
      reader = new CSVReader(new InputStreamReader(inputStream));
      String[] recipe;
      reader.readNext(); // Get rid of the header line
      while ((recipe = reader.readNext()) != null) {
        RecipeCsvEntry entry = new RecipeCsvEntry();
        entry.id = Integer.parseInt(recipe[0]);
        entry.name = recipe[1];
        
        // in case our Serves field has non-nummeric data.
        entry.serves = Integer.parseInt(recipe[2].split(" ")[0]);
        entry.author = recipe[3];
        entry.prepTimeAsString = recipe[4];
        entry.prepTime = ensureTimeInMinutes(entry.prepTimeAsString);
        entry.cookTimeAsString = recipe[5];
        entry.cookTime = ensureTimeInMinutes(entry.cookTimeAsString);
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
  public void updateRecipeTable() throws SQLException {
    System.out.println("[!] Deleting all prior recipe entries (we are in test mode!)");
    Recipe recipeDelete = new Recipe();
    recipeDelete.clearRecipeTable();
    for (RecipeCsvEntry entry : testData) {
      Recipe recipe = new Recipe();
      recipe.createRecipe(
          entry.name, entry.serves, entry.author, entry.prepTime, entry.cookTime, entry.difficulty,
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
    rt.populateTestData(
        rt.getClass().getResourceAsStream("/recipe_data_optionals_removed.csv")
    );
    System.out.println("[!] Test data read in; attempting to write to table");
    rt.updateRecipeTable();
    System.out.println("[!] If no stacktrace, assume db recipe table is populated.");
  }
}
