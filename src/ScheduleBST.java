import java.time.LocalTime;

public class ScheduleBST {
    protected LocalTime root;
    protected ScheduleBST left;
    protected ScheduleBST right;


    // explainint TREEESS:
    // kelas dipake kalo pasien sudah pilih dokter dan hari, nanti semua data dari database itu di pull. terus semua data itu dimasukkin ke tree menggunakan method insert yang di for loop buat setiap data (schedule) itu. 

    // semua data schedule itu waktu masuk ke tree bakal di sort sama tree nya mana yang masuk anak kiri mana yang masuk anak kanan pake method isBefore sama isAfter itu (ada di code nya)

    public ScheduleBST insert(LocalTime time) {
        // If this is an empty node, create a new node with the time
        if (this.root == null) {
            this.root = time;
            return this;
        }

        // Compare the new time with the current node's time
        if (time.isBefore(this.root)) {
            // Insert to the left subtree
            if (this.left == null) {
                this.left = new ScheduleBST();
            }
            this.left.insert(time);
        } else if (time.isAfter(this.root)) {
            // Insert to the right subtree
            if (this.right == null) {
                this.right = new ScheduleBST();
            }
            this.right.insert(time);
        }
        // If time equals root, we don't insert (no duplicates)

        return this;
    }

    // user masukkin jam, terus jam itu akan di recursive sampe dia ketemu jam yang sama, atau tidak ditemuin jam yang sama

    // kalo ketemu jam yang sama, uda di booking, kalo ga ketemu, brrti belum di booking anyone, so its available

    public String search(LocalTime time) {
        // If current node is empty, schedule is available
        if (this.root == null) {
            return "The current schedule is available";
        }

        // If we found a match
        if (time.equals(this.root)) {
            return "Time is not available";
        }

        // Search in left subtree if time is before current node
        if (time.isBefore(this.root)) {
            if (this.left != null) {
                return this.left.search(time);
            } else {
                return "The current schedule is available";
            }
        }

        // Search in right subtree if time is after current node
        if (time.isAfter(this.root)) {
            if (this.right != null) {
                return this.right.search(time);
            } else {
                return "The current schedule is available";
            }
        }

        return "The current schedule is available";
    }

    public static void main(String[] args) {
    // Create a new schedule BST
    ScheduleBST schedule = new ScheduleBST();
    
    // Insert some appointment times
    schedule.insert(LocalTime.of(9, 30));   // 9:30 AM
    schedule.insert(LocalTime.of(14, 15));  // 2:15 PM
    schedule.insert(LocalTime.of(11, 0));   // 11:00 AM
    schedule.insert(LocalTime.of(16, 45));  // 4:45 PM
    schedule.insert(LocalTime.of(8, 0));    // 8:00 AM
    
    // Search for existing appointments
    System.out.println("Searching for 9:30 AM: " + schedule.search(LocalTime.of(9, 30)));
    System.out.println("Searching for 2:15 PM: " + schedule.search(LocalTime.of(14, 15)));
    
    // Search for available time slots
    System.out.println("Searching for 10:00 AM: " + schedule.search(LocalTime.of(10, 0)));
    System.out.println("Searching for 3:30 PM: " + schedule.search(LocalTime.of(15, 30)));
    System.out.println("Searching for 7:00 AM: " + schedule.search(LocalTime.of(7, 0)));
    
    // Try to insert duplicate time
    schedule.insert(LocalTime.of(9, 30)); // This won't create duplicate
    System.out.println("After trying to insert duplicate 9:30 AM: " + schedule.search(LocalTime.of(9, 30)));
}
}
