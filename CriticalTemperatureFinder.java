import java.util.Scanner;
/* Question no. 1(a):
This program determines the minimum number of measurements required to find the critical temperature at which a sample fails.
 It uses dynamic programming to efficiently compute the optimal strategy. 
 The user provides two inputs: the number of identical samples (k) and the number of temperature levels (n). 
 A 2D array dp[k+1][n+1] is used, where dp[i][j] represents the minimum number of measurements needed with i samples and j temperature levels. 
 The base cases are initialized: if there is only one sample, all n levels must be tested one by one (dp[1][j] = j), 
 and if there is only one temperature level, only one test is needed (dp[i][1] = 1). 
 The program then fills the table by considering different temperature levels to test and calculating the worst-case scenario for each. 
 If a sample fails at a certain temperature, testing continues at lower levels, whereas if it survives, testing continues at higher levels. 
 The worst-case value is minimized across all possible test points. 
 Finally, the result stored in dp[k][n] gives the optimal number of measurements required. 
 This ensures an efficient strategy rather than a brute-force approach of testing each level sequentially.
 */

public class CriticalTemperatureFinder {

    public static int findMinMeasurements(int k, int n) {
        // Create a DP table
        int[][] dp = new int[k + 1][n + 1];

        // Fill base cases
        for (int i = 1; i <= k; i++) {
            dp[i][0] = 0; // 0 temperature levels need 0 measurements
            dp[i][1] = 1; // 1 temperature level needs 1 measurement
        }
        for (int j = 1; j <= n; j++) {
            dp[1][j] = j; // 1 sample needs j measurements for j temperature levels
        }

        // Fill the DP table for all combinations of samples and temperature levels
        for (int i = 2; i <= k; i++) { // For each number of samples
            for (int j = 2; j <= n; j++) { // For each number of temperature levels
                dp[i][j] = Integer.MAX_VALUE; // Start with a large number
                for (int x = 1; x <= j; x++) { // Try measuring at each temperature x
                    // Calculate the worst-case scenario
                    int worstCase = 1 + Math.max(dp[i - 1][x - 1], dp[i][j - x]);
                    // Update the minimum measurements needed
                    dp[i][j] = Math.min(dp[i][j], worstCase);
                }
            }
        }

        return dp[k][n]; // The result is in dp[k][n]
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for input
        System.out.print("Enter the number of identical samples (k): ");
        int k = scanner.nextInt();

        System.out.print("Enter the number of temperature levels (n): ");
        int n = scanner.nextInt();

        // Calculate and display the minimum number of measurements
        int result = findMinMeasurements(k, n);
        System.out.println("Minimum measurements required for k = " + k + ", n = " + n + ": " + result);

        // Close the scanner
        scanner.close();
    }
}
/* Testing Results:
Case 1:
 Enter the number of identical samples (k): 1 
 Enter the number of temperature levels (n): 2
 Minimum measurements required for k = 1, n = 2: 2

 Case 2:
 Enter the number of identical samples (k): 2
 Enter the number of temperature levels (n): 6
 Minimum measurements required for k = 2, n = 6: 3

 Case 3:
 Enter the number of identical samples (k): 3
 Enter the number of temperature levels (n): 14
 Minimum measurements required for k = 3, n = 14: 4

 Case 4:
 Enter the number of identical samples (k): 4
 Enter the number of temperature levels (n): 20
 Minimum measurements required for k = 4, n = 20: 5
 */
