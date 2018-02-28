package source.render.object;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public final class Font {

    private String name;

    private final Map<Character, FontCharacter> characterMap = new HashMap<>();

    private static final Map<String, Font> FONT_LIBRARY = new HashMap<>();

    static {
        createFont("Courier New");
    }

    public final class FontCharacter {
        public final char id;
        public final int xPos;
        public final int yPos;
        public final int width;
        public final int height;
        public final int xOffset;
        public final int yOffset;
        public final int xAdvance;
        public final int page;

        public FontCharacter(char id, int xPos, int yPos, int width, int height,
                             int xOffset, int yOffset, int xAdvance, int page) {
            this.id = id;
            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xAdvance = xAdvance;
            this.page = page;
        }
    }

    private Font(String name) {
        this.name = name;

        String fontFile = "./SeedEngine/res/fonts/" + name + ".fnt";

        try {
            Scanner fileScan = new Scanner(new File(fontFile));
            String line;
            Scanner lineScan;
            while (fileScan.hasNextLine()) {
                line = fileScan.nextLine();
                lineScan = new Scanner(line);

                if (!line.startsWith("char id=")) continue;

                int fontId = 0;
                int xPos = 0;
                int yPos = 0;
                int width = 0;
                int height = 0;
                int xOffset = 0;
                int yOffset = 0;
                int xAdvance = 0;
                int page = 0;

                String next;
                while (lineScan.hasNext()) {
                    next = lineScan.next();
                    if (!next.contains("=")) continue;

                    String[] split = next.split("=");
                    String id = split[0];
                    String value = split[1];

                    if (id.equals("id")) {
                        fontId = Integer.parseInt(value);
                    } else if (id.equals("x")) {
                        xPos = Integer.parseInt(value);
                    } else if (id.equals("y")) {
                        yPos = Integer.parseInt(value);
                    } else if (id.equals("width")) {
                        width = Integer.parseInt(value);
                    } else if (id.equals("height")) {
                        height = Integer.parseInt(value);
                    } else if (id.equals("xoffset")) {
                        xOffset = Integer.parseInt(value);
                    } else if (id.equals("yoffset")) {
                        yOffset = Integer.parseInt(value);
                    } else if (id.equals("xadvance")) {
                        xAdvance = Integer.parseInt(value);
                    } else if (id.equals("page")) {
                        page = Integer.parseInt(value);
                    }

                }

                FontCharacter character = new FontCharacter((char)fontId, xPos, yPos, width, height,
                        xOffset, yOffset, xAdvance, page);

                this.characterMap.put((char)fontId, character);

            }

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }

    public String getAtlasLocation() {
        return "./SeedEngine/res/fonts/" + name + ".png";
    }

    public FontCharacter getCharacter(char character) {
        return characterMap.get(character);
    }

    public static Font createFont(String name) {
        Font f = new Font(name);
        FONT_LIBRARY.put(name, f);
        return f;
    }

}
