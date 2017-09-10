package manufacturinginventory;

import java.io.Serializable;
import java.util.ArrayList;

/** Class Inventory holds ArrayLists for products and parts,
 * giving methods to find, add, and remove products and parts.
 * 
 * @author Paul Brassard
 */
public class Inventory implements Serializable{
    private final ArrayList<Product> products;
    private final ArrayList<Part> allParts;
    
    public Inventory(){
        products = new ArrayList<>();
        allParts = new ArrayList<>();
    }
    
    public void addProduct(Product product){
        products.add(product);
    }
    public boolean removeProduct(int index){
        return products.remove(index) instanceof Product;
    }
    public Product lookupProduct(int index){
        return products.get(index);
    }
    public void updateProduct(int index){
        //Updating of products is done with the ObservableLists in ManufacturingInventory class.
    }
    public void addPart(Part part){
        allParts.add(part);
    }
    public boolean deletePart(int index){
        return allParts.remove(index) instanceof Part;
    }
    public Part lookupPart(int index){
        return allParts.get(index);
    }
    public void updatePart(int index){
        //Updating of parts is done with the ObservableLists in ManufacturingInventory class.
    }
}
