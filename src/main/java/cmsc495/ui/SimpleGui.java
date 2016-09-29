/**
 * Class to construct the GUI
 */
package cmsc495.ui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-20
 */

public class SimpleGui extends JFrame implements ActionListener {
  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = 6580341315626032237L;
  /**
   * Private variables
   */
  private JPanel mainPanel, currentPage;
  private Map<JButton,Pages> buttonMap = new HashMap<JButton, Pages>();
  
  public SimpleGui() {
    // set the title
    this.setTitle("Shark Bread's Recipe Repository");
    // instantiate the JPanel to hold any and all JFrames
    mainPanel = new JPanel(new BorderLayout());

    // add the header frame
    mainPanel.add(getHeader(), BorderLayout.PAGE_START);
    
    // add the footer frame
    mainPanel.add(getFooter(), BorderLayout.PAGE_END);
    
    // add the navigation frame
    mainPanel.add(getNavigation(), BorderLayout.LINE_START);
    
    // add the main page
    setCurrentPage(new Page_Main());
   
    // set configurations
    setCommonActions_JFrame(this);
    setSize(800, 600);
    
    // add the page container
    this.add(mainPanel);
  }// end default constructor SimpleGui()

  /**
   * Add in the ActionListener methods required from implementation 
   */
  public void actionPerformed(ActionEvent e) {
    // Determine what the ActionEvent is
    if (e.getSource() instanceof JButton){
      JButton button = (JButton) e.getSource();
      
      // Determine if the button is in the buttonMap
      if (buttonMap.containsKey( button ) ){
        Pages enumPage = (Pages) buttonMap.get(button);
        
        // pull the panel from the enum
        Page page = enumPage.getPanel();
        
        // set the panel to the main page
        setCurrentPage( page );
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
  /**
   * method to create and return the navigation panel which allows the
   *   calls the changing of pages
   * @return Jpanel
   */
  private JPanel getNavigation() {
    // utilize Grid layout(<any number of rows>,<column>)
    JPanel panel = new JPanel( );
    panel.setLayout((LayoutManager) new BoxLayout(panel, BoxLayout.Y_AXIS));
    
    // Build buttons for all the enumerated Pages
    for (Pages enumPage : Pages.values()){
      JButton button = buildNavButton(enumPage.toString());
      
      /*
       * set the button into the hashmap buttonMap
       *   to be utilized by the actionlistener 
       */
      buttonMap.put(button, enumPage);

      // add the action listeners to the buttons
      button.addActionListener(this);
      
      // add the buttons
      panel.add(button);
      
    }// end for loop of the buttons

    // set the titled border
    setBorder_titled(panel, "Navigation");
    
    return panel; 
  }// end getNavigation

  /**
   * Method to generate web-like link buttons
   * @param string
   * @return JButton
   */
  private JButton buildNavButton(String string) {
    // Creates a button that resembles a webpage's link
    JButton button = new JButton();
    button.setText(
        String.format(
            "<HTML><FONT color=\"#000099\"><U>%s</U></FONT></HTML>",
            string)
        );
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    
    return button;
  }

  /**
   * Method to to create a label that spans the width of the 
   * frame to represent the footer & return said footer
   * @return JFrame
   */
  private Component getFooter() {
    JPanel panel = new JPanel();
    JLabel label = new JLabel("\u00a9 2016 UMUC CMSC 495");
    panel.add(label);
    setBorder(panel);
    
    return panel;
  }// end getFooter

  /**
   * Method to create and return the header
   * @return JFrame
   */
  private Component getHeader() {
    JPanel panel = new JPanel();
    JLabel label = new JLabel("Shark Bread's Recipe Repository");
    panel.add(label);
    setBorder(panel);
    
    return panel;
  }// end getHeader
  
  /**
   * Method set what page is currently shown in the BorderLayout.Center
   *    of the SimpleGui
   * @param page
   */
  public void setCurrentPage(Page page) {
    // remove the current page if it has been defined
    if (currentPage != null)
    {
      mainPanel.remove(currentPage);
    }
    
    // set the @param page to the current page
    currentPage = page;

    // set the border; titled if defined
    if (page.getTitleName() != null)
    {
      setBorder_titled(page, page.getTitleName());
    }else{
      setBorder(page);
    }
    
    // add the panel to the main panel
    mainPanel.add(currentPage,BorderLayout.CENTER);
    
    // revalidate the JFrame
    revalidate();

  }// end setCurrentPage

  /**
   * Method to have all the JFrames have the same common actions applied
   * @param frame
   */
  private static void setCommonActions_JFrame(JFrame frame) {
    // ensure the close operations are set accordingly
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Size the frame
    frame.pack();
    
    // Set it visible
    frame.setVisible(true);
    
  }//end setCommonActions_JFrame

  /* *******************************************************
   * Methods to set borders to JPanels
   */
  /**
   * Method to keep all the borders the same
   * @return
   */
  private Border getCommonBorder() {
    return BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
  }//end getCommonBorder
  
  /**
   * Method to set a border to a jpanel
   * @param panel
   */
  private void setBorder(JPanel panel){
    panel.setBorder( getCommonBorder() );
  }//end setTitleBorder

  /**
   * Method to set a border with a title to a jpanel
   * @param panel
   */
  private void setBorder_titled(JPanel panel, String string){
    TitledBorder title = BorderFactory.createTitledBorder(getCommonBorder(), string);
    Font titleFont = UIManager.getFont("TitledBorder.font");
    title.setTitleFont( titleFont.deriveFont(Font.ITALIC + Font.BOLD) );
    panel.setBorder( title );
  }//end setTitleBorder

}//end class SimpleGui
