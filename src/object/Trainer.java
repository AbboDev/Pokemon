package object;

import java.util.Random;
/**
 * @author Thomas
 */
public class Trainer {
    public String octID;
    public String hexID;
    public String name;

    public int money;
    public Party party;
    public Bag bag;
    public TrainerCard card;

    public int xCoo;
    public int yCoo;
    public int orientation;

    public boolean runningShoes;
    public boolean isUsingBike;

    private static final int maxRand = 999999999;
    private static final int minRand = 100000000;
    private Random rand;
    private int randomNum;

    public Trainer() {
    	rand = new Random();
    	randID();
    	money = 0;
    	party = new Party();
    	bag = new Bag();
    	card = new TrainerCard(octID, hexID, money);
    	orientation = 1;
    	runningShoes = false;
    	isUsingBike = false;
    }

    //generate 2 random ID
    //the first is in octal format
    //the second is in hexadecimal format
    private void randID() {
    	randomNum = rand.nextInt((maxRand - minRand) + 1) + maxRand;
    	octID = Integer.toOctalString(randomNum);
    	randomNum = rand.nextInt((maxRand - minRand) + 1) + maxRand;
    	hexID = Integer.toHexString(randomNum);
    }

    public String getOctID() {
        return octID;
    }
    public String getHexID() {
        return hexID;
    }
    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public Party getParty() {
        return party;
    }
    public Bag getBag() {
        return bag;
    }
    public TrainerCard getCard() {
        return card;
    }

    public int getxCoo() {
        return xCoo;
    }
    public int getyCoo() {
        return yCoo;
    }
    public int getOrientation() {
        return orientation;
    }

    public boolean isRunningShoes() {
        return runningShoes;
    }
    public boolean isUsingBike() {
        return isUsingBike;
    }
}
