package Algorithm;

import java.util.Iterator;

import Geom.Point3D;
import Map.MyCoords;
import Robot.Fruit;
import Robot.Packman;
import Robot.Play;

public class AutomaticPlayer {
	private Play play;
	public AutomaticPlayer (Play play) {
		this.play=play;
	}
	private Play setStartLocation() {
		Point3D fastestPackman;
		boolean packman=false;
		boolean ghost=false;
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			if (data[0].equals("P"))
				packman=true;
			if (data[0].equals("G"))
				ghost=true;
		}

		if (packman && ghost) {
			Packman startlocation=fastestPackman_farthestghost();
			String[] data=startlocation.toString().split(",");
			double x=Double.parseDouble(data[2]);
			double y=Double.parseDouble(data[3]);
			play.setInitLocation(x, y);
		}
		else if (packman) {
			Packman startlocation=fastestPackman();
			String[] data=startlocation.toString().split(",");
			double x=Double.parseDouble(data[2]);
			double y=Double.parseDouble(data[3]);
			play.setInitLocation(x, y);
		}
		else if (ghost) {
			Fruit startlocation=farthestfruit();
			String[] data=startlocation.toString().split(",");
			double x=Double.parseDouble(data[2]);
			double y=Double.parseDouble(data[3]);
			play.setInitLocation(x, y);
		}
		else {
			boolean flag=true;
			while (flag) {
				for (String obj: play.getBoard()) {
					String[] data=obj.split(",");
					if (data[0].equals("F")) {
						double x=Double.parseDouble(data[2]);
						double y=Double.parseDouble(data[3]);
						play.setInitLocation(x, y);
						flag=false;
					}
				}
			}
		}
		return play;

	}
	private Packman fastestPackman_farthestghost(){
		Packman fastest=null;
		double maxspeed=0;
		double maxdistance=0;
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			if (data[0].equals("P")) {
				double speed=Double.parseDouble(data[5]);
				if (speed>maxspeed) {
					fastest=new Packman(obj);
					maxspeed=speed;
				}
				else if (maxspeed==speed) {
					double x=Double.parseDouble(data[2]);
					double y=Double.parseDouble(data[3]);
					Point3D packmanLocation=new Point3D(x,y);
					Packman farsest=farthestghost(obj);
					String[] ghost=farsest.toString().split(",");
					x=Double.parseDouble(ghost[2]);
					y=Double.parseDouble(ghost[3]);
					Point3D ghostLocation=new Point3D(x,y);
					double distnace=MyCoords.distance3d(packmanLocation, ghostLocation);
					if (maxdistance>distnace) {
						fastest=new Packman(obj);
						maxspeed=speed;
						maxdistance=distnace;
					}
				}
			}
		}
		return fastest;
	}
	private Fruit farthestfruit() {
		Fruit fruit=null;
		double maxdistance=0;
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			if (data[0].equals("F")) {
				double x=Double.parseDouble(data[2]);
				double y=Double.parseDouble(data[3]);
				Point3D packmanLocation=new Point3D(x,y);
				Packman farthestghost=farthestghost(obj);
				String[] ghost=farthestghost.toString().split(",");
				x=Double.parseDouble(ghost[2]);
				y=Double.parseDouble(ghost[3]);
				Point3D ghostLocation=new Point3D(x,y);
				double distnace=MyCoords.distance3d(packmanLocation, ghostLocation);
				if (maxdistance>distnace) {
					fruit=new Fruit(obj);
					maxdistance=distnace;
				}
			}
		}
		return fruit;
	}
	private Packman fastestPackman() {
		Packman fastest=null;
		double maxspeed=0;
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			if (data[0].equals("P")) {
				double speed=Double.parseDouble(data[5]);
				if (speed>maxspeed) {
					maxspeed=speed;
					fastest=new Packman(obj);
				}
			}
		}
		return fastest;
	}
	private Packman farthestghost(String object) {
		String[] objinfo=object.split(",");
		double x=Double.parseDouble(objinfo[2]);
		double y=Double.parseDouble(objinfo[3]);
		Point3D packmanLocation=new Point3D(x,y);
		double maxdistance=0;
		Packman farthest=null;
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			if (data[0].equals("G")) {
				x=Double.parseDouble(data[2]);
				y=Double.parseDouble(data[3]);
				Point3D ghostLocation=new Point3D(x,y);
				double distnace=MyCoords.distance3d(packmanLocation, ghostLocation);
				if (distnace>maxdistance) {
					maxdistance=distnace;
					farthest=new Packman(obj);
				}
			}
		}
		return farthest;
	}
	
	
	/*
	package Algorithms;

	import java.util.ArrayList;
	import java.util.Iterator;
	import javax.swing.plaf.synth.SynthSpinnerUI;
	import Coords.MyCoords;
	import GIS.Fruit;
	import GIS.Game;
	import GIS.Metadata;
	import GIS.Packman;

	/**
	 * This class contains the algorithm of the shortest path to every Packman 
	 * @author Ester Reznikov and Chen Ostrovski
	 
	public class ShortestPathAlgo {

		Game game;
		public ArrayList<Fruit> existing_fruits;
		public ArrayList<Packman> packmen;
		static MyCoords mycoords;
		public ShortestPathAlgo(Game game){
			this.game=game;
			existing_fruits=new ArrayList<Fruit>(game.fruits);
			packmen=new ArrayList<Packman>(game.packmen);
			mycoords= new MyCoords();
		}

		/**
		 * This function contains the algotithm. It calculates the shortest path of every Packman 
		 * The shortest path mean the fastest time it will take to all the Packmen to eat all the Fruits
		 * @return Solution contains all the data of the game
		 
		public Solution ShortestPath() {
			Solution solution=new Solution();
			// If Packmen exist in the game
			if (!packmen.isEmpty()) {
				updateClosest();
				Packman fastest=fastest();
				// While Fruits exist in the game
				while (!existing_fruits.isEmpty()) {
					updateFastest(fastest);
					long timetofirst=fastest.getClosestFruit().getTime();
					// Remove from the existing fruits the closest Fruit to the Packman
					existing_fruits.remove(fastest.getClosestFruit().getFruit());
					for (Packman p:this.packmen) {
						// Closest Fruit to the Packman
						closestFruit(p);
					}
					long timetosecond=fastest.getClosestFruit().getTime()+timetofirst;
					// If the ArrayList of the Packmen contains more than one 
					if (this.packmen.size()>1) {
						Packman nextfastest=secondfastest(fastest);
						// While the time of the next fastets is lower than the time of the fastest and existing fruits and the next fastest is not equal to fastest
						while (nextfastest.getClosestFruit().getTime()<timetosecond && !existing_fruits.isEmpty() && !nextfastest.equals(fastest) ) {
							updateFastest(nextfastest);
							// Remove from the existing fruits the closest Fruit to the Packman
							existing_fruits.remove(nextfastest.getClosestFruit().getFruit());
							for (Packman p:this.packmen) {
								// Closest Fruit to the Packman
								closestFruit(p);
							}
							nextfastest=fastest();	
						}
					}
					closestFruit(fastest);
				}
			}
			for (Packman p: this.packmen) {
				p.getPath().getPackman().setPackmanLocation(p.getPath().getStartPoint());
				solution.add(p.path);
			}
			return solution;
		}

		/**
		 * Auxiliary function that update the fastest Packman
		 * @param fastest contains all the data of the fastest Packman
		 
		public void updateFastest(Packman fastest) {
			fastest.setGarde(fastest.getGarde()+1);
			fastest.path.add(new Metadata(fastest.getClosestFruit()));
			fastest.getPath().setTime(fastest.getPath().getTime()+fastest.getClosestFruit().getTime());
			fastest.setPackmanLocation(fastest.getClosestFruit().getPoint());
		}

		/**
		 * Auxiliary function that finds the closest Fruit to the Packman
		 * @param packman the Packman data
		 
		public void closestFruit(Packman packman){
			long time, minTime;
			double distance;
			Fruit closest=null;
			Iterator<Fruit> iter=this.existing_fruits.iterator();
			if (iter.hasNext()){
				closest=iter.next();
				distance=mycoords.distance3d(packman.getPackmanLocation(), closest.getFruitLocation());
				minTime=timeCalculate(distance, packman.getVelocity());	
				packman.setClosestFruit(closest, minTime);
				while (iter.hasNext()) {
					Fruit current=iter.next();
					distance=mycoords.distance3d(packman.getPackmanLocation(), current.getFruitLocation());
					time=timeCalculate(distance, packman.getVelocity());	
					if (time<minTime ) {
						closest=current;
						minTime=time;
					}
				}
				packman.setClosestFruit(  closest, minTime*1000);
			}


		}
		/**
		 * Auxiliary function that calculates the time it took to Packman to get to Fruit
		 * @param distance the distance between two points
		 * @param velocity the Packman velocity
		 * @return time the time it took to Packman to get to Fruit
		 
		public long timeCalculate(double distance, long velocity) {
			return (long) (distance/velocity);
		}

		/**
		 * Auxiliary function that updates the closest Fruit of every Packman
		 
		public  void updateClosest() {
			for (Packman p: this.packmen)
				closestFruit(p);
		}
		/**
		 * Auxiliary function that finds the fastest Packman
		 * @return Packman the fastest Packman
		 
		public  Packman fastest() {
			Packman fastest= this.packmen.get(0);
			for (Packman p: this.packmen) {
				if (p.getClosestFruit().getTime()<fastest.getClosestFruit().getTime())
					if (p.getClosestFruit().getTime()!=-1)
						fastest=p;
			}
			return fastest;
		}
		/**
		 * Auxiliary function that finds the second fastest Packman
		 * @return Packman the second fastest Packman
		 
		public  Packman secondfastest(Packman fastest) {
			Packman secondfastest= null;
			boolean flag=true;
			Iterator <Packman> iter= this.packmen.iterator();
			while (flag && iter.hasNext()) {
				Packman current=iter.next();
				if (!current.equals(fastest)) {
					secondfastest= current;
					flag=false;
				}
			}
			for (Packman p: this.packmen) {
				if (!p.equals(fastest)) {
					if (p.getClosestFruit().getTime()<fastest.getClosestFruit().getTime())
						if (p.getClosestFruit().getTime()!=-1)
							secondfastest=p;
				}
			}
			return secondfastest;
		}
		*/
	}




