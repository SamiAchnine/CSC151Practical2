import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Deck {

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

    private static String parseDataFromWECIBFile(String line) throws RuntimeException {
        String[] parts = line.split(":", 2);
        if (parts.length < 2) {
            throw new RuntimeException("Malformed line in WECIB file: " + line);
        }
        return parts[1].trim();
    }

    public static Cards readCardFromFile(int cardID) {
        File cardFile = new File("cards.wecib");

        String id = "";
        String type = "";
        String name = "";
        String description = "";

        int rarity = 0;
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
                    case 4: rarity = Integer.parseInt(value); break;
                    case 5: attack = Integer.parseInt(value); break;
                    case 6: defense = Integer.parseInt(value); break;
                }

                dataLine++;

                if (dataLine == 7) {
                    if (type.equalsIgnoreCase("Furniture")) {
                        return new Furniture_Card(id, name, description, String.valueOf(rarity), defense, attack/100);
                    }
                    else if (type.equalsIgnoreCase("Person")) {
                        return new Person_Card(id, name, description, String.valueOf(rarity), defense, attack);
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
