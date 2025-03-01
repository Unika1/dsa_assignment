/* Question no.1(b):
This program finds the k-th lowest product of numbers from two given lists.
First, it takes user input for two lists of numbers, separated by commas, and a value of k,
which represents the position of the smallest product to find. 
It then multiplies each number from the first list with each number from the second list, 
generating all possible products. These products are then stored in an array and sorted in ascending order. 
After sorting, the program retrieves the k-th smallest product from the sorted list and prints the result. 
If k is invalid, such as being too large or non-positive, the program displays an error message. 
Sorting is used to efficiently determine the k-th smallest product instead of searching manually. 
This approach ensures that the required smallest product is found and displayed in a straightforward manner. */
import java.util.Scanner;
import java.util.Arrays;


public class kthLowest { 
    // Method to find the kth lowest combined return
    public static int findKthLowest(int[] returns1, int[] returns2, int k) {
        // Check for invalid input
        if (returns1 == null || returns2 == null || k <= 0 || k > returns1.length * returns2.length) {
            System.out.println("Invalid input. Please ensure k is within the valid range.");
            return Integer.MIN_VALUE; // Return a sentinel value for invalid input
        }

        // Create an array to store combined returns
        int[] combinedReturns = new int[returns1.length * returns2.length];
        int index = 0;

        // Calculate all combined returns
        for (int i = 0; i < returns1.length; i++) {
            for (int j = 0; j < returns2.length; j++) {
                combinedReturns[index++] = returns1[i] * returns2[j]; // Store the product
            }
        }

        // Sort the combined returns
        Arrays.sort(combinedReturns);

        // Return the kth lowest combined return
        return combinedReturns[k - 1]; // k is 1-based index
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input for returns1
        System.out.print("Enter the elements of returns1 (comma separated): ");
        String[] input1 = scanner.nextLine().split(",");
        int[] returns1 = new int[input1.length];
        for (int i = 0; i < input1.length; i++) {
            returns1[i] = Integer.parseInt(input1[i].trim()); // Convert input to integers
        }

        // Input for returns2
        System.out.print("Enter the elements of returns2 (comma separated): ");
        String[] input2 = scanner.nextLine().split(",");
        int[] returns2 = new int[input2.length];
        for (int i = 0; i < input2.length; i++) {
            returns2[i] = Integer.parseInt(input2[i].trim()); // Convert input to integers
        }

        // Input for k
        System.out.print("Enter the value of k: ");
        int k = scanner.nextInt();

        // Calculate and display the result
        int result = findKthLowest(returns1, returns2, k);
        if (result != Integer.MIN_VALUE) { // Check for valid result
            System.out.println("The " + k + "th lowest combined return is: " + result);
        }

        scanner.close(); // Close the scanner
    }


}
/*Testing Results:
Case 1:
Enter the elements of returns1 (comma separated): 2,5
Enter the elements of returns2 (comma separated): 3,4
Enter the value of k: 2
The 2th lowest combined return is: 8

Case 2:
Enter the elements of returns1 (comma separated): 2, 3
Enter the elements of returns2 (comma separated): 4, 5
Enter the value of k: 3
The 3th lowest combined return is: 12
*/

