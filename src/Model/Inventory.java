package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    //ObservableLists for all the current parts and products in inventory
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //Keeps track of how many parts and products are in the inventory for ID purposes
    private static int partIDCount = 1;
    private static int productIDCount = 1;

    /**
     * Gets the total count for part inventory
     * @return The total number of parts in inventory
     */
    public static int getPartIDCount() {
        return partIDCount;
    }

    /**
     * Gets the total count for product inventory
     * @return The total number of
     */
    public static int getProductIDCount() {
        return productIDCount;
    }

    /**
     * Adds given part to inventory
     * @param newPart
     */
    public static void addPart(Part newPart) {
        partIDCount++;
        allParts.add(newPart);
    }

    /**
     * Adds given product to inventory
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        productIDCount++;
        allProducts.add(newProduct);
    }

    /**
     * Looks up part in inventory based on part ID
     * @param partId
     * @return Part that matches with given part ID
     */
    public static Part lookupPart(int partId) {
        for (Part allPart : allParts) {
            if (allPart.getId() == partId) {
                System.out.println(allPart.getName());
                return allPart;
            } else {
                System.out.println("Part not found.");
                return null;
            }
        }
        return null;
    }

    /**
     * Looks up product in inventory based on product ID
     * @param productId
     * @return Part that matches with given product ID
     */
    public static Product lookupProduct(int productId) {
        for(Product allProduct : allProducts) {
            if(allProduct.getId() == productId) {
                System.out.println("Product found");
                return allProduct;
            } else {
                System.out.println("Product not found.");
                return null;
            }
        }
        return null;
    }

    /**
     * Looks up part based on given part name (or partial part name)
     * @param partName
     * @return Part that matches with name (or partial match)
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> tempList = FXCollections.observableArrayList();
        for(Part allPart : allParts) {
            if(allPart.getName().equals(partName)) {
                System.out.println("Found part");
                tempList.add(allPart);
            } else {
                System.out.println("Part not found");
                return null;
            }
        }
        return tempList;
    }

    /**
     * Looks up product based on given product name (or partial product name)
     * @param productName
     * @return Product that matches with name (or partial name)
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> tempList = FXCollections.observableArrayList();
        for(Product allProduct : allProducts) {
            if(allProduct.getName().equals(productName)) {
                System.out.println("Found product");
                tempList.add(allProduct);
            } else {
                System.out.println("No product found");
                return null;
            }
        }
        return tempList;
    }

    /**
     * Updates a part at a given index based on a given updated part
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product at a given index based on a given updated product
     * @param index
     * @param newProduct
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes a part in the inventory
     * @param selectedPart
     * @return True if part was removed
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product in the inventory
     * @param selectedProduct
     * @return True if product was removed
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Returns all parts in the inventory
     * @return ObservableList of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns all products in the inventory
     * @return ObservableList of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
