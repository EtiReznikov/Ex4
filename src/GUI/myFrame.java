package GUI;

import static org.junit.Assume.assumeTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import Algorithm.Auto;
import GIS.Path;
import GameObjects.Fruit;
import Geom.Point3D;
import Robot.Play;
import Map.Map;
import Map.MyCoords;

/**
 * This class contains all the info of the Jframe and creates it
 * @author Chen Ostrovski
 * @author Ester Reznikov
 */
public class myFrame  extends JFrame implements MouseListener{
	private static final long serialVersionUID = 1L;

	private Container window;
	private JPanel _panel;
	private Graphics _paper;
	private int GameStatus;
	private int width;
	private int height;
	private Play play;
	static Map map;
	private int X, Y;
	static double angle;
	private MyCoords mycoords;
	private Object Point3D;

	public myFrame(){
		super("Packman invasion"); //setTitle("Map Counter");  // "super" Frame sets its title
		//	Moves and resizes this component. 
		//	The new location of the top-left corner is  specified by x and y, 
		//	and the new size is specified by width and height
		//	setBounds(x,y,width,height)
		this.setBounds(0,0,1433,700); //setSize(1433,700);        // "super" Frame sets its initial window size 
		//      Exit the program when the close-window button clicked
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//LatLonAlt LLA=new LatLonAlt(0,0,0);
		map=new Map();
		mycoords=new MyCoords();
		height= Map.img.getHeight();
		GameStatus=0;
	}		

	/**
	 * This function creates the GUI it contains all the info of the frame and panel
	 */
	public void createGui(){              				
		//	A Container is a component  that can contain other GUI components
		window = this.getContentPane(); 
		window.setLayout(new FlowLayout());

		//	Add "panel" to be used for drawing            
		_panel = new JPanel();
		Dimension d= new Dimension(1433,642);
		//	_panel.setPreferredSize(new Dimension(1433,642));
		//	_panel.setSize(d);
		_panel.setPreferredSize(d);		               
		window.add(_panel);
		pack();

		// A menu-bar contains menus. A menu contains menu-items (or sub-Menu)
		MenuBar menuBar;   // the menu-bar
		Menu File, Game;
		MenuItem OpenFile;
		MenuItem setPlayer, start, restart, stop, AutomaticPlayer;
		menuBar = new MenuBar();
		File = new Menu("Add");
		menuBar.add(File);
		Game = new Menu("Game");
		menuBar.add(Game);

		// The Open file button - clicking on this opens a CSV file 
		OpenFile = new MenuItem("Open file");
		File.add(OpenFile);
		OpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("csv","CSV");
				fileChooser.addChoosableFileFilter(filter);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					play=new Play(selectedFile.getAbsolutePath());//+csv

					String ID = JOptionPane.showInputDialog(null,
							"Enter ID:", JOptionPane.QUESTION_MESSAGE);
					if ( ID==null || ID.length()==0)
						play.setIDs(0);
					else {
						ID= ID.replace(" ","" );
						String[] id=ID.split(",") ;
						if (id.length==1)
							play.setIDs((Long.parseLong(id[0])));
						if (id.length==2)
							play.setIDs(Long.parseLong(id[0]),Long.parseLong(id[1]));
						if (id.length==3)
							play.setIDs(Long.parseLong(id[0]),Long.parseLong(id[1]),Long.parseLong(id[2]));
					}
					GameStatus=1;
				}
				repaint();
			}
		});

		// The Set player button - clicking on this locats the player on the board
		setPlayer = new MenuItem("Set Player");
		Game.add(setPlayer);
		setPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				angle=0;
				GameStatus=2;
				repaint();
			}
		});

		// The Start button - clicking on this starts the game after seting the player on the board
		start = new MenuItem("Start");
		Game.add(start);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				angle=0;
				play.start();
				GameStatus=3;
				threadGame();
			}
		});

		// The Stop button - clicking on this stops the game after its starts
		stop = new MenuItem("stop");
		Game.add(stop);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play.stop();
				angle=0;
			}
		});

		// The restart button - clicking on this restarts the game after its starts
		restart = new MenuItem("restart");
		Game.add(restart);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (play.isRuning())
					play.stop();
				play.getBoard().removeAll(play.getBoard());
				angle=0;
				GameStatus=-1;
				repaint();
			}
		});

		//The Automatic play button - clicking on this starts the automatic game
		AutomaticPlayer = new MenuItem("Automatic play");
		Game.add(AutomaticPlayer);
		AutomaticPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStatus=4;
				Auto automatic = new Auto (play);
				automatic.setstart();
				play.start();
				AutothreadGame(automatic);
			}
		});

		setMenuBar(menuBar);  // "this" JFrame sets its menu-bar
		// Prepare an ImageIcon
		String imgMapFilename = "Ariel1.png";    
		ImageIcon imgBck = new ImageIcon(getClass().getResource(imgMapFilename));
		JLabel labelMap = new JLabel();
		labelMap.setIcon(imgBck);
		_panel.add(labelMap);
		// panel (source) fires the MouseEvent.
		//panel adds "this" object as a MouseEvent listener.
		_panel.addMouseListener(this);

	}	

	class ObjectCoponent extends Component{
		private static final long serialVersionUID = 1L;
		ArrayList<String> Objectsdata;
		ObjectCoponent(){
			super();	
		}

		/**
		 * This function paint all the elements on the board
		 * @param g Graphics
		 */
		public void paintComponent(Graphics g) {
			if (GameStatus!=0)
				Objectsdata = new ArrayList<String>(play.getBoard());
			else
				Objectsdata = new ArrayList<String>();
			Graphics2D g2 = (Graphics2D)_paper;
			for (String obj: Objectsdata) {
				String[] data=obj.split(",");
				int id=Integer.parseInt(data[1]);
				double x=Double.parseDouble(data[2]);
				double y=Double.parseDouble(data[3]);
				Point3D gps=new Point3D(x,y);
				Point3D pointOnScreen=Map.getPositionOnScreen(gps);

				// If the board contains blocks draw a black triangle at the fruit's location
				if (data[0].equals("B")) {
					double X_min=Double.parseDouble(data[2]);
					double Y_min=Double.parseDouble(data[3]);
					double X_max=Double.parseDouble(data[5]);
					double Y_max=Double.parseDouble(data[6]);
					Point3D  min= new Point3D(X_min,Y_min);
					Point3D  max= new Point3D(X_max,Y_max);
					Point3D MinpointOnScreen=Map.getPositionOnScreen(min);
					Point3D MaxpointOnScreen=Map.getPositionOnScreen(max);
					int Rec_width= MaxpointOnScreen.ix()-MinpointOnScreen.ix();
					int Rec_hieght= MinpointOnScreen.iy()-MaxpointOnScreen.iy();
					g2.setColor(Color.BLACK);
					g2.fillRect(MinpointOnScreen.ix(), MaxpointOnScreen.iy(), Rec_width,Rec_hieght);
				}

				// If the board contains fruits draw a red circle at the fruit's location
				else if (data[0].equals("F")) {
					g2.setColor(Color.red);
					g2.fillOval(pointOnScreen.ix(),pointOnScreen.iy(), 25, 25);
				}
				// If the board contains packamns draw a yellow circle at the fruit's location
				else if (data[0].equals("P")) {
					g2.setColor(Color.yellow);
					g2.fillOval(pointOnScreen.ix(),pointOnScreen.iy(), 25, 25);
				}
				// If the board contains ghosts draw a white circle at the fruit's location
				else if (data[0].equals("G")) {
					g2.setColor(Color.white);
					g2.fillOval(pointOnScreen.ix(),pointOnScreen.iy(), 25, 25);
				}
				// If the board contains player draw a pink circle at the fruit's location
				else if (data[0].equals("M")) {
					g2.setColor(Color.pink);
					g2.fillOval(pointOnScreen.ix(),pointOnScreen.iy(), 30, 30);
				}

			}
		}
	}

	/**
	 * This function draws the image of the map and 
	 * depend of the game status draws the elements on the board
	 */
	public void paint(Graphics g) {
		g.drawImage(Map.img, 0, 0, getWidth(),getHeight(), this);
		_paper = _panel.getGraphics();
		if (GameStatus!=-1) {
			ObjectCoponent drawObject= new ObjectCoponent();
			drawObject.paintComponent(_paper);
		}
		if (GameStatus==3) 
			if (!play.isRuning()) {
				JOptionPane.showMessageDialog(_panel, play.getStatistics());
			}
		if ( GameStatus==4)
			if (!play.isRuning()) {
				GameStatus=0;
				JOptionPane.showMessageDialog(_panel, play.getStatistics());			
			}
	}

	/**
	 * The thread of the regular game where the user can move the player wherever he want
	 */
	public void threadGame() {
		Thread thread= new Thread() {
			@Override
			public void run() {
				while (play.isRuning()) {
					//	if (angle!=0)
					play.rotate(angle); 
					repaint();

					try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				}
			}
		};
		thread.start();
		repaint();
	}

	/**
	 * The thread of the automatic game where the computer move the player
	 * @param automatic
	 */
	public void AutothreadGame(Auto automatic) {
		//	while (GameStatus==4) {
		Thread thread= new Thread() {
			@Override
			public void run() {
				while (play.isRuning()) {
					ArrayList<String> path = new ArrayList<String>();
					while(!automatic.getFruits().isEmpty()) {
						path=new ArrayList<String>(automatic.closestFruit(automatic.getPlayer().getLocation(),automatic.getPlayer().getVeloctiy()));
					//	String[] Fruit = path.get(path.size()-1).split(",");
						for (int i=0; i<path.size(); i++) {
							String []nextpoint= path.get(i).split(",");
								double [] azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
										(automatic.getPlayer().getLocation(), new Point3D(Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
								angle=azimuth_elevation_dist[0];
								double time=azimuth_elevation_dist[2]/automatic.getPlayer().getVeloctiy();
								double timeTillPoint=0;
								while (timeTillPoint<time) {
									play.rotate(angle);
									repaint();
									try {
										Thread.sleep(100);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
									automatic.getFruits().clear();
									for (String obj:play.getBoard()) {
										String data[]= obj.split(",");
										if (data[0].equals("M"))
											automatic.getPlayer().setLocation(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])));
										else if (data[0].equals("F"))
											automatic.getFruits().add(new Fruit(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3]))));
									}
									azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
											(automatic.getPlayer().getLocation(), new Point3D(Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
									angle=azimuth_elevation_dist[0];
									timeTillPoint=timeTillPoint+1;
								}	
								
							}
						}
					}
				}
	//		}
		};
		thread.start();
		repaint();
		//		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		X=event.getX();
		Y=event.getY();
		Point3D point=new Point3D(X,Y);
		if (GameStatus==2) {
			point=Map.PixeltoCoordanite(point);
			play.setInitLocation(point.x(),point.y());
		}
		if (GameStatus==3) {
			point= Map.PixeltoCoordanite(point);
			Point3D player=null;
			boolean flag=true;
			while (flag) {
				for (String obj: play.getBoard()) {
					String[] data=obj.split(",");
					double x=Double.parseDouble(data[2]);
					double y=Double.parseDouble(data[3]);

					if (data[0].equals("M")) {
						player=new Point3D(x,y);
						flag=false;
					}
				}
			}
			double move[]=MyCoords.azimuth_elevation_dist(player, point);
			angle=move[0];
			play.rotate(angle); 
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	public static void main(String[] args) {
		myFrame frame = new myFrame();

		frame.setBounds(0, 0,  frame.width, frame.height);
		frame.createGui();
		frame.setVisible(true);
	}

}
