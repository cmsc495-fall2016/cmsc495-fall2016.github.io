package sharkbread.ui.support;

import sharkbread.data.Export;
import sharkbread.data.Import;
import sharkbread.database.Recipe;
import sharkbread.ui.PageDisplayRecipe;
import sharkbread.ui.SimpleGui;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;


/**
 * Class to allow user interface to accomplish imports & exports.
 * 
 * @author Adam
 */
public class ImportExport {
  /**
   * Method to all the import of a Recipe.
   * @param component Component to tie to the file chooser
   */
  public ImportExport(Component component) {
    this(component, null);
  } // end Constructor Import version

  /**
   * Method to allow the export of the Recipe.
   * @param component Component to tie to the file chooser
   * @param recipe Recipe object to be exported
   */
  public ImportExport(Component component, Recipe recipe) {
    // determine operation: import v.s. export
    if (recipe == null) {
      doImport(component);
    } else {
      doExport(component, recipe);
    }
  }

  /**
   * Handles the export of a recipe.
   * @param component - needed to determine the SimpleGui instance to 
   *                    push the newly imported recipe to
   */
  private void doExport(Component component, Recipe recipe) {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Select a folder location to export to");
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    // only act on true selections
    if (fc.showOpenDialog(component) == JFileChooser.APPROVE_OPTION) {
      String ouputFileName = recipe.getName().replaceAll("[\\\\/:*?\"<>| ]", "-") + ".json";
      File directory = fc.getSelectedFile();
      File file = new File(directory.toString() + "/" + ouputFileName);
      new Export(file, recipe);
      
      // PopUp a note to show the file was created
      String message = file.getAbsolutePath() + " has ";
      if (!file.exists()) {
        message += "not ";
      } // end if/else file exists note
      PopUp.info(null,
          String.format("%s been created", message)
      );
    } // end APPROVE_OPTION    
  } // end doExport

  /**
   * Handles the import of a recipe.
   * @param component - needed to determine the SimpleGui instance to 
   *                    push the newly imported recipe to
   */
  private void doImport(Component component) {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Select Recipe File to Import from");

    // only act on true selections
    if (fc.showOpenDialog(component) == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      try {
        Recipe imported = new Import(file).imported;
        /* test for the component to != null,
           no sense attempting to put the display in the gui */
        if (component != null) {
          SimpleGui gui = (SimpleGui) SwingUtilities.getRoot(component);
          gui.setCurrentPage(new PageDisplayRecipe(imported));
        } // end component test
      } catch (Exception execption) {
        PopUp.exception(null, execption);
      } // end try/catch
    } // end APPROVE_OPTION    
  }
} // end doImport