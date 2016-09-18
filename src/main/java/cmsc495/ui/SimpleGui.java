package cmsc495.ui;

import java.awt.BorderLayout;
import java.awt.Label;

import javax.swing.JFrame;

public class SimpleGui extends JFrame {

  /**
   * Default generated UID.
   */
  private static final long serialVersionUID = 1L;

  
  public SimpleGui() {
    super();
    
    
    // Source from https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
    
    //1. Create the frame.
    JFrame frame = new JFrame("FrameDemo");

    //2. Optional: What happens when the frame closes?
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //3. Create components and put them in the frame.
    //...create emptyLabel...
    frame.getContentPane().add(new Label("Sharkbread go!"), BorderLayout.CENTER);

    //4. Size the frame.
    frame.pack();

    //5. Show it.
    frame.setVisible(true);
  }
}
