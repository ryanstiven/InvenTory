package structures;

public class LinkedList<T> {
    private LinkedNode<T> head;
    private int size;

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(T data) {
        LinkedNode<T> newNode = new LinkedNode<>(data);
        
        if (head == null) {
            head = newNode;
        } else {
            LinkedNode<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        LinkedNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public LinkedNode<T> getHead() {
        return head;
    }
}
