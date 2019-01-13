package Algorithm;

import java.util.ArrayList;
import Geom.Point3D;
import Robot.Play;

/**
 * This Class contains all the info of the boxes in the game 
 * and checks for every point the other points it can see
 * @author Chen Ostrovski
 * @author Ester Reznikov
 */
public class Boxinfo {
	ArrayList<Point3D []> boxes;
	ArrayList <Point3D> points;
	ArrayList<ArrayList<Point3D>> neighbors;

	/**
	 * This constructor creates a new Boxinfo
	 * contains all the info of the play
	 * @param play
	 */
	public Boxinfo(Play play) {
		boxes= allpoints(play);
		points=points(boxes);
		neighbors=pointCanSee(points,boxes);
	}

	 
	/**
	 * This function checks which points every point can see ( not blocked by box)
	 * @param points the arraylist the contains all the points locations
	 * @param boxes the arraylist the contains all the points locations of the boxes
	 * @return arraylist that contains the point and the points it can see
	 */
	public static ArrayList<ArrayList<Point3D>> pointCanSee(ArrayList<Point3D> points, ArrayList<Point3D[]> boxes){

		// Create a new arraylist that contains the point and the points it can see
		ArrayList<ArrayList<Point3D>> neighbors= new ArrayList<ArrayList<Point3D>>();
		int j=0;
		int notvaild=-1;
		// Goes over all the points
		for (Point3D point:points) {
			ArrayList<Point3D> pointNeighbors=	new ArrayList<Point3D>();
			pointNeighbors.add(point);
			j++;
			// Checks if the point belong to a box
			for (Point3D [] corners: boxes) {
				if (point.equals(corners[0]))
					notvaild=2;
				else if (point.equals(corners[2]))
					notvaild=0;
				else if (point.equals(corners[3]))
					notvaild=1;
				else if (point.equals(corners[1]))
					notvaild=3;
				// Goes over all the corners of a box
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

	/**
	 * This function gets two new points and checks which other points they can see
	 * @param source the first point location
	 * @param target the second point location 
	 */
	public void addPointstoNeighbors(Point3D source, Point3D target ){

		ArrayList<Point3D> pointNeighbors=	new ArrayList<Point3D>();
		pointNeighbors.add(source);
		// Goes over all the points
		for (Point3D point: points)
			// check if the first point can see the other points
			if (Auto.blockedbybox(source, point, boxes))
				pointNeighbors.add(point);
		neighbors.add(pointNeighbors);
		this.neighbors.add(new ArrayList<Point3D>());
		this.neighbors.get(this.neighbors.size()-1).add(target);
		// Goes over all the points
		for (Point3D [] corners: boxes) {
			for (int i=0; i<corners.length; i++) {
				// check if the second point can see the other points
				if (Auto.blockedbybox(target, corners[i], boxes))
					neighbors.get(this.neighbors.size()-1).add(corners[i]); 
			}
		}
	}
	
	/**
	 * This function get the board corners info
	 * @param play
	 * @return boxes arraylist that contains all the boxes corners
	 */
	public static ArrayList <Point3D[]> allpoints(Play play) {
		ArrayList <Point3D [] > boxes=new ArrayList<Point3D[]>();
		int i=0;
		// Goes on the objects in the board
		for (String obj: play.getBoard()) {
			String[] data=obj.split(",");
			// If the object is board get the corners info
			if (data[0].equals("B")) {
				boxes.add(new Point3D[4]);
				boxes.get(i)[0]=new Point3D(Double.parseDouble(data[2]), Double.parseDouble(data[6]));
				boxes.get(i)[1]=new Point3D(Double.parseDouble(data[5]), Double.parseDouble(data[6]));
				boxes.get(i)[2]=new Point3D(Double.parseDouble(data[5]), Double.parseDouble(data[3]));
				boxes.get(i)[3]=new Point3D(Double.parseDouble(data[2]), Double.parseDouble(data[3]));
				i++;
			}
		}
		return boxes;
	}

	/**
	 * This method goes over the boxes corners and checks if they are valid
	 * @param boxes the info of all the boxes in the game 
	 * @return arraylist of all the valid corners 
	 */
	public static ArrayList<Point3D> points(ArrayList<Point3D[]> boxes){
		ArrayList <Point3D>corners =new ArrayList<Point3D>();
		for (int i=0; i<boxes.size(); i++){
			for (int j=0; j<boxes.get(i).length; j++){
				if (vaildpoint(boxes, boxes.get(i)[j], i))
					corners.add(boxes.get(i)[j]);
			}
		}
		return corners;
	}

	/**
	 * Auxiliary function that help the function above to find if the current point( the box corner) 
	 * is locaited inside of other box 
	 * @param boxes the info of all the boxes in the game 
	 * @param point the corner location
	 * @param index the index of the current box
	 * @return
	 */
	public static boolean vaildpoint(ArrayList <Point3D[]> boxes, Point3D point, int index) {
		for (int i=0; i<boxes.size(); i++){
			if (index!=i) {
				if ((check(boxes.get(i)[0], boxes.get(i)[1],boxes.get(i)[2], boxes.get(i)[3], point)))
					return false;
			}
		}
		return true;
	}

	/**
	 * This function calculates triangle area
	 * @param p1 the first corner location
	 * @param p2 the second corner location
	 * @param p3 the third corner location
	 * @return triangle area
	 */
	static float area_triangle(Point3D p1, Point3D p2, Point3D p3){ 
		return (float)Math.abs((p1.x() * (p2.y() - p3.y()) +  
				p2.x() * (p3.y() - p1.y()) + p3.x() * (p1.y() - p2.y())) / 2.0); 
	} 

	/**
	 * This function to checks whether point P(x, y) lies inside the rectangle formed by p1=A(x1, y1),  
	 * p2=B(x2, y2), p3=C(x3, y3) and p4=D(x4, y4)
	 * @param p1 the first corner location
	 * @param p2 the second corner location
	 * @param p3 the third corner location
	 * @param p4 the fourth corner location
	 * @param P the point location
	 * @return true : if the point lies inside false : if the point lies outside
	 */
	static boolean check(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Point3D P) { 

		// Calculate area of rectangle ABCD 
		float A = area_triangle(p1,p2,p3)+  
				area_triangle(p1,p4,p3); 
		
		// Calculate area of triangle PAB
		float A1 = area_triangle(P,p1,p2); 

		// Calculate area of triangle PAB 
		float A2 = area_triangle(P,p2,p3);

		// Calculate area of triangle PCD
		float A3 = area_triangle(P,p3,p4); 

		// Calculate area of triangle PAD 
		float A4 = area_triangle(P,p1,p4); 

		// Check if sum of A1, A2, A3 and A4  is same as A 
		return (A == A1 + A2 + A3 + A4); 
	} 
}
