import java.io.*;
import java.util.*;

/**
 * ================================================================
 * MAIN CLASS – BookMyStayApp
 * ================================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * Demonstrates saving system state (inventory + bookings)
 * to a file and restoring it on restart using serialization.
 *
 * @version 12.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // Try to restore previous state
        SystemState state = persistence.loadState();

        if (state == null) {
            // Fresh start
            System.out.println("Starting new system...");

            state = new SystemState();
            state.inventory.addRoom("Single", 2);
            state.inventory.addRoom("Double", 1);

            state.history.addBooking(new Reservation("Alice", "Single", "S101"));
        } else {
            System.out.println("System restored from file!");
        }

        // Display current state
        state.history.displayHistory();
        state.inventory.displayInventory();

        // Save state before exit
        persistence.saveState(state);

        System.out.println("\nSystem state saved successfully.");
    }
}

/**
 * ================================================================
 * CLASS – SystemState
 * ================================================================
 *
 * Description:
 * Wrapper class to hold complete system state.
 *
 * @version 12.0
 */

class SystemState implements Serializable {

    public Inventory inventory = new Inventory();
    public BookingHistory history = new BookingHistory();
}

/**
 * ================================================================
 * CLASS – Reservation
 * ================================================================
 *
 * Description:
 * Represents a confirmed booking.
 *
 * @version 12.0
 */

class Reservation implements Serializable {

    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

/**
 * ================================================================
 * CLASS – Inventory
 * ================================================================
 *
 * Description:
 * Maintains room availability.
 *
 * @version 12.0
 */

class Inventory implements Serializable {

    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " -> " + rooms.get(type));
        }
    }
}

/**
 * ================================================================
 * CLASS – BookingHistory
 * ================================================================
 *
 * Description:
 * Stores booking records.
 *
 * @version 12.0
 */

class BookingHistory implements Serializable {

    private List<Reservation> bookings = new ArrayList<>();

    public void addBooking(Reservation r) {
        bookings.add(r);
    }

    public void displayHistory() {
        System.out.println("\nBooking History:");
        for (Reservation r : bookings) {
            System.out.println(
                    r.getGuestName() + " | " +
                            r.getRoomType() + " | " +
                            r.getRoomId()
            );
        }
    }
}

/**
 * ================================================================
 * CLASS – PersistenceService
 * ================================================================
 *
 * Description:
 * Handles saving and loading system state using file storage.
 *
 * @version 12.0
 */

class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    /**
     * Saves system state to file
     */
    public void saveState(SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);

        } catch (IOException e) {
            System.out.println("Error saving state.");
        }
    }

    /**
     * Loads system state from file
     */
    public SystemState loadState() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (SystemState) in.readObject();

        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }
    }
}