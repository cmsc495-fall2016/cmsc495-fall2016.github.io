package sharkbread.ui;

import sharkbread.database.Recipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;



/**
 * Class find recipe.
 * 
 * @author Obinna Ojialor
 */
public class PageFind extends Page implements ActionListener {

  private List<Recipe> listRecipes;
  private JTextField fieldfind;
  private JButton buttonfind;
  private Map<JButton, Recipe> buttonMap = new HashMap<JButton, Recipe>();

  /**
   * A construtor for PageFind that takes a string.
   * 
   * @param title - The title of PageFind
   */
  public PageFind(String title) {
    super(title);
    this.setLayout(new BorderLayout());
    // fetch recipe browse default
    listRecipes = new Recipe().getAll();

    fieldfind = new JTextField("", 15);
    buttonfind = new JButton("Search");
    buttonfind.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        fieldfind.setVisible(false);
        findall(fieldfind.getText());
      }
    });
    
    JPanel panelTop = new JPanel(new FlowLayout());
    
    panelTop.add(fieldfind);
    panelTop.add(buttonfind);


    this.add(panelTop, BorderLayout.NORTH);



  }

  // find and draw alleleent
  private void findall(String sr) {
    int size = 0;
    JPanel panelcenter = new JPanel();
    for (Recipe recipe : listRecipes) {

      if (sr == recipe.getName()) {
        JButton button = buildButton(recipe);
        buttonMap.put(button, recipe);
        button.addActionListener(this);
        button.setActionCommand("Recipe_" + recipe.getId());
        panelcenter.add(button);
        size++;
        continue;
      }
      if (sr == recipe.getAuthor()) {
        JButton button = buildButton(recipe);
        buttonMap.put(button, recipe);
        button.addActionListener(this);
        button.setActionCommand("Recipe_" + recipe.getId());
        panelcenter.add(button);
        size++;
        continue;
      }
      for (int i = 0; i < recipe.getIngredients().size(); i++) {
        if (sr == recipe.getIngredients().get(i).getName()) {
          JButton button = buildButton(recipe);
          buttonMap.put(button, recipe);
          button.addActionListener(this);
          button.setActionCommand("Recipe_" + recipe.getId());
          panelcenter.add(button);
          size++;
          continue;
        }
      }

    }
    if (size == 0) {

      JPanel ane = new JPanel();
      ane.add(new JLabel("No content in table"));
    } else {
      JScrollPane scrollPane = new JScrollPane(panelcenter);
      add(scrollPane, BorderLayout.CENTER);
    }

  }

  private JButton buildButton(Recipe recipe) {
    // Creates a button that resembles a webpage's link
    JButton button = new JButton();
    button.setText(String.format(
        "<HTML><HR>" + "Recipe: <FONT color=\"#000099\"><U>%s</U></FONT><BR>" 
        + "Description: <BR>%s" + "</HTML>", recipe.getName(), recipe.getDescription()));
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);

    return button;
  }



  @Override
  /**
   * Add in the ActionListener methods required from implementation.
   */
  public void actionPerformed(ActionEvent event) {
    // Determine what the ActionEvent is
    if (event.getSource() instanceof JButton) {
      JButton button = (JButton) event.getSource();

      // Determine if the button is in the buttonMap
      if (buttonMap.containsKey(button)) {
        Recipe recipe = (Recipe) buttonMap.get(button);

        // set the panel to the main page
        SimpleGui gui = (SimpleGui) SwingUtilities.getRoot(button);
        gui.setCurrentPage(new PageDisplayRecipe(recipe));
      } else {
        // show a PopUp.error() if there is no action associated
        PopUp.error(this, "Unknown Action",
            "A JButton has been found, but there is no action associated\n" 
            + " Please open a ticket against this");
      }
    }
  }


}
