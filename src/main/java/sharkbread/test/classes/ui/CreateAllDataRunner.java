package sharkbread.test.classes.ui;

import sharkbread.test.classes.AllTest;
import sharkbread.test.classes.RecipeTest;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Method needed to thread out the All_Test class...it takes too long
 * 
 * @author Adam
 */
public class CreateAllDataRunner implements Runnable {
  AllTest test;

  public CreateAllDataRunner(AllTest test) {
    this.test = test;
  }

  /**
   * The run method.
   */
  @Override
  public void run() {
    try {
      test.createaAllData(null);
      RecipeTest rt = new RecipeTest();
      
//      System.out.println("[!] Begin ingestion of Recipe test_classes data.");
//      rt.populateTestData(
//          new File("recipe_data_optionals_removed.csv")
//      );
//      System.out.println("[!] Test data read in; attempting to write to table");
//      rt.updateRecipeTable();
      
    } catch (SQLException | IOException exception) {
      exception.printStackTrace();
    }
    System.out.println("---------\nCreateAllData Thread Actions Complete");
  }

}
