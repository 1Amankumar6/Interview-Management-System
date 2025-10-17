import java.util.*;

class Candidate {
    private String id;
    private String name;
    private String position;
    private int experience;
    private String status; // Pending, Selected, Rejected

    public Candidate(String id, String name, String position, int experience) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.experience = experience;
        this.status = "Pending";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getExperience() {
        return experience;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-15s | %-10s | %2d yrs | %-10s",
                id, name, position, experience, status);
    }
}

class Interview {
    private String id;
    private Candidate candidate;
    private String interviewer;
    private String date;

    public Interview(String id, Candidate candidate, String interviewer, String date) {
        this.id = id;
        this.candidate = candidate;
        this.interviewer = interviewer;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%-5s | %-15s | %-10s | %-15s | %s",
                id, candidate.getName(), candidate.getPosition(), interviewer, date);
    }
}

class InterviewSystem {
    private List<Candidate> candidates = new ArrayList<>();
    private List<Interview> interviews = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    private int candidateCounter = 1;
    private int interviewCounter = 1;

    private static final List<String> ALLOWED_POSITIONS = Arrays.asList("SDE", "SDET", "SWE", "SE");

    public void addCandidate() {
        System.out.print("Enter candidate name: ");
        String name = sc.nextLine().trim();

        System.out.println("Available Positions: " + ALLOWED_POSITIONS);
        System.out.print("Enter position applied for: ");
        String position = sc.nextLine().trim().toUpperCase();

        if (!ALLOWED_POSITIONS.contains(position)) {
            System.out.println("Invalid position! Please choose from " + ALLOWED_POSITIONS);
            return;
        }

        System.out.print("Enter experience (in years): ");
        int exp;
        try {
            exp = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid experience input!");
            return;
        }

        Candidate c = new Candidate("C" + candidateCounter++, name, position, exp);
        candidates.add(c);
        System.out.println("Candidate added successfully!");
    }

    public void scheduleInterview() {
        System.out.print("Enter candidate ID: ");
        String id = sc.nextLine();

        Candidate candidate = findCandidate(id);
        if (candidate == null) {
            System.out.println("Candidate not found!");
            return;
        }

        System.out.print("Enter interviewer name: ");
        String interviewer = sc.nextLine();
        System.out.print("Enter interview date (dd-mm-yyyy): ");
        String date = sc.nextLine();

        Interview interview = new Interview("I" + interviewCounter++, candidate, interviewer, date);
        interviews.add(interview);
        System.out.println("Interview scheduled successfully!");
    }

    public void updateStatus() {
        System.out.print("Enter candidate ID: ");
        String id = sc.nextLine();
        Candidate candidate = findCandidate(id);

        if (candidate == null) {
            System.out.println("Candidate not found!");
            return;
        }

        System.out.print("Enter new status (Selected / Rejected / Pending): ");
        String status = sc.nextLine().trim();

        if (!List.of("Selected", "Rejected", "Pending").contains(status)) {
            System.out.println("Invalid status entered!");
            return;
        }

        candidate.setStatus(status);
        System.out.println("Status updated successfully!");
    }

    public void viewAllCandidates() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates found!");
            return;
        }

        System.out.println("\n---- Candidate List ----");
        System.out.println("ID    | Name            | Position   | Exp | Status");
        System.out.println("---------------------------------------------------");
        for (Candidate c : candidates) {
            System.out.println(c);
        }
    }

    public void searchCandidate() {
        System.out.print("Enter name or position to search: ");
        String key = sc.nextLine().toLowerCase();

        System.out.println("\n---- Search Results ----");
        boolean found = false;
        for (Candidate c : candidates) {
            if (c.getName().toLowerCase().contains(key) || c.getPosition().toLowerCase().contains(key)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found)
            System.out.println("No matching candidates found!");
    }

    public void viewInterviews() {
        if (interviews.isEmpty()) {
            System.out.println("No interviews scheduled!");
            return;
        }

        System.out.println("\n---- Scheduled Interviews ----");
        System.out.println("ID    | Candidate       | Position   | Interviewer     | Date");
        System.out.println("---------------------------------------------------------------");
        for (Interview i : interviews) {
            System.out.println(i);
        }
    }

    public void filterByStatus() {
        System.out.print("Enter status to filter (Selected / Rejected / Pending): ");
        String status = sc.nextLine().trim();

        System.out.println("\n---- " + status.toUpperCase() + " Candidates ----");
        boolean found = false;
        for (Candidate c : candidates) {
            if (c.getStatus().equalsIgnoreCase(status)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found)
            System.out.println("No candidates found with status: " + status);
    }

    public void deleteCandidate() {
        System.out.print("Enter candidate ID to delete: ");
        String id = sc.nextLine();

        Candidate candidate = findCandidate(id);
        if (candidate == null) {
            System.out.println("Candidate not found!");
            return;
        }

        System.out.print("Are you sure you want to delete " + candidate.getName() + "? (y/n): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            candidates.remove(candidate);
            System.out.println("Candidate deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public void showSummaryReport() {
        long selected = candidates.stream().filter(c -> c.getStatus().equalsIgnoreCase("Selected")).count();
        long rejected = candidates.stream().filter(c -> c.getStatus().equalsIgnoreCase("Rejected")).count();
        long pending = candidates.stream().filter(c -> c.getStatus().equalsIgnoreCase("Pending")).count();

        System.out.println("\n===== Summary Report =====");
        System.out.println("Total Candidates : " + candidates.size());
        System.out.println("Selected          : " + selected);
        System.out.println("Rejected          : " + rejected);
        System.out.println("Pending           : " + pending);
    }

    private Candidate findCandidate(String id) {
        for (Candidate c : candidates) {
            if (c.getId().equalsIgnoreCase(id))
                return c;
        }
        return null;
    }

    public void menu() {
        while (true) {
            System.out.println("\n===== Interview Management System =====");
            System.out.println("1. Add Candidate");
            System.out.println("2. Schedule Interview");
            System.out.println("3. Update Candidate Status");
            System.out.println("4. View All Candidates");
            System.out.println("5. Search Candidate");
            System.out.println("6. View All Interviews");
            System.out.println("7. Filter Candidates by Status");
            System.out.println("8. Delete Candidate");
            System.out.println("9. Show Summary Report");
            System.out.println("10. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1 -> addCandidate();
                case 2 -> scheduleInterview();
                case 3 -> updateStatus();
                case 4 -> viewAllCandidates();
                case 5 -> searchCandidate();
                case 6 -> viewInterviews();
                case 7 -> filterByStatus();
                case 8 -> deleteCandidate();
                case 9 -> showSummaryReport();
                case 10 -> {
                    System.out.println("Goodbye! Have a great day!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        InterviewSystem system = new InterviewSystem();
        system.menu();
    }
}
