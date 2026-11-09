import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList newDeck = Deck.getDeck();

        for (int i = 0; i < newDeck.size(); i++) {
            System.out.println(newDeck.get(i));
        }
        
    }

    
}