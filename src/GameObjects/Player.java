package GameObjects;

import Geom.Point3D;

/**
 * This class contains all the info of the player in the game
 * @author Chen Ostrovski
 * @author Ester Reznikov
 */
public class Player {
	private double veloctiy;
	private double radius;
	private Point3D location;

	/**
	 * This constructor creates a new player
	 * @param location the player's location
	 * @param velocity the player's velocity
	 * @param radius the player's eating radius
	 */
	public Player( Point3D location, double velocity, double radius ) {
		this.veloctiy=velocity;
		this.radius=radius;
		this.location=location;
	}

	///////GET///////

	public double getVeloctiy() {
		return veloctiy;
	}
	public double getRadius() {
		return radius;
	}
	public Point3D getLocation() {
		return location;
	}

	///////SET///////

	public void setLocation(Point3D location) {
		this.location = location;
	}
}
