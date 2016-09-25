package cmsc495.database;

import javax.swing.*;
import java.sql.*;

/**
 * Specifies a Recipe. A Recipe is an index number (used as the primary key for the Recipe database
 * table) and several additional attributes. The Recipe class exposes Create, Read, Update & Delete for the
 * Recipe database table, abstracting the SQLite syntax from other classes in the application.
 *
 * @author  Justin Helphenstine
 * @version 0.1 - 9/24/2016
 */
public class Recipe {
    Database myDatabase = new Database();

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
    private String procedures = null;

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
                              int difficulty, String procedures, String description) throws SQLException {
        connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO recipe (name,serves,author,prep_time,cook_time,difficulty,procedures,description)" +
                        "VALUES(?,?,?,?,?,?,?,?);");
        statement.setString(1,name);
        statement.setInt(2,serves);
        statement.setString(3,author);
        statement.setInt(4,prep_time);
        statement.setInt(5,cook_time);
        statement.setInt(6,difficulty);
        statement.setString(7,procedures);
        statement.setString(8,description);
        statement.executeUpdate();
        connection.close();
    }

    /**
     * Get a Recipe by its id number & populate the relevant Recipe attributes; part of the Read functionality
     * @param id                Recipe id number
     * @throws SQLException     Error in case of SQL Exception
     */
    public void getRecipeByNumber(int id) throws SQLException {
        connection = myDatabase.getDatabaseConn();
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
        connection.close();
    }

    /**
     * Get a Recipe by its name & populate the relevant Recipe attributes; part of the Read functionality
     * @param name              Recipe name
     * @throws SQLException     Error in case of SQL Exception
     */
    public void getRecipeByName(String name) throws SQLException {
        connection = myDatabase.getDatabaseConn();
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
        connection.close();
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
        statement.setInt(2,serves);
        statement.setString(3,author);
        statement.setInt(4,prep_time);
        statement.setInt(5,cook_time);
        statement.setInt(6,difficulty);
        statement.setString(7,procedures);
        statement.setString(8,description);
        statement.setInt(9,id);
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
        // First, ensure this ingredient isn't in use by any recipes; must maintain data integrity
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM uses WHERE recipe_id = ?;");
        statement.setInt(1,id);
        ResultSet results = statement.executeQuery();
        // We don't need the same if/else as is in the Ingredient delete; we can keep tracking ingredients even if
        // they're not presently used in any Recipes - as they may be in the future.
        statement = connection.prepareStatement("DELETE FROM recipe WHERE id = ?;");
        statement.setInt(1,id);
        statement.execute();
        connection.close();
    }

    //TODO: Write a test main method...
}
