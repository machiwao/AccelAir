import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class InitializeRoutes {
	public static ArrayList<String[]> routesList = new ArrayList<>();
	public static ArrayList<String> airports = new ArrayList<>();
	public static Graph travelMap;
	
	public static void storeRoutes() { //Storing routes in local ArrayList
		routesList.clear();
		try {
			BufferedWriter initialize = new BufferedWriter(new FileWriter("RoutesList.txt", true));
			BufferedReader readRoutes = new BufferedReader(new FileReader("RoutesList.txt"));
			String currentLine;
			while((currentLine = readRoutes.readLine()) != null) {
				if(!routesList.contains(currentLine.split("\t")))
					routesList.add(currentLine.split("\t"));
			}
			readRoutes.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void addComboBoxItems() {
		airports.clear();
		for(String[] route: routesList) {
			if(!airports.contains(route[0])) {
				airports.add(route[0]);
			}
			if(!airports.contains(route[1])) {
				airports.add(route[1]);
			}
		}
		Collections.sort(airports);
	}
	
	public static void createGraph() {
		//Adding nodes in Graph DS
		travelMap  = new Graph();
		for(String airport : airports) {
			travelMap.addNode(new Node(airport));
		}
		//Setting edge weights and connecting airports based on routes
		for(String[] route : routesList) {
			travelMap.findNode(route[0]).addDestination(travelMap.findNode(route[1]), Double.parseDouble(route[2]));
		}
	}
}
