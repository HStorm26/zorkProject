


class AttackCommand extends Command{

    private String enemyName;

    AttackCommand(String enemyName){
        this.enemyName=enemyName;
    }

    String execute(){

        if(enemyName.equals("")){
            return "attack what?\n";
        }
        Weapon currentWeapon = null;
        Enemy CurrentEnemy = null;

        GameState g = GameState.instance();
        
        try{
        Weapon currentWeapon = g.getEquippedWeapon();
        }
        catch(NoWeaponException e){}

        Room current = g.getAdventurersCurrentRoom();
        try{
        Enemy currentEnemy = current.getEnemyNamed(this.enemyName);
        }
        catch(NoEnemyException e){}

        String makeAttack = currentWeapon.makeAttack(currentEnemy);

        return makeAttack;

    }

}
