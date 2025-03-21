/* Question no.2(a):
The algorithm calculates the minimum number of rewards needed for employees based on their ratings, ensuring that employees with higher ratings receive more rewards than their neighbors. It starts by initializing an array where each employee is assigned one reward. Then, the algorithm adjusts the rewards from left to right, increasing the reward of an employee if their rating is higher than the one before them. Next, it adjusts the rewards from right to left, ensuring that an employee with a higher rating than the one to their right receives more rewards, taking the maximum of their current reward or the right neighbor's reward plus one. Finally, the total rewards are calculated by summing all the rewards assigned to the employees, ensuring the minimum rewards are given while maintaining the rule that employees with higher ratings must get more rewards than their neighbors.*/
import java.util.Scanner;

public class MinRewards {

    public static int calculateMinimumRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];

        // Step 1: Give every employee at least 1 reward
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }

        // Step 2: Adjust rewards from left to right
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1; // Higher rating, more reward than the left
            }
        }

        // Step 3: Adjust rewards from right to left
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1); // Higher rating, more reward than the right
            }
        }

        // Step 4: Calculate total rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Take input for ratings
        System.out.print("Enter the ratings of employees as a comma-separated list: ");
        String[] input = scanner.nextLine().split(",");// Split the input string by commas
        int[] ratings = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            ratings[i] = Integer.parseInt(input[i].trim()); // Convert input to integers
        }

        // Calculate and display the result
        int result = calculateMinimumRewards(ratings);
        System.out.println("The minimum rewards needed are: " + result);

        scanner.close(); // Close the scanner
    }
}
/* Testing Results:
Case 1:
Enter the ratings of employees as a comma-separated list: 1,0,2
The minimum rewards needed are: 5

Case 2:
Enter the ratings of employees as a comma-separated list: 1,2,2
The minimum rewards needed are: 4

Case 3:
Enter the ratings of employees as a comma-separated list: 1,3,2,1
The minimum rewards needed are: 7
*/