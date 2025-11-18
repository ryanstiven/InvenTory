package structures;

public class Stack<T> {
    private LinkedNode<T> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T data) {
        LinkedNode<T> newNode = new LinkedNode<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        return isEmpty() ? null : top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public LinkedList<T> toList() {
        LinkedList<T> list = new LinkedList<>();
        LinkedNode<T> current = top;
        
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        
        return list;
    }
}
