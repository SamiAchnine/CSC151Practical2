import java.util.ArrayList;
import java.util.Collections;

/**
 * Player: The main object which handles common entries between CPU Players and Human players, such as the name, health, hand, etc.
 * @author Charlie Sanchez, Sami Achnine, Claude
 */
public class Player {
    private String name;
    private int health;
    private ArrayList<Cards> collection;
    private ArrayList<Cards> hand;

    /**
     * Player constructor
     * @param name A string which is used when referring to the player in battles, etc.
     * @param health The initial health level of the player
     * @param collection The cards which the player currently posseses
     */
    public Player(String name, int health, ArrayList<Cards> collection) {
        this.name = name;
        this.health = health;
        this.collection = collection;
    }

    /**
     * Sets the player's card collection to the provided card collection
     * @param collection The collection which you want to set the player's collection to.
     */
    public void setCollection(ArrayList<Cards> collection) {
        this.collection = collection;
    }

    /**
     * Getter for player's name
     * @return name - String
     */
    public String getName() {
        return name;
    }
 
    /**
     * Getter for player's health level
     * @return health - int
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Sets the health to the provided value
     * @param health The value which you want to set the player's health to
     */
    public void resetHealth(int health) {
        this.health = health;
    }
    
    /**
     * Getter for the player's hand
     * @return Player's hand - ArrayList<Cards>
     */
    public ArrayList<Cards> getHand() {
        return hand;
    }

    /**
     * Setter for the player's hand - overwrites completely, does not retain the original hand
     * @param newHand The hand that will overwrite the existing player's hand
     */
    protected void setHand(ArrayList<Cards> newHand) {
        this.hand = newHand;
    }

    /**
     * Method which attacks the victim player
     * @param victim The player to attack
     * @param atkDmg The amount to damage the other player by
     */
    public void attack(Player victim, int atkDmg) {
        victim.takeDamage(atkDmg);
    }

    /**
     * Method which makes a copy of the card stash, and puts Battle.HAND_SIZE's worth of random cards from it into the player's hand.
     */
    public void drawCardsIntoHand() {
        ArrayList<Cards> stash = new ArrayList<Cards>(this.collection);
        Collections.shuffle(stash);

        ArrayList<Cards> newHand = new ArrayList<>();

        for (int i = 0; i < Battle.HAND_SIZE; i++) {
            newHand.add(stash.get(i));
        }

        setHand(newHand);
    }

    /**
     * Reduces the HP by the specified amount
     * @param battle_damage The specified amount - int
     */
    public void takeDamage(int battle_damage) {
        health -= battle_damage;
    }

    /**
     * Returns a boolean value if the player's HP is below 0, indicating they're "dead"
     * @return the boolean value which is returned - boolean
     */
    public boolean isDefeated() {
        return health <= 0;
    }


}

/**
 * HumanPlayer: Subclass of player which handles things which are unique to human players, such as having a win/loss record
 * @author Charlie Sanchez, Claude, Sami Achnine
 * @version 1.00
 */
class HumanPlayer extends Player{
    private ArrayList<Boolean> record = new ArrayList<Boolean>();

    /**
     * Constructor for HumanPlayer, which is the same as the super constructor
     * @param name Player's name
     * @param health Player's health
     * @param collection Player's card collection
     */
    HumanPlayer(String name, int health, ArrayList<Cards> collection) {
        super(name, health, collection);
    }

    /**
     * Adds win or loss record to the player's record
     * @param opponent The opponent which the player beat, if true then they won, if false then the player lost.
     */
    public void addRecord(Player opponent) {
        record.add(opponent.isDefeated());
    }

    /**
     * Getter for record as an ArrayList of booleans
     * @return The record, ArrayList of booleans
     */
    public ArrayList<Boolean> getRecord() {
        return record;
    }

    /**
     * Gets the record, and returns it as a percent to get saved in the save file.
     * @return The record value, if the record is empty, the value is 0.0. - double
     */
    public double getRecordPercent() {
        int count = 0;
        for (int i = 0; i < record.size(); i++) {
            if (record.get(i)) {
                count++;
            }
        }
        return record.isEmpty() ? 0.0 : (double)count / record.size() * 100;
    }
}

/**
 * CPUPlayer: Subclass of player which has a special draw card into hand method that pulls straight from the gacha
 * @author Charlie Sanchez, Sami Achnine
 * @version 1.00
 */
class CPUPlayer extends Player {

    /**
     * CPUPlayer constructor. Passes an empty collection into its parent class, but is otherwise identical to the superclass constructor
     * @param name The CPU's name, is always Charles P. Underwood, which, when abbreviated, is CPU. I'm so clever.
     * @param health The CPU's default health.
     */
    CPUPlayer(String name, int health) {
        super(name, health, new ArrayList<>());
    }
    
    /**
     * Special draw card into hand method, which pulls cards straight from the gacha.
     * @param allCards The deck which has all the cards.
     */
    public void drawCardIntoHand(ArrayList<Cards> allCards) {
        ArrayList<Cards> newHand = new ArrayList<>();
        Gacha gacha = new Gacha();

        for (int i = 0; i < Battle.HAND_SIZE; i++) {
            newHand.add(gacha.pull());
        }

        setHand(newHand);
    }
}