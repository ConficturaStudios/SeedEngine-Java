package source.render.object;

import source.util.structures.Vector2;

/**
 * A screen space texture that covers the whole screen
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class ScreenTexture extends GUI {

    public ScreenTexture(int id) {
        super(id, new Vector2(0,0), new Vector2(1, 1));
    }

}
