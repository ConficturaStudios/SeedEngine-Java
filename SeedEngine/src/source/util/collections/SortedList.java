package source.util.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A sorted list class extending ArrayList to automatically sort members when adding or reassigning value
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class SortedList<E extends Comparable<? super E>> extends ArrayList<E> {

    SortedList names;

    public boolean addBulk(List<E> data) {
        for (int i = 0; i < data.size(); i++) {
            super.add(data.get(i));
        }
        Collections.sort(this);
        return true;
    }
    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt>
     */
    @Override
    public boolean add(E e) {
        if (this.isEmpty()) {
            return super.add(e);
        } else if (this.contains(e)) {
            super.add(this.indexOf(e), e);
            return true;
        } else {
            int c;
            int[] range = new int[2];
            int center;
            range[0] = 0;
            range[1] = this.size() - 1;

            while (true) {
                center = ((range[0] + range[1]) / 2);
                c = e.compareTo(this.get(center));
                if (range[0] == center) {
                    if (c < 0) {
                        break;
                    }
                }
                if (c > 0) {
                    range[0] = center + 1;
                    if (range[0] > range[1]) {
                        center += 1;
                        break;
                    }
                } else if (range[0] > range[1]) {
                    break;
                } else if (c < 0){
                    range[1] = center - 1;
                }
            }
            super.add(center, e);
            return true;
        }
    }

    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public E set(int index, E element) {
        E ret = super.set(index, element);
        Collections.sort(this);
        return ret;
    }

    public E getCenter() throws IndexOutOfBoundsException {
        return this.get(this.indexOfCenter());
    }

    public int indexOfCenter() {
        return (this.size() - 1) / 2;
    }

    public E getLast() throws IndexOutOfBoundsException {
        return this.get(this.size() - 1);
    }
}
