package sharkbread.ui.support;

import java.awt.Container;
import java.io.PrintWriter;
import java.io.StringWriter;

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
   * Method to call a confirmation pop up.
   * @param container Container (or child class) to tie the pop up to could be null
   * @param message The string to display in the middle of the window
   * @return Boolean yes or no
   */
  public static boolean confirm( Container container, String message) {
    int var = JOptionPane.showConfirmDialog(container, message,
        "Please Confirm", JOptionPane.YES_NO_OPTION);
    return (var == JOptionPane.OK_OPTION) ? true : false;
  } //end confirm
  
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
   * An error pop up to show the stack trace.
   * 
   * @param frame Container (or child class) to tie the pop up to could be null
   * @param exception Exception Object to pull the stack trace form
   */
  public static void exception( Container frame, Exception exception ) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    exception.printStackTrace(pw);
    
    error(
        frame,
        "ERROR - StackTrace",
        String.format("Error occurred, contact support\nStackTrace:\n%s",
            sw.toString()
        )
    );
  }
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
