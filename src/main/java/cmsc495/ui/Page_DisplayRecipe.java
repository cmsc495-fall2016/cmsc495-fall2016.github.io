/**
 * Class to create the displaying page for recipes
 * 
 * Design: Two (2) panels to add to the main panel
 *  1) Panel: contains Recipe name & buttons (Edit)
 *  2) Scrollable panel:JTextArea added into the center
 */
package cmsc495.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import cmsc495.database.Ingredient;
import cmsc495.database.Recipe;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-19
 */
public class Page_DisplayRecipe extends Page implements ActionListener {

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = -3430232989667398763L;
  
  private Recipe recipe;
  
  public Page_DisplayRecipe(Recipe recipe) {
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
    
  }// end constructor Page_DisplayRecipe
  
  
  public void actionPerformed(ActionEvent e) {
    /*
     * Determine the source and act 
     * NOTE: May need to change to Action Commands
     */
    if (e.getSource() instanceof JButton){
      JButton button = (JButton) e.getSource();

      // set the panel to the main page 
      // TODO Update with appropriate pages
      SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(button);
      switch (button.getActionCommand()){
        case "Edit": 
          gui.setCurrentPage(new Page_CreateEdit(recipe));
          break;
        case "Export": 
          //gui.setCurrentPage(new Page_Export(recipe));
          PopUp.Warning(this, "Page not implemented yet", "This is a place holder for the export function");
          break;
        case "Delete": 
          //TODO make confirmations maybe have add to the popups with a confirm method
          PopUp.Warning(this, "Page not implemented yet", "This is a place holder for the delete function");
          break;
      }
      
    }//end if 
    
  }// end actionPerformed

  /**
   * Method to build buttons for this page & set action command
   * @param commandAction   string to set as display and to determine what action to perform
   * @return JButton
   */
  private JButton buildNavButton(String commandAction) {
    JButton button = new JButton();
    button.setText(
        String.format(
            "<HTML><FONT color=\"#000099\"><U>%s</U></FONT></HTML>",
            commandAction
            )
        );
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    button.setActionCommand(commandAction);
    button.addActionListener(this);
    
    return button;
  }
  /**
   * Method to create the North panel that will contain the Recipe Name & buttons to edit
   * @return JPanel
   */
  private JPanel createNorthPanel() {
    // Create the north panel 
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(true);
    
    // build & add label 
    JLabel label = new JLabel(
        "<html><span style='font-size:20px'>"+recipe.getName()+"</span></html>",
        SwingConstants.LEFT);
    panel.add(label,BorderLayout.PAGE_START);
    
    // build panel & add buttons
    JPanel buttonPanel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(buildNavButton("Edit"));
    buttonPanel.add(buildNavButton("Export"));
    buttonPanel.add(buildNavButton("Delete"));
    
    panel.add(buttonPanel,BorderLayout.PAGE_END);
    
    return panel;
  }// end createNorthPanel

  /**
   * Method to center panel that will contain the Recipe data
   * @param recipe
   * @return 
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
    String nl = new String ("\n");
    insertString(doc,recipe.getName() + nl,"boldlarge");
    insertString(doc,"By: " ,"italicsmallbold");
    insertString(doc,recipe.getAuthor() + nl + nl,"small");
    //insertString(doc,"Description:\t" ,"italicbold");
    insertString(doc,recipe.getDescription() + nl + nl,"regular");
    insertString(doc,"Cook Time:\t" ,"italicbold");
    insertString(doc,recipe.getCook_time() + nl, "regular");
    insertString(doc,"Prep Time:\t" ,"italicbold");
    insertString(doc,recipe.getPrep_time() + nl, "regular");
    insertString(doc,"Difficulty:\t" ,"italicbold");
    insertString(doc,recipe.getDifficulty() + nl, "regular");
    insertString(doc,"Ingredients:" + nl,"italicbold");
    for (Ingredient ingredient : recipe.getIngredients()){
      insertString(doc,"\t" + ingredient.getName() + nl, "regular");
    }
    insertString(doc,"Procedures:" + nl,"italicbold");
    insertString(doc, recipe.getProcedures() + nl, "regular");
    
    
    // Create the scrolled area
    JScrollPane scrollPane = new JScrollPane(textPane);
    
    return scrollPane;
  }// end createCenterPanel

  /**
   * Method to structure the insert of the displayed information to the 
   *   Style Document
   * 
   * Methods defined in Style Document as seen in addStylesToDocument:
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
    try{
      /* method explained:
       *  Parameter 1 <String to write>
       *  Parameter 2 <format type>
       */
      doc.insertString(
          doc.getLength(),
          display,
          doc.getStyle(format)
          );
    } catch (BadLocationException e){
      PopUp.Error(this, 
          "Error in doc.insertString",
          "Couldn't insert initial text into text pane."
          );
    }
  }


  /**
   * Method to set the styles to the JTextPane
   * Example obtained from 
   * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextSamplerDemoProject/src/components/TextSamplerDemo.java
   * @param doc
   */
  protected void addStylesToDocument(StyledDocument doc) {
    //Initialize some styles.
    int smallFontSize = 10;
    int regFontSize   = 12;
    int largeFontSize = 16;
    Style def = StyleContext.getDefaultStyleContext().
                    getStyle(StyleContext.DEFAULT_STYLE);

    Style regular = doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");

    // italics
    Style s = doc.addStyle("italic", regular);
    StyleConstants.setItalic(s, true);
    StyleConstants.setFontSize(s, regFontSize);
    
    s = doc.addStyle("italicsmall", regular);
    StyleConstants.setItalic(s, true);
    StyleConstants.setFontSize(s, smallFontSize);

    s = doc.addStyle("italicsmallbold", regular);
    StyleConstants.setItalic(s, true);
    StyleConstants.setFontSize(s, smallFontSize);
    StyleConstants.setBold(s, true);
    
    s = doc.addStyle("italicbold", regular);
    StyleConstants.setItalic(s, true);
    StyleConstants.setFontSize(s, regFontSize);
    StyleConstants.setBold(s, true);

    s = doc.addStyle("bold", regular);
    StyleConstants.setBold(s, true);
    
    s = doc.addStyle("boldlarge", regular);
    StyleConstants.setBold(s, true);
    StyleConstants.setFontSize(s, largeFontSize);

    s = doc.addStyle("small", regular);
    StyleConstants.setFontSize(s, smallFontSize);

    s = doc.addStyle("large", regular);
    StyleConstants.setFontSize(s, largeFontSize);
    
  }//end addStylesToDocument

  
  public static void main(String[] args) {

  }// end main
}//end class Page_DisplayRecipe
