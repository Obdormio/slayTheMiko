package slayTheMiko.patches;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class MikoCommand extends ConsoleCommand {
    public MikoCommand() {
        minExtraTokens = 0;
        maxExtraTokens = 1;
        requiresPlayer = false;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] strings, int i) {
        DevConsole.log("Slay The Miko!");
    }
}
