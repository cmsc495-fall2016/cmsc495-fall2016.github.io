package cmsc495.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.StyledDocument;

public class Page_CreateEdit  extends Page implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Recipe_Hold recipe;
	
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
	    
	
	private JCheckBox fullRecipe;
	 private JCheckBox is_halal ;
	 private JCheckBox is_kosher;
	  private JCheckBox is_vegan;
	  private JCheckBox has_gluten;
	
	
	public Page_CreateEdit(String title) {
	    super(title);
	    
		   
		  
		    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
		    JPanel Panel = createNorthPanel();
		    this.add(Panel,BorderLayout.NORTH);
		    
	}
	
	
	public Page_CreateEdit(Recipe_Hold recip) {
		 super("Ediit");
		 this.recipe = recip;   
		  
		    this.setLayout((LayoutManager) new BoxLayout(this, BoxLayout.Y_AXIS));
		    JPanel Panel = createNorthPanel();
		    this.add(Panel,BorderLayout.NORTH);
		    setall();
	}
	private void setall(){ 
	this.autho.setText(this.recipe.author);;
	this.prepTim.setText(this.recipe.prepTime);
	this.cookTim.setText(this.recipe.cookTime);
	this.recipeNam.setText(this.recipe.recipeName);
	this.descriptio.setText(this.recipe.description);
	this.procedure.setText(this.recipe.procedures);
	this.fullRecipe.setSelected(this.recipe.is_full());
	this.is_halal.setSelected(this.recipe.is_halal()) ;
	this.is_kosher.setSelected(this.recipe.is_kosher());
	this.is_vegan.setSelected(this.recipe.is_vegan());
	this.has_gluten.setSelected(this.recipe.has_gluten());
	}
	 private JPanel createNorthPanel() {
		    // Create the north panel 
		    JPanel panel = new JPanel(new BorderLayout());
		    panel.setOpaque(true);
		    
		    // build & add label 
		    
		    panel.setLayout(null);
		    // build & add button
		    JButton button = new JButton();
		    button.setText(
		            "<HTML><FONT color=\"#000099\"><U>Save</U></FONT></HTML>"
		            );
		    button.setHorizontalAlignment(SwingConstants.LEFT);
		    button.setBorderPainted(false);
		    button.setOpaque(false);
		    button.setBackground(Color.WHITE);
		    button.addActionListener(this);
		    button.setBounds(600, 0, 60, 20);
		    panel.add(button,BorderLayout.LINE_END);
		    
		    this.recipeNam=new JTextField();
		    this.recipeName=new JLabel("Recipe Name:");
		    this.recipeName.setBounds(10, 0, 80, 20);
		    this.recipeNam.setBounds(90, 0, 150, 20);
		    
		    
		    
		    
		    this.autho=new JTextField();
		    this.author=new JLabel("Author:");
		    this.author.setBounds(10, 20, 80, 20);
		    this.autho.setBounds(90, 20, 150, 20);
		    
		    this.cookTim=new JTextField();
		    this.cookTime=new JLabel("Cook Time:");
		    this.cookTime.setBounds(10, 40, 80, 20);
		    this.cookTim.setBounds(90, 40, 150, 20);
		    
		    this.prepTim=new JTextField();
		    this.prepTime=new JLabel("Prep Time:");
		    this.prepTime.setBounds(10, 60, 80, 20);
		    this.prepTim.setBounds(90, 60, 150, 20);
		    
		    this.descriptio=new JTextField();
		    this.description=new JLabel("Description:");
		    this.description.setBounds(10, 80, 80, 20);
		    this.descriptio.setBounds(90, 80, 300, 20);
		    
		    this.procedure=new JTextArea();
		    this.procedures=new JLabel("Procedures:");
		    this.setBounds(10, 100, 80, 20);
		   
		    JScrollPane sp = new JScrollPane( this.procedure);
		    sp.setBounds(10, 130, 600, 300);
		    
		    
		     this.fullRecipe=new JCheckBox("Full Recipe");
		     this.fullRecipe.setBounds(500, 0, 90, 20);
			 this.is_halal=new JCheckBox("Halal");
			 this.is_halal.setBounds(300, 0, 90, 20);
			 this.is_kosher=new JCheckBox("Kosher");
			 this.is_kosher.setBounds(300, 20, 90, 20);
			 this.is_vegan=new JCheckBox("Vegan");
			 this.is_vegan.setBounds(300, 40, 90, 20);
			 this.has_gluten=new JCheckBox("Gluten");
			 this.has_gluten.setBounds(300, 60, 90, 20);
		    
		    
		    
		    
		    panel.add( this.author);
		    panel.add( this.prepTime);
		    panel.add( this.cookTime);
		    panel.add( this.recipeName);
		    panel.add( this.procedures);
		    panel.add( this.description);
		    
		    
		    panel.add( this.autho);
		    panel.add( this.prepTim);
		    panel.add( this.cookTim);
		    panel.add( this.descriptio);
		    panel.add( this.recipeNam);
		    panel.add(sp);
		    
		    panel.add( this.is_halal);
		    panel.add( this.is_kosher);
		    panel.add( this.is_vegan);
		    panel.add( this.has_gluten);
		    panel.add(this.fullRecipe);
		    return panel;
		  }// end createNorthPanel
		  /**
		   * Method to center panel that will contain the Recipe data
		   * @param recipe
		   * @return 
		   */
		
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
