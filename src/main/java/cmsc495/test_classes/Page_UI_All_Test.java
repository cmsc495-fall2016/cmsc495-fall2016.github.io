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
   * 
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
    
    // add panels 
    this.add(northPanel, BorderLayout.NORTH);
    this.add(centerPanel, BorderLayout.CENTER);
    
  }// end constructor Page_DisplayRecipe
  
  
  public void actionPerformed(ActionEvent e) {
    /*
     * Determine the source and act 
     * NOTE: May need to change to  
     */
    if (e.getSource() instanceof JButton){
      JButton button = (JButton) e.getSource();

      switch (button.getActionCommand()){
        case "Create All Data": 
          CreateaAllData cad = new CreateaAllData(test);
          Thread t = new Thread(cad);
          t.start();
          /*try {
            test.createaAllData(null);
          } catch (SQLException e1) {
            e1.printStackTrace();
          } catch (IOException e1) {
            e1.printStackTrace();
          }*/
          break;
        case "Delete All Data": 
          try {
            test.deleteAllData();
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
          break;
      }
      
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
        "<html><span style='font-size:20px'>Testing Cases</span></html>",
        SwingConstants.LEFT);
    panel.add(label,BorderLayout.PAGE_START);
    
    // build panel & add buttons
    JPanel buttonPanel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(buildNavButton("Create All Data"));
    buttonPanel.add(buildNavButton("Delete All Data"));
    
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
    /*
     * Call the tests in the actionPerformed(....)
     * SO ..... Can't touch this (oh-oh oh oh oh-oh-oh)
     */
    SimpleGui gui = new SimpleGui();
    gui.setVisible(true);
    gui.setCurrentPage(new Page_UI_All_Test());
  }

}

/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * @author www.codejava.net
 *
 */
class CustomOutputStream extends OutputStream {
    private StyledDocument doc;
    private JTextPane textpane;
     
    public CustomOutputStream(JTextPane textpane, StyledDocument doc) {
      this.textpane = textpane;  
      this.doc = doc;
    }
     
    @Override
    public void write(int b) throws IOException {
      // redirects data to the text area
      try {
        doc.insertString(
            doc.getLength(),
            String.valueOf((char)b),
            doc.getStyle("regular")
            );
      } catch (BadLocationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // scrolls the text area to the end of data
      textpane.setCaretPosition(doc.getLength());  
      //textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
class CreateaAllData implements Runnable{
  All_Test test;

  public CreateaAllData(All_Test test) {
    this.test = test;
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    try {
      test.createaAllData(null);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("---------\nCreateAllData Thread Actions Complete");
  }
  
}