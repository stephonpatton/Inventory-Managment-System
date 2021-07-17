package Model;

public class InhousePart extends Part {
    //Variable for machine ID
    private int machineId;

    /**
     * Extends Part class in order to be an InHouse part with a machine ID
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InhousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     * Sets the machine ID of an InHousePart object
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets the machine ID of an InHousePart object
     * @return machine ID
     */
    public int getMachineId() {
        return this.machineId;
    }
}
