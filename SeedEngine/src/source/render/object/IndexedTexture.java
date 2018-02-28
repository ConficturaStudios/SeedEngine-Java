package source.render.object;

import source.util.structures.Vector2;

/**
 * A Texture object encapsulating a graphics texture id
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class IndexedTexture extends TextureObject{

    private int size;

    private int n;

    /**
     * An indexed texture object for use as a grid based texture atlas
     * @param id texture id
     * @param size grid resolution
     */
    public IndexedTexture(int id, int size) {
        super(id);
        this.size = size;
        this.n = size * size;
    }

    public Vector2 getIndex(int i) {
        if (i >= n || i < 0) {
            throw new ArrayIndexOutOfBoundsException("Texture index out of range");
        }

        return new Vector2((i % size) / (float)size, (i / size) / (float)size);
    }

    public int getSize() {
        return size;
    }
}
