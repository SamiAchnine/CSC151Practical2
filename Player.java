import java.util.ArrayList;

public class Player {
    private String name;
    private int health;
    /* private deck deck; */
    /* private arrayList hand */

    public Player() {
    }

    public Player(String name, int health) {
        this.name = name;
        this.health = health;
    }

    /* add method drawing a card */

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

    HumanPlayer(String name, int health) {
        super(name, health);
    }

    public void addRecord(Player opponent) {
        record.add(opponent.isDefeated());
    }

    public ArrayList<Boolean> getRecord() {
        return record;
    }
}

/* cpu player subclass */
