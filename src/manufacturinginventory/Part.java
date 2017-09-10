package manufacturinginventory;

import java.io.Serializable;

/** Class Part describes a part with a name, price, partID,
 * amount in stock, minimum and maximum amounts.
 * Inhouse and Outsourced classes extend part to differentiate
 * where the part is coming from.
 * 
 * @author Paul Brassard
 */
public abstract class Part implements Serializable{
    private String name;
    private double price;
    private int partID, instock, min, max;

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

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
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
}

class Inhouse extends Part{
    private int machineID;
    public void setMachineID(int machineID){
        this.machineID = machineID;
    }
    public int getMachineID(){
        return machineID;
    }
}

class Outsourced extends Part{
    private String companyName;
    public void setCompanyName(String coName){
        companyName = coName;
    }
    public String getCompanyName(){
        return companyName;
    }
}