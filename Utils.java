import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utils.java
 * Utility class providing ASCII art, terminal animations,
 * color codes, and helper methods for the G_Bus application.
 */
public class Utils {

    // ─── ANSI Color Codes ────────────────────────────────────────────────────
    public static final String RESET   = "\u001B[0m";
    public static final String BOLD    = "\u001B[1m";
    public static final String RED     = "\u001B[31m";
    public static final String GREEN   = "\u001B[32m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String BLUE    = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN    = "\u001B[36m";
    public static final String WHITE   = "\u001B[37m";
    public static final String BG_BLUE = "\u001B[44m";

    // ─── Java-8-compatible string repeat ────────────────────────────────────
    public static String rep(String ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(ch);
        return sb.toString();
    }

    // ─── Dividers ─────────────────────────────────────────────────────────────
    public static final String DIVIDER      = CYAN + rep("═", 51) + RESET;
    public static final String THIN_DIVIDER = CYAN + rep("─", 51) + RESET;

    // ─── Welcome Banner ───────────────────────────────────────────────────────
    public static void printWelcomeBanner() {
        System.out.println();
        System.out.println(CYAN + rep("═", 51) + RESET);
        System.out.println();
        System.out.println(YELLOW + BOLD
            + " ██████╗      ██████╗ ██╗   ██╗███████╗" + RESET);
        System.out.println(YELLOW + BOLD
            + "██╔════╝      ██╔══██╗██║   ██║██╔════╝" + RESET);
        System.out.println(YELLOW + BOLD
            + "██║  ███╗     ██████╔╝██║   ██║███████╗" + RESET);
        System.out.println(YELLOW + BOLD
            + "██║   ██║     ██╔══██╗██║   ██║╚════██║" + RESET);
        System.out.println(YELLOW + BOLD
            + "╚██████╔╝     ██████╔╝╚██████╔╝███████║" + RESET);
        System.out.println(YELLOW + BOLD
            + " ╚═════╝      ╚═════╝  ╚═════╝ ╚══════╝" + RESET);
        System.out.println();
        System.out.println(GREEN + BOLD
            + "      Smart Bus Ticket Booking System     " + RESET);
        System.out.println();
        System.out.println(CYAN + rep("═", 51) + RESET);
        System.out.println();
    }

    // ─── Startup Loading Bar ─────────────────────────────────────────────────
    public static void startupAnimation() {
        System.out.println(CYAN + "\n  Starting G_Bus..." + RESET);
        System.out.println();

        int[] percents    = {10, 30, 60, 80, 100};
        String[] barSteps = {
            "█░░░░░░░░░░",
            "███░░░░░░░░",
            "██████░░░░░",
            "████████░░░",
            "███████████"
        };

        for (int i = 0; i < percents.length; i++) {
            String color = (percents[i] < 60) ? YELLOW
                         : (percents[i] < 100) ? MAGENTA
                         : GREEN;
            System.out.print("\r  " + color + barSteps[i]
                + RESET + "  " + BOLD + percents[i] + "%" + RESET + "   ");
            sleep(350);
        }
        System.out.println("\n");
        System.out.println(GREEN + BOLD + "  ✔  System Ready!" + RESET);
        sleep(600);
    }

    // ─── Bus Search Animation ────────────────────────────────────────────────
    public static void busSearchAnimation(String from, String to) {
        System.out.println(CYAN + "\n  Searching buses from "
            + YELLOW + from + CYAN + " → " + YELLOW + to + CYAN + " ...\n" + RESET);
        String[] frames = {"🚍", "🚍=", "🚍==", "🚍===", "🚍====", "🚍====="};
        for (String frame : frames) {
            System.out.print("\r  " + YELLOW + frame + RESET + "   ");
            sleep(200);
        }
        System.out.println("\n");
    }

    // ─── Payment Processing Animation ────────────────────────────────────────
    public static void paymentAnimation() {
        System.out.println(MAGENTA + "\n  Processing Payment...\n" + RESET);
        String[] spinner = {"|", "/", "─", "\\"};
        for (int i = 0; i < 20; i++) {
            System.out.print("\r  " + CYAN + BOLD
                + spinner[i % spinner.length] + RESET + "   ");
            sleep(120);
        }
        System.out.println("\r  " + GREEN + BOLD + "✔  Payment Successful!" + RESET + "   ");
        sleep(400);
    }

    // ─── Ticket Printing Animation ────────────────────────────────────────────
    public static void ticketPrintAnimation(Booking booking) {
        System.out.println(CYAN + "\n  Printing your ticket...\n" + RESET);
        sleep(300);

        String[] lines = buildTicketLines(booking);
        for (String line : lines) {
            System.out.println(line);
            sleep(90);
        }
    }

    // ─── Ticket Builder ───────────────────────────────────────────────────────
    private static String[] buildTicketLines(Booking booking) {
        Bus bus = booking.getBus();
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm"));

        return new String[]{
            CYAN + rep("═", 51) + RESET,
            GREEN + BOLD
                + "              G_BUS  E-TICKET              " + RESET,
            CYAN + rep("═", 51) + RESET,
            "",
            "  " + YELLOW + "Booking ID  : " + RESET + BOLD + "#" + booking.getBookingId() + RESET,
            "  " + YELLOW + "Date & Time : " + RESET + ts,
            "",
            THIN_DIVIDER,
            "  " + YELLOW + "Passenger   : " + RESET + BOLD + booking.getPassengerName() + RESET,
            "  " + YELLOW + "Bus         : " + RESET + bus.getBusName(),
            "  " + YELLOW + "Bus No.     : " + RESET + bus.getBusNumber(),
            "  " + YELLOW + "From        : " + RESET + bus.getSource(),
            "  " + YELLOW + "To          : " + RESET + bus.getDestination(),
            "  " + YELLOW + "Departure   : " + RESET + bus.getDepartureTime(),
            "  " + YELLOW + "Seat        : " + RESET + BOLD + booking.getSeatNumber() + RESET,
            "  " + YELLOW + "Fare        : " + RESET + BOLD + GREEN + "₹" + bus.getFare() + RESET,
            "",
            THIN_DIVIDER,
            "  " + GREEN + BOLD + "Status      : ✔  CONFIRMED" + RESET,
            "",
            CYAN + rep("═", 51) + RESET,
            GREEN + BOLD
                + "       Thank You For Choosing G_Bus        " + RESET,
            CYAN + rep("═", 51) + RESET,
            ""
        };
    }

    // Plain ticket (no animation) for view bookings
    public static void printTicketPlain(Booking booking) {
        Bus bus = booking.getBus();
        System.out.println(CYAN + rep("═", 51) + RESET);
        System.out.println(GREEN + BOLD
            + "              G_BUS  E-TICKET              " + RESET);
        System.out.println(CYAN + rep("═", 51) + RESET);
        System.out.println("  " + YELLOW + "Booking ID  : " + RESET + "#" + booking.getBookingId());
        System.out.println("  " + YELLOW + "Passenger   : " + RESET + booking.getPassengerName());
        System.out.println("  " + YELLOW + "Bus         : " + RESET + bus.getBusName());
        System.out.println("  " + YELLOW + "From        : " + RESET + bus.getSource());
        System.out.println("  " + YELLOW + "To          : " + RESET + bus.getDestination());
        System.out.println("  " + YELLOW + "Departure   : " + RESET + bus.getDepartureTime());
        System.out.println("  " + YELLOW + "Seat        : " + RESET + BOLD + booking.getSeatNumber() + RESET);
        System.out.println("  " + YELLOW + "Fare        : " + RESET + GREEN + BOLD + "₹" + bus.getFare() + RESET);
        System.out.println("  " + YELLOW + "Status      : " + RESET
            + (booking.isCancelled()
               ? RED + BOLD + "✘  CANCELLED" + RESET
               : GREEN + BOLD + "✔  CONFIRMED" + RESET));
        System.out.println(CYAN + rep("═", 51) + RESET);
        System.out.println();
    }

    // ─── Seat Layout Printer ──────────────────────────────────────────────────
    public static void printSeatLayout(Bus bus) {
        System.out.println(CYAN + "\n  Seat Layout – " + bus.getBusName() + RESET);
        System.out.println(THIN_DIVIDER);
        System.out.println("  " + GREEN + "[ ] Available   " + RESET
            + RED + "[X] Booked" + RESET + "\n");

        char[] rows = {'A', 'B', 'C', 'D'};
        for (char row : rows) {
            System.out.print("  ");
            for (int col = 1; col <= 4; col++) {
                String seat = "" + row + col;
                if (bus.isSeatBooked(seat)) {
                    System.out.print(RED + "[X] " + RESET);
                } else {
                    System.out.print(GREEN + "[ ] " + RESET);
                }
                System.out.print(BOLD + seat + RESET + "  ");
            }
            System.out.println();
            System.out.println();
        }
    }

    // ─── Menu Printers ────────────────────────────────────────────────────────
    public static void printUserMenu(String username) {
        System.out.println(DIVIDER);
        System.out.println(BOLD + CYAN
            + "  USER MENU  ─  Welcome, " + YELLOW + username + RESET);
        System.out.println(DIVIDER);
        System.out.println(YELLOW + "  [1]" + RESET + " View Available Buses");
        System.out.println(YELLOW + "  [2]" + RESET + " Search Bus");
        System.out.println(YELLOW + "  [3]" + RESET + " Book Ticket");
        System.out.println(YELLOW + "  [4]" + RESET + " My Bookings");
        System.out.println(YELLOW + "  [5]" + RESET + " Cancel Ticket");
        System.out.println(YELLOW + "  [0]" + RESET + " Logout");
        System.out.println(THIN_DIVIDER);
        System.out.print(BOLD + "  Choice ▸ " + RESET);
    }

    public static void printAdminMenu() {
        System.out.println(DIVIDER);
        System.out.println(BOLD + MAGENTA + "  ADMIN PANEL" + RESET);
        System.out.println(DIVIDER);
        System.out.println(YELLOW + "  [1]" + RESET + " Add New Bus");
        System.out.println(YELLOW + "  [2]" + RESET + " Remove Bus");
        System.out.println(YELLOW + "  [3]" + RESET + " View All Buses");
        System.out.println(YELLOW + "  [4]" + RESET + " View All Bookings");
        System.out.println(YELLOW + "  [5]" + RESET + " View Total Revenue");
        System.out.println(YELLOW + "  [0]" + RESET + " Logout");
        System.out.println(THIN_DIVIDER);
        System.out.print(BOLD + "  Choice ▸ " + RESET);
    }

    public static void printMainMenu() {
        System.out.println(DIVIDER);
        System.out.println(BOLD + CYAN + "  MAIN MENU" + RESET);
        System.out.println(DIVIDER);
        System.out.println(YELLOW + "  [1]" + RESET + " User Login");
        System.out.println(YELLOW + "  [2]" + RESET + " Register");
        System.out.println(YELLOW + "  [3]" + RESET + " Admin Login");
        System.out.println(YELLOW + "  [0]" + RESET + " Exit");
        System.out.println(THIN_DIVIDER);
        System.out.print(BOLD + "  Choice ▸ " + RESET);
    }

    // ─── Message Helpers ──────────────────────────────────────────────────────
    public static void success(String msg) {
        System.out.println("\n  " + GREEN + BOLD + "✔  " + msg + RESET + "\n");
    }

    public static void error(String msg) {
        System.out.println("\n  " + RED + BOLD + "✘  " + msg + RESET + "\n");
    }

    public static void info(String msg) {
        System.out.println("\n  " + CYAN + "ℹ  " + msg + RESET + "\n");
    }

    public static void warn(String msg) {
        System.out.println("\n  " + YELLOW + "⚠  " + msg + RESET + "\n");
    }

    public static void header(String title) {
        System.out.println("\n" + DIVIDER);
        System.out.println(BOLD + CYAN + "  " + title.toUpperCase() + RESET);
        System.out.println(DIVIDER);
    }

    // ─── Thread Sleep Helper ──────────────────────────────────────────────────
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ─── Input Prompt ─────────────────────────────────────────────────────────
    public static void prompt(String label) {
        System.out.print("  " + YELLOW + label + " ▸ " + RESET);
    }

    // ─── Section Label ────────────────────────────────────────────────────────
    public static void section(String label) {
        System.out.println("\n" + THIN_DIVIDER);
        System.out.println(BOLD + "  " + label + RESET);
        System.out.println(THIN_DIVIDER);
    }
}
