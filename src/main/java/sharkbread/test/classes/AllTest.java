package sharkbread.test.classes;


import sharkbread.database.Database;
import sharkbread.database.Ingredient;
import sharkbread.database.Recipe;
import sharkbread.database.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Brings together all 4 tables' test classes, allowing for creation & deletion based on what is
 * commented out.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */

public class AllTest {

  private Database database = new Database();
  private Recipe recipe = new Recipe();
  private Ingredient ingredient = new Ingredient();
  private User user = new User();

  /**
   * Populate the database with test information.
   * 
   * @throws SQLException Standard SQL Exception
   * @throws IOException Standard IO Exception
   */
  public void createaAllData(String[] args) throws SQLException, IOException {
    RecipeTest.main(args);
    IngredientTest.main(args);
    UserTest.main(args);
    UsesTest.main(args);
  }

  /**
   * Delete all data from the database.
   * 
   * @throws SQLException Standard SQL Exception
   */
  public void deleteAllData() throws SQLException {
    System.out.println("Deleting all data from Recipe, Ingredient, User, Uses tables");
    //database.clearUsesTable();
    recipe.clearRecipeTable();
    //ingredient.clearIngredientTable();
    //user.clearUserTable();
  }

  /**
   * Menu-driven test application. Offers the options to populate the database with our test
   * information, as well as clear out the tables (to leave the database clean).
   * 
   * @param args Standard command-line arguments
   * @throws IOException Standard IO Exception
   * @throws SQLException Standard SQL Exception
   */
  public static void main(String[] args) throws IOException, SQLException {
    AllTest at = new AllTest();
    System.out.println("[*] Recipe Repository Test Suite");
    System.out.println("[*] Please select one of our menu options:");
    System.out.println("[1] Populate the database with test information");
    System.out.println("[2] Clear the database of test information");
    System.out.println("[3] Quit");
    Scanner scanner = new Scanner(System.in);
    String choice = scanner.next();
    while (!(choice.contentEquals("3"))) {
      while (!((choice.contentEquals("1") | (choice.contentEquals("2") 
          | (choice.contentEquals("3")))))) {
        System.out.print("[>] Choice (1/2/3): ");
        choice = scanner.next();
      }
      int choiceAsInt = Integer.parseInt(choice);
      switch (choiceAsInt) {
        case (1): {
          at.createaAllData(args);
          choice = "";
          continue;
        }
        case (2): {
          at.deleteAllData();
          choice = "";
          continue;
        }
        case (3): {
          System.out.println("[!] User terminated; quitting.");
          return;
        }
        default: {
          choice = "";
        }
      }
    }
    scanner.close();
  }

}
