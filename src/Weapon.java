import java.util.Scanner;

public class Weapon extends Item {

    class NoWeaponException extends Exception {}
    
    private int damage;
    private double hitChance;
    private double critChance;

    public Weapon(Scanner s) throws NoItemException {        
        super(s);
        s.nextLine(); //throw away "[weapon] stats"
        this.damage = Integer.parseInt(s.nextLine().substring("Damage=".length()));
        this.hitChance = Double.parseDouble(s.nextLine().substring("Hit Chance=".length()));
        this.critChance = Double.parseDouble(s.nextLine().substring("Crit Chance=".length()));
        s.nextLine(); //throw away "---"
    }
    public int getDamage() {
        return damage;
    }

    public double getHitChance() {
        return hitChance;
    }

    public double getCritChance() {
        return critChance;
    }

    public String attack(Enemy e) {
        double roll = GameState.instance().getRandom();
        if (roll < hitChance) {
            roll = GameState.instance().getRandom();
            if(roll < critChance){
                e.takeDamage(this.damage*2);
                return "Critical hit! You hit the " + e + " for " + (damage*2) + " damage.\n";
            }
            e.takeDamage(this.damage);
            return "You hit the " + e + " for " + damage + " damage.\n";
        } else {
            return "You missed!\n";
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (Damage: " + damage + ", Hit Chance: " + (hitChance * 100) + "%)";
    }
}
