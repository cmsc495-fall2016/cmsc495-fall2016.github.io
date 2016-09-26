/**
 * Wrapper class to call the GUI
 */
package cmsc495;

import cmsc495.database.Database;
import cmsc495.ui.SimpleGui;

/**
 * @author Eliot Pearson
 * @date   2016-09-19
 */
public class Main {

  public static void main(String[] args) {
    SimpleGui gui = new SimpleGui();
    gui.setVisible(true);
    
    //Create database and tables
    Database recipe = new Database();
  }
  
}//end class Main
