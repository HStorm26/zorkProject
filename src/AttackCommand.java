class AttackCommand extends Command{

    private String enemyName;

    AttackCommand(String enemyName){
        this.enemyName=enemyName;
    }

    String execute(){
        Enemy en;
        Weapon w;
        try{
            en = GameState.instance().getAdventurersCurrentRoom().getEnemyNamed(this.enemyName);
        }catch(Exception e){
            return "There's no " + this.enemyName + " here!\n";
        }
        try{
            w = GameState.instance().getActiveWeapon();
        }catch(Exception e){
            return "You don't have a weapon to attack with!\n";
        }
        String output = w.attack(en);
        output += en.checkIfDead();
        return output;
    }
}
