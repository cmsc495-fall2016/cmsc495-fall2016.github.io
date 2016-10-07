/**
 * An ENUM to determine what pages in the project are to 
 *   be utilized and how they are called.
 *   
 * For updates:
 *   1) add a new enum
 *   2) add the implementation of the abstract method
 *      utilized to create the panel (page)
 */

package sharkbread.ui.support;

import sharkbread.test.classes.ui.PageUiAllTest;
import sharkbread.ui.Page;
import sharkbread.ui.PageBrowseRecipe;
import sharkbread.ui.PageCreateEdit;
import sharkbread.ui.PageFind;
import sharkbread.ui.PageMain;
import sharkbread.ui.SimpleGui;

/**
 * The Navigations enum.
 * This is where the navigation section of the SimpleGUI is updated.
 * To add, just add a new enum and add a constructor the specific
 * getPanel method of the enum.
 * 
 * @author Adam
 *
 */
public enum Navigations {
  MAIN {
    public Page getPanel(SimpleGui simpleGui) {
      return new PageMain();
    }
  },
  SEARCH {
    public Page getPanel(SimpleGui simpleGui) {
      return new PageFind(this.toString());
    }
  },
  BROWSE {
    public Page getPanel(SimpleGui simpleGui) {
      return new PageBrowseRecipe(this.toString());
    }
  },
  CREATE {
    public Page getPanel(SimpleGui simpleGui) {
      return new PageCreateEdit(this.toString());
    }
  },
  IMPORT {
    public Page getPanel(SimpleGui simpleGui) {
      new ImportExport(simpleGui);
      return null;
    }
  },
  TESTING {
    public Page getPanel(SimpleGui simpleGui) {

      return new PageUiAllTest();
    }
  },
  ;
  
  /**
   * Abstract method that each enum must implement for their own type of 
   *    pages.
   *    
   * @return Page
   */
  public abstract Page getPanel(SimpleGui simpleGui);
 
  /**
   * override the default toString to provide
   *   Capitalized first letter of the enum.
   *   
   * @return String
   */
  @Override
  public String toString() {
    return 
        String.format(
            "%s%s",
            super.toString().substring(0, 1).toUpperCase(),
            super.toString().substring(1).toLowerCase()
            );
  }
}
