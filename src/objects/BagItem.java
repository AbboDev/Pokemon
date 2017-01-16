package objects;

/**
 * @author Thomas
 */
public class BagItem {

    int getID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public enum TypeOfItem {
        Healt, Multiplier, Food, Berry,
        AddCritical
    };
    public enum CalcOfEffect {
        Percentage, Unit
    };
    
    public TypeOfItem toI;
    public CalcOfEffect coE;
    public int valueOfEffect;
    
    public int quantity;
    public int price;
    public int space;
    public int weight;
    public ItemUse description;
	
    public BagItem() {
    	quantity = 0;
	price = 0;
        space = 0;
        weight = 0;
	description = null;
    }
    public BagItem(String name) {
        
    }
	
    public void addQuantity(int number) { quantity += number; }	
    public void removeQuantity(int number) { quantity -= number; }

    public TypeOfItem getToI() { return toI; }
    public CalcOfEffect getCoE() { return coE; }
    public int getValueOfEffect() { return valueOfEffect; }    

    public int getPrice() { return price; }
    public int getSpace() { return space; }
    public int getWeight() { return weight; }

    public ItemUse getDescription() { return description; }
}
