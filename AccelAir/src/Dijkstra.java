import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

public class Dijkstra {

    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {

        source.setTime(0.0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getFastestTimeNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry<Node, Double> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Double edgeWeight = adjacencyPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumTime(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        
        return graph;
    }
    //compare actual time compared to calculated one in the newly explored path
    private static void CalculateMinimumTime(Node evaluationNode, Double edgeWeight, Node sourceNode) {
        Double sourceTime = sourceNode.getTime();
        if (sourceTime + edgeWeight < evaluationNode.getTime()) {
            evaluationNode.setTime(sourceTime + edgeWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
    //returns node with fastest time from unsettled nodes set
    private static Node getFastestTimeNode(Set<Node> unsettledNodes) {
        Node fastestTimeNode = null;
        double fastestTime = Double.MAX_VALUE;
        for (Node node : unsettledNodes) {
            double nodeTime = node.getTime();
            if (nodeTime < fastestTime) {
                fastestTime = nodeTime;
                fastestTimeNode = node;
            }
        }
        return fastestTimeNode;
    }
}