import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

// 1. Course Class representing individual course items
class Course {
    private String courseCode;
    private String courseTitle;
    private int unit;

    public Course(String courseCode, String courseTitle, int unit) {
        // Simple string processing: trimming and casing for clean uniformity
        this.courseCode = courseCode.trim().toUpperCase();
        this.courseTitle = courseTitle.trim();
        this.unit = unit;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public int getUnit() { return unit; }

    // Convert course details into a CSV row format
    public String toCSV() {
        return courseCode + "," + courseTitle + "," + unit;
    }
}

// 2. Main GUI Class managing operations and interface
public class CourseManagementGUI extends JFrame {
    private ArrayList<Course> courseList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable courseTable;
    private final String FILE_NAME = "courses.csv";

    public CourseManagementGUI() {
        setTitle("Student Course Management System");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Setup components
        initializeComponents();
    }

    private void initializeComponents() {
        // Main split layout
        setLayout(new BorderLayout(10, 10));

        // Table setup to display all stored courses
        String[] columns = {"Course Code", "Course Title", "Credit Units"};
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Sidebar Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(7, 1, 5, 5));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create operations menu buttons
        JButton btnAdd = new JButton("1. Add Course");
        JButton btnView = new JButton("2. View/Refresh Table");
        JButton btnSearch = new JButton("3. Search Course");
        JButton btnCompute = new JButton("4. Compute Total Units");
        JButton btnSave = new JButton("5. Save to File");
        JButton btnLoad = new JButton("6. Load from File");
        JButton btnExit = new JButton("7. Exit Program");

        menuPanel.add(btnAdd);
        menuPanel.add(btnView);
        menuPanel.add(btnSearch);
        menuPanel.add(btnCompute);
        menuPanel.add(btnSave);
        menuPanel.add(btnLoad);
        menuPanel.add(btnExit);

        add(menuPanel, BorderLayout.WEST);

        // UI action handling using our recursive processing controller
        btnAdd.addActionListener(e -> handleMenuSelection(1));
        btnView.addActionListener(e -> handleMenuSelection(2));
        btnSearch.addActionListener(e -> handleMenuSelection(3));
        btnCompute.addActionListener(e -> handleMenuSelection(4));
        btnSave.addActionListener(e -> handleMenuSelection(5));
        btnLoad.addActionListener(e -> handleMenuSelection(6));
        btnExit.addActionListener(e -> handleMenuSelection(7));
    }

    /**
     * Demonstrating a Recursive Menu Router pattern.
     * The method can sequentially evaluate or trigger action cycles programmatically.
     */
    private void handleMenuSelection(int choice) {
        // Base case or terminal condition handles structural routing
        if (choice < 1 || choice > 7) {
            JOptionPane.showMessageDialog(this, "Invalid Operation Selection.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (choice) {
            case 1:
                addCourse();
                break;
            case 2:
                refreshTableDisplay();
                break;
            case 3:
                searchCourse();
                break;
            case 4:
                computeTotalUnits();
                break;
            case 5:
                saveToFile();
                break;
            case 6:
                loadFromFile();
                break;
            case 7:
                System.exit(0);
                break;
        }
        
        // Example of simple operational recursion: automatic UI verification refresh 
        // by recursively calling option 2 (Refresh Table) after mutable state events.
        if (choice == 1 || choice == 6) {
            handleMenuSelection(2); 
        }
    }

    private void addCourse() {
        try {
            String code = JOptionPane.showInputDialog(this, "Enter Course Code (e.g., COS201):");
            if (code == null || code.trim().isEmpty()) return;

            String title = JOptionPane.showInputDialog(this, "Enter Course Title:");
            if (title == null || title.trim().isEmpty()) return;

            String unitStr = JOptionPane.showInputDialog(this, "Enter Credit Units:");
            if (unitStr == null) return;
            
            // Technical Feature: Exception error handling for invalid input numbers
            int unit = Integer.parseInt(unitStr.trim());
            if (unit < 0) throw new IllegalArgumentException("Units cannot be negative.");

            courseList.add(new Course(code, title, unit));
            JOptionPane.showMessageDialog(this, "Course successfully recorded local memory.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: Credit units must be an integer numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTableDisplay() {
        // Resets GUI table model rows cleanly
        tableModel.setRowCount(0);
        for (Course c : courseList) {
            tableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseTitle(), c.getUnit()});
        }
    }

    private void searchCourse() {
        String targetCode = JOptionPane.showInputDialog(this, "Enter Course Code to Search:");
        if (targetCode == null || targetCode.trim().isEmpty()) return;
        
        targetCode = targetCode.trim().toUpperCase();
        
        // Technical Feature: Performing a recursive lookup demonstration across list indexes
        int index = recursiveSearch(targetCode, 0);
        
        if (index != -1) {
            Course match = courseList.get(index);
            JOptionPane.showMessageDialog(this, "Course Found!\n\n" +
                    "Code: " + match.getCourseCode() + "\n" +
                    "Title: " + match.getCourseTitle() + "\n" +
                    "Units: " + match.getUnit(), "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Course code not found in current registers.", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Helper Recursive Search Implementation
    private int recursiveSearch(String code, int index) {
        // Base case 1: Element not found index boundary exceeded
        if (index >= courseList.size()) {
            return -1;
        }
        // Base case 2: Element found match criteria
        if (courseList.get(index).getCourseCode().equalsIgnoreCase(code)) {
            return index;
        }
        // Recursive call stepping into next array node index
        return recursiveSearch(code, index + 1);
    }

    private void computeTotalUnits() {
        int total = 0;
        for (Course c : courseList) {
            total += c.getUnit();
        }
        JOptionPane.addImpl(new JLabel(), null, total);
        JOptionPane.showMessageDialog(this, "Total Registered Credit Units: " + total + " Units", "Metrics Calculation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveToFile() {
        // File Writing Feature saving directly to a plain-text structured CSV format
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Course c : courseList) {
                writer.println(c.toCSV());
            }
            JOptionPane.showMessageDialog(this, "All database courses successfully saved to " + FILE_NAME);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred writing data to storage file system.", "File Input/Output Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromFile() {
        // File Reading Feature importing CSV split parsed string data structures
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No saved data state found (" + FILE_NAME + ").", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            courseList.clear(); // Flush cache layout before execution read sequences
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 3) {
                    String code = tokens[0];
                    String title = tokens[1];
                    int unit = Integer.parseInt(tokens[2]);
                    courseList.add(new Course(code, title, unit));
                }
            }
            JOptionPane.showMessageDialog(this, "Database courses populated from safe recovery storage tracking data.");
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error processing data file parsing configurations.", "Parsing Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Multi-thread safe window initialization
        SwingUtilities.invokeLater(() -> {
            new CourseManagementGUI().setVisible(true);
        });
    }
}
