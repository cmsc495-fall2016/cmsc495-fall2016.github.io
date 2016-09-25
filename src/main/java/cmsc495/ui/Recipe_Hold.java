/**
 * Place holder until method is defined by the interface team
 * Delete this once the interface team has provided the DAO
 */
package cmsc495.ui;

/**
 * @author Adam
 *
 */
public class Recipe_Hold {
  String author,prepTime,cookTime,recipeName,procedures,description;
  String[] ingredients;
  int id,serveSize,difficulty;
  private boolean fullRecipe = false;
  private boolean is_halal = false;
  private boolean is_kosher = false;
  private boolean is_vegan = false;
  private boolean has_gluten = false;
  
  public Recipe_Hold(
      int id,
      String recipeName,
      int serveSize,
      String author,
      String prepTime,
      String cookTime,
      int difficulty,
      String Procedures,
      String description,
      String[] ingredients){
    this.id          = id;
    this.author      = author;
    this.prepTime    = prepTime;
    this.cookTime    = cookTime;
    this.recipeName  = recipeName;
    this.procedures  = Procedures;
    this.description = description;
    this.ingredients = ingredients;
    this.serveSize   = serveSize;
    this.difficulty  = difficulty;
    this.fullRecipe = true;
  }
  public Recipe_Hold(
      int id,
      String recipeName,
      String description
      ){
      this.id          = id;
      this.recipeName  = recipeName;
      this.description = description;
      this.fullRecipe  = false;
    
  }
  
  public String   get_recipeName() {return recipeName;}
  public int      get_serveSize()  {return serveSize;}
  public String   get_author()     {return author;}
  public String   get_prepTime()   {return prepTime;}
  public String   get_cookTime()   {return cookTime;}
  public int      get_difficulty() {return difficulty;}
  public String   get_procedures() {return procedures;}
  public String   get_description(){return description;}
  public String[] get_ingredients(){return ingredients;}
  public int      get_id()         {return id;}
  public boolean  is_halal()       {return is_halal;}
  public boolean  is_kosher()       {return is_kosher;}
  public boolean  is_vegan()       {return is_vegan;}
  public boolean  has_gluten()       {return has_gluten;}
  public boolean  is_full()       {return fullRecipe;}
}
