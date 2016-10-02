package cmsc495.data;

import cmsc495.database.Ingredient;
import cmsc495.database.Recipe;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class handles the exporting of recipes.
 * 
 * @author Claire
 */
public class Export {

  private String path = null;
  
  /**
   * Default constructor.
   */
  public Export() {
    try {
      File currentDirectory = new File(new File(".").getCanonicalPath());
      this.path = currentDirectory + File.separator ;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  
  /**
   * Constructor to allow immediate outputting of a recipe. 
   * @param file File object to ouput to
   * @param recipe Recipe object 
   */
  public Export(File file, Recipe recipe) {
    JSONObject jsonObject = createJsonFromRecipe(recipe);
    writeJsonObject(jsonObject,file);
  }

  /**
   * Method to create the JSON Object needed for output.
   * @param recipe Recipe to feed into the JSON Object
   * @return JSONObject 
   */
  public JSONObject createJsonFromRecipe(Recipe recipe) {
    // Create the json object
    JSONObject recipeObj = new JSONObject();
    recipeObj.put("Name", recipe.getName());
    recipeObj.put("Description", recipe.getDescription());
    recipeObj.put("Serves", recipe.getServes());
    recipeObj.put("Author", recipe.getAuthor());
    recipeObj.put("Preparation Time", recipe.getPrep_time());
    recipeObj.put("Cook Time", recipe.getCook_time());
    recipeObj.put("Difficulty", recipe.getDifficulty());
    recipeObj.put("Source", recipe.getSource());
    recipeObj.put("Procedures", recipe.getProcedures());
    // Handle the ingredients
    JSONArray ingredArray = new JSONArray();
    Iterator<Ingredient> itr = recipe.getIngredients().iterator();
    while (itr.hasNext()) {
      JSONObject ingredObj = new JSONObject();
      Ingredient sample = itr.next();
      String ingredName = sample.getName();
      ingredObj.put("Name", ingredName);
      if (sample.getDescription() != null) {
        String ingredDescript = sample.getDescription();
        ingredObj.put("Description", ingredDescript);
      }
      ingredArray.add(ingredObj);
    }
    recipeObj.put("Ingredients", ingredArray);
    return recipeObj;
  }
  
  /**
   * Method to export a Recipe object with a relative path.
   * @param fileName String path based upon the root of the project
   * @param recipe Recipe object to output
   */
  public void exportRecipe(String fileName, Recipe recipe) {
    // Create the JSON object
    JSONObject jsonObject = createJsonFromRecipe(recipe);
    writeJsonObject(jsonObject,fileName);
  }

  /**
   * Method to write out the JSON object with a relative path.
   * @param jsonObject JSONObject to write out
   * @param fileName String representing a path to output
   */
  private void writeJsonObject(JSONObject jsonObject, String fileName) {
    File exported = new File(this.path + fileName);
    writeJsonObject(jsonObject, exported);
  } // end write jsconObject relative path

  /**
   * Method to output a JSON Object.
   * @param jsonObject JSONObject to ouput
   * @param file File object to output to
   */
  public void writeJsonObject(JSONObject jsonObject, File file) {
    try {
      file.createNewFile();
      FileWriter fileWrite = new FileWriter(file);
      fileWrite.write(jsonObject.toJSONString());
      fileWrite.flush();
      fileWrite.close();
    } catch (IOException exception) {
      exception.printStackTrace();
    } //end try/catch
  } // end write jsconObject absolute path
  
  
  /**
   * Main method to allow testing.
   * @param args Command line string arguments
   * @throws SQLException SQL module execptions
   */
  public static void main(String[] args) throws SQLException {
    // Create the needed recipe elements
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    Ingredient milk = new Ingredient();
    milk.createIngredient("Milk");
    ingredients.add(milk);
    Ingredient cheese = new Ingredient();
    cheese.createIngredient("Cheese", "A nice cheddar");
    ingredients.add(cheese);
    Ingredient noodles = new Ingredient();
    noodles.createIngredient("Noodles", "Elbow macaroni");
    ingredients.add(noodles);
    
    // Create the recipe
    Recipe test2 = new Recipe();
    test2.createRecipe("Macaroni and Cheese", 4, "Steve", 5, 15, 2,
        "Cook noodles.  Combine milk and cheese.", "Kids love this recipe.",
        "http://www.recip-ez.com/mac", ingredients);

    // Export the recipe
    new Export().exportRecipe("Test.json", test2);
    File file = new File("Test2.json");
    new Export(file, test2);
  }
}
