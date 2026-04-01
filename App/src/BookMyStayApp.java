import java.io.*;
import java.util.*;

/**
 * ================================================================
 * MAIN CLASS – UseCase7AddOnServices
 * ================================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * Demonstrates how optional services can be added
 * to an existing reservation without affecting
 * core booking or inventory logic.
 *
 * @version 7.0
 * MAIN CLASS – UseCase8BookingHistoryReporting
 * ================================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * Demonstrates how confirmed bookings are stored
 * and how reports are generated from historical data.
 *
 * @version 8.0
 */

public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        // Sample reservation ID
        String reservationId = "SI101";

        AddOnServiceManager manager = new AddOnServiceManager();

        // Adding services
        manager.addService(reservationId, new AddOnService("Breakfast", 500));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 1000));
        manager.addService(reservationId, new AddOnService("Spa", 1500));

        // View services
        manager.viewServices(reservationId);

        // Calculate total cost
        double total = manager.calculateTotalCost(reservationId);

        System.out.println("\nTotal Add-On Cost: ₹" + total);
    }
}

/**
 * ================================================================
 * CLASS – AddOnService
 * ================================================================
 *
 * Description:
 * Represents an optional service that can be added
 * to a reservation.
 *
 * @version 7.0
 */

class AddOnService {

    /** Name of the service */
    private String serviceName;

    /** Cost of the service */
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

/**
 * ================================================================
 * CLASS – AddOnServiceManager
 * ================================================================
 *
 * Description:
 * Manages mapping between reservations and selected services.
 *
 * @version 7.0
 */

class AddOnServiceManager {

    /** Map: Reservation ID → List of Services */
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added: " + service.getServiceName());
    }

    public void viewServices(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");

        for (AddOnService s : services) {
            System.out.println(s.getServiceName() + " - ₹" + s.getCost());
        }
    }

    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;

        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        history.addBooking(new Reservation("Alice", "Single", "S101"));
        history.addBooking(new Reservation("Bob", "Double", "D201"));
        history.addBooking(new Reservation("Charlie", "Suite", "SU301"));

        // View all bookings
        history.displayHistory();

        // Generate report
        reportService.generateSummary(history.getAllBookings());
    }
}

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

 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * Demonstrates validation and error handling using
 * custom exceptions to ensure system reliability.
 *
 * @version 9.0
 */

/**
 * ================================================================
 * CLASS – BookingService
 * ================================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class processes booking requests from the queue
 * and performs safe room allocation.
 *
 * It ensures:
 * - Unique room assignment
 * - Inventory consistency
 * - Prevention of double-booking
 *
 * @version 6.0
 */

import java.util.*;

public class BookingService {

    /** Tracks allocated room IDs to ensure uniqueness */
    private Set<String> allocatedRoomIds = new HashSet<>();

    /** Maps room type to allocated room IDs */
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    /** Reference to inventory */
    private Inventory inventory;

    /**
     * Constructor to initialize booking service with inventory
     *
     * @param inventory inventory service
     */
    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Processes next booking request from queue
     *
     * @param queue booking request queue
     */
    public void processBooking(BookingQueue queue) {

        Reservation request = queue.processNextRequest();

        if (request == null) {
            System.out.println("No booking requests available.");
            return;
        }

        String roomType = request.getRoomType();

        // Check availability
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("No rooms available for: " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Store allocation
        allocatedRoomIds.add(roomId);

        roomAllocations
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Update inventory (critical step)
        inventory.decrementRoom(roomType);

        // Confirm reservation
        System.out.println("Booking Confirmed:");
        System.out.println("Guest: " + request.getGuestName());
        System.out.println("Room Type: " + roomType);
        System.out.println("Room ID: " + roomId);
    }

    /**
     * Generates a unique room ID
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {

        String roomId;

        do {
            roomId = roomType.substring(0, 2).toUpperCase()
                    + new Random().nextInt(1000);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
// Abstract Room Class
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public abstract void displayDetails();
}

// Concrete Room Classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price: ₹" + getPricePerNight());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price: ₹" + getPricePerNight());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3000.0);
    }

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price: ₹" + getPricePerNight());
    }
}

// Inventory Class (Read + Write, but search will only READ)
class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 3);
        availabilityMap.put("Suite Room", 0); // Example: unavailable
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int change) {
        int current = availabilityMap.getOrDefault(roomType, 0);
        if (current + change >= 0) {
            availabilityMap.put(roomType, current + change);
        }
    }

    public Map<String, Integer> getAllAvailability() {
        return availabilityMap;
    }
}

// Search Service (READ-ONLY)
class SearchService {

    private RoomInventory inventory;
    private List<Room> rooms;

    public SearchService(RoomInventory inventory, List<Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    // Search available rooms (READ-ONLY)
    public void searchAvailableRooms() {
        System.out.println("\n===== Available Rooms =====");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: only show available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("--------------------------");
            }
        }
    }
}

// Main Application
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
 * Represents a confirmed booking with room allocation.
 *
 * @version 8.0
 */

class Reservation {
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
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        BookingValidator validator = new BookingValidator();

        try {
            // Valid booking
            validator.validate("Single", inventory);
            System.out.println("Booking Valid for Single");

            // Invalid booking (wrong room type)
            validator.validate("Suite", inventory);

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            // Invalid booking (no availability)
            validator.validate("Double", inventory);
            validator.validate("Double", inventory); // second call fails

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }
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
 * @version 9.0
 */

class Inventory {

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
 * Stores confirmed bookings in insertion order.
 *
 * @version 8.0
 */

class BookingHistory {

    /** List to maintain booking records */
    private List<Reservation> bookings = new ArrayList<>();

    /**
     * Adds a confirmed booking to history
     */
    public void addBooking(Reservation reservation) {
        bookings.add(reservation);
        System.out.println("Booking Stored: " + reservation.getGuestName());
    }

    /**
     * Returns all stored bookings
     */
    public List<Reservation> getAllBookings() {
        return bookings;
    }

    /**
     * Displays booking history
     */
    public void displayHistory() {

        System.out.println("\nBooking History:");

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : bookings) {
            System.out.println(
                    "Guest: " + r.getGuestName() +
                            ", Room: " + r.getRoomType() +
                            ", ID: " + r.getRoomId()
            );
        }
    }
}

/**
 * ================================================================
 * CLASS – BookingReportService
 * ================================================================
 *
 * Description:
 * Generates reports from booking history.
 *
 * @version 8.0
 */

class BookingReportService {

    /**
     * Generates summary report
     */
    public void generateSummary(List<Reservation> bookings) {

        System.out.println("\nBooking Summary Report:");

        if (bookings.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : bookings) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomTypeCount.get(type));
        }

        System.out.println("Total Bookings: " + bookings.size());
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
    public int getAvailability(String type) {
        return rooms.getOrDefault(type, -1);
    }

    public void decrementRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

/**
 * ================================================================
 * CLASS – BookingValidator
 * ================================================================
 *
 * Description:
 * Validates booking input and system constraints.
 *
 * @version 9.0
 */

class BookingValidator {

    /**
     * Validates room type and availability
     */
    public void validate(String roomType, Inventory inventory)
            throws InvalidBookingException {

        // Validate room type exists
        if (inventory.getAvailability(roomType) == -1) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate availability
        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        // Safe update (only after validation)
        inventory.decrementRoom(roomType);
    }
}

/**
 * ================================================================
 * CLASS – InvalidBookingException
 * ================================================================
 *
 * Description:
 * Custom exception for invalid booking scenarios.
 *
 * @version 9.0
 */

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
        System.out.println("====== Book My Stay - Room Search ======");

        // Create Room Objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        RoomInventory inventory = new RoomInventory();

        SearchService searchService = new SearchService(inventory, rooms);

        searchService.searchAvailableRooms();

        System.out.println("\n=======================================");
        System.out.println("Search completed. No data was modified.");
    }
}