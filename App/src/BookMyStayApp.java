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
    }
}