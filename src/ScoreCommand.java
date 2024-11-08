class ScoreCommand extends Command {
    
    String execute(){
        int points = GameState.instance().getScore();
        String response = "You have " + points;
        if(points == 1){
            response += " point. This earns you the title of ";
        }
        else{
            response += " points. This earns you the title of ";
        }
        if(points <= 20){
            response += "Novice";
        }
        else if(points <= 40){
            response += "Amatuer";
        }
        else if(points <= 60){
            response += "Competent";
        }
        else if(points <= 80){
            response += "Journeyman";
        }
        else if(points <= 99){
            response += "Expert";
        }
        else{
            response += "Master";
        }
        response += " Adventurer.\n";
        return response;
    }
}
