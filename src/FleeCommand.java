class FleeCommand extends Command{
    
    String execute(){
        if(GameState.instance().combatUpdate()){
            Room cr = GameState.instance().getAdventurersCurrentRoom();
            Room pr = GameState.instance().getPreviousRoom();
            String[] exitPaths = {"n", "s", "e", "w", "u", "d"};
            for(int i=0; i<exitPaths.length; i++){
                if(pr == cr.leaveBy(exitPaths[i])){
                    GameState.instance().setAdventurersCurrentRoom(pr);
                    return "You flee from the enemies back to " + pr + ".\n";
                }
            }
            return "You frantically search for the exit back to " + pr + ", but find nothing!\n";
        }
        else{
            return "Flee from what?\n";
        }
    }
}
