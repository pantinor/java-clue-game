package org.antinori.astar;

import com.google.common.collect.Ordering;
import java.util.Map;

/**
 * Utilities that belong nowhere else.
 *
 * @author Gene McCulley
 */
class Utilities {

    private Utilities() {
        // Inhibit construction as this class only provides static functions.
    }

    /**
     * An {@link com.google.common.collect.Ordering} which orders by value of
     * {@link java.util.Map} entries.
     *
     * @param <K> the key type
     * @param <V> the value type (must implement {@link java.lang.Comparable})
     * @return an {@link com.google.common.collect.Ordering} which uses the
     * natural ordering of entry values
     */
    static <K, V extends Comparable<V>> Ordering<Map.Entry<K, V>> orderByEntryValue() {
        return new Ordering<Map.Entry<K, V>>() {

            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        };
    }

}
