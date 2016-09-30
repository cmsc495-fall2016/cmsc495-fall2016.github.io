/**
 * Class to contain the static method of creating pop ups
 * 
 * Essentially, these methods are interfaces to JOptionPanes
 */

package cmsc495.ui;

import java.awt.Container;

import javax.swing.JOptionPane;

/**
 * @author Adam
 * @date   2016-09-24
 */
public class PopUp {
  
  public static void Error (Container frame,String title, String message){
    JOptionPane.showMessageDialog(frame,
        message,
        title,
        JOptionPane.ERROR_MESSAGE);
  }// end Error

  public static void Warning (Container frame,String title, String message){
    JOptionPane.showMessageDialog(frame,
        message,
        title,
        JOptionPane.WARNING_MESSAGE);
  }//end Warning
}