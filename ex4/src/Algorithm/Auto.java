package Algorithm;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import GameObjects.Fruit;
import GameObjects.Player;
import Geom.Point3D;
import Map.Map;
import Map.MyCoords;
import Robot.Play;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;




public class Auto {
	public Play play;
	private ArrayList<Fruit> fruits;
	private Player player;

	public static void main(String[] args) {

		//	Play play=new Play("C:\\Users\\etire\\OneDrive\\מסמכים\\data\\Ex4_OOP_example8.csv");
		//	Auto auto=new Auto(play);
		//	ArrayList<Point3D []> boxes=auto.boxinfo.boxes;
		//	auto.startgame();



		ArrayList<Point3D []> boxes= new ArrayList<Point3D []>() ;
		Point3D a=new Point3D (290,190);;
		Point3D b=new Point3D (579,190,0);
		Point3D c=new Point3D (579,352,0);
		Point3D d=new Point3D (290,352,0);
		boxes.add(new Point3D[4]);
		boxes.get(0)[0]=a;
		boxes.get(0)[1]=b;
		boxes.get(0)[2]=c;
		boxes.get(0)[3]=d;
		a=new Point3D (528,257);;
		b=new Point3D (773,257);
		c=new Point3D (773,430);
		d=new Point3D (528,430);
		boxes.add(new Point3D[4]);
		boxes.get(1)[0]=a;
		boxes.get(1)[1]=b;
		boxes.get(1)[2]=c;
		boxes.get(1)[3]=d;
		a=new Point3D (750,298);;
		b=new Point3D (1065,298);
		c=new Point3D (1065,402);
		d=new Point3D (750,402);
		boxes.add(new Point3D[4]);
		boxes.get(2)[0]=a;
		boxes.get(2)[1]=b;
		boxes.get(2)[2]=c;
		boxes.get(2)[3]=d;

		Point3D A = new Point3D(174, 429);
		Point3D B = new Point3D(885, 134);

		Boxinfo box=new Boxinfo(boxes);

		/*for (Point3D point: box.points)
			System.out.println(point.toString());*/
		/*
		for (ArrayList<Point3D> current:box.neighbors) {
			for (Point3D point:current)
				System.out.println(point.toString());
			System.out.println();
		}

		System.out.println(true);

		 */
		//	box.addPointstoNeighbors(A, B);
		/*	for (ArrayList<Point3D> current:box.neighbors) {
			for (Point3D point:current)
				System.out.println(point.toString());
			System.out.println();
		}*/

		Node tar=nextpoint(A,B,box);
		ArrayList<String> shortestPath = tar.getPath();
		System.out.println("***** Graph Demo for OOP_Ex4 *****");
		System.out.println(tar);
		System.out.println("Dist: "+tar.getDist());
		for(int i=0;i<shortestPath.size();i++) {
			//System.out.print(","+shortestPath.get(i));
			System.out.print(shortestPath.get(i)+",");
			System.out.println();

		}
	}




	public Auto(Play play) {
		this.play=play;
		boxinfo= new Boxinfo(play);
		fruits=new ArrayList<Fruit>();
		for (String obj: play.getBoard()) {
			String [] data=obj.split(",");
			if (data[0].equals("F")) {
				fruits.add(new Fruit(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), Integer.parseInt(data[1])));
			}
			else if (data[0].equals("M")) {
				player=new Player(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), Double.parseDouble(data[5]), Double.parseDouble(data[6]));
			}
		}
	}



	private Boxinfo boxinfo;

	public Player getPlayer() {
		return player;
	}




	public void setPlayer(Player player) {
		this.player = player;
	}


	/*
	public void startgame() {
		play.start();
		String [] data;
		double velocity=this.player.getVeloctiy();
		while (play.isRuning()) {
			/*
			for (String obj: this.play.getBoard()) {
				data=obj.split(",");
				if (data[0].equals("M")){
					velocity=Double.parseDouble(data[5]);
					//player= new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3]));
				}	
			}

			ArrayList<String> shortestPath=closestFruit(player.getLocation(), velocity);
			String[] fruit=shortestPath.get(shortestPath.size()-1).split(",");
			Fruit fruitlocation=new Fruit (new Point3D (Double.parseDouble(fruit[0]), Double.parseDouble(fruit[1])),Integer.parseInt( fruit[2]));
			while (fruits.contains(fruitlocation)){
				for(int i=0;i<shortestPath.size();i++) {
					String[] nextpoint=shortestPath.get(i).split(",");
					double[] azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
							(player.getLocation(), new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
					while (azimuth_elevation_dist[2]> player.getRadius()) {
						azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
								(player.getLocation(), new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
						play.rotate(azimuth_elevation_dist[0]);
						fruits.removeAll(fruits);
						for (String obj: this.play.getBoard()) {
							data=obj.split(",");
							if (data[0].equals("M")){
								player.setLocation(new Point3D(Double.parseDouble(data[2]), Double.parseDouble(data [3])));
							}
							else if (data[0].equals("F")) {
								fruits.add(new Fruit(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), Integer.parseInt(data[1])));
							}
						}
						player.setLocation(new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
					}
				}
			}
		}
	}
	 */
	public ArrayList<Fruit> getFruits() {
		return fruits;
	}




	public void setstart() {

		Point3D p=new Point3D(this.fruits.get(0).getLocation());
		this.play.setInitLocation(p.x(),p.y());
		player.setLocation(p);

	}

	public 	ArrayList<String> closestFruit(Point3D player, double velocity){
		String [] data;
		//data=this.play.getBoard().get(0).split(",");
		Node path=nextpoint(player, this.fruits.get(0).getLocation(), this.boxinfo);
		double distance=path.getDist();
		double min=distance/velocity;
		ArrayList<String> shortest=path.getPath();
		shortest.set(0, shortest.get(shortest.size()-1)+","+ this.fruits.get(0).getId());
		double time=0;
		for (String obj: this.play.getBoard()) {
			data=obj.split(",");
			if (data[0].equals("F")) {
				/*
			}
				if (!(blockedbybox(player,new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])) , this.boxinfo.boxes))) {
					distance = MyCoords.distance3d(player, new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])));
					time=distance/velocity;
				}
				else {
				ArrayList<String> path=nextpoint(player, new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), this.boxinfo.boxes);
				 */
				path=nextpoint(player, new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), this.boxinfo);
				ArrayList<String> shortestPath=new ArrayList<String>(path.getPath());
				// s=shortestPath.get(shortestPath.size()-1)+data[1];
				//		shortestPath.get(shortestPath.size()-1).replaceAll(shortestPath.get(shortestPath.size()-1), shortestPath.get(shortestPath.size()-1)+","+data[1]);
				shortestPath.set(shortestPath.size()-1, shortestPath.get(shortestPath.size()-1)+","+data[1]);
				distance=path.getDist();
				time=distance/velocity;

				if (time<=min) {
					min=time;
					shortest=shortestPath;
				}
			}
		}
		//	if (shortest.contains("0.0,0.0,0.0"))
		//	shortest.remove("0.0,0.0,0.0");
		return shortest;
	}


	public static Node nextpoint(Point3D source , Point3D target, Boxinfo boxinfo) {
		boxinfo.addPointstoNeighbors(source, target);
		for (ArrayList<Point3D> current:boxinfo.neighbors) {

			for (Point3D point:current)
				System.out.println(point.toString());
			System.out.println();
		}
		Graph G = new Graph(); 
		G.add(new Node(source.toString())); 
		for (Point3D point:boxinfo.points) {
			G.add(new Node(point.toString()));
		}
		G.add(new Node(target.toString())); 
		ArrayList<String []> paths = new ArrayList<String[]>();
		//	paths.add(new String []{source.toString(),  source.toString()});
		for (ArrayList<Point3D> points: boxinfo.neighbors) {
			Point3D start= points.get(0);
			//	G.addEdge(source.toString(), start.toString(), MyCoords.distance3d(source, start));
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

		//	System.out.println(G.getNodeByName(target.toString()));
		//Node tar = G.getNodeByName(target.toString());
		Node tar = G.getNodeByName(target.toString());
		tar.getPath().add(target.toString());
		/*
		ArrayList<String> shortestPath = tar.getPath();
		for(int i=0;i<shortestPath.size();i++) {
			//System.out.print(","+shortestPath.get(i));
			System.out.print(shortestPath.get(i)+",");
			System.out.println();
		}
		/*
	//	ArrayList<String> shortestPath = tar.getPath();
		Graph finalG = new Graph(); 
		G.add(new Node(target.toString())); 
		for (int i=0 ;i<shortestPath.size()-1; i++){
			String [] current= shortestPath.get(i).split(",");
			String [] next= shortestPath.get(i+1).split(",");
			finalG.addEdge(shortestPath.get(i), shortestPath.get(i+1),
					MyCoords.distance3d(new Point3D (Double.parseDouble(current[0]),Double.parseDouble(current[1]))
							,new Point3D (Double.parseDouble(next[0]),Double.parseDouble(next[1]))));
		}
		String [] current= shortestPath.get(shortestPath.size()-1).split(",");
		//String [] next= target.toString().split(",");
		finalG.addEdge(shortestPath.get(shortestPath.size()-1), target.toString(),
				MyCoords.distance3d(new Point3D (Double.parseDouble(current[0]),Double.parseDouble(current[1]))
						,target));


			for(int i=1;i<shortestPath.size();i++) {
				String[] previouspoint=shortestPath.get(i-1).split(",");
				String[] nextpoint=shortestPath.get(i).split(",");
				double[] azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
						(new Point3D (Double.parseDouble(previouspoint[0]),Double.parseDouble(previouspoint[1])), new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
				play.rotate(azimuth_elevation_dist[0]);
			}

		Graph_Algo.dijkstra(finalG, source.toString());
		 */
		// tar = G.getNodeByName(target.toString());

		boxinfo.neighbors.remove(boxinfo.neighbors.size()-1);
		boxinfo.neighbors.remove(boxinfo.neighbors.size()-1);
		return tar;
	}


	private static boolean isInList(ArrayList<String []> paths, String [] starttopoint, String[] pointtostart){
		for( String[] item : paths){
			if(Arrays.equals(starttopoint, item) || Arrays.equals(pointtostart, item)){
				return true;
			}
		}
		return false;
	}

	public static boolean blockedbybox(Point3D source , Point3D target, ArrayList<Point3D []> boxes) {
		boolean ans=false;
		for (int i=0; i<boxes.size(); i++) {	
			boolean flag=true;
			/*	if ((boxes.get(i)[3].x()>target.x() && boxes.get(i)[3].x()> source.x()) || ( boxes.get(i)[2].x()< source.x() && boxes.get(i)[2].x()<target.x())) {
				if (target.y()>boxes.get(i)[2].y() && source.y()>boxes.get(i)[2].y())
					flag=false;
				if (target.y()<boxes.get(i)[1].y() && source.y()<boxes.get(i)[1].y())
					flag=false;
			}
			if((boxes.get(i)[3].y()<target.y() && boxes.get(i)[3].y()<source.y() )||  ( boxes.get(i)[0].y()>source.y() && boxes.get(i)[0].y()>target.y())) {
				if (target.x()<boxes.get(i)[0].x() && source.x()<boxes.get(i)[0].x())
					flag=false;
				if (target.x()>boxes.get(i)[2].x() && source.x()>boxes.get(i)[2].x())
					flag=false;
			}*/
			if ((boxes.get(i)[3].x()>target.x() && boxes.get(i)[3].x()> source.x()) || ( boxes.get(i)[2].x()< source.x() && boxes.get(i)[2].x()<target.x()))
				flag=false;
			if((boxes.get(i)[3].y()<target.y() && boxes.get(i)[3].y()<source.y() )||  ( boxes.get(i)[0].y()>source.y() && boxes.get(i)[0].y()>target.y()))
				flag=false;
			if(flag) {
				ans= pathcutbybox(source, target, boxes.get(i));
				if (!ans)
					return false;	
			}
		}
		return ans;
	}

	public static boolean pathcutbybox(Point3D source, Point3D target,Point3D [] box) {
		double Incline=(source.y()-target.y())/(source.x()-target.x());
		boolean flag;

		double y=(box[0].x()-source.x())*Incline+source.y();
		/*
		if (y>=box[0].y() && y<=box[3].y())
			return false; 
		y=(box[1].x()-source.x())*Incline+source.y();
		if (y>=box[1].y() && y<=box[2].y())
			return false;
		if (Incline!=0) {
		double x=(box[0].y()-source.y())/Incline+source.x();
		if (x<=box[1].x() && x>=box[0].x())
			return false;
		x=(box[3].y()-source.y())/Incline+source.x();
		if (x<=box[2].x() && x>=box[3].x())
			return false;
		}

		 */
		if (Incline==1)
			return false;
		if (y>box[0].y() && y<box[3].y())
			return false; 
		y=(box[1].x()-source.x())*Incline+source.y();
		if (y>box[1].y() && y<box[2].y())
			return false;
		double x=(box[0].y()-source.y())/Incline+source.x();
		if (x<box[1].x() && x>box[0].x())
			return false;
		x=(box[3].y()-source.y())/Incline+source.x();
		if (x<box[2].x() && x>box[3].x())
			return false;

		return true;
	}

	public Play getPlay() {
		return play;
	}
}
