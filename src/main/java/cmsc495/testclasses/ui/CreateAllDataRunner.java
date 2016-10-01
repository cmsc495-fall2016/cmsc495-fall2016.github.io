package cmsc495.testclasses.ui;

import cmsc495.testclasses.AllTest;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Method needed to thread out the All_Test class...it takes too long
 * 
 * @author Adam
 */
class CreateaAllDataRunner implements Runnable {
  AllTest test;

  public CreateaAllDataRunner(AllTest test) {
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
