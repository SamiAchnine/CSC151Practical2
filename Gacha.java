/* this part is ALL vibe coded, Ms. Keller gave us the OK */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Gacha {
    public static final int PULL_COST = 100;

    // Rarity number (from cards.wecib) -> relative chance of being rolled.
    // Assumes 1 = common ... 4 = legendary. Change to match your file.
    private static final Map<Integer, Double> RARITY_WEIGHTS = Map.of(
            1, 67.67,
            2, 25.0,
            3, 5.95,
            4, 1.75,
            5, 0.1
    );

    private final ArrayList<Cards> pool;                      // one template per card in the file
    private final Map<Integer, Double> activeWeights = new LinkedHashMap<>(); // only tiers that exist

    public Gacha() {
        pool = Deck.getDeck();

        for (Cards c : pool) {
            int rarity = rarityOf(c);
            activeWeights.put(rarity, RARITY_WEIGHTS.getOrDefault(rarity, 1.0));
        }

        if (pool.isEmpty()) {
            throw new IllegalStateException("No cards were loaded, so the gacha has nothing to give.");
        }
    }

    /** Rolls a rarity tier, picks a random card of that tier, and returns a NEW copy of it. */
    public Cards pull() {
        int rarity = rollRarity();

        // collect the positions of every card with that rarity
        ArrayList<Integer> matches = new ArrayList<>();
        for (int i = 0; i < pool.size(); i++) {
            if (rarityOf(pool.get(i)) == rarity) {
                matches.add(i);
            }
        }

        int index = matches.get(ThreadLocalRandom.current().nextInt(matches.size()));

        // Re-reading by index builds a brand new object, so two copies of the
        // same card never share one instance (important for per-copy random stats).
        return Deck.readCardFromFile(index);
    }

    private int rollRarity() {
        int total = 0;
        for (double weight : activeWeights.values()) {
            total += weight;
        }

        int roll = ThreadLocalRandom.current().nextInt(total);
        for (Map.Entry<Integer, Double> entry : activeWeights.entrySet()) {
            roll -= entry.getValue();
            if (roll < 0) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Rarity roll fell through; weights are broken.");
    }

    private static int rarityOf(Cards card) {
        return Integer.parseInt(card.getRarity());
    }
}