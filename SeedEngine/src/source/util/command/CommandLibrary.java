package source.util.command;

import source.engine.Debugger;
import source.engine.GameEngine;
import source.engine.Time;
import source.util.collections.dictionary.Dictionary;


/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class CommandLibrary extends Dictionary<Command>{
    private static final CommandLibrary STANDARD_LIBRARY = new CommandLibrary();
    private static final Dictionary<Getter> GETTER_DICTIONARY = new Dictionary<>();
    private static final Dictionary<Setter> SETTER_DICTIONARY = new Dictionary<>();

    static {
        //****** Populate standard library ********
        STANDARD_LIBRARY.put("exit", (args) -> {
            GameEngine.close();
        });
        STANDARD_LIBRARY.put("help", (args) -> {
            Debugger.out.println("Available commands:");
            for (CharSequence command : STANDARD_LIBRARY.getKeySet()) {
                Debugger.out.println("   " + command);
            }
        });
        STANDARD_LIBRARY.put("pause", (args) -> {
            if (!Time.isPaused()) {
                Time.togglePause();
                Debugger.out.println("Game is now paused");
            }
            else Debugger.out.println("Game is already paused");
        });
        STANDARD_LIBRARY.put("unpause", (args) -> {
            if (Time.isPaused()) {
                Time.togglePause();
                Debugger.out.println("Game is now unpaused");
            }
            else Debugger.out.println("Game is already unpaused");
        });
        STANDARD_LIBRARY.put("togglePause", (args) -> {
            Time.togglePause();
            if (Time.isPaused()) Debugger.out.println("Game is now paused");
            else Debugger.out.println("Game is now unpaused");
        });
        STANDARD_LIBRARY.put("print", (args) -> {
            if (args.length != 2) {
                Debugger.out.println("Invalid format");
            } else if (!GETTER_DICTIONARY.contains(args[1])) {
                Debugger.out.println("Invalid token - " + args[1] + "\" not recognized");
            } else {
                Debugger.out.println(GETTER_DICTIONARY.define(args[1]).get());
            }
        });
        STANDARD_LIBRARY.put("set", (args) -> {
            if (args.length != 3) {
                Debugger.out.println("Invalid format");
            } else if (!SETTER_DICTIONARY.contains(args[1])) {
                Debugger.out.println("Invalid token - " + args[1] + "\" not recognized");
            } else {
                if (SETTER_DICTIONARY.define(args[1]).set(args[2])) {
                    Debugger.out.println(args[1] + " set to " + args[2]);
                }
            }
        });

        //******** Populate getter library ********
        GETTER_DICTIONARY.put("fps", () -> (GameEngine.CURRENT_FPS + "") );
        GETTER_DICTIONARY.put("ups", () -> (GameEngine.CURRENT_UPS + "") );
        GETTER_DICTIONARY.put("timeScale", () -> (Time.getTimeScale() + "") );
        GETTER_DICTIONARY.put("deltaTime", () -> (Time.getDeltaTime() + "") );
        GETTER_DICTIONARY.put("elapsedTime", () -> (Time.getElapsedTime() + "") );
        GETTER_DICTIONARY.put("paused", () -> (Time.isPaused() + "") );

        //******** Populate setter library ********
        SETTER_DICTIONARY.put("timeScale", (value) -> {
            try {
                Time.setTimeScale(parseFloat(value));
                return true;
            } catch (Exception e) {
                Debugger.out.println("Invalid token - \"" + value + "\" not recognized");
                return false;
            }
        });

    }

    private CommandLibrary() {
        super();
    }

    public static void execute(String[] args) {
        if (args.length == 0) return;
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            c.append(args[i]);
            c.append(" ");
        }
        c.append(args[args.length - 1]);
        Debugger.out.println(c);
        if (!STANDARD_LIBRARY.contains(args[0])) {
            Debugger.out.println("Invalid command");
            Debugger.out.println();
        } else {
            STANDARD_LIBRARY.define(args[0]).execute(args);
            Debugger.out.println();
        }
    }

    public static void execute(String line) {
        execute(line.split("\\s"));
    }
    
    private static double parseDouble(String value) throws Exception {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    private static float parseFloat(String value) throws Exception {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    private static int parseInteger(String value) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    private static int parseHex(String value) throws Exception {
        try {
            return (int) Long.parseLong(value, 16);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    private static int parseBinary(String value) throws Exception {
        try {
            return (int) Long.parseLong(value, 2);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    private static String intToHex(int value) {
        return Integer.toHexString(value);
    }

}
