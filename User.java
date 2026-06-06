/**
 * User.java
 * Represents a registered user of the G_Bus system.
 * Stores credentials and profile information.
 */
public class User {

    // ─── Fields ───────────────────────────────────────────────────────────────
    private static int idCounter = 1000;

    private int    userId;
    private String name;
    private String username;
    private String password;      // stored as plain text (demo scope)
    private String phone;
    private String email;

    // ─── Constructor ──────────────────────────────────────────────────────────
    public User(String name, String username, String password,
                String phone, String email) {
        this.userId   = idCounter++;
        this.name     = name;
        this.username = username;
        this.password = password;
        this.phone    = phone;
        this.email    = email;
    }

    // ─── Authentication ───────────────────────────────────────────────────────

    /** Returns true when the supplied credentials match this account. */
    public boolean authenticate(String username, String password) {
        return this.username.equalsIgnoreCase(username)
            && this.password.equals(password);
    }

    // ─── Display ─────────────────────────────────────────────────────────────

    public void printProfile() {
        System.out.println("  " + Utils.YELLOW + "User ID   : " + Utils.RESET + "#" + userId);
        System.out.println("  " + Utils.YELLOW + "Name      : " + Utils.RESET + Utils.BOLD + name + Utils.RESET);
        System.out.println("  " + Utils.YELLOW + "Username  : " + Utils.RESET + username);
        System.out.println("  " + Utils.YELLOW + "Phone     : " + Utils.RESET + phone);
        System.out.println("  " + Utils.YELLOW + "Email     : " + Utils.RESET + email);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public int    getUserId()  { return userId; }
    public String getName()    { return name; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getPhone()   { return phone; }
    public String getEmail()   { return email; }

    // ─── Setters ──────────────────────────────────────────────────────────────
    public void setName(String name)       { this.name = name; }
    public void setPassword(String pass)   { this.password = pass; }
    public void setPhone(String phone)     { this.phone = phone; }
    public void setEmail(String email)     { this.email = email; }

    // Reset counter (testing)
    public static void resetIdCounter() { idCounter = 1000; }
}
