/**
 * Class to create the database and tables as specified in the ERD
 * 
 */
package cmsc495.database;

import java.sql.*;

/**
 *
 * @author Claire 
 */
public class Database {
    private String path = "jdbc:sqlite:recipe.db";
    private Connection databaseConn = null;
    private Statement stmt = null;
    /**
     * Constructor that creates the recipe database
     */
    public Database(){

        /**
         * Try to connect to the database, or create it if it doesn't exist
         */
        try{
            this.databaseConn = DriverManager.getConnection(path);
            //System.out.println("Successfully connected");
        }
        catch (Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        
    }

    /**
     * Returns a connection string. Used by entity wrappers.
     *
     * @return  A database connection
     */
    public Connection getDatabaseConn(){
        try{
            Connection connection = DriverManager.getConnection(path);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method creates the tables as specified in the 
     * project ERD
     */
    public void createTables(){
        try{

            stmt = this.databaseConn.createStatement();
            
            String createIngredient = "CREATE TABLE IF NOT EXISTS ingredient " + 
                                      "(id INTEGER PRIMARY KEY  NOT NULL, " +
                                        " name TEXT NOT NULL, "+
                                        " description TEXT)";
            stmt.executeUpdate(createIngredient);
            //System.out.println("Created Ingredient table");

            String createRecipe = "CREATE TABLE IF NOT EXISTS recipe " + 
                                  "(id INTEGER PRIMARY KEY NOT NULL," +
                                  "name text NOT NULL,"+
                                  "serves INTEGER"+
                                  "author text" +
                                  "prep_time INTEGER"+
                                  "cook_time INTEGER"+
                                  "difficulty INTEGER"+
                                  "procedures text"+
                                  "description text)";
            stmt.executeUpdate(createRecipe);
            //System.out.println("Created recipe table");
            
            String createUses = "CREATE TABLE IF NOT EXISTS uses " + 
                                "(id INTEGER PRIMARY KEY NOT NULL," +
                                "ingredient_id INTEGER NOT NULL,"+
                                "recipe_id INTEGER NOT NULL,"+
                        "FOREIGN KEY(ingredient_id) REFERENCES ingredient(id),"+
                                "FOREIGN KEY(recipe_id) REFERENCES recipe(id))";
            stmt.executeUpdate(createUses);
            //System.out.println("Created Uses table");
            
            stmt.close();
            this.databaseConn.close();
            
        }
        catch (SQLException e){
            System.out.println(e.getClass().getName()+e.getMessage()+":"+e.getSQLState());
        }
    }
   
    public static void main( String args[] )
    {
        Database test = new Database();
        test.createTables();
    }
}

