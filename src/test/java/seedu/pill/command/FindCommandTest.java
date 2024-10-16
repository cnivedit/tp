package seedu.pill.command;

import seedu.pill.exceptions.PillException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import seedu.pill.util.ItemMap;
import seedu.pill.util.Storage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FindCommandTest{
    @Test
    public void listFindEmptyNotFoundPasses() throws PillException {
        // Initialize test environment
        ItemMap itemMap = new ItemMap();
        Storage storage = new Storage();
        FindCommand findCommand = new FindCommand("abc");

        // Declare expected output
        String outputFromAddFoundTasks = "";
        String expectedOutput = outputFromAddFoundTasks + "The inventory is empty."
                + System.lineSeparator();

        // Redirect Output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Test command
        findCommand.execute(itemMap, storage);

        //Compare output
        String output = outputStream.toString();
        assertEquals(expectedOutput, output);
    }
    @Test
    public void listFindSimplePasses() throws PillException {
        // Initialize test environment
        ItemMap itemMap = new ItemMap();
        itemMap.addItem("Bandage", 20);
        itemMap.addItem("Syringe", 10);
        itemMap.addItem("Band-aid", 5);
        Storage storage = new Storage();
        FindCommand findCommand = new FindCommand("Band");

        // Declare expected output
        String expectedOutput = "Listing all items:" + System.lineSeparator() +
                "1. Bandage: 20 in stock" + System.lineSeparator() +
                "2. Band-aid: 5 in stock" + System.lineSeparator();

        // Redirect Output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Test command
        findCommand.execute(itemMap, storage);

        //Compare output
        String output = outputStream.toString();
        assertEquals(expectedOutput, output);
    }
    @Test
    public void listFindNotFoundPasses() throws PillException {
        // Initialize test environment
        ItemMap itemMap = new ItemMap();
        itemMap.addItem("Bandage", 20);
        itemMap.addItem("Syringe", 10);
        itemMap.addItem("Band-aid", 5);
        Storage storage = new Storage();
        FindCommand findCommand = new FindCommand("abc");

        // Declare expected output
        String outputFromAddFoundTasks = "";
        String expectedOutput = outputFromAddFoundTasks + "The inventory is empty."
                + System.lineSeparator();

        // Redirect Output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        // Test command
        findCommand.execute(itemMap, storage);

        //Compare output
        String output = outputStream.toString();
        assertEquals(expectedOutput, output);
    }
}
