import java.io.*;
import java.util.*;

public class CourseManagementSystem {

    static final int totalCourse = 100;
    static String[] courseIds = new String[totalCourse];
    static String[] courseNames = new String[totalCourse];
    static int[] creditHours = new int[totalCourse];
    static int numberOfCourses = 0;

    public static void main(String[] args) {
        Options();

        Scanner sc = new Scanner(System.in);
        System.out.print("Select an option: ");
        int option = sc.nextInt();
        sc.nextLine(); // clear buffer

        switch (option) {
            case 1 -> Login();
            case 2 -> Registration();
            default -> System.out.println("Invalid input");
        }
    }

    static void Options() {
        System.out.println("\n************* WELCOME TO COURSE MANAGEMENT SYSTEM *************\n");
        System.out.println("1. Login");
        System.out.println("2. Register");
    }

    static void Registration() {
        Scanner sc = new Scanner(System.in);
        String studentId, password;

        System.out.print("Enter your student Id: ");
        studentId = sc.nextLine();
        System.out.print("Enter the password: ");
        password = sc.nextLine();

        boolean exist = false;

        try (Scanner fileReader = new Scanner(new File("users.txt"))) {
            while (fileReader.hasNext()) {
                String fileId = fileReader.next();
                String filePass = fileReader.next();
                if (fileId.equals(studentId)) {
                    exist = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // file might not exist yet, ignore
        }

        if (exist) {
            System.out.println("This student ID " + studentId + " is already registered.");
        } else {
            try (FileWriter fw = new FileWriter("users.txt", true)) {
                fw.write(studentId + " " + password + "\n");
                System.out.println("Registration is successful");
            } catch (IOException e) {
                System.out.println("Error writing to file");
            }
        }
    }

    static void Login() {
        Scanner sc = new Scanner(System.in);
        String studentId, password;

        System.out.print("Enter your student_id: ");
        studentId = sc.nextLine();
        System.out.print("Enter your password: ");
        password = sc.nextLine();

        boolean success = false;

        try (Scanner fileReader = new Scanner(new File("users.txt"))) {
            while (fileReader.hasNext()) {
                String fileId = fileReader.next();
                String filePass = fileReader.next();
                if (fileId.equals(studentId) && filePass.equals(password)) {
                    success = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: users file not found");
        }

        if (success) {
            System.out.println("\n ************** Login successful! Welcome ************* ");
            courseOptions();
        } else {
            System.out.println("Login failed! Invalid ID or password.");
        }
    }

    static void courseOptions() {
        Scanner sc = new Scanner(System.in);
        int choice;
        String courseID;

        do {
            System.out.println("\n--- Course Options ---");
            System.out.println("1. Add Course");
            System.out.println("2. Delete Course");
            System.out.println("3. Search Course");
            System.out.println("4. Update Course");
            System.out.println("5. List All Courses");
            System.out.println("6. Logout / Exit");
            System.out.print("Select an Option: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1 -> {
                    courseLoad();
                    addCourse();
                }
                case 2 -> {
                    courseLoad();
                    System.out.print("Enter the course_id: ");
                    courseID = sc.nextLine();
                    deleteCourse(courseID);
                }
                case 3 -> {
                    courseLoad();
                    System.out.print("Enter the Course id: ");
                    courseID = sc.nextLine();
                    searchCourse(courseID);
                }
                case 4 -> {
                    courseLoad();
                    System.out.print("Enter course id: ");
                    courseID = sc.nextLine();
                    updateCourse(courseID);
                }
                case 5 -> {
                    courseLoad();
                    listCourses();
                }
                case 6 -> System.out.println("Logged out.");
                default -> System.out.println("Invalid input");
            }

        } while (choice != 6);
    }

    static void courseLoad() {
        numberOfCourses = 0;
        try (Scanner fileReader = new Scanner(new File("course.txt"))) {
            while (fileReader.hasNext() && numberOfCourses < totalCourse) {
                courseIds[numberOfCourses] = fileReader.next();
                courseNames[numberOfCourses] = fileReader.next();
                creditHours[numberOfCourses] = fileReader.nextInt();
                numberOfCourses++;
            }
        } catch (FileNotFoundException e) {
            // No file yet, ignore
        }
    }

    static void addToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter("course.txt"))) {
            for (int i = 0; i < numberOfCourses; i++) {
                out.println(courseIds[i] + " " + courseNames[i] + " " + creditHours[i]);
            }
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    static void addCourse() {
        Scanner sc = new Scanner(System.in);
        String courseId, courseName;
        int creditHrs;
        boolean exist = false;

        System.out.print("Enter course id: ");
        courseId = sc.nextLine();
        System.out.print("Enter the course Name: ");
        courseName = sc.nextLine();
        System.out.print("Enter the credit hours: ");
        creditHrs = sc.nextInt();

        for (int i = 0; i < numberOfCourses; i++) {
            if (courseIds[i].equals(courseId)) {
                exist = true;
                break;
            }
        }

        if (exist) {
            System.out.println("The entered information already exists.");
        } else {
            courseIds[numberOfCourses] = courseId;
            courseNames[numberOfCourses] = courseName;
            creditHours[numberOfCourses] = creditHrs;
            numberOfCourses++;
            addToFile();
            System.out.println("The course has been added successfully.");
        }
    }

    static void deleteCourse(String targetId) {
        boolean found = false;
        for (int i = 0; i < numberOfCourses; i++) {
            if (courseIds[i].equals(targetId)) {
                for (int x = i; x < numberOfCourses - 1; x++) {
                    courseIds[x] = courseIds[x + 1];
                    courseNames[x] = courseNames[x + 1];
                    creditHours[x] = creditHours[x + 1];
                }
                numberOfCourses--;
                found = true;
                break;
            }
        }

        if (found) {
            addToFile();
            System.out.println("Course " + targetId + " deleted successfully.");
        } else {
            System.out.println("Course " + targetId + " not found.");
        }
    }

    static void updateCourse(String targetId) {
        Scanner sc = new Scanner(System.in);
        String newId, courseName;
        int creditHrs;
        boolean found = false;

        for (int i = 0; i < numberOfCourses; i++) {
            if (courseIds[i].equals(targetId)) {
                System.out.println("\nCourse found. Enter new details:");
                System.out.print("Enter new course id: ");
                newId = sc.nextLine();
                System.out.print("Enter new course name: ");
                courseName = sc.nextLine();
                System.out.print("Enter new creditHours: ");
                creditHrs = sc.nextInt();

                courseIds[i] = newId;
                courseNames[i] = courseName;
                creditHours[i] = creditHrs;
                found = true;
                break;
            }
        }

        if (found) {
            addToFile();
            System.out.println("Course " + targetId + " updated successfully!");
        } else {
            System.out.println("Course with ID " + targetId + " not found.");
        }
    }

    static void searchCourse(String searchId) {
        boolean found = false;
        for (int i = 0; i < numberOfCourses; i++) {
            if (courseIds[i].equals(searchId)) {
                found = true;
                System.out.println("\n *********** Course Found! *********** ");
                System.out.println("ID: " + courseIds[i]);
                System.out.println("Name: " + courseNames[i]);
                System.out.println("Credit Hours: " + creditHours[i]);
            }
        }
        if (!found) {
            System.out.println("There is no course with ID " + searchId);
        }
    }

    static void listCourses() {
        System.out.println("\n *********** All Courses *********** ");
        for (int i = 0; i < numberOfCourses; i++) {
            System.out.println("ID: " + courseIds[i]);
            System.out.println("Name: " + courseNames[i]);
            System.out.println("Credit Hours: " + creditHours[i]);
            System.out.println("--------------------------");
        }
    }
}
