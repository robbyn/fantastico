package org.tastefuljava.mvc;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class QuickMap<K,V> implements Map<K,V> {

    public abstract V get(Object key);

    public final int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final V put(K key, V value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final V remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
