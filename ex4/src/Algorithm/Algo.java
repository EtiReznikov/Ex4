package Algorithm;

import java.util.Iterator;

import Geom.Point3D;
import Map.Map;
import Map.MyCoords;
import Robot.Fruit;
import Robot.Packman;
import Robot.Play;


public class Algo {
	private Play play;
	private Map map;
	Boardinfo [][]  subBoardInfo;
	public Algo(Play play) {
		this.play=play;
		map=new Map();
		subBoardInfo= new Boardinfo[10][10];
		for (int i=0; i<subBoardInfo.length; i++) {
			for (int  j=0; j<subBoardInfo[i].length; j++) {
				subBoardInfo[i][j]=new Boardinfo();
			}
		}
	}
	public Play getPlay() {
		return play;
	}
	public void game () {
		fillInfo();
		setstartpoint();
		String player="";
		boolean flag=true;
		while (flag) {
			for (String obj: play.getBoard()) {
				String[] data=obj.split(",");
				if (data[0]=="M") {
					player=obj;
					flag=false;
				}
			}
		}
		while (play.isRuning()) {
			player= closest(player);
		}
	}
	public void fillInfo() {
		for (int i=0,k=0; i<=Map.img.getWidth()-Map.img.getWidth()/10; i=i+Map.img.getWidth()/10, k++) {
			for (int j=0, l=0; j<=Map.img.getHeight()-Map.img.getHeight()/10; j=j+Map.img.getHeight()/10, l++) {
				for (String obj: play.getBoard()) {
					String [] data=obj.split(",");
					double x=Double.parseDouble(data[2]);
					double y=Double.parseDouble(data[3]);
					Point3D point=new Point3D(x,y);
					point=Map.getPositionOnScreen(point);
					if (i<=point.x() && point.x()<i+Map.img.getWidth()/10 && j<=point.y() && point.y()<j+Map.img.getHeight()/10) {
						if (data[0]=="F") {
							subBoardInfo[k][l].setNumofFruits(subBoardInfo[k][l].getNumofFruits()+1);
							Fruit fruit=new Fruit(obj);
							subBoardInfo[k][l].fruits.add(fruit);
						}
						else if (data[0]=="P") {
							subBoardInfo[k][l].setNumofPackmen(subBoardInfo[k][l].getNumofPackmen()+1);
							Packman packman=new Packman (obj);
							subBoardInfo[k][l].packmen.add(packman);

						}
						else if (data[0]=="G") {
							subBoardInfo[k][l].setNumofghosts(subBoardInfo[k][l].getNumofghosts()+1);	
							Packman ghost=new Packman (obj);
							subBoardInfo[k][l].ghosts.add(ghost);
						}
					}
				}
			}
		}
	}
	public void setstartpoint() {
		System.out.println("start");
		int k=0,l=0;
		int maxtoeat=subBoardInfo[k][l].getNumofFruits()+subBoardInfo[k][l].getNumofPackmen();

		int mintoavoid=subBoardInfo[k][l].getNumofghosts();
		for (int i=0; i<subBoardInfo.length; i++) {
			for (int  j=0; j<subBoardInfo[i].length; j++) {
				int toeat=subBoardInfo[i][j].getNumofFruits()+subBoardInfo[i][j].getNumofPackmen();
				int toavoid=subBoardInfo[i][j].getNumofghosts();
				if (toeat >= maxtoeat && toavoid <= mintoavoid) {
					maxtoeat=toeat;
					mintoavoid=toavoid;
					k=i; l=j;
				}
			}
		}
		if (subBoardInfo[k][l].packmen.isEmpty()) {
			if (!subBoardInfo[k][l].fruits.isEmpty()) {
				String[] fruit=subBoardInfo[k][l].fruits.get(0).toString().split(",");
				play.setInitLocation(Double.parseDouble(fruit[2]), Double.parseDouble(fruit[3]));
			}
		}
		else {
			String [] fastest= fastestPackman(k, l).split(",");
			play.setInitLocation(Double.parseDouble(fastest[2]), Double.parseDouble(fastest[3]));
		}
	}

	public String fastestPackman(int i, int j) {
		Packman fastest=null;
		double maxSpeed=0;
		for (Packman pac: subBoardInfo[i][j].packmen ) {
			String[] packman=pac.toString().split(",");
			double speed= Double.parseDouble(packman[5]);
			if (speed>maxSpeed)
				fastest=pac;		
		}
		return fastest.toString();
	}
	public String closest(String player) {
		System.out.println(true);
		Iterator<String> iter=play.getBoard().iterator();
		String[] playerInfo=player.split(",");
		String[] closest=player.split(",");
		Point3D point=new Point3D (Double.parseDouble(playerInfo[2]), Double.parseDouble(playerInfo[3]));
		boolean flag=false;
		Point3D aim;
		double mindistance=0;
		while (iter.hasNext()) {

			String[] current=iter.next().toString().split(",");
			if (!flag) {
				if (current[0]=="F" || current[0]=="P") {
					aim=new Point3D (Double.parseDouble(current[2]),Double.parseDouble(current[3]));
					mindistance=MyCoords.distance3d(point, aim);
					closest=current;
					flag=true;
				}	
			}
			else {
				if (current[0]=="F" || current[0]=="P") {
					aim=new Point3D (Double.parseDouble(current[2]),Double.parseDouble(current[3]));
					double distance = MyCoords.distance3d(point, aim);
					if (distance<mindistance) {
						mindistance=distance;
						closest=current;
					}
				}
			}
		}
		play.setInitLocation(Double.parseDouble(closest[2]), Double.parseDouble(closest[3]));	
		playerInfo[2]=closest[2];
		playerInfo[3]=closest[3];
		return playerInfo.toString();
	}
}
