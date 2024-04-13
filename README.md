# COMP3005_ProjectV2
Student name: Chloé Lachance-Soulard
Student ID: 101096672
Video Link: https://youtu.be/TLybyIHhId0?si=7RtE-ZpfMrm5ZOQ1

Setup instructions:

In pgadmin 4, create a new database (right click on databases and select create, name it Project2 and create/populate the table using the A3P1.txt file found in the database_files folder (copy/paste contents of A3P1.txt into query tool))
Open IntelliJ and create a new project
Select maven and click next
Name your project and create it.
Open the pom.xml file and add the dependencies found in the pom.xml dependencies file found in the source_code folder (copy the contents of the pom.xml dependencies file found in the source_code folder and paste it under the properties in the pom.xml file in IntelliJ)
Load the maven changes (should have an m with a blue arrow symbol that shows up on page)
Right click the src->main->java folder in IntelliJ and create a new class called Main.java
Copy the code found in the Main.java file found in the source_code folder and paste it into the class you created
You are now ready to run the application
To see the edits made to the database in pgadmin 4, open the query tool in your A3 databse and copy/paste the query found in the test_query file in the database_files folder Note. Make sure you edit the main.java script lines to reflect the correct URL, username and password for your database
How to run application:

In IntelliJ, run the script. Note: in main function, comment out any of the application functions you do not want to test (getAllStudents(), addStudent(first_name, last_name, email, enrollment_date), updateStudentEmail(student_id, new_email), deleteStudent(student_id)). The function tests are hardcoded so edit the function calls to test out with more values if wanted.
