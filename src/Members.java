import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Members {
    static int currentlyloggedin;
    static Connection connection;
    static String url = "jdbc:postgresql://localhost:5432/Project";
    static String user = "postgres";
    static String password = "admin";


    public static void registerUser() {
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
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
                System.out.println("WELCOME " + memberName);
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
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Please select what you would like to do:\n 1. Manage my profile \n 2. Display my dashboard \n 3. Manage my Schedule");
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
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Please select what you would like to do: \n 1.Update my personal information \n 2.Update my fitness goals \n 3.Update my health metric");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1) {
            System.out.println("Please select what you would like to update: \n 1.First Name \n 2.Last name \n 3.Date of Birth \n 4. Address");
            int choice2 = Integer.parseInt(scannerObj.nextLine());
            if (choice2 == 1) {
                System.out.print("Please enter your new first name: ");
                String newName = scannerObj.nextLine();
                String updateSQL = "UPDATE Members SET first_name = ? WHERE member_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    //Statement statement = connection.createStatement();
                    //ResultSet resultSet = statement.getResultSet();
                    pstmt.setString(1, newName);
                    pstmt.setInt(2, currentlyloggedin);
                    pstmt.executeUpdate();
                    System.out.println("Update complete");
                    memberChoice();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (choice2 == 2) {
                System.out.print("Please enter your new last name: ");
                String newName = scannerObj.nextLine();
                String updateSQL = "UPDATE Members SET last_name = ? WHERE member_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    pstmt.setString(1, newName);
                    pstmt.setInt(2, currentlyloggedin);
                    pstmt.executeUpdate();
                    System.out.println("Update complete");
                    memberChoice();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (choice2 == 3) {
                System.out.print("Please enter your birthday: ");
                Date date = Date.valueOf(scannerObj.nextLine());
                String updateSQL = "UPDATE Members SET DOB = ? WHERE member_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    pstmt.setDate(1, date);
                    pstmt.setInt(2, currentlyloggedin);
                    pstmt.executeUpdate();
                    System.out.println("Update complete");
                    memberChoice();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (choice2 == 4) {
                System.out.print("Please enter your new house number: ");
                int num = Integer.parseInt(scannerObj.nextLine());
                String updateSQL = "UPDATE Members SET home_num = ? WHERE member_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, num);
                    pstmt.setInt(2, currentlyloggedin);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e);
                }
                String street = scannerObj.nextLine();
                String updateSQL2 = "UPDATE Members SET street = ? WHERE member_id = ?";
                try (PreparedStatement pstmt2 = connection.prepareStatement(updateSQL2)) {
                    pstmt2.setString(1, street);
                    pstmt2.setInt(2, currentlyloggedin);
                    pstmt2.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e);
                }
                String pcode = scannerObj.nextLine();
                String updateSQL3 = "UPDATE Members SET pcode = ? WHERE member_id = ?";
                try (PreparedStatement pstmt3 = connection.prepareStatement(updateSQL3)) {
                    pstmt3.setString(1, pcode);
                    pstmt3.setInt(2, currentlyloggedin);
                    pstmt3.executeUpdate();
                    System.out.println("Update complete");
                    memberChoice();
                } catch (Exception e) {
                    System.out.println(e);
                }
                memberChoice();
            }
        }
        if(choice == 2) {
            System.out.println("Please select what you would like to do: \n 1.Create a new goal \n 2. Mark a current fitness goal as completed \n 3. Delete a fitness goal");
            int choice2 = Integer.parseInt(scannerObj.nextLine());
            if (choice2 == 1) {
                System.out.print("Please enter the start date: ");
                Date sdate = Date.valueOf(scannerObj.nextLine());
                System.out.print("Please enter the end date: ");
                Date edate = Date.valueOf(scannerObj.nextLine());
                System.out.print("Please enter your goal: ");
                String goal = scannerObj.nextLine();
                String insertSQL2 = "INSERT INTO FitnessGoal(goal_id, start_date, end_date, goal, status, member_id) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement pstmt2 = connection.prepareStatement(insertSQL2)) {
                    Random rand = new Random();
                    int upper = 100;
                    int random_int = rand.nextInt(upper);
                    pstmt2.setInt(1, random_int);
                    pstmt2.setDate(2, sdate);
                    pstmt2.setDate(3, edate);
                    pstmt2.setString(4, goal);
                    pstmt2.setInt(5, 0);
                    pstmt2.setInt(6, currentlyloggedin);
                    pstmt2.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e);
                }
                memberChoice();
            }
            if (choice2 == 2) {
                try {
                    Statement statement = connection.createStatement();

                    System.out.println("Here are you current fitness goals: ");
                    statement.executeQuery("SELECT * FROM FitnessGoal WHERE member_id =" + currentlyloggedin + " AND status = " + 0 + " ");
                    ResultSet resultSet2 = statement.getResultSet();
                    while (resultSet2.next()) {
                        System.out.println("Goal ID: " + resultSet2.getInt("goal_id") + "\t");

                        System.out.println("Goal: " + resultSet2.getString("goal") + "\t");
                        System.out.println("Started: " + resultSet2.getString("start_date") + "\t");
                        System.out.println("Ended: " + resultSet2.getString("end_date") + "\t");
                        System.out.println("------------");
                    }
                    System.out.println("Please enter the ID of the goal you completed: ");
                    int selected = Integer.parseInt(scannerObj.nextLine());
                    String updateSQL = "UPDATE FitnessGoal SET status = ? WHERE goal_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                        pstmt.setInt(1, 1);
                        pstmt.setInt(2, selected);
                        pstmt.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                memberChoice();
            }

            if (choice2 == 3) {
                try {
                    Statement statement = connection.createStatement();
                    System.out.println("Here are you current fitness goals: ");
                    statement.executeQuery("SELECT * FROM FitnessGoal WHERE member_id =" + currentlyloggedin + " AND status = " + 0 + " ");
                    ResultSet resultSet2 = statement.getResultSet();
                    while (resultSet2.next()) {
                        System.out.println("Goal ID: " + resultSet2.getInt("goal_id") + "\t");
                        System.out.println("Goal: " + resultSet2.getString("goal") + "\t");
                        System.out.println("Started: " + resultSet2.getString("start_date") + "\t");
                        System.out.println("Ended: " + resultSet2.getString("end_date") + "\t");
                        System.out.println("------------");
                    }
                    System.out.println("Please enter the ID of the goal you would like to delete: ");
                    int selected = Integer.parseInt(scannerObj.nextLine());
                    String deleteSQL = "DELETE FROM FitnessGoal WHERE goal_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
                        pstmt.setInt(1, selected);
                        pstmt.executeUpdate();
                        System.out.println("goal deleted");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                memberChoice();
            }
        }
        if(choice == 3){
            System.out.println("Please select what you would like to update: \n 1.current weight \n 2.current height \n 3.current heart rate");
            int choice3 = Integer.parseInt(scannerObj.nextLine());
            if(choice3 == 1) {
                try {
                    Statement statement = connection.createStatement();
                    System.out.print("Please enter your new weight: ");
                    int weight = Integer.parseInt(scannerObj.nextLine());
                    statement.executeQuery("SELECT * FROM HealthStatistics WHERE member_id =" + currentlyloggedin + " ");
                    ResultSet resultSet = statement.getResultSet();
                    int min_weight = 0;
                    int max_weight = 0;
                    //loop through results to see if we need to update health stats
                    while (resultSet.next()) {
                        //get min_weight
                        min_weight = resultSet.getInt("min_weight");
                        //get max_weight
                        max_weight = resultSet.getInt("max_weight");
                        break;
                    }
                    //update health stats if needed
                    if(weight>max_weight){
                        String updateSQL = "UPDATE HealthStatistics SET max_weight = ? WHERE member_id = ?";
                        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                            pstmt.setInt(1, weight);
                            pstmt.setInt(2, currentlyloggedin);
                            pstmt.executeUpdate();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    if(weight<min_weight){
                        String updateSQL = "UPDATE HealthStatistics SET min_weight = ? WHERE member_id = ?";
                        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                            pstmt.setInt(1, weight);
                            pstmt.setInt(2, currentlyloggedin);
                            pstmt.executeUpdate();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    String updateSQL = "UPDATE Members SET curr_weight = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                        pstmt.setInt(1, weight);
                        pstmt.setInt(2, currentlyloggedin);
                        pstmt.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }catch (Exception e) {
                    System.out.println(e);
                }
                memberChoice();
            }
            if(choice3 == 2){
                System.out.print("Please enter your new height: ");
                int height = Integer.parseInt(scannerObj.nextLine());
                String updateSQL = "UPDATE Members SET curr_height = ? WHERE member_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, height);
                    pstmt.setInt(2, currentlyloggedin);
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    System.out.println(e);
                }
                memberChoice();
            }
            if(choice3 == 3){
                try {
                    Statement statement = connection.createStatement();
                    System.out.print("Please enter your new heart rate: ");
                    int heart_rate = Integer.parseInt(scannerObj.nextLine());
                    statement.executeQuery("SELECT * FROM HealthStatistics WHERE member_id =" + currentlyloggedin + " ");
                    ResultSet resultSet = statement.getResultSet();
                    int min_hr = 0;
                    int max_hr = 0;
                    //loop through results to see if we need to update health stats
                    while (resultSet.next()) {
                        //get min_weight
                        min_hr = resultSet.getInt("min_heart_rate");
                        //get max_weight
                        max_hr = resultSet.getInt("max_heart_rate");
                        break;
                    }
                    //update health stats if needed
                    if(heart_rate>max_hr){
                        String updateSQL = "UPDATE HealthStatistics SET max_heart_rate = ? WHERE member_id = ?";
                        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                            pstmt.setInt(1, heart_rate);
                            pstmt.setInt(2, currentlyloggedin);
                            pstmt.executeUpdate();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    if(heart_rate<min_hr){
                        String updateSQL = "UPDATE HealthStatistics SET min_heart_rate = ? WHERE member_id = ?";
                        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                            pstmt.setInt(1, heart_rate);
                            pstmt.setInt(2, currentlyloggedin);
                            pstmt.executeUpdate();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    String updateSQL = "UPDATE Members SET curr_heart_rate = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                        pstmt.setInt(1, heart_rate);
                        pstmt.setInt(2, currentlyloggedin);
                        pstmt.executeUpdate();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    memberChoice();
                }catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
    public static void dashboard(){
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
                System.out.println("Ends: " + resultSet3.getString("end_date") + "\t");
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
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
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
            Trainers.getAllTrainers();
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
                    Random rand = new Random();
                    int upper = 100;
                    int random_int = rand.nextInt(upper);
                    pstmt2.setInt(1, random_int);
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

}
