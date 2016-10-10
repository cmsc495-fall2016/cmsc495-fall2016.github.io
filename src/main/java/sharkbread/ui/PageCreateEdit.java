package sharkbread.ui;

import sharkbread.database.Ingredient;
import sharkbread.database.Recipe;
import sharkbread.ui.support.ButtonEditor;
import sharkbread.ui.support.ButtonRenderer;
import sharkbread.ui.support.PopUp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


/**
 * Class to create the needed page to create and edit recipes.
 * @author Obinna Ojialor
 * @author Adam Howell
 */
public class PageCreateEdit extends Page implements ActionListener {
  
  private static final long serialVersionUID = -7189486032976973625L;
  private String[] recipeFields = {
      "name","author","description","source","cookTime","prepTime","serves",
      "difficulty"
      };
  private Recipe recipe;
  private String sw = "CREATE";
  private Map<String,JLabel> labels = new HashMap<String,JLabel>();
  private Map<String,JTextField> textFields = new HashMap<String,JTextField>();
  private JTextArea procedures;
  private JTable table;
  private DefaultTableModel tableModel;
  
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
    this.sw = "EDIT";
    this.recipe = recipe;
    buildCommon();
    setall();
  } // end constructor for editing
  
  /**
  * Method to consolidate common actions between two constructors.
  */
  private void buildCommon() {
    //Initialized panel    
    this.setLayout((LayoutManager) new BorderLayout());
    //add panel Layout
    this.add(createNorthPanel(), BorderLayout.NORTH);
    this.add(createCenterPanel(),BorderLayout.CENTER);
  } // end BuildCommon
  
  /**
   * Method to define the actions to be performed by objects in this class. 
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    // test the source of the ActionEvent; currently there are only JButtons
    if (event.getSource() instanceof JButton) {
      JButton button = (JButton)event.getSource();
      // the inputs for editing & creating are the same see testInputs
      if (testInputs()) {
        switch (this.sw) {
          case "CREATE":
            createRecipe();
            break;
          case "EDIT":
            updateRecipe();
            break;
          default:
            PopUp.warning(this, "Button action undefined",
                "There is no action defined for " + event.getActionCommand());
        } // end switch
        // if the recipe fails to create or edit then the recipe is null 
        if (recipe instanceof Recipe) {
          // set the panel to the main page
          SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(button);
          gui.setCurrentPage(new PageDisplayRecipe(recipe));
          
        } // end instanceof test of recipe
      } // end of testing inputs
    } else {
      PopUp.warning(this, "Action listener undefined",
          "There is no action defined for " + event.getSource());
    } // end if/else JButton test
  } // end actionPerformed

  /**
   * Objective for the panel is to create two (2) things.
   * 1) JTextArea for the procedures resizes nicely 
   * 2) An area for the ingredients 
   * @return panel
   */
  private Container createCenterPanel() {
    // Create the procedure text area in a scrolled area
    procedures = new JTextArea(10,10);
    // Some recipes have long strings with out line breaks in them
    //    So let's set the line wrap on
    procedures.setWrapStyleWord(true);
    procedures.setLineWrap(true);
    
    // panel to hold the recipe's procedure
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JScrollPane(procedures),BorderLayout.CENTER);
    
    // stuff the procedures and the ingredients into a tabbed pane
    JTabbedPane tabbedpane = new JTabbedPane();
    tabbedpane.addTab("Procedure", panel);
    tabbedpane.addTab("Ingredients", createIngredientPanel());
    
    return tabbedpane;
  } //end createCenterPanel

  /**
   * Method to create the ingredient scroll panel.
   * @return {@link JScrollPane} with a {@link JTable} containing {@link JButton}s
   */
  private Component createIngredientPanel() {
    table = new JTable();
    tableModel = new DefaultTableModel();
    table.setModel(tableModel);
    
    //set the column headers
    tableModel.setColumnIdentifiers(new String[]{"Add Row","Remove Row","Name","Description"});
    
    // set the first row
    addTableRow("","");

    ArrayList<ButtonEditor> buttonEditorList = new ArrayList<ButtonEditor>();
    // update the button row to be a button
    for (String rowName : new String[]{"Add Row","Remove Row"}) {
      table.getColumn(rowName).setCellRenderer(new ButtonRenderer());
      ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox());
      buttonEditorList.add(buttonEditor);
      table.getColumn(rowName).setCellEditor(buttonEditor);
    }
    
    // update the commands for the ButtonEditor
    for (ButtonEditor buttonEditor : buttonEditorList) {
      Class[] parameterTypes = new Class[1];
      parameterTypes[0] = String.class;
      Method methodAdd = null;
      try {
        methodAdd = PageCreateEdit.class.getMethod("addRemoveRow", parameterTypes);
        buttonEditor.setActionCommand(this,methodAdd,"-");
        buttonEditor.setActionCommand(this,methodAdd,"+");
      } catch (Exception exception) {
        // Just incase, haven't found that this one catches but it's bound to happen
        exception.printStackTrace();
      }
    }
    
    // update the buttons columns to be a maximum width
    for (int integer : new int[]{0,1}) {
      table.getColumnModel().getColumn(integer).setMaxWidth(100);
    }    
    
    //table.getColumn("Button").setCellRenderer(new ButtonRenderer());
    //table.getColumn("Button").setCellEditor(new ButtonEditor(new JCheckBox()));
    JScrollPane scrollPane = new JScrollPane(table);
    return scrollPane;
  }
  
  /**
   * Method built to allow the {@link ButtonEditor} class call back action.
   * @param command simple command ie '-' '+'
   */
  public void addRemoveRow(String command) {

    // determine requested command 
    if (command == "-") {

      //Check if user wants to remove the top most row
      if (table.getSelectedRow() == 0) {
        PopUp.warning(this, "User Error", "Cannot remove the top most row");
      } else {
        // remove the requested row
        tableModel.removeRow(table.getSelectedRow());
      }
      
    } else if (command == "+") {
      addTableRow("", "");
    }
  }

  /**
   * Create panel with fields for the recipe input.
   *     Does not include the procedure or ingredients
   * @return JPanel Contains all fields but procedures & ingredients
   */
  private JPanel createNorthPanel() {
    // build & add 'Save' button
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
   * Method to create a new recipe from the users' inputs.
   */
  private void createRecipe() {
    recipe = new Recipe();
    // try to create a new recipe or pop-up the printed stack
    try {
      recipe.createRecipe(
          textFields.get("name").getText(),
          Integer.parseInt(
              textFields.get("serves").getText()
          ),
          textFields.get("author").getText() , 
          Integer.parseInt(
              textFields.get("prepTime").getText()
          ), 
          Integer.parseInt(
              textFields.get("cookTime").getText()
          ) , 
          Integer.parseInt(
              textFields.get("difficulty").getText()
          ) ,
          procedures.getText() , 
  
          textFields.get("description").getText() , 
          textFields.get("source").getText(),
          pullIngredients()
      );
    } catch (Exception execption) {
      PopUp.exception(this,execption);
    } // end try/catch
  } // end createRecipe

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
   * Method to get the Entry.TextField's string value
   * @return ArrayList of Ingredient objects containing the user's input
   */
  private ArrayList<Ingredient> pullIngredients() {
    ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    for (String[] strings : pullIngredientsNoCreate()) {
      Ingredient ingredient = new Ingredient();
      try {
        ingredient.createIngredient(strings[0],strings[1]);
        list.add(ingredient);
      } catch (SQLException execption) {
        PopUp.exception(this, execption);
      } //  end try/catch
    } // end for
    return list;
  } // end pullIngredients

  /**
   * Method to get the Entry.TextField's string value
   * @return ArrayList of Ingredient objects containing the user's input
   */
  private ArrayList<String[]> pullIngredientsNoCreate() {
    ArrayList<String[]> list = new ArrayList<String[]>();
    //cycle through the EntryList and pull the text strings
    for (int i = 0; i < table.getRowCount(); i++) {
      String name  = String.valueOf(table.getModel().getValueAt(i, 2));
      String description = String.valueOf(table.getModel().getValueAt(i, 3));
      
      /* only add if the name is longer than 0 characters */
      if (name.length() > 0 ) {
        list.add(new String[]{name,description});
      }
    } // end for
    
    return list;
  } // end pullIngredients

  /**
   * Initialized all recipe field.
   */
  private void setall() {
    // Clean the table ... should only be one ... buy hey why not a while loop
    while (table.getRowCount() > 0) {
      tableModel.removeRow(0);
    }
    // populate the easy fields
    textFields.get("name").setText(String.format("%s", recipe.getName()));
    textFields.get("author").setText(String.format("%s", recipe.getAuthor()));
    textFields.get("description").setText(recipe.getDescription());
    textFields.get("source").setText(String.format("%s", recipe.getSource()));
    textFields.get("cookTime").setText(String.format("%s", recipe.getCook_time()));
    textFields.get("prepTime").setText(String.format("%s", recipe.getPrep_time()));
    textFields.get("serves").setText(String.format("%s", recipe.getServes()));
    textFields.get("difficulty").setText(String.format("%s", recipe.getDifficulty()));
    this.procedures.setText(this.recipe.getProcedures());
    
    // populate the ingredients ... unless there is none
    ArrayList<Ingredient> ingredientList = recipe.getIngredients();
    if (ingredientList.size() == 0) {
      addTableRow("", "");
    }
    for (Ingredient ingredient : ingredientList) {
      addTableRow(ingredient);
    }

  } // end setAll
  
  /**
   * method to add an ingredient to the JTable.
   * @param ingredient ingredient to add to the table
   */
  private void addTableRow(Ingredient ingredient) {
    addTableRow(ingredient.getName(),ingredient.getDescription());
  }

  /**
   * Method to insert a new row in the JTable.
   * @param string Name
   * @param string2 Description
   */
  private void addTableRow(String string, String string2) {
    tableModel.addRow(
        new String[]{"+", "-", string, string2}
    );
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
    if (testTextFieldLength("name") 
        && testTextFieldLength("author")
        && testTextFieldLength("source")
        && testTextFieldLength("cookTime")
        && testTextFieldIntParse("cookTime")
        && testTextFieldLength("prepTime")
        && testTextFieldIntParse("prepTime")
        && testTextFieldLength("serves")
        && testTextFieldIntParse("serves")
        && testTextFieldLength("difficulty")
        && testTextFieldIntParse("difficulty")
        && testStringLength(procedures.getText()) 
        && true) {
      return true;
    }
    return false;
  } // end testInputs

  /**
   * Common method to test the length of a string
   * @param string String object to test against
   * @return true if the string.length() > 0
   */
  private boolean testStringLength(String string) {
    return string.length() > 0 ;
  }

  /**
   * Method to test the value.getText() length from the textFields map 
   * @param string used to fetch the text field from the map
   * @return true if text is > 0
   */
  private boolean testTextFieldIntParse(String string) {
    try {
      Integer.parseInt(textFields.get(string).getText() ) ;
      // nothing to occur here just testing
    } catch (Exception execption) {
      PopUp.error(this, "Invalid Input", "The " + string + " must be an integer");
      return false;
    }
    return true;

  } // end testTextFieldIntParse

  /**
   * Method to ensure the string input length is correct.
   * @param string Field in the textFields to test
   * @return true if the field contains a parse-able integer
   */
  private boolean testTextFieldLength(String field) {
    if ( testStringLength(textFields.get(field).getText() ) ) {
      return true;
    }
    PopUp.error(this,
        "Invalid Input",
        "The " + field + " must be Populated");
    return false;
  } // end testTextFieldLength

  /**
   * Method to update a recipes values based upon the corresponding JTextField.
   */
  private void updateRecipe() {
    /* 
     * check for ingredient deltas 
     * Three (3) cases exist
     *   1) No deltas; no change to ingredients
     *     ACTION: NONE
     *   2) Deltas exist, only to ingredient description
     *     ACTION: Update the ingredient description
     *   3) Deltas exist to ingredient quantity or name(s)
     *     ACTION: Delete recipe and create a new one
     */
    boolean deltasExist = false;
    ArrayList<String[]> newIngredients = pullIngredientsNoCreate();
    
    // check for ingredient quantity differences
    if (newIngredients.size() != recipe.getIngredients().size()) {
      deltasExist = true;
    }
    
    // check for ingredient description or Id deltas
    outerLoop:
    for (String[] newIngredient : newIngredients) {
      // case to exit outerLoop
      if (deltasExist) {
        break outerLoop;
      }
      
      boolean found = false;
      innerLoop:
      for (Ingredient oldIngredient : recipe.getIngredients()) {
        // check the name, as ingredient names are unique in the data base
        if (newIngredient[0] == oldIngredient.getName()) {
          found = true;
          
          /* 
           *   If description change, update the ingredient 
           */
          if (newIngredient[1] != oldIngredient.getDescription()) {
            try {
              oldIngredient.updateIngredient(
                  oldIngredient.getId(),
                  oldIngredient.getName(),
                  newIngredient[1]
              );
            } catch (SQLException exception) {
              PopUp.exception(this, exception);
            }
          }
          break innerLoop;
        }
      } // end for ingredients inner
      
      if (!found) {
        deltasExist = true;
      }
    } // end for ingredients outer
    
    if (deltasExist) {
      /* Try to delete & create the recipe*/
      try {
        recipe.deleteRecipe(recipe.getId());
      } catch (SQLException execption) {
        PopUp.exception(this, execption);
        return; // cannot do anything else here
      } // end try/catch
      
      createRecipe();
    } else {
      /* Address the recipe if there was no update to the ingredients  */
      try {
        recipe.updateRecipe(
            recipe.getId(),
            textFields.get("name").getText(),
            Integer.parseInt(
                textFields.get("serves").getText()
            ),
            textFields.get("author").getText() , 
            Integer.parseInt(
                textFields.get("prepTime").getText()
            ), 
            Integer.parseInt(
                textFields.get("cookTime").getText()
            ) , 
            Integer.parseInt(
                textFields.get("difficulty").getText()
            ) ,
            procedures.getText() , 
    
            textFields.get("description").getText() , 
            textFields.get("source").getText() 
        );
        
      } catch (NumberFormatException | SQLException exception) {
        PopUp.exception(this, exception);
      } //end try/catch for recipe updating
    } // end if/else deltasEsist
  } // end updateRecipe
} // end class PageCreateEdit
