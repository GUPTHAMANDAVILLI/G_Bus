import java.util.ArrayList;
import java.util.List;

/**
 * Bus.java
 * Represents a bus with seat management (4 rows × 4 cols = 16 seats).
 * Seat IDs follow the pattern: A1..A4, B1..B4, C1..C4, D1..D4.
 */
public class Bus {

    // ─── Fields ───────────────────────────────────────────────────────────────
    private static int idCounter = 1;

    private int    busId;
    private String busNumber;
    private String busName;
    private String source;
    private String destination;
    private String departureTime;
    private double fare;
    private int    totalSeats;

    // Stores seat IDs that are already booked
    private List<String> bookedSeats;

    // ─── Constructor ──────────────────────────────────────────────────────────
    public Bus(String busNumber, String busName, String source,
               String destination, String departureTime, double fare) {
        this.busId         = idCounter++;
        this.busNumber     = busNumber;
        this.busName       = busName;
        this.source        = source;
        this.destination   = destination;
        this.departureTime = departureTime;
        this.fare          = fare;
        this.totalSeats    = 16;          // 4 rows × 4 cols
        this.bookedSeats   = new ArrayList<>();
    }

    // ─── Seat Operations ──────────────────────────────────────────────────────

    /** Returns true if the given seat ID is valid (A1-D4). */
    public static boolean isValidSeat(String seatId) {
        if (seatId == null || seatId.length() < 2) return false;
        char row = Character.toUpperCase(seatId.charAt(0));
        String colStr = seatId.substring(1);
        if (row < 'A' || row > 'D') return false;
        try {
            int col = Integer.parseInt(colStr);
            return col >= 1 && col <= 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Returns true if the seat is already booked. */
    public boolean isSeatBooked(String seatId) {
        return bookedSeats.contains(seatId.toUpperCase());
    }

    /** Books the seat; returns false if already booked or invalid. */
    public boolean bookSeat(String seatId) {
        seatId = seatId.toUpperCase();
        if (!isValidSeat(seatId) || isSeatBooked(seatId)) return false;
        bookedSeats.add(seatId);
        return true;
    }

    /** Releases the seat; returns false if the seat was not booked. */
    public boolean releaseSeat(String seatId) {
        return bookedSeats.remove(seatId.toUpperCase());
    }

    /** Number of seats still available. */
    public int availableSeats() {
        return totalSeats - bookedSeats.size();
    }

    /** Returns a formatted one-line summary of the bus. */
    public String getSummary() {
        return String.format(
            Utils.BOLD + "#%-3d" + Utils.RESET
            + " %-22s %-14s → %-14s  %-8s  " + Utils.GREEN + Utils.BOLD
            + "₹%-6.0f" + Utils.RESET + "  Seats: " + Utils.YELLOW + "%2d/%-2d" + Utils.RESET,
            busId, busName, source, destination,
            departureTime, fare, availableSeats(), totalSeats);
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────
    public int    getBusId()        { return busId; }
    public String getBusNumber()    { return busNumber; }
    public String getBusName()      { return busName; }
    public String getSource()       { return source; }
    public String getDestination()  { return destination; }
    public String getDepartureTime(){ return departureTime; }
    public double getFare()         { return fare; }
    public int    getTotalSeats()   { return totalSeats; }
    public List<String> getBookedSeats() { return bookedSeats; }

    public void setBusNumber(String busNumber)       { this.busNumber = busNumber; }
    public void setBusName(String busName)           { this.busName = busName; }
    public void setSource(String source)             { this.source = source; }
    public void setDestination(String destination)   { this.destination = destination; }
    public void setDepartureTime(String time)        { this.departureTime = time; }
    public void setFare(double fare)                 { this.fare = fare; }

    // Reset the static counter (useful for testing)
    public static void resetIdCounter() { idCounter = 1; }
}
