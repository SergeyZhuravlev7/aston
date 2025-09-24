package ru.zhuravlev.Task1;

import java.util.*;
import java.util.stream.Collectors;

public class MyHashMap<K,V> extends AbstractMap<K, V> implements Map<K, V> {

    static class Entry<K,V> implements Map.Entry<K, V> {

        K key;
        V value;
        Entry<K, V> next;

        Entry(K key,V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Map.Entry<?, ?> e)) return false;
            return Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }


    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    private final static int DEFAULT_CAPACITY = 10;
    final double LOAD_FACTOR = 0.75;
    private int capacity;
    private Entry<K, V>[] entryArray;
    private Set<Map.Entry<K, V>> entrySet;
    private int size;

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());

    }

    MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        createNewArray();
        entrySet = new HashSet<>(capacity);
    }

    MyHashMap(int capacity) {
        this.capacity = capacity;
        createNewArray();
        entrySet = new HashSet<>(capacity);
    }

    @Override
    public V get(Object key) {
        if (key == null) return getNull();
        int index = getIndex(key);
        Entry<K, V> entry = entryArray[index];
        if (entry != null) {
            Entry<K, V> dummy = entry;
            while (dummy != null) {
                if (dummy.getKey().equals(key)) {
                    return dummy.getValue();
                }
                dummy = dummy.next;
            }
        }
        return null;
    }

    @Override
    public V put(K key,V value) {
        if (key == null) return updateNullKey(value);
        int index = getIndex(key);
        if (size >= capacity * LOAD_FACTOR) createNewArray();
        Entry<K, V> newEntry = new Entry<>(key,value);
        Entry<K, V> entry = entryArray[index];
        if (entry != null) {
            Entry<K, V> dummy = entry;
            while (dummy != null) {
                if (dummy.getKey().equals(key)) {
                    V oldValue = dummy.getValue();
                    dummy.setValue(value);
                    return oldValue;
                }
                if (!dummy.hasNext()) break;
                dummy = dummy.next;
            }
            dummy.next = newEntry;
        } else entryArray[index] = newEntry;
        entrySet.add(newEntry);
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key);
        Entry<K, V> prev = null;
        Entry<K, V> entry = entryArray[index];
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                if (prev == null) {
                    entryArray[index] = entry.next;
                } else {
                    prev.next = entry.next;
                }
                size--;
                entrySet.remove(entry);
                return entry.getValue();
            }
            prev = entry;
            entry = entry.next;
        }
        return null;
    }

    private int getIndex(Object key) {
        int hashCode = hash(key);
        int index = hashCode % capacity;
        return index;
    }

    private void createNewArray() {
        if (this.entryArray == null) this.entryArray = (Entry<K, V>[]) new Entry[capacity];
        else incArraySize();
    }

    private void incArraySize() {
        this.entrySet.clear();
        this.size = 0;
        Entry<K, V>[] oldArray = this.entryArray;
        int newCapacity = (int) (entryArray.length * 1.5) + 1;
        this.capacity = newCapacity;
        this.entryArray = (Entry<K, V>[]) new Entry[capacity];
        for (Entry<K, V> entry : oldArray) {
            while (entry != null) {
                put(entry.getKey(),entry.getValue());
                entry = entry.next;
            }
        }
    }

    private V updateNullKey(V value) {
        Entry<K,V> entry = entryArray[0];
        if (entry == null) entryArray[0] = new Entry<>(null,value);
        Entry<K,V> dummy = entry;
        while (dummy != null) {
            if (dummy.getKey() == null) {
                V oldValue = dummy.getValue();
                dummy.setValue(value);
                return oldValue;
            }
            if (!dummy.hasNext()) dummy.next = new Entry<>(null,value);
            dummy = dummy.next;
        }
        return null;
    }

    private V getNull() {
        Entry<K,V> entry = entryArray[0];
        if (entry != null) {
            Entry<K,V> dummy = entry;
            while (dummy != null) {
                if (dummy.getKey() == null) return dummy.getValue();
                dummy = dummy.next;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        this.entryArray = (Entry<K, V>[]) new Entry[capacity];
        this.entrySet.clear();
        this.size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = getIndex(key);
        Entry<K,V> entry = entryArray[index];
        Entry<K,V> dummy = entry;
        while (dummy != null) {
            if (key != null && dummy.getKey().equals(key)) return true;
            else if (key == null && dummy.getKey() == null) return true;
            dummy = dummy.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return entrySet.stream().anyMatch(e -> e.getValue().equals(value));
    }

    @Override
    public Set<K> keySet() {
        return entrySet.stream().map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
            put(entry.getKey(),entry.getValue());
    }

    @Override
    public Collection<V> values() {
        return entrySet.stream().map(Map.Entry::getValue).toList();
    }
}
