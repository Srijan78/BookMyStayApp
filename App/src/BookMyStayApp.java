import java.util.*;

/**
 * ================================================================
 * MAIN CLASS – BookMyStayApp
 * ================================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * Demonstrates validation and error handling using
 * custom exceptions to ensure system reliability.
 *
 * @version 9.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

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
 * @version 9.0
 */

class Inventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public void addRoom(String type, int count) {
        rooms.put(type, count);
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
    }
}