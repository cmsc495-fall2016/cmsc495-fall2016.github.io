package cmsc495.ui;

import cmsc495.data.Export;
import cmsc495.data.Import;
import cmsc495.database.Recipe;

import java.awt.Component;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.Popup;
import javax.swing.SwingUtilities;


/**
 * Class to allow user interface to accomplish imports & exports.
 * @author Adam
 *
 */
public class ImportExport {
  private boolean isImporting = false;

  /**
   * Method to all the import of a Recipe.
   * @param component Component to tie to the file chooser
   */
  public ImportExport(Component component) {
    this(component,null);
  } // end Constructor Import version
  
  /**
   * Method to allow the export of the Recipe.
   * @param component Component to tie to the file chooser
   * @param recipe Recipe object to be exported
   */
  public ImportExport( Component component, Recipe recipe ) {
    // determine the method to be utilized
    this.isImporting  = ( recipe == null ) ? true : false;
    
    // open a file selector for the user to specify a file path
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle( 
        String.format("Select Recipe File to %s",
        (this.isImporting) ? "Import from" : "Export to"
          )
    );
    int returnVal = fc.showOpenDialog(component);

    // only act on true selections
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      if (this.isImporting) {
         Recipe imported;
         try {
           imported = new Import(file).imported;
           //test for the component to != null, 
           //no sense attempting to put the display in the gui
           if (component != null) {
             SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(component);
             gui.setCurrentPage(new PageDisplayRecipe(imported));
           } // end component test
         } catch (Exception execption) {
           PopUp.error(null,
               "Invalid Recipe File",
               "The selected file is in the incorrect format,please try again");
         } //end try/catch
      } else {
        new Export(file, recipe);
      } // end if importing
    } // end APPROVE_OPTION
  }
  
  /**
   * Testing stuff.
   * @param args cmd line arguments
   */
  public static void main( String[] args ) {
    Recipe recipe = new Recipe();
    try {
      recipe.getRecipeByNumber(1);
      System.out.println(recipe.getAuthor());
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
    if (new ImportExport(null,recipe).isImporting == true) {
      System.out.println("Constructor failed Export");
    }
    if (new ImportExport(null).isImporting == false) {
      System.out.println("Constructor failed import");
    }
  }
}
