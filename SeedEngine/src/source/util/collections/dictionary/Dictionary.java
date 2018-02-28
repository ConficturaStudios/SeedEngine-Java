package source.util.collections.dictionary;

import source.util.structures.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A dictionary class which uses word - object pairs to represent definitions,
 * with multiple definitions stored as a list
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class Dictionary<E> {
    private HashMap<Word, E> dictionary;

    public Dictionary() {
        dictionary = new HashMap<>();
    }

    public E define(Word word) {
        return dictionary.get(word);
    }

    public E define(CharSequence word) {
        return this.define(new Word(word));
    }

    public void put(Word word, E definition) {
        dictionary.put(word, definition);
    }

    public void put(CharSequence word, E definition) {
        this.put(new Word(word), definition);
    }

    public void remove(Word word) {
        dictionary.remove(word);
    }

    public void remove(CharSequence word) {
        this.remove(new Word(word));
    }

    public boolean contains(Word word) {
        return dictionary.containsKey(word);
    }

    public boolean contains(CharSequence word) {
        return this.contains(new Word(word));
    }

    public List<Word> findMatches(CharSequence regex) {
        ArrayList<Word> ret = new ArrayList<>();
        for (Word word : dictionary.keySet()) {
            if (word.contains(regex)) {
                ret.add(word);
            }
        }
        return ret;
    }

    public Set<Word> getKeySet() {
        return dictionary.keySet();
    }

}
