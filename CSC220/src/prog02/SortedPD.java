package prog02;

import java.io.*;

/**
 * This is an implementation of PhoneDirectory that uses a sorted
 * array to store the entries.
 *
 * @author 
 */
public class SortedPD extends ArrayBasedPD {

    /**
     * Add an entry to the directory.
     *
     * @param index    The index at which to add the entry to theDirectory.
     * @param newEntry The new entry to add.
     * @return The DirectoryEntry that was just added.
     */
    protected DirectoryEntry add(int index, DirectoryEntry newEntry) {
        if (size == theDirectory.length)
            reallocate();
        for(int i = size; i > index; i--){
            theDirectory[i] = theDirectory[i - 1];
        }
        theDirectory[index] = newEntry;
        size++;
        return newEntry;
    }
    /**
     * Find an entry in the directory.
     *
     * @param name The name to be found
     * @return The index of the entry with that name or, if it is not
     * there, (-insert_index - 1), where insert_index is the index
     * where should be added.
     */
    protected int find(String name) {
        int low = 0;
        int high = size - 1;
        int middle = 0;
        while (low <= high) {
            middle = (low + high) / 2;
            if (theDirectory[middle].getName().compareTo(name) < 0) {
                low = middle + 1;
            }
            else if (theDirectory[middle].getName().compareTo(name) > 0) {
                high = middle - 1;
            }
            else {
                return middle;
            }
        }
        if (theDirectory[low] != null && theDirectory[low].getName().equals(name))
            return low;
        else
            return -low -1;
    }
    /**
     * Remove an entry from the directory.
     *
     * @param index The index in theDirectory of the entry to remove.
     * @return The DirectoryEntry that was just removed.
     */
    protected DirectoryEntry remove(int index) {
        DirectoryEntry entry = theDirectory[index];
        //theDirectory[index] = theDirectory[size - 1];
        while (index  < size - 1){
            theDirectory[index] = theDirectory[index + 1];
            index++;
        }
        size--;
        return entry;
    }

}
