package sharkbread.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * This is the entry list class.
 *
 * @author Obinna Ojialor
 */

// List with fields
public class EntryList extends JPanel {
  private List<Entry> entries;
  // Replace with database stuff

  // Initialized new field
  public EntryList() {
    this.entries = new ArrayList<Entry>();
    setLayout(new GridLayout(0, 3));
    Entry initial = new Entry("", this);
    addItem(initial);
  }

  // clone object
  public void cloneEntry(Entry entry) {

    Entry theClone = new Entry("", this);

    addItem(theClone);
  }

  // add object
  private void addItem(Entry entry) {
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

}
