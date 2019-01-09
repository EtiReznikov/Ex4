
package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Rectangle;
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
import Algorithm.Algo;
import Algorithm.Auto;
import GameObjects.Fruit;
import Geom.Point3D;
import Robot.Play;

import Map.Map;
import Map.MyCoords;



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
		//window.pack();
		//	pack();


	}		

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


		setPlayer = new MenuItem("Set Player");
		Game.add(setPlayer);
		setPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStatus=2;
				repaint();
			}
		});
		start = new MenuItem("Start");
		Game.add(start);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play.start();
				GameStatus=3;
				//	repaint();
				threadGame();
				//	repaint();
				/*			
				ObjectCoponent drawObject= new ObjectCoponent();

				Thread thread=new Thread() {
					public void run() {
						while (play.isRuning()) {
							play.rotate(move[0]); 
							drawObject.paintComponent(_paper);
							try {
								sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
				};
				thread.start();
			}
				 */
			}

		});
		stop = new MenuItem("stop");
		Game.add(stop);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play.stop();
			}
		});
		restart = new MenuItem("restart");
		Game.add(restart);
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play.getBoard().removeAll(play.getBoard());
				GameStatus=-1;
				repaint();
			}
		});
		AutomaticPlayer = new MenuItem("Automatic play");
		Game.add(AutomaticPlayer);
		AutomaticPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Auto automatic = new Auto (play);

				//	System.out.println(play);
				automatic.setstart();
				System.out.println();
				for( String s:play.getBoard())
					System.out.println(s);
			//	play=automatic.getPlay(); 
				//Point3D player=new Point3D (Map.getPositionOnScreen(automatic.getPlayer().getLocation()));
				//X=(int)player.x();
				//Y=(int)player.y();
				repaint();

				//	play=automatic.getPlay();
				play.start();
				threadGame();
				while (play.isRuning()) {
				angle=30;
				//AutomaticGame(automatic);
				//		play=automatic.play;
				//	play=automatic.getPlay();

					//	repaint();
				}

				//	play=automatic.getPlay();

				//repaint();
			}

			/*repaint();
				boolean flag=true;
				String player="";
				while (flag) {
					for (String obj: play.getBoard()) {
						String[] data=obj.split(",");
						if (data[0]=="M") {
							player=obj;
							flag=false;
						}
					}
				}
			 */
			//play.start();
			/*
				while (play.isRuning()) {
				//	player= automatic.closest(player);
					repaint();
				}
			 */
			//	}
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
				//	double x=Double.parseDouble(data[3]);
				//	double y=Double.parseDouble(data[2]);
				Point3D gps=new Point3D(x,y);
				Point3D pointOnScreen=Map.getPositionOnScreen(gps);
				//			System.out.println(data[0]+" "+pointOnScreen.toString());
				//		if (data[0]!="B") {
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
					//	int Rec_hieght= Math.abs(MaxpointOnScreen.iy()-MinpointOnScreen.iy());
					//int Rec_hieght= MinpointOnScreen.iy()-MaxpointOnScreen.iy();
					//	Rectangle rec = new Rectangle(MinpointOnScreen.ix(), MaxpointOnScreen.iy(), Rec_width,Rec_hieght); 

					g2.setColor(Color.BLACK);
					g2.fillRect(MinpointOnScreen.ix(), MaxpointOnScreen.iy(), Rec_width,Rec_hieght);
					//			g2.fillRect(MinpointOnScreen.ix(), MinpointOnScreen.iy(), Rec_width,Rec_hieght);
					//	g2.drawRect((int)X_min,(int) Y_min, Rec_width, Rec_hieght);
					//				    Point3D minplusmteter= MyCoords.addmetertopoint(X_min, Y_min);
					//				    minplusmteter=Map.getPositionOnScreen(minplusmteter);
					//				    Point3D maxplusmteter= MyCoords.addmetertopoint(X_max, Y_max);
					//				    maxplusmteter=Map.getPositionOnScreen(maxplusmteter);
					//				    int Rec_widthplus= maxplusmteter.ix()-minplusmteter.ix();
					//				    int Rec_hieghtplus= minplusmteter.ix()-maxplusmteter.ix();
					//				    g2.drawRect(minplusmteter.ix(), maxplusmteter.iy(), Rec_widthplus, Rec_hieghtplus);
				}
				else if (data[0].equals("F")) {
					if (id%4==0)
						_paper.drawImage(Map.Fruit1,pointOnScreen.ix(),pointOnScreen.iy(),25,25,  null);
					else  if (id%4==1)
						_paper.drawImage(Map.Fruit2,pointOnScreen.ix(),pointOnScreen.iy(),25,25,  null);
					else if (id%4==2)
						_paper.drawImage(Map.Fruit3,pointOnScreen.ix(),pointOnScreen.iy(),25,25,  null);
					else if (id%4==3)
						_paper.drawImage(Map.Fruit4,pointOnScreen.ix(),pointOnScreen.iy(),25,25,  null);   
				}
				else if (data[0].equals("P")) {
					_paper.drawImage(Map.Packman,pointOnScreen.ix(),pointOnScreen.iy(),25,25,  null);
				}
				else if (data[0].equals("G")) {
					_paper.drawImage(Map.ghost,pointOnScreen.ix(),pointOnScreen.iy(),25,25,  null);
				}
				else if (data[0].equals("M")) {
					_paper.drawImage(Map.player,pointOnScreen.ix(),pointOnScreen.iy(),35,35,  null);
				}

			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(Map.img, 0, 0, getWidth(), getHeight(), this);
		//		g.drawImage(Map.img, 0, 0,getHeight() ,getWidth(), this);
		_paper = _panel.getGraphics();
		if (GameStatus!=-1) {
			ObjectCoponent drawObject= new ObjectCoponent();
			drawObject.paintComponent(_paper);
		}
		if (GameStatus==3)
			if (!play.isRuning()) {
				JOptionPane.showMessageDialog(_panel, play.getStatistics());
			}
	}

	public void threadGame() {

		Thread thread= new Thread() {
			@Override
			public void run() {
				while (play.isRuning()) {
					if (angle!=0)
						play.rotate(angle); 
					repaint();

					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				}
			}
		};
		thread.start();
		repaint();
	}

	public void AutomaticGame(Auto automatic) {
		String [] data;
		double velocity=automatic.getPlayer().getVeloctiy();
		ArrayList<String> shortestPath=automatic.closestFruit(automatic.getPlayer().getLocation(), velocity);
		String[] fruit=shortestPath.get(shortestPath.size()-1).split(",");
		Fruit fruitlocation=new Fruit (new Point3D (Double.parseDouble(fruit[0]), Double.parseDouble(fruit[1])),Integer.parseInt( fruit[3]));
		//	System.out.println(automatic.getFruits().isEmpty());
		while (automatic.getFruits().contains(fruitlocation)){
			System.out.println(true);
			for(int i=0;i<shortestPath.size();i++) {
				//		System.out.println(true);
				String[] nextpoint=shortestPath.get(i).split(",");
				double[] azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
						(automatic.getPlayer().getLocation(), new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
				while (azimuth_elevation_dist[2]>automatic.getPlayer().getRadius()) {
					azimuth_elevation_dist=MyCoords.azimuth_elevation_dist
							(automatic.getPlayer().getLocation(), new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));
					angle=azimuth_elevation_dist[0];
					System.out.println("angle:"+angle);

					automatic.getFruits().removeAll(automatic.getFruits());
					for (String obj: play.getBoard()) {
						data=obj.split(",");
						if (data[0].equals("M")){
							automatic.getPlayer().setLocation(new Point3D(Double.parseDouble(data[2]), Double.parseDouble(data [3])));
						}
						else if (data[0].equals("F")) {
							automatic.getFruits().add(new Fruit(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3])), Integer.parseInt(data[1])));
						}
					}
					automatic.getPlayer().setLocation(new Point3D (Double.parseDouble(nextpoint[0]),Double.parseDouble(nextpoint[1])));	
					play=automatic.play;
					play.start();
					//	play.rotate(angle);
				}
			}
		}
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
		//frame.setBounds(0, 0, 1433, 642);
		//frame.setSize(1433, 642);
		//frame.setBounds(0, 0, 700, 700);

		frame.setBounds(0, 0,  frame.width, frame.height);
		frame.createGui();
		frame.setVisible(true);
	}

}
