package gui.helpers;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Class for an IterableEnumerator.
 * @param <T> Type of the object you want to change into IterableEnumerator.
 */
public class IterableEnumeration<T> implements Iterable<T> {
    private final Enumeration<T> en;

    /**
     *  Creates an iterator from an enumeration.
     * @param en Enumeration you want to iterate over.
     */
    public IterableEnumeration(Enumeration<T> en) {
        this.en = en;
    }

    /**
     * Creates an iterator object.
     * @return an adaptor for the Enumeration.
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            public boolean hasNext() {
                return en.hasMoreElements();
            }

            public T next() {
                return en.nextElement();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Method to make an enumeration into an iterator.
     * @param en The object-type you want to iterate.
     * @param <T> The object-type you want to iterate.
     * @return
     */
    public static <T> Iterable<T> make(Enumeration<T> en) {
        return new IterableEnumeration<T>(en);
    }
}
