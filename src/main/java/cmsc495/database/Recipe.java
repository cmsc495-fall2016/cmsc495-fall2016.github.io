package cmsc495.database;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Specifies a Recipe. A Recipe is an index number (used as the primary key for the Recipe database
 * table) and several additional attributes. The Recipe class exposes Create, Read, Update & Delete for the
 * Recipe database table, abstracting the SQLite syntax from other classes in the application.
 *
 * @author  Justin Helphenstine
 * @version 0.1 - 9/24/2016
 */
public class Recipe {
    private Database myDatabase = new Database();

    /** A database connection */
    private Connection connection = null;
    /** A SQLite statement */
    private Statement statement = null;
    private int id = -1;
    private String name = null;
    private String description = null;
    private int serves = -1;
    private String author = null;
    private int prep_time = -1; // question: is this in seconds? minutes?
    private int cook_time = -1; // question: is this in seconds? minutes?
    private int difficulty = -1;

    /**
     *
     * @return  Recipe id #
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return  Recipe Name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return  A description of the intended outcome
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return  # of people served by this Recipe
     */
    public int getServes() {
        return serves;
    }

    /**
     *
     * @return  Recipe author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return  Prep time required for this Recipe
     */
    public int getPrep_time() {
        return prep_time;
    }

    /**
     *
     * @return  Cook time required for this Recipe
     */
    public int getCook_time() {
        return cook_time;
    }

    /**
     *
     * @return  Difficulty level of the Recipe
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     *
     * @return Procedures to create this Recipe
     */
    public String getProcedures() {
        return procedures;
    }

    /**
     *
     * @return  ArrayList of Ingredients used in this Recipe
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    private String procedures = null;
    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

    // Assumption: There is no need for multiple constructors as anyone creating a recipe *must* provide the right
    // number of fields.

    /**
     * Creates a Recipe in the Recipe table.
     * @param name              Recipe name
     * @param serves            How many people will this recipe serve
     * @param author            Recipe provider
     * @param prep_time         Prep time (in units of undetermined time) TODO: Determine units of time
     * @param cook_time         Cook time (in units of undetermined time) TODO: Determine units of time
     * @param difficulty        Difficulty on a 1...n scale
     * @param procedures        Procedures for preparing the recipe
     * @param description       A description of the intended outcome
     * @throws SQLException     Error in case of SQL Exception
     */
    public void createRecipe(String name, int serves, String author, int prep_time, int cook_time,
                              int difficulty, String procedures, String description, ArrayList<Ingredient> ingredients)
            throws SQLException {
        connection = myDatabase.getDatabaseConn();
        //testConnection(connection);
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO recipe (name,serves,author,prep_time,cook_time,difficulty,procedures,description)" +
                        "VALUES(?,?,?,?,?,?,?,?);");
        statement.setString(1,name);
        this.name = name;
        statement.setInt(2,serves);
        this.serves = serves;
        statement.setString(3,author);
        this.author = author;
        statement.setInt(4,prep_time);
        this.prep_time = prep_time;
        statement.setInt(5,cook_time);
        this.cook_time = cook_time;
        statement.setInt(6,difficulty);
        this.difficulty = difficulty;
        statement.setString(7,procedures);
        this.procedures = procedures;
        statement.setString(8,description);
        this.description = description;
        statement.executeUpdate();
        connection.close();
        getRecipeByName(this.name); // update the recipe ID #
        connection = myDatabase.getDatabaseConn();

        for (Ingredient i: ingredients) {
            this.ingredients.add(i);
            // Create a row in the 'uses' table.
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO uses (ingredient_id,recipe_id) VALUES (?,?);");
            stmt.setInt(1,i.getId());
            stmt.setInt(2,this.id);
            stmt.execute();
            stmt.close();
        }
        connection.close();
    }

    /**
     * Get a Recipe by its id number & populate the relevant Recipe attributes; part of the Read functionality
     * @param id                Recipe id number
     * @throws SQLException     Error in case of SQL Exception
     */
    public void getRecipeByNumber(int id) throws SQLException {
        connection = myDatabase.getDatabaseConn();
        //testConnection(connection);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT name,serves,author,prep_time,cook_time,difficulty,procedures,description FROM recipe " +
                "WHERE id = ?;");
        statement.setInt(1,id);
        ResultSet results = statement.executeQuery();
        this.name = results.getString(1);
        this.serves = results.getInt(2);
        this.author = results.getString(3);
        this.prep_time = results.getInt(4);
        this.cook_time = results.getInt(5);
        this.difficulty = results.getInt(6);
        this.procedures = results.getString(7);
        this.description = results.getString(8);
        populateIngredients(id, connection);
        //connection.close();
    }

    /**
     * Get a Recipe by its name & populate the relevant Recipe attributes; part of the Read functionality
     * @param name              Recipe name
     * @throws SQLException     Error in case of SQL Exception
     */
    public void getRecipeByName(String name) throws SQLException {
        connection = myDatabase.getDatabaseConn();
        //testConnection(connection);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id,serves,author,prep_time,cook_time,difficulty,procedures,description FROM recipe " +
                        "WHERE name = ? COLLATE NOCASE;");
        statement.setString(1,name);
        ResultSet results = statement.executeQuery();
        this.id = results.getInt(1);
        this.serves = results.getInt(2);
        this.author = results.getString(3);
        this.prep_time = results.getInt(4);
        this.cook_time = results.getInt(5);
        this.difficulty = results.getInt(6);
        this.procedures = results.getString(7);
        this.description = results.getString(8);
        populateIngredients(this.id, connection);
        //connection.close();
    }

    /**
     * Populate the Recipe's Ingredients list anew.
     * @param id            The Recipe's id number
     * @throws SQLException Standard SQL Exception
     */
    public void populateIngredients(int id, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT ingredient_id FROM uses WHERE recipe_id = ?;");
        statement.setInt(1,id);
        ResultSet results = statement.executeQuery();
        connection.close();
        this.ingredients.clear();
        while(results.next()){
            Ingredient i = new Ingredient();
            i.getIngredientByNumber(results.getInt(1));
            this.ingredients.add(i);
        }
    }

    /**
     * Update a recipe; this way allows us to avoid tricky which-field logic by assuming we are handed everything back
     * that we handed out to begin with, with only relevant changes.
     * @param id            Recipe id number, to identify the record for modification
     * @param name          Recipe name
     * @param serves        How many people this recipe serves
     * @param author        Who provided the recipe
     * @param prep_time     Time required for Recipe prep work TODO: Determine units of time
     * @param cook_time     Time required to cook the Recipe TODO: Determine units of time
     * @param difficulty    Difficulty level from 1...n for this Recipe
     * @param procedures    Procedures to prepare this recipe, 'steps'
     * @param description   A description of the intended outcome
     * @throws SQLException Error in case of SQL Exception
     */
    public void updateRecipe(int id, String name, int serves, String author, int prep_time, int cook_time,
                             int difficulty, String procedures, String description) throws SQLException {
        connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE recipe SET name = ?,serves = ?,author = ?,prep_time = ?,cook_time = ?,difficulty = ?," +
                        "procedures = ?,description =? WHERE id = ?;");
        statement.setString(1,name);
        this.name = name;
        statement.setInt(2,serves);
        this.serves = serves;
        statement.setString(3,author);
        this.author = author;
        statement.setInt(4,prep_time);
        this.prep_time = prep_time;
        statement.setInt(5,cook_time);
        this.cook_time = cook_time;
        statement.setInt(6,difficulty);
        this.difficulty = difficulty;
        statement.setString(7,procedures);
        this.procedures = procedures;
        statement.setString(8,description);
        this.description = description;
        statement.setInt(9,id);
        this.id = id;

        // Verify we have all of the appropriate pairings in the 'uses' table - perhaps by delete / create?
        // That's the 'brute force' way, we can optimize it later.
        // We've deleted all previous references; now create based on the current Ingredients list.
        PreparedStatement stmtDel = connection.prepareStatement("DELETE FROM uses WHERE recipe_id = ?;");
        stmtDel.setInt(1,id);
        stmtDel.execute();
        for (Ingredient i: this.ingredients) {
            PreparedStatement stmtAdd = connection.prepareStatement(
                    "INSERT INTO uses (ingredient_id,recipe_id) VALUES (?,?);");
            stmtAdd.setInt(1,i.getId());
            stmtAdd.setInt(2,id);
            stmtAdd.executeUpdate();
        }
        statement.executeUpdate();
        connection.close();
    }

    /**
     * Delete a Recipe from the Recipe table. DOES NOT ask for confirmation - that's a UI function.
     * @param id            Recipe id number
     * @throws SQLException Error for SQL Exceptions
     */
    public void deleteRecipe(int id) throws SQLException {
        connection = myDatabase.getDatabaseConn();
        //testConnection(connection);
        // First, ensure this ingredient isn't in use by any recipes; must maintain data integrity
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM uses WHERE recipe_id = ?;");
        statement.setInt(1,id);
        ResultSet results = statement.executeQuery();
        // We don't need the same if/else as is in the Ingredient delete; we can keep tracking ingredients even if
        // they're not presently used in any Recipes - as they may be in the future.
        statement = connection.prepareStatement("DELETE FROM recipe WHERE id = ?;");
        statement.setInt(1,id);
        statement.execute();
        // Execute a delete statement from the Uses table for every row with our Recipe id.
        statement = connection.prepareStatement("DELETE FROM uses WHERE recipe_id = ?;");
        statement.setInt(1,id);
        statement.execute();
        connection.close();
    }

    /**
     * Utility method to verify db exists & create it if it doesn't.
     * @param connection    Database connection to test.
     *
    public void testConnection(Connection connection){
        if(connection == null){
            JOptionPane.showMessageDialog(null,"Database not found!");
            Database db = new Database();
            db.createTables();
            return;
        }
    }*/

    /**
     * Main method for development testing & debugging. TODO: Comment this out before moving into production.
     * @param args          Standard command-line arguments
     * @throws SQLException Standard SQL Exception
     */
    public static void main( String args[] ) throws SQLException{
        Recipe test = new Recipe();
        System.out.println("[!] Begin test with Recipe creation.");
        //test.myDatabase.getDatabaseConn().close();
        // Test ingredients
        Ingredient flour = new Ingredient();
        flour.createIngredient("flour", "wheat product used to bake most anything");
        Ingredient otherCookieIngredients = new Ingredient();
        otherCookieIngredients.createIngredient("other cookie stuff", "things used to make chocolate chip cookies");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(flour);
        ingredients.add(otherCookieIngredients);
        test.createRecipe("Cookies", 4, "Justin", 15, 30, 2, "Read directions off of cookie pack", "De-lish chocolate cookies", ingredients);
        System.out.println("[*] Testing getRecipeByName");
        test.getRecipeByName("cookies");
        System.out.println("[*] Serves: " + test.serves);
        System.out.println("[#] Testing updateRecipe to serve 5 instead of 4");
        test.updateRecipe(test.id, "Cookies", 5, "Justin", 15, 30, 2, "Read directions off of cookie pack", "De-lish chocolate cookies");
        System.out.println("[*] Serves: " + test.serves);
        System.out.println("[!] Deleting recipe (unless you commented out the next line)");
        //test.deleteRecipe(test.id); COMMENTED OUT FOR VALIDATION
        System.out.println("[!] Tests complete; check sqlite explorer to verify deletion.");
    }
}