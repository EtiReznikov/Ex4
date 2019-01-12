

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
	public static BufferedImage Fruit1;
	public static BufferedImage Fruit2;
	public static BufferedImage Fruit3;
	public static BufferedImage Fruit4;
	public static BufferedImage Packman;
	public static BufferedImage ghost;
	public static BufferedImage player;

	public Map() {
		try {
			img = ImageIO.read(new File("Ariel1.png"));
		} catch (IOException e) {
		}
		try {
			Fruit1= ImageIO.read(new File("strawberry.png"));
		} catch (IOException e) {
		}
		try {
			Fruit2= ImageIO.read(new File("banana.png"));
		} catch (IOException e) {
		}
		try {
			Fruit3= ImageIO.read(new File("GreenApple.png"));
		} catch (IOException e) {
		}
		try {
			Fruit4 = ImageIO.read(new File("Grapes.png"));
		} catch (IOException e) {
		}
		try {
			Packman = ImageIO.read(new File("packman.png"));
		} catch (IOException e) {
		}
		try {
			ghost = ImageIO.read(new File("ghost.png"));
		} catch (IOException e) {
		}
		try {
			player = ImageIO.read(new File("player.png"));
		} catch (IOException e) {
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

	public static  Point3D PixeltoCoordanite(Point3D p_inPixel) {
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
  



/*

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
/*
public class Map {

	// offsets
	private static final double mapLatitudeStart = 35.202574;
	private static final double mapLongitudeStart = 32.106046;
	private static final double mapLatitude = 35.212405;
	private static final double mapLongitude = 32.101858;
	/*
	private static final double mapLongitudeStart = 35.20237;
	private static final double mapLatitudeStart = 32.10571;
	private static final double mapLongitude = 35.21239;
	private static final double mapLatitude = 32.10189;
	*/
	//	static final int mapWidth =1498 , mapHeight =600;
//	private static final double WIDTH=mapLatitudeStart-mapLatitude;
//	private static final double HEIGHT=mapLongitude-mapLongitudeStart;
	/*
	private static final double WIDTH=mapLatitudeStart-mapLatitude;
	private static final double HEIGHT=mapLongitudeStart-mapLongitude;
	*/
	//	private static final double WIDTH=mapLongitude-mapLongitudeStart;
	//	private static final double HEIGHT=mapLatitudeStart-mapLatitude;
	/*static MyCoords mycoords=new MyCoords();

	// The images of the map. packmen and fruits
	public static BufferedImage img;
	public static BufferedImage Fruit1;
	public static BufferedImage Fruit2;
	public static BufferedImage Fruit3;
	public static BufferedImage Fruit4;
	public static BufferedImage Packman;
	public static BufferedImage ghost;
	public static BufferedImage player;

	public Map() {
		try {
			img = ImageIO.read(new File("Ariel1.png"));
		} catch (IOException e) {
		}
		try {
			Fruit1= ImageIO.read(new File("strawberry.png"));
		} catch (IOException e) {
		}
		try {
			Fruit2= ImageIO.read(new File("banana.png"));
		} catch (IOException e) {
		}
		try {
			Fruit3= ImageIO.read(new File("GreenApple.png"));
		} catch (IOException e) {
		}
		try {
			Fruit4 = ImageIO.read(new File("Grapes.png"));
		} catch (IOException e) {
		}
		try {
			Packman = ImageIO.read(new File("packman.png"));
		} catch (IOException e) {
		}
		try {
			ghost = ImageIO.read(new File("ghost.png"));
		} catch (IOException e) {
		}
		try {
			player = ImageIO.read(new File("player.png"));
		} catch (IOException e) {
		}
	}


	/**
	 * This function convert the data from coordinate to pixel
	 * @param gps the data of the point in coordinate
	 * @return point data in pixel
	 */
/*
	public static Point3D getPositionOnScreen(Point3D gps){

		
		double  Widht=mapLatitudeStart-mapLatitude;
		double hight= mapLongitude-mapLongitudeStart;
		
		double dx= (mapLatitudeStart-gps.x())/Widht;
		double dy= (gps.y()-mapLongitudeStart)/hight;
		
		double Y=(img.getHeight()*dx);
		double X=(img.getWidth()*dy);
		/*
		double x_pixel =(img.getWidth())*((Math.abs(mapLongitudeStart-gps.y()))/WIDTH);
		double y_pixel = (img.getHeight())*((Math.abs(gps.x()-mapLatitudeStart))/HEIGHT);  
		double x_pixel =( Map.img.getWidth())*((gps.x()-mapLatitudeStart)/WIDTH);
		double y_pixel = (Map.img.getHeight())*((gps.y()-mapLongitudeStart)/HEIGHT); 

		double x_pixel=(mapLatitudeStart-gps.x())/WIDTH*img.getWidth();
		double y_pixel=(mapLongitudeStart-gps.y())/HEIGHT*img.getHeight();

		double dx=(mapLatitudeStart-gps.x())/WIDTH;
		double dy=(gps.y()-mapLongitudeStart)/HEIGHT;
		double x_pixel=img.getWidth()*dy;
		double y_pixel=img.getHeight()*dx;
		return new Point3D((int)x_pixel, (int)y_pixel);
		return new Point3D((int)y_pixel, (int)x_pixel);
		*/
		
//		return new Point3D((int)X,(int)Y);
//	}



	/**
	 * This function convert the data from pixel to coordinate
	 * @param gps the data of the point in pixel
	 * @return point data in coordinate
	 */
/*
	public static  Point3D PixeltoCoordanite(Point3D p_inPixel) {
		double dx=p_inPixel.x()/Map.img.getWidth();
		double dy=p_inPixel.y()/Map.img.getHeight();
		
		double totallon=mapLongitude-mapLongitudeStart;
		double totallat= mapLatitude-mapLatitudeStart;
		
		double goLon=totallon*dx;
		double golat= totallat*dy;
		
		return new Point3D(mapLatitudeStart+golat, mapLongitudeStart+goLon);
		/*
		double y_coord=(p_inPixel.x()*WIDTH)/Map.img.getWidth()+mapLatitudeStart;
		double x_coord=Math.abs(((p_inPixel.y()*HEIGHT)/Map.img.getHeight()-mapLongitudeStart));
		double y_coord=mapLongitudeStart-(p_inPixel.x()*WIDTH)/Map.img.getWidth();
		double x_coord=Math.abs(((p_inPixel.y()*HEIGHT)/Map.img.getHeight()+mapLatitudeStart));
		double y_coord=mapLongitudeStart-(p_inPixel.x()*WIDTH)/Map.img.getWidth();
		double x_coord=(p_inPixel.x()/img.getWidth())*WIDTH+mapLatitudeStart;

		double dx=p_inPixel.x()/img.getWidth()*HEIGHT;
		double dy=p_inPixel.y()/img.getHeight()*(-1*WIDTH);
		double x_pixel=dy+mapLatitudeStart;
		double y_pixel=dx+mapLongitudeStart;
		return new Point3D((int)x_pixel, (int)y_pixel);
		*/
//	}

	/**
	 * This function takes two points in pixel and calculates their distance, elevation and azimuth in coordinate
	 * @param a_pixle the first point data in pixel
	 * @param b_pixle the second point data in pixel
	 * @return distance, elevation and azimuth in coordinate
	 */
	/*
	public static double[] azimuth_elevation_dist(Point3D a_pixle,Point3D b_pixle) {
		Point3D coord_a=new Point3D(PixeltoCoordanite(a_pixle));
		Point3D coord_b=new Point3D(PixeltoCoordanite(b_pixle));
		//return MyCoords.azimuth_elevation_dist(coord_a, coord_b);
		return MyCoords.azimuth_elevation_dist(coord_a, coord_b);
	}

}


*/