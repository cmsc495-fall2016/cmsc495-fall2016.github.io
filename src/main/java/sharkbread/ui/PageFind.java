/**
 * Class to find the displaying recipes
 * 
 * Design: Two (2) panels to add to the main panel
 *  1) Panel: Jlable : buttons (Find)
 *  2) Scrollable panel:JTable added into the center
 * */
package sharkbread.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import sharkbread.database.Recipe;
/*
* @author Obinna Ojialor
 */
public class PageFind  extends Page {
	
	private  List<Recipe> listRecipes;
        private  ArrayList <Integer>  listnubers;
	private  JTextField Fieldfind;
	private  JButton Buttonfind;
	private JTable table;
	private  DefaultTableModel dtm;
    /**
   * Constructor method to find recipe.
   * @param  Recipe to display
   */
	public PageFind(String title) {
		    super(title);
                      // set layout manager
		    this.setLayout(new BorderLayout());
		      // create default TableModel
		    dtm = new DefaultTableModel(0, 0);
                    // create list nubers
		    listnubers=new ArrayList<Integer>();
                    // create list Recipes
                    listRecipes = new Recipe().getAll();
              
		    //create panel top
		    JPanel paneltop = new JPanel(new FlowLayout());
		    //top button and lable
		    Fieldfind=new JTextField("", 15);
		    Buttonfind=new JButton("Search");
                    //Action listener for display recipe 
		    Buttonfind.addActionListener(new ActionListener()
			  {
			    public void actionPerformed(ActionEvent e)
			    {
			    	//clean table
			    	while (dtm.getRowCount()>0) {
                                dtm.removeRow(0);
                                }
                                
			    	findall(Fieldfind.getText());
			    }
			  });
		    paneltop.add(Fieldfind);
		    paneltop.add(Buttonfind);
		    
		    //adding panel
		    this.add(paneltop,BorderLayout.NORTH);
		
                    
		      String columnheaders[] = { "Recipe Name", "Author", "Time","Difficulty","Ingredients"  };
		  
		   
		    
		    //create Jtable
		    table = new JTable(){
		         public boolean editCellAt(int row, int column, java.util.EventObject e) {
		             return false;
		          }
		       };
                    
                    //setings  Default Table Model 
		     dtm = new DefaultTableModel(0, 0);
		    dtm.setColumnIdentifiers(columnheaders);
		    table.setModel(dtm);
                    
                    //Create table
		    JScrollPane scrollPane = new JScrollPane(table);
			add(scrollPane,BorderLayout.CENTER);
                        //new mouse Listener when click on table disply recipe
                        MouseListener mouseListener = new MouseAdapter(){
                         public void mouseClicked(MouseEvent mouseEvent) {
                             
                             if (mouseEvent.getClickCount() == 1) {
                                 int selRow = table.getSelectedRow();
                               //check
                                if (selRow >= 0) {
                                  
                          
                                    Recipe recipe = (Recipe) listRecipes.get(listnubers.get(table.getSelectedRow()));
        
        
                                    SimpleGui gui = (SimpleGui)SwingUtilities.getRoot(table);
                                    gui.setCurrentPage(new PageDisplayRecipe(recipe));
                             }
                         }
                         }
                        };
			table.addMouseListener(mouseListener);
    
}
   
      
		    
		    
		    
		     
		    
	 //Add new raw
	private void Addraw(Recipe recipe,int n){
		dtm.addRow(new Object[]{recipe.getName(), recipe.getAuthor(), recipe.getCook_time(),recipe.getDifficulty(),recipe.getIngredients().get(5).getName()});
		listnubers.add(new Integer(n));
	}
	
        //find with keywords
	private void findall(String sr){
		int size=0;
		JPanel panelcenter = new JPanel();
		for (Recipe recipe : listRecipes) {
			
			if(recipe.getName().toLowerCase().contains(sr.toLowerCase())){
		      Addraw(recipe,size);
		      size++;
		      continue;
		    }
                        if (recipe.getAuthor().toLowerCase().contains(sr.toLowerCase())){
				Addraw(recipe,size);
			      size++;
			      continue;
			}
                     
                   
			
			for(int i=0;i<recipe.getIngredients().size();i++){
				if(recipe.getIngredients().get(i).getName().toLowerCase().contains(sr.toLowerCase())){
					Addraw(recipe,size);
				      size++;
				      continue;
				}
			}
                         try {
                         Integer.valueOf(sr);
                    } 
                    catch (NumberFormatException e) {
                        continue;
                    }
                    if(recipe.getCook_time()== Integer.valueOf(sr)){
		      Addraw(recipe,size);
		      size++;
		      continue;
		    }
                     if(recipe.getDifficulty()== Integer.valueOf(sr)){
		      Addraw(recipe,size);
		      size++;
		      continue;
		    }
			
	}
		if(size==0){
                dtm.addRow(new Object[]{"Not found", "Not found", "Not found","Not found","Not found"});

			
		}
		
	    
	}
}	 
	  
 
	 
	
	 
	
	

