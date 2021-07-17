package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    //List of associated parts to a product object
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    //Variables for product objects
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    //Constructor for product creation
    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

    /**
     * Gets the product ID of a product object
     * @return product id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id to a product object
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of a product object
     * @return The name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a product
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of a product object
     * @return The price of a product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of a product
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the number of stock of a product in the inventory
     * @return The number of stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the number of stock of a product in the inventory
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the minimum number of stock for a product
     * @return minimum number of stock
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum number of stock for a product
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the maximum number of stock for a product
     * @return maximum number of stock
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximum number of stock for a product
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds an associated part to a product object
     * @param part
     */
    public void addAssociatePart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from a product object
     * @param selectedAssociatedPart
     * @return True if the part association was removed
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for(Part part : associatedParts) {
            if(part.equals(selectedAssociatedPart)) {
                associatedParts.remove(part);
                System.out.println("Deleted associated part");
                return true;
            } else {
                System.out.println("Part not found.");
            }
        }
        return false;
    }

    /**
     * Gets a list of associated parts to a product object
     * @return ObservableList of associated parts
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
}
