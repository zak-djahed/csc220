package prog06;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;

public class LinkedMap <K extends Comparable<K>, V>
    extends AbstractMap<K, V> {

    protected class Entry implements Map.Entry<K, V> {
	K key;
	V value;
	Entry previous, next;
    
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

	public String toString () {
	    return "{" + key + "=" + value + "}";
	}
    }
  
    protected Entry first, last;
  
    /**
     * Find the Entry with key or the previous key.
     * @param key The Key to be found.
     * @return The Entry e with e.key.equals(key)
     * or the last Entry with e.key < key
     * or null
     */
    protected Entry find (K key) {
	// EXERCISE
	// Look at size() method.
	///
	for (Entry entry = first ; entry != null; entry = entry.next) {
		if(entry.key == key){
			return entry;
		}
		else if (entry.key.compareTo(key) > 0){
			return entry.previous;
		}
	    // Compare entry.key to key.

	    // return entry if entry.key is equal to key


	    // return entry.previous is entry.key is greater than key

		/*
		int count = 0;
	for (Entry entry = first; entry != null; entry = entry.next)
	    count++;
	return count;
		 */

	}
	///
	return last; // Did not find the entry, so last must be previous.
    }    
  
    /**
     * Determine if the Entry returned by find is the one we are looking
     * for.
     * @param entry The Entry returned by find.
     * @param key The Key to be found.
     * @return true if find found the entry with that key
     * or false otherwise
     */
    protected boolean found (Entry entry, K key) {
	// EXERCISE
	// Fix this.
	///
	if(entry != null){
		return entry.key.equals(key);
	}
	return false;
	/*
	if(entry == null){
		return false;
	}
	else if(entry.getKey() == key){
		return true;
	}
	return false; // wrong
	///
	*/
    }

    public boolean containsKey (Object keyAsObject) {
	K key = (K) keyAsObject;
	Entry entry = find(key);
	return found(entry, key);
    }
  
    public V get (Object keyAsObject) {
	// EXERCISE
	// Look at containsKey.
	// If Entry with key was found, return its value.
	///
	K key = (K) keyAsObject;
	Entry entry = find(key);
	if(found(entry, key)){
		return entry.getValue();
	}
	/*
	K key = (K) keyAsObject;
	Entry entry = find(key);
	return found(entry, key);
	 */



	///
	return null;
    }
  
    /**
     * Add newEntry just after previousEntry or as first Entry if
     * previousEntry is null.
     * @param previousEntry Entry before newEntry or null if there isn't one.
     * @param newEntry The new Entry to be inserted after previousEntry.
     */
    protected void add (Entry previousEntry, Entry newEntry) {
	// EXERCISE
	Entry nextEntry = null;
	///
	// Set nextEntry.  Two cases.
	if(previousEntry == null){
		nextEntry = first;
	}
	else{
		nextEntry = previousEntry.next;
	}

	// Set previousEntry.next or first to newEntry.
	if(previousEntry != null){
		previousEntry.next = newEntry;
	}
	else{
		first = newEntry;
	}
	// Set nextEntry.previous or last to newEntry.
	if(nextEntry == null){
		last = newEntry;
	}
	else{
		nextEntry.previous = newEntry;
	}

	newEntry.previous = previousEntry;


	newEntry.next = nextEntry;

	// Set newEntry.previous and newEntry.next.


















	///
    }

    public V put (K key, V value) {
	Entry entry = find(key);
	// EXERCISE
	///
	// Handle the case that the key is already there.
	// Save yourself typing:  setValue returns the old value!

	if (found(entry, key)){
		return entry.setValue(value);
	} else {
		add(entry, new Entry(key, value));
	}


		// key was not found:


	///
	return null;
    }      
  
    /**
     * Remove Entry entry from list.
     * @param entry The entry to remove.
     */
    protected void remove (Entry entry) {
		// EXERCISE
		///

	Entry prev = entry.previous;
	Entry next = entry.next;
/*
	if(prev == null){
		first = next;
	}
	else{
		prev.next = next;
	}
	if(next == null){
		last = prev;
	}
	else{
		next.previous = prev;
	}
	*/
	if(first == null && last == null){
		return;
	}
	if((entry.previous == null) && (entry.next == null)){
		first = null;
		last = null;
		return;
	}
	if((entry.previous == null) && (entry.next != null)){
		first = entry.next;
		first.previous = null;
		return;
	}
	else if ((entry.previous != null) && (entry.next == null)){
		last = entry.previous;
		last.next = null;
		return;
	}
	else if ((entry.previous != null) && (entry.next != null)){
		prev.next = entry.next;
		next.previous = entry.previous;
	}

    }

    public V remove (Object keyAsObject) {
	// EXERCISE
	// Use find, but make sure you got the right Entry!
	// Look at containsKey.
	// If you do, then remove it and return its value.
	///
	Entry entryKey = find((K) keyAsObject);
	if(found(entryKey, (K) keyAsObject)){
		remove(entryKey);
		return entryKey.value;
	}
	else{
		return null;
	}





	///
	//return null;
    }      

    protected class Iter implements Iterator<Map.Entry<K, V>> {
	// EXERCISE
	// Set entry to the first entry.
	///
	Entry entry = first;
	///
    
	public boolean hasNext () { 
	    // EXERCISE
	    ///
	    return (entry != null);
	    ///
	}
    
	public Map.Entry<K, V> next () {
	    Entry ret = entry;
	    // EXERCISE
		entry = entry.next;
	    // Set entry to the next value of entry.
	    ///

	    ///
	    return ret;
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    public int size () {
	int count = 0;
	for (Entry entry = first; entry != null; entry = entry.next)
	    count++;
	return count;
    }

    protected class Setter extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator () {
	    return new Iter();
	}
    
	public int size () { return LinkedMap.this.size(); }
    }
  
    public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }
  
    static void test (Map<String, Integer> map) {
	if (false) {
	    map.put("Victor", 50);
	    map.put("Irina", 45);
	    map.put("Lisa", 47);
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
    
	    System.out.println(map.put("Irina", 55));

	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());

	    System.out.println(map.remove("Irina"));
	    System.out.println(map.remove("Irina"));
	    System.out.println(map.get("Irina"));
    
	    for (Map.Entry<String, Integer> pair : map.entrySet())
		System.out.println(pair.getKey() + " " + pair.getValue());
	}
	else {
	    String[] keys = { "Vic", "Ira", "Sue", "Zoe", "Bob", "Ann", "Moe" };
	    for (int i = 0; i < keys.length; i++) {
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + -i + ") = ");
		System.out.println(map.put(keys[i], -i));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("get(" + keys[i] + ") = ");
		System.out.println(map.get(keys[i]));
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	    for (int i = keys.length; --i >= 0;) {
		System.out.print("remove(" + keys[i] + ") = ");
		System.out.println(map.remove(keys[i]));
		System.out.println(map);
		System.out.print("put(" + keys[i] + ", " + i + ") = ");
		System.out.println(map.put(keys[i], i));
		System.out.println(map);
	    }
	}
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new LinkedMap<String, Integer>();
	test(map);
    }
}
