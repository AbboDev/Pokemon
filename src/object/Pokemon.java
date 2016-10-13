package object;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.ImageIcon;
import object.Move.StatsOfAttacks;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
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
        Freeze, Asleep, BadPoison, Confused
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
    
    //Exp Stats
    private int maxExp;
    private int nextExp;
    private int basicExp;
    private int exp;
    
    //Maximun Value
    private static final int maxIV = 31;
    private static final int maxEV = 510;
    private static final int maxMoves = 6;
    private static final int maxAccuracy = 100;
    private static final int maxElusion = 0;
    private static final int maxHappiness = 0;
    private static final int maxLevel = 100;
    private static final int minCrit = 1;
    private static final int maxCrit = 1;
    
    //Nature Stats
    private Nature nature;
    private double natAtk = 1;
    private double natDef = 1;
    private double natSpAtk = 1;
    private double natSpDef = 1;
    private double natSpd = 1;
    
    //Temp Stats (only battle)
//    private int tempAtk;
//    private int tempDef;
//    private int tempSpAtk;
//    private int tempSpDef;
//    private int tempSpd;
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
    private boolean isMale;
    
    private BagItem ownItem;
    private Ability ability;
    private ArrayList<Move> moveSet = new ArrayList<>();
    private BidiMap<String, Integer> moveLevel = new DualHashBidiMap<>();
    
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
    
    private static final File moves = new File(System.getProperty("user.dir")+"\\src\\pokemon\\move.csv");
    
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
    }
    
    //Create a random Pokemon from .CSV
    public Pokemon(File csvStatsRegion, File csvMovesRegion, int ID, int level,
            boolean wild, String trainerIDhex, String trainerIDoct) {
        String pathStat = csvStatsRegion.getPath();
        BufferedReader bufferStats = null;
        String line, split = ";", splitMultiple = "$";
        try {
            bufferStats = new BufferedReader(new FileReader(pathStat));
            while ((line = bufferStats.readLine()) != null ) {
                String[] currentLine = line.split(split);
                if (currentLine[0].equals(String.valueOf(ID))) {
                    this.ID = parseInteger(currentLine[0]);
                    name = currentLine[1];
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
                    
                    genderRatio = Double.parseDouble(currentLine[27]);
                    catchRatio = parseInteger(currentLine[28]);
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
                    if (trainerIDhex != null && trainerIDoct != null) {
                        IDOfTrainer = trainerIDoct;
                        secretIDOfTrainer = trainerIDoct;
                    } else {
                        IDOfTrainer = secretIDOfTrainer = null;
                    }
                    
                    random = rand.nextInt((25 - 1) + 1) + 1;
                    randomNature(random);
                    generateIV();
                    assignStat();
                    assignMove(csvMovesRegion);
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
        tempCrit = minCrit;
        levelAtk = 0;
        levelDef = 0;
        levelSpAtk = 0;
        levelSpDef = 0;
        levelSpd = 0;
    }
    public final void assignMove(File csvMovesRegion) {
        String pathMove = csvMovesRegion.getPath();
        BufferedReader bufferMove = null;
        String line, split = ";", splitMove = "#";
        try {
            BidiMap<String, Integer> unsortMap = new DualHashBidiMap<>();
            bufferMove = new BufferedReader(new FileReader(pathMove));
            while ((line = bufferMove.readLine()) != null ) {
                String[] currentLine = line.split(split);
                if (currentLine[0].equals(String.valueOf(ID))) {
                    for (int i = 1; i < currentLine.length; ++i) {
                        String[] word = currentLine[i].split(splitMove);
                        String move = word[0];
                        Integer levelOfMove = parseInteger(word[1]);
                        unsortMap.put(move, levelOfMove);
                    }
                }
            }
            moveLevel = sortByValue(unsortMap);
            ArrayList<Integer> move = new ArrayList<>(moveLevel.values());
            for(int i = move.size()-1; i >= 0; --i){
                Integer integer = move.get(i);
                if (moveSet.size() < maxMoves) {
                    if (integer <= this.level && !moveSet.contains(moveLevel.getKey(integer))) {
                        moveSet.add(0, new Move(moves, moveLevel.getKey(integer)));
                    }
                } else { break; }
            }
        } catch (FileNotFoundException ex) {} catch (IOException ex) {}
        finally {
            if (bufferMove != null) {
                try { bufferMove.close(); } catch (IOException e) {}
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
        switch (statsLevel) {
            case -6: stat *= 2/8; break;
            case -5: stat *= 2/7; break;
            case -4: stat *= 2/6; break;
            case -3: stat *= 2/5; break;
            case -2: stat *= 2/4; break;
            case -1: stat *= 2/3; break;
            case 0: stat *= 1; break;
            case 1: stat *= 3/2; break;
            case 2: stat *= 2; break;
            case 3: stat *= 5/2; break;
            case 4: stat *= 3; break;
            case 5: stat *= 7/2; break;
            case 6: stat *= 4; break;
            default: stat *= 1; break;
        }
        return stat;
    }
    /**
     * Increase or decrease the int stat
     * Accuracy, Evasion
     * @param stat
     * @return 
     */
    private int modOtherStats(int stat, int statLevel) {
        switch (statLevel) {
            case -6: stat *= 3/9; break;
            case -5: stat *= 3/8; break;
            case -4: stat *= 3/7; break;
            case -3: stat *= 3/6; break;
            case -2: stat *= 3/5; break;
            case -1: stat *= 3/4; break;
            case 0: stat *= 1; break;
            case 1: stat *= 4/3; break;
            case 2: stat *= 5/3; break;
            case 3: stat *= 2; break;
            case 4: stat *= 7/3; break;
            case 5: stat *= 8/3; break;
            case 6: stat *= 3; break;
            default: stat *= 1; break;
        }
        return stat;
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

    public static int getMaxMoves() { return maxMoves; }
    public static int getMaxAccuracy() { return maxAccuracy; }
    public static int getMaxElusion() { return maxElusion; }
    public static int getMaxHappiness() { return maxHappiness; }
    public static int getMaxLevel() { return maxLevel; }
    public static int getMaxIV() { return maxIV; }
    public static int getMaxEV() { return maxEV; }
    public static int getMinCrit() { return minCrit; }
    public static int getMaxCrit() { return maxCrit; }
    
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
    public int getTempAcc() { return modBaseStats(accuracy, levelSpd); }
    public int getTempEva() { return modBaseStats(elusion, levelSpd); }
    public int getTempCrit() { return tempCrit; }
    
    public int getLevelAtk() { return levelAtk; }
    public int getLevelDef() { return levelDef; }
    public int getLevelSpAtk() { return levelSpAtk; }
    public int getLevelSpDef() { return levelSpDef; }
    public int getLevelSpd() { return levelSpd; }
    public int getLevelAcc() { return levelAcc; }
    public int getLevelEva() { return levelEva; }
    public int setLevelAtk(int level) { return levelAtk += level; }
    public int setLevelDef(int level) { return levelDef += level; }
    public int setLevelSpAtk(int level) { return levelSpAtk += level; }
    public int setLevelSpDef(int level) { return levelSpDef += level; }
    public int setLevelSpd(int level) { return levelSpd += level; }
    public int setLevelAcc(int level) { return levelAcc += level; }
    public int setLevelEva(int level) { return levelEva += level; }

    public Type getFirstType() { return firstType; }
    public Type getSecondType() { return secondType; }

    public boolean getIfPowered() { return isPowered; }
    public boolean getIfShiny() { return isShiny; }
    public boolean getIfPokerus() { return hasPokerus; }
    public boolean getIfMale() { return isMale; }
    public boolean getIfFlying() { return isFlying; }
    public boolean getIfDig() { return isDig; }
    public boolean getIfSub() { return isSub; }
    
    public double getGenderRatio() { return genderRatio; }
    public int getCatchRatio() { return catchRatio; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status sts) { status = sts; }
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
    
    public String getNextPokemon() { return nextPokemon; }
    public Evolution getEvolution() { return evolution; }
    public int getEvolutionLevel() { return evolutionLevel; }
    public String getEvolutionWay() { return evolutionWay; }
    public String getNextPokemon2() { return nextPokemon2; }
    public Evolution getEvolution2() { return evolution2; }
    public int getEvolutionLevel2() { return evolutionLevel2; }
    public String getEvolutionWay2() { return evolutionWay2; }
    
    public Map<String, Integer> getMoveLevel() { return moveLevel; }
    public ArrayList<Move> getMoveSet() { return moveSet; }
    public int getNumberOfMoves() { return moveSet.size(); }
    public Move getRandomMove(boolean morePP, int usedPP) {
        if (useStruggle()) return new Move(moves, "Struggle");
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
        if (moveSet.contains(name)) {
            return moveSet.get(moveSet.indexOf(name));
        } else {
            return null;
        }
    }
    
    public void setStatus(Move move) {
        switch (move.getStatus()) {
            case BadPoisoned: status = Status.BadPoison; break;
            case Burn: status = Status.Burn; break;
            case Freeze: status = Status.Freeze; break;
            case Paralysis: status = Status.Paralysis; break;
            case Poison: status = Status.Poison; break;
            case Sleep: status = Status.Asleep; break;
            default: break;
        }
    }
    public void setTempStats(Move move) {
//        for (StatsOfAttacks stat: move.getStats()) {
        for (int i = 0; i < move.getStats().length-1; ++i) {
            StatsOfAttacks stat = move.getStats()[i];
            if (stat != null) {
                switch (stat) {
                    case Atk: levelAtk += move.getStatsLevel()[i]; break;
                    case Def: levelDef += move.getStatsLevel()[i]; break;
                    case SpAtk: levelSpAtk += move.getStatsLevel()[i]; break;
                    case SpDef: levelSpDef += move.getStatsLevel()[i]; break;
                    case Spd: levelSpd += move.getStatsLevel()[i]; break;
                    case Acc: levelAcc += move.getStatsLevel()[i]; break;
                    case Eva: levelEva += move.getStatsLevel()[i]; break;
        //            case Critical: tempAtk = move.modBaseStats(tempAtk); break;
                    default: break;
                }
                System.out.println("");
            }
        }
    }
    public void resetCritical() {
        tempCrit = minCrit;
    }
    
    public ImageIcon getImage(String path) {
        String image = getImagePath(path);
        System.out.println(image);
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH));
        return imageIcon;
    }
    public ImageIcon getImageMirrored(String path) {
        String image = getImagePath(path);
        System.out.println(image);
        ImageIcon imageIcon = new MirrorImageIcon(new ImageIcon(image).getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH));
        return imageIcon;
    }
    public String getImagePath(String sprite) {
        String path = "";
        if (this.getIfShiny()) {
            path += "shiny\\"; 
        }
        if (!this.getIfMale()) {
            boolean check = new File(sprite+"female\\"+ID+".png").exists();
            if (check)
                path += "female\\";
        }
        String image = sprite + path + ID + ".png";
        return image;
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
        random = rand.nextInt((maxIV - 1) + 1) + 1;
        IVHP = random;
        random = rand.nextInt((maxIV - 1) + 1) + 1;
        IVAttack = random;
        random = rand.nextInt((maxIV - 1) + 1) + 1;
        IVDefense = random;
        random = rand.nextInt((maxIV - 1) + 1) + 1;
        IVSPAttack = random;
        random = rand.nextInt((maxIV - 1) + 1) + 1;
        IVSPDefense = random;
        random = rand.nextInt((maxIV - 1) + 1) + 1;
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
    
    //Thanks to https://www.mkyong.com/java/how-to-sort-a-map-in-java/
    private static BidiMap<String, Integer> sortByValue(BidiMap<String, Integer> unsortMap) {
        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        BidiMap<String, Integer> sortedMap = new DualLinkedHashBidiMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
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