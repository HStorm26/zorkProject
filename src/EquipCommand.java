class EquipCommand extends Command{

    private String action;
    private String weaponName;

    EquipCommand(String a, String wN){
        this.action = a;
        this.weaponName = wN;
    }

    String execute(){
        Weapon w = GameState.instance().getActiveWeapon();
        if(action.equals("unequip")){
            if(w != null){
                String wN = w.getPrimaryName();
                GameState.instance().setActiveWeapon(null);
                return wN + " unequipped.\n";
            }else{
                return "You don't have anything to unequip!\n";
            }
        }
        if(weaponName.equals("")){
            return "equip what?\n";
        }
        try{
            GameState.instance().getItemFromInventoryNamed(weaponName);
        }catch(Exception e){
            return "You don't have an item named " + weaponName + " in your inventory.\n";
        }
        try{
            w = (Weapon) GameState.instance().getItemFromInventoryNamed(weaponName);
        }catch(Exception e){
            return "You can't use that as a weapon!\n";
        }
        if(action.equals("equip")){
            GameState.instance().setActiveWeapon(w);
            return w.getPrimaryName() + " equipped.\n";
        }
        return "uh oh. Something went wrong.\n";
    }
}
