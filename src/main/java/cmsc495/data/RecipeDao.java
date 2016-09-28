package cmsc495.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cmsc495.database.Database;
import cmsc495.database.Recipe;

public class RecipeDao implements SimpleDao<Recipe> {
  private static Database database;

  // EP: Using Database as a factory.
  public RecipeDao() {
    database = new Database();
  }

  @Override
  public void create(Recipe item) {
    // TODO Auto-generated method stub

  }

  @Override
  public Recipe read(int index) {
    // TODO: EP fix this to be more efficient.  Right
    // all recipes are pulled from the data and we do
    // filtering in the method.
    Recipe found = null;
    
    List<Recipe> recipes = getAll();
    
    for(Recipe recipe: recipes) {
      if(recipe.getId() == index) {
        found = recipe;
      }
    }
    
    
    return found;
    
  }

  @Override
  public void update(Recipe item) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(int index) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Recipe> getAll() {
    List<Recipe> results = new ArrayList<>();
    try {
      database.getDatabaseConn();

      Connection connection = database.getDatabaseConn();
      PreparedStatement statement = connection.prepareStatement(
          "SELECT id,name,serves,author,prep_time,cook_time,difficulty,procedures,description,source FROM recipe");

      ResultSet recipeResults = statement.executeQuery();
      while (recipeResults.next()) {
        Recipe recipe = new Recipe();

        recipe.setId(recipeResults.getInt("id"));
        recipe.setName(recipeResults.getString("name"));
        recipe.setServes(recipeResults.getInt("serves"));
        recipe.setAuthor(recipeResults.getString("author"));
        recipe.setPrep_time(recipeResults.getInt("prep_time"));
        recipe.setCook_time(recipeResults.getInt("cook_time"));
        recipe.setDifficulty(recipeResults.getInt("difficulty"));
        recipe.setProcedures(recipeResults.getString("procedures"));
        recipe.setDescription(recipeResults.getString("description"));
        recipe.setSource(recipeResults.getString("source"));

        results.add(recipe);

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return results;
  }
}
