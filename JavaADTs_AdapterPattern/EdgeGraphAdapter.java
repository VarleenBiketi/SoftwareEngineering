import java.util.*;

public class EdgeGraphAdapter implements EdgeGraph {
    private Graph g;

    public EdgeGraphAdapter(Graph g) {
        this.g = g;
    }

    public boolean addEdge(Edge e) {
        // Automatically add nodes if they don't exist
        g.addNode(e.getSrc());
        g.addNode(e.getDst());
        return g.addEdge(e.getSrc(), e.getDst()); // Add edge and return result
    }

    public boolean hasNode(String n) {
        return g.hasNode(n); // Delegate to the underlying graph
    }

    public boolean hasEdge(Edge e) {
        return g.hasEdge(e.getSrc(), e.getDst()); // Check if edge exists
    }

    public boolean removeEdge(Edge e) {
        return g.removeEdge(e.getSrc(), e.getDst()); // Remove the edge and return result
    }

    public List<Edge> outEdges(String n) {
        if (!hasNode(n)) {
            throw new NoSuchElementException("Node not found: " + n);
        }
        List<Edge> edges = new ArrayList<>();
        for (String succ : g.succ(n)) {
            edges.add(new Edge(n, succ)); // Create edge for each successor
        }
        return edges; // Return list of outgoing edges
    }

    public List<Edge> inEdges(String n) {
        if (!hasNode(n)) {
            throw new NoSuchElementException("Node not found: " + n);
        }
        List<Edge> edges = new ArrayList<>();
        for (String pred : g.pred(n)) {
            edges.add(new Edge(pred, n)); // Create edge for each predecessor
        }
        return edges; // Return list of incoming edges
    }

    public List<Edge> edges() {
        List<Edge> allEdges = new ArrayList<>();
        for (String n : g.nodes()) {
            for (String succ : g.succ(n)) {
                allEdges.add(new Edge(n, succ)); // Add all edges to the list
            }
        }
        return allEdges; // Return the list of all edges
    }

    public EdgeGraph union(EdgeGraph eg) {
        EdgeGraph newGraph = new EdgeGraphAdapter(new ListGraph()); // Create a new EdgeGraph

        // Add edges from this graph
        for (Edge edge : this.edges()) {
            newGraph.addEdge(edge);
        }

        // Add edges from the other EdgeGraph
        for (Edge edge : eg.edges()) {
            newGraph.addEdge(edge);
        }

        return newGraph; // Return the union graph
    }

    public boolean hasPath(List<Edge> edges) {
        if (edges.isEmpty()) {
            return true; // The empty path is always present
        }
        for (int i = 0; i < edges.size() - 1; i++) {
            Edge e1 = edges.get(i);
            Edge e2 = edges.get(i + 1);
            if (!hasEdge(e1) || !e1.getDst().equals(e2.getSrc())) {
                return false; // If any edge is not present or not connecting correctly
            }
        }
        return true; // All edges are valid in the path
    }
}
