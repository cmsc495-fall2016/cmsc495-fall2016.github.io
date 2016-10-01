package cmsc495.ui;

import java.awt.Container;

import javax.swing.JOptionPane;

/**
 * Class to contain the static method of creating pop ups. Essentially, these methods are interfaces
 * to JOptionPanes
 * 
 * @author Adam
 * @date 2016-09-24
 */
public class PopUp {
  /**
   * An error pop up... has an error picture
   * 
   * @param frame Container (or child class) to tie the pop up to could be null
   * @param title The string to display on the window
   * @param message The string to display in the middle of the window
   */
  public static void error( Container frame,String title,String message ) {
    JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
  } // end Error

  /**
   * An warning pop up... has an warning picture
   * 
   * @param frame Container (or child class) to tie the pop up to could be null
   * @param title The string to display on the window
   * @param message The string to display in the middle of the window
   */
  public static void warning( Container frame, String title, String message ) {
    JOptionPane.showMessageDialog(frame, message, title, JOptionPane.WARNING_MESSAGE);
  } // end Warning
}
