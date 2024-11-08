public class ItemSpecificCommand extends Command {
    private String verb;
    private String noun;

    public ItemSpecificCommand(String verb, String noun) {
        this.verb = verb;
        this.noun = noun;
    }

    @Override
        public String execute() {

            Room currentRoom = GameState.instance().getAdventurersCurrentRoom();       try{

                Item item = GameState.instance().getItemInVicinityNamed(noun.strip());           
                String actionResult = item.getMessageForVerb(verb);
                if(actionResult==null){
                    return "You can't " + verb + " the " + noun + ".\n";
                }
                item.executeActionsForVerb();
                return actionResult + "\n";
            }

            catch(NoItemException e){
                return "There is no " + noun + " here to " + verb + ".\n";
            }
        }
}
