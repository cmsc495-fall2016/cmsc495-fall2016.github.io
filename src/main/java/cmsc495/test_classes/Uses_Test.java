package cmsc495.test_classes;

import cmsc495.database.Database;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Tests the Uses table as a standalone. Does not integrate Recipe or Ingredient testing.
 * Requires Uses relationships arrive in a .csv. Support is provided for the 3 fields of our test input.
 *
 * @author Justin
 * @version 0.1 9/27/2016
 */

public class Uses_Test {

    private class Uses_CSV_Entry{
        int id = -1;
        int ingredient = -1;
        int recipe = -1;
    }

    private ArrayList<Uses_CSV_Entry> testData = new ArrayList<>();

    /**
     * Reads Uses information from a test file, populating our testData ArrayList
     * @param file          File containing Uses relationship information
     * @throws IOException  Standard IOException
     */
    private void populateTestData(File file) throws IOException {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] uses;
            reader.readNext(); // discard column header line
            while ((uses = reader.readNext()) != null) {
                Uses_CSV_Entry entry = new Uses_CSV_Entry();
                entry.id = Integer.parseInt(uses[0]);
                entry.ingredient = Integer.parseInt(uses[1]);
                entry.recipe = Integer.parseInt(uses[2]);
                testData.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Commit our test data to the Uses table, deleting all prior Uses first, as
     * we are in testing mode and want only this data in the table.
     * @throws SQLException Standard SQL Exception
     */
    private void updateIngredientTable() throws SQLException {
        System.out.println("[!] Deleting all prior uses entries (we are in test mode!)");
        Database uses = new Database();
        uses.clearUsesTable();
        for (Uses_CSV_Entry entry : testData){
            uses.createUsesEntry(entry.ingredient, entry.recipe);
        }
    }

    /**
     * Reads data from a .csv & populates the appropriate table in our database.
     * @param args          Standard cmdline arguments
     * @throws IOException  Standard IO Exception
     * @throws SQLException Standard SQL Exception
     */
    public static void main( String[] args ) throws IOException, SQLException {
        Uses_Test ut = new Uses_Test();
        System.out.println("[!] Begin ingestion of Uses test_classes data.");
        ut.populateTestData(new File("src/main/java/cmsc495/test_data/uses_data.csv"));
        System.out.println("[!] Test data read in; attempting to write to table");
        ut.updateIngredientTable();
        System.out.println("[!] If no stacktrace, assume db ingredient table is populated.");
    }

}
