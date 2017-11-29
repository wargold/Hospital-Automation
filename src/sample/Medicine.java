package sample;

/**
 * Medicine is a class for holding necessary information about all the medicine a patient take.
 * All data is retrieved from a database.
 *
 * @author War
 * @version 1.0
 */
public class Medicine {
    private int id;
    private int cost;
    private String name;

    /**
     * Constructor of the class.
     *
     * @param id   Patients ID.
     * @param cost Cost of the Medicine.
     * @param name Name of the Medicine.
     */
    public Medicine(int id, int cost, String name) {
        this.id = id;
        this.cost = cost;
        this.name = name;
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
     * Return the cost of a medicine.
     *
     * @return cost of a specific medicine.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Return the name of the medicine.
     *
     * @return name of a specific medicine.
     */
    public String getName() {
        return name;
    }
}
