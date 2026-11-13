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

    public void drawCardsIntoHand(ArrayList<Cards> collection) {
        Collections.shuffle(collection);
        ArrayList<Cards> tempHand = new ArrayList<>();

        for (int i = 0; i < collection.size(); i++) {
            tempHand.add(collection.remove(0));
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
