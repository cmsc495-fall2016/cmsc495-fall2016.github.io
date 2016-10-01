/*
* This class handles the importing of recipes
* @author Claire
*/
package cmsc495.data;

import cmsc495.database.Ingredient;
import cmsc495.database.Recipe;
import org.json.simple.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.parser.JSONParser;

public class Import {
    private String path = null;
    
    public Import(){
            try{
            File currentDirectory = new File(new File(".").getCanonicalPath());
            this.path = currentDirectory+File.separator+"import"+File.separator;
        }
        catch (Exception e){
          e.printStackTrace();
        }
}
    
    public void importRecipe(String filename){
        //Read in the JSON file
        JSONParser parser = new JSONParser();
        try{
            FileReader reader = new FileReader(this.path+filename);
            Object input = parser.parse(reader);
            JSONObject recipeObj = (JSONObject) input;
            
            //Process the fields in the JSON file
            String name = (String) recipeObj.get("Name");
            String description = (String) recipeObj.get("Description");
            String author = (String) recipeObj.get("Author");
            int serves = (int) (long) recipeObj.get("Serves");
            int prepTime = (int) (long) recipeObj.get("Preparation Time");
            int cookTime = (int) (long) recipeObj.get("Cook Time");
            int difficulty = (int) (long) recipeObj.get("Difficulty");
            String source = (String) recipeObj.get("Source");
            String procedures = (String) recipeObj.get("Procedures");
            JSONArray ingredArray= (JSONArray)recipeObj.get("Ingredients");
            ArrayList<Ingredient> ingredList = new ArrayList();
            //Process ingredient array
            Iterator itr = ingredArray.iterator();
            while(itr.hasNext()){
                JSONObject ingredObj = (JSONObject)itr.next();
                String ingredName = (String)ingredObj.get("Name");
                Ingredient sample = new Ingredient();
                String ingredDescript = "";
                ingredDescript = (String) ingredObj.get("Description");
                sample.createIngredient(ingredName, ingredDescript);
                ingredList.add(sample);      
            }
            //Now create recipe
            Recipe imported = new Recipe();
            imported.createRecipe(name, serves, author, prepTime, cookTime, difficulty, procedures, description, source, ingredList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main( String args[] ) {
        Import test = new Import();
        test.importRecipe("Test.json");
        Import test2 = new Import();
        test2.importRecipe("CrazyRecipe.json");
    }
}
