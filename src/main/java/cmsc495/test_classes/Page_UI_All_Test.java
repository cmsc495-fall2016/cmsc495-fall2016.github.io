package cmsc495.test_classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import cmsc495.ui.Page;
import cmsc495.ui.SimpleGui;

public class Page_UI_All_Test extends Page implements ActionListener{

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = -8580704290926577189L;

    // build the all mighty all_test
    private All_Test test = new All_Test();

  public Page_UI_All_Test() {
    super("Test Running");
    
    // set layout manager
    setLayout(new BorderLayout());
    
    // create the north panel
    JPanel northPanel = createNorthPanel();
    
    // create the center panel
    JScrollPane centerPanel = createCenterPanel();
    
    // add panels to this panel
    this.add(northPanel, BorderLayout.NORTH);
    this.add(centerPanel, BorderLayout.CENTER);
    
  }// end constructor Page_DisplayRecipe
  
  /**
   * Method to define actions for the buttons in this Page
   * @param ActionEvent An event triggered by an object
   */
  public void actionPerformed(ActionEvent e) {
    /*
     * Determine the source and act 
     */
    if (e.getSource() instanceof JButton){
      JButton button = (JButton) e.getSource();

      switch (button.getActionCommand()){
        case "Create All Data": 
          /*
           * Since this method is a long one, thread it out
           */
          CreateaAllData cad = new CreateaAllData(test);
          Thread t = new Thread(cad);
          t.start();
          break;
        case "Delete All Data": 
          try {
            test.deleteAllData();
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
          break;
        case "Make an error print":
          System.err.println("a manual ErRoR PRINT");
          try{
            String.format("Attempting to divide by zero: %s", 1/0);
          }catch (Exception e2) {
            e2.printStackTrace();
          }
      }// end switch | case
      
    }//end if 
    
  }// end actionPerformed

  /**
   * Method to build buttons for this page & set action command
   * @param commandAction   string to set as display and to determine what action to preform
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
  }// end buildNavButton
  
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
        "<html><span style='font-size:20px'>Testing Cases</span></html>",
        SwingConstants.LEFT);
    panel.add(label,BorderLayout.PAGE_START);
    
    // build panel & add buttons
    JPanel buttonPanel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(buildNavButton("Create All Data"));
    buttonPanel.add(buildNavButton("Delete All Data"));
    buttonPanel.add(buildNavButton("Make an error print"));
    
    panel.add(buttonPanel,BorderLayout.PAGE_END);
    
    return panel;
  }// end createNorthPanel

  /**
   * Method to create a panel with a JTextPane & re-route the STDOU & STDERR to that JTextPane
   * @param Recipe
   * @return JScrollPane
   */
  private JScrollPane createCenterPanel() {
    // create the text are to contain the information
    JTextPane textPane = new JTextPane();
    textPane.setEditable(false);
    StyledDocument doc = textPane.getStyledDocument();
    addStylesToDocument(doc);

    /*
     * Pipe the Stdout & stderr to the JTextArea
     */
    PrintStream printStream = new PrintStream(new CustomOutputStream(textPane,doc));

    // re-assigns standard output stream and error output stream
    System.setOut(printStream);
    System.setErr(printStream);
    
    // Create the scrolled area
    JScrollPane scrollPane = new JScrollPane(textPane);
    
    return scrollPane;
  }// end createCenterPanel

  /**
   * Method to set the styles to the JTextPane
   * Example obtained from 
   * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TextSamplerDemoProject/src/components/TextSamplerDemo.java
   * @param doc
   */
  protected void addStylesToDocument(StyledDocument doc) {
    Style def = StyleContext.getDefaultStyleContext().
                    getStyle(StyleContext.DEFAULT_STYLE);

    doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");
  }//end addStylesToDocument

  
  public static void main(String[] args) {
    /*
     * Call the tests in the actionPerformed(....) not here
     * SO ..... Can't touch this (oh-oh oh oh oh-oh-oh)
     */
    SimpleGui gui = new SimpleGui();
    gui.setVisible(true);
    gui.setCurrentPage(new Page_UI_All_Test());
  }// end main

}

/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 *
 */
class CustomOutputStream extends OutputStream {
    private StyledDocument styledDocument;
    private JTextPane textpane;
    private PrintStream originalSysOut,originalSysErr;

    public CustomOutputStream(JTextPane textpane, StyledDocument styledDocument) {
      this.textpane = textpane;  
      this.styledDocument = styledDocument;
      originalSysOut = System.out;
      originalSysErr = System.err;
    }// end constructor
     
    /**
     * Method to write data to the StyledDocument
     */
    @Override
    public void write(int b) throws IOException {
      // redirects data to the text area
      try {
        styledDocument.insertString(
            styledDocument.getLength(),
            String.valueOf((char)b),
            styledDocument.getStyle("regular")
            );
      } catch (BadLocationException e) {
        /*
         *  not sure what would happen here
         *  this class is utilized to re-route the stdout & stderr
         *     to the JTextPane.
         *  if the try fails ... then it is going to attempt to write it again
         *  this could be very bad it the issue occurs ... but how to test it?
         *  
         */
        System.setErr(originalSysErr);
        System.setOut(originalSysOut);
        e.printStackTrace();
      }// end try & catch
      
      // scrolls the text area to the end of data
      textpane.setCaretPosition(styledDocument.getLength());  
    }// end write
}

/**
 * Method needed to thread out the All_Test class...it takes too long
 * @author Adam
 * @date   2016-09-29
 * @version 0.1
 * @category single usage
 */
class CreateaAllData implements Runnable{
  All_Test test;

  public CreateaAllData(All_Test test) {
    this.test = test;
  }

  /**
   * Interface method
   */
  @Override
  public void run() {
    try {
      test.createaAllData(null);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("---------\nCreateAllData Thread Actions Complete");
  }
  
}