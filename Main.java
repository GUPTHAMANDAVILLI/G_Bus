import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java
 * Entry point for the G_Bus Smart Bus Ticket Booking System.
 *
 * Responsibilities:
 *   - Seed demo buses
 *   - Handle main menu (User Login / Register / Admin Login / Exit)
 *   - Delegate to user menu and admin menu flows
 *
 * All blocking I/O uses a single shared Scanner to avoid resource leaks.
 */
public class Main {

    // ─── Shared Data Stores ───────────────────────────────────────────────────
    private static final List<Bus>     buses    = new ArrayList<>();
    private static final List<User>    users    = new ArrayList<>();
    private static final List<Booking> bookings = new ArrayList<>();

    // ─── Admin Credentials ────────────────────────────────────────────────────
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    // ─── Shared Scanner ───────────────────────────────────────────────────────
    private static final Scanner sc = new Scanner(System.in);

    // ═════════════════════════════════════════════════════════════════════════
    // MAIN
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        // 1. Startup animation (runs in calling thread – simple sequential anim)
        Utils.startupAnimation();
        Utils.sleep(400);

        // 2. Welcome banner
        Utils.printWelcomeBanner();

        // 3. Seed demo data
        seedBuses();

        // 4. Main menu loop
        boolean running = true;
        while (running) {
            Utils.printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": handleUserLogin();  break;
                case "2": handleRegister();   break;
                case "3": handleAdminLogin(); break;
                case "0":
                    Utils.info("Thank you for using G_Bus. Safe travels! 🚍");
                    running = false;
                    break;
                default:
                    Utils.error("Invalid option. Please try again.");
            }
        }
        sc.close();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SEED DATA
    // ═════════════════════════════════════════════════════════════════════════
    private static void seedBuses() {
        buses.add(new Bus("AP01-1234", "APSRTC Super Luxury",
                "Hyderabad",   "Vijayawada", "06:00 AM", 650));
        buses.add(new Bus("TN02-5678", "KPN Travels AC Sleeper",
                "Chennai",     "Bengaluru",  "08:30 PM", 850));
        buses.add(new Bus("MH03-9012", "Shivneri Express",
                "Pune",        "Mumbai",     "07:15 AM", 420));
        buses.add(new Bus("KA04-3456", "KSRTC Airavat Club",
                "Bengaluru",   "Mysuru",     "05:45 PM", 310));
        buses.add(new Bus("TS05-7890", "Orange Travels Deluxe",
                "Hyderabad",   "Warangal",   "09:00 AM", 200));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // REGISTRATION
    // ═════════════════════════════════════════════════════════════════════════
    private static void handleRegister() {
        Utils.header("Register New Account");

        Utils.prompt("Full Name");
        String name = sc.nextLine().trim();

        Utils.prompt("Username");
        String username = sc.nextLine().trim();

        // Check duplicate username
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                Utils.error("Username '" + username + "' is already taken. Try another.");
                return;
            }
        }

        Utils.prompt("Password");
        String password = sc.nextLine().trim();

        Utils.prompt("Phone Number");
        String phone = sc.nextLine().trim();

        Utils.prompt("Email");
        String email = sc.nextLine().trim();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Utils.error("Name, username, and password cannot be empty.");
            return;
        }

        User newUser = new User(name, username, password, phone, email);
        users.add(newUser);
        Utils.success("Account created! You can now log in as '" + username + "'.");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // USER LOGIN
    // ═════════════════════════════════════════════════════════════════════════
    private static void handleUserLogin() {
        Utils.header("User Login");

        Utils.prompt("Username");
        String username = sc.nextLine().trim();

        Utils.prompt("Password");
        String password = sc.nextLine().trim();

        User loggedIn = null;
        for (User u : users) {
            if (u.authenticate(username, password)) {
                loggedIn = u;
                break;
            }
        }

        if (loggedIn == null) {
            Utils.error("Invalid credentials. Please try again.");
            return;
        }

        Utils.success("Welcome back, " + loggedIn.getName() + "!");
        userMenu(loggedIn);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // USER MENU
    // ═════════════════════════════════════════════════════════════════════════
    private static void userMenu(User user) {
        boolean active = true;
        while (active) {
            Utils.printUserMenu(user.getName());
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": viewAllBuses();            break;
                case "2": searchBus();               break;
                case "3": bookTicket(user);          break;
                case "4": viewMyBookings(user);      break;
                case "5": cancelTicket(user);        break;
                case "0":
                    Utils.info("Logged out. See you again!");
                    active = false;
                    break;
                default:
                    Utils.error("Invalid option.");
            }
        }
    }

    // ─── 1. View All Buses ────────────────────────────────────────────────────
    private static void viewAllBuses() {
        Utils.header("Available Buses");
        if (buses.isEmpty()) {
            Utils.warn("No buses available at the moment.");
            return;
        }
        printBusTableHeader();
        for (Bus b : buses) {
            System.out.println(b.getSummary());
        }
        System.out.println();
    }

    // ─── 2. Search Bus ────────────────────────────────────────────────────────
    private static void searchBus() {
        Utils.header("Search Bus");

        Utils.prompt("From (City)");
        String from = sc.nextLine().trim();

        Utils.prompt("To (City)");
        String to = sc.nextLine().trim();

        // Run search animation in a separate thread
        Thread animThread = new Thread(() -> Utils.busSearchAnimation(from, to));
        animThread.start();
        try { animThread.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<Bus> results = new ArrayList<>();
        for (Bus b : buses) {
            if (b.getSource().equalsIgnoreCase(from)
                    && b.getDestination().equalsIgnoreCase(to)) {
                results.add(b);
            }
        }

        if (results.isEmpty()) {
            Utils.warn("No buses found from '" + from + "' to '" + to + "'.");
            return;
        }

        System.out.println(Utils.GREEN + Utils.BOLD
            + "  " + results.size() + " bus(es) found:\n" + Utils.RESET);
        printBusTableHeader();
        for (Bus b : results) {
            System.out.println(b.getSummary());
        }
        System.out.println();
    }

    // ─── 3. Book Ticket ───────────────────────────────────────────────────────
    private static void bookTicket(User user) {
        Utils.header("Book Ticket");

        if (buses.isEmpty()) {
            Utils.warn("No buses available.");
            return;
        }

        viewAllBuses();

        Utils.prompt("Enter Bus ID to book");
        int busId;
        try {
            busId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            Utils.error("Invalid Bus ID.");
            return;
        }

        Bus selectedBus = findBusById(busId);
        if (selectedBus == null) {
            Utils.error("Bus with ID " + busId + " not found.");
            return;
        }

        if (selectedBus.availableSeats() == 0) {
            Utils.error("Sorry, this bus is fully booked!");
            return;
        }

        // Show seat layout
        Utils.printSeatLayout(selectedBus);

        Utils.prompt("Enter Seat Number (e.g. A1, B3)");
        String seat = sc.nextLine().trim().toUpperCase();

        if (!Bus.isValidSeat(seat)) {
            Utils.error("'" + seat + "' is not a valid seat. Choose A1-D4.");
            return;
        }
        if (selectedBus.isSeatBooked(seat)) {
            Utils.error("Seat " + seat + " is already booked. Please choose another.");
            return;
        }

        Utils.prompt("Passenger Name");
        String passengerName = sc.nextLine().trim();
        if (passengerName.isEmpty()) passengerName = user.getName();

        // Confirm booking details
        System.out.println();
        System.out.println(Utils.THIN_DIVIDER);
        System.out.println(Utils.BOLD + "  Booking Summary" + Utils.RESET);
        System.out.println(Utils.THIN_DIVIDER);
        System.out.println("  Passenger : " + Utils.BOLD + passengerName + Utils.RESET);
        System.out.println("  Bus       : " + selectedBus.getBusName());
        System.out.println("  From      : " + selectedBus.getSource());
        System.out.println("  To        : " + selectedBus.getDestination());
        System.out.println("  Departure : " + selectedBus.getDepartureTime());
        System.out.println("  Seat      : " + Utils.BOLD + seat + Utils.RESET);
        System.out.println("  Fare      : " + Utils.GREEN + Utils.BOLD
            + "₹" + selectedBus.getFare() + Utils.RESET);
        System.out.println();

        Utils.prompt("Confirm booking? (Y/N)");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("Y")) {
            Utils.info("Booking cancelled.");
            return;
        }

        // Payment animation in a separate thread
        Thread payThread = new Thread(Utils::paymentAnimation);
        payThread.start();
        try { payThread.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Perform booking
        boolean booked = selectedBus.bookSeat(seat);
        if (!booked) {
            Utils.error("Seat booking failed. Please try again.");
            return;
        }

        Booking booking = new Booking(user, selectedBus, passengerName, seat);
        bookings.add(booking);

        // Ticket print animation in a separate thread
        Thread printThread = new Thread(() -> Utils.ticketPrintAnimation(booking));
        printThread.start();
        try { printThread.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // ─── 4. View My Bookings ──────────────────────────────────────────────────
    private static void viewMyBookings(User user) {
        Utils.header("My Bookings");

        List<Booking> myBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUser().getUserId() == user.getUserId()) {
                myBookings.add(b);
            }
        }

        if (myBookings.isEmpty()) {
            Utils.warn("You have no bookings yet.");
            return;
        }

        for (Booking b : myBookings) {
            Utils.printTicketPlain(b);
        }
    }

    // ─── 5. Cancel Ticket ────────────────────────────────────────────────────
    private static void cancelTicket(User user) {
        Utils.header("Cancel Ticket");

        List<Booking> active = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUser().getUserId() == user.getUserId() && !b.isCancelled()) {
                active.add(b);
            }
        }

        if (active.isEmpty()) {
            Utils.warn("You have no active bookings to cancel.");
            return;
        }

        System.out.println(Utils.BOLD + "\n  Your Active Bookings:\n" + Utils.RESET);
        for (Booking b : active) {
            System.out.println(b.getSummary());
        }
        System.out.println();

        Utils.prompt("Enter Booking ID to cancel");
        int bookingId;
        try {
            bookingId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            Utils.error("Invalid Booking ID.");
            return;
        }

        Booking target = null;
        for (Booking b : active) {
            if (b.getBookingId() == bookingId) {
                target = b;
                break;
            }
        }

        if (target == null) {
            Utils.error("Booking #" + bookingId + " not found in your active bookings.");
            return;
        }

        Utils.prompt("Are you sure you want to cancel booking #" + bookingId + "? (Y/N)");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("Y")) {
            Utils.info("Cancellation aborted.");
            return;
        }

        if (target.cancel()) {
            Utils.success("Booking #" + bookingId + " cancelled successfully. Seat released.");
        } else {
            Utils.error("Could not cancel booking #" + bookingId + ".");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ADMIN LOGIN
    // ═════════════════════════════════════════════════════════════════════════
    private static void handleAdminLogin() {
        Utils.header("Admin Login");

        Utils.prompt("Admin Username");
        String username = sc.nextLine().trim();

        Utils.prompt("Admin Password");
        String password = sc.nextLine().trim();

        if (!ADMIN_USERNAME.equals(username) || !ADMIN_PASSWORD.equals(password)) {
            Utils.error("Invalid admin credentials.");
            return;
        }

        Utils.success("Admin access granted!");
        adminMenu();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ADMIN MENU
    // ═════════════════════════════════════════════════════════════════════════
    private static void adminMenu() {
        boolean active = true;
        while (active) {
            Utils.printAdminMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": adminAddBus();         break;
                case "2": adminRemoveBus();      break;
                case "3": viewAllBuses();        break;
                case "4": adminViewAllBookings(); break;
                case "5": adminViewRevenue();    break;
                case "0":
                    Utils.info("Admin logged out.");
                    active = false;
                    break;
                default:
                    Utils.error("Invalid option.");
            }
        }
    }

    // ─── Admin 1: Add Bus ────────────────────────────────────────────────────
    private static void adminAddBus() {
        Utils.header("Add New Bus");

        Utils.prompt("Bus Number (e.g. AP07-1234)");
        String busNumber = sc.nextLine().trim();

        Utils.prompt("Bus Name");
        String busName = sc.nextLine().trim();

        Utils.prompt("Source City");
        String source = sc.nextLine().trim();

        Utils.prompt("Destination City");
        String dest = sc.nextLine().trim();

        Utils.prompt("Departure Time (e.g. 08:30 AM)");
        String time = sc.nextLine().trim();

        Utils.prompt("Fare (₹)");
        double fare;
        try {
            fare = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            Utils.error("Invalid fare amount.");
            return;
        }

        if (busNumber.isEmpty() || busName.isEmpty() || source.isEmpty() || dest.isEmpty()) {
            Utils.error("All fields are required.");
            return;
        }

        Bus newBus = new Bus(busNumber, busName, source, dest, time, fare);
        buses.add(newBus);
        Utils.success("Bus '" + busName + "' (ID #" + newBus.getBusId() + ") added successfully!");
    }

    // ─── Admin 2: Remove Bus ─────────────────────────────────────────────────
    private static void adminRemoveBus() {
        Utils.header("Remove Bus");

        if (buses.isEmpty()) {
            Utils.warn("No buses to remove.");
            return;
        }

        viewAllBuses();

        Utils.prompt("Enter Bus ID to remove");
        int busId;
        try {
            busId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            Utils.error("Invalid Bus ID.");
            return;
        }

        Bus target = findBusById(busId);
        if (target == null) {
            Utils.error("Bus ID " + busId + " not found.");
            return;
        }

        // Check active bookings on this bus
        boolean hasActiveBookings = false;
        for (Booking b : bookings) {
            if (b.getBus().getBusId() == busId && !b.isCancelled()) {
                hasActiveBookings = true;
                break;
            }
        }
        if (hasActiveBookings) {
            Utils.warn("This bus has active bookings. Cancel them first before removing.");
            return;
        }

        buses.remove(target);
        Utils.success("Bus '" + target.getBusName() + "' removed successfully.");
    }

    // ─── Admin 4: View All Bookings ───────────────────────────────────────────
    private static void adminViewAllBookings() {
        Utils.header("All Bookings");

        if (bookings.isEmpty()) {
            Utils.warn("No bookings have been made yet.");
            return;
        }

        for (Booking b : bookings) {
            System.out.println(b.getSummary());
        }
        System.out.println();
    }

    // ─── Admin 5: Total Revenue ───────────────────────────────────────────────
    private static void adminViewRevenue() {
        Utils.header("Revenue Report");

        double total = 0;
        int confirmed = 0;
        int cancelled = 0;

        for (Booking b : bookings) {
            if (!b.isCancelled()) {
                total += b.getAmountPaid();
                confirmed++;
            } else {
                cancelled++;
            }
        }

        System.out.println(Utils.THIN_DIVIDER);
        System.out.println("  " + Utils.YELLOW + "Total Bookings    : " + Utils.RESET
            + Utils.BOLD + (confirmed + cancelled) + Utils.RESET);
        System.out.println("  " + Utils.GREEN  + "Confirmed         : " + Utils.RESET
            + Utils.BOLD + confirmed + Utils.RESET);
        System.out.println("  " + Utils.RED    + "Cancelled         : " + Utils.RESET
            + Utils.BOLD + cancelled + Utils.RESET);
        System.out.println("  " + Utils.YELLOW + "Total Revenue     : " + Utils.RESET
            + Utils.GREEN + Utils.BOLD + "₹" + String.format("%.2f", total) + Utils.RESET);
        System.out.println(Utils.THIN_DIVIDER);
        System.out.println();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═════════════════════════════════════════════════════════════════════════

    private static Bus findBusById(int id) {
        for (Bus b : buses) {
            if (b.getBusId() == id) return b;
        }
        return null;
    }

    private static void printBusTableHeader() {
        System.out.println(Utils.THIN_DIVIDER);
        System.out.printf("  " + Utils.BOLD + "%-4s  %-22s %-14s   %-14s  %-8s  %-8s  %s%n" + Utils.RESET,
            "ID", "Bus Name", "From", "To", "Departs", "Fare", "Seats");
        System.out.println(Utils.THIN_DIVIDER);
    }
}
