/**
 * Wrapper class to call the GUI
 */

package cmsc495;

import cmsc495.ui.SimpleGui;

/**
 * The main bootstrap class.
 * 
 * @author Eliot Pearson
 * @date   2016-09-19
 */
public class Main {

  /**
   * The main method.
   * 
   * @param args - command line args.
   */
  public static void main(String[] args) {

    SimpleGui gui = new SimpleGui();
    gui.setVisible(true);
    
  }
  
}