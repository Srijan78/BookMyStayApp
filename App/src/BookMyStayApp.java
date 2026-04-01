import java.util.*;

/**
 * ================================================================
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
 * CLASS – Reservation
 * ================================================================
 *
 * Description:
 * Represents a confirmed booking with room allocation.
 *
 * @version 8.0
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
    }
}