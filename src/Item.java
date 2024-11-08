import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;

public class Item {

    private String primaryName;
    private int weight;
    private Hashtable<String, String> messages;
    private HashSet<String> aliases;
    private ArrayList<String> actions;

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
                boolean hasActions = false;
                int colon = next.indexOf(":");
                String verb = next.substring(0, colon);
                if(verb.contains("[")){
                    hasActions = true;
                    verb = verb.substring(0, verb.indexOf("["));
                    this.actions = new ArrayList<>();
                }
                String mssg = next.substring(colon + 1);
                this.messages.put(verb, mssg);
                if(hasActions){
                    String[] actionArray = next.substring((next.indexOf("[") + 1), next.indexOf("]")).split(",");
                    actions.addAll(java.util.Arrays.asList(actionArray));
                }
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
    public void/*maybe?*/ executeActionsForVerb(/*maybe args? */){
        if(actions == null) {}
        else{
            for(int i=0; i<actions.size(); i++){
                String prefix = actions.get(i).substring(0, 3);
                //all of the item events have unique combinations of letters at the beginning.
                //we can check these against a set and use a switch case instead of a
                //less efficient if-else chain.
                String fullAction = actions.get(i);
                switch(prefix){
                    case "Sco":
                        int pointsToAdd = Integer.parseInt(fullAction.substring(
                         (fullAction.indexOf("(") + 1), fullAction.indexOf(")")));
                        GameState.instance().addScore(pointsToAdd);
                        actions.remove(i);
                        i--;
                        //actions can only add points once. repeated use will not grant repeat
                        //points.
                    case "Wou":

                    case "Die":
                    case "Win":
                    case "Dro":
                    case "Dis":
                    case "Tra":
                    case "Tel":
                    default:
                }
            }

        }    
    }
}
