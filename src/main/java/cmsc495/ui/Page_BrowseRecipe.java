/**
 * Class to create a page to browse recipes in the database
 *    Initialize display will be truncated information 
 *    Once the user selects a recipe, the Display Recipe page will be loaded
 *     with the selected recipe
 */
package cmsc495.ui;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import cmsc495.data.RecipeDao;
import cmsc495.database.Recipe;

public class Page_BrowseRecipe extends Page implements ActionListener{

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = -1007375598685985229L;

  private Map<JButton,Recipe> buttonMap = new HashMap<JButton, Recipe>();
  private RecipeDao recipeDao;
  
  public Page_BrowseRecipe(String title) {
    super(title);
    
    recipeDao = new RecipeDao();
    
    // fetch recipe browse default
    // TODO replace this call with the DAO method once it is created
    //List<Recipe> listRecipes = fetchBrowseDefault();
    List<Recipe> listRecipes = recipeDao.getAll();
    
    
    // build the panel & set its' layout manager
    //JPanel panel = new JPanel();
    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
    
    /*
     *  build the button & add to:
     *    recipe map (helps with action listener
     *    panel 
     */
    for( Recipe recipe : listRecipes){
      JButton button  = buildButton(recipe);
      buttonMap.put(button, recipe);
      button.addActionListener( this );
      button.setActionCommand("Recipe_"+ recipe.getId());
      add(button);
    }// end for listRecipes
    
  }// end constructor
  
  /**
   * Method to generate web-like link buttons
   * @param recipe
   * @return JButton
   */
  private JButton buildButton(Recipe recipe) {
    // Creates a button that resembles a webpage's link
    JButton button = new JButton();
    button.setText(
        String.format(
            "<HTML><HR>" + 
                "Recipe: <FONT color=\"#000099\"><U>%s</U></FONT><BR>" + 
                "Description: <BR>%s" + 
                "</HTML>",
            recipe.getName(),
            recipe.getDescription()
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
  private ArrayList<Recipe> fetchBrowseDefault() {
    ArrayList<Recipe> list = new ArrayList<Recipe>();
    

    
    // TODO reconfigure this to the methods the interface teams' method
    for (int i = 1; i <= 40; i++){
      System.out.println("Getting:"+i);
      try {
        Recipe recipe = new Recipe();
        recipe.getRecipeByNumber(i);
        System.out.println("NAME:"+recipe.getName());
        if (recipe.getName() != null)
          list.add(recipe);
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
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
        Recipe recipe = (Recipe) buttonMap.get(button);
        
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
