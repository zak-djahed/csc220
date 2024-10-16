package prog06;
import java.util.Map;
import java.util.Random;

public class SkipMap<K extends Comparable<K>, V> extends LinkedMap<K, V> {
    // SkipMap containing half the elements chosen at random.
    SkipMap<K, Entry> skip;

    // Coin flipping code.
    Random random = new Random(1);
    /** Flip a coin.
     * @return true if you flip heads.
     */
    boolean heads () {
	return random.nextInt() % 2 == 0;
    }

    protected void add (Entry nextEntry, Entry newEntry) {
	super.add(nextEntry, newEntry);
    boolean coin = heads();
    if(coin == true){
        if(skip == null){
            skip = new SkipMap<>();
        }
        skip.put(newEntry.getKey(), newEntry);
    }
	// EXERCISE
	// Flip a coin.  If you flip heads, put newEntry into skip, using
	// its own key as key.  Don't forget to allocate skip if it hasn't
	// been allocated yet.
	///





	///
    }

    protected Entry find (K key) {
	Entry entry = first;
    if(skip != null){
        Entry entrySkip = entry;
        Map.Entry<K, Entry> a = skip.find(key);
        if(a != null){
            entrySkip = a.getValue();
        }
        if(entrySkip != null){
            entry = entrySkip;
        }
    }

    for(Entry index = entry; index != null; index = index.next){
        if(index.getKey().equals(key)){
            return index;
        }
        if(index.getKey().compareTo(key) > 0){
            return index.previous;
        }
    }



	// EXERCISE
	// Call find for the key in skip.
	// Set entry to the value of that Entry in skip.
	// Check for null so you don't crash.
	///





	///

	// EXERCISE
	// Use the same search as in LinkedMap.find,
	// but use the current value of entry
	// instead of starting at first.
	///



	///
    
	return last;
    }

    protected void remove (Entry entry) {
	super.remove(entry);
	// EXERCISE
	// Remove the key of entry from skip.  (Use public remove.)
	// If skip becomes empty, set it to null.
	///
    if(skip != null) {
        skip.remove(entry.key);
        if (skip.first == null) {
            skip = null;
        }
    }




	///
    }

    public String toString () {
	if (skip == null)
	    return super.toString();
	return skip.toString() + "\n" + super.toString();
    }

    public static void main (String[] args) {
	Map<String, Integer> map = new SkipMap<String, Integer>();
	test(map);
    }
}

