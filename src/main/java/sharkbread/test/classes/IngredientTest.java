package sharkbread.test.classes;

import com.opencsv.CSVReader;

import sharkbread.database.Ingredient;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tests the Ingredient entity as a standalone. Does not integrate Recipe testing, also does not
 * populate the 'uses' table. Requires ingredients arrive in a .csv. Support is provided for the 3
 * fields of our test input.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */
public class IngredientTest {

  File ingredientTestData = null;

  private class IngredientCsvEntry {
    int id = -1;
    String name = null;
    String description = null;
  }

  private ArrayList<IngredientCsvEntry> testData = new ArrayList<>();

  /**
   * Reads Ingredient information from a test file, populating our testData ArrayList.
   * 
   * @param file File containing Ingredient information
   * @throws IOException Standard IOException
   */
  private void populateTestData(File file) throws IOException {
    CSVReader reader;
    try {
      reader = new CSVReader(new FileReader(file));
      String[] ingredient;
      reader.readNext(); // discard title line
      reader.readNext(); // discard column titles line
      while ((ingredient = reader.readNext()) != null) {
        IngredientCsvEntry entry = new IngredientCsvEntry();
        entry.id = Integer.parseInt(ingredient[0]);
        entry.name = ingredient[1];
        entry.description = ingredient[2];
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
      String[] ingredient;
      reader.readNext(); // discard title line
      reader.readNext(); // discard column titles line
      while ((ingredient = reader.readNext()) != null) {
        IngredientCsvEntry entry = new IngredientCsvEntry();
        entry.id = Integer.parseInt(ingredient[0]);
        entry.name = ingredient[1];
        entry.description = ingredient[2];
        testData.add(entry);
      }
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
    System.out.println("[!] Deleting all prior ingredient entries (we are in test mode!)");
    Ingredient recipeDelete = new Ingredient();
    recipeDelete.clearIngredientTable();
    for (IngredientCsvEntry entry : testData) {
      Ingredient ingredient = new Ingredient();
      ingredient.createIngredient(entry.name, entry.description);
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
    IngredientTest it = new IngredientTest();
    System.out.println("[!] Begin ingestion of Ingredient test_classes data.");
    //it.populateTestData(new File("src/main/java/sharkbread/test/data/ingredient_data.csv"));
    it.getClass().getResourceAsStream("/ingredient_data.csv");
    System.out.println("[!] Test data read in; attempting to write to table");
    it.updateIngredientTable();
    System.out.println("[!] If no stacktrace, assume db ingredient table is populated.");
  }


}
