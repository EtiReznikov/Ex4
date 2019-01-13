package GameObjects;

import Geom.Point3D;

/**
 * This class contains all the info of the fruits in the game 
 * @author Chen Ostrovski
 * @author Ester Reznikov
 */
public class Fruit {
	private Point3D location;
	

	/**
	 * This constructor creates a new fruit 
	 * @param location the fruit's location
	 * @param id the fruit's id
	 */
	public Fruit(Point3D location){
		this.location=location;
	}



	/**
	 * This method check if two fruits are equal 
	 * which means that both contains same location and id
	 * @param other the other fruit info
	 * @return true: both contains same location and id false : have different location and id
	 */
	public boolean equals (Fruit other) {
		if ( this.location.equals(other.location))
			return true;
		return false;
	}



	///////GET///////

	public Point3D getLocation() {
		return location;
	}

}
