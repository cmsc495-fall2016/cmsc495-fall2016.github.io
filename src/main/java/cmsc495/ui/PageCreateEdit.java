package cmsc495.ui;

import cmsc495.database.Recipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 * TODO: Add a Class descriptor on this line.
 * TODO OO: populate method headers & comment throughout
 * TODO AH: make the display resize nicely
 * TODO : implement ingredients
 * TODO : implement ingredient new field creator from the page ie start with one field and add more
 *  at the discretion of the user
 *  
 * @author Obinna Ojialor
 *
 */
public class PageCreateEdit extends Page implements ActionListener {
  
  private static final long serialVersionUID = -7189486032976973625L;
  private String[] recipeFields = {
      "name","author","description","source","cookTime","prepTime","serves",
      "difficulty"
      };
  private Recipe recipe;
  private String sw;
  private Map<String,JLabel> labels = new HashMap<String,JLabel>();
  private Map<String,JTextField> textFields = new HashMap<String,JTextField>();
  private JTextArea procedures;
  
  /**
   * Constructor method to create the creating a new recipe.
   * @param title String to be displayed as the title
   */
  public PageCreateEdit( String title ) {
    super(title);
    buildCommon();

  } //end constructor for creating

  /**
   * Constructor method to edit an existing recipe.
   * @param recipe Recipe to edit
   */
  public PageCreateEdit( Recipe recipe ) {
    super("Edit");
    this.recipe = recipe;
    buildCommon();
    setall();
  } // end constructor for editing
  
  /**
  * Method to consolidate common actions between two constructors.
  */
  public void buildCommon() {
    //Initialized panel    
    this.setLayout((LayoutManager) new BorderLayout());
    this.sw = "Create";
    //add panel Layout
    this.add(createNorthPanel(), BorderLayout.NORTH);
    this.add(createCenterPanel(),BorderLayout.CENTER);
  }
  
  /**
   * Objective for the panel is to create two (2) things.
   * 1) JTextArea for the procedures resizes nicely 
   * 2) An area for the ingredients 
   * @return panel
   */
  private Container createCenterPanel() {
    // Create the procedure text area in a scrolled area
    procedures = new JTextArea(10,10);
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JScrollPane(procedures),BorderLayout.CENTER);
    
    // add the ingredient listed items
    JPanel panel2 = new JPanel(new BorderLayout());
    panel2.add(new JScrollPane(new EntryList()),BorderLayout.CENTER);
    
    JTabbedPane tabbedpane = new JTabbedPane();
    tabbedpane.addTab("Procedure", panel);
    tabbedpane.addTab("Ingredients", panel2);
    return tabbedpane;
  }

  /**
   * Create panel with fields.
   * @return JPanel
   */
  private JPanel createNorthPanel() {
    // build & add button
    JButton button = new JButton();
    button.setText("<HTML><FONT color=\"#000099\"><U>Save</U></FONT></HTML>");
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    button.addActionListener(this);
    button.setActionCommand("Save");
    button.setBounds(600, 0, 60, 20);
    
    //Make JLabels & Text fields
    for (String string : recipeFields) {
      //make the display objects for the recipe elements
      // Capitalize the first char in each word
      String labelString = string.substring(0, 1).toUpperCase() + string.substring(1) + " ";;
      if (string == "cookTime" || string == "prepTime") {
        labelString = String.format("%s%s %s%s",
            string.substring(0, 1).toUpperCase(), string.substring(1,4),
            string.substring(4, 5).toUpperCase(), string.substring(5,8)
            );
      } //end unique label for cookTime & prepTime
      textFields.put(string, new JTextField("",30));
      labels.put(string,new JLabel(labelString + ":"));
    }
    
    // Make a sub panel to add to the main...the main will anchor it the left
    JPanel subPanel = new JPanel( new GridBagLayout() );
    subPanel.add(button,makeGbc(1,1));
    int row = 2;
    for (String string : recipeFields) {
      // Add the components
      subPanel.add(labels.get(string),makeGbc(1,row));
      subPanel.add(textFields.get(string),makeGbc(2,row++));
    }
    // Create the north panel
    JPanel panel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.LEFT));
    panel.setOpaque(true);
    panel.add(subPanel,BorderLayout.PAGE_START);
    panel.add(button,BorderLayout.PAGE_END);
  
    return panel;
  } // end createNorthPanel

  /**
   * Initialized all recipe field.
   */
  private void setall() {
    textFields.get("name").setText(String.format("%s", recipe.getName()));
    textFields.get("author").setText(String.format("%s", recipe.getAuthor()));
    textFields.get("description").setText(recipe.getDescription());
    textFields.get("source").setText(String.format("%s", recipe.getSource()));
    textFields.get("cookTime").setText(String.format("%s", recipe.getCook_time()));
    textFields.get("prepTime").setText(String.format("%s", recipe.getPrep_time()));
    textFields.get("serves").setText(String.format("%s", recipe.getServes()));
    textFields.get("difficulty").setText(String.format("%s", recipe.getDifficulty()));
    this.procedures.setText(this.recipe.getProcedures());
    
  }

  /**
   * method to help structure the GridBagLayout.
   * @param xgrid specifies the x coordinates
   * @param yGrid specifies the x coordinates
   * @return GridBagConstraints
   */
  private GridBagConstraints makeGbc(int xgrid, int ygrid) {
    GridBagConstraints gbc = new GridBagConstraints();
    // assign location
    gbc.gridx = xgrid;
    gbc.gridy = ygrid;
    // assign padding
    gbc.ipadx = 5;
    gbc.ipady = 0;
    // set the spacing of the cells
    gbc.insets = new Insets(0, 0, 0, 0);
    // set where the grid anchors to
    gbc.anchor = GridBagConstraints.NORTHEAST;
    // allow the grid to expand
    gbc.fill = GridBagConstraints.BOTH;
    return gbc;
  } // end makeGbc
  
  /**
   * Method to define the actions to be performed by objects in this class. 
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() instanceof JButton) {
      // the inputs for editing & creating are the same see test inputs
      if (testInputs()) {
        switch (this.sw) {
          case "Create":
            //TODO
            PopUp.warning(this, "Place Holder", " This is a place holder for Creating a recipe");
            break;
          case "Edit":
            //TODO
            PopUp.warning(this, "Place Holder", " This is a place holder for editing a recipe");
            break;
          default:
            PopUp.warning(this, "Button action undefined",
                "There is no action defined for " + event.getActionCommand());
        } // end switch
      }
    } else {
      PopUp.warning(this, "Action listener undefined",
          "There is no action defined for " + event.getSource());
    }
  }

  /**
   * method designed to determine if the input parameters are correct & defined
   * @return Boolean is all correct.
   */
  private boolean testInputs() {
    /*
     * Required inputs as per ERD
     * name
     * serves
     * author
     * preptime
     * cooktime
     * difficulty
     * procedures
     * ingredients
     * 
     * Optional:
     *  Description
     */
    return false;
  }

}
