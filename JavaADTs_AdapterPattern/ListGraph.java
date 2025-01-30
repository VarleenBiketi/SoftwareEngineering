import java.util.*;

public class ListGraph implements Graph {
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    public boolean addNode(String n) {
        if (nodes.containsKey(n)) {
            return false; // Node already exists
        }
        nodes.put(n, new LinkedList<>()); // Add the node with an empty list of successors
        return true; // Node added
    }

    public boolean addEdge(String n1, String n2) {
        if (!hasNode(n1) || !hasNode(n2)) {
            throw new NoSuchElementException("One or both nodes not found: " + n1 + ", " + n2);
        }
        if (nodes.get(n1).contains(n2)) {
            return false; // Edge already exists
        }
        nodes.get(n1).add(n2); // Add edge from n1 to n2
        return true; // Edge added
    }

    public boolean hasNode(String n) {
        return nodes.containsKey(n); // Check if the node exists
    }

    public boolean hasEdge(String n1, String n2) {
        return hasNode(n1) && nodes.get(n1).contains(n2); // Check if the edge exists
    }

    public boolean removeNode(String n) {
        if (!hasNode(n)) {
            return false; // Node doesn't exist
        }
        nodes.remove(n); // Remove the node
        // Remove all edges to this node from other nodes
        for (String key : nodes.keySet()) {
            nodes.get(key).remove(n); // Remove edges from other nodes to n
        }
        return true; // Node removed
    }

    public boolean removeEdge(String n1, String n2) {
        if (!hasNode(n1) || !hasNode(n2)) {
            throw new NoSuchElementException("One or both nodes not found: " + n1 + ", " + n2);
        }
        return nodes.get(n1).remove(n2); // Remove the edge if it exists
    }

    public List<String> nodes() {
        return new ArrayList<>(nodes.keySet()); // Return a list of all nodes
    }

    public List<String> succ(String n) {
        if (!hasNode(n)) {
            throw new NoSuchElementException("Node not found: " + n);
        }
        return new ArrayList<>(nodes.get(n)); // Return the successors of the node
    }

    public List<String> pred(String n) {
        if (!hasNode(n)) {
            throw new NoSuchElementException("Node not found: " + n);
        }
        List<String> predecessors = new ArrayList<>();
        for (String key : nodes.keySet()) {
            if (nodes.get(key).contains(n)) {
                predecessors.add(key); // If key has an edge to n, add key to predecessors
            }
        }
        return predecessors; // Return the list of predecessors
    }

    public Graph union(Graph g) {
        List<String> allNodes = g.nodes();
        Graph newGraph = new ListGraph();

        // Add all nodes from this graph
        for (String node : nodes.keySet()) {
            newGraph.addNode(node);
        }

        // Add all edges from this graph
        for (String n1 : nodes.keySet()) {
            for (String n2 : nodes.get(n1)) {
                newGraph.addEdge(n1, n2);
            }
        }

        // Add all nodes and edges from the other graph
        for (String node : allNodes) {
            newGraph.addNode(node);
            List<String> successors = g.succ(node);
            for (String successor : successors) {
                newGraph.addEdge(node, successor);
            }
        }
        
        return newGraph; // Return the new graph containing all nodes and edges
    }

    public Graph subGraph(Set<String> nodeSet) {
        Graph subGraph = new ListGraph();
        for (String node : nodeSet) {
            if (hasNode(node)) {
                subGraph.addNode(node);
            }
        }
        // Add edges among the nodes in the subGraph
        for (String n1 : nodeSet) {
            if (hasNode(n1)) {
                for (String n2 : succ(n1)) {
                    if (nodeSet.contains(n2)) {
                        subGraph.addEdge(n1, n2);
                    }
                }
            }
        }
        return subGraph; // Return the subGraph
    }

    public boolean connected(String n1, String n2) {
        if (!hasNode(n1) || !hasNode(n2)) {
            throw new NoSuchElementException("One or both nodes not found: " + n1 + ", " + n2);
        }
        // Use BFS or DFS to check if there's a path from n1 to n2
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(n1);
        visited.add(n1);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(n2)) {
                return true; // Found a path to n2
            }
            for (String successor : succ(current)) {
                if (!visited.contains(successor)) {
                    visited.add(successor);
                    queue.add(successor);
                }
            }
        }
        return false; // No path found
    }
}
