package sharkbread.ui.support;

import sharkbread.database.Recipe;
import sharkbread.ui.PageDisplayRecipe;
import sharkbread.ui.SimpleGui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Class to create a JTable with custom listeners.
 * @author Adam
 *
 */
public class JTableSb extends JTable implements MouseListener {

  private static final long serialVersionUID = 1152789676858106885L;
  private ArrayList<Recipe> recipes;
  private boolean editable = false;
  private DefaultTableModel tableModel;
  
  /**
   * Default constructor: has defaulted editable to false.
   */
  public JTableSb() {
    super();
    tableModel = new DefaultTableModel(0, 0);
    recipes = new ArrayList<Recipe>();
    this.setModel(tableModel);
    this.addMouseListener(this);
  }
  
  /**
   * Constructor that allows the cells to be editable.
   * @param editable Boolean flag to allow editable cells
   */
  public JTableSb(boolean editable) {
    this();
    this.editable = editable;
    // if the cells will be editable, we surly do not want a mouselistner on
    if (!editable) {
      this.removeMouseListener(this);
    }
  }
  
  /**
   * Method to add a row & hold a recipe reference for launching later.
   * @param strings Array of strings to display into the TableModel
   */
  public void addRow(String[] strings, Recipe recipe) {
    //Add the strings array to the default table model
    this.tableModel.addRow(strings);
    
    //Store the recipe for later retrieval 
    recipes.add(recipe);
  }
  
  /**
   * Method to override the allowable editing of any cell.
   */
  @Override
  public boolean editCellAt(int row, int column, java.util.EventObject event) {
    return editable;
  }
  
  /**
   * Returns the DefaultTableModel rowCount.
   * @return Integer
   */
  public int getRowCount() {
    return tableModel.getRowCount();
  }
  
  /**
   * Method to define what will happen when an item is clicked in the table.
   */
  @Override
  public void mouseClicked(MouseEvent event) {
    // Determine if the root parent is an implemented SimpleGui
    SimpleGui gui = (SimpleGui) SwingUtilities.getRoot(this);
    if (gui.isShowing()) {
      // fetch the recipe at the row count
      int row = this.getSelectedRow();
      // determine if there is a recipe at the selected row
      try {
        Recipe recipe = recipes.get(row);
        gui.setCurrentPage(new PageDisplayRecipe(recipe));
      } catch (IndexOutOfBoundsException iobe) {
        // nothing to do here this is the catch for usages outside of recipes
      } // end try/catch
    } //end if GUI is showing
    
  } //end mouseClicked

  /**
   * Method to defined what will happen when the mouse enters the table.
   * We have nothing to do for this one ... no code
   */
  @Override
  public void mouseEntered(MouseEvent event) {
  }

  /**
   * Method to defined what will happen when the mouse exits the table.
   * We have nothing to do for this one ... no code
   */
  @Override
  public void mouseExited(MouseEvent event) {
  }

  /**
   * Method to defined what will happen when the mouse is pressed
   * We have nothing to do for this one ... no code
   */
  @Override
  public void mousePressed(MouseEvent event) {
  }

  /**
   * Method to defined what will happen when the mouse is released
   * We have nothing to do for this one ... no code
   */
  @Override
  public void mouseReleased(MouseEvent event) {
  }

  /**
   * Interface to the {@link DefaultTableModel} method of removeRow.
   * @param row the index to be removed
   */
  public void removeRow(int row) {
    try {
      tableModel.removeRow(row);
    } catch (ArrayIndexOutOfBoundsException execption) {
      // nothing to do here 
    }
  }
  
  /**
   * Sets the column headers for the table.
   * @param strings Array of strings to utilize as the headers
   */
  public void setColumnIdentifiers(String[] strings) {
    tableModel.setColumnIdentifiers(strings);
  }
  
  /**
   * Method to catch the setting of the table model.
   * Try to utilize a {@link DefaultTableModel}
   */
  @Override
  public void setModel(TableModel tableModel) {
    this.tableModel = (DefaultTableModel) tableModel;
    super.setModel(tableModel);
  }
} //end class JTableSb
