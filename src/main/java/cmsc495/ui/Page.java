/**
 * Parent Class for all pages
 */

package cmsc495.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

/**
 * A Page class that extends JPanel.
 * 
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-20
 */

public class Page extends JPanel {

  /**
   * Generated serial ID.
   */
  private static final long serialVersionUID = 8490689814447716962L;

  private String title;
  
  /**
   * Constructor to contain/set all common things between the pages.
   * NOTE: only enforcing the use of "setTitle" to allow the border 
   *       to have a name
   * 
   * @param title - title of the page
   */
  public Page(String title){
    this.title = title;
  }

  public String getTitleName() {
    return this.title;
  }

  /**
   * Method to create JTextAreas.  boolean flag determines if JTextArea is editable.
   * 
   * @param editable      boolean
   * @return JTextArea
   */
  public static JTextArea createJTextArea(boolean editable) {
    JTextArea jtextarea = new JTextArea(10,10);
    jtextarea.setEditable(editable);
    jtextarea.setFont(new Font("Courier", Font.BOLD, 14));
    jtextarea.setBorder(new LineBorder(Color.BLACK, 1));
    jtextarea.setWrapStyleWord(true);
    
    return jtextarea;
  }
}
