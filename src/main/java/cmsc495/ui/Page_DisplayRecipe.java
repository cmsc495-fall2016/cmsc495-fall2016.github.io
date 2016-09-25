/**
 * Class to create the displaying pages:
 * 
 * Pages to create:
 *    1) Display the recipe R/O (Display)
 *    2) Display the recipe R/W (Editing)
 *    3) Display the recipe W   (Creating)
 */
package cmsc495.ui;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-19
 */
public class Page_DisplayRecipe extends Page {

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = -3430232989667398763L;

  public Page_DisplayRecipe(){
    // constructor for creating/inputting recipe
    super("Display");
  }
  public Page_DisplayRecipe(Recipe recipe, Boolean editing) {
    // constructor for display or editing of recipe only
    super("Display");
    configurePage(editing);
  }

  /**
   * Method to configure the fields
   * @param editing
   */
  private void configurePage(Boolean editing) {
    // TODO Auto-generated method stub
    /*
     *  TODO utilize the boolean field to determine
     *       editable text, checkboxes and the like
     */
    
  }//end configurePage

}//end class Page_DisplayRecipe
