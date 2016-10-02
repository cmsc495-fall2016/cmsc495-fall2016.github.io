package cmsc495.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 * Specifies an Ingredient. An Ingredient is an index number (used as the primary key for the
 * Ingredient database table), a Name, and an optional Description. The Ingredient class exposes
 * Create, Read, Update & Delete for the Ingredient database table, abstracting the SQLite syntax
 * from other classes in the application.
 *
 * @author Justin Helphenstine
 * @version 0.1 - 9/24/2016
 */
public class Ingredient {
  private Database myDatabase = new Database();

  private Connection connection = null;
  private Statement statement = null;
  private int id = -1;
  private String name = null;
  private String description = null;

  /**
   * Gets id of the ingredient.
   *
   * @return Ingredient id
   */
  public int getId() {
    return id;
  }



  /**
   * Creates an Ingredient with a name.
   * 
   * @param name A string containing the Ingredient's name
   * @throws SQLException Standard SQL Exception
   */
  public void createIngredient(String name) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("INSERT OR IGNORE INTO ingredient (name) VALUES(?)");
    statement.setString(1, name);
    this.name = name;
    statement.executeUpdate();
    connection.close();
    getIngredientByName(name); // Set the id #
  }

  /**
   * Creates an Ingredient with both name and description.
   * 
   * @param name A string containing the Ingredient's name
   * @param description A String describing the Ingredient
   * @throws SQLException Standard SQL Exception
   */
  public void createIngredient(String name, String description) throws SQLException {
    this.name = name;
    this.description = description;
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("INSERT OR IGNORE INTO ingredient (name,description) VALUES (?,?)");
    statement.setString(1, name);
    this.name = name;
    statement.setString(2, description);
    this.description = description;
    statement.executeUpdate();
    connection.close();
    getIngredientByName(name); // Set the id #
  }

  /**
   * Translates the ingredient's name into a number. One of the 'Read' functions for Ingredient;
   * populates the Ingredient's id & description fields.
   * 
   * @param name Ingredient name
   * @throws SQLException Standard SQL Exception
   */
  public void getIngredientByName(String name) throws SQLException {
    getIngredientByCriteria("name", name);
  }

  /**
   * Translates the ingredient's number into a name. One of the 'Read' functions for Ingredient;
   * populates the Ingredient's name & description fields.
   * 
   * @param id Ingredient id number
   * @throws SQLException Standard SQL Exception
   */
  public void getIngredientByNumber(int id) throws SQLException {
    getIngredientByCriteria("id", String.valueOf(id));
  }

  /**
   * Retrieves Ingredient information based on search criteria. Populates the Ingredient's id, name
   * & description.
   * 
   * @param criteria Search criteria (id or name)
   * @param term Search term
   * @throws SQLException Standard SQL Exception
   */
  private void getIngredientByCriteria(String criteria, String term) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement(
            "SELECT id,name,description FROM ingredient " + "WHERE " + criteria + " = ?;");

    if (criteria.contentEquals("name")) {
      statement.setString(1, term);
    } else {
      statement.setInt(1, Integer.parseInt(term));
    }
    try (ResultSet results = statement.executeQuery()) {
      if (results.next()) { // else no results
        this.id = results.getInt("id");
        this.name = results.getString("name");
        this.description = results.getString("description");
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    connection.close();
  }

  /**
   * Update an ingredient's name.
   * 
   * @param id Ingredient id number
   * @param newName Ingredient new name
   * @throws SQLException Standard SQL Exception
   */
  public void updateIngredient(int id, String newName) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("UPDATE ingredient SET name = ? WHERE id = ?;");
    statement.setString(1, newName);
    statement.setInt(2, id);
    statement.executeUpdate();
    this.name = newName;
    connection.close();
  }

  /**
   * Update an ingredient's description.
   * 
   * @param id Ingredient id number
   * @param name Ingredient name, provided for constructor discrimination
   * @param newDescription Ingredient new description
   * @throws SQLException Standard SQL Exception
   */
  public void updateIngredient(int id, String name, String newDescription) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("UPDATE ingredient SET description = ? WHERE id = ?;");
    statement.setString(1, newDescription);
    statement.setInt(2, id);
    statement.executeUpdate();
    this.description = newDescription;
    connection.close();
  }

  /**
   * Deletes an ingredient from the Ingredient table after ensuring the Ingredient isn't in use by a
   * Recipe. DOES NOT prompt for confirmation - that's a UI function. Displays an alert if the
   * Ingredient is used by a Recipe.
   * 
   * @param id Ingredient id number
   * @throws SQLException Standard SQL Exception
   */
  public void deleteIngredient(int id) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    // First, ensure this ingredient isn't in use by any recipes; must maintain data integrity
    PreparedStatement statement = 
        connection.prepareStatement("SELECT * FROM uses WHERE ingredient_id = ?;");
    statement.setInt(1, id);
    ResultSet results = statement.executeQuery();
    if (!results.isClosed()) {
      JOptionPane.showMessageDialog(null, 
          "This ingredient is in use by at least one recipe and cannot be deleted.");
    } else {
      statement = connection.prepareStatement("DELETE FROM ingredient WHERE id = ?;");
      statement.setInt(1, id);
      statement.execute();
    }
    connection.close();
  }

  /**
   * Getter method for the Ingredient name.
   * 
   * @return Ingredient name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter method for the Ingredient description.
   * 
   * @return Ingredient description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Utility method to support testing. TODO: Comment out before moving to production.
   * 
   * @throws SQLException Standard SQL Exception
   */
  public void clearIngredientTable() throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement("DELETE FROM ingredient;");
    statement.execute();
    connection.close();
  }


  /**
   * Debugging test_classes method; TODO: Comment out before production.
   * 
   * @param args Default command-line arguments
   * @throws SQLException Standard SQL Exception
   */
  public static void main(String[] args) throws SQLException {
    System.out.println("[!] Begin Ingredient Test.");
    Ingredient test = new Ingredient();
    test.createIngredient("Paprika");
    test.getIngredientByName("Paprika");
    System.out.println("[#] Number is: " + test.id);
    System.out.println("[*] Name is: " + test.name);
    test.createIngredient("Paprika", "Spice");
    test.getIngredientByName("Paprika");
    System.out.println("[#] Number is: " + test.id);
    System.out.println("[*] Name is: " + test.name);
    test.createIngredient("Paprika");
    test.getIngredientByName("Paprika");
    System.out.println("[#] Number is: " + test.id);
    System.out.println("[*] Name is: " + test.name);
    System.out.println("[*] Testing updateIngredient");
    test.updateIngredient(test.id, "Turmeric");
    test.getIngredientByNumber(test.id);
    System.out.println("[*] Name is: " + test.name);
    test.deleteIngredient(test.id);
    System.out.println("[!] Test complete.");
  }
}
