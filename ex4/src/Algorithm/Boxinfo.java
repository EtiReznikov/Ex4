package Algorithm;

import java.util.ArrayList;
import Geom.Point3D;
import Robot.Play;

public class Boxinfo {
	ArrayList<Point3D []> boxes;
	ArrayList <Point3D> points;
	ArrayList<ArrayList<Point3D>> neighbors;

	public Boxinfo(Play play) {
		boxes= allpoints(play);
		points=points(boxes);
		neighbors=pointCanSee(points,boxes);
	
	}
	public Boxinfo(ArrayList<Point3D[]> boxes) {
		this.boxes= boxes;
		points=points(boxes);
		neighbors=pointCanSee(points,boxes);
	}

	public static void main(String[] args) {
		Play play=new Play("C:\\Users\\etire\\OneDrive\\מסמכים\\data\\Ex4_OOP_example8.csv");
		Boxinfo test= new Boxinfo(play);
		for (ArrayList<Point3D> current: test.neighbors) {
			for (Point3D point: current)
				System.out.println(point.toString());
			System.out.println();
		}

	}


	public static ArrayList<ArrayList<Point3D>> pointCanSee(ArrayList <Point3D> points,  ArrayList<Point3D []> boxes){
		ArrayList<ArrayList<Point3D>> neighbors= new ArrayList<ArrayList<Point3D>>();
		int j=0;
		int notvaild=-1;
		for (Point3D point:points) {
			ArrayList<Point3D> pointNeighbors=	new ArrayList<Point3D>();
			pointNeighbors.add(point);
			j++;
			for (Point3D [] corners: boxes) {
				if (point.equals(corners[0]))
						  notvaild=2;
				else if (point.equals(corners[2]))
						 notvaild=0;
				else if (point.equals(corners[3]))
					 notvaild=1;
				else if (point.equals(corners[1]))
					 notvaild=3;
				for (int i=0; i<corners.length; i++) {
					if (!point.equals(corners[i]) && points.contains(corners[i]) && (i!=notvaild)) {
						if (Auto.blockedbybox(point, corners[i], boxes))
							pointNeighbors.add(corners[i]);
					}
				}
			}
			notvaild=-1;
			neighbors.add(pointNeighbors);
		}
		return neighbors;
	}

	public  void addPointstoNeighbors(Point3D source, Point3D target ){
	//	this.neighbors.add(new ArrayList<Point3D>());
	//	this.neighbors.get(this.neighbors.size()-1).add(source);
		ArrayList<Point3D> pointNeighbors=	new ArrayList<Point3D>();
		pointNeighbors.add(source);
		for (Point3D point: points)
			if (Auto.blockedbybox(source, point, boxes))
				pointNeighbors.add(point);
		neighbors.add(pointNeighbors);
			/*
		//for (Point3D [] corners: boxes) {
		//	for (int i=0; i<corners.length; i++) {
				if (Auto.blockedbybox(source, corners[i], boxes))
					neighbors.get(this.neighbors.size()-1).add(corners[i]); 
		//	}
		//}	
		*/
		this.neighbors.add(new ArrayList<Point3D>());
		this.neighbors.get(this.neighbors.size()-1).add(target);
		for (Point3D [] corners: boxes) {
			for (int i=0; i<corners.length; i++) {
				if (Auto.blockedbybox(target, corners[i], boxes))
					neighbors.get(this.neighbors.size()-1).add(corners[i]); 
			}
		}
	}
	public static ArrayList <Point3D []> allpoints(Play play) {
		ArrayList <Point3D [] > boxes=new ArrayList<Point3D[]>();
		int i=0;
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			if (data[0].equals("B")) {
				boxes.add(new Point3D[4]);
				boxes.get(i)[0]=new Point3D(Double.parseDouble(data[2]), Double.parseDouble(data[6]));
				boxes.get(i)[1]=new Point3D(Double.parseDouble(data[5]), Double.parseDouble(data[6]));
				boxes.get(i)[2]=new Point3D(Double.parseDouble(data[5]), Double.parseDouble(data[3]));
				boxes.get(i)[3]=new Point3D(Double.parseDouble(data[2]), Double.parseDouble(data[3]));
				i++;
				//להוסיף מטר
			}
		}
		return boxes;
	}

	public static ArrayList <Point3D> points( ArrayList <Point3D []> boxes){
		ArrayList <Point3D>corners =new ArrayList<Point3D>();
		for (int i=0; i<boxes.size(); i++){
			for (int j=0; j<boxes.get(i).length; j++){
				if (vaildpoint(boxes, boxes.get(i)[j], i))
					corners.add(boxes.get(i)[j]);
			}
		}
		return corners;
	}

	public static boolean vaildpoint(ArrayList <Point3D []> boxes,Point3D point, int k) {
		for (int i=0; i<boxes.size(); i++){
			if (k!=i) {
				if ((check(boxes.get(i)[0], boxes.get(i)[1],boxes.get(i)[2], boxes.get(i)[3], point)))
					return false;
			}
		}
		return true;
	}

	static float area_triangle(Point3D p1, Point3D p2, Point3D p3)
	{ 
		return (float)Math.abs((p1.x() * (p2.y() - p3.y()) +  
				p2.x() * (p3.y() - p1.y()) + p3.x() * (p1.y() - p2.y())) / 2.0); 
	} 

	/* A function to check whether point P(x, y)  
lies inside the rectangle formed by A(x1, y1),  
B(x2, y2), C(x3, y3) and D(x4, y4) */
	static boolean check(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Point3D P) 
	{ 

		/* Calculate area of rectangle ABCD */
		float A = area_triangle(p1,p2,p3)+  
				area_triangle(p1,p4,p3); 

		/* Calculate area of triangle PAB */
		float A1 = area_triangle(P,p1,p2); 

		/* Calculate area of triangle PBC */
		float A2 = area_triangle(P,p2,p3); ; 

		/* Calculate area of triangle PCD */
		float A3 = area_triangle(P,p3,p4); 

		/* Calculate area of triangle PAD */
		float A4 = area_triangle(P,p1,p4); 

		/* Check if sum of A1, A2, A3 and A4  is same as A */
		return (A == A1 + A2 + A3 + A4); 
	} 
}
