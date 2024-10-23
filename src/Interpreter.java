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

                command = promptUser(commandLine);
            }

            System.out.println("Bye!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String promptUser(Scanner commandLine) {

        System.out.print("> ");
        return commandLine.nextLine();
    }

}
