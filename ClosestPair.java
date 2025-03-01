/* Question no.2(b):
Input: Two arrays: x_coords (x-coordinates) and y_coords (y-coordinates).
Manhattan Distance Formula: For two points 
((x1, y1)) and ((x2, y2)) is calculated as (|x1 - x2| + |y1 - y2|).
Logic:
Compare every pair of points.
Keep track of the smallest distance (minDistance) and the indices of the points.
Update minDistance whenever a smaller distance is found.
Output: Return the indices of the closest pair of points. */
import java.util.Scanner;

public class ClosestPair {

    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int[] result = new int[2]; // To store the indices of the closest pair
        int minDistance = Integer.MAX_VALUE; // Start with a very large number

        // Compare every pair of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If this pair has a smaller distance, update the result
                if (distance < minDistance) {
                    minDistance = distance; // Update the minimum distance
                    result[0] = i; // Save the first index
                    result[1] = j; // Save the second index
                }
            }
        }

        return result; // Return the indices of the closest pair
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user for x-coordinates
        System.out.print("Enter x-coordinates of points (comma-separated): ");
        String[] xInput = scanner.nextLine().split(","); // Split input into an array
        int[] x_coords = new int[xInput.length];
        for (int i = 0; i < xInput.length; i++) {
            x_coords[i] = Integer.parseInt(xInput[i].trim()); // Convert each value to an integer
        }

        // Ask the user for y-coordinates
        System.out.print("Enter y-coordinates of points (comma-separated): ");
        String[] yInput = scanner.nextLine().split(","); // Split input into an array
        int[] y_coords = new int[yInput.length];
        for (int i = 0; i < yInput.length; i++) {
            y_coords[i] = Integer.parseInt(yInput[i].trim()); // Convert each value to an integer
        }

        // Check if the number of x and y coordinates matches
        if (x_coords.length != y_coords.length) {
            System.out.println("Error: The number of x and y coordinates must be the same!");
            return; // Exit the program if input is invalid
        }

        // Find the closest pair of points
        int[] closestPair = findClosestPair(x_coords, y_coords);

        // Print the result
        System.out.println("The closest pair of points is: [" + closestPair[0] + ", " + closestPair[1] + "]");

        scanner.close(); // Close the scanner
    }
}

/* Testing Results:
Case 1:
Enter x-coordinates of points (comma-separated): 1,2,3,2,4
Enter y-coordinates of points (comma-separated): 2,3,1,2,3
The closest pair of points is: [0, 3]

Case 2:
Enter x-coordinates of points (comma-separated): 1,4,5,8,2
Enter y-coordinates of points (comma-separated): 3,5,2,7,1
The closest pair of points is: [0, 4]
*/