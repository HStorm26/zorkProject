class HealthCommand extends Command{
    
    String execute(){
        int hp = GameState.instance().getHealth();
        if(hp == 100){
            return "You feel capable and excited for the adventure to continue.\n";
        }
        else if(hp >= 80){
            return "You trace a few cuts across your body, but nothing you can't shake off.\n";
        }
        else if(hp >= 60){
            return "There are a few more bruises than before. Pain blooms at each point if you touch them.\n";
        }
        else if(hp >= 40){
            return "Each breath is heavy and labored. A bone or two doesn't feel quite right.\n";
        }
        else if(hp >= 20){
            return "Your heartbeat rings in your ears. Each pound shoots more pain into your head. Everything hurts.\n";
        }
        else{
            return "Your vision is dark around the edges. A feeling of doom has engulfed your thoughts.\n";
        }
    }
}
