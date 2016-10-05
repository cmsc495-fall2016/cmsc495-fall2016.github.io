/**
 * Class to create the displaying page for recipes
 * 
 * Design: Two (2) panels to add to the main panel
 *  1) Panel: contains Recipe name & buttons (Edit)
 *  2) Scrollable panel:JTextArea added into the center
 */

package sharkbread.ui;


import sharkbread.database.Ingredient;
import sharkbread.database.Recipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * @author Adam Howell, Obinna Ojialor.
 * @date   2016-09-19
 */
public class PageDisplayRecipe extends Page implements ActionListener {

  /**
   * Generated serial ID.
   */
  private static final long serialVersionUID = -3430232989667398763L;
  
  private Recipe recipe;
  
  /**
   * Constructor method to display recipe.
   * @param recipe Recipe to display
   */
  public PageDisplayRecipe( Recipe recipe ) {
    super("Display");
    this.recipe = recipe;
    
    // set layout manager
    setLayout(new BorderLayout());
    
    // create the north panel
    JPanel northPanel = createNorthPanel();
    
    // create the center panel
    JScrollPane centerPanel = createCenterPanel();
    
    // add panels 
    this.add(northPanel, BorderLayout.NORTH);
    this.add(centerPanel, BorderLayout.CENTER);
    
  }
  
  /**
   * Method to define the actions to be performed by objects in this class. 
   */
  public void actionPerformed( ActionEvent event ) {
    /*
     * Determine the source and act 
     * NOTE: May need to change to Action Commands
     */
    if (event.getSource() instanceof JButton) {
      JButton button = (JButton) event.getSource();

      // set the panel to the main page 
      SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(button);
      switch (button.getActionCommand()) {
        case "Edit": 
          gui.setCurrentPage(new PageCreateEdit(recipe));
          break;
        case "Export": 
          new ImportExport(this, recipe);
          break;
        case "Delete": 
          if (recipe != null) {
            boolean confirm = PopUp.confirm(this,
                "Are you sure that you want to delete: " + recipe.getName()
            );
            if (confirm == true) {
              try {
                recipe.deleteRecipe(recipe.getId());
                gui.setCurrentPage(new PageDisplayRecipe(null));
              } catch (SQLException exception) {
                PopUp.exception(this, exception);
              } //end try/catch
            } // end confirmation of delete
          } //end test for recipe to not be null
          break;
        default:
          break;
      }
      
    } //end if 
    
  } // end actionPerformed

  /**
   * Method to set the styles to the JTextPane
   * Example obtained from 
   * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextSamplerDemoProject/src/components/TextSamplerDemo.java
   * 
   * @param doc - the StyledDocument
   */
  protected void addStylesToDocument(StyledDocument doc) {
    //Initialize some styles.
    Style def = StyleContext.getDefaultStyleContext()
        .getStyle(StyleContext.DEFAULT_STYLE);
  
    Style regular = doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");
    
    int regFontSize = 12;
    
    // italics
    Style style = doc.addStyle("italic", regular);
    StyleConstants.setItalic(style, true);
    StyleConstants.setFontSize(style, regFontSize);
    
    int smallFontSize = 10;
    
    style = doc.addStyle("italicsmall", regular);
    StyleConstants.setItalic(style, true);
    StyleConstants.setFontSize(style, smallFontSize);
  
    style = doc.addStyle("italicsmallbold", regular);
    StyleConstants.setItalic(style, true);
    StyleConstants.setFontSize(style, smallFontSize);
    StyleConstants.setBold(style, true);
    
    style = doc.addStyle("italicbold", regular);
    StyleConstants.setItalic(style, true);
    StyleConstants.setFontSize(style, regFontSize);
    StyleConstants.setBold(style, true);
  
    style = doc.addStyle("bold", regular);
    StyleConstants.setBold(style, true);
    
    int largeFontSize = 16;
    
    style = doc.addStyle("boldlarge", regular);
    StyleConstants.setBold(style, true);
    StyleConstants.setFontSize(style, largeFontSize);
  
    style = doc.addStyle("small", regular);
    StyleConstants.setFontSize(style, smallFontSize);
  
    style = doc.addStyle("large", regular);
    StyleConstants.setFontSize(style, largeFontSize);
    
  }

  /**
   * Method to build buttons for this page & set action command.
   * @param commandAction   string to set as display and to determine what action to perform
   * @return JButton
   */
  private JButton buildNavButton(String commandAction) {
    JButton button = new JButton();
    button.setText(
        String.format(
            "<HTML><FONT color=\"#000099\"><U>%s</U></FONT></HTML>",
            commandAction
            ));
    
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    button.setActionCommand(commandAction);
    button.addActionListener(this);
    
    return button;
  }
  
  /**
   * Method to create the North panel that will contain the Recipe Name & buttons to edit.
   * 
   * @return JPanel
   */
  
  private JPanel createNorthPanel() {
    // Create the north panel 
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(true);
    
    // build & add label 
    JLabel label = new JLabel(
        "<html><span style='font-size:20px'>"
        + ((recipe == null) ? "" : recipe.getName() ) 
        + "</span></html>",
        SwingConstants.LEFT);
    panel.add(label,BorderLayout.PAGE_START);
    
    // build panel & add buttons
    JPanel buttonPanel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(buildNavButton("Edit"));
    buttonPanel.add(buildNavButton("Export"));
    buttonPanel.add(buildNavButton("Delete"));
    
    panel.add(buttonPanel,BorderLayout.PAGE_END);
    
    return panel;
  }

  /**
   * Method to center panel that will contain the Recipe data.
   * 
   * @return JScrollPane
   */
  private JScrollPane createCenterPanel() {
    // create the text are to contain the information
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    StyledDocument doc = textPane.getStyledDocument();
    addStylesToDocument(doc);

    /* insert formatted text
     * Styles created:
     *    "regular", "italic","italicsmall","italicsmallbold","bold", 
     *    "boldlarge","small","large",
     */
    if (recipe != null) {
      String nl = new String("\n");
      insertString(doc,recipe.getName() + nl,"boldlarge");
      insertString(doc,"By: " ,"italicsmallbold");
      insertString(doc,recipe.getAuthor() + nl + nl,"small");
      //insertString(doc,"Description:\t" ,"italicbold");
      insertString(doc,recipe.getDescription() + nl + nl,"regular");
      insertString(doc,"Source: " ,"italicsmallbold");
      insertString(doc,recipe.getSource() + nl,"small");
      insertString(doc,"Cook Time:\t" ,"italicbold");
      insertString(doc,recipe.getCook_time() + nl, "regular");
      insertString(doc,"Prep Time:\t" ,"italicbold");
      insertString(doc,recipe.getPrep_time() + nl, "regular");
      insertString(doc,"Difficulty:\t" ,"italicbold");
      insertString(doc,recipe.getDifficulty() + nl, "regular");
      insertString(doc,"Ingredients:" + nl,"italicbold");
      for (Ingredient ingredient : recipe.getIngredients()) {
        insertString(doc,"\t" + ingredient.getName() + nl, "regular");
      }
      insertString(doc,"Procedures:" + nl,"italicbold");
      insertString(doc, recipe.getProcedures() + nl, "regular");
      
      // set the text pane to the first line
      textPane.setCaretPosition(0);
    }
    
    // Create the scrolled area
    JScrollPane scrollPane = new JScrollPane(textPane);
    
    return scrollPane;
  }

  /**
   * Method to structure the insert of the displayed information to the 
   *   Style Document.
   *
   * <p>Methods defined in Style Document as seen in addStylesToDocument:
   *   regular  : SanSerif  (all are based upon this & modified later)
   *   italic   
   *   bold
   *   boldlarge        :  font size 16
   *   small            :  font size 10
   *   italicsmall      :  font size 12
   *   italicsmallbold  :  font size 12
   *   italicbold 
   *   large            :  font size 16
   *   
   * 
   * @param doc             StyledDocument that has been "Styled"
   * @param string          String to display
   * @param string2         String of format
   */
  private void insertString(StyledDocument doc, String display, String format) {
    try {
      /* method explained:
       *  Parameter 1 <String to write>
       *  Parameter 2 <format type>
       */
      doc.insertString(
          doc.getLength(),
          display,
          doc.getStyle(format));
    } catch (BadLocationException exception) {
      PopUp.exception(this, exception);
    }
  }
}
