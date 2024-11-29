import java.util.Scanner;

public class Interpreter {

    public static String USAGE_MSG =
        "Usage: Interpreter dungeonFile.zork|saveFile.sav.";

    public static void main(String args[]) {

        if (args.length < 1) {
            System.err.println(USAGE_MSG);
            System.exit(1);
        }

        String command;
        Scanner commandLine = new Scanner(System.in);

        try {
            if (args[0].endsWith(".zork")) {
                GameState.instance().initialize(new Dungeon(args[0]));
                System.out.println("\nWelcome to " +
                    GameState.instance().getDungeon().getTitle() + "!");
            } else if (args[0].endsWith(".sav")) {
                GameState.instance().restore(args[0]);
                System.out.println("\nWelcome back to " +
                    GameState.instance().getDungeon().getTitle() + "!");
            } else {
                System.err.println(USAGE_MSG);
                System.exit(2);
            }
            // Use describeOnEntry() to display the initial room description
            System.out.print("\n" +
                GameState.instance().getAdventurersCurrentRoom().describeOnEntry() +
                "\n");

            command = promptUser(commandLine);

            while (!command.equals("q")) {

                System.out.print(
                    CommandFactory.instance().parse(command).execute());
                while(GameState.instance().combatUpdate()){
                //for when the player gets into a fight
                    command = promptUser(commandLine);
                    if(command.equals("q")){
                        break;
                    }
                    else if(command.startsWith("save")){
                        System.out.println("You can't save during a fight!");
                    }
                    else{
                        System.out.print(
                            CommandFactory.instance().parse(command).execute());
                        for(Enemy enemy : GameState.instance().getAdventurersCurrentRoom().getAllEnemies()){
                            System.out.println(enemy.attack());
                        }
                    }
                    if(GameState.instance().checkIfDead()){
                        break;
                    }
                }
                if(GameState.instance().checkIfDead()){
                    System.out.println("Everything goes dark, and you die.");
                    break;
                }
                if(GameState.instance().checkIfWon()){
                    System.out.println("You won! " + CommandFactory.instance().parse("score").execute() + "Great job!");
                    break;
                }
                if(!command.equals("q")){
                    command = promptUser(commandLine);
                }
            }
            if(command.equals("q")){
                System.out.println("Bye!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String promptUser(Scanner commandLine) {

        System.out.print("> ");
        return commandLine.nextLine();
    }

}
