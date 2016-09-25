/**
 * Class to construct the GUI
 */
package cmsc495.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date   2016-09-20
 */
public class SimpleGui extends JFrame {

  /**
   * Default generated UID.
   */
  private static final long serialVersionUID = -2392353801787181754L;

  private JPanel wholePageContainer;
  private JFrame currentPage;
  
  public SimpleGui() {
    // instantiate the JPanel to hold any and all JFrames
    wholePageContainer = new JPanel(new BorderLayout());

    // add the header frame
    wholePageContainer.add(getHeader(), BorderLayout.PAGE_START);
    
    // add the footer frame
    wholePageContainer.add(getFooter(), BorderLayout.PAGE_END);
    
    // add the navigation frame
    wholePageContainer.add(getNavigation(), BorderLayout.LINE_START);
    
    // add the main page
    setCurrentPage(new Page_Main());
  }// end default constructor SimpleGui()

  private Component getNavigation() {
    // TODO Auto-generated method stub
    return null;
  }// end getNavigation

  /**
   * Method to to create a label that spans the width of the 
   * frame to represent the footer & return said footer
   * @return JFrame
   */
  private Component getFooter() {
    JFrame frame = new JFrame("Footer");
    JLabel label = new JLabel("\u00a9 UMUC CMSC 495");
    frame.add(label);
    
    return frame;
  }// end getFooter

  /**
   * Method to create and return the header
   * @return JFrame
   */
  private Component getHeader() {
    JFrame frame = new JFrame("Header");
    JLabel label = new JLabel("Shark Bread's Recipe Repository");
    frame.add(label);
    
    return frame;
  }// end getHeader

  /**
   * Method set what page is currently shown in the BorderLayout.Center
   *    of the SimpleGui
   * @param page
   */
  private void setCurrentPage(JFrame page) {
    // remove the current page if it has been defined
    if (currentPage.getTitle() != "")
    {
      wholePageContainer.remove(currentPage);
    }
    
    // set the @param page to the current page
    currentPage = page;
    wholePageContainer.add(currentPage,BorderLayout.CENTER);
    
    // Set the common actions of a JFrame
    setCommonActions_JFrame(currentPage);
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

}//end class SimpleGui
