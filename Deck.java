import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Deck: This class returns an ArrayList of Cards called deck, and should only be called in a static context.
 * @author Sami Achnine
 * @version 1.00
 */
public class Deck {

    /**
     * Reads from a file in the same directory called cards.wecib, and adds them to an ArrayList called deck. 
     * This deck is used for all gacha related card-drawing logic.
     * This method also makes use of other helper methods within the Deck.java file. These helper methods are private 
     * and should NEVER be called outside of this method.
     * 
     * 
     * @author Sami Achnine
     * @return ArrayList of Cards
     * @version 1.00
     */
    public static ArrayList getDeck() {
        ArrayList<Cards> deck = new ArrayList<>();
        
        int i = 0;
        Cards card;
        while ((card = readCardFromFile(i)) != null) {
            deck.add(card);
            i++;
        }

        return deck;
    }

    /**
     * This method gets a line, splits it at the ":", and returns the second half, 
     * effectively reading the value from a key-value pair in the cards.wecib file.
     * 
     * @param line
     * @return Value from line (after ":").
     * @throws RuntimeException
     * @author Sami Achnine
     * @version 1.00
     */
    private static String parseDataFromWECIBFile(String line) throws RuntimeException {
        String[] parts = line.split(":", 2);
        if (parts.length < 2) {
            throw new RuntimeException("Malformed line in WECIB file: " + line);
        }
        return parts[1].trim();
    }

    /**
     * Reads all prior entries in cards.wecib, and creates/returns Cards objects of the correct type.
     * 
     * @param cardID
     * @return Cards object (Furniture_Card or Person_Card) to be put into deck ArrayList.
     * @author Sami Achnine
     * @version 1.00
     */
    public static Cards readCardFromFile(int cardID) {
        File cardFile = new File("cards.wecib");

        String id = "";
        String type = "";
        String name = "";
        String description = "";
        String rarity = "";

        int attack = 0;
        int defense = 0;

        int dataLine = 0;
        int currentCard = -1;

        try (Scanner cardsScanner = new Scanner(cardFile)) {
            while (cardsScanner.hasNextLine()) {
                String data = cardsScanner.nextLine().trim();

                if (data.isEmpty()) {
                    continue;
                }

                if (data.startsWith("[Card]")) {
                    currentCard++;
                    dataLine = 0;
                    continue;
                }

                if (currentCard != cardID) {
                    continue;
                }

                String value = parseDataFromWECIBFile(data);

                switch (dataLine) {
                    case 0: id = value; break;
                    case 1: type = value; break;
                    case 2: name = value; break;
                    case 3: description = value; break;
                    case 4: rarity = value; break;
                    case 5: attack = Integer.parseInt(value); break;
                    case 6: defense = Integer.parseInt(value); break;
                }

                dataLine++;

                if (dataLine == 7) {
                    if (type.equalsIgnoreCase("Furniture")) {
                        return new Furniture_Card(id, name, description, rarity, defense, attack/100);
                    }
                    else if (type.equalsIgnoreCase("Person")) {
                        return new Person_Card(id, name, description, rarity, defense, attack);
                    }
                }
            }

            

        } catch (Exception ex) {
            System.out.println("FATAL ERROR: Could not read card data. Ensure cards.wecib is present and written correctly.\nStack trace:");
            ex.printStackTrace();
        }
        
        return null;
        
    }
}
