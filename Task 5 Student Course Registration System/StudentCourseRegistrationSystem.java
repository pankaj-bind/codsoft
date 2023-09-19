import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

class Course {
    private String code;
    private String title;
    private String description;
    private int capacity;
    private String schedule;

    public Course(String code, String title, String description, int capacity, String schedule) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return code + " - " + title;
    }
}

class Student {
    private String id;
    private String name;
    private ArrayList<Course> registeredCourses;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    public void addCourse(Course course) {
        registeredCourses.add(course);
    }

    public void removeCourse(Course course) {
        registeredCourses.remove(course);
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

class CourseDatabase {
    private ArrayList<Course> courses;

    public CourseDatabase() {
        courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public ArrayList<Course> listAllCourses() {
        return courses;
    }
}

class StudentDatabase {
    private ArrayList<Student> students;

    public StudentDatabase() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public ArrayList<Student> listAllStudents() {
        return students;
    }
}

class CourseListingFrame extends JFrame {
    private JTextArea courseListArea;

    public CourseListingFrame(CourseDatabase courseDatabase) {
        setTitle("Course Listing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        courseListArea = new JTextArea();
        courseListArea.setEditable(false);

        ArrayList<Course> courses = courseDatabase.listAllCourses();

        for (Course course : courses) {
            courseListArea.append(
                    course.getCode() + " - " + course.getTitle() + " (Slots: " + (course.getCapacity() - 1) + ")\n");
        }

        add(courseListArea);
        setVisible(true);
    }
}

class StudentRegistrationFrame extends JFrame {
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;
    private JButton registerButton;
    private CourseDatabase courseDatabase; // Added

    public StudentRegistrationFrame(StudentDatabase studentDatabase, CourseDatabase courseDatabase) {
        this.courseDatabase = courseDatabase; // Added
        setTitle("Student Registration");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentComboBox = new JComboBox<>();
        ArrayList<Student> students = studentDatabase.listAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student.toString());
        }

        courseComboBox = new JComboBox<>();
        ArrayList<Course> courses = courseDatabase.listAllCourses();
        for (Course course : courses) {
            courseComboBox.addItem(course.toString());
        }

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String selectedCourse = (String) courseComboBox.getSelectedItem();

                String studentId = selectedStudent.split(" - ")[0];
                String courseCode = selectedCourse.split(" - ")[0];

                Student selectedStudentObject = null;
                for (Student student : students) {
                    if (student.getId().equals(studentId)) {
                        selectedStudentObject = student;
                        break;
                    }
                }

                Course selectedCourseObject = null;
                for (Course course : courses) {
                    if (course.getCode().equals(courseCode)) {
                        selectedCourseObject = course;
                        break;
                    }
                }

                if (selectedStudentObject != null && selectedCourseObject != null) {
                    selectedStudentObject.addCourse(selectedCourseObject);
                    courseDatabase.removeCourse(selectedCourseObject);
                    JOptionPane.showMessageDialog(null, "Registered for course: " + selectedCourse);
                } else {
                    JOptionPane.showMessageDialog(null, "Error registering for course.");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(studentComboBox);
        panel.add(courseComboBox);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }
}

class CourseRemovalFrame extends JFrame {
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;
    private JButton removeButton;
    private StudentDatabase studentDatabase;
    private CourseDatabase courseDatabase;

    public CourseRemovalFrame(StudentDatabase studentDatabase, CourseDatabase courseDatabase) {
        this.studentDatabase = studentDatabase;
        this.courseDatabase = courseDatabase;
        setTitle("Course Removal");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentComboBox = new JComboBox<>();
        ArrayList<Student> students = studentDatabase.listAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student.toString());
        }

        courseComboBox = new JComboBox<>();

        studentComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                courseComboBox.removeAllItems();
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String studentId = selectedStudent.split(" - ")[0];

                Student selectedStudentObject = null;
                for (Student student : students) {
                    if (student.getId().equals(studentId)) {
                        selectedStudentObject = student;
                        break;
                    }
                }

                if (selectedStudentObject != null) {
                    ArrayList<Course> registeredCourses = selectedStudentObject.getRegisteredCourses();
                    for (Course course : registeredCourses) {
                        courseComboBox.addItem(course.toString());
                    }
                }
            }
        });

        removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String selectedCourse = (String) courseComboBox.getSelectedItem();

                String studentId = selectedStudent.split(" - ")[0];
                String courseCode = selectedCourse.split(" - ")[0];

                Student selectedStudentObject = null;
                for (Student student : students) {
                    if (student.getId().equals(studentId)) {
                        selectedStudentObject = student;
                        break;
                    }
                }

                Course selectedCourseObject = null;
                for (Course course : courseDatabase.listAllCourses()) {
                    if (course.getCode().equals(courseCode)) {
                        selectedCourseObject = course;
                        break;
                    }
                }

                if (selectedStudentObject != null && selectedCourseObject != null) {
                    selectedStudentObject.removeCourse(selectedCourseObject);
                    courseDatabase.addCourse(selectedCourseObject);
                    JOptionPane.showMessageDialog(null, "Removed course: " + selectedCourse);
                } else {
                    JOptionPane.showMessageDialog(null, "Error removing course.");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(studentComboBox);
        panel.add(courseComboBox);
        panel.add(removeButton);

        add(panel);
        setVisible(true);
    }
}

public class StudentCourseRegistrationSystem {
    public static void main(String[] args) {
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.addCourse(new Course("CS101", "Intro to Java", "Introduction to Java Programming", 30, "MWF 9:00 AM"));
        courseDatabase.addCourse(new Course("CS102", "Data Structures", "Advanced Data Structures", 25, "TTH 2:00 PM"));
        courseDatabase.addCourse(new Course("CS103", "Algorithms", "Introduction to Algorithms", 20, "MWF 1:00 PM"));
        courseDatabase.addCourse(new Course("CS104", "Database Systems", "Introduction to Database Management", 25, "TTH 10:00 AM"));
        courseDatabase.addCourse(new Course("CS105", "Web Development", "Introduction to Web Development", 30, "MWF 3:00 PM"));
        courseDatabase.addCourse(new Course("CS106", "Artificial Intelligence", "Introduction to AI", 20, "TTH 3:00 PM"));
        courseDatabase.addCourse(new Course("CS107", "Computer Networks", "Introduction to Networking", 25, "MWF 10:00 AM"));
        courseDatabase.addCourse(new Course("CS108", "Operating Systems", "Introduction to Operating Systems", 20, "TTH 1:00 PM"));
        courseDatabase.addCourse(new Course("CS109", "Computer Graphics", "Introduction to Computer Graphics", 30, "MWF 2:00 PM"));
        courseDatabase.addCourse(new Course("CS110", "Software Engineering", "Introduction to Software Engineering", 25, "TTH 9:00 AM"));

        StudentDatabase studentDatabase = new StudentDatabase();
        studentDatabase.addStudent(new Student("1001", "John Doe"));
        studentDatabase.addStudent(new Student("1002", "Jane Smith"));
        studentDatabase.addStudent(new Student("1003", "James Johnson"));
        studentDatabase.addStudent(new Student("1004", "Emily Davis"));
        studentDatabase.addStudent(new Student("1005", "Michael Brown"));
        studentDatabase.addStudent(new Student("1006", "Olivia Taylor"));
        studentDatabase.addStudent(new Student("1007", "William Anderson"));
        studentDatabase.addStudent(new Student("1008", "Emma Thomas"));
        studentDatabase.addStudent(new Student("1009", "Joseph Jackson"));
        studentDatabase.addStudent(new Student("1010", "Ava White"));
        studentDatabase.addStudent(new Student("1011", "Alexander Harris"));
        studentDatabase.addStudent(new Student("1012", "Sophia Martin"));
        studentDatabase.addStudent(new Student("1013", "Daniel Thompson"));
        studentDatabase.addStudent(new Student("1014", "Isabella Garcia"));
        studentDatabase.addStudent(new Student("1015", "Matthew Martinez"));
        studentDatabase.addStudent(new Student("1016", "Charlotte Robinson"));
        studentDatabase.addStudent(new Student("1017", "Samuel Clark"));
        studentDatabase.addStudent(new Student("1018", "Oliver Rodriguez"));
        studentDatabase.addStudent(new Student("1019", "Lucas Lewis"));
        studentDatabase.addStudent(new Student("1020", "Amelia Lee"));
        


        CourseListingFrame courseListingFrame = new CourseListingFrame(courseDatabase);
        StudentRegistrationFrame studentRegistrationFrame = new StudentRegistrationFrame(studentDatabase,
                courseDatabase);
        CourseRemovalFrame courseRemovalFrame = new CourseRemovalFrame(studentDatabase, courseDatabase);
    }
}
