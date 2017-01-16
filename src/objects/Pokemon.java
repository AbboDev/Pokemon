package objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import objects.Move.StatsOfAttacks;
import screen.Board;

import static objects.Move.StatsOfAttacks.*;

/**
 * @author Thomas
 */
public class Pokemon {
    public enum Type {
        Normal, Fighting, Flying, Rock, Ground,
        Steel, Ghost, Dark, Psychic, Bug,
        Poison, Grass, Water, Ice, Fire,
        Electric, Fairy, Dragon, Dinosaur, Light
    };
    public enum Status {
        OK, KO, Poison, Paralysis, Burn,
        Freeze, Asleep, BadPoison
    };
    public enum Evolution {
        Level, Switch, Stone, SwitchItem,
        Happiness, LevelWithStats, LevelInPlace,
        LevelInTime, LevelInWeather, LevelWithItem,
        LevelWithMove, SwitchEachOther, Eevee
    };
    public enum Nature {
        Hardy, Lonely, Brave, Adamant, Naughty,
        Bold, Docile, Relaxed, Impish, Lax,
        Timid, Hasty, Serious, Jolly, Naive,
        Modest, Mild, Quiet, Bashful, Rash,
        Calm, Gentle, Sassy, Careful, Quirky
    };
    
    //Maximun Value
    private static final int MAX_IV = 31;
    private static final int MAX_EV = 510;
    private static final int MAX_MOVES = 6;
//    private static final int MAX_ACC = 100;
//    private static final int MAX_ELU = 0;
    private static final int MAX_HPNS = 0;
    private static final int MAX_LVL = 100;
    private static final int MIN_CRIT = 1;
    private static final int MAX_CRIT = 1;
    private static final int MIN_SLEEP = 1;
    private static final int MAX_SLEEP = 3;
    
    //Round Value
    private int roundBadPSN;
    private int roundSLP;
    private int roundCNF;
    
    //Nature Stats
    private Nature nature;
    private double natAtk = 1;
    private double natDef = 1;
    private double natSpAtk = 1;
    private double natSpDef = 1;
    private double natSpd = 1;
    
    private int tempCrit;
    
    //Level Stats (only battle)
    private HashMap<StatsOfAttacks, Integer> levelStats = new HashMap<>();
    private HashMap<String, Integer> stats = new HashMap<>();
    
    //boolean
    private boolean isPowered = false;
    private boolean isShiny = false;
    private boolean hasPokerus = false;
    private boolean isFlying = false;
    private boolean isDig = false;
    private boolean isSub = false;
    private boolean isConfused = false;
    private boolean isInfatuated = false;
    private boolean isFlinched = false;
    private boolean isMale;
    private boolean noSex;
    
    private BagItem ownItem;
    private Ability ability;
    private ArrayList<Move> moveSet = new ArrayList<>();
    private ArrayList<String> allMoves = new ArrayList<>();
    
    //Evolution
    private String nextPokemon;
    private Evolution evolution;
    private int evolutionLevel;
    private String evolutionWay;
    private String nextPokemon2;
    private Evolution evolution2;
    private int evolutionLevel2;
    private String evolutionWay2;
    
    //Pokemon Features
    private String name;
    private String surname;
    private String nameOfTrainer;
    private String IDOfTrainer;
    private String secretIDOfTrainer;
    private String region;
    private Type firstType;
    private Type secondType;
    private Status status;
    private BagItem pokeball;
    
    //Ratio
    private double genderRatio;
    private int catchRatio;
    
    private Random rand = new Random();
    private int random;
    
    public static File KANTO;
    public static File KANTO_MOVE;
    public static File JOHTO;
    public static File JOHTO_MOVE;
    public static File MOVES;
    
    public Pokemon() {
        firstType = null;
        secondType = null;
        status = Status.OK;
        isShiny = false;
        ownItem = null;
        ability = null;
        nextPokemon = null;
        name = null;
        surname = null;
        nameOfTrainer = null;
        assignFile();
    }
    
    private void assignFile() {
        MOVES = new File(Board.ROOT+"/resources/database/move.csv");
        KANTO = new File(Board.ROOT+"/resources/database/kanto.csv");
        KANTO_MOVE = new File(Board.ROOT+"/resources/database/kantoMove.csv");
        JOHTO = new File(Board.ROOT+"/resources/database/johto.csv");
        JOHTO_MOVE = new File(Board.ROOT+"/resources/database/johtoMove.csv");
    }
    
    //Create a random Pokemon from .CSV
    public Pokemon(int ID, int level, boolean wild, String trainerIDhex, String trainerIDoct) {
        assignFile();
        stats.put("ID", ID);
        File csvStats, csvMoves;
        if (ID >= 1 && ID <= 151) {
            csvStats = KANTO;
            csvMoves = KANTO_MOVE;
        } else {
            csvStats = JOHTO;
            csvMoves = JOHTO_MOVE;
        }
        BufferedReader bufferStats = null;
        String line, split = ";", splitMultiple = "@";
        try {
            bufferStats = new BufferedReader(new FileReader(csvStats));
            while ((line = bufferStats.readLine()) != null ) {
                String[] currentLine = line.split(split);
                if (currentLine[0].equals(String.valueOf(ID))) {
                    name = currentLine[1]; surname = name;
                    
                    firstType = setType(currentLine[2]);
                    secondType = setType(currentLine[3]);
                    
                    if (!currentLine[4].equals(currentLine[5])) {
                        random = rand.nextInt((20 - 1) + 1) + 1;
                        if (random >= 1 && random <= 10) {
//                            ability = currentLine[4];
                        } else if (random > 10 && random <= 20) {
//                            ability = currentLine[5];
                        }
                    } else {
//                        ability = currentLine[4];
                    }
                    if (level > MAX_LVL) level = MAX_LVL;
                    stats.put("Level", level);
                    
                    status = Status.OK;
                    
                    random = rand.nextInt((4096 - 1) + 1) + 1;
                    if (random == 1) {
                        isShiny = true;
                    }
                    
                    if (currentLine[7].contains(splitMultiple)) {
                        String[] evo = currentLine[7].split(splitMultiple);
                        evolution = setTypeOfEvolution(evo[0]);
                        evolution = setTypeOfEvolution(evo[1]);
                    } else {
                        evolution = setTypeOfEvolution(currentLine[7]);
                    }
                    
                    String[] ways = currentLine[8].split(splitMultiple);
                    
                    for (int i = 0; i < ways.length-1; ++i) {
                        try {
                            if (i == 0)
                                evolutionLevel = Integer.parseInt(ways[0]);
                            else if (i == 1)
                                evolutionLevel2 = Integer.parseInt(ways[1]);
                        } catch (Exception e) {
                            if (i == 0)
                                evolutionWay = ways[0];
                            else if (i == 1)
                                evolutionWay2 = ways[1];
                        }
                    }
                    
                    if (currentLine[9].contains(splitMultiple)) {
                        String[] next = currentLine[9].split(splitMultiple);
                        nextPokemon = next[0];
                        nextPokemon = next[1];
                    } else {
                        nextPokemon = currentLine[9];
                    }
                    
                    if (!"null".equals(currentLine[10])) {
                        random = rand.nextInt(((100 - 0) + 1) + 0);
                        String[] percent2 = null;
                        int percent;
                        if (currentLine[11].contains(splitMultiple)) {
                            percent2 = currentLine[11].split(splitMultiple);
                            percent = parseInteger(percent2[0]) + parseInteger(percent2[1]);
                        } else {
                            percent = parseInteger(currentLine[11]);
                        }
                        if (random <= percent) {
                            if (currentLine[10].contains(splitMultiple)) {
                                String[] item = currentLine[10].split(splitMultiple);
                                if (random <= parseInteger(percent2[0])) {
                                    ownItem = new BagItem(item[0]);
                                } else {
                                    ownItem = new BagItem(item[1]);
                                }
                            } else {
                                ownItem = new BagItem(currentLine[10]);
                            }
                        }
                    } else {
                        ownItem = null;
                    }
                    
                    stats.put("BasicHP", parseInteger(currentLine[12]));
                    stats.put("BasicAttack", parseInteger(currentLine[13]));
                    stats.put("BasicDefense", parseInteger(currentLine[14]));
                    stats.put("BasicSPAttack", parseInteger(currentLine[15]));
                    stats.put("BasicSPDefense", parseInteger(currentLine[16]));
                    stats.put("BasicSpeed", parseInteger(currentLine[17]));
                    
                    stats.put("BasicEVHP", parseInteger(currentLine[18]));
                    stats.put("BasicEVAttack", parseInteger(currentLine[19]));
                    stats.put("BasicEVDefense", parseInteger(currentLine[20]));
                    stats.put("BasicEVSPAttack", parseInteger(currentLine[21]));
                    stats.put("BasicEVSPDefense", parseInteger(currentLine[22]));
                    stats.put("BasicEVSpeed", parseInteger(currentLine[23]));
                    
                    stats.put("Happiness", parseInteger(currentLine[24]));
                    
                    stats.put("BasicExp", parseInteger(currentLine[25]));
                    stats.put("MaxExp", parseInteger(currentLine[26]));
                    stats.put("Exp", getLevelExperience(stats.get("Level")));
                    
                    stats.put("CatchRatio", parseInteger(currentLine[28]));
                    try {
                        genderRatio = Double.parseDouble(currentLine[27]);
                        if (genderRatio > 0 && genderRatio < 100) {
                            random = rand.nextInt((100 - 0) + 1) + 0;
                            if (random <= genderRatio) {
                                isMale = true;
                            } else if (random < genderRatio) {
                                isMale = false;
                            }
                        } else if (genderRatio == 0) {
                            isMale = false;
                        } else if (genderRatio == 100) {
                            isMale = true;
                        }
                        noSex = false;
                    } catch (Exception e) {
                        noSex = true;
                        isMale = false;
                    }
                    if (trainerIDhex != null && trainerIDoct != null) {
                        IDOfTrainer = trainerIDoct;
                        secretIDOfTrainer = trainerIDoct;
                    } else {
                        IDOfTrainer = secretIDOfTrainer = null;
                    }
                    roundBadPSN = 1;
                    roundSLP = 0;
                    
                    random = rand.nextInt((25 - 1) + 1) + 1;
                    randomNature(random);
                    generateIV();
                    assignStat();
                    assignMove(csvMoves);
                }
            }
        } catch(FileNotFoundException e) {} catch (IOException e) {}
        finally {
            if (bufferStats != null) {
                try { bufferStats.close(); } catch (IOException e) {}
            }
        }
    }
    /**
     * 
     */
    public final void assignStat() {
        stats.put("EVHP", 0); stats.put("EVAttack", 0);
        stats.put("EVDefense", 0); stats.put("EVSPAttack", 0);
        stats.put("EVSPDefense", 0); stats.put("EVSpeed", 0);
        levelStats.put(Atk, 0); levelStats.put(Def, 0);
        levelStats.put(SpAtk, 0); levelStats.put(SpDef, 0);
        levelStats.put(Spd, 0); levelStats.put(Eva, 0);
        levelStats.put(Acc, 0); tempCrit = MIN_CRIT;
        
        int level = stats.get("Level");
        int HP = ((stats.get("IVHP")+(2*stats.get("BasicHP"))+
                (stats.get("EVHP")/4))*level/100)+10+level;
        int attack = (int) ((((stats.get("IVAttack")+(2*stats.get("BasicAttack"))+
                (stats.get("EVAttack")/4))*level/100)+5)*natAtk);
        int defense = (int) ((((stats.get("IVDefense")+(2*stats.get("BasicDefense"))+
                (stats.get("EVDefense")/4))*level/100)+5)*natDef);
        int spAttack = (int) ((((stats.get("IVSPAttack")+(2*stats.get("BasicSPAttack"))+
                (stats.get("EVSPAttack")/4))*level/100)+5)*natSpAtk);
        int spDefense = (int) ((((stats.get("IVSPDefense")+(2*stats.get("BasicSPDefense"))+
                (stats.get("EVSPDefense")/4))*level/100)+5)*natSpDef);
        int speed = (int) ((((stats.get("IVSpeed")+(2*stats.get("BasicSpeed"))+
                (stats.get("EVSpeed")/4))*level/100)+5)*natSpd);
        
        stats.put("HP", HP); stats.put("MaxHP", HP);
        stats.put("Atk", attack); stats.put("Def", defense);
        stats.put("SpAtk", spAttack); stats.put("SpDef", spDefense);
        stats.put("Spd", speed); stats.put("Acc", 100); stats.put("Eva", 100);
    }
    /**
     * 
     * @param csvMoves
     * @throws IOException 
     */
    public final void assignMove(File csvMoves) throws IOException {
        String line, split = ";", splitMove = "#";
        BufferedReader bufferMove;
        
        bufferMove = new BufferedReader(new FileReader(csvMoves));
        while ((line = bufferMove.readLine()) != null) {
            String[] currentLine = line.split(split);
            if (currentLine[0].equals(String.valueOf(this.getStat("ID")))) {
                for (int i = currentLine.length; i > 1; --i) {
                    allMoves.add(currentLine[i-1]);
                }
            }
        }
        for (int y = 0; y < allMoves.size(); ++y) {
            String[] currentMove = allMoves.get(y).split(splitMove);
            if (parseInteger(currentMove[1]) <= this.getStat("Level")) {
                if (moveSet.size() < 6) {
                    Move move = new Move(currentMove[0]);
                    if (!getIfContainsMove(move)) {
                        moveSet.add(0, move);
                    }
                } else {
                    break;
                }
            }
        }
    }
    /**
     * 
     * @param item 
     */
    public void giveItem(BagItem item) { ownItem = item; }
    /**
     * 
     * @param key
     * @return 
     */
    public int getStat(String key) { return stats.get(key); }
    /**
     * 
     * @param key
     * @return 
     */
    public String getStatName(String key) {
        String name = null;
        switch (key) {
            case "Atk": name = "Attack"; break;
            case "Def": name = "Defense"; break;
            case "SpAtk": name = "Special Attack"; break;
            case "SpDef": name = "Special Defense"; break;
            case "Spd": name = "Speed"; break;
            case "Eva": name = "Evasion"; break;
            case "Acc": name = "Accuracy"; break;
        }
        return name;
    }

    public static int getMaxMoves() { return MAX_MOVES; }
//    public static int getMaxAccuracy() { return MAX_ACC; }
//    public static int getMaxElusion() { return MAX_ELU; }
    public static int getMaxHappiness() { return MAX_HPNS; }
    public static int getMaxLevel() { return MAX_LVL; }
    public static int getMaxIV() { return MAX_IV; }
    public static int getMaxEV() { return MAX_EV; }
    public static int getMinCrit() { return MIN_CRIT; }
    public static int getMaxCrit() { return MAX_CRIT; }
    
    public double getNatAtk() { return natAtk; }
    public double getNatDef() { return natDef; }
    public double getNatSpAtk() { return natSpAtk; }
    public double getNatSpDef() { return natSpDef; }
    public double getNatSpd() { return natSpd; }

    public int getTempBaseStat(StatsOfAttacks key) {
        return modBaseStats(key);
    }
    public int getTempOtherStat(StatsOfAttacks key) {
        return modOtherStats(key);
    }
    
    public int getTempCrit() { return tempCrit; }
    
    public int getLevelStats(StatsOfAttacks stat) {
        return levelStats.get(stat);
    }
    
    public void setLevelStats(StatsOfAttacks stat, int level) {
        int lvl = levelStats.get(stat);
        if (lvl > -6 && lvl < 6) {
            if ((lvl+level) < -6) lvl = -6;
            else if ((lvl+level) > 6) lvl = 6;
            else lvl += level;
        }
        levelStats.put(stat, lvl);
    }
    
    /**
     * Increase or decrease the int stat
     * Attack, Defense, Sp Attack, Sp Defense, Speed
     * @param stat
     * @return 
     */
    private int modBaseStats(StatsOfAttacks key) {
        int stat, newStat;
        stat = stats.get(key.toString());
        int statLevel = levelStats.get(key);
        switch (statLevel) {
            case -6: newStat = (stat*2)/8; break;
            case -5: newStat = (stat*2)/7; break;
            case -4: newStat = (stat*2)/6; break;
            case -3: newStat = (stat*2)/5; break;
            case -2: newStat = (stat*2)/4; break;
            case -1: newStat = (stat*2)/3; break;
            case 0: newStat = stat * 1; break;
            case 1: newStat = (stat*3)/2; break;
            case 2: newStat = stat * 2; break;
            case 3: newStat = (stat*5)/2; break;
            case 4: newStat = stat * 3; break;
            case 5: newStat = (stat*7)/2; break;
            case 6: newStat = stat * 4; break;
            default: newStat = stat * 1; break;
        }
        return newStat;
    }
    /**
     * Increase or decrease the int stat
     * Accuracy, Evasion
     * @param stat
     * @return 
     */
    private int modOtherStats(StatsOfAttacks key) {
        int stat = 0, newStat;
        stats.get(key.toString());
        int statLevel = levelStats.get(key);
        switch (statLevel) {
            case -6: newStat = (stat*3)/9; break;
            case -5: newStat = (stat*3)/8; break;
            case -4: newStat = (stat*3)/7; break;
            case -3: newStat = (stat*3)/6; break;
            case -2: newStat = (stat*3)/5; break;
            case -1: newStat = (stat*3)/4; break;
            case 0: newStat = stat * 1; break;
            case 1: newStat = (stat*4)/3; break;
            case 2: newStat = (stat*5)/3; break;
            case 3: newStat = stat * 2; break;
            case 4: newStat = (stat*7)/3; break;
            case 5: newStat = (stat*8)/3; break;
            case 6: newStat = stat * 3; break;
            default: newStat = stat * 1; break;
        }
        return newStat;
    }
    
    public void setTempStats(Move move) {
        for (Map.Entry<StatsOfAttacks, Integer> statLvl : move.getStatLevelSet()) {
            if (statLvl.getValue() != 0) {
                setLevelStats(statLvl.getKey(), move.getStatLevel(statLvl.getKey()));
                System.out.println(statLvl.getKey()+": "+levelStats.get(statLvl.getKey()));
            }
        }
    }
    
    public void resetCritical() {
        tempCrit = MIN_CRIT;
    }

    public Type getFirstType() { return firstType; }
    public Type getSecondType() { return secondType; }

    public boolean getIfPowered() { return isPowered; }
    public boolean getIfShiny() { return isShiny; }
    public boolean getIfPokerus() { return hasPokerus; }
    public boolean getIfMale() { return isMale; }
    public boolean getIfAsessual() { return noSex; }
    public boolean getIfFlying() { return isFlying; }
    public boolean getIfDig() { return isDig; }
    public boolean getIfSub() { return isSub; }
    public boolean getIfConfused() { return isConfused; }
    public void setIfConfused(boolean cnf) { isConfused = cnf; }
    public boolean getIfInfatuated() { return isInfatuated; }
    public void setIfInfatuated(boolean inf) { isInfatuated = inf; }
    public boolean getIfFlinched() { return isFlinched; }
    public void setIfFlinched(boolean fli) { isFlinched = fli; }
    
    public double getGenderRatio() { return genderRatio; }
    public int getCatchRatio() { return catchRatio; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status sts) { status = sts; }
    public int getRoundBadPSN() { return roundBadPSN; }
    public int getRoundSLP() { return roundSLP; }
    public int getRoundCNF() { return roundCNF; }
    public Ability getAbility() { return ability; }
    public BagItem getOwnItem() { return ownItem; }

    public int getID() { return stats.get("ID"); }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getNameOfTrainer() { return nameOfTrainer; }
    public String getIDOfTrainer() { return IDOfTrainer; }
    public String getSecretIDOfTrainer() { return secretIDOfTrainer; }
    public String getRegion() { return region; }
    public Nature getNature() { return nature; }
    public BagItem getPokeball() { return pokeball; }
    
    public String getNextPokemon() { return nextPokemon; }
    public Evolution getEvolution() { return evolution; }
    public int getEvolutionLevel() { return evolutionLevel; }
    public String getEvolutionWay() { return evolutionWay; }
    public String getNextPokemon2() { return nextPokemon2; }
    public Evolution getEvolution2() { return evolution2; }
    public int getEvolutionLevel2() { return evolutionLevel2; }
    public String getEvolutionWay2() { return evolutionWay2; }
    
/******************************************************************************/
    /**
     * 
     * @return 
     */
    public ArrayList<Move> getMoveSet() { return moveSet; }
    /**
     * 
     * @return 
     */
    public ArrayList<String> getAllMoves() { return allMoves; }
    /**
     * 
     * @param newMove
     * @return 
     */
    public boolean getIfContainsMove(Move newMove) {
        for (Move move : moveSet) {
            if (newMove.getName().equals(moveSet.get(moveSet.indexOf(move)).getName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 
     * @return 
     */
    public int getNumberOfMoves() { return moveSet.size(); }
    /**
     * 
     * @param morePP
     * @param usedPP
     * @return 
     */
    public Move getRandomMove(boolean morePP, int usedPP) {
        if (useStruggle()) return new Move("Struggle");
        random = rand.nextInt((moveSet.size() - 1) + 1) + 1;
        while (moveSet.get(random-1).getPP() <= 0) {
            random = rand.nextInt((moveSet.size() - 1) + 1) + 1;
        }
        moveSet.get(random-1).decreasePP(morePP, usedPP);
        return moveSet.get(random-1);
    }
    /**
     * 
     * @return 
     */
    public boolean useStruggle() {
        for (int i = 0; i < moveSet.size(); ++i) {
            if (moveSet.get(i).getPP() > 0) return false;
        }
        return true;
    }
    /**
     * 
     * @param index
     * @return 
     */
    public String getNameOfMoves(int index) { return moveSet.get(index).toString(); }
    /**
     * 
     * @param name
     * @return 
     */
    public Move getMoves(String name) {
        if (moveSet.contains(new Move(name))) {
            return moveSet.get(moveSet.indexOf(name));
        } else {
            return null;
        }
    }
    /**
     * 
     * @param move 
     */
    public void setStatus(Move move) {
        switch (move.getStatus()) {
            case BadPoisoned:
                if ((this.firstType != Type.Poison && this.firstType != Type.Steel) &&
                        (this.secondType != Type.Poison && this.secondType != Type.Steel))
                    status = Status.BadPoison; break;
            case Burn:
                if (this.firstType != Type.Fire && this.secondType != Type.Fire)
                    status = Status.Burn; break;
            case Freeze:
                if (this.firstType != Type.Ice && this.secondType != Type.Ice)
                    status = Status.Freeze; break;
            case Paralysis:
                if (this.firstType != Type.Electric && this.secondType != Type.Electric)
                    status = Status.Paralysis; break;
            case Poison:
                if ((this.firstType != Type.Poison && this.firstType != Type.Steel) &&
                        (this.secondType != Type.Poison && this.secondType != Type.Steel))
                    status = Status.Poison; break;
            case Sleep:
                status = Status.Asleep; random = rand.nextInt((MAX_SLEEP - MIN_SLEEP) + 1) + MIN_SLEEP;
                roundSLP = random; break;
            default: break;
        }
    }
    /**
     * 
     */
    public void defeat() {
        status = Status.KO;
    }
    /**
     * 
     * @param cond
     * @param ID
     * @param dir
     * @return 
     */
    public static String getImagePath(ArrayList<Boolean> cond, int ID, String dir) {
        String path = Board.ROOT+"/resources/"+dir+"/";
        if (cond.get(0)) { //is shiny
            boolean checkF;
            File f = new File(path+"shiny/"+ID+".png");
            checkF = f.exists();
            if (checkF) path += "shiny/";
        }
        if (!cond.get(1)) { //has sex
            if (cond.get(2)) { //is female
                boolean checkF;
                File f = new File(path+"female/"+ID+".png");
                checkF = f.exists();
                if (checkF) path += "female/";
            }
        }
        String image = path + ID + ".png";
        return image;
    }
    /**
     * 
     * @return 
     */
    public int getNextLevelExperience() {
        int nextLevelExp = getLevelExperience(stats.get("Level")+1);
        int nextExp = nextLevelExp - stats.get("Exp");
        return nextExp;
    }
    /**
     * 
     * @param level
     * @return 
     */
    public final int getLevelExperience(int level) {
        int levelExp = 0;
        switch (stats.get("MaxExp")) {
            case 600000:
                if (level <= 25) {
                    levelExp = (int) ((Math.pow((double)level, 3) * (100 - level)) / 50);
                } else if (level >= 51 && level <= 68) {
                    levelExp = (int) ((Math.pow((double)level, 3) * (150 - level)) / 100);
                } else if (level >= 69 && level <= 98) {
                    levelExp = (int) ((Math.pow((double)level, 3) * ((1911 - 10*level) / 3)) / 500);
                } else if (level >= 99 && level <= 100) {
                    levelExp = (int) ((Math.pow((double)level, 3) * (160 - level)) / 100);
                }
                break;
            case 800000:
                levelExp = (int) (4 * Math.pow((double)level, 3) / 5);
                break;
            case 1000000:
                levelExp = (int) Math.pow((double)level, 3);
                break;
            case 1059860:
                levelExp = (int) (6 * Math.pow((double)level, 3) / 5 -
                        15 * Math.pow((double)level, 2) + 100 * level - 140);
                break;
            case 1250000:
                levelExp = (int) (5* Math.pow((double)level, 3) / 4);
                break;
            case 1640000:
                if (level <= 15) {
                    levelExp = (int) (Math.pow((double)level, 3) * (24 + ((level+1) / 3) / 50));
                } else if (level >= 16 && level <= 35) {
                    levelExp = (int) (Math.pow((double)level, 3) * (14 + level) / 50);
                } else if (level >= 36 && level <= 100) {
                    levelExp = (int) (Math.pow((double)level, 3) * (32 + (level / 2)) / 50);
                }
                break;
            default:
                break;
        }
        return levelExp;
    }
//    public boolean obtainExp(int addExp) {
//        if (stats.get("Exp") < stats.get("MaxExp")) {
//            for (int i = 0; i < addExp; ++i) {
//                ++exp;
//                if (stats.get("Exp") >= getLevelExperience(level+1)) {
//                    ++level;
//                    assignStat();
//                    //move
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    
    /**
     * 
     * @param wild
     * @param basicExp
     * @param otherLevel
     * @param doFight
     * @param trainerID
     * @return 
     */
    public int calcExp(boolean wild, int basicExp, int otherLevel, boolean doFight, String trainerID) {
        double a = 1, t = 1, e = 1, p = 1, f = 1, v = 1, s = 1;
        int b = basicExp, L = otherLevel;
        
        if (!wild) a = 1.5;
        if (!IDOfTrainer.equals(trainerID)) t = 1.6;
        if (ownItem.getID() == 231) e = 1.5;
        if (stats.get("Happiness") > 100) f = 1.2;
        if (stats.get("Level") > evolutionLevel) v = 1.2;
        if (!doFight) s = 2;
        
        int expCalculate = (int) ((a * t * b * e * L * p * f * v) / (7*s));
        return expCalculate;
    }
    
    /**
     * 
     * @param damage 
     */
    public void takeDamage(int damage) {
        int HP = stats.get("HP");
        if ((HP-damage) > 0) {
            HP -= damage;
        } else {
            HP = 0;
        }
        stats.put("HP", HP);
    }
    
    /**
     * 
     */
    public void increaseRoundBPSN() {
        ++roundBadPSN;
    }
    /**
     * 
     */
    public void decreaseRoundSLP() {
        if (roundSLP > 0)
            --roundSLP;
    }
    /**
     * 
     */
    public void decreaseRoundCNF() {
        if (roundCNF > 0)
            --roundCNF;
    }
    
    private int parseInteger(String i) {
        return Integer.parseInt(i);
    }
    
    private void natureMultiplier(Nature nature) {
        switch (nature) {
            case Adamant:
                natAtk = 1.1; natSpAtk = 0.9; break;
            case Bashful:
                break;
            case Bold:
                natDef = 1.1; natAtk = 0.9; break;
            case Brave:
                natAtk = 1.1; natSpd = 0.9; break;
            case Calm:
                natSpDef = 1.1; natAtk = 0.9; break;
            case Careful:
                natSpDef = 1.1; natSpAtk = 0.9; break;
            case Docile:
                break;
            case Gentle:
                natSpDef = 1.1; natDef = 0.9; break;
            case Hardy:
                break;
            case Hasty:
                natSpd = 1.1; natDef = 0.9; break;
            case Impish:
                natDef = 1.1; natSpAtk = 0.9; break;
            case Jolly:
                natSpd = 1.1; natSpAtk = 0.9; break;
            case Lax:
                natDef = 1.1; natSpDef = 0.9; break;
            case Lonely:
                natAtk = 1.1; natDef = 0.9; break;
            case Mild:
                natSpAtk = 1.1; natDef = 0.9; break;
            case Modest:
                natSpAtk = 1.1; natAtk = 0.9; break;
            case Naive:
                natSpd = 1.1; natSpDef = 0.9; break;
            case Naughty:
                natAtk = 1.1; natSpDef = 0.9; break;
            case Quiet:
                natSpAtk = 1.1; natSpd = 0.9; break;
            case Quirky:
                break;
            case Rash:
                natSpAtk = 1.1; natSpDef = 0.9; break;
            case Relaxed:
                natDef = 1.1; natSpd = 0.9; break;
            case Sassy:
                natSpDef = 1.1; natSpd = 0.9; break;
            case Serious:
                break;
            case Timid:
                natSpd = 1.1; natAtk = 0.9; break;
            default:
                break;
        }
    }
    private void randomNature(int index) {        
        switch (index) {
            case 1:
                nature = Nature.Adamant; break;
            case 2:
                nature = Nature.Bashful; break;
            case 3:
                nature = Nature.Bold; break;
            case 4:
                nature = Nature.Brave; break;
            case 5:
                nature = Nature.Calm; break;
            case 6:
                nature = Nature.Careful; break;
            case 7:
                nature = Nature.Docile; break;
            case 8:
                nature = Nature.Gentle; break;
            case 9:
                nature = Nature.Hardy; break;
            case 10:
                nature = Nature.Hasty; break;
            case 11:
                nature = Nature.Impish; break;
            case 12:
                nature = Nature.Jolly; break;
            case 13:
                nature = Nature.Lax; break;
            case 14:
                nature = Nature.Lonely; break;
            case 15:
                nature = Nature.Mild; break;
            case 16:
                nature = Nature.Modest; break;
            case 17:
                nature = Nature.Naive; break;
            case 18:
                nature = Nature.Naughty; break;
            case 19:
                nature = Nature.Quiet; break;
            case 20:
                nature = Nature.Quirky; break;
            case 21:
                nature = Nature.Rash; break;
            case 22:
                nature = Nature.Relaxed; break;
            case 23:
                nature = Nature.Sassy; break;
            case 24:
                nature = Nature.Serious; break;
            case 25:
                nature = Nature.Timid; break;
            default:
                nature = Nature.Serious; break;
        }
        natureMultiplier(nature);
    }
    private void generateIV() {
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        stats.put("IVHP", random);
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        stats.put("IVAttack", random);
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        stats.put("IVDefense", random);
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        stats.put("IVSPAttack", random);
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        stats.put("IVSPDefense", random);
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        stats.put("IVSpeed", random);
    }
    private Type setType(String type) {
        Type tempType;
        switch (type) {
            case "Normal":
                tempType = Type.Normal; break;
            case "Fighting":
                tempType = Type.Fighting; break;
            case "Flying":
                tempType = Type.Flying; isFlying = true; break;
            case "Rock":
                tempType = Type.Rock; break;
            case "Ground":
                tempType = Type.Ground; break;
            case "Steel":
                tempType = Type.Steel; break;
            case "Ghost":
                tempType = Type.Ghost; break;
            case "Dark":
                tempType = Type.Dark; break;
            case "Psychic":
                tempType = Type.Psychic; break;
            case "Bug":
                tempType = Type.Bug; break;
            case "Poison":
                tempType = Type.Poison; break;
            case "Grass":
                tempType = Type.Grass; break;
            case "Water":
                tempType = Type.Water; break;
            case "Ice":
                tempType = Type.Ice; break;
            case "Fire":
                tempType = Type.Fire; break;
            case "Electric":
                tempType = Type.Electric; break;
            case "Fairy":
                tempType = Type.Fairy; break;
            case "Dragon":
                tempType = Type.Dragon; break;
            case "Dinosaur":
                tempType = Type.Dinosaur; break;
            case "Light":
                tempType = Type.Light; break;
            default:
                tempType = null; break;
        }
        return tempType;
    }
    private Evolution setTypeOfEvolution(String evo) {
        Evolution tempEvo;
        switch (evo) {
            case "Level":
                tempEvo = Evolution.Level; break;
            case "Stone":
                tempEvo = Evolution.Stone; break;
            case "Switch":
                tempEvo = Evolution.Switch; break;
            case "SwitchItem":
                tempEvo = Evolution.SwitchItem; break;
            case "Happiness":
                tempEvo = Evolution.Happiness; break;
            case "LevelWithStats":
                tempEvo = Evolution.LevelWithStats; break;
            case "LevelOnPlace":
                tempEvo = Evolution.LevelInPlace; break;
            case "LevelOnTime":
                tempEvo = Evolution.LevelInTime; break;
            case "LevelOnWeather":
                tempEvo = Evolution.LevelInWeather; break;
            case "LevelWithItem":
                tempEvo = Evolution.LevelWithItem; break;
            case "LevelWithMove":
                tempEvo = Evolution.LevelWithMove; break;
            case "SwitchEachOther":
                tempEvo = Evolution.SwitchEachOther; break;
            case "Eevee":
                tempEvo = Evolution.Eevee; break;
            default:
                tempEvo = Evolution.Level; break;
        }
        return tempEvo;
    }
} 