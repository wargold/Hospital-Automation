package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * PatientGUI is a class for connecting to a database by using the JDBC API.
 * This class contains methods for mainting the database e.g retrieving and adding
 * values to the tables in the database.
 *
 * @author War
 * @version 1.0
 */
public class PatientGUI {
    private GridPane gridPane;
    private GridPane gridQueue;
    private GridPane gridAddPatient;
    private DBConnection db;
    private NurseGUI nurseGUI;
    private Button addPatientButton;
    private TextField idField;
    private TextField nameField;
    private TextField ageField;
    private ComboBox priorityBox;
    private ComboBox genderBox;
    private ComboBox<String> medicalIssueBox;
    private ArrayList<String> medicalIssue;
    private int patientAge;
    private int patientPriority;
    private String patientName;
    private String patientGender;
    private int patientID;
    private String patientmedIssueValue;
    private ArrayList<Integer> allPatientsIDInDB;
    private Random randomGenerator;
    private int teamIDSpecialization;


    public PatientGUI(GridPane gridPane, DBConnection db, NurseGUI nurseGUI) {
        this.gridPane = gridPane;
        this.db = db;
        gridQueue = new GridPane();
        gridAddPatient = new GridPane();
        addPatientButton = new Button("Add Patient");
        this.nurseGUI = nurseGUI;
        medicalIssue = new ArrayList<>();
        randomGenerator = new Random();
    }

    /**
     * Alert method that alerts the user if they forgot to fill up an
     * necessary text field or forgot to click an option in a drop down menu.
     */
    private void alertBox() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Oops! Something went wrong");
        alert.setContentText("You have forgotten to fill up an important field!");
        alert.showAndWait();
    }

    private void alertAddedPatient() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("You've added a patient to the System.");
        alert.setHeaderText("Information Alert");
        alert.setContentText("Patient with ID: " + patientID + " is added to the System.");
        alert.showAndWait();
    }


    private void alertExistingPatient() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Information Alert");
        alert.setContentText("Patient with ID: " + patientID + " already exist in the System.");
        alert.showAndWait();
    }

    /**
     * Alert method that alerts the user if they forgot to fill up an
     * necessary text field or forgot to click an option in a drop down menu.
     */
    private void alertMedicalIssue() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Oops! Something went wrong");
        alert.setContentText("You have forgotten to choose a medical issue!");
        alert.showAndWait();
    }

    private void createFields() {
        // Creating Text that informs the nurse what to fill up.
        Text addPatientTitle = new Text("Add Patient");
        addPatientTitle.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 24));
        gridPane.add(addPatientTitle, 0, 0, 2, 1);

        Label idLabel = new Label("ID");
        gridPane.add(idLabel, 0, 1);
        idField = new TextField();
        gridPane.add(idField, 0, 2);

        Label nameLabel = new Label("Name:");
        gridPane.add(nameLabel, 1, 1);
        nameField = new TextField();
        gridPane.add(nameField, 1, 2);

        Label ageLabel = new Label("Age:");
        gridPane.add(ageLabel, 2, 1);
        ageField = new TextField();
        gridPane.add(ageField, 2, 2);

        Label priorityLabel = new Label("Priority:");
        gridPane.add(priorityLabel, 3, 1);
        //Create a choice box for priority
        ObservableList<String> priorityList = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        priorityBox = new ComboBox(priorityList);
        gridPane.add(priorityBox, 3, 2);

        Label genderLabel = new Label("Gender:");
        gridPane.add(genderLabel, 4, 1);
        //Create a choice box for gender
        ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female", "Other");
        genderBox = new ComboBox(genderList);
        gridPane.add(genderBox, 4, 2);

        Label medicalIssueLabel = new Label("Medical Issue:");
        gridPane.add(medicalIssueLabel, 5, 1);
        //Create a choice box for medical issue and all issue fetched from the database.
        ObservableList<String> medicalIssueList = FXCollections.observableArrayList();
        try {
            ResultSet rs = db.SQlQuery("SELECT * FROM Medical_Issue");
            while (rs.next()) {
                medicalIssueList.add(rs.getString(2));//Get  the name
                medicalIssue.add(rs.getString(2));
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        medicalIssueBox = new ComboBox<>(medicalIssueList);
        gridPane.add(medicalIssueBox, 5, 2);
    }

    private Boolean checkMedicalIssueField() {
        patientmedIssueValue = medicalIssueBox.getValue(); // Get value from the medical issue box
        if (patientmedIssueValue == null) {
            alertMedicalIssue();
            return true;
        }
        return false;
    }

    /**
     * Build the patient GUI
     */
    public void startPatientGUI() {

        createFields();
        addPatient();


        // Get Patient Queue for different
        Button addQueueButton = new Button("Get Queue");
        addQueueButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ObservableList<String> medicalTeamList = FXCollections.observableArrayList();
                gridQueue.getChildren().clear();


                if (checkMedicalIssueField()) {
                    return;
                }

                try {
                    ResultSet teams = db.SQlQuery("SELECT * FROM team, specialization, medical_issue "
                            + "WHERE team.id = specialization.team_id and specialization.medical_issue_id = medical_issue.id "
                            + "and medical_issue.id ="+medicalIssue.indexOf(patientmedIssueValue));

                    for (int team = 0; teams.next(); team++) { //Get the team name and the patient they can take care of.
                        medicalTeamList.add(teams.getString("Name"));
                        ScrollPane patientScroll = new ScrollPane();
                        patientScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
                        patientScroll.setPrefHeight(250);
                        GridPane teamGridPane = new GridPane();

                        teamGridPane.setAlignment(Pos.TOP_LEFT);
                        teamGridPane.setHgap(10);
                        teamGridPane.setVgap(10);
                        teamGridPane.setPadding(new Insets(25, 25, 25, 25));

                        //Add info about patient in the scroll menu
                        Label pID = new Label("PatientID:");
                        Label pName = new Label("Name:");
                        Label pAge = new Label("Age:");
                        Label pPriority = new Label("Priority");
                        Label pwait = new Label("Waiting Time");
                        teamGridPane.add(pID, 0, 0);
                        teamGridPane.add(pName, 1, 0);
                        teamGridPane.add(pAge, 2, 0);
                        teamGridPane.add(pPriority, 3, 0);
                        teamGridPane.add(pwait, 4, 0);

                        /**
                         * Get the patient that a team can take care of and order them according to first their
                         * priority then their waiting time.
                         */
                        ResultSet patientInTeamQueue = db.SQlQuery("SELECT * FROM patient WHERE team_id=" +
                                teams.getString("Id") + " ORDER BY wait DESC, priority DESC");
                        for (int i = 1; patientInTeamQueue.next(); i++) {// Display the patients.
                            Label idLabel = new Label(patientInTeamQueue.getString("Id"));
                            Label nameLabel = new Label(patientInTeamQueue.getString("Name"));
                            Label ageLabel = new Label(patientInTeamQueue.getString("Age"));
                            Label priorityLabel = new Label(patientInTeamQueue.getString("Priority"));
                            Label waitingLabel = new Label(patientInTeamQueue.getString("Wait"));
                            teamGridPane.add(idLabel, 0, i);
                            teamGridPane.add(nameLabel, 1, i);
                            teamGridPane.add(ageLabel, 2, i);
                            teamGridPane.add(priorityLabel, 3, i);
                            teamGridPane.add(waitingLabel, 4, i);
                        }
                        patientScroll.setContent(teamGridPane);//Put the patients inside the scroll.

                        Label teamName = new Label(teams.getString("Name"));
                        gridQueue.add(teamName, team, 6);
                        gridQueue.add(patientScroll, 0 + (team), 7);
                    }

                    Label medicalTeamLabel = new Label("Choose Team:");
                    gridQueue.add(medicalTeamLabel, 0, 10);
                    ComboBox<String> medicalTeamBox = new ComboBox<>(medicalTeamList);
                    gridQueue.add(medicalTeamBox, 0, 11);

                    /**
                     * Add patient to the databas if not already in the database.
                     * Check that all fields have values before assigned patients
                     * to a team.
                     */
                    Button addPatientToTeamButton = new Button("Add Patient into a selected Team ");
                    addPatientToTeamButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            if (medicalTeamBox.getValue() == null) {//"Choose team" field must have a value.
                                alertBox();
                                return;
                            }
                            if (checkFields()) {
                                return;
                            }
                            System.out.println("1");
                            System.out.println("2");
                            System.out.println("3");
                            System.out.println("4");
                            System.out.println("TeamName: " +medicalTeamBox.getValue());
                            int teamID=0;
                            try {ResultSet teamsID = db.SQlQuery("SELECT id FROM team WHERE name=" +" '"+medicalTeamBox.getValue()+"'");
                                while (teamsID.next()) {
                                    teamID = teamsID.getInt(1);
                                }
                            }catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }

                            try {
                                ResultSet waitResult = db.SQlQuery("SELECT * FROM patient, team where patient.Team_Id=team.id and team.id=" + teamID
                                        +" ORDER BY patient.wait DESC, patient.priority DESC");
                                int positionInQue=0;
                                while (waitResult.next()){
                                positionInQue++;
                                }
                                System.out.println("Antalet före i kön: " + positionInQue);
                                db.insertSql("UPDATE patient SET Wait="+(10*patientPriority)*positionInQue+",team_id= " + teamID + " WHERE patient.id="+patientID);
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }

                            gridQueue.getChildren().clear();
                        }

                    });

                    gridQueue.add(addPatientToTeamButton, 0, 12);


                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        gridPane.add(addQueueButton, 0, 3);
        gridPane.add(gridQueue, 0, 6, 5, 1);


        // Go to start screen
        Button goStartScreen = new Button("Go to main menu");
        goStartScreen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                nurseGUI.goStartScreen();
            }

        });
        gridPane.add(goStartScreen, 0, 12);
    }

    /**
     * Checks if all the fields are proparly field up with values.
     *
     * @return True if an empty field exist otherwise false.
     */
    private Boolean checkFields() {
        String insertPatientID = idField.getCharacters().toString();
        String getAge = ageField.getCharacters().toString();
        patientName = nameField.getCharacters().toString();
        checkMedicalIssueField();
        if (insertPatientID == "") {
            alertBox();
            return true;
        }

        if (patientName.equals("")) {//"name" field must have a value.
            alertBox();
            return true;
        }

        if (getAge.equals("")) {//"Age" field must have a value.
            alertBox();
            return true;
        }

        if (priorityBox.getValue() == null) {//"Priority" field must have a value.
            alertBox();
            return true;
        }

        if (ageField.getCharacters().toString().equals("")) {//"Age" field must have a value.
            alertBox();
            return true;
        }
        if (genderBox.getValue() == null) {//"Gender" field must hava a value.
            alertBox();
            return true;
        }

        patientID = Integer.parseInt(insertPatientID.replaceAll("\\s", ""));
        patientAge = Integer.parseInt(getAge.replaceAll("\\s", ""));//cast string to integer
        patientPriority = Integer.parseInt(priorityBox.getValue().toString());
        patientGender = genderBox.getValue().toString();
        return false;
    }

    /**
     * Add action to "Add Patient" button, check if patient already in db otherwise
     * add patient
     */
    public void addPatient() {
        // Get Patient Queue for different
        addPatientButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                gridAddPatient.getChildren().clear();
                if (checkFields()) {
                    return;
                }
                getAllPatientsIDInDB();
                if (!allPatientsIDInDB.contains(patientID)) {
                    db.insertSql("INSERT INTO patient(id,name,age,gender,priority,wait,medical_issue_id) VALUES(" + patientID + ", '" + patientName + "', " + patientAge + ", '"
                            + patientGender + "', " + patientPriority + ", " + 0 + ", " + medicalIssue.indexOf(patientmedIssueValue) + ")");//TODO välj team från choose team ruta
                    System.out.println("med_issue_id: " + medicalIssue.indexOf(patientmedIssueValue));
                    System.out.println("ADDED PAtient with id: " + patientID + ", And name: " + patientName);
                    alertAddedPatient();
                } else {
                    System.out.println("ERROR: Could not load patient...");
                    alertExistingPatient();
                }
            }
        });
        gridPane.add(addPatientButton, 2, 3);
        gridPane.add(gridAddPatient, 2, 6, 5, 1);
    }


    /**
     * Adds patientID to arraylist that will or contains all patientsID in database.
     */
    private void getAllPatientsIDInDB() {
        ResultSet result = db.SQlQuery("SELECT id FROM patient");
        allPatientsIDInDB = new ArrayList<>();
        try {
            while (result.next()) {
                if (!allPatientsIDInDB.contains(patientID)) {//If patient id not already in arraylist add.
                    allPatientsIDInDB.add(result.getInt(1));
                    System.out.println(result.getInt(1));
                }
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Add all teamID that can threat a medical illnes and pick the random team to threat that patient.
     *
     * @return teamID.
     */
    private void getTeamIDSpecialization() {
        ResultSet result = db.SQlQuery("select team_id from specialization where medical_issue_id=" + medicalIssue.indexOf(patientmedIssueValue));
        ArrayList<Integer> teamID = new ArrayList<>();
        try {
            while (result.next()) {
                teamID.add(result.getInt(1));
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        teamIDSpecialization=teamID.get(randomGenerator.nextInt(teamID.size()));
    }
}
