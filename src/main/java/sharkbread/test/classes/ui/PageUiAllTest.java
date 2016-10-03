package sharkbread.test.classes.ui;

import sharkbread.test.classes.AllTest;
import sharkbread.ui.Page;
import sharkbread.ui.PopUp;
import sharkbread.ui.SimpleGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class PageUiAllTest extends Page implements ActionListener {

  private static final long serialVersionUID = -8580704290926577189L;

  // build the all mighty all_test
  private AllTest test = new AllTest();

  /**
   * Method to construct a Page to wrap around the test cases.
   */
  public PageUiAllTest() {
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

  }

  /**
   * Method to define actions for the buttons in this Page.
   * 
   * @param event An event triggered by an object
   */
  public void actionPerformed( ActionEvent event ) {
    /*
     * Determine the source and act
     */
    if (event.getSource() instanceof JButton) {
      JButton button = (JButton) event.getSource();

      switch (button.getActionCommand()) {
        case "Create All Data":
          /*
           * Since this method is a long one, thread it out
           */
          CreateaAllDataRunner cad = new CreateaAllDataRunner(test);
          Thread thread = new Thread(cad);
          thread.start();
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
          try {
            String.format("Attempting to divide by zero: %s", 1 / 0);
          } catch (Exception e2) {
            e2.printStackTrace();
          }
          break;
        default:
          PopUp.error(this, "Error", "There is a missing action for the object in this page");
          break;
      }

    }

  }

  /**
   * Method to build buttons for this page & set action command.
   * 
   * @param commandAction string to set as display and to determine what action to perform
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
        "<html><span style='font-size:20px'>Testing Cases</span></html>",
        SwingConstants.LEFT);
    panel.add(label, BorderLayout.PAGE_START);

    // build panel & add buttons
    JPanel buttonPanel = new JPanel((LayoutManager) new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(buildNavButton("Create All Data"));
    buttonPanel.add(buildNavButton("Delete All Data"));
    buttonPanel.add(buildNavButton("Make an error print"));

    panel.add(buttonPanel, BorderLayout.PAGE_END);

    return panel;
  } // end createNorthPanel

  /**
   * Method to create a panel with a JTextPane & re-route the STDOU & STDERR to that JTextPane.
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
    PrintStream printStream = new PrintStream(new CustomOutputStream(textPane, doc));

    // re-assigns standard output stream and error output stream
    System.setOut(printStream);
    System.setErr(printStream);

    // Create the scrolled area
    JScrollPane scrollPane = new JScrollPane(textPane);

    return scrollPane;
  } // end createCenterPanel

  /**
   * Method to set the styles to the JTextPane Example obtained from
   * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/
   * tutorial/uiswing/examples/components/TextSamplerDemoProject/src/components/TextSamplerDemo.java
   * 
   * @param doc Styled Document to add definitions to for outputting
   */
  protected void addStylesToDocument(StyledDocument doc) {
    Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

    doc.addStyle("regular", def);
    StyleConstants.setFontFamily(def, "SansSerif");
  } // end addStylesToDocument

  /**
   * Main to call the SimpleGui.
   * @param args command line arguments
   */
  public static void main(String[] args) {
    /*
     * Call the tests in the actionPerformed(....) not here
     * SO ..... Can't touch this (oh-oh oh oh oh-oh-oh)
     */
    SimpleGui gui = new SimpleGui();
    gui.setVisible(true);
    gui.setCurrentPage(new PageUiAllTest());
  } // end main

}