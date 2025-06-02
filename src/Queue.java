import java.util.ArrayList;

public class Queue {
    protected ArrayList<Medicine> medicines;

    public Queue() {
        this.medicines = new ArrayList<>();
    }

    public void enqueue(Medicine medicine) {
        this.medicines.add(medicine);
    }
    public Medicine dequeue() {
        if (!medicines.isEmpty()) {
            return medicines.remove(0);
        }
        return null; // or throw an exception
    }
    public Medicine peek() {
        if (!medicines.isEmpty()) {
            return medicines.get(0);
        }
        return null; // or throw an exception
    }
    public boolean isEmpty() {
        return medicines.isEmpty();
    }
    public int size() {
        return medicines.size();
    }
}
