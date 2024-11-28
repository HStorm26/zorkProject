
import java.util.Scanner;
public class Enemy{
    private String name,attackMessage,deathMessage,missMessage;
    private int health,strength,deathMoney;
    private double accuracy;

    
    public Enemy(Scanner s)throws NoEnemyException{
        String next = s.nextLine();
        if(next.equals("Items:")){
            next = s.nextLine();
        }
        if(!next.equals("===")){
            while(!next.equals("---")){
                this.name=next;
                next=s.nextLine();
                this.health= Integer.parseInt(next.substring("hp: ".length()));
                next = s.nextLine();
                this.strength=Integer.parseInt(next.substring("strength: ".length()));
                next= s.nextLine();
                this.accuracy=Double.parseDouble(next.substring("accuracy: ".length()));
                next = s.nextLine();
                if(GameState.instance().getDungeon().hasShops()){
                    this.deathMoney = Integer.parseInt(next.substring("money: ".length()));
                    next = s.nextLine();
                }
                this.attackMessage = next.substring("hit: ".length());
                next = s.nextLine();
                this.missMessage = next.substring(("miss: ".length()));
                next = s.nextLine();
                this.deathMessage = next.substring(("die: ".length()));
                next = s.nextLine();
            }
        }

        else{
            throw new NoEnemyException();
        }
    }


    String attack(){
        GameState g = GameState.instance();

        
        return " ";
    }
    void takeDamage(int damage){
        this.health-=damage;
    }
    public String toString(){
        return this.name;
    }
    String getName(){
        return this.name;
    }
    String checkIfDead(){
        if(this.health <= 0){
            try{
                GameState.instance().getAdventurersCurrentRoom().removeEnemy(this);
            }catch(Exception e){
                e.printStackTrace();
            }
            return this.deathMessage + "\n";
        }
        else{
            return "";
        }
    }
}
