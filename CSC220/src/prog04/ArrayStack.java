package prog04;

import prog02.DirectoryEntry;

import java.util.EmptyStackException;

/**
 * Implementation of the interface StackInterface<E> using an array.
 *
 * @author vjm
 */

public class ArrayStack<E> implements StackInterface<E> {
    // Data Fields
    /**
     * Storage for stack.
     */
    E[] stackArray;

    /**
     * Index to top of stack.
     */
    int topIndex = -1; // initially -1 because there is no top

    private static final int INITIAL_CAPACITY = 4;

    /**
     * Construct an empty stack with the default initial capacity.
     */
    public ArrayStack() {
        stackArray = (E[]) new Object[INITIAL_CAPACITY];
    }

    /**
     * Pushes an item onto the top of the stack and returns the item
     * pushed.
     *
     * @param obj The object to be inserted.
     * @return The object inserted.
     */
    public E push(E obj) {
        topIndex++;

        if(topIndex == stackArray.length) {
            reallocate();
        }

        stackArray[topIndex] = obj;
        return obj;
    }

    /**
     * Returns the object at the top of the stack and removes it.
     * post: The stack is one item smaller.
     *
     * @return The object at the top of the stack.
     * @throws EmptyStackException if stack is empty.
     */
    public E pop() {
        if (empty())
            throw new EmptyStackException();

        /**** EXERCISE ****/
        E val = stackArray[topIndex];
        topIndex--;
        return val;
    }
    public E peek(){
        if (empty())
            throw new EmptyStackException();

        return stackArray[topIndex];
    }
    public boolean empty(){
        if (topIndex == -1){
            return true;
        }
        else{
            return false;
        }
    }

    public void reallocate(){
        E[] newData = (E[]) new Object[2 * stackArray.length];
        System.arraycopy(stackArray, 0, newData, 0, stackArray.length);
        stackArray = newData;
    }

    /**** EXERCISE ****/

}