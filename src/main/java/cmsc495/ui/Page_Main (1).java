/**
 * Class to create the main page Design is to be simple & show the logo, description
 */
package cmsc495.ui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Adam Howell, Obinna Ojialor
 * @date 2016-09-19
 */
public class Page_Main extends Page {

  /**
   * Generated serial ID
   */
  private static final long serialVersionUID = 4670577079792236241L;

  public Page_Main() {
    super("Main");
    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
    JPanel Panels = createPanel();
    this.add(Panels);
  }// end constructor

  private JPanel createPanel() {
    // Create the north panel
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(true);

    // build & add label

    panel.setLayout(null);
    // build & add button
    // TODO add group logo
    JLabel logo = new JLabel("SHARK BREAK RECIPE REPOSITORY");
    logo.setBounds(300, 200, 200, 20);
    panel.add(logo);
    return panel;
  }// end createNorthPanel

}// end class Page_Main
