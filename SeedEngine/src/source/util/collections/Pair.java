package source.util.collections;

/**
 * A generic pair class for storing a set of 2 objects
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Pair<K,V> {

    private K key;

    private V value;

    public Pair() {

    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void set(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
