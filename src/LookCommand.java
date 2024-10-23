class LookCommand extends Command {
    @Override
    String execute() {
        return GameState.instance().getAdventurersCurrentRoom().describeFull();  // Use describeFull() here
    }
}
