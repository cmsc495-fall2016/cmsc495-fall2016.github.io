/**
 * An ENUM to determine what pages in the project are to 
 *   be utilized and how they are called.
 *   
 * For updates:
 *   1) add a new enum
 *   2) add the implementation of the abstract method
 *      utilized to create the panel (page)
 */
package cmsc495.ui;

/**
 * @author Adam
 *
 */
public enum Pages {
  MAIN {
    public Page getPanel(){
      return new Page_Main();
    }
  },
  SEARCH {
    public Page getPanel(){
      return new Page(this.toString());
    }
  },
  BROWSE {
    public Page getPanel(){
      return new Page_BrowseRecipe(this.toString());
    }
  },
  CREATE {
    public Page getPanel(){
      return new Page(this.toString());
    }
  },
  ;
  
  /**
   * Abstract method that each enum must implement for their own type of 
   *    pages 
   * @return Page
   */
  public abstract Page getPanel();
 
  /**
   * override the default toString to provide
   *   Capitalized first letter of the enum
   * @return String
   */
  @Override
  public String toString(){
    return 
        String.format(
            "%s%s",
            super.toString().substring(0, 1).toUpperCase(),
            super.toString().substring(1).toLowerCase()
            );
  }
}
