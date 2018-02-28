package editor.ui;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class Widget {

    private final long UID;

    private String name;

    private int layer;

    private int posX, posY;
    private int boundX, boundY;

    protected Widget(String name) {
        this(name, 0, 0, 1, 1);
    }

    protected Widget(String name, int posX, int posY, int boundX, int boundY) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.boundX = boundX;
        this.boundY = boundY;
        this.layer = 0;
        this.UID = System.identityHashCode(this);
    }

    public long getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getBoundX() {
        return boundX;
    }

    public void setBoundX(int boundX) {
        this.boundX = boundX <= posX ? posX + 1 : boundX;
    }

    public int getBoundY() {
        return boundY;
    }

    public void setBoundY(int boundY) {
        this.boundY = boundY <= posY ? posY + 1 : boundY;
    }
}
