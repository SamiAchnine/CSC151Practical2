import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Cards> cards = new ArrayList<>();
        cards.add(readCardFromFile());
        System.out.println(cards.get(cards.size() - 1));
    }

    static String parseDataFromWECIBFile(String line) throws RuntimeException {
        String[] parts = line.split(":", 2);
        if (parts.length < 2) {
            throw new RuntimeException("Malformed line in WECIB file: " + line);
        }
        return parts[1].trim();
    }

    static Cards readCardFromFile() {
        File cardFile = new File("cards.wecib");

        String id = "";
        String name = "";
        String description = "";

        int rarity = 0;
        int attack = 0;
        int defense = 0;

        int dataLine = 0;

        try (Scanner cardsScanner = new Scanner(cardFile)) {
            while (cardsScanner.hasNextLine()) {
                String data = cardsScanner.nextLine().trim();

                if (data.isEmpty() || data.startsWith("[")) {
                    dataLine = 0;
                    continue;
                }

                String value = parseDataFromWECIBFile(data);

                switch (dataLine) {
                    case 0: id = value; break;
                    case 1: name = value; break;
                    case 2: description = value; break;
                    case 3: rarity = Integer.parseInt(value); break;
                    case 4: attack = Integer.parseInt(value); break;
                    case 5: defense = Integer.parseInt(value); break;
                }

                dataLine++;
            }

            

        } catch (Exception ex) {
            System.out.println("FATAL ERROR: Could not read card data. Ensure cards.wecib is present and written correctly.\nStack trace:");
            ex.printStackTrace();
        }
        
        return new Cards(id, name, description, "Freshman", defense);
        
    }
}