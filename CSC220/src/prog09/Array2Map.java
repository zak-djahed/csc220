package prog09;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Array2Map <K extends Comparable<K>> extends ArrayMap<K, Object> {
    Array2Map () {}

    // Add a new Entry with the ArrayMap as its value
    // and the key of its first Entry.
    Array2Map (ArrayMap<K, Object> map) {
	add(0, new Entry(map.entries[0].key, map));
    }

    ArrayMap<K, Object> getMap (int i) {
	return (ArrayMap<K, Object>) entries[i].value;
    }

    public int size () {
	int n = 0;
	for (int i = 0; i < size; ++i)
	    n += getMap(i).size();
	return n;
    }

    // Returns index of ArrayMap that COULD contain the key.
    // Return -1 if none can because key is less than minimum key.
    int findMap (K key) {
	int index = find(key);
	if (index >= 0)
	    return index;
	return (-index-1)-1;
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	// An Array2Map is an array of ArrayMap.
	// Find the index of the ArrayMap that (possibly) contains the key.
	int index = findMap(key);
	if (index < 0)
	    return false;
	// Get that ArrayMap and check if it actually contains the key.
	return getMap(index).containsKey(key);
    }
  
    public Object get (Object keyAsObject) {
	// EXERCISE
	///
	K key = (K) keyAsObject;
	int index = findMap(key);
	if(index < 0){
		return null;
	}

	return getMap(index).get(key);
	///
    }

    // Set key of entries[index] to minimum key in its ArrayMap.
    void updateKey (int index) {
	entries[index].key = getMap(index).entries[0].key;
    }

    // Split the ArrayMap at index in half
    // and put the right half in index+1.
    void split (int index) {
	// Left half is at index.
	ArrayMap<K, Object> left = getMap(index);
	// Right half might be an Array2Map if this is part of a BTree.
	ArrayMap<K, Object> right = left instanceof Array2Map ? new Array2Map<K>() : new ArrayMap<K, Object>();
	// Move two entries from left to right.
	right.add(0, left.remove(left.size - 1));
	right.add(0, left.remove(left.size - 1));
	// Add a new Entry whose value is is the new right half ArrayMap.
	// key is key of first ArrayMap entry.
	// Add it at index+1.
	add(index+1, new Entry(right.entries[0].key, right));
    }

    public Object put (K key, Object value) {
	Object oldValue = null;
	int index = findMap(key);

	// EXERCISE
	// If key="Aaron" and the current minimum is "Ann",
	// what will be the value of index?
	// In which index ArrayMap do we want to put "Aaron"?
	// Update the value of index.
	///
	if(index < 0){
		index = 0;
	}

	///

	// Put the key and value into the ArrayMap at index,
	// and save the value in oldValue.

	oldValue = getMap(index).put(key, value);


	// Update the key at index using updateKey().
	updateKey(index);
	// If the ArrayMap at index has 4 entries,
	// split it using split() (above).
	if(getMap(index).size == 4){
		split(index);
	}


	return oldValue;
    }

    // Merge the ArrayMap at index with the one at index+1
    // and remove the one at index+1.
    // This is the opposite of split.
    void join (int index) {
	ArrayMap<K, Object> left = getMap(index);
	ArrayMap<K, Object> right = getMap(index+1);
	// EXERCISE
	// Remove each element from right (from which index?)
	while(!right.isEmpty()){
		left.add(left.size, right.remove(0));
	}
	// and add it to left (to which index?)
	// How do you know when you are done?


	// Remove the Entry at index+1 from this Array2Map
	// (not left or right).
		remove(index+1);
    }

    public Object remove (Object keyAsObject) {
	K key = (K) keyAsObject;
	int index = findMap(key);

	// EXERCISE
	// If key="Aaron" and the current minimum is "Ann",
	// what will be the value of index?
	// What should remove do?
	///
	if(index < 0){
		return null;
	}

	///

	Object value = null;
	// Remove the key from the map at index.
	// Save the value in value.
	//ArrayMap<K, Object> map = getMap(index);
	value = getMap(index).remove(key);

	// Update the key at index using updateKey().
	updateKey(index);

	// If the map at index has 1 entry
	if(getMap(index).size == 1){
		// If index is not the last index
		if(!(index == size - 1)) {
			// Join the map at index to the one at index+1
			join(index);

			// If the map at index is too large
			// split it
			if (getMap(index).size == 4) {
				split(index);
			}
		}
		else if(index == size - 1){
			join(index -1);
			if(getMap(index - 1).size == 4){
				split(index - 1);
			}
		}


	}







	    // Else if the index is the last index

		// How do we call join?

		// Which map might be too large now?
		// What do we do if it is?




	return value;
    }

    protected class Iter implements Iterator<Map.Entry<K, Object>> {
	int index = 0;
	Iterator<Map.Entry<K, Object>> iter;
    
	Iter () {
	    if (index < size)
		iter = getMap(index).entrySet().iterator();
	}

	public boolean hasNext () { 
	    return index < size && iter.hasNext();
	}
    
	public Map.Entry<K, Object> next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    Map.Entry<K, Object> entry = iter.next();
	    if (!iter.hasNext()) {
		index++;
		if (index < size)
		    iter = getMap(index).entrySet().iterator();
	    }
	    return entry;
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    protected class Setter extends AbstractSet<Map.Entry<K, Object>> {
	public Iterator<Map.Entry<K, Object>> iterator () {
	    return new Iter();
	}
    
	public int size () { return Array2Map.this.size(); }
    }
  
    // public Set<Map.Entry<K, Object>> entrySet () { return new Setter(); }

    void putTest (K key, int value) {
	System.out.println("put(" + key + ", " + value + ") = " + put(key, value));
	System.out.println(this);
	if (!get(key).equals(value))
	    System.out.println("ERROR: get(" + key + ") = " + get(key));
    }

    void removeTest (K key) {
	Object v = get(key);
	Object value = remove(key);
	if (!v.equals(value))
	    System.out.print("ERROR: ");
	System.out.println("remove(" + key + ") = " + value);
	value = remove(key);
	if (value != null)
	    System.out.println("ERROR: remove(" + key + ") = " + value);
	System.out.println(this);
    }

    public static void main (String[] args) {
	ArrayMap<String, Object> map = new ArrayMap<String, Object>();
	map.put("b", 1);
	Array2Map<String> map2 = new Array2Map<String>(map);
	// map2.put("b", map);
	// map2.add(0, map2.new Entry("b", map));
	// map2.size = 1;
	// map2.put("b", 1);
	System.out.println(map2);

	map2.putTest("c", 2);
	map2.putTest("c", 7);
	map2.putTest("a", 0);
	map2.putTest("d", 3);
	map2.putTest("d", 7);
	map2.removeTest("a");
	map2.putTest("e", 9);
	map2.putTest("f", 8);
	map2.removeTest("b");
	map2.removeTest("e");
	map2.putTest("e", 9);
	map2.putTest("b", 1);
	map2.removeTest("f");
	map2.putTest("f", 3);
	map2.putTest("g", 4);
	map2.removeTest("d");
	map2.putTest("d", 6);
	map2.putTest("h", 8);
	map2.removeTest("e");
    }
}
