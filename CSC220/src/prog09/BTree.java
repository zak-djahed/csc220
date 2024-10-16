package prog09;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;

public class BTree <K extends Comparable<K>, V>
    extends AbstractMap<K, V> {
  
    ArrayMap<K, Object> map = new ArrayMap<K, Object>();

    public int size () { return map.size(); }

    public boolean containsKey (Object keyAsObject) {
	return map.containsKey(keyAsObject);
    }
  
    public V get (Object keyAsObject) {
	return (V) map.get(keyAsObject);
    }

    public V put (K key, V value) {
	V oldValue = null;
	// EXERCISE
	// Look at containsKey and get.
	// What should oldValue be set to?
	// Don't call get or containsKey.  Call put!
	// ONE LINE
	oldValue = (V) map.put(key,value);

	if (map.size == 4) {
	    // Create a new Array2Map with map as its only entry (value).
	    Array2Map<K> newMap = new Array2Map<K>(map);
	    
	    // EXERCISE
	    // newMap is an Array2Map with a single ArrayMap that has 4 entries.
	    // How do you fix it?
	    // ONE LINE
		newMap.split(0);
		//newMap.add(0, newMap.new Entry(map.entries[0].key, map));

	    map = newMap;
	}
	return oldValue;
    }

    public V remove (Object keyAsObject) {
	V value = (V) map.remove(keyAsObject);
	if (map.size == 1 && map instanceof Array2Map) {
	    // map2 is the same as map but is type Array2Map
	    Array2Map map2 = (Array2Map<K>) map;

	    // EXERCISE
	    // map2 has only one ArrayMap.
	    // What should we set map to?
	    // ONE LINE
		map = map2.getMap(0);
	}
	return value;
    }

    void addEntries(ArrayMap<K, Object> map, List<Map.Entry<K, V>> list) {
	if (map instanceof Array2Map) {
	    Array2Map<K> map2 = (Array2Map<K>) map;
	    for (int i = 0; i < map2.size; i++)
		addEntries(map2.getMap(i), list);
	}
	else {
	    for (int i = 0; i < map.size; i++)
		list.add((Map.Entry<K, V>) map.entries[i]);
	}
    }

    Iterator<Map.Entry<K, V>> myIterator () {
	List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>();
	addEntries(map, list);
	return list.iterator();
    }

    int mySize () {
	List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>();
	addEntries(map, list);
	return list.size();
    }	

    protected class Setter extends AbstractSet<Map.Entry<K, V>> {
	public Iterator<Map.Entry<K, V>> iterator () {
	    return myIterator();
	}
    
	public int size () {
	    return mySize(); 
	}
    }

    public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }

    void print (ArrayMap map, String indent) {
	Set<Map.Entry<K, Object>> set = map.entrySet();
	for (Map.Entry<K, Object> entry : set) {
	    System.out.print(indent + entry.getKey());
	    if (entry.getValue() instanceof ArrayMap) {
		System.out.println();
		print((ArrayMap) entry.getValue(), indent + "  ");
	    }
	    else
		System.out.println(" " + entry.getValue());
	}
    }

    void putTest (K key, V value) {
	System.out.println("put(" + key + ", " + value + ") = " + put(key, value));
	if (!get(key).equals(value))
	    System.out.println("ERROR: get(" + key + ") = " + get(key));
	System.out.println(map);
	print(map, "");
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
	System.out.println(map);
	print(map, "");
    }

    public static void main (String[] args) {
	BTree<String, Integer> tree = new BTree<String, Integer>();
	System.out.println("tree = " + tree);

	tree.putTest("a", 0);
	tree.putTest("b", 1);
	tree.putTest("c", 2);
	tree.putTest("d", 3);
	tree.putTest("e", 4);
	tree.putTest("f", 5);
	tree.putTest("g", 6);
	tree.putTest("h", 7);
	tree.putTest("i", 8);
	tree.removeTest("a");
    }
}

