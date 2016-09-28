package cmsc495.database;

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

    /** A SQLite statement */
    private int id = -1;
    private String name = null;
    private String description = null;
    private int serves = -1;
    private String author = null;
    private int prep_time = -1; // question: is this in seconds? minutes?
    private int cook_time = -1; // question: is this in seconds? minutes?
    private int difficulty = -1;
    private String source = null;

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

    /**
     *
     * @return  Source of this Recipe.
     */
    public String getSource(){ return source; }

    private String procedures = null;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();

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
                              int difficulty, String procedures, String description, String source, ArrayList<Ingredient> ingredients)
            throws SQLException {
        Connection connection = myDatabase.getDatabaseConn();
        //testConnection(connection);
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO recipe (name,serves,author,prep_time,cook_time,difficulty,procedures,description,source)" +
                        "VALUES(?,?,?,?,?,?,?,?,?);");
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
        statement.setString(9,source);
        this.source = source;
        statement.executeUpdate();
        connection.close();
        getRecipeByName(this.name); // update the recipe ID #
        connection = myDatabase.getDatabaseConn();

        if(ingredients != null){ // Necessary in case we have no defined ingredients.
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
        }

        connection.close();
    }

    /**
     * Get a Recipe by its id number & populate the relevant Recipe attributes; part of the Read functionality
     * @param id                Recipe id number
     * @throws SQLException     Error in case of SQL Exception
     */
    public void getRecipeByNumber(int id) throws SQLException {
        getRecipeByCriteria("id", String.valueOf(id));
    }

    /**
     * Get a Recipe by its name & populate the relevant Recipe attributes; part of the Read functionality
     * @param name              Recipe name
     * @throws SQLException     Error in case of SQL Exception
     */
    public void getRecipeByName(String name) throws SQLException {
        getRecipeByCriteria("name", name);
    }

    /**
     * Consolidated method to map a ResultSet to Recipe attributes. Checks to ensure nonzero results.
     * @param criteria      String for either 'id' or 'name'
     * @param term          String for search term
     * @throws SQLException Standard SQL Exception
     */
    private void getRecipeByCriteria(String criteria, String term) throws SQLException {

        Connection connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id,name,serves,author,prep_time,cook_time,difficulty,procedures,description,source FROM recipe " +
                        "WHERE " + criteria + " = ? COLLATE NOCASE;");
        //statement.setString(1,criteria);
        if(criteria.contentEquals("name")){
            statement.setString(1,term);
        }else{
            statement.setInt(1,Integer.parseInt(term));
        }
        try (ResultSet recipeResults = statement.executeQuery()) {
            if (recipeResults.next()) { // else we have no results
                this.id = recipeResults.getInt("id");
                this.name = recipeResults.getString("name");
                this.serves = recipeResults.getInt("serves");
                this.author = recipeResults.getString("author");
                this.prep_time = recipeResults.getInt("prep_time");
                this.cook_time = recipeResults.getInt("cook_time");
                this.difficulty = recipeResults.getInt("difficulty");
                this.procedures = recipeResults.getString("procedures");
                this.description = recipeResults.getString("description");
                this.source = recipeResults.getString("source");
                connection.close();
                populateIngredients(this.id);
            }
        }
    }

    /**
     * Populate the Recipe's Ingredients list anew.
     * @param id            The Recipe's id number
     * @throws SQLException Standard SQL Exception
     */
    private void populateIngredients(int id) throws SQLException {
        Connection connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement("SELECT ingredient_id FROM uses WHERE recipe_id = ?;");
        statement.setInt(1,id);
        ResultSet ingredientResults = statement.executeQuery();
        this.ingredients.clear();
        if(ingredientResults.next()) { // else no Ingredients.
            while (ingredientResults.next()) {
                Ingredient i = new Ingredient();
                i.getIngredientByNumber(ingredientResults.getInt(1));
                this.ingredients.add(i);
            }
        }
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
                             int difficulty, String procedures, String description, String source) throws SQLException {
        Connection connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE recipe SET name = ?,serves = ?,author = ?,prep_time = ?,cook_time = ?,difficulty = ?," +
                        "procedures = ?,description =?,source=? WHERE id = ?;");
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
        statement.setString(9,source);
        this.source = source;
        statement.setInt(10,id);
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
        Connection connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM recipe WHERE id = ?;");
        statement.setInt(1,id);
        statement.execute();
        // Execute a delete statement from the Uses table for every row with our Recipe id.
        statement = connection.prepareStatement("DELETE FROM uses WHERE recipe_id = ?;");
        statement.setInt(1,id);
        statement.execute();
        connection.close();
    }

    /**
     * Utility method to support testing. TODO: Comment out before moving to production.
     * @throws SQLException Standard SQL Exception
     */
    public void clearRecipeTable() throws SQLException {
        Connection connection = myDatabase.getDatabaseConn();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM recipe;");
        statement.execute();
        connection.close();
    }

    /**
     * Main method for development testing & debugging. TODO: Comment this out before moving into production.
     * @param args          Standard command-line arguments
     * @throws SQLException Standard SQL Exception
     */
    public static void main( String args[] ) throws SQLException{
        Recipe test = new Recipe();
        System.out.println("[!] Begin test_classes with Recipe creation.");
        //test_classes.myDatabase.getDatabaseConn().close();
        // Test ingredients
        Ingredient flour = new Ingredient();
        flour.createIngredient("flour", "wheat product used to bake most anything");
        Ingredient otherCookieIngredients = new Ingredient();
        otherCookieIngredients.createIngredient("other cookie stuff", "things used to make chocolate chip cookies");
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(flour);
        ingredients.add(otherCookieIngredients);
        test.createRecipe("Cookies", 4, "Justin", 15, 30, 2, "Read directions off of cookie pack", "De-lish chocolate cookies", "http://www.recip-ez.com/cookies", ingredients);
        System.out.println("[*] Testing getRecipeByName");
        test.getRecipeByName("cookies");
        System.out.println("[*] Serves: " + test.serves);
        System.out.println("[#] Testing updateRecipe to serve 5 instead of 4");
        test.updateRecipe(test.id, "Cookies", 5, "Justin", 15, 30, 2, "Read directions off of cookie pack", "De-lish chocolate cookies", "http://www.recip-ez.com/cookies");
        System.out.println("[*] Serves: " + test.serves);
        System.out.println("[!] Deleting recipe (unless you commented out the next line)");
        //test_classes.deleteRecipe(test_classes.id); COMMENTED OUT FOR VALIDATION
        System.out.println("[!] Tests complete; check sqlite explorer to verify deletion.");
    }
}