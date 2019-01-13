

package Map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import Geom.Point3D;


/**
 * This class contains all the data of the map.
 * Contain the map size and all the images of the map itself, the Packmen and Fruits.
 * Also, contains the conversion between coordinate to pixel and pixel to coordinate.
 * @author Ester Reznikov and Chen Ostrovski
 */
public class Map {

	// offsets
	private static final double mapLatitudeStart = 35.202574;
	private static final double mapLongitudeStart = 32.106046;
	private static final double mapLatitude = 35.212405;
	private static final double mapLongitude = 32.101858;
	private static final double WIDTH=mapLatitude-mapLatitudeStart;
	private static final double HEIGHT=mapLongitudeStart-mapLongitude;
	//static MyCoords mycoords=new MyCoords();

	// The images of the map. packmen and fruits
	public static BufferedImage img;

	public Map() {
		try {
			img = ImageIO.read(new File("Ariel1.png"));
		}
		catch (IOException e) {
		}

	}

	/**
	 * This function convert the data from coordinate to pixel
	 * @param gps the data of the point in coordinate
	 * @return point data in pixel
	 */
	public static Point3D getPositionOnScreen(Point3D gps){
		double x_pixel =( Map.img.getWidth())*((gps.y()-mapLatitudeStart)/WIDTH);
		double y_pixel = (Map.img.getHeight())*((mapLongitudeStart-gps.x())/HEIGHT);  
		return new Point3D((int)x_pixel, (int)y_pixel);
	}


	/**
	 * This function convert the data from pixel to coordinate
	 * @param gps the data of the point in pixel
	 * @return point data in coordinate
	 */
	public static Point3D PixeltoCoordanite(Point3D p_inPixel) {
		double y_coord=(p_inPixel.x()*WIDTH)/Map.img.getWidth()+mapLatitudeStart;
		double x_coord=Math.abs(((p_inPixel.y()*HEIGHT)/Map.img.getHeight()-mapLongitudeStart));
		return new Point3D(x_coord, y_coord);

	}

	/**
	 * This function takes two points in pixel and calculates their distance, elevation and azimuth in coordinate
	 * @param a_pixle the first point data in pixel
	 * @param b_pixle the second point data in pixel
	 * @return distance, elevation and azimuth in coordinate
	 */
	public static double[] azimuth_elevation_dist(Point3D a_pixle,Point3D b_pixle) {
		Point3D coord_a=new Point3D(PixeltoCoordanite(a_pixle));
		Point3D coord_b=new Point3D(PixeltoCoordanite(b_pixle));
		return  MyCoords.azimuth_elevation_dist(coord_a, coord_b);
	}
}
