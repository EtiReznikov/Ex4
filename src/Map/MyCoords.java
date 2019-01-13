package Map;

import Geom.Point3D;

public class MyCoords {
	static final int RADIOS_EARTH=6371000;


	/**
	 * computes a new point which is the gps point transformed by a 3D vector (in meters)
	 * @param gps first point3D coordinate
	 * @param local_vector_in_meter the distance in meter from the first point to the second point
	 * @return newPoint3D second point3D coordinate
	 */

	
	public static Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		double LonNorm=Math.cos(gps.x()*Math.PI/180);
		// Radiant distance between the x of the first coordinate and the x of the second coordinate
		double diff_radianX = Math.asin(local_vector_in_meter.x()/RADIOS_EARTH);
		// Radiant distance between the y of the first coordinate and the y of the second coordinate
		double diff_radianY = Math.asin((local_vector_in_meter.y()/(RADIOS_EARTH)*LonNorm));
		double x= find_point(diff_radianX, gps.x());
		double y= find_point(diff_radianY, gps.y());
		double z=local_vector_in_meter.z()+gps.z();
		// Create a new Point3D 
		Point3D newPoint3D = new Point3D(x,y,z);
		return newPoint3D;
	}

	/**
	 * Auxiliary function that help the add function to find the point coordinate
	 * @param diff_radian the radiant distance between the first point and the second point
	 * @param p the type of the wanted parameter(x or y)
	 * @return the point coordinate
	 */

	public static double find_point(double diff_radian,double p) {
		return diff_radian*180/Math.PI+p;
	}

	/** computes the 3D distance (in meters) between the two gps like points
	 * @param gps0 first point3D coordinate
	 * @param gps1 second point3D coordinate
	 * @return distance in meters between gps0 and gps1
	 */

	
	public  static double distance3d(Point3D gps0, Point3D gps1) {
		double LonNorm=Math.cos(gps0.x()*Math.PI/180);
		// Radiant distance between the x of the first coordinate and the x of the second coordinate
		double diffx_radian=diff_radian(gps0.x(),gps1.x());	
		// Radiant distance between the y of the first coordinate and the y of the second coordinate
		double diffy_radian=diff_radian(gps0.y(),gps1.y());
		// Convert from radiant to meters
		double toMeterX=Math.sin(diffx_radian)*RADIOS_EARTH;
		double toMeterY=Math.sin(diffy_radian)*RADIOS_EARTH*LonNorm;
		// Return the distance in meters between gps0 and gps1
		return Math.sqrt(toMeterX*toMeterX+toMeterY*toMeterY);
	}

	/**
	 * Auxiliary function, given two points calculate the radiant distance.
	 * Calculates it between the x or y of the first coordinate and the x or y of the second coordinate(suitability)
	 * @param p0 x or y of the first coordinate(suitability)
	 * @param p1 x or y of the second coordinate(suitability)
	 * @return radiant distance
	 */

	private static double diff_radian(double p0, double p1) {
		double diff=p1-p0;
		return diff*Math.PI/180;
	}

	/**
	 * computes the 3D vector (in meters) between two gps like points 
	 * @param gps0 first point3D coordinate
	 * @param gps1 second point3D coordinate
	 * @return Vector the 3D vector in meters between the points
	 */

	public static Point3D vector3D(Point3D gps0, Point3D gps1) {
		double LonNorm=Math.cos(gps0.x()*Math.PI/180);
		// Radiant distance between the x of the first coordinate and the x of the second coordinate
		double diffx_radian=diff_radian(gps0.x(),gps1.x());
		// Radiant distance between the y of the first coordinate and the y of the second coordinate
		double diffy_radian=diff_radian(gps0.y(),gps1.y());
		// Convert from radiant to meters
		double toMeterX=Math.sin(diffx_radian)*RADIOS_EARTH;
		double toMeterY=Math.sin(diffy_radian)*RADIOS_EARTH*LonNorm;
		double toMeterZ=gps1.z()-gps0.z();
		// Create a new Point3D 
		Point3D Vector = new Point3D(toMeterX,toMeterY,toMeterZ);
		return Vector;
	}

	/**
	 * computes the polar representation of the 3D vector be gps0-->gps1 
	 * @param gps0 first point3D coordinate
	 * @param gps1 second point3D coordinate
	 * @return []azimuth_elevation_dist array that contain azimuth, elevation and distance
	 */


	public static double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		// Build a new array to contain azimuth, elevation and distance
		double [] azimuth_elevation_dist= new double[3];
		// Calculates the azimuth
		double deltaY=Math.toRadians(gps1.y()-gps0.y());
		double x1=Math.toRadians(gps1.x());
		double x0=Math.toRadians(gps0.x());
		double Y= Math.sin(deltaY) * Math.cos(x1);
		double X = Math.cos(x0)*Math.sin(x1) -Math.sin(x0)*Math.cos(x1)*Math.cos(deltaY);
		double teta = Math.atan2(Y, X);
		double azimuth=(Math.toDegrees(teta)+360)%360;
	
		// Put the Azimuth value at the first place in the array
		azimuth_elevation_dist[0]=azimuth;
	
	
		double distance=distance3d(gps0,gps1);
		double deltaz=Math.toDegrees(gps1.z()-gps0.z());
		double elevation=deltaz/distance;
		azimuth_elevation_dist[1]=elevation;
	
		// Put the Distance value at the third place in the array
		azimuth_elevation_dist[2]=distance;
		return azimuth_elevation_dist;


	}

	/**
	 * return true if this point is a valid lat, lon , lat coordinate: [-180,+180],[-90,+90],[-450, +inf]
	 * @param p Point3D x, y, z coordinates
	 * @return true if this point is a valid, false if this point is a invalid
	 */


	public static boolean isValid_GPS_Point(Point3D p) {
		if((p.x()<180 && p.x()>-180) && (p.y()<90 && p.y()>-90) && (p.z()>-450)) {
			return true;
		}
		return false;
	}

}
