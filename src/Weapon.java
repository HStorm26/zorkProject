public class Weapon extends Item {
    private int damage;
    private double hitChance;
    private double critChance;

    public Weapon(Scanner s) throws NoItemException {
        super(s);
        this.damage = Integer.parseInt(s.nextLine().substring("Damage=".length()));
        this.hitChance = Double.parseDouble(s.nextLine().substring("Hit Chance=".length()));
        this.critChance = Double.parseDouble(s.nextLine().substring("Crit Chance=".length()));
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

    public String attack() {
        double roll = GameState.instance().getRandom();
        if (roll < critChance) {
            return "Critical hit! " + (damage * 2) + " damage dealt.";
        } else if (roll < hitChance) {
            return damage + " damage dealt.";
        } else {
            return "You missed!";
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (Damage: " + damage + ", Hit Chance: " + (hitChance * 100) + "%)";
    }
}
