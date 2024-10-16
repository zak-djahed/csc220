package prog10;

import java.util.Map;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Set;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.io.PrintWriter;
import java.io.FileWriter;

public class HashTable<K, V> extends AbstractMap<K, V> {
    private class Entry implements Map.Entry<K, V> {
	K key;
	V value;
	Entry next;

	public K getKey () { return key; }
	public V getValue () { return value; }
	public V setValue (V value) { 
	    V oldValue = this.value;
	    this.value = value; 
	    return oldValue;
	}

	Entry (K key, V value) {
	    this.key = key;
	    this.value = value;
	}
    }

    private final static int DEFAULT_CAPACITY = 5;
    private Entry[] table = new HashTable.Entry[DEFAULT_CAPACITY];
    private int size;

    private int hashIndex (Object key) {
	int index = key.hashCode() % table.length;
	if (index < 0)
	    index += table.length;
	return index;
    }

    public V put (K key, V value) {
	// System.out.println("put " + key + " " + value + " hash index " + hashIndex(key));

	// EXERCISE 1
	Entry entry = find(key);
	if (entry != null) {
	    // What do you do if you find the Entry with that key?
	    V newVal = entry.getValue();
		entry.setValue(value);
		return newVal;
	}
	
	// If size equals the length of the table,
	// rehash with double the length.
	if (size == table.length)
	    rehash(2 * table.length);
	int entryIndex = hashIndex(key);
	Entry newEntry = new Entry(key, value);
	newEntry.next = table[entryIndex];
	table[entryIndex] = newEntry;
	// Create a new Entry and add it to the appropriate list.

	// Increment size.
	size++;
	return null;
    }
	
    private Entry find (Object key) {

	int index = hashIndex(key);
    
	// EXERCISE 2
	// Search the list stored in table[index]
	// for an Entry with that key.
	for(Entry entry = table[index]; entry != null; entry = entry.next){
		// If you find it, return it.
		if(key.equals(entry.getKey())){
			return entry;
		}
	}
	return null;
    }

    public boolean containsKey (Object key) {
	return find(key) != null;
    }

    public V get (Object key) {
	Entry entry = find(key);
	if (entry == null)
	    return null;
	return entry.value;
    }

    private void rehash (int newCapacity) {

	//System.out.println("rehash " + newCapacity);
	Entry[] oldTable = table;
	table = new HashTable.Entry[newCapacity];

	for (int i = 0; i < oldTable.length; i++) {
	    // EXERCISE 3
	    // Move every Entry in oldTable[i] to the correct index list in table.
		while(oldTable[i] != null){

			Entry nextEntry = oldTable[i];
			oldTable[i] = nextEntry.next;
			int keyHash = hashIndex(nextEntry.key);
			nextEntry.next = table[keyHash];
			table[keyHash] = nextEntry;
		}
	}

	// Remove this line:
	//table = oldTable;
    }

    public V remove (Object key) {

	// System.out.println("remove " + key + " hash index " + hashIndex(key));

	// EXERCISE 4
	// If size is a quarter the length or less, rehash with half the length,
	// but not less than DEFAULT_CAPACITY.
	if(size <= table.length/4 && table.length /2 >= DEFAULT_CAPACITY) {

		rehash(table.length / 2);
	}

	// Get the index for the key.
	int keyIndex = hashIndex(key);
 
	// What do you do if the linked list at that index is empty?
	if(table[keyIndex] == null){
		return null;
	}


	// What do you do if the first element of the list has the key?
		if(table[keyIndex].key.equals(key)){
			V val = table[keyIndex].value;
			table[keyIndex] = table[keyIndex].next;
			size--;

			return val;
		}






	// If the key is farther down the list, make sure you keep track
	// of the pointer to the previous entry, because you will need to
	// change its next variable.
	for(Entry prevEntry = table[keyIndex]; prevEntry.next != null; prevEntry = prevEntry.next){
		if(prevEntry.next.key.equals(key)){
			V value = prevEntry.next.value;
			prevEntry.next = prevEntry.next.next;
			size--;

			return value;
		}
	}









	// Return null otherwise.
	return null;
    }

    private Iterator<Map.Entry<K, V>> entryIterator () {
	return new EntryIterator();
    }

    private class EntryIterator implements Iterator<Map.Entry<K, V>> {
	// EXERCISE 5
	int index;
	Entry entry;

	private EntryIterator () {
	    // Set index to the  index of the first non-empty list
	    // or table.length if there isn't one.
	    // Set entry to the first entry in that list.
		index = 0;
		while(index < table.length && table[index] == null){
			index++;
		}
		if(index < table.length){

			entry = table[index];

		}




	}

	public boolean hasNext () {
	    // You just need to use the value of index.
	    return index < table.length;

	}

	public Map.Entry<K, V> next () {
	    if (!hasNext()){
			throw new NoSuchElementException();
		}


	    Entry nextEntry = entry;

	    // Set entry to the next entry in the list.
		entry = entry.next;

	    // Or if there isn't one,
	    // set index to the index of the next non-null list,
	    // or table.length if there isn't one,
	    // and set entry to the first entry in that list.
		if(entry == null){
			index++;

			while(index < table.length && table[index] == null){
				index++;
			}
			if(index < table.length){
				entry = table[index];
			}
		}

	    return nextEntry;
	}

	public void remove () {}
    }

    public Set<Map.Entry<K, V>> entrySet() { return new EntrySet(); }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
	public int size() { return size; }

	public Iterator<Map.Entry<K, V>> iterator () {
	    return entryIterator();
	}

	public void remove () {}
    }

    public String toString () {
	String ret = super.toString() + "\n";
	for (int i = 0; i < table.length; i++) {
	    ret = ret + i + ":";
	    for (Entry entry = table[i]; entry != null; entry = entry.next)
		ret = ret + " " + entry.key + " " + entry.value;
	    ret = ret + "\n";
	}
	return ret;
    }

    static void print (PrintWriter out, Object s) {
	out.println(s);
	System.out.println(s);
    }

    public static void main (String[] args) {
	PrintWriter out = null;
	try {
	    out = new PrintWriter(new FileWriter("hashtable-output.txt"));
	} catch (Exception e) {
	    System.out.println(e);
	    return;
	}

	HashTable<String, Integer> table =
	    new HashTable<String, Integer>();

	print(out, "put Brad 46 hash index " + table.hashIndex("Brad") + " returns " + table.put("Brad", 46));
	print(out, table);
	print(out, "put Hal 10 hash index " + table.hashIndex("Hal") + " returns " + table.put("Hal", 10));
	print(out, table);
	print(out, "put Brad 60 hash index " + table.hashIndex("Brad") + " returns " + table.put("Brad", 60));
	print(out, table);
	print(out, "put Hal 24 hash index " + table.hashIndex("Hal") + " returns " + table.put("Hal", 24));
	print(out, table);
	print(out, "put Kyle 6 hash index " + table.hashIndex("Kyle") + " returns " + table.put("Kyle", 6));
	print(out, table);
	print(out, "put Lisa 43 hash index " + table.hashIndex("Lisa") + " returns " + table.put("Lisa", 43));
	print(out, table);
	print(out, "put Lynne 43 hash index " + table.hashIndex("Lynne") + " returns " + table.put("Lynne", 43));
	print(out, table);
	print(out, "hash code of Victor is " + "Victor".hashCode());
	print(out, "put Victor 46 hash index " + table.hashIndex("Victor") + " returns " + table.put("Victor", 46));
	print(out, table);
	print(out, "put Zoe 6 hash index " + table.hashIndex("Zoe") + " returns " + table.put("Zoe", 6));
	print(out, table);
	print(out, "put Zoran 76 hash index " + table.hashIndex("Zoran") + " returns " + table.put("Zoran", 76));
	print(out, table);

	for (String key : table.keySet())
	    System.out.print(key + " ");
	System.out.println();

	print(out, "remove Fred returns " + table.remove("Fred"));
	print(out, table);
	print(out, "remove Hal returns " + table.remove("Hal"));
	print(out, table);
	print(out, "remove Brad returns " + table.remove("Brad"));
	print(out, table);
	print(out, "remove Lynne returns " + table.remove("Lynne"));
	print(out, table);
	print(out, "remove Lisa returns " + table.remove("Lisa"));
	print(out, table);
	print(out, "remove Lisa returns " + table.remove("Lisa"));
	print(out, table);
	print(out, "remove Zoran returns " + table.remove("Zoran"));
	print(out, table);
	print(out, "remove Zoe returns " + table.remove("Zoe"));
	print(out, table);
	print(out, "remove Victor returns " + table.remove("Victor"));
	print(out, table);
	print(out, "remove Hal returns " + table.remove("Hal"));
	print(out, table);

	out.close();
    }
}
			     
