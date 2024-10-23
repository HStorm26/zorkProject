class LookCommand extends Command {
    @Override
    String execute() {
        return GameState.instance().getAdventurersCurrentRoom().describe();
    }
}
