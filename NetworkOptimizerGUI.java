// /* Question no.5:
//  * This program implements a Network Optimizer that helps in designing an efficient network topology using Minimum Spanning Tree (MST) 
//  * and Shortest Path (Dijkstra). The user can visualize the network as nodes (servers/clients) and edges (connections with costs and bandwidths).
//  * It computes the MST to minimize the total cost and finds the shortest path between nodes considering both cost and bandwidth. 
//  * The GUI enables the user to interactively design a network topology and calculate optimal paths.
// */

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class NetworkOptimizerGUI extends JFrame {

    // Graph representation: nodes and edges with costs and bandwidths
    private Map<String, Map<String, Edge>> graph = new HashMap<>();
    private JTextArea outputArea; // Text area to display results
    private JPanel graphPanel; // Panel to visualize the graph
    private Map<String, Point> nodePositions; // Store positions of nodes

    // Constructor to initialize the GUI
    public NetworkOptimizerGUI() {
        setTitle("Network Topology Optimizer"); // Set window title
        setSize(1000, 800); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setLayout(new BorderLayout()); // Use BorderLayout for the frame

        // Initialize node positions
        nodePositions = new HashMap<>();

        // Create a panel for graph visualization
        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g); // Draw the graph on the panel
            }
        };
        graphPanel.setBackground(Color.WHITE); // Set background color
        add(graphPanel, BorderLayout.CENTER); // Add graph panel to the center

        // Create a panel for controls and output
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Button to add a node
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(e -> addNode());
        controlPanel.add(addNodeButton);

        // Button to add an edge
        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(e -> addEdge());
        controlPanel.add(addEdgeButton);

        // Button to optimize the network
        JButton optimizeButton = new JButton("Optimize Network");
        optimizeButton.addActionListener(e -> optimizeNetwork());
        controlPanel.add(optimizeButton);

        // Button to calculate the shortest path
        JButton shortestPathButton = new JButton("Calculate Shortest Path");
        shortestPathButton.addActionListener(e -> calculateShortestPath());
        controlPanel.add(shortestPathButton);

        // Output area to display results
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        controlPanel.add(scrollPane);

        add(controlPanel, BorderLayout.EAST); // Add control panel to the right
    }

    // Method to add a node to the graph
    private void addNode() {
        String nodeName = JOptionPane.showInputDialog(this, "Enter node name:");
        if (nodeName != null && !nodeName.isEmpty()) {
            graph.put(nodeName, new HashMap<>()); // Add node to the graph

            // Generate random position for the new node within panel bounds
            int x = (int) (Math.random() * (graphPanel.getWidth() - 60)); // Random x position
            int y = (int) (Math.random() * (graphPanel.getHeight() - 60)); // Random y position

            nodePositions.put(nodeName, new Point(x, y)); // Store node position

            graphPanel.repaint(); // Redraw the graph
            outputArea.append("Node added: " + nodeName + " at (" + x + ", " + y + ")\n"); // Update output area

            // Debug: Print node positions
            System.out.println("Node Positions: " + nodePositions);
        }
    }

    // Method to add an edge to the graph
    private void addEdge() {
        String fromNode = JOptionPane.showInputDialog(this, "Enter source node:");
        String toNode = JOptionPane.showInputDialog(this, "Enter target node:");
        String costStr = JOptionPane.showInputDialog(this, "Enter cost:");
        String bandwidthStr = JOptionPane.showInputDialog(this, "Enter bandwidth:");

        if (fromNode != null && toNode != null && costStr != null && bandwidthStr != null) {
            try {
                int cost = Integer.parseInt(costStr);
                int bandwidth = Integer.parseInt(bandwidthStr);
                graph.get(fromNode).put(toNode, new Edge(cost, bandwidth)); // Add edge to the graph
                graphPanel.repaint(); // Redraw the graph
                outputArea.append("Edge added: " + fromNode + " -> " + toNode +
                                  " (Cost: " + cost + ", Bandwidth: " + bandwidth + ")\n"); // Update output area

                // Debug: Print graph structure
                System.out.println("Graph: " + graph);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for cost or bandwidth.");
            }
        }
    }

    // Method to optimize the network (minimize cost and latency)
    private void optimizeNetwork() {
        // Placeholder for optimization algorithm (e.g., Kruskal's or Prim's for MST)
        outputArea.append("Optimizing network...\n");
        // TODO: Implement optimization logic here
    }

    // Method to calculate the shortest path between two nodes
    private void calculateShortestPath() {
        String fromNode = JOptionPane.showInputDialog(this, "Enter source node:");
        String toNode = JOptionPane.showInputDialog(this, "Enter target node:");

        if (fromNode != null && toNode != null) {
            // Placeholder for shortest path algorithm (e.g., Dijkstra's)
            outputArea.append("Calculating shortest path from " + fromNode + " to " + toNode + "...\n");
            // TODO: Implement shortest path logic here
        }
    }

    // Method to draw the graph on the panel
    private void drawGraph(Graphics g) {
        int nodeRadius = 30; // Radius of each node

        // Draw nodes
        for (String node : nodePositions.keySet()) {
            Point position = nodePositions.get(node);
            g.setColor(Color.BLUE);
            g.fillOval(position.x, position.y, nodeRadius, nodeRadius); // Draw node as a filled circle
            g.setColor(Color.BLACK);
            g.drawString(node, position.x + 10, position.y + 20); // Label the node
        }

        // Draw edges
        g.setColor(Color.RED);
        for (String fromNode : graph.keySet()) {
            Point fromPoint = nodePositions.get(fromNode);
            for (String toNode : graph.get(fromNode).keySet()) {
                Point toPoint = nodePositions.get(toNode);

                // Draw zigzag edge
                drawZigzagEdge(g, fromPoint, toPoint, nodeRadius);

                // Display edge description (cost and bandwidth)
                Edge edge = graph.get(fromNode).get(toNode);
                String description = "Cost: " + edge.cost + ", Bandwidth: " + edge.bandwidth;
                int midX = (fromPoint.x + toPoint.x) / 2;
                int midY = (fromPoint.y + toPoint.y) / 2;
                g.drawString(description, midX, midY); // Draw description at the midpoint of the edge
            }
        }
    }

    // Method to draw a zigzag edge between two points
    private void drawZigzagEdge(Graphics g, Point fromPoint, Point toPoint, int nodeRadius) {
        int segments = 5; // Number of segments for the zigzag
        int deltaX = (toPoint.x - fromPoint.x) / segments; // Horizontal distance per segment
        int deltaY = (toPoint.y - fromPoint.y) / segments; // Vertical distance per segment

        int x1 = fromPoint.x + nodeRadius / 2; // Start from the center of the source node
        int y1 = fromPoint.y + nodeRadius / 2;

        for (int i = 1; i <= segments; i++) {
            int x2 = fromPoint.x + i * deltaX;
            int y2 = fromPoint.y + i * deltaY;

            // Alternate the direction of the segments to create a zigzag effect
            if (i % 2 == 0) {
                y2 += 20; // Move down
            } else {
                y2 -= 20; // Move up
            }

            g.drawLine(x1, y1, x2, y2); // Draw the segment
            x1 = x2;
            y1 = y2;
        }

        // Draw the final segment to the target node
        g.drawLine(x1, y1, toPoint.x + nodeRadius / 2, toPoint.y + nodeRadius / 2);
    }

    // Inner class to represent an edge with cost and bandwidth
    private static class Edge {
        int cost;
        int bandwidth;

        Edge(int cost, int bandwidth) {
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NetworkOptimizerGUI gui = new NetworkOptimizerGUI();
            gui.setVisible(true); // Make the GUI visible
        });
    }
}
