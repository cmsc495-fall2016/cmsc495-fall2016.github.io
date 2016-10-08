package sharkbread.ui.support;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 * @version 1.0 11/09/98
 */

public class ButtonEditor extends DefaultCellEditor implements ActionListener{
  private static final long serialVersionUID = -4999912757120204926L;

  /* Maps to store any commands associated to the buttons */
  private Map<String,Object> label2Object = new HashMap<String,Object>();
  private Map<String,Method> label2Method = new HashMap<String,Method>();
  private Map<String,Object[]> label2parameters = new HashMap<String,Object[]>();

  protected JButton button;

  private String label;

  private boolean isPushed;

  public ButtonEditor(JCheckBox checkBox) {
    super(checkBox);
    button = new JButton();
    button.setOpaque(true);
    button.addActionListener(this);
  }

  /**
   * Implemented methods for implementing {@link ActionListener}
   */
  @Override
  public void actionPerformed(ActionEvent arg0) {
    fireEditingStopped();
  }
  
  /**
   * Override'n method in {@link DefaultCellEditor}.
   * Sets the button background & foreground
   */
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
      int row, int column) {
    if (isSelected) {
      button.setForeground(table.getSelectionForeground());
      button.setBackground(table.getSelectionBackground());
    } else {
      button.setForeground(table.getForeground());
      button.setBackground(table.getBackground());
    }
    label = (value == null) ? "" : value.toString();
    button.setText(label);
    isPushed = true;
    return button;
  }

  /**
   * Override'n method in {@link DefaultCellEditor}.
   * Allows action to happen based upon button selection
   */
  @Override
  public Object getCellEditorValue() {
    if (isPushed) {
      /* this is the tricky part */
      if (label2Object.containsKey(label) 
          && label2Method.containsKey(label)
          && label2parameters.containsKey(label)) {
        Method method = label2Method.get(label);
        Object[] parameters = label2parameters.get(label);
        Object object = label2Object.get(label);
        try {
          method.invoke(object, parameters);
        } catch (
            IllegalAccessException | IllegalArgumentException | InvocationTargetException 
            exception) {
          // Should never get here, but just in case
          exception.printStackTrace();
        }
      }
    }
    isPushed = false;
    return new String(label);
  }


  /**
   * Override'n method in {@link DefaultCellEditor}.
   * Assists in controlling the isPushed attribute
   */
  public boolean stopCellEditing() {
    isPushed = false;
    return super.stopCellEditing();
  }


  /**
   * Override'n method in {@link DefaultCellEditor}.
   * Stops the execution of the button
   */
  protected void fireEditingStopped() {
    try{
      super.fireEditingStopped();
    } catch (ArrayIndexOutOfBoundsException exception) {
      // this is a catch for any removed elements
    }
  }

  /**
   * Custom method to allow referenced method calls from another object.
   * 
   * this was a pain to figure out.
   * @param object - Instance to call a method from
   * @param method - Method in a instance to call 
   * @param label - label to distinguish the command...implemented like {@link ActionListener}.setActionCommand
   */
  public void setActionCommand(Object object, Method method, String label)  {
    Object[] parameters = new Object[1];
    parameters[0] = label;
    label2Object.put(label, object);
    label2Method.put(label, method);
    label2parameters.put(label, parameters);
  }

}
