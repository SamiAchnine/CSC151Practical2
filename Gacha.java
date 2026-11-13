import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gacha: This is a helper class which handles all the randomized 'Gacha' style card pulling. This class only works when a Deck object is initialized, and should not be instantiated otherwise.
 * 
 * @author Charlie Sanchez, Claude
 * @version 1.00
 */
public class Gacha {
    public static final int PULL_COST = 100;

    private static final Map<String, Double> RARITY_WEIGHTS = Map.of(
            "Freshman", 67.67,
            "Sophomore", 25.0,
            "Junior", 5.95,
            "Senior", 1.75,
            "Super Senior", 1.0,
            "Graduate", 0.1
    );

    // one template per card in the file
    private final ArrayList<Cards> pool;                     
    // only tiers that exist
    private final Map<String, Double> activeWeights = new LinkedHashMap<>();

    public Gacha() {
        pool = Deck.getDeck();

        for (Cards c : pool) {
            String rarity = rarityOf(c);
            activeWeights.put(rarity, RARITY_WEIGHTS.getOrDefault(rarity, 1.0));
        }

        if (pool.isEmpty()) {
            throw new IllegalStateException("No cards were loaded, so the gacha has nothing to give.");
        }
    }

    /**
     * Rolls a rarity tier, picks a random card of that tier, and returns a new copy of it.
     * @return Card object which was randomly selected, utilizing the weights in Gacha.java
     * @author Charlie Sanchez, Claude
     */
    public Cards pull() {
        String rarity = rollRarity();

        // collect the positions of every card with that rarity
        ArrayList<Integer> matches = new ArrayList<>();
        for (int i = 0; i < pool.size(); i++) {
            if (rarityOf(pool.get(i)).equals(rarity)) {
                matches.add(i);
            }
        }

        int index = matches.get(ThreadLocalRandom.current().nextInt(matches.size()));

        // Re-reading by index builds a brand new object, 
        // which is bad for memory usage (maybe) but good for ensuring corruption doesn't happen I guess
        return Deck.readCardFromFile(index);
    }

    /**
     * Helper method for Gacha.pull():
     * selects a rarity utilizing the weights from the map defined in Gacha.java, acting as the engine to the gacha system.
     * @return The name of the selected rarity, which is intended to be used as a clamp for the card pull to be a card only of that rarity.
     * @author Charlie Sanchez, Claude
     */
    private String rollRarity() {
        double total = 0;
        for (double weight : activeWeights.values()) {
            total += weight;
        }

        double roll = ThreadLocalRandom.current().nextDouble(total);
        for (Map.Entry<String, Double> entry : activeWeights.entrySet()) {
            roll -= entry.getValue();
            if (roll < 0) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Rarity roll fell through; weights are broken.");
    }

    /**
     * Returns the rarity of the specified card
     * @param card The card object of which rarity must be checked.
     * @return The card's rarity (as a String).
     * @author Charlie Sanchez, Claude
     * @version 1.00
     */
    private static String rarityOf(Cards card) {
        return card.getRarity();
    }
}