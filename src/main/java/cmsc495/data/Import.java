/*
 * This class handles the importing of recipes
 * 
 * @author Claire
 */
package cmsc495.data;

import cmsc495.database.Ingredient;
import cmsc495.database.Recipe;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Import {
  private String path = null;
  public Recipe imported;

  /**
   * Constructor method via relative path.
   */
  public Import() {
    try {
      File currentDirectory = new File(new File(".").getCanonicalPath());
      this.path = currentDirectory + File.separator + "import" + File.separator;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Constructor method via File Object.
   * @param file File object to read from
   */
  public Import(File file) throws Exception{
    createDbRecipe(file);
  }
  
  /**
   * Method to insert a recipe into the database.
   * @param file File object to read from
   */
  private void createDbRecipe(File file) throws Exception{
    // Read in the JSON file
      FileReader reader = new FileReader(file);
      JSONParser parser = new JSONParser();
      Object input = parser.parse(reader);
      JSONObject recipeObj = (JSONObject) input;

      // Process the fields in the JSON file
      String name = (String) recipeObj.get("Name");
      String description = (String) recipeObj.get("Description");
      String author = (String) recipeObj.get("Author");
      int serves = (int) (long) recipeObj.get("Serves");
      int prepTime = (int) (long) recipeObj.get("Preparation Time");
      int cookTime = (int) (long) recipeObj.get("Cook Time");
      int difficulty = (int) (long) recipeObj.get("Difficulty");
      String source = (String) recipeObj.get("Source");
      String procedures = (String) recipeObj.get("Procedures");
      JSONArray ingredArray = (JSONArray) recipeObj.get("Ingredients");
      ArrayList<Ingredient> ingredList = new ArrayList<Ingredient>();
      // Process ingredient array
      Iterator<JSONObject> itr = ingredArray.iterator();
      while (itr.hasNext()) {
        JSONObject ingredObj = itr.next();
        String ingredName = (String) ingredObj.get("Name");
        Ingredient sample = new Ingredient();
        String ingredDescript = "";
        ingredDescript = (String) ingredObj.get("Description");
        sample.createIngredient(ingredName, ingredDescript);
        ingredList.add(sample);
      }
      // Now create recipe
      imported = new Recipe();
      imported.createRecipe(name, serves, author, prepTime, cookTime, difficulty, procedures,
          description, source, ingredList);
  } //end createDbRecipe
  
  /**
   * Method to import a recipe from a File Object
   * @param file File object to import from 
   */
  public void importRecipe(File file) throws Exception {
    createDbRecipe(file);
  } //end importRecipe File
  
  /**
   * Method to import a recipe
   * @param filename String object to infer relative File object path 
   */
  public void importRecipe(String filename) throws Exception {
    File reader = new File(this.path + filename);
    createDbRecipe(reader);   
  }

  /**
   * Main method to allow testing
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    Import test = new Import();
    try {
      test.importRecipe("Test.json");
    } catch (Exception execption) {
      execption.printStackTrace();
    }
    //Import test2 = new Import();
    //test2.importRecipe("CrazyRecipe.json");
  }
}
