package object;

import java.util.ArrayList;
/**
 * @author Thomas
 */
public class Bag {
    private int maxSpace;
    private int maxWeight;
    public int actualSpace;
    public int actualWeight;

    public ArrayList<BagItem> bag;
	
    public Bag() {
	maxSpace = 100;
	maxWeight = 100;
	bag = new ArrayList<>();
	actualSpace = 0;
	actualWeight = 0;
    }
	
    //If the bag is not full the item is add correctely
    //This action function outside to the bag menù
    public void addItem(BagItem item, String acquisition) {
    	if (isNotFull(item.getSpace(), item.getWeight()) == true) {
            if (bag.contains(item)) {
                int n = bag.indexOf(bag);
		bag.get(n).addQuantity(1);
            } else {
		bag.add(item);
                int n = bag.indexOf(bag);
		bag.get(n).addQuantity(1);
            }
	System.out.println(tellAction(acquisition, item, 1, 0, 0));
	}
    }
		
    //If the bag is not full the items are add correctely
    //This action function outside to the bag menù
    public void addMoreItems(BagItem item, String acquisition, int quantity) {
    	if (isNotFull(item.getSpace()*quantity, item.getWeight()*quantity) == true) {
            if (bag.contains(item)) {
                int n = bag.indexOf(bag);
		bag.get(n).addQuantity(1);
            } else {
		bag.add(item);
                int n = bag.indexOf(bag);
		bag.get(n).addQuantity(1);
            }
            System.out.println(tellAction(acquisition, item, quantity, 0, 0));
	}
    }
	
    //Controll the correct removing of item
    //This action function inside to the bag menù
    public void removeItem(BagItem item, String toss, int quantity) {
    	if (quantity < item.quantity) {
            item.removeQuantity(quantity);
            System.out.println(tellAction("tossBag", item,
                quantity, item.getSpace()*quantity, item.getWeight()*quantity));
	} else if (quantity == item.quantity) {
            bag.remove(item);
            System.out.println(tellAction("tossAll", item,
                0, item.getSpace()*quantity, item.getWeight()*quantity));
	} else {
            System.out.println(tellAction("cannotDo", null, 0, 0, 0));
	}
    }
	
    //Controll the correct moving of item
    //This action function inside to the bag menù
    public void moveItem(BagItem item, int position) {
    	bag.remove(item);
    	bag.add(position, item);
    	System.out.println(tellAction("moveOK", null, 0, 0, 0));
    }



    private boolean isNotFull(int space, int weight) {
        if ((actualSpace + space) <= maxSpace && (actualWeight + weight) <= maxWeight) {
            return true;
	} else {
            System.out.println(tellAction("isFull", null, 0, space, weight));
            return false;
	}
    }

    private String tellAction(String action, BagItem item, int quantity, int space, int weight) {
	switch (action) {
            case "market":
		if (quantity == 1) { return ("Thank you to buy a " + item); }
		else { return ("Thank you to buy a " + item + " for " + quantity + " pieces"); }
	
            case "addBag":
		if (quantity == 1) { return (item + " was added to bag."); }
		else { return (item + " were added to bag."); }

            case "isFull":
		if ((actualSpace + space) <= maxSpace) {
                    return ("The bag cannot support other weight!");
                } else if ((actualWeight + weight) <= maxWeight) {
                    return ("There isn't other space in the bag!");
                } else { 
                    return ("The bag is full!");
                }
                
            case "tossBag":
                return ("Were toss " + item + " for " + quantity + " pieces!\n")
                        + (" It was released " + space + " space and " + weight + " weight.");

            case "tossAll":
                return (item + " were all toss! It was released " + space + " space and " + weight + " weight.");

            case "cannotDo":
                return ("The following action is not allowed.");

            case "moveOK":
                return ("The item was correctely moved.");

            default: return null;
    	}
    }
}
