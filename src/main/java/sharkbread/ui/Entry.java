package sharkbread.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class for add new field.
 * 
 * @author Obinna Ojialor
 */
public class Entry extends JPanel {
  private static final long serialVersionUID = -7543351226139978896L;
  private JTextField textField;
  private JButton plus;
  private JButton minus;
  private EntryList parent;

  /**
   * Constructor method to create two (2) buttons ('-','+') and a JTextField
   * @param textFieldText Sting value 
   * @param list EntryList to associate as the parent of this Entry
   */
  public Entry(String textFieldText, EntryList list) {

    setLayout(new BorderLayout());
    this.parent = list;
    this.plus = new JButton(new AddEntryAction());
    plus.setSize(new Dimension(0, 1));
    this.minus = new JButton(new RemoveEntryAction());
    this.textField = new JTextField(40);
    this.setField(textFieldText);
    
    JPanel panel = new JPanel(new FlowLayout());
    panel.add(plus);
    panel.add(minus);
    add(panel,BorderLayout.LINE_START);
    add(this.textField);
  }


  // wait for click "+" then add new field
  public class AddEntryAction extends AbstractAction {

    private static final long serialVersionUID = 9007319513607797216L;

    public AddEntryAction() {
      super("+");
    }

    public void actionPerformed(ActionEvent event) {
      parent.cloneEntry(Entry.this);
      parent.repaint();
    }

  }

  public class RemoveEntryAction extends AbstractAction {

    private static final long serialVersionUID = 6804252255410189204L;

    public RemoveEntryAction() {
      super("-");
    }

    public void actionPerformed(ActionEvent event) {
      parent.removeItem(Entry.this);
      parent.repaint();
    }
  }

  // get access for add
  public void enableAdd(boolean enabled) {
    this.plus.setEnabled(enabled);
  }

  // get access for delete
  public void enableMinus(boolean enabled) {
    this.minus.setEnabled(enabled);
  }
  
  /**
   * Method to fecth the string contents of the field.
   * @return String
   */
  public String getField(){
    return this.textField.getText();
  }
  
  /**
   * Method to set the string of the field.
   * @param string string 
   */
  public void setField(String string) {
    this.textField.setText(string);
  }
}
