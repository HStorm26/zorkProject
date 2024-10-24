import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;

public class Item {

    private String primaryName;
    private int weight;
    private Hashtable<String, String> messages;
    private HashSet<String> aliases;

    public Item(Scanner s) throws NoItemException {
        this.aliases = new HashSet<>();
        this.messages = new Hashtable<>();
        String next = s.nextLine().strip();
        if (next.equals("Items:")) {
            next = s.nextLine();
        }
        if (!next.equals("===")) {
            String[] splits = next.split(",");
            this.primaryName = splits[0];

            for (String split : splits) {
                this.aliases.add(split.toLowerCase());
            }

            next = s.nextLine();
            this.weight = Integer.parseInt(next);
            next = s.nextLine();

            while (!next.equals("---")) {
                int colon = next.indexOf(":");
                String verb = next.substring(0, colon);
                String mssg = next.substring(colon + 1);
                this.messages.put(verb, mssg);
                next = s.nextLine();
            }
        } else {
            throw new NoItemException();
        }
    }

    // Simple constructor for testing purposes
    public Item(String primaryName, int weight) {
        this.primaryName = primaryName;
        this.weight = weight;
        this.aliases = new HashSet<>();
        this.aliases.add(primaryName.toLowerCase());  // Add primary name as alias
        this.messages = new Hashtable<>();
    }

    public boolean goesBy(String name) {
        return this.aliases.contains(name.toLowerCase());
    }

    public String getPrimaryName() {
        return this.primaryName;
    }

    public String getAliases() {
        return this.aliases.toString(); // Debugging: return aliases as a string
    }

    public String getMessageForVerb(String verb) {
        return this.messages.get(verb);
    }

    public int getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return primaryName;
    }
}
