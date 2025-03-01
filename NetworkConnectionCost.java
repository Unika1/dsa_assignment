/* Question No.3 (a):
Determine the minimum total cost to connect all devices in the network.

Algorithm Explanation:
1. Treat each device as a node in a graph. Devices can either be connected via direct connections or by installing communication modules.
2. Introduce a virtual node representing the central communication hub. Connect this virtual node to all devices with edges equal to the module installation cost.
3. Combine all the given direct connections and the virtual edges into a single list.
4. Sort all edges based on their cost in ascending order.
5. Apply Kruskal's algorithm to find the Minimum Spanning Tree (MST), ensuring all devices are connected with the minimum total cost.
6. Use the Union-Find data structure to efficiently detect cycles during MST formation.
7. Stop adding edges when all devices are connected, i.e., when we have n edges in total (including virtual edges).
*/

import java.util.*;

class NetworkConnection {
    // Class to represent an edge (connection) between two devices
    static class Edge {
        int device1, device2, cost;

        Edge(int d1, int d2, int c) {
            this.device1 = d1;
            this.device2 = d2;
            this.cost = c;
        }
    }

    // Union-Find (Disjoint Set) for Kruskal's Algorithm
    static class UnionFind {
        int[] parent, rank;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i; // Each element is its own parent initially
                rank[i] = 0;   // Initialize rank as 0
            }
        }

        // Find the root of a set with path compression
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Compress path for efficiency
            }
            return parent[x];
        }

        // Union by rank: Merging two sets
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false; // They are already connected

            // Attach the smaller tree under the bigger tree
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    public static int minNetworkCost(int n, int[] modules, int[][] connections) {
        List<Edge> edges = new ArrayList<>();
        int virtualNode = n; // Virtual node represented as node 'n'

        // Step 1: Add all direct connections to the list
        for (int[] conn : connections) {
            edges.add(new Edge(conn[0], conn[1], conn[2]));
        }

        // Step 2: Add virtual edges for module installation costs
        for (int i = 0; i < n; i++) {
            edges.add(new Edge(virtualNode, i, modules[i])); // Virtual node connected to each device
        }

        // Step 3: Sort edges by cost (cheapest connections first)
        edges.sort(Comparator.comparingInt(e -> e.cost));

        // Step 4: Use Kruskal’s Algorithm to find the Minimum Spanning Tree (MST)
        UnionFind uf = new UnionFind(n + 1); // n devices + 1 virtual node
        int totalCost = 0, edgesUsed = 0;

        for (Edge edge : edges) {
            if (uf.union(edge.device1, edge.device2)) { // If adding this edge doesn't form a cycle
                totalCost += edge.cost; // Add its cost
                edgesUsed++;

                // Stop when all devices are connected (n edges in total)
                if (edgesUsed == n) break;
            }
        }

        return totalCost; // Return the minimum cost to connect all devices
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: number of devices
        System.out.print("Enter number of devices: ");
        int n = scanner.nextInt();

        // Input: module installation costs
        int[] modules = new int[n];
        System.out.println("Enter module installation costs (comma separated): ");
        scanner.nextLine(); // Consume newline
        String[] moduleInput = scanner.nextLine().split(",");
        for (int i = 0; i < n; i++) {
            modules[i] = Integer.parseInt(moduleInput[i].trim()); // Convert to integers
        }

        // Input: number of direct connections
        System.out.println("Enter number of connections: ");
        int connCount = scanner.nextInt();
        int[][] connections = new int[connCount][3];

        System.out.println("Enter connections in format 'device1 device2 cost' per line:");
        for (int i = 0; i < connCount; i++) {
            connections[i][0] = scanner.nextInt();
            connections[i][1] = scanner.nextInt();
            connections[i][2] = scanner.nextInt();
        }

        scanner.close();

        // Compute and print the minimum network cost
        int result = minNetworkCost(n, modules, connections);
        System.out.println("Minimum total cost to connect all devices: " + result);
    }
}

/*
Testing Results:
Case 1:
Enter number of devices: 3
Enter module installation costs (comma separated): 1, 2, 2
Enter number of connections: 2
Enter connections in format 'device1 device2 cost' per line:
1 2 1
2 0 1
Minimum total cost to connect all devices: 3

Case 2:
Enter number of devices: 4
Enter module installation costs (comma separated): 4, 2, 3, 5
Enter number of connections: 4
Enter connections in format 'device1 device2 cost' per line:
0 1 1
1 2 2
2 3 3
0 3 4
Minimum total cost to connect all devices: 8
*/
