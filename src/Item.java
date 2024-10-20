import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
public class Item{


   private String primaryName;
   private int weight;
   private Hashtable<String,String> messages;
   private HashSet<String> aliases;

   public Item(Scanner s) throws NoItemException{
        this.aliases = new HashSet<String>();
        this.messages = new Hashtable<String,String>();
       String next = s.nextLine().strip(); 
       if(next.equals("Items:")){
           next = s.nextLine();
            
        }
        if(!next.equals("===")){
        
  
        String[] splits = next.split(",");

        this.primaryName=splits[0];

        for(int i = 1; i<splits.length;i++){
            this.aliases.add(splits[i]);
        }
 
        next=s.nextLine();

        this.weight = Integer.parseInt(next);
        next = s.nextLine();
     
       while(!next.equals("---")){
        int colon = next.indexOf(":");
        String verb = next.substring(0,colon);
     
        String mssg = next.substring(colon + 1);
        this.messages.put(verb,mssg);
       
        next = s.nextLine();
        }
        }
        
        else{
            throw new NoItemException();
        }
   }

   public static void main (String args[]){
        try{
        Scanner stdin = new Scanner(new FileReader("thing.zork"));
  try{
            while(true){
        Item box = new Item(stdin);
            }
          
        }
  catch(NoItemException e){
        }
        }
        catch (Exception e){}
        }
   public boolean goesBy(String name){
       if(this.aliases.contains(name)){
           return true;
       }

        return false;

   }

   public String getPrimaryName(){
       return this.primaryName;
   }

   public String getMessageForVerb(String verb){
        return this.messages.get(verb);
   }
    
   public String toString(){
        return "";
   }

   public int getWeight(){
       return this.weight;
   }
}
