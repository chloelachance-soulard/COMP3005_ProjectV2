INSERT INTO Members(member_id, member_password, first_name, last_name, curr_weight, curr_height, curr_heart_rate)
VALUES
(1, 'workouts321', 'Macy', 'Smith', 125, 195, 90),
(2, 'ilovedogs', 'Jojo', 'Woz', 199, 143, 101),
(3, 'fun51', 'James', 'Funguy', 173, 175, 85);

INSERT INTO Trainers(trainer_id, trainer_password, first_name, last_name)
VALUES
(1, 'trainer123', 'John', 'Train'),
(2, 'itrain', 'Amanda', 'Pro'),
(3, 'fitnessisfun', 'Melissa', 'Babybel');

INSERT INTO AdminStaff(admin_id, admin_password, first_name, last_name)
VALUES
(1, 'jonah123', 'Jonah', 'Johnson'),
(2, 'dalpup', 'Dallan', 'Unip');

INSERT INTO HealthStatistics(min_weight, max_weight, min_heart_rate, max_heart_rate, member_id)
VALUES
(120, 130, 80, 100, 1),
(160, 205, 99, 105, 2),
(165, 179, 70, 86, 3);

INSERT INTO FitnessGoal(goal_id, end_date, goal, status, member_id)
VALUES
(1, '2024-07-07', 'Lose 50 lbs',0, 1),
(2, '2025-01-01', 'Be able to do 10 push ups',0, 3),
(3, '2024-04-10','Lose 10 lbs', 1, 1);

INSERT INTO ExerciseRoutine(routine_id, exercise, num_of_reps, member_id)
VALUES
(1, 'Jumping Jacks', 50, 1),
(2, 'Push-ups', 5, 3);

INSERT INTO Bill(bill_id, total_price, num_of_sessions, status, member_id)
VALUES
(1, 50, 2, 0, 1),
(2, 100, 4, 1, 2);

INSERT INTO Rooms(room_id, capacity)
VALUES
(1, 20),
(2, 50),
(3, 3),
(4, 100),
(5, 5);

INSERT INTO GroupClass(class_id, start_time, end_time, class_date, price, trainer_id, room_id)
VALUES
(1, '11:30', '12:30', '2024-04-19', 25, 1, 1),
(2, '10:00', '10:30', '2024-04-20', 10, 2, 3);

INSERT INTO PersonalTraining(session_id, start_time, end_time, session_date, price, trainer_id, member_id, room_id)
VALUES
(1, '11:45', '13:45', '2024-04-13', 100, 2, 1, 3);

INSERT INTO SignedUpFor(class_id, member_id)
VALUES
(1,1),
(1,2),
(1,3),
(2,3);

INSERT INTO Availability(availability_id, start_time, end_time, available_date, price, trainer_id)
VALUES
(1, '10:00', '12:00', '2024-04-23', 50, 1),
(2, '13:00', '15:00', '2024-04-23', 50, 1),
(3, '9:00', '10:00', '2024-04-26', 25, 2),
(4, '17:00', '19:00', '2024-04-25', 75, 3);

INSERT INTO Equipment(equipment_id, equipment_type, last_maintenance_date, room_id)
VALUES
(1, '50 pound dumbbells', '2024-01-01', 1),
(2, '50 pound dumbbells', '2024-01-01', 2),
(3, '50 pound dumbbells', '2024-01-01', 3),
(4, '50 pound dumbbells', '2024-01-01', 4),
(5, '10 pound dumbbells', '2024-01-01', 1),
(6, '10 pound dumbbells', '2024-01-01', 2),
(7, '10 pound dumbbells', '2024-01-01', 3),
(8, 'Squat Rack', '2024-01-01', 5),
(9, 'Squat Rack', '2024-01-01', 2);