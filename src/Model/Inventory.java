package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Inventory {


//    private static ObservableList<Part> allParts = FXCollections.observableArrayList(); this is for debugging/console logging
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    private static int partIDCount = 1;
    private static int productIDCount = 1;

    public static int getPartIDCount() {
        return partIDCount;
    }

    public static int getProductIDCount() {
        return productIDCount;
    }
    public static void addPart(Part newPart) {
        partIDCount++;
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        productIDCount++;
        allProducts.add(newProduct);
    }

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

    public static void updatePart(int index, Part selectedPart) {
        //TODO: Add part to given index
        //TODO: See if the old index should be deleted or if certain aspects of the object are only updated
    }

    public static void updateProduct(int index, Product newProduct) {
        allProducts.add(index, newProduct);
        System.out.println("PRODUCT UPDATED HERES THE UPDATE: ");
        System.out.println(allProducts.get(index).getName());
        System.out.println("Heres the next index name");
        System.out.println(allProducts.get(index+1).getName());
    }

    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


}
