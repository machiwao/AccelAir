import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {

    private String name;

    private LinkedList<Node> shortestPath = new LinkedList<>();

    private Double time = Double.MAX_VALUE;

    private Map<Node, Double> adjacentNodes = new HashMap<>();

    public Node(String name) {
        this.name = name;
    }

    public void addDestination(Node destination, double time) {
        adjacentNodes.put(destination, time);
    }
    
    public void removePath(Node destination) {
    	adjacentNodes.remove(destination);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Node, Double> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

}