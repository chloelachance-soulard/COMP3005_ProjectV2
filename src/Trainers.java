import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Trainers {
    static int currentlyloggedin;
    static Connection connection;
    static String url = "jdbc:postgresql://localhost:5432/Project";
    static String user = "postgres";
    static String password = "admin";

    public static void trainerLogin(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        try {
            Statement statement = connection.createStatement();
            Scanner scannerObj = new Scanner(System.in);
            //ask trainer to input their ID
            System.out.println("Enter your id: ");
            int inputted_id = Integer.parseInt(scannerObj.nextLine());

            //create statement to be able to get all trainers
            statement.executeQuery("SELECT * FROM Trainers");
            ResultSet resultSet = statement.getResultSet();
            int idFound = 0;
            String correct_password = "IDDOESNOTESIST";
            String trainer_name = "NONAMEYET";
            //loop through results to find id user inputted
            while (resultSet.next()){
                if(resultSet.getInt("trainer_id")==inputted_id){
                    idFound = 1;
                    //set correct password to password linked to profile
                    correct_password = resultSet.getString("trainer_password");
                    //get trainer's name
                    trainer_name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                    break;
                }
            }
            if(idFound == 0){
                System.out.print("No trainer with that ID exists");
                return;
            }
            //ask user to enter their password
            System.out.println("Enter your password: ");
            String inputted_password = scannerObj.nextLine();

            //check to see if password is correct, log user in if it is
            if(inputted_password.equals(correct_password)){
                currentlyloggedin = inputted_id;
                System.out.print("password correct, logging you in...\n");
                System.out.println("WELCOME " + trainer_name);
                trainerOptions();
            }
            else{
                System.out.print("password is inccorrect, goodbye");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void trainerOptions(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Please select what you would like to do: \n 1. Set an available time \n 2. Look up member by name");

        int choice1 = Integer.parseInt(scannerObj.nextLine());
        if(choice1 == 1){
            trainerAvailability();
        }
        if(choice1 == 2) {
            memberLookup();
        }
    }
    public static void trainerAvailability(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        //ask trainer to input name of member
        System.out.println("Please enter the date of your availability: ");
        String date = scannerObj.nextLine();
        System.out.println("Please enter the start time of your availability: ");
        Time stime = Time.valueOf(scannerObj.nextLine()+":00");
        System.out.println("Please enter the end time of your availability: ");
        Time etime = Time.valueOf(scannerObj.nextLine()+":00");
        System.out.println("Please enter the price of your session: ");
        int price = Integer.parseInt(scannerObj.nextLine());
        String insertSQL = "INSERT INTO Availability(availability_id, start_time, end_time, available_date, price, trainer_id) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            Random rand = new Random();
            int upper = 100;
            int random_int = rand.nextInt(upper);
            pstmt.setInt(1, random_int);
            pstmt.setTime(2, stime);
            pstmt.setTime(3, etime);
            pstmt.setDate(4, Date.valueOf(date));
            pstmt.setInt(5, price);
            pstmt.setInt(6, currentlyloggedin);
            pstmt.executeUpdate();
            System.out.println("Availability added");

        }catch (Exception e) {
            System.out.println(e);
        }

    }
    public static void memberLookup(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        try {
            Statement statement = connection.createStatement();
            Scanner scannerObj = new Scanner(System.in);
            //ask trainer to input name of member
            System.out.println("Please enter the name of the member you would like to look up: ");
            String inputted_name = scannerObj.nextLine();
            statement.executeQuery("SELECT * FROM Members WHERE first_name = '" + inputted_name+ "' ");
            ResultSet chosen = statement.getResultSet();
            System.out.println("MEMBERS NAMED "+ inputted_name + ": ");
            System.out.println("----------------------------------");
            while (chosen.next()) {
                System.out.println("Member ID: " + chosen.getInt("member_id") + "\t");
                System.out.println("First name: " + chosen.getString("first_name") + "\t");
                System.out.println("Last name: " + chosen.getString("last_name") + "\t");
                System.out.println("------------");
            }
            System.out.println("Please enter the id of the member who's schedule you would like to see: ");
            String inputted_id = scannerObj.nextLine();
            System.out.println("Here is " + inputted_name+ "'s schedule: ");
            try {
                statement.executeQuery("SELECT * FROM PersonalTraining WHERE member_id =" + inputted_id + " ");
                ResultSet resultSet7 = statement.getResultSet();
                System.out.println("PERSONAL TRAINING SESSIONS ");
                System.out.println("--------------------------------------------------");
                while (resultSet7.next()) {
                    System.out.println("Session ID: " + resultSet7.getInt("session_id") + "\t");
                    System.out.println("Room: " + resultSet7.getInt("room_id") + "\t");
                    System.out.println("Trainer ID: " + resultSet7.getInt("trainer_id") + "\t");
                    System.out.println("Date: " + resultSet7.getString("session_date") + "\t");
                    System.out.println("Starting Time: " + resultSet7.getString("start_time") + "\t");
                    System.out.println("End Time: " + resultSet7.getString("end_time") + "\t");
                    System.out.println("Price: " + resultSet7.getInt("price") + "$" + "\t");
                    System.out.println("------------");
                }
                //Get group classes member is signed up for
                statement.executeQuery("SELECT * FROM SignedUpFor WHERE member_id =" + inputted_id + " ");
                ResultSet registered = statement.getResultSet();
                List<Integer> classes = new ArrayList<Integer>();
                System.out.println("GROUP CLASSES");
                System.out.println("--------------------------------------------------");
                while (registered.next()) {
                    classes.add(registered.getInt("class_id"));
                }
                for (int i = 0; i < classes.size(); i++) {
                    statement.executeQuery("SELECT * FROM GroupClass WHERE class_id =" + classes.get(i) + " ");
                    ResultSet resultSet = statement.getResultSet();
                    while (resultSet.next()) {
                        System.out.println("Class ID: " + resultSet.getInt("class_id") + "\t");
                        System.out.println("Room: " + resultSet.getInt("room_id") + "\t");
                        System.out.println("Trainer ID: " + resultSet.getInt("trainer_id") + "\t");
                        System.out.println("Date: " + resultSet.getString("class_date") + "\t");
                        System.out.println("Starting Time: " + resultSet.getString("start_time") + "\t");
                        System.out.println("End Time: " + resultSet.getString("end_time") + "\t");
                        System.out.println("Price: " + resultSet.getInt("price") + "$" + "\t");
                        System.out.println("------------");
                    }
                }
            }catch (Exception e) {
                System.out.println(e);
            }

        }catch (Exception e) {
            System.out.println(e);
        }

    }
    public static void getAllTrainers() {
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        try {
        //statement/query to retrieve all students from database
        Statement statement = connection.createStatement();
        statement.executeQuery("SELECT * FROM Trainers");
        ResultSet resultSet = statement.getResultSet();
        //go through results and display all of the students' information
        while (resultSet.next()) {
            System.out.print(resultSet.getInt("trainer_id") + "\t");
            System.out.print(resultSet.getString("first_name") + "\t");
            System.out.println(resultSet.getString("last_name") + "\t");
        }
    } catch (Exception e) {
        System.out.println(e);
        }
    }
}
