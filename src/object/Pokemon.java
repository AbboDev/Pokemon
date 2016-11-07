package object;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import object.Move.StatsOfAttacks;

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
    
    //HP Stats
    private int IVHP;
    private int EVHP = 0;
    private int basicHP;
    private int maxHP;
    private int HP;
    
    //Attack Stats
    private int IVAttack;
    private int EVAttack = 0;
    private int maxAttack;
    private int basicAttack;
    private int attack;
    
    //Defense Stats
    private int IVDefense;
    private int maxDefense;
    private int EVDefense = 0;
    private int basicDefense;
    private int defense;
    
    //Special Attack Stats
    private int IVSPAttack;
    private int EVSPAttack = 0;
    private int maxSPAttack;
    private int basicSPAttack;
    private int spAttack;
    
    //Special Defense Stats
    private int IVSPDefense;
    private int EVSPDefense = 0;
    private int basicSPDefense;
    private int maxSPDefense;
    private int spDefense;
    
    //Speed Stats
    private int IVSpeed;
    private int EVSpeed = 0;
    private int basicSpeed;
    private int maxSpeed;
    private int speed;
    
    //Hidden Stats
    private int accuracy;
    private int elusion;
    private int happiness;
    private int level = 1;
    private int roundBadPSN;
    private int roundSLP;
    private int roundCNF;
    
    //Exp Stats
    private int maxExp;
    private int nextExp;
    private int basicExp;
    private int exp;
    
    //Maximun Value
    private static final int MAX_IV = 31;
    private static final int MAX_EV = 510;
    private static final int MAX_MOVES = 6;
    private static final int MAX_ACC = 100;
    private static final int MAX_ELU = 0;
    private static final int MAX_HPNS = 0;
    private static final int MAX_LVL = 100;
    private static final int MIN_CRIT = 1;
    private static final int MAX_CRIT = 1;
    private static final int MIN_SLEEP = 1;
    private static final int MAX_SLEEP = 3;
    
    //Nature Stats
    private Nature nature;
    private double natAtk = 1;
    private double natDef = 1;
    private double natSpAtk = 1;
    private double natSpDef = 1;
    private double natSpd = 1;
    
    private int tempCrit;
    
    //Level Stats (only battle)
    private int levelAtk;
    private int levelDef;
    private int levelSpAtk;
    private int levelSpDef;
    private int levelSpd;
    private int levelAcc;
    private int levelEva;
    
    //BOOLEAN
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
    private int ID;
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
    
    private final ClassLoader classLoader = getClass().getClassLoader();
    public static File KANTO;
    public static File KANTO_MOVE;
    public static File JOHTO;
    public static File JOHTO_MOVE;
    public static File MOVES;

    private static String OS = System.getProperty("os.name").toLowerCase();
    
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
        MOVES = new File(classLoader.getResource("res/database/move.csv").getFile());
        KANTO = new File(classLoader.getResource("res/database/kanto.csv").getFile());
        KANTO_MOVE = new File(classLoader.getResource("res/database/kantoMove.csv").getFile());
        JOHTO = new File(classLoader.getResource("res/database/johto.csv").getFile());
        JOHTO_MOVE = new File(classLoader.getResource("res/database/johtoMove.csv").getFile());
    }
    
    //Create a random Pokemon from .CSV
    public Pokemon(int ID, int level, boolean wild, String trainerIDhex, String trainerIDoct) {
        assignFile();
        this.ID = ID;
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
                    name = currentLine[1];
                    surname = name;
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
                    if (level > 100) level = 100;
                    this.level = level;
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
                    basicHP = parseInteger(currentLine[12]);
                    basicAttack = parseInteger(currentLine[13]);
                    basicDefense = parseInteger(currentLine[14]);
                    basicSPAttack = parseInteger(currentLine[15]);
                    basicSPDefense = parseInteger(currentLine[16]);
                    basicSpeed = parseInteger(currentLine[17]);
                    
                    EVHP = parseInteger(currentLine[18]);
                    EVAttack = parseInteger(currentLine[19]);
                    EVDefense = parseInteger(currentLine[20]);
                    EVSPAttack = parseInteger(currentLine[21]);
                    EVSPDefense = parseInteger(currentLine[22]);
                    EVSpeed = parseInteger(currentLine[23]);
                    
                    happiness = parseInteger(currentLine[24]);
                    
                    basicExp = parseInteger(currentLine[25]);
                    maxExp = parseInteger(currentLine[26]);
                    exp = getLevelExperience(this.level);
                    
                    catchRatio = parseInteger(currentLine[28]);
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
    
    public final void assignStat() {
        HP = maxHP = ((IVHP+(2*basicHP)+(EVHP/4))*level/100)+10+level;
        attack = (int) ((((IVAttack+(2*basicAttack)+(EVAttack/4))*level/100)+5)*natAtk);
        defense = (int) ((((IVDefense+(2*basicDefense)+(EVDefense/4))*level/100)+5)*natDef);
        spAttack = (int) ((((IVSPAttack+(2*basicSPAttack)+(EVSPAttack/4))*level/100)+5)*natSpAtk);
        spDefense = (int) ((((IVSPDefense+(2*basicSPDefense)+(EVSPDefense/4))*level/100)+5)*natSpDef);
        speed = (int) ((((IVSpeed+(2*basicSpeed)+(EVSpeed/4))*level/100)+5)*natSpd);
        tempCrit = MIN_CRIT;
        levelAtk = 0;
        levelDef = 0;
        levelSpAtk = 0;
        levelSpDef = 0;
        levelSpd = 0;
    }
    public final void assignMove(File csvMoves) throws IOException {
        String line, split = ";", splitMove = "#";
        BufferedReader bufferMove = null;
        
        bufferMove = new BufferedReader(new FileReader(csvMoves));
        while ((line = bufferMove.readLine()) != null) {
            String[] currentLine = line.split(split);
            if (currentLine[0].equals(String.valueOf(ID))) {
                for (int i = currentLine.length; i > 1; --i) {
                    allMoves.add(currentLine[i-1]);
                }
            }
        }
        for (int y = 0; y < allMoves.size(); ++y) {
            String[] currentMove = allMoves.get(y).split(splitMove);
            if (parseInteger(currentMove[1]) <= this.getLevel()) {
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
     * Increase or decrease the int stat
     * Attack, Defense, Sp Attack, Sp Defense, Speed
     * @param stat
     * @return 
     */
    private int modBaseStats(int stat, int statsLevel) {
        int newStat;
        switch (statsLevel) {
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
    private int modOtherStats(int stat, int statLevel) {
        int newStat;
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
    
    public void giveItem(BagItem item) { ownItem = item; }
    
    public int getIVHP() { return IVHP; }
    public int getEVHP() { return EVHP; }
    public int getMaxHP() { return maxHP; }
    public int getHP() { return HP; }

    public int getIVAttack() { return IVAttack; }
    public int getEVAttack() { return EVAttack; }
    public int getMaxAttack() { return maxAttack; }
    public int getAttack() { return attack; }

    public int getIVDefense() { return IVDefense; }
    public int getEVDefense() { return EVDefense; }
    public int getMaxDefense() { return maxDefense; }
    public int getDefense() { return defense; }

    public int getIVSPAttack() { return IVSPAttack; }
    public int getEVSPAttack() { return EVSPAttack; }
    public int getMaxSPAttack() { return maxSPAttack; }
    public int getSpAttack() { return spAttack; }
    
    public int getIVSPDefense() { return IVSPDefense; }
    public int getEVSPDefense() { return EVSPDefense; }
    public int getMaxSPDefense() { return maxSPDefense; }
    public int getSpDefense() { return spDefense; }
    
    public int getIVSpeed() { return IVSpeed; }
    public int getEVSpeed() { return EVSpeed; }
    public int getMaxSpeed() { return maxSpeed; }
    public int getSpeed() { return speed; }
    
    public int getAccuracy() { return accuracy;}
    public int getElusion() { return elusion; }
    public int getHappiness() { return happiness; }
    public int getLevel() { return level; }
    public int getMaxExp() { return maxExp; }
    public int getNextExp() { return nextExp; }
    public int getExp() { return exp; }

    public static int getMaxMoves() { return MAX_MOVES; }
    public static int getMaxAccuracy() { return MAX_ACC; }
    public static int getMaxElusion() { return MAX_ELU; }
    public static int getMaxHappiness() { return MAX_HPNS; }
    public static int getMaxLevel() { return MAX_LVL; }
    public static int getMaxIV() { return MAX_IV; }
    public static int getMaxEV() { return MAX_EV; }
    public static int getMinCrit() { return MIN_CRIT; }
    public static int getMaxCrit() { return MAX_CRIT; }
    
    public int getBasicHP() { return basicHP; }
    public int getBasicAttack() { return basicAttack; }
    public int getBasicDefense() { return basicDefense; }
    public int getBasicSPAttack() { return basicSPAttack; }
    public int getBasicSPDefense() { return basicSPDefense; }
    public int getBasicSpeed() { return basicSpeed; }
    public int getBasicExp() { return basicExp; }
    
    public double getNatAtk() { return natAtk; }
    public double getNatDef() { return natDef; }
    public double getNatSpAtk() { return natSpAtk; }
    public double getNatSpDef() { return natSpDef; }
    public double getNatSpd() { return natSpd; }

    public int getTempAtk() { return modBaseStats(attack, levelAtk); }
    public int getTempDef() { return modBaseStats(defense, levelDef); }
    public int getTempSpAtk() { return modBaseStats(spAttack, levelSpAtk); }
    public int getTempSpDef() { return modBaseStats(spDefense, levelSpDef); }
    public int getTempSpd() { return modBaseStats(speed, levelSpd); }
    public int getTempAcc() { return modOtherStats(accuracy, levelSpd); }
    public int getTempEva() { return modOtherStats(elusion, levelSpd); }
    public int getTempCrit() { return tempCrit; }
    
    public int getLevelAtk() { return levelAtk; }
    public int getLevelDef() { return levelDef; }
    public int getLevelSpAtk() { return levelSpAtk; }
    public int getLevelSpDef() { return levelSpDef; }
    public int getLevelSpd() { return levelSpd; }
    public int getLevelAcc() { return levelAcc; }
    public int getLevelEva() { return levelEva; }
    public int setLevelAtk(int level) {
        if (levelAtk > -6 && levelAtk < 6) {
            if ((levelAtk+level) < -6) levelAtk = -6;
            else if ((levelAtk+level) > 6) levelAtk = 6;
            else levelAtk += level;
        }
        return levelAtk;
    }
    public int setLevelDef(int level) { 
        if (levelDef > -6 && levelDef < 6) {
            if ((levelDef+level) < -6) levelDef = -6;
            else if ((levelDef+level) > 6) levelDef = 6;
            else levelDef += level;
        }
        return levelDef;
    }
    public int setLevelSpAtk(int level) {
        if (levelSpAtk > -6 && levelSpAtk < 6) {
            if ((levelSpAtk+level) < -6) levelSpAtk = -6;
            else if ((levelSpAtk+level) > 6) levelSpAtk = 6;
            else levelSpAtk += level;
        }
        return levelSpAtk;
    }
    public int setLevelSpDef(int level) {
        if (levelSpDef > -6 && levelSpDef < 6) {
            if ((levelSpDef+level) < -6) levelSpDef = -6;
            else if ((levelSpDef+level) > 6) levelSpDef = 6;
            else  levelSpDef += level;
        }
        return levelSpDef;
    }
    public int setLevelSpd(int level) {
        if (levelSpd > -6 && levelSpd < 6) {
            if ((levelSpd+level) < -6) levelSpd = -6;
            else if ((levelSpd+level) > 6) levelSpd = 6;
            else levelSpd += level;
        }
        return levelSpd;
    }
    public int setLevelAcc(int level) {
        if (levelAcc > -6 && levelAcc < 6) {
            if ((levelAcc+level) < -6) levelAcc = -6;
            else if ((levelAcc+level) > 6) levelAcc = 6;
            else levelAcc += level;
        }
        return levelAcc;
    }
    public int setLevelEva(int level) {
        if (levelEva > -6 && levelEva < 6) {
            if ((levelEva+level) < -6) levelEva = -6;
            else if ((levelEva+level) > 6) levelEva = 6;
            else levelEva += level;
        }
        return levelEva;
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

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getNameOfTrainer() { return nameOfTrainer; }
    public int getID() { return ID; }
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
    
    public ArrayList<Move> getMoveSet() { return moveSet; }
    public ArrayList<String> getAllMoves() { return allMoves; }
    public boolean getIfContainsMove(Move newMove) {
        for (Move move : moveSet) {
            if (newMove.getName().equals(moveSet.get(moveSet.indexOf(move)).getName())) {
                return true;
            }
        }
        return false;
    }
    public int getNumberOfMoves() { return moveSet.size(); }
    public Move getRandomMove(boolean morePP, int usedPP) {
        if (useStruggle()) return new Move("Struggle");
        random = rand.nextInt((moveSet.size() - 1) + 1) + 1;
        while (moveSet.get(random-1).getPP() <= 0) {
            random = rand.nextInt((moveSet.size() - 1) + 1) + 1;
        }
        moveSet.get(random-1).decreasePP(morePP, usedPP);
        return moveSet.get(random-1);
    }
    public boolean useStruggle() {
        for (int i = 0; i < moveSet.size(); ++i) {
            if (moveSet.get(i).getPP() > 0) return false;
        }
        return true;
    }
    public String getNameOfMoves(int index) { return moveSet.get(index).toString(); }
    public Move getMoves(String name) {
        if (moveSet.contains(new Move(name))) {
            return moveSet.get(moveSet.indexOf(name));
        } else {
            return null;
        }
    }
    
    public void setStatus(Move move) {
        switch (move.getStatus()) {
            case BadPoisoned:
                if ((this.firstType != Type.Poison && this.firstType != Type.Steel) &&
                        (this.secondType != Type.Poison && this.secondType != Type.Steel))
                    status = Status.BadPoison; System.out.println(move.getStatus()); break;
            case Burn:
                if (this.firstType != Type.Fire && this.secondType != Type.Fire)
                    status = Status.Burn; System.out.println(move.getStatus()); break;
            case Freeze:
                if (this.firstType != Type.Ice && this.secondType != Type.Ice)
                    status = Status.Freeze; System.out.println(move.getStatus()); break;
            case Paralysis:
                if (this.firstType != Type.Electric && this.secondType != Type.Electric)
                    status = Status.Paralysis; System.out.println(move.getStatus()); break;
            case Poison:
                if ((this.firstType != Type.Poison && this.firstType != Type.Steel) &&
                        (this.secondType != Type.Poison && this.secondType != Type.Steel))
                    status = Status.Poison; System.out.println(move.getStatus()); break;
            case Sleep:
                status = Status.Asleep; random = rand.nextInt((MAX_SLEEP - MIN_SLEEP) + 1) + MIN_SLEEP;
                roundSLP = random; System.out.println(move.getStatus());break;
            default: break;
        }
    }
    public void setTempStats(Move move) {
        for (int i = 0; i < move.getStats().length-1; ++i) {
            StatsOfAttacks stat = move.getStats()[i];
            if (stat != null) {
                switch (stat) {
                    case Atk: setLevelAtk(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelAtk); break;
                    case Def: setLevelDef(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelDef); break;
                    case SpAtk: setLevelSpAtk(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelSpAtk); break;
                    case SpDef: setLevelSpDef(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelSpDef); break;
                    case Spd: setLevelSpd(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelSpd); break;
                    case Acc: setLevelAcc(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelAcc); break;
                    case Eva: setLevelEva(move.getStatsLevel()[i]);
                        System.out.println(stat+": "+levelEva); break;
        //            case Critical: tempAtk = move.modBaseStats(tempAtk); break;
                    default: break;
                }
            }
        }
    }
    public void resetCritical() {
        tempCrit = MIN_CRIT;
    }
    public void defeat() {
        status = Status.KO;
    }
    
    public ImageIcon getSprite(String path, int width, int height, boolean mirror, boolean sex) {
        String imagePath = getImagePath(path, sex);
        BufferedImage image = loadImage(imagePath, path);
        ImageIcon imageIcon;
        if (!mirror) {
            imageIcon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } else {
            imageIcon = new MirrorImageIcon(new ImageIcon(image).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        return imageIcon;
    }
    private String getImagePath(String sprite, boolean sex) {
        String path = "/";
        if (this.getIfShiny()) {
            boolean checkS;
            checkS = new File(sprite+"shiny/"+ID+".png").exists();
            if (checkS) {
                path += "shiny/";
            } 
        }
        if (sex) {
            if (!this.getIfMale()) {
                boolean checkF;
                File f = new File(sprite+path+"female/"+ID+".png");
                checkF = f.exists();
                if (checkF) {
                    path += "female/";
                }
            }
        }
        String image = sprite + path + ID + ".png";
        return image;
    }
    private BufferedImage loadImage(String path, String sprite) {
        BufferedImage buff;
        try {
            buff = ImageIO.read(classLoader.getResourceAsStream(path));
        } catch (IOException e) {
            try {
                String newPath = getImagePath(sprite, false);
                buff = ImageIO.read(classLoader.getResourceAsStream(newPath));
            } catch (Exception ex) {
                return null;
            }
        }
        return buff;
    }
    
    public int getNextLevelExperience() {
        int nextLevelExp = getLevelExperience(level+1);
        
        nextExp = nextLevelExp - exp;
        return nextExp;
    }
    public final int getLevelExperience(int level) {
        int levelExp = 0;
        switch (maxExp) {
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
    public boolean obtainExp(int addExp) {
        if (this.exp < maxExp) {
            for (int i = 0; i < addExp; ++i) {
                ++exp;
                if (exp >= getLevelExperience(level+1)) {
                    ++level;
                    assignStat();
                    //move
                    return true;
                }
            }
        }
        return false;
    }
    public int calcExp(boolean wild, int basicExp, int otherLevel, boolean doFight, String trainerID) {
        double a = 1, t = 1, e = 1, p = 1, f = 1, v = 1, s = 1;
        int b = basicExp, L = otherLevel;
        
        if (!wild) a = 1.5;
        if (!IDOfTrainer.equals(trainerID)) t = 1.6;
        if (ownItem.getID() == 231) e = 1.5;
        if (happiness > 100) f = 1.2;
        if (level > evolutionLevel) v = 1.2;
        if (!doFight) s = 2;
        
        int expCalculate = (int) ((a * t * b * e * L * p * f * v) / (7*s));
        return expCalculate;
    }
        
    public void takeDamage(int damage) {
        if ((HP-damage) > 0) {
            HP -= damage;
        } else {
            HP = 0;
        }
    }
    
    public void increaseRoundBPSN() {
        ++roundBadPSN;
    }
    public void decreaseRoundSLP() {
        if (roundSLP > 0)
            --roundSLP;
    }
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
        IVHP = random;
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        IVAttack = random;
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        IVDefense = random;
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        IVSPAttack = random;
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        IVSPDefense = random;
        random = rand.nextInt((MAX_IV - 1) + 1) + 1;
        IVSpeed = random;
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
/**
 * This class return an instance of image
 * which is horizontaly flip
 * http://stackoverflow.com/questions/1708011/create-a-imageicon-that-is-the-mirror-of-another-one
 */
@SuppressWarnings("serial")
class MirrorImageIcon extends ImageIcon {

    public MirrorImageIcon(Image filename) {
    	super(filename);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
    	Graphics2D g2 = (Graphics2D)g.create();
    	g2.translate(getIconWidth(), 0);
    	g2.scale(-1, 1);
    	super.paintIcon(c, g2, -x, y);
    }

}