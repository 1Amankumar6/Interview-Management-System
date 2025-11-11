import java.util.*;
import java.sql.*;

class Candidate {
    private String id;
    private String name;
    private String position;
    private int experience;
    private String status;

    public Candidate(String id, String name, String position, int experience) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.experience = experience;
        this.status = "Pending";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getExperience() { return experience; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-5s | %-15s | %-10s | %2d yrs | %-10s",
                id, name, position, experience, status);
    }
}

class InterviewSystem {
    private final Scanner sc = new Scanner(System.in);
    private Connection conn;

    private static final List<String> ALLOWED_POSITIONS = Arrays.asList("SDE", "SDET", "SWE", "SE");

    // Constructor to establish database connection
    public InterviewSystem() {
        connectDatabase();
    }

    // Database connection setup
    private void connectDatabase() {
        String url = "jdbc:mysql://127.0.0.1:3306/interview_db";
        String user = "root";
        String pass = "Aman12345"; // your DB password
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Database Connected Successfully!");
        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            conn = null;
        }
    }

    // Ensure DB connection is valid
    private boolean isConnected() {
        if (conn == null) {
            System.out.println("No Database Connection. Trying to reconnect...");
            connectDatabase();
        }
        return conn != null;
    }

    // Add Candidate
    public void addCandidate() {
        if (!isConnected()) return;
        System.out.print("Enter candidate name: ");
        String name = sc.nextLine().trim();

        System.out.println("Available Positions: " + ALLOWED_POSITIONS);
        System.out.print("Enter position applied for: ");
        String position = sc.nextLine().trim().toUpperCase();

        if (!ALLOWED_POSITIONS.contains(position)) {
            System.out.println(" Invalid position!");
            return;
        }

        System.out.print("Enter experience (in years): ");
        int exp;
        try {
            exp = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" Invalid number!");
            return;
        }

        String id = "C" + (System.currentTimeMillis() % 10000);

        String sql = "INSERT INTO candidates (id, name, position, experience, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, position);
            ps.setInt(4, exp);
            ps.setString(5, "Pending");
            ps.executeUpdate();
            System.out.println(" Candidate added successfully with ID: " + id);
        } catch (SQLException e) {
            System.out.println(" Error adding candidate: " + e.getMessage());
        }
    }

    // View All Candidates
    public void viewAllCandidates() {
        if (!isConnected()) return;

        String sql = "SELECT * FROM candidates";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n---- Candidate List ----");
            System.out.println("ID    | Name            | Position   | Exp | Status");
            System.out.println("---------------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5s | %-15s | %-10s | %2d yrs | %-10s%n",
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getInt("experience"),
                        rs.getString("status"));
            }
            if (!found) System.out.println("No candidates found.");
        } catch (SQLException e) {
            System.out.println(" Error viewing candidates: " + e.getMessage());
        }
    }

    // Schedule Interview
    public void scheduleInterview() {
        if (!isConnected()) return;

        System.out.print("Enter candidate ID: ");
        String candidateId = sc.nextLine();
        System.out.print("Enter interviewer name: ");
        String interviewer = sc.nextLine();
        System.out.print("Enter interview date (dd-mm-yyyy): ");
        String date = sc.nextLine();

        String id = "I" + (System.currentTimeMillis() % 10000);

        String sql = "INSERT INTO interviews (id, candidate_id, interviewer, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, candidateId);
            ps.setString(3, interviewer);
            ps.setString(4, date);
            ps.executeUpdate();
            System.out.println("Interview scheduled successfully!");
        } catch (SQLException e) {
            System.out.println("Error scheduling interview: " + e.getMessage());
        }
    }

    //  Update Candidate Status
    public void updateStatus() {
        if (!isConnected()) return;

        System.out.print("Enter candidate ID: ");
        String id = sc.nextLine();
        System.out.print("Enter new status (Selected / Rejected / Pending): ");
        String status = sc.nextLine().trim();

        String sql = "UPDATE candidates SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Status updated successfully!");
            else System.out.println("Candidate not found!");
        } catch (SQLException e) {
            System.out.println("Error updating status: " + e.getMessage());
        }
    }

    // View All Interviews
    public void viewInterviews() {
        if (!isConnected()) return;

        String sql = """
            SELECT i.id, c.name, c.position, i.interviewer, i.date
            FROM interviews i
            JOIN candidates c ON i.candidate_id = c.id
            """;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n---- Scheduled Interviews ----");
            System.out.println("ID    | Candidate       | Position   | Interviewer     | Date");
            System.out.println("---------------------------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5s | %-15s | %-10s | %-15s | %s%n",
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("interviewer"),
                        rs.getString("date"));
            }
            if (!found) System.out.println("No interviews scheduled.");
        } catch (SQLException e) {
            System.out.println("Error viewing interviews: " + e.getMessage());
        }
    }

    public void filterByStatus(){
        if(!isConnected()) return;
        System.out.println("Enter the status(Selected/Rejected/Pending): ");
        String status = sc.nextLine();

        String sql = "SELECT * FROM candidates where status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();;
            System.out.println("\nCandidates with status '" + status + "':");
            System.out.println("-------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%s | %s | %s%n",
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("status"));
            }

            if (!found) System.out.println("No candidates found with status " + status);
        } catch (SQLException e) {
            System.out.println("Error filtering candidates: " + e.getMessage());
        }
    }

    public void deleteCandidateById() {
        if (!isConnected()) return;

        System.out.print("Enter Candidate ID to delete: ");
        String candidateId = sc.nextLine();

        String deleteInterviewSQL = "DELETE FROM interviews WHERE candidate_id = ?";
        String deleteCandidateSQL = "DELETE FROM candidates WHERE id = ?";

        try (
                PreparedStatement ps1 = conn.prepareStatement(deleteInterviewSQL);
                PreparedStatement ps2 = conn.prepareStatement(deleteCandidateSQL)
        ) {
            // Step 1: delete related interview first (foreign key constraint)
            ps1.setString(1, candidateId);
            int interviewDeleted = ps1.executeUpdate();

            // Step 2: delete candidate record
            ps2.setString(1, candidateId);
            int candidateDeleted = ps2.executeUpdate();

            if (candidateDeleted > 0) {
                System.out.println("Candidate with ID " + candidateId + " deleted successfully.");
            } else {
                System.out.println(" No candidate found with ID " + candidateId);
            }

            if (interviewDeleted > 0) {
                System.out.println(" Related interview records also deleted (" + interviewDeleted + " row(s)).");
            }

        } catch (SQLException e) {
            System.out.println(" Error deleting candidate: " + e.getMessage());
        }
    }

    // Show Summary Report
    public void showSummaryReport() {
        if (!isConnected()) return;

        try (Statement st = conn.createStatement()) {
            ResultSet total = st.executeQuery("SELECT COUNT(*) FROM candidates");
            total.next();
            System.out.println("\n===== Summary Report =====");
            System.out.println("Total Candidates : " + total.getInt(1));

            String[] statuses = {"Selected", "Rejected", "Pending"};
            for (String s : statuses) {
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM candidates WHERE status='" + s + "'");
                rs.next();
                System.out.println(s + " : " + rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(" Error generating report: " + e.getMessage());
        }
    }

    //  Menu
    public void menu() {
        while (true) {
            System.out.println("\n===== Interview Management System =====");
            System.out.println("1. Add Candidate");
            System.out.println("2. Schedule Interview");
            System.out.println("3. Update Candidate Status");
            System.out.println("4. View All Candidates");
            System.out.println("5. View All Interviews");
            System.out.println("6. Filter Candidate By Status");
            System.out.println("7. Deleted Data By Id");
            System.out.println("8. Show Summary Report");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try { choice = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Invalid input!"); continue; }

            switch (choice) {
                case 1 -> addCandidate();
                case 2 -> scheduleInterview();
                case 3 -> updateStatus();
                case 4 -> viewAllCandidates();
                case 5 -> viewInterviews();
                case 6 -> filterByStatus();
                case 7 -> deleteCandidateById();
                case 8 -> showSummaryReport();
                case 9 -> {
                    closeConnection();
                    System.out.println(" Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    //  Close DB Connection Gracefully
    private void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println(" Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println(" Error closing connection: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        new InterviewSystem().menu();
    }
}
