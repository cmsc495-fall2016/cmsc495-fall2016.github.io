package sharkbread.ui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This is the entry list class.
 *
 * @author Obinna Ojialor
 */
public class EntryList extends JPanel {
  private static final long serialVersionUID = 8712758062786643007L;
  private ArrayList<Entry> entries;
  // Replace with database stuff

  /**
   * The default constructor.
   */
  public EntryList() {
    this.entries = new ArrayList<Entry>();
    setLayout(new GridLayout(0, 1));
    Entry initial = new Entry("", this);
    addItem(initial);
  }


  /**
   * Method to clone an item.
   * 
   * @param entry - Entry to clone.
   */
  public void cloneEntry(Entry entry) {

    Entry theClone = new Entry("", this);

    addItem(theClone);
  }


  /**
   * Method to add an item.
   * 
   * @param entry - Entry to add
   */
  public void addItem(Entry entry) {
    entries.add(entry);

    add(entry);
    refresh();
  }


  /**
   * Method to remove an item.
   * 
   * @param entry - Entry to remove
   */
  public void removeItem(Entry entry) {
    entries.remove(entry);
    remove(entry);
    refresh();
  }

  // refresh object
  private void refresh() {
    revalidate();

    if (entries.size() == 1) {
      entries.get(0).enableMinus(false);
    } else {
      for (Entry e : entries) {
        e.enableMinus(true);
      }
    }
  }

  /**
   * Method to fetch the entry list.
   * 
   * @return EntryList
   */
  public ArrayList<Entry> getList() {
    return entries;
  }

}
