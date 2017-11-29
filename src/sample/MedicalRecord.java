package sample;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * MedicalRecord is a class for creating medical records of patients.
 * Writes all necessary data of a patient in a .txt file.
 *
 * @author War
 * @version 1.0
 */
public class MedicalRecord {

    private String record;

    public MedicalRecord() {
    }

    /**
     * Create a new medical record of a patient.
     *
     * @param patient The patient.
     * @param note
     * @return True if report is created.
     */
    public boolean newRecord(Patient patient, String note) {
        String content = "ID: " + patient.getId();
        content += "\nName: " + patient.getName();
        content += "\nAge: " + patient.getAge();
        content += "\nGender: " + patient.getGender();
        content += "\nMedical Issue: " + patient.getMedicalIssueName();
        content += "\nWait: " + patient.getWait() + " Minutes";

        content += "\n\nNote:\n" + note;

        record = content;

        byte data[] = content.getBytes();
        String fileName = patient.toString().replace(' ', '-') + "-" + patient.getId();
        Path path = Paths.get("/Users/War/Dropbox/ER/src/sample/records/" + fileName + ".txt");

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(path, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            System.err.println(x);
        }
        return true;
    }

    /**
     * Return the content of the medical record of a patient.
     *
     * @return record of a patient.
     */
    public String getFullReport() {
        return record;
    }
}
