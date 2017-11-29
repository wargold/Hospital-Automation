package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The doctors GUI, this class handles everything that should be displayed
 * for the doctor and every action the doctor can do.
 *
 * @author War
 * @version 1.0
 */
public class DoctorGUI {
    private GridPane gridPane;
    private DBConnection db;
    private NurseGUI nurseGUI;

    private ComboBox<Patient> patientBox;
    private ComboBox<Medicine> medicineBox1, medicineBox2, medicineBox3;
    private ComboBox<String> medicalPreBox, teamBox;
    private int currentPatientID;

    public DoctorGUI(GridPane gridPane, DBConnection db, NurseGUI nurseGUI) {
        this.gridPane = gridPane;
        this.db = db;
        this.nurseGUI = nurseGUI;
        currentPatientID=0;
    }

    private Label newLabel(String s, Font f) {
        Label l = new Label(s);
        l.setFont(f);
        return l;
    }

    public void createFields() {

        Label doctorsViewTitle = new Label("Doctor Healer");
        doctorsViewTitle.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 24));
        gridPane.add(doctorsViewTitle, 0, 0);

        // cp=Current patient, show info about a patient when doctor choose one.
        GridPane currentpGrid = new GridPane();
        currentpGrid.setHgap(20);
        currentpGrid.setVgap(5);
        Label currentpID = new Label("ID:");
        Label currentpName = new Label("Name:");
        Label currentpAge = new Label("Age:");
        Label currentpGender = new Label("Gender:");
        Label currentpMedicalIssue = new Label("Medical Issue:");
        currentpGrid.add(currentpID, 0, 0);
        currentpGrid.add(currentpName, 1, 0);
        currentpGrid.add(currentpAge, 2, 0);
        currentpGrid.add(currentpGender, 3, 0);
        currentpGrid.add(currentpMedicalIssue, 4, 0);
        Font font = Font.font("Verdana", FontWeight.SEMI_BOLD, 14);
        Label cpId = newLabel("", font);
        Label cpName = newLabel("", font);
        Label cpAge = newLabel("", font);
        Label cpGender = newLabel("", font);
        Label cpMedicalIssue = newLabel("", font);
        currentpGrid.add(cpId, 0, 1);
        currentpGrid.add(cpName, 1, 1);
        currentpGrid.add(cpAge, 2, 1);
        currentpGrid.add(cpGender, 3, 1);
        currentpGrid.add(cpMedicalIssue, 4, 1);
        gridPane.add(currentpGrid, 0, 3, 3, 3);

        // Medicine
        GridPane medicineGrid = new GridPane();
        medicineGrid.setHgap(100);
        medicineGrid.setVgap(2);
        Label medicine1 = new Label("Medicine Name:");
        Label medicine2 = new Label("Medicine Name:");
        Label medicine3 = new Label("Medicine Name:");
        medicineGrid.add(medicine1, 1, 8);
        medicineGrid.add(medicine2, 2, 8);
        medicineGrid.add(medicine3, 3, 8);
        Label medName1 = newLabel("", font);
        Label medName2 = newLabel("", font);
        Label medName3= newLabel("", font);
        medicineGrid.add(medName1,1,9);
        medicineGrid.add(medName2,2,9);
        medicineGrid.add(medName3,3,9);
        gridPane.add(medicineGrid, 1, 8, 3, 3);

        // Choose team
        Label patientTeamLabel = new Label("Team:");
        gridPane.add(patientTeamLabel, 0, 1);

        ObservableList<String> teamOptions = FXCollections.observableArrayList();
        ResultSet rs = db.SQlQuery("SELECT * FROM team");
        try {
            while (rs.next()) {
                teamOptions.add(rs.getString("Name"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Display Doctors team
        teamBox = new ComboBox<>(teamOptions);
        teamBox.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    patientBox.getItems().clear();
                    ResultSet patients = db.SQlQuery("SELECT * FROM patient,team WHERE team.id = patient.team_id AND "
                            + "team.name = '" + newValue + "' AND patient.Wait>=0 ORDER BY Wait DESC, Priority DESC");
                    try {
                        while (patients.next()) {
                            Patient np = new Patient(patients.getInt("id"), db);
                            patientBox.getItems().add(np);
                        }
                        patients.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        gridPane.add(teamBox, 0, 2);



        // Choose Patient from the list and display
        Label patientLabel = new Label("Patient:");
        gridPane.add(patientLabel, 2, 1);

        ObservableList<Patient> patientList = FXCollections.observableArrayList();
        patientBox = new ComboBox<>(patientList);
        patientBox.valueProperty().addListener(new ChangeListener<Patient>() {

            @Override
            public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {
                if (newValue != null) {
                    try {
                        ResultSet choosenPatient = db.SQlQuery("SELECT patient.name as name, patient.age as age,"
                                + " patient.gender as gender, "
                                + " medical_issue.name as medical_issue"
                                + " FROM patient,medical_issue WHERE "
                                + " medical_issue.id=patient.medical_issue_id AND patient.id=" + newValue.getId());
                        while (choosenPatient.next()) {
                            cpId.setText(Integer.toString(newValue.getId()));
                            cpName.setText(choosenPatient.getString("name"));
                            cpAge.setText(choosenPatient.getString("age"));
                            cpGender.setText(choosenPatient.getString("gender"));
                            cpMedicalIssue.setText(choosenPatient.getString("medical_issue"));
                        }
                        currentPatientID=newValue.getId();
                        System.out.println("Doctor choosen patient with ID: "+ currentPatientID);
                        choosenPatient.close();
                        addMedicalProcedure();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gridPane.add(patientBox, 2, 2);



        // Choose Medicine and display

        ObservableList<Medicine> medicineOptions = FXCollections.observableArrayList();
        try {
            ResultSet drugs = db.SQlQuery("SELECT * FROM medicine");
            while (drugs.next()) {
                Medicine d = new Medicine(drugs.getInt("Id"), drugs.getInt("Cost"),drugs.getString("Name"));
                medicineOptions.add(d);
            }
            drugs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        medicineBox1 = new ComboBox<>(medicineOptions);
        medicineBox2 = new ComboBox<>(medicineOptions);
        medicineBox3 = new ComboBox<>(medicineOptions);

        Label medTitel = new Label("Medicine:");
        gridPane.add(medTitel, 1, 6);

        //Display medicine1
        medicineBox1.valueProperty().addListener(new ChangeListener<Medicine>() {

            @Override
            public void changed(ObservableValue<? extends Medicine> observable, Medicine oldValue, Medicine newValue) {
                if (newValue != null) {
                    try {

                        ResultSet med = db.SQlQuery("SELECT * FROM medicine WHERE name= '" + newValue.getName()+"'");
                        while (med.next()) {
                            medName1.setText(med.getString("name"));
                        }
                        med.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gridPane.add(medicineBox1, 1, 7);


        //Display medicine2
        medicineBox2.valueProperty().addListener(new ChangeListener<Medicine>() {
            @Override
            public void changed(ObservableValue<? extends Medicine> observable, Medicine oldValue, Medicine newValue) {
                if (newValue != null) {
                    try {

                        ResultSet med = db.SQlQuery("SELECT * FROM medicine WHERE name= '" + newValue.getName()+"'");
                        while (med.next()) {
                            medName2.setText(med.getString("name"));
                        }
                        med.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gridPane.add(medicineBox2, 2, 7);

        //Display medicine3
        medicineBox3.valueProperty().addListener(new ChangeListener<Medicine>() {
            @Override
            public void changed(ObservableValue<? extends Medicine> observable, Medicine oldValue, Medicine newValue) {
                if (newValue != null) {
                    try {

                        ResultSet med = db.SQlQuery("SELECT * FROM medicine WHERE name= '" + newValue.getName()+"'");
                        while (med.next()) {
                            medName3.setText(med.getString("name"));
                        }
                        med.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gridPane.add(medicineBox3, 3, 7);




        Button sendHome = new Button("Print Record");
        sendHome.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                patientHomeOrStay("Send Home");
            }

        });
        gridPane.add(sendHome, 0, 8);

        Button stayHospital = new Button("Stay At Hospital");
        stayHospital.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                patientHomeOrStay("Stay At Hospital");
            }

        });
        gridPane.add(stayHospital, 0, 9);

        // Go to start screen
        Button goStartScreen = new Button("Go to main menu");
        goStartScreen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                nurseGUI.goStartScreen();
            }

        });
        gridPane.add(goStartScreen, 0, 11);

    }

    private void patientHomeOrStay(String doctorChoice) {
        Patient p = patientBox.valueProperty().get();
        Medicine[] drugs = {medicineBox1.valueProperty().get(),
                medicineBox2.valueProperty().get(),
                medicineBox3.valueProperty().get()};


        int medicalProcedureCost = 0;
        try {
            ResultSet getMedicalPreCost = db.SQlQuery("SELECT * FROM medical_procedure");
            while (getMedicalPreCost.next()){
                if (getMedicalPreCost.getString("name").equals(medicalPreBox.valueProperty().get())){
                    medicalProcedureCost=getMedicalPreCost.getInt("cost");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MedicalRecord medRecord = new MedicalRecord();
        String note = "Medical Procedure:\n" + medicalPreBox.valueProperty().get() + ": " + medicalProcedureCost + " SEK";
        note += "\nUsed Medicine:";
        for (Medicine drug : drugs) {
            if (!(drug == null)) {
                note += "\n" + drug.getName() + ": " + drug.getCost() + " SEK";
                db.insertSql("INSERT INTO Used VALUES(" + drug.getId() + ", " + p.getId() + ")");
            }
        }

        note += "\n\n" + doctorChoice;

        medRecord.newRecord(p, note);

        db.insertSql("INSERT INTO Medical_Record VALUES('" + medRecord.getFullReport() + "')");
        db.insertSql("DELETE FROM Used where patient_id= " + p.getId()); //remove used drugs of patient.
        db.insertSql("DELETE FROM Patient where id= " + p.getId());//remove patient from the db.


        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(doctorChoice);
        alert.setHeaderText(null);
        alert.setContentText(patientBox.valueProperty().get().toString() + ", Saved in medical record.");
        alert.showAndWait();
    }

    /**
     * Display all medical purpose for a illness.
     */
    private void addMedicalProcedure(){
        Label medProcedureTitle = new Label("Medical Procedure:");
        gridPane.add(medProcedureTitle, 0, 6);
        ObservableList<String> medProcedureList = FXCollections.observableArrayList();
        try {
            ResultSet medicalpre = db.SQlQuery("select medical_procedure.name as medproname from medical_issue, patient, medical_procedure where medical_issue.id=patient.Medical_Issue_Id and " +
                    "medical_issue.id=medical_procedure.Medical_Issue_Id and patient.id=" + currentPatientID);
            while (medicalpre.next()) {
                medProcedureList.add(medicalpre.getString("medproname"));
            }
            medicalpre.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        medicalPreBox = new ComboBox<>(medProcedureList);
        gridPane.add(medicalPreBox, 0, 7);
    }
}