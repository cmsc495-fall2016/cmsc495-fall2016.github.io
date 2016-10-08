package sharkbread.ui.support;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
/**
 * Class to implement a button in a {@link JTable}.
 * @version 1.0 11/09/98
 * @author http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
 * @author Adam Howell
 */

public class ButtonRenderer  extends JButton implements TableCellRenderer {
  private static final long serialVersionUID = 468722949645401425L;

  public ButtonRenderer() {
    setOpaque(true);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(UIManager.getColor("Button.background"));
    }
    setText((value == null) ? "" : value.toString());
    return this;
  }
}