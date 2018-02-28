package source.util.structures;

/**
 * [Description]
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Octree<E> {

    private OctreeNode root;

    private OctreeNode activeNode;

    private class OctreeNode {
        OctreeNode[] children;
        OctreeNode parent;
        int index;
        E data;

        public OctreeNode(E data, OctreeNode parent, int index) {
            this.data = data;
            this.children = null;
            this.parent = parent;
            this.index = index;
        }

        public void partition() {
            //noinspection unchecked
            this.children = (OctreeNode[]) new Object[8];
            for (int i = 0; i < 8; i++) {
                this.children[i] = new OctreeNode(this.data, this, i);
            }
        }
    }

    public Octree() {
        this(null);
    }

    public Octree(E data) {
        this.root = new OctreeNode(data, null, 0);
        this.activeNode = this.root;
    }

    public void subdivide(int levels) {
        if (levels > 0) {
            if (this.activeNode.children == null) {
                this.activeNode.partition();
                subdivide(levels - 1);
            } else {
                for (int i = 0; i < 8; i++) {
                    descend(i);
                    subdivide(levels - 1);
                    ascend();
                }
            }
        }
    }

    public void descend(int branch) {
        if (branch < 0 || branch > 7) throw new IllegalArgumentException();
        if (this.activeNode.children == null) this.activeNode.partition();
        this.activeNode = this.activeNode.children[branch];
    }

    public int ascend() {
        int aIndex = this.activeNode.index;
        if (this.activeNode.parent != null) this.activeNode = this.activeNode.parent;
        return aIndex;
    }

}
