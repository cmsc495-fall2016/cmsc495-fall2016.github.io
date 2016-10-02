package cmsc495.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *Class for add new field
* @author Obinna Ojialor
 */
public class Entry extends JPanel {
    private JTextField textField;
    private JButton plus;
    private JButton minus;
    private EntryList parent;
//create one field
    public Entry( String textFieldText, EntryList list) {
       
        this.parent = list;
        this.plus = new JButton(new AddEntryAction());
        this.minus = new JButton(new RemoveEntryAction());
        this.textField = new JTextField(10);
        this.textField.setText(textFieldText);
        add(this.plus);
        add(this.minus);
        add(this.textField);
    }

 
//wait for click "+" then add new field
    public class AddEntryAction extends AbstractAction {

        public AddEntryAction() {
            super("+");
        }

        public void actionPerformed(ActionEvent e) {
            parent.cloneEntry(Entry.this);
            parent.repaint();
        }

    }
  //wait for click "-" then delete new field
    public class RemoveEntryAction extends AbstractAction {

        public RemoveEntryAction() {
            super("-");
        }

        public void actionPerformed(ActionEvent e) {
            parent.removeItem(Entry.this);
            parent.repaint();
        }
    }

//get access for delete
    public void enableAdd(boolean enabled) {
        this.plus.setEnabled(enabled);
     
    }
//get access for delete
    public void enableMinus(boolean enabled) {
        this.minus.setEnabled(enabled);
      
    }
}