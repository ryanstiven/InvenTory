package structures;

public class Queue<T> {
    private LinkedNode<T> front;
    private LinkedNode<T> rear;
    private int size;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(T data) {
        LinkedNode<T> newNode = new LinkedNode<>(data);
        
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T data = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        size--;
        return data;
    }

    public T peek() {
        return isEmpty() ? null : front.data;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    public LinkedList<T> toList() {
        LinkedList<T> list = new LinkedList<>();
        LinkedNode<T> current = front;
        
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        
        return list;
    }
}
