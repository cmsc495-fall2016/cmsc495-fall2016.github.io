package sharkbread.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Specifies a User. A user is an index number (used as the primary key for the Ingredient database
 * table), a user_name, first_name, last_name, and an email address. The user class exposes Create,
 * Read, Update & Delete for the user database table, abstracting the SQLite syntax from other
 * classes in the application.
 *
 * @author Claire Breer
 * @version 0.1 - 9/24/2016
 */

public class User {

  Database myDatabase = new Database();

  private Connection connection = null;
  //private Statement statement = null;
  private int id = -1;
  private String userName = null;
  private String firstName = null;
  private String lastName = null;
  private String emailAddress = null;

  /**
   * Creates a user with a user_name, first_name, last_name.
   * 
   * @param userName A string containing the user name
   * @param firstName A string containing the first name
   * @param lastName A string containing the last name
   * @throws SQLException Standard SQL Exception
   */
  public void createUser(String userName, String firstName, String lastName) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement =
        connection.prepareStatement(
            "INSERT INTO user (user_name, first_name, last_name) VALUES(?,?,?)");
    statement.setString(1, userName);
    statement.setString(2, firstName);
    statement.setString(3, lastName);
    statement.executeUpdate();
    connection.close();
  }

  /**
   * Creates a user with a user_name, first_name, last_name, email_address.
   * 
   * @param userName A string containing the user name
   * @param firstName A string containing the first name
   * @param lastName A string containing the last name
   * @param emailAddress A string containing the email address
   * @throws SQLException Standard SQL Exception
   */
  public void createUser(String userName, String firstName, String lastName, String emailAddress)
      throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement("INSERT INTO " 
            + "user (user_name, first_name, last_name, email_address) VALUES(?,?,?,?)");
    statement.setString(1, userName);
    statement.setString(2, firstName);
    statement.setString(3, lastName);
    statement.setString(4, emailAddress);
    statement.executeUpdate();
    connection.close();
  }

  /**
   * Translates the user name into a number. One of the 'Read' functions for user; populates the
   * user's id & first_name, last_name, and email_address fields.
   * 
   * @param userName User's user name
   * @return User's id number
   * @throws SQLException Standard SQL Exception
   */
  public int getUserByName(String userName) throws SQLException {
    getUserByCriteria("user_name", userName);
    return this.id;
  }

  /**
   * Translates the user's number into a user_name. One of the 'Read' functions for User; populates
   * the user's id & first_name, last_name, and email_address fields.
   * 
   * @param id User id number
   * @return User name
   * @throws SQLException Standard SQL Exception
   */
  public String getUserByNumber(int id) throws SQLException {
    getUserByCriteria("id", String.valueOf(id));
    return this.userName;
  }

  private void getUserByCriteria(String criteria, String term) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement(
        "SELECT " + "id,first_name,last_name,email_address,user_name FROM user " 
        + "WHERE " + criteria + " = ?;");
    if (criteria.contentEquals("name")) {
      statement.setString(1, term);
    } else {
      statement.setInt(1, Integer.parseInt(term));
    }
    ResultSet results = statement.executeQuery();
    if (!results.isClosed()) {
      this.id = results.getInt(1);
      this.firstName = results.getString(2);
      this.lastName = results.getString(3);
      this.emailAddress = results.getString(4);
      this.userName = results.getString(5);
    }
    connection.close();
  }

  /**
   * Update a user's user_name.
   * 
   * @param id Ingredient id number
   * @param newUserName Ingredient new name
   * @throws SQLException Standard SQL Exception
   */
  public void updateUser(int id, String newUserName) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("UPDATE " + "user SET user_name = ? WHERE id = ?;");
    statement.setString(1, newUserName);
    statement.setInt(2, id);
    statement.executeUpdate();
    this.userName = newUserName;
    connection.close();
  }

  /**
   * Update an user's first name.
   * 
   * @param id User id number
   * @param firstName User name, provided for constructor discrimination
   * @param newFirstName User new first name
   * @throws SQLException Standard SQL Exception
   */
  public void updateUser(int id, String firstName, String newFirstName) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("UPDATE " + "user SET first_name = ? WHERE id = ?;");
    statement.setString(1, newFirstName);
    statement.setInt(2, id);
    statement.executeUpdate();
    this.firstName = newFirstName;
    connection.close();
  }

  /**
   * Update an user's first name.
   * 
   * @param id User id number
   * @param userName Username
   * @param firstName User first name, provided for constructor discrimination
   * @param lastName User last name, provided for constructor discrimination
   * @param emailAddress User email address
   * @param newLastName User new last name
   * @throws SQLException Standard SQL Exception
   */
  public void updateUser(int id, String userName, String firstName, String lastName, 
      String newLastName, String emailAddress) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("UPDATE " + "user SET last_name = ? WHERE id = ?;");
    statement.setString(1, newLastName);
    statement.setInt(2, id);
    statement.executeUpdate();
    this.firstName = newLastName;
    connection.close();
  }

  /**
   * Update an user's email address.
   * 
   * @param id User id number
   * @param userName User user name, provided for constructor discrimination
   * @param firstName User first name, provided for constructor discrimination
   * @param lastName User last name, provided for constructor discrimination
   * @param newEmailAddress User new email address
   * @throws SQLException Standard SQL Exception
   */
  public void updateUser(
      int id, String userName, String firstName, String lastName, String newEmailAddress)
      throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement(
        "UPDATE " + "user SET email_address = ? WHERE id = ?;");
    statement.setString(1, newEmailAddress);
    statement.setInt(2, id);
    statement.executeUpdate();
    this.emailAddress = newEmailAddress;
    connection.close();
  }

  /**
   * Deletes a user from the User table after ensuring the User isn't in use by a Recipe. Displays
   * an alert if the User is used by a Recipe.
   * 
   * @param id User id number
   * @throws SQLException Standard SQL Exception
   */
  public void deleteUser(int id) throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = 
        connection.prepareStatement("SELECT * FROM uses WHERE ingredient_id = ?;");
    statement.setInt(1, id);
    statement = connection.prepareStatement("DELETE FROM user WHERE id = ?;");
    statement.setInt(1, id);
    statement.execute();
    connection.close();
  }

  /**
   * Getter method for the User name.
   * 
   * @return User user_name
   */
  public String getUser_name() {
    return userName;
  }

  /**
   * Getter method for the User first name.
   * 
   * @return User first_name
   */
  public String getFirst_name() {
    return firstName;
  }

  /**
   * Getter method for the User last name.
   * 
   * @return User last_name
   */
  public String getLast_name() {
    return lastName;
  }

  /**
   * Getter method for the User email address.
   * 
   * @return User email_address
   */
  public String getEmail_address() {
    return emailAddress;
  }

  /**
   * Utility method to support testing. TODO: Comment out before moving to production.
   * 
   * @throws SQLException Standard SQL Exception
   */
  public void clearUserTable() throws SQLException {
    connection = myDatabase.getDatabaseConn();
    PreparedStatement statement = connection.prepareStatement("DELETE FROM user;");
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
    System.out.println("[!] Begin User Test.");
    User test = new User();
    test.createUser("JohnD", "John", "Doe");
    test.createUser("SusanA", "Susan", "Anderson", "susan@test_classes.com");
    test.getUserByName("JohnD");
    System.out.println("[#] Number is: " + test.id);
    System.out.println("[*] User Name is: " + test.userName);
    System.out.println("[*] First Name is: " + test.firstName);
    System.out.println("[*] Last Name is: " + test.lastName);
    System.out.println("[*] Email address is: " + test.emailAddress);
    System.out.println("[*] Testing updateUser");
    test.updateUser(test.id, "MarkS");
    test.updateUser(test.id, "John", "Joe");
    test.updateUser(test.id, "Joe", "Doe", "Strong", "joes@test_classes.com");
    test.updateUser(test.id, "Joe", "Strong", "", "joes@test_classes.com");
    test.getUserByNumber(test.id);
    System.out.println("[*] User Name is: " + test.userName);
    System.out.println("[*] First Name is: " + test.firstName);
    System.out.println("[*] Last Name is: " + test.lastName);
    System.out.println("[*] Email address is: " + test.emailAddress);
    test.deleteUser(test.id);
    test.getUserByName("SusanA");
    System.out.println("[#] Number is: " + test.id);
    System.out.println("[*] User Name is: " + test.userName);
    System.out.println("[*] First Name is: " + test.firstName);
    System.out.println("[*] Last Name is: " + test.lastName);
    System.out.println("[*] Email address is: " + test.emailAddress);
    System.out.println("[!] Test complete.");
  }

}
