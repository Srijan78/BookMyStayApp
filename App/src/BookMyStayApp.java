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