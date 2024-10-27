package seedu.pill.util;

import seedu.pill.exceptions.ExceptionMessages;
import seedu.pill.exceptions.PillException;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Represents a list of items and provides methods to add, delete, list, and edit items.
 */
public class ItemMap implements Iterable<Map.Entry<String, TreeSet<Item>>> {
    private static final Logger LOGGER = PillLogger.getLogger();
    Map<String, TreeSet<Item>> items;

    /**
     * Constructor for ItemMap.
     * Initializes the internal Map to store items.
     */
    public ItemMap() {
        this.items = new LinkedHashMap<>();
        LOGGER.info("New ItemMap instance created");
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public Iterator<Map.Entry<String, TreeSet<Item>>> iterator() {
        return items.entrySet().iterator();
    }

    /**
     * Compares this ItemMap to the specified object for equality.
     *
     * <p>This method returns {@code true} if and only if the specified object
     * is also an ItemMap and both ItemMaps contain the same key-value pairs,
     * where keys are strings and values are sets of Item objects. The equality
     * of Item objects is determined by their own {@link Item#equals(Object)}
     * method.</p>
     *
     * @param obj the object to be compared for equality with this ItemMap
     * @return {@code true} if the specified object is equal to this ItemMap;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ItemMap itemMap) {
            return this.items.equals(itemMap.items);
        }
        return false;
    }

    /**
     * Adds a new item to the list.
     *
     * @param newItem The item to be added.
     */
    public void addItem(Item newItem) {
        String name = newItem.getName();
        int quantity = newItem.getQuantity();
        Optional<LocalDate> expiryDate = newItem.getExpiryDate();

        assert name != null && !name.trim().isEmpty() : "Item name cannot be null or empty";
        assert quantity > 0 : "Quantity must be positive";

        if (name == null || name.trim().isEmpty() || quantity <= 0) {
            LOGGER.warning("Attempt to add invalid item: name=" + name + ", quantity=" + quantity);
            System.out.println("Invalid item name or quantity.");
            return;
        }

        // If the item name exists, check for items with the same expiry date
        if (items.containsKey(name)) {
            TreeSet<Item> itemSet = items.get(name);
            boolean itemUpdated = false;

            // Check if an item with the same expiry date already exists
            for (Item item : itemSet) {
                if (item.getExpiryDate().equals(expiryDate)) {
                    int newQuantity = item.getQuantity() + quantity;
                    item.setQuantity(newQuantity);
                    itemUpdated = true;
                    expiryDate.ifPresentOrElse(
                            expiry -> {
                                LOGGER.info("Updated existing item with expiry date: " + newItem);
                                System.out.println("Item already exists with the same expiry date. Updated quantity: \n"
                                        + newItem);
                            },
                            () -> {
                                LOGGER.info("Updated existing item with no expiry date: " + newItem);
                                System.out.println("Item already exists with no expiry date. Updated quantity: \n"
                                    + newItem);
                            }
                    );
                    break;
                }
            }

            // If no item with the same expiry date, add a new one
            if (!itemUpdated) {
                itemSet.add(newItem);
                LOGGER.info("Added new item with different expiry date: " + newItem);
                System.out.println("Added new item with a different expiry date: \n"
                        + newItem);
            }
        } else {
            // If the item doesn't exist, create a new list for the item and add it
            TreeSet<Item> itemSet = new TreeSet<>();
            itemSet.add(newItem);
            items.put(name, itemSet);
            LOGGER.info("Added new item: " + newItem);
            System.out.println("Added the following item to the inventory: \n"
                    + newItem);
        }
    }

    /**
     * Adds a new item to the list. Does not print any output.
     *
     * @param newItem The item to be added.
     */
    public void addItemSilent(Item newItem) {
        String name = newItem.getName();
        int quantity = newItem.getQuantity();
        Optional<LocalDate> expiryDate = newItem.getExpiryDate();

        assert name != null && !name.trim().isEmpty() : "Item name cannot be null or empty";
        assert quantity > 0 : "Quantity must be positive";

        if (name == null || name.trim().isEmpty() || quantity <= 0) {
            LOGGER.warning("Attempt to silently add invalid item: name=" + name + ", quantity=" + quantity);
            return;
        }

        if (items.containsKey(name)) {
            TreeSet<Item> itemSet = items.get(name);
            boolean itemUpdated = false;
            for (Item item : itemSet) {
                if (item.getExpiryDate().equals(expiryDate)) {
                    int newQuantity = item.getQuantity() + quantity;
                    item.setQuantity(newQuantity);
                    itemUpdated = true;
                    LOGGER.fine("Silently updated existing item: " + name + ", new quantity: " + newQuantity);
                    break;
                }
            }
            if (!itemUpdated) {
                itemSet.add(newItem);
                LOGGER.fine("Silently added new item: " + newItem);
            }
        } else {
            TreeSet<Item> itemSet = new TreeSet<>();
            itemSet.add(newItem);
            items.put(name, itemSet);
            LOGGER.fine("Silently added new item: " + newItem);
        }
    }

    /**
     * Deletes an item from the list by its name.
     *
     * @param name       The name of the item to be deleted.
     * @param expiryDate The date of the item to be deleted.
     */
    public void deleteItem(String name, Optional<LocalDate> expiryDate) {
        assert name != null : "Item name cannot be null";

        if (name == null || name.trim().isEmpty()) {
            LOGGER.warning("Attempt to delete item with invalid name: " + name);
            System.out.println("Invalid item name.");
            return;
        }

        TreeSet<Item> itemSet = items.get(name);
        if (itemSet != null) {
            Item dummyItem = expiryDate.map(ex -> new Item(name, 0, ex))
                            .orElse(new Item(name, 0));
            Item removedItem = itemSet.ceiling(dummyItem);
            if (removedItem != null && removedItem.getExpiryDate().equals(expiryDate)) {
                itemSet.remove(removedItem);
                LOGGER.info("Deleted item: " + removedItem);
                System.out.println("Deleted the following item from the inventory: \n"
                        + removedItem);
                if (itemSet.isEmpty()) {
                    items.remove(name);
                }
            } else {
                LOGGER.warning("Attempt to delete non-existent item: " + removedItem);
                System.out.println("Item not found: " + removedItem);
            }
        } else {
            LOGGER.warning("Attempt to delete non-existent item: " + name);
            System.out.println("Item not found: " + name);
        }
    }

    /**
     * Edits an item by its name and expiry date.
     *
     * @param updatedItem The updated item that has a new quantity.
     */
    public void editItem(Item updatedItem) {
        String name = updatedItem.getName();
        int quantity = updatedItem.getQuantity();
        Optional<LocalDate> expiryDate = updatedItem.getExpiryDate();

        assert name != null && !name.trim().isEmpty() : "Item name cannot be null or empty";
        assert quantity > 0 : "Quantity must be positive";

        if (name == null || name.trim().isEmpty() || quantity <= 0) {
            LOGGER.warning("Attempt to edit item with invalid parameters: name=" + name + ", quantity=" + quantity);
            System.out.println("Invalid item name or quantity.");
            return;
        }

        TreeSet<Item> itemSet = items.get(name);
        boolean isUpdated = false;
        if (itemSet != null) {
            for (Item item : itemSet) {
                if (item.getExpiryDate().equals(expiryDate)) {
                    item.setQuantity(quantity);
                    isUpdated = true;
                }
            }
            if (isUpdated) {
                LOGGER.info("Edited item: " + updatedItem);
                System.out.println("Edited item: " + updatedItem);
            } else {
                LOGGER.warning("Attempt to edit non-existent item: " + updatedItem);
                System.out.println("Item not found: " + updatedItem);
            }
        } else {
            LOGGER.warning("Attempt to edit non-existent item: " + name);
            System.out.println("Item not found: " + name);
        }
    }

    /**
     * Lists all the items in the inventory.
     */
    public void listItems() {
        if (items.isEmpty()) {
            LOGGER.info("Attempted to list items, but inventory is empty");
            System.out.println("The inventory is empty.");
            return;
        }
        LOGGER.info("Listing all items in inventory");
        System.out.println("Listing all items:");
        int index = 1;
        for (Map.Entry<String, TreeSet<Item>> entry : items.entrySet()) {
            TreeSet<Item> itemSet = entry.getValue();
            for (Item item : itemSet) {
                System.out.println(index + ". " + item.toString());
                index++;
            }
        }
    }

    /**
     * Lists all items in this {@code ItemMap} that have expired or are expiring before the specified cutoff date.
     * <p>
     * Retrieves expired or expiring items from {@link #getExpiringItems(LocalDate)}, and logs a message
     * if there are no items meeting the specified date criteria. If there are items that have expired
     * (when {@code cutOffDate} is today) or will expire before {@code cutOffDate} (for a future date),
     * the method logs and prints a message listing these items.
     * </p>
     *
     * @param cutOffDate the date against which item expiry is checked. If the date is today,
     *                   the method lists items that have expired. If the date is in the future,
     *                   it lists items expiring before the cutoff date.
     */
    public void listExpiringItems(LocalDate cutOffDate) {
        ItemMap expiringItems = this.getExpiringItems(cutOffDate);
        if (expiringItems.isEmpty()) {
            if (cutOffDate.isEqual(LocalDate.now())) {
                LOGGER.info("There are no items that have expired.");
                System.out.println("There are no items that have expired.");
            } else {
                LOGGER.info("There are no items expiring before " + cutOffDate + ".");
                System.out.println("There are no items expiring before " + cutOffDate + ".");
            }
            return;
        }
        if (cutOffDate.isEqual(LocalDate.now())) {
            LOGGER.info("Listing all items that have expired");
            System.out.println("Listing all items that have expired");
        } else {
            LOGGER.info("Listing all items expiring before " + cutOffDate);
            System.out.println("Listing all items expiring before " + cutOffDate);
        }
        List<Item> itemList = expiringItems.items.values().stream()
                .flatMap(Collection::stream)
                .toList();

        IntStream.range(0, itemList.size())
                .forEach(i -> System.out.println((i + 1) + ". " + itemList.get(i)));

        System.out.println();
    }

    /**
     * Lists all the items in the inventory for restock command, given a threshold value.
     * Prints all items with quantity strictly less than threshold.
     *
     * @param threshold The minimum number of items before it is deemed to require replenishment.
     */
    public void listItemsToRestock(int threshold){
        try {
            if (items.isEmpty()) {
                LOGGER.info("Attempted to list items, but inventory is empty");
                return;
            }

            if (threshold < 0) {
                throw new PillException(ExceptionMessages.INVALID_QUANTITY);
            }

            List<Item> filteredItems = items.values().stream()
                    .flatMap(TreeSet::stream)
                    .filter(item -> item.getQuantity() <= threshold)
                    .toList();

            if (filteredItems.isEmpty()) {
                LOGGER.info(String.format("There are no items that have quantity less than %d.", threshold));
                System.out.printf("There are no items that have quantity less than %d:%n", threshold);

            } else {
                LOGGER.info(String.format("Listing all items that need too be restocked (less than %d):", threshold));
                System.out.printf("Listing all items that need too be restocked (less than %d):%n", threshold);
                IntStream.rangeClosed(1, filteredItems.size())
                        .forEach(i -> System.out.println(i + ". " + filteredItems.get(i - 1).toString()));
            }

        } catch (PillException e) {
            LOGGER.severe(e.getMessage());
            PillException.printException(e);
        }
    }

    /**
     * Finds an item in the list.
     *
     * @param itemName The name of the item.
     */
    public ItemMap findItem(String itemName) {
        assert itemName != null : "Item name cannot be null";

        ItemMap foundItems = new ItemMap();
        if (itemName == null || itemName.trim().isEmpty()) {
            LOGGER.warning("Attempt to find item with null or empty name");
            return foundItems;
        }
        LOGGER.info("Searching for items containing: " + itemName);
        for (Map.Entry<String, TreeSet<Item>> entry : items.entrySet()) {
            TreeSet<Item> itemSet = entry.getValue();
            if (itemSet.first().getName().toLowerCase().contains(itemName.toLowerCase())) {
                for (Item item : itemSet) {
                    foundItems.addItemSilent(item);
                }
            }
        }
        LOGGER.info("Found " + foundItems.items.size() + " items matching: " + itemName);
        return foundItems;
    }

    /**
     * Retrieves all items that expire before the cutOffDate from the item map.
     *
     * <p>This method iterates through all items in the item map and checks each item's expiry date.
     * If the expiry date is before the cut off date, the item is added to a new {@code ItemMap}
     * containing only the expiring items.</p>
     *
     * @param cutOffDate date before which all items are considered to be expiring
     * @return an {@code ItemMap} containing all items that are expiring.
     */
    public ItemMap getExpiringItems(LocalDate cutOffDate) {
        ItemMap expiringItems = new ItemMap();
        for (Map.Entry<String, TreeSet<Item>> entry : items.entrySet()) {
            TreeSet<Item> itemSet = entry.getValue();
            itemSet.stream()
                    .flatMap(item -> item.getExpiryDate().stream()
                            .filter(expiry -> expiry.isBefore(cutOffDate))
                            .map(expiry -> item))
                    .forEach(expiringItems::addItemSilent);
        }
        return expiringItems;
    }

    /**
     * Returns the total number of items in the map.
     * This counts each individual item, including those with different expiry dates.
     *
     * @return The total number of items in the ItemMap.
     */
    public int size() {
        int totalSize = 0;
        for (TreeSet<Item> itemSet : items.values()) {
            totalSize += itemSet.size();
        }
        return totalSize;
    }

    /**
     * Returns the set of items with the given name.
     *
     * @param itemName The name of the item to retrieve.
     * @return A TreeSet of items with the given name, or null if the item does not exist.
     */
    public TreeSet<Item> get(String itemName) {
        return items.getOrDefault(itemName, new TreeSet<>());
    }
}
