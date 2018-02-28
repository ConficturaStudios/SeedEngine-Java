package source.entity.components;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class DynamicMeshComponent extends MeshComponent {

    public DynamicMeshComponent(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
