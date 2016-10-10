/**
 * Class to find the displaying recipes
 * 
 * Design: Two (2) panels to add to the main panel
 *  1) Panel: Jlable : buttons (Find)
 *  2) Scrollable panel:JTable added into the center
 *
 */

package sharkbread.ui;

import sharkbread.database.Recipe;
import sharkbread.ui.support.JTableSb;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/*
* @author Obinna Ojialor
 */
public class PageFind  extends Page implements ActionListener, KeyListener {

  private static final long serialVersionUID = -2400088182616171103L;
  private List<Recipe> listRecipes;
  private JTextField fieldFind;
  private JButton buttonFind;
  private JTableSb table;
  private DefaultTableModel dtm;
  
  /**
   * Constructor method to find recipe.
   * @param  title string to display
   */
  public PageFind(String title) {
    super(title);
    // set layout manager
    this.setLayout(new BorderLayout());
    // create default TableModel
    dtm = new DefaultTableModel(0, 0);
    // create list Recipes
    listRecipes = new Recipe().getAll();
    // top button and label
    fieldFind = new JTextField("", 15);
    buttonFind = new JButton("Search");
    // Action listener for display recipe
    fieldFind.addKeyListener(this);
    buttonFind.addActionListener(this);
    buttonFind.setActionCommand("SEARCH");

    // create panel top
    JPanel paneltop = new JPanel(new FlowLayout());
    paneltop.add(fieldFind);
    paneltop.add(buttonFind);

    // adding panel
    this.add(paneltop, BorderLayout.NORTH);

    String[] columnheaders = {"Recipe Name", "Author", "Time", "Difficulty", "Ingredients"};

    // create JTable
    table = new JTableSb();

    // settings for the JTableSb
    table.setColumnIdentifiers(columnheaders);

    // Create table
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

  } // end constructor

  /**
   * Method to define the actions to be performed by objects in this class.
   */
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand() == "SEARCH") {
      actionSearchAll();
    }
  }
  
  /**
   * Common method between {@link KeyListener} & {@link ActionListener}.
   */
  private void actionSearchAll() {
    // clean table
    while (table.getRowCount() > 0) {
      table.removeRow(0);
    }
    findall(fieldFind.getText());
  }

  //Add new raw
  private void addDraw(Recipe recipe, int position) {
    String[] strings = {
        recipe.getName(),
        recipe.getAuthor(),
        String.valueOf(recipe.getCook_time()),
        String.valueOf(recipe.getDifficulty()),
        recipe.getIngredients().get(position).getName()
    };
    table.addRow(strings, recipe);
  }
  
  //Add new raw
  private void addDraw(Recipe recipe) {
    String[] strings = {
        recipe.getName(),
        recipe.getAuthor(),
        String.valueOf(recipe.getCook_time()),
        String.valueOf(recipe.getDifficulty()),
        ""
    };
    table.addRow(strings, recipe);
  }
  
  // find with keywords
  private void findall(String string) {
    boolean found = false;
    for (Recipe recipe : listRecipes) {
      // check the recipe's name
      if (recipe.getName().toLowerCase().contains(string.toLowerCase())) {
        addDraw(recipe);
        found = true;
        continue;
      }
      
      // check the recipe's author
      if (recipe.getAuthor().toLowerCase().contains(string.toLowerCase())) {
        addDraw(recipe);
        found = true;
        continue;
      }

      // check the ingredients
      for (int i = 0; i < recipe.getIngredients().size(); i++) {
        if (recipe.getIngredients().get(i).getName().toLowerCase().contains(string.toLowerCase())) {
          addDraw(recipe,i);
          found = true;
          continue;
        }
      }
      
      // test for integer
      int integer;
      try {
        integer = Integer.valueOf(string);
      } catch (NumberFormatException event) {
        // no sense in continuing if the search string is not an integer
        continue;
      }
      
      //check the cook time
      if (recipe.getCook_time() == integer) {
        addDraw(recipe);
        found = true;
        continue;
      } else if (recipe.getDifficulty() == integer) {
        addDraw(recipe);
        found = true;
        continue;
      }
    } // end looping through all the recipes
    
    // if the recipe is not found ... state as such
    if (!found) {
      dtm.addRow(new Object[] {"Not found", "Not found", "Not found", "Not found", "Not found"});
    }
  }

  /**
   * Implemented method from {@link KeyListener}.
   * Not needed here
   */
  @Override
  public void keyPressed(KeyEvent event) {
  }

  /**
   * Set the actions for an enter strike on the {@link JTextField}.
   */
  @Override
  public void keyReleased(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.VK_ENTER) {
      actionSearchAll();
    }
  }

  /**
   * Implemented method from {@link KeyListener}.
   * Not needed here
   */
  @Override
  public void keyTyped(KeyEvent event) {
  }
}