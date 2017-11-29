package sample;

import java.sql.ResultSet;

/**
 * Patient is a class fo holding necessary information about a patient.
 * All data is retrieved from a database.
 *
 * @author War
 * @version 1.0
 */
public class Patient {
    private int id;
    private int age;
    private int medicalIssueId;
    private int teamId;
    private int wait;
    private int priority;
    private String name;
    private String gender;
    private String medicalIssueName;

    /**
     * Initialize...
     *
     * @param id ID of the patient
     */
    public Patient(int id,DBConnection db) {
        this.id = id;
        ResultSet res = db.SQlQuery("SELECT * FROM patient JOIN medical_issue ON patient.medical_issue_id= " +
                "medical_issue.id AND patient.id=" + id);
        try {
            while (res.next()) {
                name = res.getString(2);
                age = res.getInt(3);
                gender = res.getString(4);
                priority = res.getInt(5);
                wait = res.getInt(6);
                medicalIssueId = res.getInt(7);
                teamId = res.getInt(8);
                medicalIssueName = res.getString(10);
                res.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Return the patients ID.
     *
     * @return ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Return the patients Age.
     *
     * @return Age
     */
    public int getAge() {
        return age;
    }

    /**
     * Return the ID of the patients medical issue.
     *
     * @return
     */
    public int getMedicalIssueId() {
        return medicalIssueId;
    }

    /**
     * Return the teamId of the medical team.
     *
     * @return
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Return the amount of minutes a patients have waited in total.
     *
     * @return Minutes of Total Wait.
     */
    public int getWait() {
        return wait;
    }

    /**
     * Return the number that indicates the priority of a patient.
     *
     * @return priority of a patient.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Return the patients name.
     *
     * @return name of the patient.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the patients gender.
     *
     * @return name of the patient.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Return the patients medical Issue/Illness.
     *
     * @return name of the patient.
     */
    public String getMedicalIssueName() {
        return medicalIssueName;
    }
}
