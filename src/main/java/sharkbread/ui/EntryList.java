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
   *  clone object.
   *  
   * @param entry to clone
   */
  public void cloneEntry(Entry entry) {

    Entry theClone = new Entry("", this);

    addItem(theClone);
  }


  /**
   * add object.
   * @param entry Entry Object to add to this EntryList
   */
  public void addItem(Entry entry) {
    entries.add(entry);

    add(entry);
    refresh();
  }

  /**
   * remove object.
   * @param entry Entry Object to remove from this EntryList
   */
  public void removeItem(Entry entry) {
    entries.remove(entry);
    remove(entry);
    refresh();
  }


  /**
   * Method to refresh the EntryList.
   */
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
   * Method to fetch the entries controlled by this EntryList.
   * 
   * @return ArrayList of Entry's
   */
  public ArrayList<Entry> getList() {
    return entries;
  }

}
