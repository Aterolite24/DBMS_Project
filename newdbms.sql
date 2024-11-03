create database project_db;
drop database project_db;
use project_db;

-- done
CREATE TABLE Lawyer (
    LawyerID INT PRIMARY KEY auto_increment,
    FName VARCHAR(50),
    MName VARCHAR(50),
    LName VARCHAR(50),
    DateOfBirth DATE,
    Qualification VARCHAR(100),
    Experience INT,
    Expertise VARCHAR(50),
    Positions VARCHAR(50)
);
-- drop table lawyer;

-- done
CREATE TABLE LawyerPhone (
    LawyerID INT,
    PhoneNumber VARCHAR(20),
    PRIMARY KEY (LawyerID, PhoneNumber),  -- Corrected to use LawyerID instead of LawyerNo
    FOREIGN KEY (LawyerID) REFERENCES Lawyer(LawyerID) on delete cascade
);

-- done
CREATE TABLE LawyerEmail (
    LawyerID INT,
    EmailAddress VARCHAR(100),
    PRIMARY KEY (LawyerID, EmailAddress),  -- Corrected to use LawyerID instead of LawyerNo
    FOREIGN KEY (LawyerID) REFERENCES Lawyer(LawyerID) on delete cascade
);

-- done
CREATE TABLE Paralegal (
    ParalegalID INT PRIMARY KEY auto_increment,
    FName VARCHAR(50),
    MName VARCHAR(50),
    LName VARCHAR(50),
	DateOfBirth DATE,
    Qualification VARCHAR(100),
    Experience INT,
    Positions VARCHAR(50),
    PhoneNumber VARCHAR(20),
    Email VARCHAR(100)
);

-- done
CREATE TABLE ParalegalPhone (
    ParalegalID INT,
    PhoneNumber VARCHAR(20),
    PRIMARY KEY (ParalegalID, PhoneNumber),  -- Corrected to use ParalegalID instead of ParalegalNo
    FOREIGN KEY (ParalegalID) REFERENCES Paralegal(ParalegalID) on delete cascade
);

-- done
CREATE TABLE ParalegalEmail (
    ParalegalID INT,
    EmailAddress VARCHAR(100),
    PRIMARY KEY (ParalegalID, EmailAddress),  -- Corrected to use ParalegalID instead of ParalegalNo
    FOREIGN KEY (ParalegalID) REFERENCES Paralegal(ParalegalID) on delete cascade
);

-- done
CREATE TABLE Client (
    ClientID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    FName VARCHAR(50),
    MName VARCHAR(50),
    LName VARCHAR(50),
    Occupation VARCHAR(100),
    DateOfBirth DATE,
    Spouse VARCHAR(100),
    Address VARCHAR(100),
    Children integer
);

-- done
CREATE TABLE ClientEmail (
    ClientID INT,
    EmailAddress VARCHAR(100),
    PRIMARY KEY (ClientID, EmailAddress),
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID) on delete cascade
);

-- done
CREATE TABLE ClientPhone (
    ClientID INT,
    PhoneNumber VARCHAR(20),
    PRIMARY KEY (ClientID, PhoneNumber),
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID) on delete cascade
);

-- done
CREATE TABLE Category (
CatID INT PRIMARY KEY auto_increment,
CaseType VARCHAR(255)
);

-- done
CREATE TABLE CorporateCase (
    CorporateCaseID INT AUTO_INCREMENT,
    CaseDesc TEXT,
    StartDate VARCHAR(20),
    EndDate VARCHAR(20) default NULL,
    ClientID INT NOT NULL, 
    CaseStatus VARCHAR(50),
    PRIMARY KEY (CorporateCaseID),
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID)
);
-- DROP TABLE CORPORATECASE;

-- done
 CREATE TABLE CriminalCase (
    CriminalCaseID INT PRIMARY KEY auto_increment,
    CaseDesc TEXT,
    StartDate VARCHAR(20),
    ArrestDate VARCHAR(20),
    Appeal VARCHAR(50),
    EndDate VARCHAR(20),
    ClientID INT NOT NULL,
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID)
);

-- done
CREATE TABLE CivilCase (
    CivilCaseID INT PRIMARY KEY auto_increment,
    CaseDesc TEXT,
    StartDate VARCHAR(20),
    EndDate VARCHAR(20),
    Appeal VARCHAR(50),
    ClientID INT NOT NULL,
    Compensation INT,
    Location VARCHAR(50),
    DisputeType VARCHAR(50),
    MediationState VARCHAR(50),
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID)
);

-- done
CREATE TABLE MatrimonialCase (
    MatrimonialCaseID INT PRIMARY KEY auto_increment,
    CaseDesc TEXT,
    StartDate VARCHAR(20),
    EndDate VARCHAR(20),
    ClientID INT NOT NULL,
    MarriedStatus VARCHAR(50),
    DisputeType VARCHAR(50),
    AlimonyStatus VARCHAR(50),
    Children INT,
    CustodyDetail TEXT,
    MarriageDate VARCHAR(50),
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID)
);

-- drop table criminalcase;

-- done
CREATE TABLE Appointment (
    AppointmentID INT PRIMARY KEY AUTO_INCREMENT,
    CatID INT NOT NULL,
    CaseID INT NOT NULL,
    LawyerID INT NOT NULL,
    ClientID INT NOT NULL,
    AppointmentDate DATE,
    AppointmentTime TIME,
    Location VARCHAR(50),
    FOREIGN KEY (CatID) REFERENCES Category(CatID) ON DELETE CASCADE,
    FOREIGN KEY (LawyerID) REFERENCES Lawyer(LawyerID) ON DELETE CASCADE,
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID) ON DELETE CASCADE
);
-- drop table appointment;

-- done
CREATE TABLE Task (
    TaskID INT not null auto_increment,
    CatID INT not null,
    CaseID INT not null,
    TaskDesc TEXT,
    Status VARCHAR(50),
    DueDate DATE,
    PRIMARY KEY (TaskID,CatID,CaseID),
    FOREIGN KEY (CatID) REFERENCES Category(CatID) ON DELETE CASCADE
);

-- done
-- CREATE TABLE Taskassigned (
--     TaskID INT not null,
--     EmployeeType VARCHAR(50),
--     EmpID INT,
--     PRIMARY KEY (TaskID,EmployeeType,EmpID),
--     FOREIGN KEY (TaskID) REFERENCES Task(TaskID) ON DELETE CASCADE
-- );

CREATE TABLE Tasklawassigned (
    TaskID INT not null,
    EmpID INT,
    PRIMARY KEY (TaskID,EmpID),
    FOREIGN KEY (TaskID) REFERENCES Task(TaskID) ON DELETE CASCADE
);

CREATE TABLE Taskparassigned(
	TaskID INT not null,
    EmpID INT,
    PRIMARY KEY (TaskID,EmpID),
    FOREIGN KEY (TaskID) REFERENCES Task(TaskID) ON DELETE CASCADE
);

-- drop table taskassigned;


-- done
CREATE TABLE Invoice (
    InvoiceID INT PRIMARY KEY AUTO_INCREMENT,
    CaseID INT NOT NULL,
    CatID INT NOT NULL,
    InvoiceDate DATE,
    Amount DECIMAL(10, 2) NOT NULL,
    DueDate DATE,
    Status VARCHAR(50),
    FOREIGN KEY (CatID) REFERENCES Category(CatID) on delete cascade
);

CREATE TABLE CaseNotes (
    CaseNoteID INT auto_increment ,
    NoteText TEXT,
    Relevance VARCHAR(50),
    DateCreated DATE,
    DateModified DATE,
    CaseID INT NOT NULL,
    CatID INT NOT NULL,
    LawyerID INT NOT NULL,
    PRIMARY KEY (CaseNoteID, CaseID,CatID),
    FOREIGN KEY (LawyerID) REFERENCES Lawyer(LawyerID) on delete cascade,
    foreign key (CatID) REFERENCES Category(CatID)  on delete cascade
);
-- drop table casenotes; 

CREATE TABLE WitAndEvi (
    EviID INT PRIMARY KEY auto_increment,
    ProofType VARCHAR(100),
    PhoneNo VARCHAR(20), 
    witName VARCHAR(255),
    EvidenceFilePath TEXT,
    WitnessText TEXT
);
-- drop table witAndevi;
-- drop table casewit;

CREATE TABLE CaseWit(
	EviID INT NOT NULL,
	 CaseID INT NOT NULL,
    CatID INT NOT NULL,
    PRIMARY KEY (EviID,CaseID,CatID),
     foreign key (CatID) REFERENCES Category(CatID) on delete cascade,
     FOREIGN KEY (EviID) references WitAndEvi(EviID) on delete cascade
);

-- done
CREATE TABLE Court (
    CourtID INT PRIMARY KEY AUTO_INCREMENT,
    CourtName VARCHAR(255),
    CourtPincode VARCHAR(20),
    CourtState VARCHAR(100),
    CourtCity VARCHAR(100),
    jFName VARCHAR(50),
    jMName VARCHAR(50),
    jLName VARCHAR(50)
);

-- done
CREATE TABLE CaseCourt(
CourtID INT NOT NULL,
CaseID INT NOT NULL,
CatID INT NOT NULL,
PRIMARY KEY(CourtID,CaseID,CatID),
foreign key (CatID) REFERENCES Category(CatID) on delete cascade,
foreign key (CourtID) references Court(CourtID) on delete cascade
);