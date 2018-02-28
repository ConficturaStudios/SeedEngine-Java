package source.render.object;

/**
 * A Texture object encapsulating a graphics texture id
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class TextureObject {

    private int textureID;

    public TextureObject(int id) {
        this.textureID = id;
    }

    public int getTextureID() {
        return this.textureID;
    }

}
