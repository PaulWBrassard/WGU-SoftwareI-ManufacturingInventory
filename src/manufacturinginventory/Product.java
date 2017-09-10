package manufacturinginventory;

import java.io.Serializable;
import java.util.ArrayList;

/** Class product describes a product with a name, price, productID,
 * amount in stock, ArrayList of parts associated, minimum and maximum amount.
 *
 * @author Paul Brassard
 */
public class Product implements Serializable{
    private final ArrayList<Part> associatedParts;
    private String name;
    private double price;
    private int productID, instock, min, max;
    
    public Product(String name, double price){
        associatedParts = new ArrayList<>();
        this.name = name;
        this.price = price;
    }
    
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getInstock() {
        return instock;
    }

    public void setInstock(int instock) {
        this.instock = instock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    
    public boolean removeAssociatedPart(int index){
        return associatedParts.remove(index) instanceof Part;
    }
    
    public Part lookupAssociatedPart(int index){
        return associatedParts.get(index);
    }

    public int getAssociatedPartsSize(){
        return associatedParts.size();
    }
    
    public void clearAssociatedParts(){
        associatedParts.clear();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
}
