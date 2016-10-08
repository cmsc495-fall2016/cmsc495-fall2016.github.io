package sharkbread.ui.support;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @version 1.0 11/09/98
 * @author http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
 */
public class JButtonTableExample extends JFrame {
  private static final long serialVersionUID = -2021937300647258207L;

  public JButtonTableExample() {
    super("sharkbread - JTableButton Example");

    JTable table = new JTable();
    DefaultTableModel dm = new DefaultTableModel();
    //dm.setDataVector(
    //    new Object[][] { {"button 1", "foo"}, {"button 2", "bar"} },
    //    new Object[] {"Button", "String"});
    
    table.setModel(dm);
    
    //set the column headers
    dm.setColumnIdentifiers(new String[]{"Add Row","Remove Row","Name","Description"});
    
    // set the first row
    String[] blankRow = {"+","-","",""};
    dm.addRow(blankRow);

    // update the button row to be a button
    for (String rowName : new String[]{"Add Row","Remove Row"}) {
      table.getColumn(rowName).setCellRenderer(new ButtonRenderer());
      table.getColumn(rowName).setCellEditor(new ButtonEditor(new JCheckBox()));
    }
    
    // update the buttons columns to be a maximum width
    for (int integer : new int[]{0,1}) {
      table.getColumnModel().getColumn(integer).setMaxWidth(100);
    }
    
    
    
    
    
    
    
    //table.getColumn("Button").setCellRenderer(new ButtonRenderer());
    //table.getColumn("Button").setCellEditor(new ButtonEditor(new JCheckBox()));
    JScrollPane scroll = new JScrollPane(table);
    getContentPane().add(scroll);
    setSize(400, 100);
    setVisible(true);
  }

  public static void main(String[] args) {
    JButtonTableExample frame = new JButtonTableExample();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}