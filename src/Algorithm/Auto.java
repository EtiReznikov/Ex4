package Algorithm;

import java.util.ArrayList;
import GameObjects.Fruit;
import GameObjects.Player;
import Geom.Point3D;
import Map.MyCoords;
import Robot.Play;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;

/**
 * This class contains all the info in order to play automatic game 
 * it contains the shortest path that the player should go, checks if two points are divided by box 
 * @author Chen Ostrovski
 * @author Ester Reznikov
 */
public class Auto {
	private Play play;
	private ArrayList<Fruit> fruits;
	private Player player;
	private Boxinfo boxinfo;

	/**
	 * This method get all the info for the auto game: 
	 * it gets all the block, fruits and player locations 
	 * @param play 
	 */
	public Auto(Play play) {
		this.play=play;
		boxinfo= new Boxinfo(play);
		fruits=new ArrayList<Fruit>();
		// Gets the board info: the fruits and player locations
		for (String obj: play.getBoard()) {
			String [] data=obj.split(",");
			if (data[0].equals("F")) {
				fruits.add(new Fruit(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3]))));
			}
			else if (data[0].equals("M")) {
				player=new Player(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), Double.parseDouble(data[5]), Double.parseDouble(data[6]));
			}
		}
	}

	/**
	 * This method set the start location of the player under certain conditions
	 */
	public void setstart() {
		double velocity=0;
		Point3D fastest=null;
		boolean containsPackman=false;
		boolean setstart=false;
		// Goes over the board info
		for (String obj:play.getBoard()) {
			String  data []=obj.split(",");
			// If the board contains packman, set the player's position at the fastest packman location
			if (data[0].equals("P")) {
				containsPackman=true;
				if (Double.parseDouble(data[5])>velocity) {
					velocity=Double.parseDouble(data[5]);
					fastest=new Point3D (Double.parseDouble(data[2]),Double.parseDouble(data[3]));
					this.play.setInitLocation(fastest.x(),fastest.y());
					player.setLocation(fastest);
				}
			}
		}

		// If the board contains only fruits, set the player's position at a random fruit location
		if (!containsPackman) {
			int Fruitindex=(int)(Math.random() * ((fruits.size()) ));
			this.play.setInitLocation(fruits.get(Fruitindex).getLocation().x(),fruits.get(Fruitindex).getLocation().y());
			player.setLocation(fruits.get(Fruitindex).getLocation());
		}
	}

	/**
	 * This method finds the shortest path of a player by finding the closest fruits to him
	 * @param player the player location
	 * @param velocity the velocity of the player
	 * @return the shortest path of the player 
	 */
	public ArrayList<String> closestFruit(Point3D player, double velocity){
		double distance;
		ArrayList<String> shortest=new ArrayList<String>();
		if (boxinfo.boxes.isEmpty()) {
			Point3D closest=new Point3D(this.fruits.get(0).getLocation());
			double Mindistance=MyCoords.distance3d(player, closest);
			for (Fruit fruit:this.fruits) {
				distance=MyCoords.distance3d(player, fruit.getLocation());
				if (distance<Mindistance) {
					closest=new Point3D (fruit.getLocation());
					Mindistance=distance;
				}
			}
			shortest.add(player.toString());
			shortest.add(closest.toString());

		}
		else {
			// Finds path between two points
			Node path=nextpoint(player, this.fruits.get(0).getLocation(), this.boxinfo);
			// Finds the distance between the player and the first fruit and the time it took to him to get it 
			distance=path.getDist();
			double mintime=distance/velocity;
			shortest=path.getPath();
			double time=0;
			ArrayList<String> currentPath=new ArrayList<String>();
			for (Fruit fruit:this.fruits) {
							if (blockedbybox(player, fruit.getLocation(),this.boxinfo.boxes)) {
								distance=MyCoords.distance3d(player, fruit.getLocation());
								time=distance/velocity;
								currentPath.add(player.toString());
								currentPath.add(fruit.getLocation().toString());
							}		
						else {
								path=nextpoint(player, new Point3D(fruit.getLocation().x(),fruit.getLocation().y()), this.boxinfo);
								currentPath=path.getPath();
								// Finds the distance between the player and the first fruit and the time it took to him to get it 
								distance=path.getDist();
								time=distance/velocity;
							}
							// if the time is lower than min it mean that we found a shorter path
							if (time<mintime) {
								mintime=time;
								// clear the shortest path and add the shortest path 
								shortest.clear();
								shortest.addAll(currentPath);
								currentPath.clear();

							}
						}
					}
		return shortest;
	}

	/**
	 * Auxiliary function that help the function above to find path between two points 
	 * in order to find the shortest path
	 * @param source the first point location
	 * @param target the second point location
	 * @param boxinfo the info of every box in the game
	 * @return 
	 */
	public static Node nextpoint(Point3D source, Point3D target, Boxinfo boxinfo) {
		boxinfo.addPointstoNeighbors(source, target);
		Graph G = new Graph(); 
		// Add the valid points, source and target to the graph
		G.add(new Node(source.toString()));
		for (Point3D point:boxinfo.points) {
			G.add(new Node (point.toString()));
		}
		G.add(new Node(target.toString()));
		// contains all the edges that we added to the graph
		ArrayList<String[]> paths = new ArrayList<String[]>();
		// calculates the distance between point and its neighbors
		for (ArrayList<Point3D> points: boxinfo.neighbors) {
			Point3D start= points.get(0);
			for (int i=1; i<points.size(); i++) {
				String[] strattopoint=new String []{start.toString(),  points.get(i).toString()};
				String[] pointtostart=new String []{points.get(i).toString(), start.toString()};
				if (!(isInList(paths, strattopoint, pointtostart))) {
					G.addEdge(start.toString(), points.get(i).toString(), MyCoords.distance3d(start, points.get(i)));
					paths.add(new String []{start.toString(),  points.get(i).toString()});
				}
			}
		}
		Graph_Algo.dijkstra(G, source.toString());
		Node path = G.getNodeByName(target.toString());
		path.getPath().add(target.toString());
		boxinfo.neighbors.remove(boxinfo.neighbors.size()-1);
		boxinfo.neighbors.remove(boxinfo.neighbors.size()-1);
		return path;
	}

	/**
	 * Checks that the path between two points is single and there is no multiplication 
	 * for example: if the path between a to b is in the list checks if b to a is also there
	 * @param paths the arraylist that contains all the path of the player
	 * @param starttopoint the first point info
	 * @param pointtostart the second point info
	 * @return
	 */
	private static boolean isInList(ArrayList<String []> paths, String [] starttopoint, String[] pointtostart){
		if (paths.contains(starttopoint) || paths.contains(pointtostart))
			return true;
		return false;
	}

	/**
	 * This method Goes through all the possibilities of positioning two points so that they are not cut by box 
	 * every box divided into 4 part, when the upper left corner marked as [0] 
	 * the lower right corner marked as [2]
	 * @param source the first point location
	 * @param target the second point location
	 * @param boxes the info of the corners in every box in the game
	 * @return true: if the points not blocked false: if the points blocked
	 */
	public static boolean blockedbybox(Point3D source, Point3D target, ArrayList<Point3D[]> boxes) {
		boolean ans=true;

		// Goes over all the boxes in the game
		// Checks if the source and target are located in the same area relative to the box
		for (int i=0; i<boxes.size(); i++) {	
			boolean flag=true;
			if ((boxes.get(i)[3].x()>target.x() && boxes.get(i)[3].x()> source.x()) || ( boxes.get(i)[2].x()< source.x() && boxes.get(i)[2].x()<target.x()))
				flag=false;
			if((boxes.get(i)[3].y()<target.y() && boxes.get(i)[3].y()<source.y() )||  ( boxes.get(i)[0].y()>source.y() && boxes.get(i)[0].y()>target.y()))
				flag=false;

			if (boxes.get(i)[1].x()<source.x() && boxes.get(i)[1].y()>source.y() ) 
				if (target.equals(boxes.get(i)[0]) || target.equals(boxes.get(i)[1]) || target.equals(boxes.get(i)[2]))
					flag=false;
			if (boxes.get(i)[3].x()>source.x() && boxes.get(i)[3].y()<source.y() ) 
				if (target.equals(boxes.get(i)[0]) || target.equals(boxes.get(i)[3]) || target.equals(boxes.get(i)[2]))
					flag=false;
			if (boxes.get(i)[0].x()>source.x() && boxes.get(i)[0].y()>source.y() ) 
				if (target.equals(boxes.get(i)[0]) || target.equals(boxes.get(i)[1]) || target.equals(boxes.get(i)[3]))
					flag=false;
			if (boxes.get(i)[2].x()<source.x() && boxes.get(i)[2].y()<source.y() ) 
				if (target.equals(boxes.get(i)[1]) || target.equals(boxes.get(i)[3]) || target.equals(boxes.get(i)[2]))
					flag=false;


			// Sand to auxiliary function if none of the above are happened
			if(flag) {
				ans= pathcutbybox(source, target, boxes.get(i));
				if (!ans)
					return false;	
			}
		}
		return ans;
	}

	/**
	 * Auxiliary function that help the function above to check if two points are cut by box 
	 * the function is used when source and target are not located in the same area relative to the box 
	 * by using a straight equation 
	 * @param source the first point location
	 * @param target the second point location
	 * @param box the info of the corners in every box in the game
	 * @return true : if the path is not cut by box false : if the path is cut by box
	 */
	public static boolean pathcutbybox(Point3D source, Point3D target,Point3D [] box) {
		double y,x;
		double incline=(source.y()-target.y())/(source.x()-target.x());
		if (incline==0)
			return false;
		y=(box[0].x()-source.x())*incline+source.y();
		if (y>box[0].y() && y<box[3].y())
			return false;
		y=(box[1].x()-source.x())*incline+source.y();
		if (y>box[1].y() && y<box[2].y())
			return false;
		x=(box[0].y()-source.y())/incline+source.x();
		if (x>box[0].x() && x<box[1].x())
			return false;
		x=(box[3].y()-source.y())/incline+source.x();
		if (x>box[3].x() && x<box[2].x())
			return false;

		return true;
	}

	///////GET///////

	public Play getPlay() {
		return play;
	}
	public Player getPlayer() {
		return player;
	}
	public ArrayList<Fruit> getFruits() {
		return fruits;
	}

	///////SET///////

	public void setPlayer(Player player) {
		this.player = player;
	}
}
