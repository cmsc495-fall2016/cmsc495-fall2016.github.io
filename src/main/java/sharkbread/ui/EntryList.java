package sharkbread.ui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This is the entry list class.
 *
 * @author Obinna Ojialor
 */

// List with fields
public class EntryList extends JPanel {
  private static final long serialVersionUID = 8712758062786643007L;
  private ArrayList<Entry> entries;
  // Replace with database stuff

  // Initialized new field
  public EntryList() {
    this.entries = new ArrayList<Entry>();
    setLayout(new GridLayout(0, 1));
    Entry initial = new Entry("", this);
    addItem(initial);
  }

  // clone object
  public void cloneEntry(Entry entry) {

    Entry theClone = new Entry("", this);

    addItem(theClone);
  }

  // add object
  public void addItem(Entry entry) {
    entries.add(entry);

    add(entry);
    refresh();
  }

  // remove object
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
   * Method to fecth the entrylist
   * @return 
   * @return EntryList
   */
  public ArrayList<Entry> getList() {
    return entries;
  }

}
