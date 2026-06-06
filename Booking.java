/**
 * Booking.java
 * Represents a single bus ticket booking.
 * Links a User to a Bus with a chosen seat number.
 */
public class Booking {

    // ─── Fields ───────────────────────────────────────────────────────────────
    private static int idCounter = 5000;

    private int    bookingId;
    private User   user;
    private Bus    bus;
    private String passengerName;   // may differ from account holder
    private String seatNumber;
    private double amountPaid;
    private boolean cancelled;

    // ─── Constructor ──────────────────────────────────────────────────────────
    public Booking(User user, Bus bus, String passengerName, String seatNumber) {
        this.bookingId     = idCounter++;
        this.user          = user;
        this.bus           = bus;
        this.passengerName = passengerName;
        this.seatNumber    = seatNumber.toUpperCase();
        this.amountPaid    = bus.getFare();
        this.cancelled     = false;
    }

    // ─── Cancel ───────────────────────────────────────────────────────────────

    /**
     * Cancels this booking and releases the seat on the bus.
     * Returns false if already cancelled.
     */
    public boolean cancel() {
        if (cancelled) return false;
        cancelled = true;
        bus.releaseSeat(seatNumber);
        return true;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public int     getBookingId()     { return bookingId; }
    public User    getUser()          { return user; }
    public Bus     getBus()           { return bus; }
    public String  getPassengerName() { return passengerName; }
    public String  getSeatNumber()    { return seatNumber; }
    public double  getAmountPaid()    { return amountPaid; }
    public boolean isCancelled()      { return cancelled; }

    // ─── Display ─────────────────────────────────────────────────────────────

    /** One-line summary used in list views. */
    public String getSummary() {
        String status = cancelled
            ? Utils.RED  + Utils.BOLD + "[CANCELLED]" + Utils.RESET
            : Utils.GREEN + Utils.BOLD + "[CONFIRMED]" + Utils.RESET;

        return String.format(
            "  " + Utils.BOLD + "#%-5d" + Utils.RESET
            + " %-20s %-15s → %-15s  Seat: " + Utils.BOLD + "%-4s" + Utils.RESET
            + "  " + Utils.GREEN + Utils.BOLD + "₹%-6.0f" + Utils.RESET + "  %s",
            bookingId, passengerName,
            bus.getSource(), bus.getDestination(),
            seatNumber, amountPaid, status);
    }

    // Reset counter (testing)
    public static void resetIdCounter() { idCounter = 5000; }
}
