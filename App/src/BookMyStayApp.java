import java.util.*;

/**
 * ================================================================
 * MAIN CLASS – BookMyStayApp
 * ================================================================
 *
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Description:
 * Simulates multiple booking requests processed concurrently
 * using threads while ensuring thread-safe operations.
 *
 * @version 11.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        // Shared resources
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);

        BookingQueue queue = new BookingQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));

        // Create multiple threads
        Thread t1 = new Thread(new BookingProcessor(queue, inventory));
        Thread t2 = new Thread(new BookingProcessor(queue, inventory));

        // Start threads
        t1.start();
        t2.start();
    }
}

/**
 * ================================================================
 * CLASS – Reservation
 * ================================================================
 *
 * Description:
 * Represents a booking request.
 *
 * @version 11.0
 */

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * ================================================================
 * CLASS – BookingQueue
 * ================================================================
 *
 * Description:
 * Thread-safe queue for booking requests.
 *
 * @version 11.0
 */

class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}

/**
 * ================================================================
 * CLASS – Inventory
 * ================================================================
 *
 * Description:
 * Thread-safe inventory management.
 *
 * @version 11.0
 */

class Inventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public synchronized void addRoom(String type, int count) {
        rooms.put(type, count);
    }

    public synchronized boolean allocateRoom(String type) {

        int available = rooms.getOrDefault(type, 0);

        if (available <= 0) {
            return false;
        }

        rooms.put(type, available - 1);
        return true;
    }
}

/**
 * ================================================================
 * CLASS – BookingProcessor
 * ================================================================
 *
 * Description:
 * Processes booking requests concurrently using threads.
 *
 * @version 11.0
 */

class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(BookingQueue queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r = queue.getNextRequest();

            if (r == null) break;

            // Critical section
            synchronized (inventory) {
                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println(
                            Thread.currentThread().getName() +
                                    " booked for " + r.getGuestName()
                    );
                } else {
                    System.out.println(
                            Thread.currentThread().getName() +
                                    " failed for " + r.getGuestName()
                    );
                }
            }
        }
    }
}