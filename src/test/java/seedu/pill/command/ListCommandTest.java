package seedu.pill.command;

import seedu.pill.exceptions.PillException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import seedu.pill.util.ItemMap;
import seedu.pill.util.Storage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ListCommandTest {
    @Test
    public void listCommandEmptyPasses() throws PillException {
        // Initialize test environment
        ItemMap itemMap = new ItemMap();
        Storage storage = new Storage();
        ListCommand listCommand = new ListCommand();

        // Declare expected output
        String expectedOutput = "The inventory is empty." + System.lineSeparator();

        // Redirect Output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Test command
        listCommand.execute(itemMap, storage);

        //Compare output
        String output = outputStream.toString();
        assertEquals(expectedOutput, output);
    }
    @Test
    public void listCommandSimplePasses() throws PillException {
        // Initialize test environment
        ItemMap itemMap = new ItemMap();
        itemMap.addItem("Bandage", 20);
        itemMap.addItem("Syringe", 10);
        Storage storage = new Storage();
        ListCommand listCommand = new ListCommand();

        // Declare expected output
        String expectedOutput = "Listing all items:" + System.lineSeparator() +
                "1. Bandage: 20 in stock" + System.lineSeparator() +
                "2. Syringe: 10 in stock" + System.lineSeparator();

        // Redirect Output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Test command
        listCommand.execute(itemMap, storage);

        //Compare output
        String output = outputStream.toString();
        assertEquals(expectedOutput, output);
    }
}
