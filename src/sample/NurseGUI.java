package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * NurseGUI is a class for starting the GUI of this program.
 *
 * @author War
 * @version 1.0
 */
public class NurseGUI extends Application {

    private Stage primaryStage;
    private Scene startScene;
    private DBConnection db;
    private Scene patientScene;
    private Scene doctorScene;
    private GridPane gridpaneStart;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    public void goStartScreen() {
        primaryStage.setScene(startScene);
    }

    /**
     * Add start screen
     */
    public void startingScreen() {
        gridpaneStart = new GridPane();
        gridpaneStart.setAlignment(Pos.CENTER);
        gridpaneStart.setHgap(10);
        gridpaneStart.setVgap(10);
        gridpaneStart.setPadding(new Insets(25, 25, 25, 25));
        scene = new Scene(gridpaneStart, 1280, 720);
        this.startScene = scene;
    }

    /**
     * Add patient GUI.
     */
    public void patientGUI() {
        GridPane patientGrid = new GridPane();
        patientGrid.setAlignment(Pos.CENTER);
        patientGrid.setHgap(10);
        patientGrid.setVgap(10);
        patientGrid.setPadding(new Insets(25, 25, 25, 25));
        PatientGUI apv = new PatientGUI(patientGrid, db, this);
        apv.startPatientGUI();
        patientScene = new Scene(patientGrid, 1280, 720);
    }

    /**
     * Add patient GUI.
     */
    public void doctorGUI() {
        GridPane doctorGrid = new GridPane();
        doctorGrid.setAlignment(Pos.CENTER);
        doctorGrid.setHgap(10);
        doctorGrid.setVgap(10);
        doctorGrid.setPadding(new Insets(25, 25, 25, 25));
        DoctorGUI dv = new DoctorGUI(doctorGrid, db, this);
        dv.createFields();
        doctorScene = new Scene(doctorGrid, 1280, 720);
    }

    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Nurse view");
        db = new DBConnection();

        startingScreen();
        patientGUI();
        doctorGUI();

        // Buttons
        Button buttonPatient = new Button("Add patient");
        Button buttonDoctor = new Button("Doctors view");
        buttonPatient.setPrefSize(300, 300);
        buttonDoctor.setPrefSize(300, 300);

        buttonPatient.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(patientScene);
            }

        });

        buttonDoctor.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(doctorScene);
            }

        });

        gridpaneStart.add(buttonPatient, 0, 1);
        gridpaneStart.add(buttonDoctor, 1, 1);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
