package cmsc495.test_classes;

import cmsc495.database.Ingredient;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tests the Ingredient entity as a standalone. Does not integrate Recipe testing, also does not populate the 'uses'
 * table. Requires ingredients arrive in a .csv. Support is provided for the 3 fields of our test input.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */
public class Ingredient_Test {

    File ingredient_test_data = null;

    private class Ingredient_CSV_Entry {
        int id = -1;
        String name = null;
        String description = null;
    }

    private ArrayList<Ingredient_CSV_Entry> testData = new ArrayList<>();

    /**
     * Reads Ingredient information from a test file, populating our testData ArrayList
     * @param file          File containing Ingredient information
     * @throws IOException  Standard IOException
     */
    private void populateTestData(File file) throws IOException {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] ingredient;
            reader.readNext(); // discard title line
            reader.readNext(); // discard column titles line
            while ((ingredient = reader.readNext()) != null) {
                Ingredient_CSV_Entry entry = new Ingredient_CSV_Entry();
                entry.id = Integer.parseInt(ingredient[0]);
                entry.name = ingredient[1];
                entry.description = ingredient[2];
                testData.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Commit our test data to the Ingredients table, deleting all prior Ingredients first, as
     * we are in testing mode and want only this data in the table.
     * @throws SQLException Standard SQL Exception
     */
    private void updateIngredientTable() throws SQLException {
        System.out.println("[!] Deleting all prior ingredient entries (we are in test mode!)");
        Ingredient recipeDelete = new Ingredient();
        recipeDelete.clearIngredientTable();
        for (Ingredient_CSV_Entry entry : testData){
            Ingredient i = new Ingredient();
            i.createIngredient(entry.name, entry.description);
        }
    }

    /**
     * Reads data from a .csv & populates the appropriate table in our database.
     * @param args          Standard cmdline arguments
     * @throws IOException  Standard IO Exception
     * @throws SQLException Standard SQL Exception
     */
    public static void main( String[] args ) throws IOException, SQLException {
        Ingredient_Test it = new Ingredient_Test();
        System.out.println("[!] Begin ingestion of Ingredient test_classes data.");
        it.populateTestData(new File("src/main/java/cmsc495/test_data/ingredient_data.csv"));
        System.out.println("[!] Test data read in; attempting to write to table");
        it.updateIngredientTable();
        System.out.println("[!] If no stacktrace, assume db ingredient table is populated.");
    }


}
