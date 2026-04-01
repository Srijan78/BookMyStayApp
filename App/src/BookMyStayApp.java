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
    }
}