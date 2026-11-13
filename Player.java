import java.util.ArrayList;
import java.util.Collections;

public class Player {
    private String name;
    private int health;
    private ArrayList<Cards> collection;
    private ArrayList<Cards> hand;

    public Player() {
    }

    public Player(String name, int health, ArrayList<Cards> collection) {
        this.name = name;
        this.health = health;
        this.collection = collection;
    }

    public void setCollection(ArrayList<Cards> collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }
 
    public int getHealth() {
        return health;
    }
 
    public void resetHealth(int health) {
        this.health = health;
    }
 
    public ArrayList<Cards> getHand() {
        return hand;
    }

    protected void setHand(ArrayList<Cards> newHand) {
        this.hand = newHand;
    }

    public void attack(Player victim, int atkDmg) {
        victim.takeDamage(atkDmg);
    }

    public void drawCardsIntoHand() {
        ArrayList<Cards> collectionCP = new ArrayList<>(this.collection);
        Collections.shuffle(collectionCP);
        ArrayList<Cards> tempHand = new ArrayList<>();

        for (int i = 0; i < collectionCP.size(); i++) {
            tempHand.add(collectionCP.remove(0));
        }

        this.hand = tempHand;
    }


    public void takeDamage(int battle_damage) {
        health -= battle_damage;
    }

    public boolean isDefeated() {
        return health  <= 0;
    }


}

/* human player subclass */
class HumanPlayer extends Player{
    private ArrayList<Boolean> record = new ArrayList<Boolean>();

    HumanPlayer(String name, int health, ArrayList<Cards> collection) {
        super(name, health, collection);
    }

    public void addRecord(Player opponent) {
        record.add(opponent.isDefeated());
    }

    public ArrayList<Boolean> getRecord() {
        return record;
    }
}

/* cpu player subclass */
class CPUPlayer extends Player {

    CPUPlayer(String name, int health) {
        super(name, health, new ArrayList<>());
    }

    public void drawCardIntoHand(ArrayList<Cards> allCards) {
        ArrayList<Cards> newHand = new ArrayList<>();

        ArrayList<Cards> shuffled = new ArrayList<>(allCards);
        Collections.shuffle(shuffled);

        // CPU draws up to 5 random cards.
        int handSize = Math.min(5, shuffled.size());

        for (int i = 0; i < handSize; i++) {
            newHand.add(shuffled.get(i));
        }

        setHand(newHand);
    }
}