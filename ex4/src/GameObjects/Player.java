package GameObjects;

import Geom.Point3D;

public class Player {
	private double veloctiy;
	private double radius;
	private Point3D location;

	public Player( Point3D location, double velocity, double radius ) {
		this.veloctiy=velocity;
		this.radius=radius;
		this.location=location;
	}

	public double getVeloctiy() {
		return veloctiy;
	}

	public double getRadius() {
		return radius;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}
}
