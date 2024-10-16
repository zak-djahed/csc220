package prog05;

import java.util.Queue;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements the Queue interface using a singly-linked list.
 **/
public class LinkedQueue<E> extends AbstractQueue<E>
        implements Queue<E> {

    // Data Fields
    /** Reference to the Node with the first element in the queue. */
    protected Node first;
    /** Reference to the Node with the last element in the queue. */
    protected Node last;
    /** Size of queue. */
    protected int size;

    /** A Node is the building block for a single-linked list. */
    protected class Node {
        // Data Fields

        /** The reference to the element. */
        protected E element;
        /** The reference to the next node. */
        protected Node next;

        // Constructors
        /**
         * Creates a new node with a null next field.
         * @param element The element stored
         */
        protected Node (E element) {
            this.element = element;
            next = null;
        }
    } //end class Node

    // Methods
    /**
     * Return the first element of the queue without removing it.
     * @return The first element the queue if successful;
     * return null if the queue is empty
     */
    @Override
    public E peek () {
        if (isEmpty())
            return null;

        // EXERCISE 5
        return first.element; // fix

    }

    /**
     * Remove the first element of the queue and return it
     * if the queue is not empty.
     * @post first references element that was second in the queue.
     * @return The element removed if successful, or null if not
     */
    @Override
    public E poll () {
        E element = null;
        if(size == 0){
            return null;
        }
        element = first.element;
        first = first.next;
        size--;
        return element;

        // EXERCISE 5
        // Do the right thing if the queue is empty.
        // Do the same thing as pop to set element to the correct value.
        // Also decrement size.
        // Set last to null if first ends up null.
        ///
        ///
    }

    /**
     * Insert an element at the end of the queue.
     * @post element is added to the end of the queue.
     * @param element The element to add
     * @return true (always successful)
     */
    @Override
    public boolean offer (E element) {
        if (first == null){
            last = new Node(element);
            first = last;
        }
        else{
            last.next = new Node(element);
            last = last.next;
        }
        size++;
        return true;
    }

    /**
     * Returns the size of the queue
     * @return the size of the queue
     */
    @Override
    public int size () {
        return size;
    }

    /**
     * Returns an iterator to the contents of this queue
     * @return an iterator to the contents of this queue
     */
    public Iterator<E> iterator () {
        return new Iter();
    }

    /**
     * Inner class to provide an iterator to the contents of
     * the queue.
     */
    protected class Iter implements Iterator<E> {
        // The node whose element will be returned by next()
        Node node = first;

        /**
         * Returns true if there are more elements in the iteration
         * @return true if there are more elements in the iteration
         */
        @Override
        public boolean hasNext () {
            if(node == null){
                return false;
            }
            return true; // fix

        }

        /**
         * Return the next element in the iteration and advance the iterator
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next () {
            if (!hasNext())
                throw new NoSuchElementException();
            E element = node.element;
            node = node.next;
            return element;

            // EXERCISE
            // Set element to the correct value.
            // Set node to the next node.

        }
        /**
         * Removes the last element returned by this iteration
         * @throws UnsupportedOperationException this operation is not
         * supported
         */
        @Override
        public void remove () {
            throw new UnsupportedOperationException();
        }
    }
}
