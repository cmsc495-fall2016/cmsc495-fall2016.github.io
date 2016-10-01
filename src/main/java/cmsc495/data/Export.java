/*
* This class handles the exporting of recipes
* @author Claire
*/
package cmsc495.data;

import cmsc495.database.Ingredient;
import cmsc495.database.Recipe;
import org.json.simple.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;


public class Export {
 
    private String path = null;
    
    public Export(){
        try{
            File currentDirectory = new File(new File(".").getCanonicalPath());
            this.path = currentDirectory+File.separator+"export"+File.separator;
        }
        catch (Exception e){
          e.printStackTrace();
        }
    }
    
    public void exportRecipe(String fileName, Recipe inputRecipe){
       //Get all the data fields from the Recipe
       String name = inputRecipe.getName();
       String description = inputRecipe.getDescription();
       int serves = inputRecipe.getServes();
       String author = inputRecipe.getAuthor();
       int prepTime = inputRecipe.getPrep_time();
       int cookTime = inputRecipe.getCook_time();
       int difficulty = inputRecipe.getDifficulty();
       String source = inputRecipe.getSource();
       String procedures = inputRecipe.getProcedures();
       ArrayList<Ingredient> ingredients = inputRecipe.getIngredients();
    
       //Create the json object
       JSONObject recipeObj = new JSONObject();
       recipeObj.put("Name", name);
       recipeObj.put("Description", description);
       recipeObj.put("Serves", serves);
       recipeObj.put("Author", author);
       recipeObj.put("Preparation Time", prepTime);
       recipeObj.put("Cook Time",cookTime);
       recipeObj.put("Difficulty", difficulty);
       recipeObj.put("Source", source);
       recipeObj.put("Procedures", procedures);
       //Handle the ingredients
       JSONArray ingredArray = new JSONArray();
       Iterator<Ingredient> itr = ingredients.iterator();
       while(itr.hasNext()){
           JSONObject ingredObj = new JSONObject();
           Ingredient sample = itr.next();
           String ingredName = sample.getName();
           ingredObj.put("Name", ingredName);
           if (sample.getDescription() != null){
            String ingredDescript = sample.getDescription();
            ingredObj.put("Description", ingredDescript);
           }
           ingredArray.add(ingredObj);
       }
       recipeObj.put("Ingredients", ingredArray);
       //Write the recipe to a file
       try{
           File exported = new File(this.path+fileName);
           exported.createNewFile();
           FileWriter fileWrite = new FileWriter(exported);
           fileWrite.write(recipeObj.toJSONString());
           fileWrite.flush();
           fileWrite.close();
       }
       catch (IOException e) {
            e.printStackTrace();
        }
       
    }
    
        public static void main( String args[] ) throws SQLException {
        Export test = new Export();
        Recipe test2 = new Recipe();
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
        test2.createRecipe("Macaroni and Cheese", 4, "Steve", 5, 15, 2, "Cook noodles.  Combine milk and cheese.", "Kids love this recipe.", "http://www.recip-ez.com/mac", ingredients);
        test.exportRecipe("Test.json", test2);
    }
}
