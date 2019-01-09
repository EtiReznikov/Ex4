package GameObjects;

import com.sun.org.apache.xpath.internal.operations.Equals;

import Geom.Point3D;

public class Fruit {
private Point3D location;
private int id;

public Fruit(Point3D location, int id) {
	this.location=location;
	this.id=id;
}


public boolean equals (Fruit other) {
	if (this.id== other.id && this.location.equals(other.location))
		return true;
	return false;
}
public int getId() {
	return id;
}

public Point3D getLocation() {
	return location;
}
public String toString() {
	return this.location.toString()+","+this.id;
}
}
