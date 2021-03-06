package sharkbread.test.classes.ui;

import sharkbread.test.classes.AllTest;

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
    } catch (SQLException | IOException exception) {
      exception.printStackTrace();
    }
    System.out.println("---------\nCreateAllData Thread Actions Complete");
  }

}
