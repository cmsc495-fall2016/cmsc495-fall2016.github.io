/**
 * Class to create a page to browse recipes in the database
 *    Initilaze display will be truncated information 
 *    Once the user selects a recipe, the Display Recipe page will be loaded
 *     with the selected recipe
 */
package cmsc495.ui;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Page_BrowseRecipe extends Page implements ActionListener{

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = -1007375598685985229L;
  private Map<JButton,Recipe_Hold> buttonMap = new HashMap<JButton, Recipe_Hold>();
  
  public Page_BrowseRecipe(String title) {
    super(title);
    
    // fetch recipe browse default
    // TODO replace this call with the DAO method once it is created
    ArrayList<Recipe_Hold> listRecipes = fetchBrowseDefault();
    
    // build the panel & set its' layout manager
    //JPanel panel = new JPanel();
    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
    
    /*
     *  build the button & add to:
     *    recipe map (helps with action listener
     *    panel 
     */
    for( Recipe_Hold recipe : listRecipes){
      JButton button  = buildButton(recipe);
      buttonMap.put(button, recipe);
      button.addActionListener( this );
      button.setActionCommand("Recipe_"+ recipe.get_id());
      add(button);
    }// end for listRecipes
    
  }// end constructor
  
  /**
   * Method to generate web-like link buttons
   * @param recipe
   * @return JButton
   */
  private JButton buildButton(Recipe_Hold recipe) {
    // Creates a button that resembles a webpage's link
    JButton button = new JButton();
    button.setText(
        String.format(
            "<HTML><HR>" + 
                "Recipe: <FONT color=\"#000099\"><U>%s</U></FONT><BR>" + 
                "Description: <BR>%s" + 
                "</HTML>",
            recipe.get_recipeName(),
            recipe.get_description()
            )
        );
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    
    return button;
  }

  /**
   * TODO Update this with the DAO from the interface team
   * @return ArrayList<Recipe> 
   */
  private ArrayList<Recipe_Hold> fetchBrowseDefault() {
    ArrayList<Recipe_Hold> list = new ArrayList<Recipe_Hold>();
    // TODO reconfigure this to the methods the interface teams' method
    list.add(new Recipe_Hold(1,   "Cheesy Mac and Trees","Yummy Mac & Cheese with brocolli"));
    list.add(new Recipe_Hold(2,   "Mexi Mac and Cheese","Description here"));
    list.add(new Recipe_Hold(3,   "Strawberry Shortcakes","Yummy Ymuuy Yummy"));
    list.add(new Recipe_Hold(4,   "Duck Stock","Duck, Duck, oh yeah DUCK!"));
    list.add(new Recipe_Hold(5,   "Warm Mexican rice salad with borlotti beans & avocado salsa","Sound like a whole lot of messy"));
    list.add(new Recipe_Hold(6,   "Crispy Tortillas with Guacamole","Chips & Huac"));
    list.add(new Recipe_Hold(7,   "Lamb Shanks Braise with Figs and Root Vegetables","Who knew I like ROOTS"));
    list.add(new Recipe_Hold(8,   "RoastTurkey with Spiked Gravy","ok cool"));
    list.add(new Recipe_Hold(10,  "Ham in Coca-Cola","Ham & Coke? What"));
    
    return list;
  }//end fetchBrowseDefault


  /**
   * Add in the ActionListener methods required from implementation 
   */
  public void actionPerformed(ActionEvent e) {
    // Determine what the ActionEvent is
    if (e.getSource() instanceof JButton){
      JButton button = (JButton) e.getSource();
      
      // Determine if the button is in the buttonMap
      if (buttonMap.containsKey( button ) ){
        Recipe_Hold recipe = (Recipe_Hold) buttonMap.get(button);
        
        // set the panel to the main page
        SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(button);
        gui.setCurrentPage(new Page_DisplayRecipe(recipe));
      }else{
        // show a PopUp.error() if there is no action associated
        PopUp.Error(this,
            "Unknown Action", 
            "A JButton has been found, but there is no action associated\n"
            + " Please open a ticket against this"
            );
      }//end if button map contains check & else
    }// end if e.getSource is a button
  }//end actionPerformed
}// end class Page_BroseRecipe
