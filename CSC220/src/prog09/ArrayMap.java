package prog09;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;

public class ArrayMap <K extends Comparable<K>, V>
    extends AbstractMap<K, V> {

    static class Entry <K extends Comparable<K>, V>
	implements Map.Entry<K, V> {

	K key;
	V value;
    
	Entry (K key, V value) {
	    this.key = key;
	    this.value = value;
	}
    
	public K getKey () { return key; }
	public V getValue () { return value; }
	public V setValue (V newValue) {
	    V oldValue = value;
	    value = newValue;
	    return oldValue;
	}
    }

    Entry<K, V>[] entries;
    int size = 0;

    ArrayMap () {
	entries = (Entry<K, V>[]) new Entry[4];
    }

    void reallocate () {
	System.err.println("reallocating " + entries.length);
	Entry[] newEntries = (Entry<K, V>[]) new Entry[2 * entries.length];
	System.arraycopy(entries, 0, newEntries, 0, size);
	entries= newEntries;
    }    

    public int size () { return size; }

    /**
     * Find an entry in entries.
     *
     * @param key The key to be found
     * @return The index of the entry with that key or, if it is not
     * there, (-insert_index - 1), where insert_index is the index
     * where should be added (to keep things sorted by key).
     */
    int find (K key) {
	// EXERCISE
	///
	int low = 0;
	int high = size - 1;
	int middle = 0;
	while(low <= high){
		middle = (low + high)/2;
		if(key.compareTo(entries[middle].key) > 0){
			low = middle + 1;
		}
		else if(key.compareTo(entries[middle].key) < 0){
			high = middle - 1;
		}
		else{
			return middle;
		}
	}
			return -low -1;

	//return low;
	///
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	return index >= 0;
    }
  
    public V get (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	if (index >= 0)
	    return entries[index].value;
	return null;
    }

    /**
     * Add an entry to entries.
     *
     * @param index    The index at which to add the entry to entries.
     * @param newEntry The new entry to add.
      */
    void add (int index, Entry newEntry) {
	if (size == entries.length)
	    reallocate();
	// EXERCISE
	///
	for(int i = size-1; i >= index; --i){
		entries[i+1] = entries[i];
	}

	entries[index] = newEntry;
	size++;

	///
    }

    public V put (K key, V value) {
	int index = find(key);
	if (index >= 0)
	    return entries[index].setValue(value);
	add(-index - 1, new Entry(key, value));
	return null;
    }

    /**
     * Remove an entry from entries.
     *
     * @param index The index in entries of the entry to remove.
     * @return The Entry that was just removed.
     */
    Entry<K, V> remove (int index) {
	Entry<K, V> entry = entries[index];
	// EXERCISE
	///
	for(int i = index+1; i < size; ++i){
		entries[i-1] = entries[i];
	}

	size--;

	///
	return entry;
    }

    public V remove (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = find(key);
	if (index >= 0)
	    return remove(index).value;
	return null;
    }

    protected class Iter implements Iterator<Map.Entry<K, V>> {
	int index = 0;
    
	public boolean hasNext () { 
	    return index < size;
	}
    
	public Map.Entry<K, V> next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    return entries[index++];
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    protected class Setter extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator () {
	    return new Iter();
	}
    
	public int size () { return ArrayMap.this.size(); }
    }
  
    public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }

    public static void main (String[] args) {
	ArrayMap<String, Integer> map = new ArrayMap<String, Integer>();

	/*
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);
	  map.put("a", 0);
	  System.out.println("map = " + map);
	  map.remove("b");
	  System.out.println("map = " + map);
	  map.put("c", 2);
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);
	  map.remove("b");
	  System.out.println("map = " + map);
	  map.put("b", 1);
	  System.out.println("map = " + map);

	  map.put("d", 3);
	  System.out.println("map = " + map);
	  map.put("i", 8);
	  System.out.println("map = " + map);
	  map.put("h", 7);
	  System.out.println("map = " + map);
	  map.put("f", 5);
	  System.out.println("map = " + map);
	  map.put("e", 4);
	  System.out.println("map = " + map);
	  map.put("g", 6);
	  System.out.println("map = " + map);
	*/

	System.out.println("map = " + map);
	System.out.println("put(\"m\", 7) = " + map.put("m", 7));
	System.out.println("map = " + map);
	System.out.println("get(\"m\") = " + map.get("m"));
	System.out.println("put(\"m\", 9) = " + map.put("m", 9));
	System.out.println("map = " + map);
	System.out.println("remove(\"m\") = " + map.remove("m"));
	System.out.println("map = " + map);
	System.out.println("remove(\"m\") = " + map.remove("m"));
	System.out.println("map = " + map);
	System.out.println("put(\"m\", 2) = " + map.put("m", 2));
	System.out.println("map = " + map);
	System.out.println("put(\"g\", 7) = " + map.put("g", 7));
	System.out.println("map = " + map);
	System.out.println("get(\"g\") = " + map.get("g"));
	System.out.println("put(\"g\", 9) = " + map.put("g", 9));
	System.out.println("map = " + map);
	System.out.println("remove(\"g\") = " + map.remove("g"));
	System.out.println("map = " + map);
	System.out.println("put(\"s\", 8) = " + map.put("s", 8));
	System.out.println("map = " + map);
	System.out.println("get(\"s\") = " + map.get("s"));
	System.out.println("put(\"s\", 2) = " + map.put("s", 2));
	System.out.println("map = " + map);
	System.out.println("remove(\"s\") = " + map.remove("s"));
	System.out.println("map = " + map);
	System.out.println("remove(\"s\") = " + map.remove("s"));
	System.out.println("map = " + map);
	System.out.println("put(\"s\", 0) = " + map.put("s", 0));
	System.out.println("map = " + map);
	System.out.println("put(\"b\", 3) = " + map.put("b", 3));
	System.out.println("map = " + map);
	System.out.println("get(\"b\") = " + map.get("b"));
	System.out.println("put(\"b\", 9) = " + map.put("b", 9));
	System.out.println("map = " + map);
	System.out.println("remove(\"b\") = " + map.remove("b"));
	System.out.println("map = " + map);
	System.out.println("put(\"p\", 2) = " + map.put("p", 2));
	System.out.println("map = " + map);
	System.out.println("get(\"p\") = " + map.get("p"));
	System.out.println("put(\"p\", 5) = " + map.put("p", 5));
	System.out.println("map = " + map);
	System.out.println("remove(\"p\") = " + map.remove("p"));
	System.out.println("map = " + map);
	System.out.println("put(\"w\", 4) = " + map.put("w", 4));
	System.out.println("map = " + map);
	System.out.println("get(\"w\") = " + map.get("w"));
	System.out.println("put(\"w\", 7) = " + map.put("w", 7));
	System.out.println("map = " + map);
	System.out.println("remove(\"w\") = " + map.remove("w"));
	System.out.println("map = " + map);
	System.out.println("remove(\"w\") = " + map.remove("w"));
	System.out.println("map = " + map);
    }
}
