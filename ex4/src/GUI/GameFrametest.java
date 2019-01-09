package GUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import GIS.Path;
import Geom.Point3D;
import Map.Map;
import Map.MyCoords;

/**
 * This class represents all the info of the Jframe and creates it
 * @author Ester Reznikov and Chen Ostrovski
 */
public class GameFrametest extends JFrame implements MouseListener{

	private static final long serialVersionUID = 1L;

	private Container window;
	private JPanel _panel;
	private Graphics _paper;
	private int x, y;
	private int GameStatus;
	static Map map;
	private int width;
	private int height;
	private MyCoords mycoords;
	static ArrayList<LineComponent> lineComponents;



	public GameFrametest(){
		super("Packman invasion"); //setTitle("Map Counter");  // "super" Frame sets its title
		//	Moves and resizes this component. 
		//	The new location of the top-left corner is  specified by x and y, 
		//	and the new size is specified by width and height
		//	setBounds(x,y,width,height)
		this.setBounds(0,0,1433,700); //setSize(1433,700);        // "super" Frame sets its initial window size
		//      Exit the program when the close-window button clicked
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		map=new Map();
		width = Map.img.getWidth();
		height= Map.img.getHeight();
		mycoords=new MyCoords();
		lineComponents=new ArrayList<LineComponent>();
		pack();

	}
	/**
	 * This function contains all the gui info: the size of panel and all the buttons actions
	 */
	public void createGui(){              				
		//	A Container is a component  that can contain other GUI components
		window = this.getContentPane(); 
		window.setLayout(new FlowLayout());

		//	Add "panel" to be used for drawing            
		_panel = new JPanel();
		Dimension d= new Dimension(1433,642);
		_panel.setPreferredSize(d);		               
		window.add(_panel);

		// A menu-bar contains menus. A menu contains menu-items (or sub-Menu)
		MenuBar menuBar;   // the menu-bar
		Menu AddMenu;
		Menu FileMenu, GameMenu;       // each menu in the menu-bar
		MenuItem PackmanMenu, FruitMenu, ShowPointMenu; // an item in a menu
		MenuItem OpenFileMenu, SaveCsvMenu,SaveKmlMenu;
		MenuItem PlayMenu, RestartMenu;

		menuBar = new MenuBar();


		AddMenu = new Menu("Add");

		menuBar.add(AddMenu);  // the menu-bar adds this menu

		PackmanMenu = new MenuItem("Packman");
		AddMenu.add(PackmanMenu); // the menu adds this item
		PackmanMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStatus=1;
			}
		});
		FruitMenu = new MenuItem("Fruit");
		AddMenu.add(FruitMenu); // the menu adds this item
		FruitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStatus=2;
			}
		});  
		ShowPointMenu = new MenuItem("Show point");
		AddMenu.add(ShowPointMenu); // the menu adds this item
		ShowPointMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStatus=0;
			}
		});  

		FileMenu = new Menu("File");

		menuBar.add(FileMenu);  // the menu-bar adds this menu

		OpenFileMenu = new MenuItem("Open file");
		FileMenu.add(OpenFileMenu); // the menu adds this item
		OpenFileMenu.addActionListener(new ActionListener() {
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
				}

				GameStatus=2;
				repaint();
			}

		});

		SaveCsvMenu = new MenuItem("Save csv");
		FileMenu.add(SaveCsvMenu); // the menu adds this item
		SaveCsvMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("/home/me/Documents"));
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
		});  
		SaveKmlMenu = new MenuItem("Save Kml");
		FileMenu.add(SaveKmlMenu); // the menu adds this item
		SaveKmlMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("/home/me/Documents"));
				int retrival = chooser.showSaveDialog(null);
				if (retrival == JFileChooser.APPROVE_OPTION) {
					try {
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}); 
		GameMenu = new Menu("Game");

		menuBar.add(GameMenu);  // the menu-bar adds this menu

		PlayMenu = new MenuItem("Play");
		GameMenu.add(PlayMenu); // the menu adds this item
		PlayMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameStatus=-1;
				repaint();
				threadGame();
				repaint();
			}
		});
		RestartMenu = new MenuItem("Restart");
		GameMenu.add(RestartMenu); // the menu adds this item
		RestartMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lineComponents.clear();
				GameStatus=0;
				repaint();
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
	/**
	 * This function paint the elements on the map, also paint the lines between the Packman and the Fruits if the use press the "Play" button 
	 */
	public void paint(Graphics g) {
		g.drawImage(Map.img, 0, 0, getWidth(), getHeight(), this);
		paintElement();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		if (GameStatus==-1){
			/*
			for (Path path: solution) {
				LineComponent lineComponent=new LineComponent( path.getColor());
				Point3D start=Map.getPositionOnScreen(path.getStartPoint());
				if (!path.isEmpty())
					lineComponent.addLine(start.x(),path.get(0).getFruit().getLocationInPixle().x(), start.y(), path.get(0).getFruit().getLocationInPixle().y());
				for (int i=0; i<path.size()-1; i++) {
					lineComponent.addLine(path.get(i).getFruit().getLocationInPixle().x(), path.get(i+1).getFruit().getLocationInPixle().x(),
							path.get(i).getFruit().getLocationInPixle().y(), path.get(i+1).getFruit().getLocationInPixle().y());
				} 
				lineComponents.add(lineComponent);
				lineComponent.paintComponent(_paper);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			*/


		}
	}
	/**
	 *  Auxiliary function that paints all the elements
	 */
	protected void paintElement() {
		_paper = _panel.getGraphics();
		FruitCoponent draw_fruits= new FruitCoponent();
		PackmanCoponent draw_packmen= new PackmanCoponent();
		draw_fruits.paintComponent(_paper);
		draw_packmen.paintComponent(_paper);

		_paper.setFont(new Font("Monospaced", Font.PLAIN, 14));               
		_paper.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);

	}

	/**
	 * Function that create a thread to every path and paint it
	 */
	public void threadGame() {
/*
		for (Path path: solution) {

			Thread thread= new Thread() {
				@Override
				public void run() {

					for (Metadata metadata: path)
					{
						// Calculates the distance, time, vector between the Packman and its text Fruit. Move the Packman to the next Fruit on the vector
						double distance= mycoords.distance3d(path.getPackman().getPackmanLocation(), metadata.getPoint());
						double time=(distance-path.getPackman().getRadius())/path.getPackman().getVelocity();
						Point3D vector= new Point3D(mycoords.vector3D(path.getPackman().getPackmanLocation(), metadata.getPoint()));
						double norma= norma(vector, path.getPackman().getVelocity());
						Point3D normalVector=new Point3D(vector.x()/norma, vector.y()/norma, vector.z()/norma);
						game.packmen.indexOf(path.getPackman());
						int i=0;
						while (i<=time) {
							normalVector.set_x(normalVector.x()*path.getPackman().getVelocity());
							normalVector.set_y(normalVector.y()*path.getPackman().getVelocity());
							normalVector.set_z(normalVector.z()*path.getPackman().getVelocity());
							distance=mycoords.distance3d(path.getPackman().getPackmanLocation(), normalVector);
							path.getPackman().getRadius();
							path.getPackman().getVelocity();
							path.getPackman().setPackmanLocation(mycoords.add(path.getPackman().getPackmanLocation(),normalVector));
							path.getPackman().setLocationInPixle((int)(path.getPackman().getLocationInPixle().x()*path.getPackman().getVelocity()),(int)(path.getPackman().getLocationInPixle().y()*path.getPackman().getVelocity()));
							repaint();
							i=i+5;

							try {
								Thread.sleep(10*path.getPackman().getVelocity());
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						path.getPackman().setPackmanLocation(metadata.getPoint());
						repaint();

						try {
							Thread.sleep(10*path.getPackman().getVelocity());
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						// Remove the eaten Fruits
						game.fruits.remove(metadata.getFruit());
						repaint();

					}
				}
				
			};
			// Start the thread
			thread.start();		
			if (game.fruits.isEmpty())
				repaint();
		}
*/
	}
	
	/**
	 * This function calculates the norma of the vector
	 * @param vector the vector on which the calculation is made
	 * @param velocity the velocity of the vector
	 * @return
	 */
	private static double norma(Point3D vector, long velocity) {
		return Math.sqrt(Math.pow(vector.x(), 2)+Math.pow(vector.y(), 2)+Math.pow(vector.z(), 2))*velocity;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		x=event.getX();
		y=event.getY();

		Point3D tocoord= new Point3D(x,y);
		if (GameStatus==1) {
			tocoord=Map.PixeltoCoordanite(tocoord);
		//	Packman p= new Packman(tocoord);
		//	game.packmen.add(p);
		}
		else if (GameStatus==2) {
			tocoord=Map.PixeltoCoordanite(tocoord);
		//	Fruit f= new Fruit(tocoord);
		//	game.fruits.add(f);
		}
		repaint();
	}


	// Not Used, but need to provide an empty body for compilation
	public void mouseReleased(MouseEvent event){}
	public void mouseClicked(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	public static void main(String[] args) {
		GameFrametest frame = new GameFrametest ();
		frame.setBounds(0, 0, frame.width, frame.height);
		frame.createGui();
		frame.setVisible(true);
		System.out.println(frame.getHeight()+" " +frame.getWidth());
		System.out.println(Map.img.getHeight()+" " +Map.img.getWidth());
		System.out.println( frame.width+" "+frame.height);
	}
	/**
	 * This class shows the Fruits on the map
	 */
	class FruitCoponent extends Component{
		private static final long serialVersionUID = 1L;
	//	ArrayList<Fruit> fruits;
		FruitCoponent(){
			super();
		//	fruits = new ArrayList<Fruit>(game.fruits);
		}
		public void paintComponent(Graphics g) {
			/*
			if (!this.fruits.isEmpty()) {
				for (Fruit fruit : this.fruits) {
					if (game.fruits.contains(fruit)) {
						int x=(int)fruit.getLocationInPixle().x();
						int y=(int)fruit.getLocationInPixle().y();
						if (fruit.getId()%4==0)
							_paper.drawImage(Map.Fruit1,x,y,25,25,  null);
						if (fruit.getId()%4==1)
							_paper.drawImage(Map.Fruit2,x,y,30,30,  null);
						if (fruit.getId()%4==2)
							_paper.drawImage(Map.Fruit3,x,y,25,25,  null);
						if (fruit.getId()%4==3)
							_paper.drawImage(Map.Fruit4,x,y,30,30,  null);
					}
				}
			}
			*/
		}
	}
	/**
	 * This class shows the Packmen on the map
	 */
	class PackmanCoponent extends Component{
		private static final long serialVersionUID = 1L;
	//	ArrayList<Packman> packmen;
		PackmanCoponent(){
			super();
		//	packmen = game.packmen;
		}
		public void paintComponent(Graphics g) {
/*
			for (Packman packman : packmen) {
				_paper.drawImage(Map.Packman,(int)packman.getLocationInPixle().x(),(int)packman.getLocationInPixle().y(),20,20,  null);
			}
			*/
		}

	}
	/**
	 * This class draws a line between two points
	 */
	class LineComponent extends Component {

		private static final long serialVersionUID = 1L;
		ArrayList<Line2D.Double> lines;
		Color color;

		LineComponent(Color color){
			super();
			lines = new ArrayList<Line2D.Double>();
			this.color=color;
		}
		/**
		 * Add line between two points
		 * @param x1 the x coordinate of the first point
		 * @param x2 the x coordinate of the second point
		 * @param y1 the y coordinate of the first point
		 * @param y2 the y coordinate of the second point
		 */
		public void addLine(double x1, double x2, double y1, double y2) {

			Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);
			lines.add(line);
			repaint();
		}

		/**
		 * Draws the lines
		 * @param _paper the panel that we draw on it
		 */
		public void paintComponent(Graphics _paper) {
			Graphics2D g2 = (Graphics2D) _paper;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(this.color);
			for (Line2D.Double line : lines) {
				g2.drawLine((int)line.getX1(),(int)line.getY1(),(int)line.getX2(),(int)line.getY2());   
			}

		}
	}
}

