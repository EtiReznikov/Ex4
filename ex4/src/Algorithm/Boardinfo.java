package Algorithm;

import java.util.ArrayList;

import Robot.Fruit;
import Robot.Packman;

public class Boardinfo {
	private int numofPackmen;
	private int numofFruits;
	private int numofghosts;
	ArrayList<Packman> packmen;
	ArrayList<Packman> ghosts;
	ArrayList<Fruit> fruits;
	

	public Boardinfo () {
		numofFruits=0;
		numofghosts=0;
		numofPackmen=0;
		packmen= new ArrayList<Packman>();
		ghosts= new ArrayList<Packman>();
		fruits= new ArrayList<Fruit>();
	}

	public int getNumofPackmen() {
		return numofPackmen;
	}

	public void setNumofPackmen(int numofPackmen) {
		this.numofPackmen = numofPackmen;
	}

	public int getNumofFruits() {
		return numofFruits;
	}

	public void setNumofFruits(int numofFruits) {
		this.numofFruits = numofFruits;
	}

	public int getNumofghosts() {
		return numofghosts;
	}

	public void setNumofghosts(int numofghosts) {
		this.numofghosts = numofghosts;
	}
}
