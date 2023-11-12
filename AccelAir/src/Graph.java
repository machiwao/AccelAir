import java.util.HashSet;
import java.util.Set;

public class Graph {

    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
    
    public Node findNode(String name) {
    	for(Node currentNode : nodes) {
    		if(currentNode.getName().equals(name))
    			return currentNode;
    	}
    	return null;
    }
    
    public void removeNode(String name) {
    	nodes.remove(findNode(name));
    }
}