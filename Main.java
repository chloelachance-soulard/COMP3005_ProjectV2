import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Connection connection;
    static int currentlyloggedin;
    public static void main(String[] args) {
        //variables to connect to database
        String url = "jdbc:postgresql://localhost:5432/Try6";
        String user = "postgres";
        String password = "admin";
        try {
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
            //initiate login - ask which type of user is logging in
            Scanner scannerObj = new Scanner(System.in);
            System.out.println("Welcome to Chloe's Health and Fitness Club\n If you are a new member and would like to register," +
                    " please enter 1.\n If you already have an account and would like to login, please enter 2.");
            int choice1 = Integer.parseInt(scannerObj.nextLine());
            if(choice1 == 1){
                registerUser();
            }
            if(choice1 == 2) {
                System.out.print("Please select your account type: \n 1. Member \n 2. Trainer \n 3. Administrative Staff \n Enter choice:");
                int choice2 = Integer.parseInt(scannerObj.nextLine());
                if (choice2 == 1) {
                    memberLogin();
                }
                if (choice2 == 2) {
                    trainerLogin();
                }
                if (choice2 == 3) {
                    adminLogin();
                }
            }

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void registerUser() {
        //get information needed to create profile from new member
        Scanner scannerObj = new Scanner(System.in);
        System.out.print("We are glad you chose us to help you embark on your fitness journey! To start off, please enter your first name: ");
        String fname = scannerObj.nextLine();
        System.out.print("Thanks " + fname + ". Now please enter your last name: ");
        String lname = scannerObj.nextLine();
        System.out.println("Great, hello " + fname + " " + lname);
        System.out.print("Please enter a password for your account: ");
        String password = scannerObj.nextLine();
        System.out.print("Thanks! We just need a bit more information before your registration is complete... \n Please enter your current weight in lbs: ");
        int weight = Integer.parseInt(scannerObj.nextLine());
        System.out.print("Now please enter your current height in cms: ");
        int height = Integer.parseInt(scannerObj.nextLine());
        System.out.print("Lastly, please enter your current heart rate: ");
        int heart_rate = Integer.parseInt(scannerObj.nextLine());
        System.out.println("Thanks! Now creating your profile... ");
        //get member_id for profile
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT COUNT(*) AS mem_id FROM Members");
            ResultSet result = statement.getResultSet();
            int mem_id = 1000000;
            if (result.next()) {
                mem_id = result.getInt("mem_id")+1;
            }

        //Add member to member table
            String insertSQL = "INSERT INTO Members(member_id, first_name, last_name, member_password, curr_weight, curr_height, curr_heart_rate) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

                pstmt.setInt(1, mem_id);
                pstmt.setString(2, fname);
                pstmt.setString(3, lname);
                pstmt.setString(4, password);
                pstmt.setInt(5, weight);
                pstmt.setInt(6, height);
                pstmt.setInt(7, heart_rate);
                pstmt.executeUpdate();
                //Add health metrics to health statistics
                String insertSQL2 = "INSERT INTO HealthStatistics(min_weight, max_weight, min_heart_rate, max_heart_rate, member_id) VALUES (?,?,?,?,?)";
                try (PreparedStatement pstmt2 = connection.prepareStatement(insertSQL2)) {
                    pstmt2.setInt(1, weight);
                    pstmt2.setInt(2, weight);
                    pstmt2.setInt(3, heart_rate);
                    pstmt2.setInt(4, heart_rate);
                    pstmt2.setInt(5, mem_id);
                    pstmt2.executeUpdate();
                }catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println("Profile created successfully, registration complete! Here is your memberID: " + mem_id + "\nTo ensure everything works properly, please log in ");
                memberLogin();
            }catch (Exception e) {
                System.out.println(e);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void memberLogin(){
        try {
            Statement statement = connection.createStatement();
            Scanner scannerObj = new Scanner(System.in);
            //ask trainer to input their ID
            System.out.println("Enter your id: ");
            int inputted_id = Integer.parseInt(scannerObj.nextLine());

            //create statement to be able to get all members
            statement.executeQuery("SELECT * FROM Members");
            ResultSet resultSet = statement.getResultSet();
            int idFound = 0;
            String correct_password = "IDDOESNOTESIST";
            String memberName = "NONAMEYET";

            //loop through results to find id user inputted
            while (resultSet.next()){
                if(resultSet.getInt("member_id")==inputted_id){
                    idFound = 1;
                    //set correct password to password linked to profile
                    correct_password = resultSet.getString("member_password");
                    //get name
                    memberName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");

                    break;
                }
            }
            if(idFound == 0){
                System.out.print("No member with that ID exists, goodbye");
                return;
            }
            //ask user to enter their password
            System.out.println("Enter your password: ");
            String inputted_password = scannerObj.nextLine();

            //check to see if password is correct, log user in if it is
            if(inputted_password.equals(correct_password)){
                currentlyloggedin = inputted_id;
                System.out.print("password correct, logging you in...\n");
                System.out.print("WELCOME " + memberName);
                //Once User is logged in, give function choices
               memberChoice();
            }
            else{
                System.out.print("password is incorrect, goodbye");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void memberChoice() {
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Please select what you would like to do:\n 1. Manage my profile \n 2.Display my dashboard \n 3.Manage my Schedule");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1){
            profileManagement();
        }
        if(choice == 2) {
            System.out.println("Now displaying your dashboard");
            dashboard();
        }
        if(choice == 3){
            member_schedule();
        }
    }

    public static void profileManagement() {
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1){
        }
        if(choice == 2) {

        }
    }
    public static void dashboard(){
        try {
            //statement/query to retrieve all students from database
            Statement statement = connection.createStatement();
            //get health statistics from member profile
            statement.executeQuery("SELECT * FROM HealthStatistics WHERE member_id =" + currentlyloggedin + " ");
            ResultSet resultSet = statement.getResultSet();
            //go through health statistics and display results
            System.out.println("\nHEALTH STATISTICS");
            System.out.println("--------------------------------------------------");
            while (resultSet.next()) {
                System.out.println("Min Weight: "+ resultSet.getInt("min_weight") + "\t");
                System.out.println("Max Weight: "+ resultSet.getInt("max_weight") + "\t");
                System.out.println("Min Heart Rate: "+ resultSet.getInt("min_heart_rate") + "\t");
                System.out.println("Max Heart Rate: "+ resultSet.getInt("max_heart_rate") + "\t");
            }

            //go through fitness achievements and display results
            statement.executeQuery("SELECT * FROM FitnessGoal WHERE member_id =" + currentlyloggedin + " AND status = " + 1 + " ");
            ResultSet resultSet2 = statement.getResultSet();
            System.out.println("\nFitness Achievements");
            System.out.println("--------------------------------------------------");
            System.out.println("Achieved:");
            while (resultSet2.next()) {
                    System.out.println("Goal ID: " + resultSet2.getInt("goal_id") + "\t");
                    System.out.println("Goal: " + resultSet2.getString("goal") + "\t");
                    System.out.println("Started: " + resultSet2.getString("start_date") + "\t");
                    System.out.println("Ended: " + resultSet2.getString("end_date") + "\t");
                    System.out.println("------------");
            }

            //go through fitness goals in progress and display results
            statement.executeQuery("SELECT * FROM FitnessGoal WHERE member_id =" + currentlyloggedin + " AND status = " + 0 + " ");
            ResultSet resultSet3 = statement.getResultSet();
            System.out.println("In progress:");
            while (resultSet3.next()) {
                    System.out.println("Goal ID: " + resultSet3.getInt("goal_id") + "\t");
                    System.out.println("Goal: " + resultSet3.getString("goal") + "\t");
                    System.out.println("Started: " + resultSet3.getString("start_date") + "\t");
                    System.out.println("Ended: " + resultSet3.getString("end_date") + "\t");
                    System.out.println("------------");
            }
            statement.executeQuery("SELECT * FROM ExerciseRoutine WHERE member_id =" + currentlyloggedin + " ");
            ResultSet resultSet4 = statement.getResultSet();
            System.out.println("\nEXERCISE ROUTINES");
            System.out.println("--------------------------------------------------");
            while (resultSet4.next()) {
                System.out.println("Routine ID: " + resultSet4.getInt("routine_id") + "\t");
                System.out.println("Exercise: " + resultSet4.getString("exercise") + "\t");
                System.out.println("Number of reps: " + resultSet4.getInt("num_of_reps") + "\t");
                System.out.println("------------");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        memberChoice();
    }
    public static void member_schedule() {

        Scanner scannerObj = new Scanner(System.in);
        System.out.println("\n Please select what you would like to do: \n 1. View my schedule \n 2. Book a personal training session \n 3. Book a group class");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if (choice == 1) {
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM PersonalTraining WHERE member_id =" + currentlyloggedin + " ");
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
                statement.executeQuery("SELECT * FROM SignedUpFor WHERE member_id =" + currentlyloggedin + " ");
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
                memberChoice();

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (choice == 2) {
            System.out.println("Select which trainer you would like to book with (please enter their id): ");
            getAllTrainers();
            int chosenTrainer = Integer.parseInt(scannerObj.nextLine());
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM Availability WHERE trainer_id = " + chosenTrainer + " ");
                ResultSet resultSet = statement.getResultSet();
                System.out.println("Please select the private session you would like to book: ");
                while (resultSet.next()) {
                    System.out.print("Availability ID: " + resultSet.getInt("availability_id") + "\t");
                    System.out.print("Date: " + resultSet.getString("available_date") + "\t");
                    System.out.print("Start Time: " + resultSet.getString("start_time") + "\t");
                    System.out.print("End Time: " + resultSet.getString("end_time") + "\t");
                    System.out.println("Price: " + resultSet.getInt("price") + "\t");
                }
                int chosenSession = Integer.parseInt(scannerObj.nextLine());
                statement.executeQuery("SELECT * FROM Availability WHERE trainer_id = " + chosenTrainer + " ");
                ResultSet chosen = statement.getResultSet();
                Time stime = Time.valueOf("00:00" + ":00");
                Time etime = Time.valueOf("00:00" + ":00");
                String date = "";
                int price = 0;
                while (chosen.next()) {
                    stime = Time.valueOf(chosen.getString("start_time"));
                    etime = Time.valueOf(chosen.getString("end_time"));
                    date = chosen.getString("available_date");
                    price = chosen.getInt("price");
                }

                String insertSQL2 = "INSERT INTO PersonalTraining(session_id, start_time, end_time, session_date, price, trainer_id, member_id, room_id) VALUES (?,?,?,?,?,?,?,?)";
                try (PreparedStatement pstmt2 = connection.prepareStatement(insertSQL2)) {
                    //fix this to get a unique ID (number of sessions already in the personal training thing + 1)
                    pstmt2.setInt(1, chosenSession + 100);
                    pstmt2.setTime(2, stime);
                    pstmt2.setTime(3, etime);
                    pstmt2.setDate(4, Date.valueOf(date));
                    pstmt2.setInt(5, price);
                    pstmt2.setInt(6, chosenTrainer);
                    pstmt2.setInt(7, currentlyloggedin);
                    pstmt2.setInt(8, chosenTrainer);
                    pstmt2.executeUpdate();

                    System.out.println("You have successfully registered for personal training session " + chosenSession);
                }
                //Now that the personal training session has been booked, delete it from the availability list
                String deleteSQL = "DELETE FROM Availability WHERE availability_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
                    Statement checkID = connection.createStatement();
                    //ResultSet resultSet1 = checkID.getResultSet();
                    pstmt.setInt(1, chosenSession);
                    pstmt.executeUpdate();


                } catch (Exception e) {
                    System.out.println(e);
                }
            }catch (Exception e) {
                System.out.println(e);
            }
        }

            if (choice == 3) {
                try {
                    Statement statement = connection.createStatement();
                    System.out.println("Here are the available group classes, please enter the class id of the one you would like to register for: ");
                    statement.executeQuery("SELECT * FROM GroupClass ");
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
                    int selectedClass = Integer.parseInt(scannerObj.nextLine());
                    String insertSQL2 = "INSERT INTO SignedUpFor(class_id, member_id) VALUES (?,?)";
                    try (PreparedStatement pstmt2 = connection.prepareStatement(insertSQL2)) {
                        pstmt2.setInt(1, selectedClass);
                        pstmt2.setInt(2, currentlyloggedin);
                        pstmt2.executeUpdate();
                        System.out.println("You have successfully registered for group class " + selectedClass);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                //go back to user menu
                memberChoice();
            }
        }

    public static void trainerLogin(){
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
            //CHANGE THIS SO THAT ITS A DIFFERENT SESSION ID EVERYTIME CORRESPONDING TO HOW MANY ARE IN THE TABLE
            pstmt.setInt(1, 100);
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
        try {
            Statement statement = connection.createStatement();
            Scanner scannerObj = new Scanner(System.in);
            //ask trainer to input name of member
            System.out.println("Please enter the name of the member you would like to look up: ");
            String inputted_name = scannerObj.nextLine();
            //statement.executeQuery("SELECT * FROM Members WHERE first_name =" + inputted_name + " ");
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
    public static void adminLogin(){
        try {
            Statement statement = connection.createStatement();
            Scanner scannerObj = new Scanner(System.in);
            //ask admin staff to input their ID
            System.out.println("Enter your id: ");
            int inputted_id = Integer.parseInt(scannerObj.nextLine());

            //create statement to be able to get all admin staff
            statement.executeQuery("SELECT * FROM AdminStaff");
            ResultSet resultSet = statement.getResultSet();
            int idFound = 0;
            String correct_password = "IDDOESNOTESIST";
            String admin_name = "nonameyet";
            //loop through results to find id user inputted
            while (resultSet.next()){
                if(resultSet.getInt("admin_id")==inputted_id){
                    idFound = 1;
                    //set correct password to password linked to profile
                    correct_password = resultSet.getString("admin_password");
                    admin_name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                    break;
                }
            }
            if(idFound == 0){
                System.out.print("No Admin with that ID exists");
                return;
            }
            //ask user to enter their password
            System.out.println("Enter your password: ");
            String inputted_password = scannerObj.nextLine();

            //check to see if password is correct, log user in if it is
            if(inputted_password.equals(correct_password)){
                currentlyloggedin = inputted_id;
                System.out.print("password correct, logging you in...\n");
                System.out.print("WELCOME" + admin_name);
            }
            else{
                System.out.print("password is incorrect, goodbye");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void getAllTrainers() {
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
