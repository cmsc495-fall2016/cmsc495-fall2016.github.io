/**
 * Class to create a page to browse recipes in the database
 *    Initialize display will be truncated information 
 *    Once the user selects a recipe, the Display Recipe page will be loaded
 *     with the selected recipe
 */

package cmsc495.ui;

import cmsc495.database.Recipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class PageBrowseRecipe extends Page implements ActionListener {

  /**
   * Generated serial ID.
   */
  private static final long serialVersionUID = -1007375598685985229L;

  private Map<JButton,Recipe> buttonMap = new HashMap<JButton, Recipe>();
  
  /**
   * Constructor that takes a title.
   * 
   * @param title - A string that's the tile for the page.
   */
  public PageBrowseRecipe(String title) {
    super(title);
    this.setLayout(new BorderLayout());

    // fetch recipe browse default
    List<Recipe> listRecipes = new Recipe().getAll();
    
    /*
     *  build the button & add to:
     *    recipe map (helps with action listener)
     *    panel helps to align things
     */
    JPanel panel = new JPanel(new GridLayout(0, 1));
    
    for (Recipe recipe : listRecipes) {
      JButton button  = buildButton(recipe);
      buttonMap.put(button, recipe);
      button.addActionListener( this );
      button.setActionCommand("Recipe_" + recipe.getId());
      panel.add(button);
    }
    
    JScrollPane scrollPane = new JScrollPane(panel);
    add(scrollPane,BorderLayout.CENTER);
    
  }
  
  /**
   * Method to generate web-like link buttons.
   * 
   * @param recipe - Recipe used to build a button.
   * @return JButton
   */
  private JButton buildButton(Recipe recipe) {
    // Creates a button that resembles a webpage's link
    JButton button = new JButton();
    button.setText(
        String.format(
            "<HTML><HR>" 
            + "Recipe: <FONT color=\"#000099\"><U>%s</U></FONT><BR>"
            + "Description: <BR>%s" 
            + "</HTML>",
            recipe.getName(),
            recipe.getDescription()
            ));
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    
    return button;
  }

  /**
   * Add in the ActionListener methods required from implementation. 
   */
  public void actionPerformed(ActionEvent event) {
    // Determine what the ActionEvent is
    if (event.getSource() instanceof JButton) {
      JButton button = (JButton) event.getSource();
      
      // Determine if the button is in the buttonMap
      if (buttonMap.containsKey( button ) ) {
        Recipe recipe = (Recipe) buttonMap.get(button);
        
        // set the panel to the main page
        SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(button);
        gui.setCurrentPage(new PageDisplayRecipe(recipe));
      } else {
        // show a PopUp.error() if there is no action associated
        PopUp.error(this,
            "Unknown Action", 
            "A JButton has been found, but there is no action associated\n"
            + " Please open a ticket against this");
      }
    }
  }
}
