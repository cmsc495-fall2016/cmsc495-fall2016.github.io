package cmsc495.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import cmsc495.database.Recipe;

/**
 * TODO OO: populate method headers & comment throughout
 * TODO AH: make the display resize nicely
 * TODO : implement ingredients
 * TODO : implement ingredient new field creator from the page ie start with one field and add more at the discretion of the user
 *  
 * @author Obinna Ojialor
 *
 */
public class Page_CreateEdit extends Page implements ActionListener {

  private static final long serialVersionUID = 1L;
  private Recipe recipe;

  private JLabel author;
  private JLabel prepTime;
  private JLabel cookTime;
  private JLabel recipeName;
  private JLabel procedures;
  private JLabel description;
  private JLabel difficulty;

  private JTextField autho;
  private JTextField prepTim;
  private JTextField cookTim;
  private JTextField recipeNam;
  private JTextField descriptio;
  private JTextArea procedure;

  public Page_CreateEdit(String title) {
    super(title);



    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
    JPanel Panel = createNorthPanel();
    this.add(Panel, BorderLayout.NORTH);

  }


  public Page_CreateEdit(Recipe recip) {
    super("Ediit");
    this.recipe = recip;

    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
    JPanel Panel = createNorthPanel();
    this.add(Panel, BorderLayout.NORTH);
    setall();
  }

  private void setall() {
    this.autho.setText(String.format("%s", this.recipe.getAuthor()));
    this.prepTim.setText(String.format("%s", this.recipe.getPrep_time()));
    this.cookTim.setText(String.format("%s", this.recipe.getCook_time()));
    this.recipeNam.setText(this.recipe.getName());
    this.descriptio.setText(this.recipe.getDescription());
    this.procedure.setText(this.recipe.getProcedures());
  }

  private JPanel createNorthPanel() {
    // Create the north panel
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(true);

    // build & add label

    panel.setLayout(null);
    // build & add button
    JButton button = new JButton();
    button.setText("<HTML><FONT color=\"#000099\"><U>Save</U></FONT></HTML>");
    button.setHorizontalAlignment(SwingConstants.LEFT);
    button.setBorderPainted(false);
    button.setOpaque(false);
    button.setBackground(Color.WHITE);
    button.addActionListener(this);
    button.setActionCommand("Save");
    button.setBounds(600, 0, 60, 20);
    panel.add(button, BorderLayout.LINE_END);

    this.recipeNam = new JTextField();
    this.recipeName = new JLabel("Recipe Name:");
    this.recipeName.setBounds(10, 0, 80, 20);
    this.recipeNam.setBounds(90, 0, 150, 20);



    this.autho = new JTextField();
    this.author = new JLabel("Author:");
    this.author.setBounds(10, 20, 80, 20);
    this.autho.setBounds(90, 20, 150, 20);

    this.cookTim = new JTextField();
    this.cookTime = new JLabel("Cook Time:");
    this.cookTime.setBounds(10, 40, 80, 20);
    this.cookTim.setBounds(90, 40, 150, 20);

    this.prepTim = new JTextField();
    this.prepTime = new JLabel("Prep Time:");
    this.prepTime.setBounds(10, 60, 80, 20);
    this.prepTim.setBounds(90, 60, 150, 20);

    this.descriptio = new JTextField();
    this.description = new JLabel("Description:");
    this.description.setBounds(10, 80, 80, 20);
    this.descriptio.setBounds(90, 80, 300, 20);

    this.procedure = new JTextArea();
    this.procedures = new JLabel("Procedures:");
    this.setBounds(10, 100, 80, 20);

    JScrollPane sp = new JScrollPane(this.procedure);
    sp.setBounds(10, 130, 600, 300);

    panel.add(this.author);
    panel.add(this.prepTime);
    panel.add(this.cookTime);
    panel.add(this.recipeName);
    panel.add(this.procedures);
    panel.add(this.description);


    panel.add(this.autho);
    panel.add(this.prepTim);
    panel.add(this.cookTim);
    panel.add(this.descriptio);
    panel.add(this.recipeNam);
    panel.add(sp);

    return panel;
  }// end createNorthPanel


  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    if (e.getSource() instanceof JButton) {
      switch (e.getActionCommand()) {
        case "Save":
          // TODO save actions go here; may need two types 1) update if editing 2) creating if not
          // editing
          break;
        default:
          PopUp.Warning(this, "Button action undefined",
              "There is no action defined for " + e.getActionCommand());
      }// end switch
    } // end test of JButton
    else {
      PopUp.Warning(this, "Action listener undefined",
          "There is no action defined for " + e.getSource());
    }
  }

}
