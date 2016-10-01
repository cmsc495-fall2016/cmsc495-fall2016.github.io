package cmsc495.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates and provides connectivity to the project's database, implementing the schema as specified
 * in the ERD.
 * 
 * @author Claire, Justin
 * @version 1.0
 */
public class Database {
  private String path = null;
  private Connection databaseConn = null;
  private Statement stmt = null;
  private boolean verbose = false;

  /**
   * Constructor that sets verbose debugging Constructor that creates the recipe database & executes
   * the schema.
   * 
   * @param verbose boolean flag to prints lots of stuff
   */
  public Database(boolean verbose) {
    this();
    this.verbose = verbose;
  }

  /**
   * Constructor that creates the recipe database & executes the schema.
   */

  public Database() {
    /*
     * Try to connect to the database, or create it if it doesn't exist
     */
    try {
      // The database path should be cmsc495-fall2016.github.io/database/
      File currentDirectory = new File(new File(".").getCanonicalPath());
      this.path = "jdbc:sqlite:" + currentDirectory + File.separator + "database" 
          + File.separator + "recipe.db";
      this.databaseConn = DriverManager.getConnection(path);
      createTables(); // Create if not exists
      if (verbose) {
        System.out.println("Successfully connected");
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }

  }

  /**
   * Returns a connection string. Used by entity wrappers.
   *
   * @return A database connection
   */
  public Connection getDatabaseConn() {
    try {
      return DriverManager.getConnection(path);
    } catch (SQLException exception) {
      exception.printStackTrace();
      return null;
    }
  }

  /**
   * This method creates the tables as specified in the project ERD
   * 
   * @param verbose Prints all kinds of System.out information
   */
  public void createTables(boolean verbose) {
    this.verbose = verbose;
    createTables();
  }

  /**
   * This method creates the tables as specified in the project ERD.
   */
  public void createTables() {
    try {

      stmt = this.databaseConn.createStatement();

      String createIngredient = "CREATE TABLE IF NOT EXISTS ingredient " 
          + "(id INTEGER PRIMARY KEY  NOT NULL, "
          + " name TEXT NOT NULL, " + " description TEXT)";
      stmt.executeUpdate(createIngredient);
      if (verbose) {
        System.out.println("Created Ingredient table");
      }

      String createRecipe = "CREATE TABLE IF NOT EXISTS recipe "
          + "(id INTEGER PRIMARY KEY NOT NULL,"
          + "name text NOT NULL," + "serves INTEGER," + "author text,"
          + "prep_time INTEGER," + "cook_time INTEGER,"
          + "difficulty INTEGER," + "procedures text," + "description text," + "source text)";
      stmt.executeUpdate(createRecipe);
      if (verbose) {
        System.out.println("Created recipe table");
      }

      String createUses = "CREATE TABLE IF NOT EXISTS uses " + "(id INTEGER PRIMARY KEY NOT NULL,"
          + "ingredient_id INTEGER NOT NULL," + "recipe_id INTEGER NOT NULL,"
          + "FOREIGN KEY(ingredient_id) REFERENCES ingredient(id)," 
          + "FOREIGN KEY(recipe_id) REFERENCES recipe(id))";
      stmt.executeUpdate(createUses);
      if (verbose) {
        System.out.println("Created Uses table");
      }

      String createUserTable = "CREATE TABLE IF NOT EXISTS user " 
          + "(id INTEGER PRIMARY KEY NOT NULL, "
          + "user_name TEXT," + "first_name TEXT," + "last_name TEXT, " + "email_address TEXT)";
      stmt.executeUpdate(createUserTable);

      stmt.close();
      this.databaseConn.close();

    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Utility method to support testing. TODO: Comment out before moving to production. The 'uses'
   * table does not have its own entity, so its utility delete is stored in the parent Database
   * class.
   * 
   * @throws SQLException Standard SQL Exception
   */
  public void clearUsesTable() throws SQLException {
    Connection connection = this.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement("DELETE FROM uses;");
    statement.execute();
    this.databaseConn.close();
  }

  /**
   * Utility method to support testing. TODO: Comment out before moving to production. The 'uses'
   * table does not have its own entity, so its utility method for creating entries is stored in the
   * parent Database class.
   * 
   * @param ingredient ID # of the Ingredient
   * @param recipe ID # of the Recipe
   * @throws SQLException Standard SQL Exception
   */
  public void createUsesEntry(int ingredient, int recipe) throws SQLException {
    Connection connection = this.getDatabaseConn();
    PreparedStatement stmtAdd = connection.prepareStatement(
        "INSERT INTO uses (ingredient_id,recipe_id) VALUES (?,?);");
    stmtAdd.setInt(1, ingredient);
    stmtAdd.setInt(2, recipe);
    stmtAdd.executeUpdate();
    this.databaseConn.close();
  }

  /**
   * Test method for development & debugging. TODO: Comment out before moving to production.
   * 
   * @param args Standard command-line arguments
   */
  public static void main(String[] args) {
    Database test = new Database();
    test.createTables();
  }
}

