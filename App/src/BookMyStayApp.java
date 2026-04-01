import java.util.*;

/**
 * ================================================================
 * MAIN CLASS – BookMyStayApp
 * ================================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * Demonstrates safe cancellation of bookings with
 * rollback of inventory and room allocation.
 *
 * @version 10.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        // Setup inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);

        // Booking history
        BookingHistory history = new BookingHistory();

        // Cancellation service
        CancellationService service = new CancellationService(inventory, history);

        // Create and confirm booking
        Reservation r1 = new Reservation("Alice", "Single", "S101");
        history.addBooking(r1);
        inventory.decrementRoom("Single");

        // Attempt cancellation
        service.cancelBooking("S101");

        // Invalid cancellation (already cancelled)
        service.cancelBooking("S101");
    }
}

/**
 * ================================================================
 * CLASS – Reservation
 * ================================================================
 *
 * Description:
 * Represents a confirmed booking.
 *
 * @version 10.0
 */

class Reservation {

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
 * @version 10.0
 */

class Inventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public int getAvailability(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void incrementRoom(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }

    public void decrementRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

/**
 * ================================================================
 * CLASS – BookingHistory
 * ================================================================
 *
 * Description:
 * Stores and manages booking records.
 *
 * @version 10.0
 */

class BookingHistory {

    private Map<String, Reservation> bookings = new HashMap<>();

    public void addBooking(Reservation r) {
        bookings.put(r.getRoomId(), r);
        System.out.println("Booking Confirmed: " + r.getRoomId());
    }

    public Reservation getBooking(String roomId) {
        return bookings.get(roomId);
    }

    public void removeBooking(String roomId) {
        bookings.remove(roomId);
    }
}

/**
 * ================================================================
 * CLASS – CancellationService
 * ================================================================
 *
 * Description:
 * Handles booking cancellation and rollback logic.
 *
 * @version 10.0
 */

class CancellationService {

    private Inventory inventory;
    private BookingHistory history;

    /** Stack to track released room IDs (LIFO rollback) */
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    /**
     * Cancels a booking safely
     */
    public void cancelBooking(String roomId) {

        Reservation r = history.getBooking(roomId);

        // Validation
        if (r == null) {
            System.out.println("Cancellation Failed: Booking not found for " + roomId);
            return;
        }

        // Push to rollback stack
        rollbackStack.push(roomId);

        // Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Remove booking
        history.removeBooking(roomId);

        System.out.println("Booking Cancelled: " + roomId);
    }
}