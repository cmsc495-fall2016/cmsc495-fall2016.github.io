/**
 * Class to create the main page Design is to be simple & show the logo, description
 */

package sharkbread.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Page Main class.
 * @author Adam Howell, Obinna Ojialor
 * @date 2016-09-19
 */
public class PageMain extends Page {

  /**
   * Generated serial ID.
   */
  private static final long serialVersionUID = 4670577079792236241L;
  public File logoFilePath = new File("src/main/resources/sharkbread-logo.jpg");
    
  /**
   * Child constructor for Page class.
   */
  public PageMain() {
    super("Main");
    this.setLayout((LayoutManager) new FlowLayout(FlowLayout.CENTER));
    setOpaque(true);
    add(createPanel());
  }

  /**
   * Creates the the main display panel.
   * @return {@link Component} to display in this class {@link JPanel}
   */
  private Component createPanel() {
    // attempt to load the image file ... otherwise load a text string
    JLabel logo;
    try {
      BufferedImage logoImage = ImageIO.read(logoFilePath);
      // Build the scaled image to half of the SimpleGui overall size
      ImageIcon imageIcon = new ImageIcon(
          new ImageIcon(logoImage).getImage().getScaledInstance(
              SimpleGui.initWidth / 2,
              SimpleGui.initHeight / 2,
              Image.SCALE_DEFAULT
          )
      );
      logo = new JLabel(imageIcon);
    } catch (IOException exception) {
      logo = new JLabel("SHARK BREAK RECIPE REPOSITORY");
    }
    
    // Create a label to contain string about the project
    JLabel info = new JLabel("Where it's your recipes");
    info.setHorizontalAlignment(JLabel.CENTER);
    
    // Add logo & info labels
    JPanel panel = new JPanel( new BorderLayout());
    panel.add(logo,BorderLayout.NORTH);
    panel.add(info, BorderLayout.CENTER);
    
    return panel;
  }

}
