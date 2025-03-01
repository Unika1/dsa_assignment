/* Question No.6 (a):
ALgorithm Explanation:

1. Purpose:
   - Three threads (`ZeroThread`, `EvenThread`, and `OddThread`) print numbers in the sequence: 0102030405... up to a given number `n`.
   - `ZeroThread` prints `0`, `EvenThread` prints even numbers, and `OddThread` prints odd numbers.

2. Thread Coordination:
   - Threads must be synchronized to ensure the correct order of printing.
   - The `NumberPrinter` class uses `synchronized` methods and `wait()` / `notifyAll()` for synchronization.

3. Execution Flow:
   - The `ZeroThread` prints `0` first.
   - After printing `0`, it signals either the `OddThread` or `EvenThread` to print the next number.
   - The `OddThread` prints the next odd number, followed by `0` being printed again.
   - The `EvenThread` prints the next even number, followed by `0` being printed again.
   - This process continues until all numbers up to `n` are printed.

4. Synchronization Mechanism:
   - Each thread waits (`wait()`) until it's its turn to print.
   - Once a thread prints, it signals (`notifyAll()`) other threads to continue execution.
   - This prevents race conditions and ensures the correct order of printing.

5. Stopping Condition:
   - The process stops when the current number exceeds `n`, ensuring threads do not continue indefinitely.
*/

import java.util.Scanner;

// NumberPrinter class that prints zero, even, and odd numbers in sequence
class NumberPrinter {
    private int currentNumber = 1; // Keeps track of the next number to print
    private final int maxNumber; // The maximum number to print
    private int turn = 0; // Controls which thread should print (0 = zero, 1 = odd, 2 = even)

    // Constructor to set the maximum number
    public NumberPrinter(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    // Method to print 0
    public synchronized void printZero() {
        for (int i = 1; i <= maxNumber; i++) {
            while (turn != 0) { // Wait until it's ZeroThread's turn
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("0"); // Print zero
            turn = (currentNumber % 2 == 0) ? 2 : 1; // Next turn: even or odd
            notifyAll(); // Notify other threads
        }
    }

    // Method to print even numbers
    public synchronized void printEven() {
        while (currentNumber <= maxNumber) {
            while (turn != 2) { // Wait until it's EvenThread's turn
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(currentNumber++); // Print the even number
            turn = 0; // Set turn back to zero
            notifyAll(); // Notify other threads
        }
    }

    // Method to print odd numbers
    public synchronized void printOdd() {
        while (currentNumber <= maxNumber) {
            while (turn != 1) { // Wait until it's OddThread's turn
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(currentNumber++); // Print the odd number
            turn = 0; // Set turn back to zero
            notifyAll(); // Notify other threads
        }
    }
}

// ZeroThread class to print 0s
class ZeroThread extends Thread {
    private final NumberPrinter numberPrinter;

    public ZeroThread(NumberPrinter numberPrinter) {
        this.numberPrinter = numberPrinter;
    }

    @Override
    public void run() {
        numberPrinter.printZero();
    }
}

// EvenThread class to print even numbers
class EvenThread extends Thread {
    private final NumberPrinter numberPrinter;

    public EvenThread(NumberPrinter numberPrinter) {
        this.numberPrinter = numberPrinter;
    }

    @Override
    public void run() {
        numberPrinter.printEven();
    }
}

// OddThread class to print odd numbers
class OddThread extends Thread {
    private final NumberPrinter numberPrinter;

    public OddThread(NumberPrinter numberPrinter) {
        this.numberPrinter = numberPrinter;
    }

    @Override
    public void run() {
        numberPrinter.printOdd();
    }
}

// ThreadController class to coordinate the threads
class ThreadController {
    private final NumberPrinter numberPrinter;

    public ThreadController(int n) {
        this.numberPrinter = new NumberPrinter(n);
    }

    public void printNumbers() {
        ZeroThread zeroThread = new ZeroThread(numberPrinter);
        EvenThread evenThread = new EvenThread(numberPrinter);
        OddThread oddThread = new OddThread(numberPrinter);

        zeroThread.start();
        evenThread.start();
        oddThread.start();

        try {
            zeroThread.join();
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Main class to test the threads
public class ThreadSequencePrinter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number up to which to print the sequence: ");
        int n = scanner.nextInt();
        scanner.close();

        ThreadController threadController = new ThreadController(n);
        threadController.printNumbers();
    }
}
/*Testing Results:
  Case 1:
  Enter the number up to which to print the sequence: 5
  output-0102030405
  
  Case 2:
  Enter the number up to which to print the sequence: 10
  output-01020304050607080910

  Case 3:
  Enter the number up to which to print the sequence: 6
  output-010203040506
 */