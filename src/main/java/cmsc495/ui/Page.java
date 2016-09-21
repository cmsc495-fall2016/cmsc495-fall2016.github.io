/**
 * Parent Class for all pages
 */
package cmsc495.ui;

import javax.swing.JFrame;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-20
 */
public class Page extends JFrame{

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = 8490689814447716962L;

  /**
   * Constructor to contain/set all common things between the pages
   * NOTE: only enforcing the use of "setTitle" to allow checking 
   *       of instantiation of JFrame.  use a better method if available
   * @param title
   */
  public Page(String title){
    this.setTitle(title);
  }//end Page

}//end class Page
