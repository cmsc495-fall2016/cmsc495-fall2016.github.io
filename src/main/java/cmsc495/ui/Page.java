/**
 * Parent Class for all pages
 */
package cmsc495.ui;

import javax.swing.JPanel;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-20
 */
public class Page extends JPanel{

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = 8490689814447716962L;
  private String title;
  
  /**
   * Constructor to contain/set all common things between the pages
   * NOTE: only enforcing the use of "setTitle" to allow the border 
   *       to have a name
   * @param title
   */
  public Page(String title){
    this.title = title;
  }//end Page

  public String getTitleName(){
    return this.title;
  }
  
}//end class Page
