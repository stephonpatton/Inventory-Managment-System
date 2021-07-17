package Model;

public class OutsourcedPart extends Part {
    //Variable for company name
    private String companyName;
    //Super constructor to create outsourced part with company name
    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /**
     * Sets the company name of an outsourced part
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Gets the company name of an outsourced part
     * @return Company name
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
