package prog04;

import java.util.EmptyStackException;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of the interface StackInterface<E> using a List.
 *
 * @author vjm
 */

public class ListStack<E> implements StackInterface<E> {
    // Data Fields
    /**
     * Storage for stack.
     */
    List<E> stackList;

    /**
     * Initialize stackList to an empty List.
     */
    public ListStack() {
        stackList = new ArrayList<E>();
    }

    /**
     * Pushes an item onto the top of the stack and returns the item
     * pushed.
     *
     * @param obj The object to be inserted.
     * @return The object inserted.
     */
    public E push(E obj) {
        stackList.add(obj);
        return obj;
    }

    public E peek(){
        if(empty()){
            throw new EmptyStackException();
        }

        return stackList.get(stackList.size() - 1);
    }

    public E pop(){
        if(empty()){
            throw new EmptyStackException();
        }
        E val4 = stackList.get(stackList.size() - 1);
        stackList.remove(val4);
        return val4;
    }

    public boolean empty(){
        return (stackList.isEmpty());
    }
    /**** EXERCISE ****/
}
