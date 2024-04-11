import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AdminStaff {
    static int currentlyloggedin;
    static Connection connection;
    static String url = "jdbc:postgresql://localhost:5432/Project";
    static String user = "postgres";
    static String password = "admin";

    public static void adminLogin(){
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
                System.out.print("WELCOME " + admin_name);
                adminOptions();
            }
            else{
                System.out.print("password is incorrect, goodbye");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void adminOptions(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("\n Please select what you would like to do: \n 1. Manage Room Bookings \n 2. Monitor Equipment Maintenance \n 3. Update Class Schedule \n 4. Billing and Payment Processing");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1){
            manageBookings();
        }
        if(choice == 2) {
            monitorEquipment();
        }
        if(choice == 3) {
            updateClass();
        }
        if(choice == 4) {
            processBill();
        }
    }

    public static void manageBookings(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Please select what you would like to update: \n 1.Personal Training Room \n 2. Group Class Room");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1){
            System.out.println("Here are all of the personal training session: ");
            try {
                Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM PersonalTraining");
            ResultSet resultSet7 = statement.getResultSet();
            System.out.println("PERSONAL TRAINING SESSIONS ");
            System.out.println("--------------------------------------------------");
            while (resultSet7.next()) {
                System.out.println("Session ID: " + resultSet7.getInt("session_id") + "\t");
                System.out.println("Room: " + resultSet7.getInt("room_id") + "\t");
                System.out.println("Trainer ID: " + resultSet7.getInt("trainer_id") + "\t");
                System.out.println("Member ID: " + resultSet7.getInt("member_id") + "\t");
                System.out.println("Date: " + resultSet7.getString("session_date") + "\t");
                System.out.println("Starting Time: " + resultSet7.getString("start_time") + "\t");
                System.out.println("End Time: " + resultSet7.getString("end_time") + "\t");
                System.out.println("Price: " + resultSet7.getInt("price") + "$" + "\t");
                System.out.println("------------");
            }

            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Please enter the id of the session you would like to move to a different room: ");
            int selection = Integer.parseInt(scannerObj.nextLine());
            System.out.println("Please enter the id of the room you would like to move it to: ");
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM Rooms");
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    System.out.println("Room ID: " + resultSet.getInt("room_id") + "\t");
                    System.out.println("Capacity: " + resultSet.getInt("capacity") + "\t");
                    System.out.println("------------------");
                }
                int roomid = Integer.parseInt(scannerObj.nextLine());
                String updateSQL = "UPDATE PersonalTraining SET room_id = ? WHERE session_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, roomid);
                    pstmt.setInt(2, selection);
                     pstmt.executeUpdate();
                    System.out.println("Update complete");
                    adminOptions();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if(choice == 2) {
            System.out.println("Here are all of the classes: ");
            try {
                Statement statement = connection.createStatement();
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

            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Please enter the id of the class you would like to move to a different room: ");
            int selection = Integer.parseInt(scannerObj.nextLine());
            System.out.println("Please enter the id of the room you would like to move it to: ");
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM Rooms");
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    System.out.println("Room ID: " + resultSet.getInt("room_id") + "\t");
                    System.out.println("Capacity: " + resultSet.getInt("capacity") + "\t");
                    System.out.println("------------------");
                }
                int roomid = Integer.parseInt(scannerObj.nextLine());
                String updateSQL = "UPDATE GroupClass SET room_id = ? WHERE class_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, roomid);
                    pstmt.setInt(2, selection);
                    pstmt.executeUpdate();
                    System.out.println("Update complete");
                    adminOptions();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    public static void monitorEquipment(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Here is all of the equipment information: ");
        System.out.println("Please select what you would like to do: \n 1. Update maintenance date \n 2. Just checking to see when things were maintained!");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1){
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM Equipment");
                ResultSet resultSet = statement.getResultSet();
                //go through results and display all of the equipment information
                while (resultSet.next()) {
                    System.out.print(resultSet.getInt("equipment_id") + "\t");
                    System.out.print(resultSet.getString("equipment_type") + "\t");
                    System.out.print(resultSet.getDate("last_maintenance_date") + "\t");
                    System.out.println(resultSet.getInt("room_id") + "\t");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Please enter the id of the equipment that has been maintenanced: ");
            int id = Integer.parseInt(scannerObj.nextLine());
            System.out.println("Please enter the the date that maintenance happened: ");
            Date date = Date.valueOf(scannerObj.nextLine());
            String updateSQL = "UPDATE Equipment SET last_maintenance_date = ? WHERE equipment_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setDate(1, date);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
                System.out.println("Update complete");
                adminOptions();
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        if(choice == 2){
            try {
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM Equipment");
                ResultSet resultSet = statement.getResultSet();
                //go through results and display all of the equipment information
                while (resultSet.next()) {
                    System.out.print(resultSet.getInt("equipment_id") + "\t");
                    System.out.print(resultSet.getString("equipment_type") + "\t");
                    System.out.print(resultSet.getDate("last_maintenance_date") + "\t");
                    System.out.println(resultSet.getInt("room_id") + "\t");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            adminOptions();
        }

    }
    public static void processBill() {
        try {
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Please select what you would like to do: \n 1. Create bill \n 2. Cancel bill \n 3. Update bill");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if (choice == 1) {
            System.out.println("Please select what you would like to do: \n 1. Collect membership fees \n 2. Collect money for bookings");
            int choice2 = Integer.parseInt(scannerObj.nextLine());
            if (choice2 == 1) {
                try {
                    Statement statement = connection.createStatement();
                    statement.executeQuery("SELECT * FROM Members");
                    ResultSet chosen = statement.getResultSet();
                    System.out.println("----------------------------------");
                    while (chosen.next()) {
                        System.out.println("Member ID: " + chosen.getInt("member_id") + "\t");
                        System.out.println("First name: " + chosen.getString("first_name") + "\t");
                        System.out.println("Last name: " + chosen.getString("last_name") + "\t");
                        System.out.println("------------");
                    }
                    System.out.println("Please select the member you would like to collect fees from: ");
                    int selected = Integer.parseInt(scannerObj.nextLine());
                    System.out.println("Creating membership fees bill...");
                    String insertSQL2 = "INSERT INTO Bill(bill_id, total_price, num_of_sessions, status, member_id) VALUES (?,?,?,?,?)";
                    try (PreparedStatement pstmt2 = connection.prepareStatement(insertSQL2)) {
                        Random rand = new Random();
                        int upper = 100;
                        int random_int = rand.nextInt(upper);
                        pstmt2.setInt(1, random_int);
                        pstmt2.setInt(2, 100);
                        pstmt2.setInt(3, 0);
                        pstmt2.setInt(4, 0);
                        pstmt2.setInt(5, selected);
                        pstmt2.executeUpdate();
                        System.out.println("Bill created");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                adminOptions();
            }
            if (choice2 == 2) {
                try {
                    Statement statement = connection.createStatement();
                    statement.executeQuery("SELECT * FROM Members");
                    ResultSet chosen = statement.getResultSet();
                    System.out.println("----------------------------------");
                    while (chosen.next()) {
                        System.out.println("Member ID: " + chosen.getInt("member_id") + "\t");
                        System.out.println("First name: " + chosen.getString("first_name") + "\t");
                        System.out.println("Last name: " + chosen.getString("last_name") + "\t");
                        System.out.println("------------");
                    }
                    System.out.println("Please select the member you would like to create a bill for : ");
                    int selected = Integer.parseInt(scannerObj.nextLine());
                    int num_booked = 0;
                    int tot_price = 0;
                    statement.executeQuery("SELECT * FROM PersonalTraining");
                    ResultSet pt_sessions = statement.getResultSet();
                    while(pt_sessions.next()){
                        if(pt_sessions.getInt("member_id") == selected){
                            tot_price += pt_sessions.getInt("price");
                            num_booked +=1;
                        }
                    }
                    statement.executeQuery("SELECT * FROM SignedUpFor WHERE member_id =" + selected + " ");
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
                            num_booked+=1;
                            tot_price +=resultSet.getInt("price");
                        }
                    }
                    String insertSQL2 = "INSERT INTO Bill(bill_id, total_price, num_of_sessions, status, member_id) VALUES (?,?,?,?,?)";
                    try (PreparedStatement pstmt2 = connection.prepareStatement(insertSQL2)) {
                        Random rand = new Random();
                        int upper = 100;
                        int random_int = rand.nextInt(upper);
                        pstmt2.setInt(1, random_int);
                        pstmt2.setInt(2, tot_price);
                        pstmt2.setInt(3, num_booked);
                        pstmt2.setInt(4, 0);
                        pstmt2.setInt(5, selected);
                        pstmt2.executeUpdate();
                        System.out.println("Bill created");
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
                adminOptions();
            }
        }
            if (choice == 2) {
                try {
                    Statement statement = connection.createStatement();
                    System.out.println("Here are all of the bills: ");
                    statement.executeQuery("SELECT * FROM Bill");
                    ResultSet chosen = statement.getResultSet();
                    System.out.println("----------------------------------");
                    while (chosen.next()) {
                        System.out.println("Bill ID: " + chosen.getInt("bill_id") + "\t");
                        System.out.println("Member ID: " + chosen.getInt("member_id") + "\t");
                        System.out.println("Total price: " + chosen.getInt("total_price") + "\t");
                        System.out.println("Number of sessions: " + chosen.getString("num_of_sessions") + "\t");
                        System.out.println("Status: " + chosen.getInt("status") + "\t");
                        System.out.println("------------");
                    }
                    System.out.println("Please select the bill you would like to cancel: ");
                    int selected = Integer.parseInt(scannerObj.nextLine());
                    String deleteSQL = "DELETE FROM Bill WHERE bill_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
                        pstmt.setInt(1, selected);
                        pstmt.executeUpdate();
                        System.out.println("bill deleted");
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }catch (Exception e) {
                    System.out.println(e);
                }
                adminOptions();
            }
            if (choice == 3) {
                try {
                    Statement statement = connection.createStatement();
                    System.out.println("Here are all of the bills: ");
                    statement.executeQuery("SELECT * FROM Bill");
                    ResultSet chosen = statement.getResultSet();
                    System.out.println("----------------------------------");
                    while (chosen.next()) {
                        System.out.println("Bill ID: " + chosen.getInt("bill_id") + "\t");
                        System.out.println("Member ID: " + chosen.getInt("member_id") + "\t");
                        System.out.println("Total price: " + chosen.getInt("total_price") + "\t");
                        System.out.println("Number of sessions: " + chosen.getString("num_of_sessions") + "\t");
                        System.out.println("Status: " + chosen.getInt("status") + "\t");
                        System.out.println("------------");
                    }
                    System.out.println("Please select the bill that has been payed to update its status: ");
                    int selected = Integer.parseInt(scannerObj.nextLine());
                    String updateSQL = "UPDATE Bill SET status = ? WHERE bill_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                        pstmt.setInt(1, 1);
                        pstmt.setInt(2, selected);
                        pstmt.executeUpdate();
                        System.out.println("Update complete");
                    }catch (Exception e) {
                        System.out.println(e);
                    }

                }catch (Exception e) {
                    System.out.println(e);
                }
                adminOptions();

            }
    }

    public static void updateClass(){
        try{
            Class.forName("org.postgresql.Driver");
            //initialize connection object
            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e) {
            System.out.println(e);
        }
        Scanner scannerObj = new Scanner(System.in);
        System.out.println("Here are all of the classes: ");
        try {
            Statement statement = connection.createStatement();
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

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Please select the class you would like to update");
        int classid = Integer.parseInt(scannerObj.nextLine());
        System.out.println("Please select what you would like to do: \n 1. Update date \n 2. Update start time \n 3. Update end time \n 4. Update instructor \n 5. Update price");
        int choice = Integer.parseInt(scannerObj.nextLine());
        if(choice == 1){
            System.out.println("Please enter the new date: ");
            Date date = Date.valueOf(scannerObj.nextLine());
            String updateSQL = "UPDATE GroupClass SET class_date = ? WHERE class_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setDate(1, date);
                pstmt.setInt(2, classid);
                pstmt.executeUpdate();
                System.out.println("Update complete");
                adminOptions();
            }catch (Exception e) {
                System.out.println(e);
            }
        }
        if(choice == 2) {
            System.out.println("Please enter the new start time: ");
            Time stime = Time.valueOf(scannerObj.nextLine()+":00");
            String updateSQL = "UPDATE GroupClass SET start_time = ? WHERE class_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setTime(1, stime);
                pstmt.setInt(2, classid);
                pstmt.executeUpdate();
                System.out.println("Update complete");
                adminOptions();
            }catch (Exception e) {
                System.out.println(e);
            }
        }
        if(choice == 3) {
            System.out.println("Please enter the new end time: ");
            Time etime = Time.valueOf(scannerObj.nextLine()+":00");
            String updateSQL = "UPDATE GroupClass SET end_time = ? WHERE class_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setTime(1, etime);
                pstmt.setInt(2, classid);
                pstmt.executeUpdate();
                System.out.println("Update complete");
                adminOptions();
            }catch (Exception e) {
                System.out.println(e);
            }

        }
        if(choice == 4) {
            Trainers.getAllTrainers();
            System.out.println("Please enter the id of the new trainer: ");
            int id = Integer.parseInt(scannerObj.nextLine());
            String updateSQL = "UPDATE GroupClass SET trainer_id = ? WHERE class_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setInt(1, id);
                pstmt.setInt(2, classid);
                pstmt.executeUpdate();
                System.out.println("Update complete");
                adminOptions();
            }catch (Exception e) {
                System.out.println(e);
            }
        }
        if(choice == 5) {
            Trainers.getAllTrainers();
            System.out.println("Please enter the new price: ");
            int price = Integer.parseInt(scannerObj.nextLine());
            String updateSQL = "UPDATE GroupClass SET trainer_id = ? WHERE class_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setInt(1, price);
                pstmt.setInt(2, classid);
                pstmt.executeUpdate();
                System.out.println("Update complete");
                adminOptions();
            }catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}



