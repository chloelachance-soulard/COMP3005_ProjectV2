CREATE TABLE Members (
	member_id INTEGER PRIMARY KEY,
	member_password VARCHAR(255) NOT NULL,
	DOB DATE,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	home_num INTEGER,
	street VARCHAR(255),
	pcode VARCHAR(255),
	curr_weight INTEGER NOT NULL,
	curr_height INTEGER NOT NULL,
	curr_heart_rate INTEGER NOT NULL
);

CREATE TABLE Trainers (
	trainer_id INTEGER PRIMARY KEY,
	trainer_password VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL
);

CREATE TABLE AdminStaff(
	admin_id INTEGER PRIMARY KEY,
	admin_password VARCHAR(255) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL
);

CREATE TABLE HealthStatistics(
	min_weight INTEGER NOT NULL,
	max_weight INTEGER NOT NULL,
	min_heart_rate INTEGER NOT NULL,
	max_heart_rate INTEGER NOT NULL,
	member_id INTEGER REFERENCES Members(member_id)
);

--status INTEGER: 0 = in progress, 1 = completed
CREATE TABLE FitnessGoal(
	goal_id INTEGER PRIMARY KEY,
	start_date DATE DEFAULT CURRENT_DATE,
	end_date DATE NOT NULL,
	goal TEXT NOT NULL,
	status INTEGER NOT NULL,
	member_id INTEGER REFERENCES Members(member_id)
);

CREATE TABLE ExerciseRoutine(
	routine_id INTEGER PRIMARY KEY,
	exercise VARCHAR(255) NOT NULL,
	num_of_reps INTEGER NOT NULL,
	member_id INTEGER REFERENCES Members(member_id)
);

--status INTEGER: 0 = in progress, 1 = completed
CREATE TABLE Bill(
	bill_id INTEGER PRIMARY KEY,
	total_price INTEGER NOT NULL,
	num_of_sessions INTEGER NOT NULL,
	status INTEGER NOT NULL,
	member_id INTEGER REFERENCES Members(member_id)
);

CREATE TABLE Rooms(
	room_id INTEGER PRIMARY KEY,
	capacity INTEGER NOT NULL
);

CREATE TABLE GroupClass(
	class_id INTEGER PRIMARY KEY,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	class_date DATE NOT NULL,
	price INTEGER NOT NULL,
	trainer_id INTEGER REFERENCES Trainers(trainer_id),
	room_id INTEGER REFERENCES Rooms(room_id)
);

CREATE TABLE PersonalTraining(
	session_id INTEGER PRIMARY KEY,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	session_date DATE NOT NULL,
	price INTEGER NOT NULL,
	trainer_id INTEGER REFERENCES Trainers(trainer_id),
	member_id INTEGER REFERENCES Members(member_id),
	room_id INTEGER REFERENCES Rooms(room_id)
);

CREATE TABLE SignedUpFor(
	class_id INTEGER REFERENCES GroupClass(class_id),
	member_id INTEGER REFERENCES Members(member_id)
);

CREATE TABLE Availability(
	availability_id INTEGER UNIQUE NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL,
	available_date DATE NOT NULL,
	price INTEGER NOT NULL,
	trainer_id INTEGER REFERENCES Trainers(trainer_id)
);

CREATE TABLE Equipment(
	equipment_id INTEGER PRIMARY KEY,
	equipment_type VARCHAR(255) NOT NULL,
	last_maintenance_date DATE NOT NULL,
	room_id INTEGER REFERENCES Rooms(room_id)
);