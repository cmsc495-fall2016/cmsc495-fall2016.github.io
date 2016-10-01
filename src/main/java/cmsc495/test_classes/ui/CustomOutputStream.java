package cmsc495.test_classes.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 * 
 * @author www.codejava.net
 *
 */
class CustomOutputStream extends OutputStream {
  private StyledDocument styledDocument;
  private JTextPane textpane;
  private PrintStream originalSysOut;
  private PrintStream originalSysErr;

  public CustomOutputStream(JTextPane textpane, StyledDocument styledDocument) {
    this.textpane = textpane;
    this.styledDocument = styledDocument;
    originalSysOut = System.out;
    originalSysErr = System.err;
  } // end constructor

  /**
   * Method to write data to the StyledDocument.
   */
  @Override
  public void write(int integer) throws IOException {
    // redirects data to the text area
    try {
      styledDocument.insertString(styledDocument.getLength(), String.valueOf((char) integer),
          styledDocument.getStyle("regular"));
    } catch (BadLocationException exception) {
      /*
       * not sure what would happen here this class is utilized to re-route the stdout & stderr to
       * the JTextPane. if the try fails ... then it is going to attempt to write it again this
       * could be very bad it the issue occurs ... but how to test it?
       * 
       */
      System.setErr(originalSysErr);
      System.setOut(originalSysOut);
      exception.printStackTrace();
    } // end try & catch

    // scrolls the text area to the end of data
    textpane.setCaretPosition(styledDocument.getLength());
  } // end write
}