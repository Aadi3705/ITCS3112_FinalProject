import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//class User focuses on creating user accounts or signing in to an existing account
public class User {
    public static String sessUserId; //variable created for storing the userId for a user in a current session

    //the userChoice() method focuses on giving a customer two choices: signing in to an account, or creating a new one
    public void userChoice() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current customers-press Y to login, new customers-press N to continue");
        String choice = scanner.nextLine();
        if(choice.equalsIgnoreCase("Y")) {
            userSignIn();
            new BankEntryPoint().userOptions();
        }
        System.out.println("New Account-press Y, exit-N");
        String choice2 = scanner.nextLine();
        if(choice2.equalsIgnoreCase("Y")) {
            createNewAccount();
        }

    }
    //this method will perform the operation of signing in to an account
    static void userSignIn() throws SQLException{
        Scanner scanner2 = new Scanner(System.in);
        System.out.println("Enter your userId.");
        String userId = scanner2.nextLine();
        System.out.println("Enter your password.");
        String password = scanner2.nextLine();
        //implementation of checking if the user and password is in the database
        String url = "jdbc:mysql://localhost:3306/bank_schemas";
        String sql = "SELECT passcode FROM customer WHERE user_id = ?";

        try(Connection conn = DriverManager.getConnection(url,"root", "AN3705@nair!"); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String pass = rs.getString("passcode");
                if(pass.equals(password)) {
                    sessUserId = userId;
                    System.out.println("Welcome back: " + userId);
                } else {
                    System.out.println("Wrong password!");
                }
            }
        }





        //implementation of going to the class for existing user options



    }
    //the method will perform the operation of creating an account, and storing information in the Customer database
    static void createNewAccount() {
        //part of the method that focuses on retrieving the details
        Scanner console = new Scanner(System.in);
        System.out.println("Please enter your first name.");
        String firstName = console.nextLine();
        System.out.println("Please enter your last name.");
        String lastName = console.nextLine();
        System.out.println("Enter you date of birth. Enter in standard format...yyyy/MM/dd");
        String dateOfBirth = console.nextLine();
        var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //pass the overloaded constructor with the formatter
        LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
        if(birthDate.isAfter(LocalDate.of(2007, Month.FEBRUARY, 6))) {
            System.out.println("You are too young for a membership");
            createNewAccount();
            return;
        }
        Date sqlDateOfBirth = Date.valueOf(birthDate);

        System.out.println("Please enter your email address.");
        String email = console.nextLine();
        System.out.println("Please enter your password.");
        String password = console.nextLine();
        System.out.println("Please enter a userId.");
        String userId = console.nextLine();
        System.out.println("Please enter SSN.");
        int ssn = console.nextInt();
        //uses the insertRow() method to store details in SQL table
        try {
            insertRow(firstName, lastName, sqlDateOfBirth, email, password, userId, ssn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //method which will do the operation of storing details into SQL table
    static void insertRow(String firstName, String lastName, Date dateOfBirth, String email, String password, String userId, int ssn) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bank_schemas";

        String sql = "INSERT INTO customer( first_name, last_name, birth_date, email, passcode, user_id, ssn) VALUES(?,?,?,?,?,?,?)";

        try(Connection conn = DriverManager.getConnection(url, "root", "AN3705@nair!"); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setDate(3, dateOfBirth);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setString(6, userId);
            ps.setInt(7, ssn);


            ps.executeUpdate();

            System.out.println("Update successful!");

        }


    }
}
