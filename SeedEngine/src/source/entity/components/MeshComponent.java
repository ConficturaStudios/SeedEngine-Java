package source.entity.components;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public abstract class MeshComponent extends PrimitiveComponent {

    protected int vaoID;
    protected int vertexCount;

    public abstract int getVaoID();

    public abstract int getVertexCount();

}
