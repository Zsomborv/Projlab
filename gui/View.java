package gui;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import characters.Eskimo;
import characters.PolarBear;
import characters.Researcher;
import game.GameWorld;
import game.IceTable;
import game.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import objects.*;

/**
 * A nézetet (View) megvalósító osztály. Grafikus felület megjelenítéséért felel, nyilványtartja az
 * ablak méretét és kirajzolja minden update hívásra az objektumok aktuális állapotát. 
 * @author BCSV
 *
 */
public class View implements Serializable{
	/**
	 * GameWorld objektum, a játékvilág és a nézet összekapcsolására
	 */
	private GameWorld gw;
	/**
	 * Ablak fix szélessége
	 */
	private static int width = 1300;
	/**
	 * Ablak fix magassága
	 */
	private static int height = 800;
	/**
	 * Játék indításnál bekért eszkimo karakterek száma
	 */
	private int eskimonum = 0;
	/**
	 * Játék indításnál bekért sarkkutató karakterek száma
	 */
	private int researchernum = 0;
	/**
	 * Eskimok neveinek tárolása
	 */
	private ArrayList<String> eskimoname = new ArrayList<String>();
	/**
	 * Sarkkutatok neveinek tárolása
	 */
	private ArrayList<String> researchername = new ArrayList<String>();
	/**
	 * Karaktertípus bekérésénél fontos, hogy mindig az előzőleg megadott karakter
	 * típusának ellentettjét vegyük.
	 */
	private boolean iseskimo;
	/**
	 * Lett-e már kiválasztva karaktertípus az adatok bekérésénél.
	 */
	private int charchoice = 0;
	/**
	 * Boolean, ha túl kevés a beadott karakterek száma (<3), akkor true
	 */
	private boolean toolow = false;
	/**
	 * Karaktereket reprezentáló körök sugara
	 */
	private double charsize = 10;
	/**
	 * Main objektum.
	 */
	private Main main;
	/**
	 * Boolean, ha már használtunk egy Itemet, akkor true
	 */
	private boolean usedsomething=false;
	/**
	 * Jégtáblák koordinátáit tároló lista
	 */
	private ArrayList<int[]> itcoords = new ArrayList<int[]>();
	/**
	 * Jégtáblákat reprezentáló körök sugara
	 */
	private double r = 50;
	/**
	 * Jégtáblák alapjának mérete.
	 */
	private double paneSize = 1.45 * r;
	/**
	 * Boolean, ha a játék véget ér, akkor true
	 */
	private boolean gameover = false;
	/**
	 * Játék indításánál fontos
	 */
	private boolean start= true;
	/**
	 * Meglepetés szövegét tároló string
	 */
	private String surprisestate;
	/**
	 * Boolean, ha true akkor hóviharba kerültünk
	 */
	private boolean snowstormstate;
	/**
	 * Játék kezelő gombjai
	 */
	private Button[] buttons=new Button[] {
			new Button("Step"),
			new Button("Special"),
			new Button("Pick up"),
			new Button("Manage"),
			new Button("Bag"),
			new Button("Build gun")
	};
	/**
	 * Játék információit leíró Labelek
	 */
	private Label[] labels = new Label[] {
    		new Label("Name: "),//+gw.getActualplayer().getCharName()),
    		new Label("E: "),//gw.getActualplayer().getEnergy()),
    		new Label("Hp: "),//+gw.getActualplayer().getLife())
    		new Label("IT: ")
    	};

	/**
	 * Eskimo nevek gettere, a GameWorld kéri el
	 * @return az eskimo nevek listáját
	 */
	public ArrayList<String> getEskimoNames()
	{
		return eskimoname;
	}
	/**
	 * Reseracher nevek gettere, a GameWorld kéri el
	 * @return a researcher nevek listáját
	 */
	public ArrayList<String> getResearcherNames()
	{
		return researchername;
	}
	/**
	 * Eskimo számának lekérése
	 * @return Eskimok száma
	 */
	public int getNumOfEskimo()
	{
		return eskimonum;
	}
	/**
	 * Reseracherek számának lekérése
	 * @return Reseracherek száma
	 */
	public int getNumOfResearcher()
	{
		return researchernum;
	}
	/**
	 * A nézet frissítéséért felelős metódus, ami minden hívásra kirajzolja az aktuális játék állapotát.
	 * @param primaryStage
	 */
	//Updateben rajzolunk ujat, ami változott
	public void update(Stage primaryStage) 
	{
		if(gameover)
		{
			endAlert();
		}
		labels[0].setText("Name: " + gw.getActualplayer().getCharName());
		if(gw.getActualplayer().getClass().equals(characters.Eskimo.class))
		{
			labels[0].setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		else
		{
			labels[0].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));  
		}
		labels[0].setStyle("-fx-text-fill: WHITE;");
		labels[1].setText("E: "+gw.getActualplayer().getEnergy());
		labels[2].setText("Hp: "+gw.getActualplayer().getLife());
		labels[3].setText("IT: "+gw.getTables().indexOf(gw.getActualplayer().getMyTable()));
		if(snowstormstate)
		{
			SnowStormPopup();
			snowstormstate = false;
		}
		//int [][] matrix = gw.getNeighboursMatrix();
        primaryStage.setTitle("Antarctica");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("igloo.png")));
        
        MenuBar menubar = new MenuBar();
        menubar = Menus();
        VBox vBox = new VBox(menubar);
        
        BorderPane program = new BorderPane();
        program.setTop(vBox);
        HBox hboxbuttons = new HBox();
        hboxbuttons.getChildren().add(addButtonsPanel(gw));
        hboxbuttons.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(hboxbuttons, Pos.CENTER);
        BorderPane.setMargin(hboxbuttons, new Insets(12,12,12,12));
        program.setBottom(hboxbuttons);
        if(!start)
        {
        	program.setCenter(drawAllIceTable());
        	start = false;
        }
        
        
        StackPane root = new StackPane();
        
        root.getChildren().add(program);
        root.setBackground(new Background(new BackgroundFill(Color.web("#6699ff"), CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.setScene(new Scene(root, width, height)); //TODO fix ablakméret.
        primaryStage.setResizable(false);
        primaryStage.show();
        System.out.println("Updated");
	}
	/**
	 * Main gettere
	 * @return main
	 */
    public Main getMain()
    {
    	return main;
    }
    /**
     * A GameWorld inicializálásához szükséges adatok bekéréséért felel
     * Eskimok, researcherek száma és azok nevei.
     * @param primaryStage
     * @param main
     */
	public void initGui(Stage primaryStage,Main main)
	{
		this.main = main;
		GameSettings();
		if(!main.getAdded())
		{
			 GameSettings();
		        while(eskimonum+researchernum <3)
		        {
		        	toolow = true;
		        	if(!iseskimo) iseskimo = !iseskimo;
		        	charchoice--;
		        	GameSettings();
		        }         
		}
	}
	/**
	 * A játék végét jelző metódus
	 * Egy ablakban jelez a játékosnak, hogy vége van a játéknak!
	 */
	public void endAlert()
	{
			Alert alert = new Alert(AlertType.INFORMATION);
    		alert.getDialogPane().setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("igloo.png"),50,50,true,false)));
    		alert.setTitle("Game over");
    		alert.setHeaderText("Game over");
    		alert.setContentText("Thank you for playing");
    		alert.showAndWait().ifPresent(response->{
    			System.exit(0);
    		});
    		gw.setOngoing(false);
    		/*for(int i= 0;i<buttons.length;i++)
    		{
    			buttons[i].setDisable(true);
    		}*/
	}
	/**
	 * GameOver gettere
	 * @return gameover vége van-e a játéknak
	 */
	public boolean getGameover() {
		return gameover;
	}
	/**
	 * GameOver setter
	 * @param gameover
	 */
	public void setGameover(boolean gameover) {
		this.gameover = gameover;
	}
	/**
	 * Eskimonum getter
	 * @return Eskimok száma
	 */
	public int getEskimoNum() {
		return eskimonum;
	}
	/**
	 * Researchernum getter
	 * @return Sarkkutatok száma
	 */
	public int getResNum() {
		return researchernum;
	}
	/**
	 * GameGui setter
	 * @param gw
	 */
	public void gameGui(GameWorld _gw) {
		this.gw = _gw;   
	}
	/**
	 * Kirajzolja az összes jégtáblát, végigiterál a jégtáblákon és a megadott 
	 * méretben kirajzolja őket
	 * és összeköti a jégtáblákat.
	 * @return jégtáblákat tartalmazó Pane-t
	 */
	public Pane drawAllIceTable() 
	{
		Pane p = new Pane();
		int x = 50;
		int y = 75;
		boolean odd = true;
		int itcnt = 0;
		for(int row = 0; row<4;row++)
		{
			if(!odd) x = width/6-50; 
			else x = 50;
			for(int i = 0; odd? i<6 : i<5 ; i ++)
			{
				int[] xy = {x,y};
				itcoords.add(xy);
				x += width/6;
				itcnt++;
			}
			odd = !odd;
			y+=height/6;
		}
		int toadd = (int)(Math.cos(0.75)*r);
		for(int i =0;i<gw.getTables().size()-1;i++)
		{
			for(int j = i+1;j<gw.getTables().size();j++)
			{
				if(gw.getNeighboursMatrix()[i][j]==1)
				{
					int[] coordsbegin = itcoords.get(i);
					int[] coordsend = itcoords.get(j);
					
					Line line = new Line(coordsbegin[0]+toadd, coordsbegin[1]+toadd, coordsend[0]+toadd, coordsend[1]+toadd);
					line.setStroke(Color.ANTIQUEWHITE);
					line.setStrokeWidth(5);
					p.getChildren().add(line);
				}
			}
		}
		for(int k = 0; k<gw.getTables().size();k++)
		{
			p.getChildren().add(IceFieldDraw(k,itcoords.get(k)[0],itcoords.get(k)[1],gw.getTables().get(k)));
		}
		return p;
	}
	/**
	 * Az egyes jégtáblákon lévő karakterek, itemek, medve és jégtáblák adatait megjelenítő függvény
	 * @param idx Jégtáblák listában lévő index
	 * @param x jégtábla x pozíciója
	 * @param y jégtábla y pozíciója
	 * @param it IceTable param
	 * @return Mindent tartalmazó StackPane-t ad vissza
	 */
	public StackPane IceFieldDraw(int idx,int x, int y,IceTable it)
	{
		
		String color = "black";
		double r = 50;
		double paneSize = 1.45 * r;
		StackPane icetable = new StackPane();
		Circle node = new Circle();
		node.setRadius(r);
		if(it.getStrength() != 0 || it.getSnow() >0)
		{
			node.setFill(Color.ANTIQUEWHITE);
			color = "black";
		}
		else
		{
			node.setFill(Color.BLUE);
			color = "white";			
		}
		icetable.getChildren().add(node);
		icetable.setPrefSize(paneSize, paneSize);
        icetable.setMaxSize(paneSize, paneSize);
        icetable.setMinSize(paneSize, paneSize);
        icetable.setTranslateX(x);
		icetable.setTranslateY(y);
			Label item = new Label();
			Label ho = new Label("S: "+Integer.toString(it.getSnow())); //snow quantity
			Label icetablename = new Label("IT: "+Integer.toString(idx));
			Label strength = new Label("");
			if(it.isChecked() == true)
			{
				strength = new Label(Integer.toString(it.getStrength()));
			}
			ho.setStyle("-fx-text-fill: "+color+";");
			icetablename.setStyle("-fx-text-fill: "+color+";");
			item.setStyle("-fx-text-fill: "+color+";");
			strength.setStyle("-fx-text-fill: "+color+";");
			if(it.getSnow() == 0)
			{
				item.setText(getItemName(it));  //item name
				if(it.isFrozen() == true)
				{
					item.setBackground(new Background(new BackgroundFill(Color.web("#A5F2F3"), CornerRadii.EMPTY, Insets.EMPTY)));
					item.setStyle("-fx-text-fill: black;");
				}
			}
		else 
			{
				item.setText("");
				
			}
			VBox vb = new VBox(); //characters vbox
			int eskimo=0;
			int researcher = 0;
			int bear = 0;
			for(int i =0;i<it.getCharacters().size();i++)
			{
				if(it.getCharacters().get(i).getClass().equals(characters.Eskimo.class))
					eskimo++;
				if(it.getCharacters().get(i).getClass().equals(characters.Researcher.class))
					researcher++;
				if(it.getCharacters().get(i).getClass().equals(characters.PolarBear.class))
					bear++;
			}
			if(eskimo>0)
				vb.getChildren().add(drawEskimo(eskimo));
			if(researcher>0)
				vb.getChildren().add(drawResearcher(researcher));
			if(bear>0)
				vb.getChildren().add(drawBear());
			//vb.getChildren().addAll(drawEskimo(),drawResearcher(),drawBear());
			icetable.getChildren().addAll(ho,icetablename,item,vb,strength);
			icetable.setAlignment(ho,Pos.TOP_RIGHT);
			icetable.setAlignment(item,Pos.BOTTOM_RIGHT);
			icetable.setAlignment(vb,Pos.TOP_LEFT);
			icetable.setAlignment(strength, Pos.BOTTOM_LEFT);
			if(it.isIglu()>0) {
			if(it.getIglu().getClass().equals(objects.Iglu.class))
			{
				Circle iglu = new Circle();
				iglu.setRadius(20);
				iglu.setFill(Color.TRANSPARENT);
				iglu.setStroke(Color.DARKGRAY);
				iglu.setStrokeWidth(4);
				icetable.getChildren().add(iglu);
			}
			if(it.getIglu().getClass().equals(objects.Camp.class))
			{
				Polygon camp = new Polygon();
				camp.getPoints().addAll(new Double[] {
						icetable.getTranslateX(), icetable.getTranslateY()-40,   
						icetable.getTranslateX()-10,icetable.getTranslateY()+(Math.cos(60)*20),
						icetable.getTranslateX()+10,icetable.getTranslateY()+(Math.cos(60)*20)						
				});
				camp.setFill(Color.GREEN);
				icetable.getChildren().add(camp);
				icetable.setAlignment(camp,Pos.BOTTOM_CENTER);
			}
			}
		return icetable;
		
	}
	/**
	 * Az osztályok alapján meghatározott item nevet ad vissza
	 * @param it aktuális IceTable
	 * @return Item neve
	 */
	public String getItemName(IceTable it)
	{
		try
		{
			if(it.getItem().getClass().equals(objects.BreakableShovel.class))
				return "BSH";
			if(it.getItem().getClass().equals(objects.Camp.class))
				return "C";
			if(it.getItem().getClass().equals(objects.Food.class))
				return "F";
			if(it.getItem().getClass().equals(objects.Gun.class))
				return "G";
			if(it.getItem().getClass().equals(objects.Light.class))
				return "L";
			if(it.getItem().getClass().equals(objects.Rope.class))
				return "R";
			if(it.getItem().getClass().equals(objects.Shovel.class))
				return "SH";
			if(it.getItem().getClass().equals(objects.Stencil.class))
				return "ST";
			if(it.getItem().getClass().equals(objects.Surprise.class))
				return "SP";
			if(it.getItem().getClass().equals(objects.Swimsuit.class))
				return "SW";
			return "";
		}
		catch(Exception e) {return "";}
	}
    /**
     * A jégtáblán lévő eskimokat rajzolja ki
     * @param n hány darab van egy adott jégtáblán
     * @return Eskimokat tartalmazó StackPane
     */
	public StackPane drawEskimo(int n)
	{
		StackPane es = new StackPane();
		double paneSize = charsize*1.5;
		es.setPrefSize(paneSize, paneSize);
        es.setMaxSize(paneSize, paneSize);
        es.setMinSize(paneSize, paneSize);
		double r = charsize;
		Circle e = new Circle();
		e.setRadius(r);
		e.setFill(Color.BLUE);
		Label txt = new Label(Integer.toString(n));
		txt.setStyle("-fx-text-fill: white;");
		es.getChildren().addAll(e,txt);
		return es;
	}
	/**
     * A jégtáblán lévő researchereket rajzolja ki
     * @param n hány darab van egy adott jégtáblán
     * @return Researchereket tartalmazó StackPane
     */
	public StackPane drawResearcher(int n)
	{
		StackPane res = new StackPane();
		double paneSize = charsize*1.5;
		res.setPrefSize(paneSize, paneSize);
        res.setMaxSize(paneSize, paneSize);
        res.setMinSize(paneSize, paneSize);
		double r = charsize;
		Circle re = new Circle();
		re.setRadius(r);
		re.setFill(Color.RED);
		Label txt = new Label(Integer.toString(n));
		txt.setStyle("-fx-text-fill: white;");
		res.getChildren().addAll(re,txt);
		return res;
	}
	/**
	 * Jegesmedvét megjelenítő függvény
	 * @return Maci köre
	 */
	public Circle drawBear()
	{
		double r = charsize;
		Circle b = new Circle();
		b.setRadius(r);
		b.setFill(Color.AQUA);
		return b;
	}
    /**
     * Menusort megjelenítő metódus
     * @return MenuBar típussal tér vissza, hozzáadjuk a fő képernyőhöz
     */
    public MenuBar Menus()
    {
    	MenuItem save = new Menu("Save");
    	MenuItem load = new Menu("Load");
    	load.setDisable(true);
    	MenuItem info = new Menu("Info");
    	MenuItem exit = new Menu("Exit");
    	Menu file = new Menu("File");
    	file.getItems().addAll(save,load,info,exit);
    	MenuBar menubar = new MenuBar();
    	
    	save.setOnAction(e ->{
    		gw.saveGame();
    		System.out.println("Exporting...");
    	});
    	load.setOnAction(e ->{
    		
    		System.out.println("Loading...");
    	});
    	info.setOnAction(e ->{
    		
    		System.out.println("Opening info...");
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.getDialogPane().setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("igloo.png"),50,50,true,false)));
    		alert.setTitle("Game information");
    		alert.setHeaderText("Antarctica");
    		String s = new String();
    		String txt = new String();
    		FileReader fr;
			try {
				fr = new FileReader("leiras.txt");
				BufferedReader br = new BufferedReader(fr);
				while((txt=br.readLine())!=null)
				{
					s += txt;
					s += System.lineSeparator();
				}
				br.close();
			} catch (IOException e1) {
				System.out.println("An error occurred.");
				e1.printStackTrace();
			}
    		alert.setContentText(s);
    		gw.getActualplayer().addItem(new Camp());
    		gw.getActualplayer().addItem(new Rope());
    		gw.getActualplayer().addItem(new Surprise());
    		alert.showAndWait();
    	});
    	exit.setOnAction(e ->{
    		
    		System.out.println("Exiting...");
    		System.exit(0);
    	});
    	menubar.getMenus().add(file);
    	return menubar;
    }
    /**
     * Kezelő gombok hozzáadása a fő képernyőhöz
     * @param gw GamseWorld
     * @return GridPane táblázatos elrendezést ad vissza
     */
    public GridPane addButtonsPanel(GameWorld gw) {
    	
    	GridPane buttonGrid = new GridPane();
    	
    	
    	for(int k = 0 ; k<6 ; k++)
    	{
    		buttons[k].setPrefSize(100, 50);
    	}
    	for(int k = 0 ; k<4 ; k++)
    	{
    		labels[k].setFont(new Font("Times New Roman",30));
    	}
    		buttonGrid.add(labels[0],0,0,4,1);
    		buttonGrid.add(labels[1],3,0,1,1);
    		buttonGrid.add(labels[2],4,0,1,1);
    		buttonGrid.add(labels[3],5,0,1,1);
    	
    	for (int i=0; i<6; i++) 
    	{
            buttonGrid.add(buttons[i],i,1,1,1);
        }
    	buttons[0].setOnAction(e -> {StepButton();});
    	buttons[1].setOnAction(e ->{SpecialButton();});
    	buttons[2].setOnAction(e ->{PickupButton();});
    	buttons[3].setOnAction(e -> {ManageButton();});
    	buttons[4].setOnAction(e ->{BagButton();});
    	buttons[5].setOnAction(e ->{BuildGunButton();});
    	
    	return buttonGrid;
    }
    /**
     * Lépés gomb megvalósítja a lépést, meghívja GameWorld play() metódusát
     */
    @SuppressWarnings("unchecked")
	public void StepButton()
    {
    	Dialog stepDialog = new Dialog();
    	stepDialog.setTitle("Step");
    	stepDialog.setHeaderText("Choose an icetable!");
    	Stage stage = (Stage) stepDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("step.png").toString()));
    	final ToggleGroup group = new ToggleGroup();
    	
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));
    	for(int i = 0;i<gw.getActualplayer().getMyTable().getNeighbours().size();i++)
    	{
    		RadioButton b = new RadioButton("IT " +gw.getTables().indexOf(gw.getActualplayer().getMyTable().getNeighbour(i)));
    		b.setToggleGroup(group);
    		grid.add(b, i, 0);
    	}
    	stepDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
    	stepDialog.getDialogPane().setContent(grid);
    	
    	stepDialog.showAndWait().ifPresent(response->{
            if(response == ButtonType.OK)
            {
            	int index = group.getToggles().indexOf(group.getSelectedToggle());
            	gw.setButtonCommand("step "+ index);
            	gw.setWaitForComm(true);
            }
        });    	
    }
    /**
     * Speciális képességet hívó gomb meghívja GameWorld play() metódusát
     */
    @SuppressWarnings("unchecked")
	public void SpecialButton()
    {
    	
    	if(gw.getActualplayer().getClass().equals(characters.Eskimo.class))
    	{
    		
    		if(gw.getActualplayer().getMyTable().isIglu()==0)
    		{
    			gw.setButtonCommand("build");
            	gw.setWaitForComm(true);
    		}
    		
    	}
    	else
    	{
    		Dialog checkDialog = new Dialog();
    		checkDialog.setTitle("Check strength");
    		checkDialog.setHeaderText("Choose an icetable to check its strength!");
        	final ToggleGroup group = new ToggleGroup();
        	
        	GridPane grid = new GridPane();
        	grid.setHgap(10);
        	grid.setVgap(10);
        	grid.setPadding(new Insets(20, 150, 10, 10));
        	for(int i = 0;i<gw.getActualplayer().getMyTable().getNeighbours().size();i++)
        	{
        		RadioButton b = new RadioButton("IT " +gw.getTables().indexOf(gw.getActualplayer().getMyTable().getNeighbour(i)));
        		b.setToggleGroup(group);
        		grid.add(b, i, 0);
        	}
        	checkDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        	checkDialog.getDialogPane().setContent(grid);
        	
        	checkDialog.showAndWait().ifPresent(response->{
                if(response == ButtonType.OK)
                {
                	int index = group.getToggles().indexOf(group.getSelectedToggle());
                	gw.setButtonCommand("check "+index);
                	gw.setWaitForComm(true);
                	gw.getActualplayer().getMyTable().getNeighbour(index).setChecked(true);
                }
            });   
    	}
    }
    /**
     * Tárgy felvételét megvalósító gomb
     */
    public void PickupButton()
    {
    	gw.setButtonCommand("pickup");
    	gw.setWaitForComm(true);
    }
    /**
     * Táska gombja, benne található az összes olyan gomb létrehozása, ami az aktuális játékosnál van éppen
     */
    @SuppressWarnings("unchecked")
	public void BagButton()
    {
    	
    	/*gw.getActualplayer().addItem(new Shovel());
    	gw.getActualplayer().addItem(new Food());
    	gw.getActualplayer().addItem(new Swimsuit());
    	gw.getActualplayer().addItem(new Gun());*/
    	ArrayList<Button> bagButtons = new ArrayList<Button>();
    	Dialog bagDialog = new Dialog();
		bagDialog.setTitle("Bag");
		bagDialog.setHeaderText("Choose an item from your bag!");
		Stage stage = (Stage) bagDialog.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("backpack.png").toString()));
    	final ToggleGroup group = new ToggleGroup();
    	
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));
    	if(gw.getActualplayer().getItems().size() != 0)
    	{
    		for(int i = 0;i<gw.getActualplayer().getItems().size();i++)
        	{
        		String bname = gw.getActualplayer().getItems().get(i).getClass().toString();
        		String[] bnamesplit = bname.split("\\.");
        		Button b = new Button(bnamesplit[1]);
        		grid.add(b, 0, i);
        		bagButtons.add(b);
        	}
        	for(int j = 0; j<bagButtons.size();j++)
        	{
        		int item;
        		if(bagButtons.get(j).getText().equals("Shovel"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("BreakableShovel"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Camp"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Food"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Gun"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Rope"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				
        				Dialog ropeDialog = new Dialog();
        		    	ropeDialog.setTitle("Rope");
        		    	ropeDialog.setHeaderText("Choose an icetable to rope!");
        		    	final ToggleGroup ropegroup = new ToggleGroup();
        		    	
        		    	GridPane ropegrid = new GridPane();
        		    	ropegrid.setHgap(10);
        		    	ropegrid.setVgap(10);
        		    	ropegrid.setPadding(new Insets(20, 150, 10, 10));
        		    	for(int i = 0;i<gw.getActualplayer().getMyTable().getNeighbours().size();i++)
        		    	{
        		    		RadioButton b = new RadioButton("IT " +gw.getTables().indexOf(gw.getActualplayer().getMyTable().getNeighbour(i)));
        		    		b.setToggleGroup(ropegroup);
        		    		ropegrid.add(b, i, 0);
        		    	}
        		    	ropeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        		    	ropeDialog.getDialogPane().setContent(ropegrid);
        		    	
        		    	ropeDialog.showAndWait().ifPresent(response->{
        		            if(response == ButtonType.OK)
        		            {
        		            	int index = ropegroup.getToggles().indexOf(ropegroup.getSelectedToggle());
        		            	gw.getActualplayer().setStepDirection(index);
        		            	gw.setButtonCommand("use "+ item);
        		            	gw.setWaitForComm(true);
        		            	usedsomething=true;
                            	bagDialog.close();
        		            }
        		        });
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Light"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Stencil"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Surprise"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	SurprisePopup();
                    	bagDialog.close();
            		});
    			}
        		else if(bagButtons.get(j).getText().equals("Swimsuit"))
    			{
        			item = j;
        			bagButtons.get(j).setOnAction(e ->{
        				gw.setButtonCommand("use "+ item);
                    	gw.setWaitForComm(true);
                    	usedsomething=true;
                    	bagDialog.close();
            		});
    			}
        	}
    	}
    	else
    	{
    		grid.add(new Label("You dont have any items!"), 0, 0);
    	}
    	
    	bagDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
    	bagDialog.getDialogPane().setContent(grid);
    	
    	bagDialog.showAndWait().ifPresent(e ->{
    		if(usedsomething == true)
    		{
    			bagDialog.close();
    			usedsomething=false;
    		}
    	});
    }
    /**
     * Jégtbála menedzselését megvalósító gomb
     * Hó lapátolás, jég, iglu, sátor törés
     */
    public void ManageButton() {
    	gw.setButtonCommand("manage");
    	gw.setWaitForComm(true);
    }
    /**
     * Játék végén megnyomandó gomb hívása, meghívja a GameWorld buildgun metódusát
     */
    public void BuildGunButton()
    {
    	gw.setButtonCommand("buildgun");
    	gw.setWaitForComm(true);
    }
    /**
     * Meglepetés item üzenetét jeleníti meg
     */
    public void SurprisePopup()
    {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("igloo.png"),50,50,true,false)));
		alert.setTitle("Surprise");
		alert.setHeaderText("You got a meglepetes!");
		alert.setContentText(surprisestate);
		alert.showAndWait();
    }
    /**
     * Ha hóviharba kerülünk, azt jelzi egy felugró üzenetben
     */
    public void SnowStormPopup()
    {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("igloo.png"),50,50,true,false)));
		alert.setTitle("SnowStorm");
		alert.setHeaderText("You got a SnowStorm!");
		//alert.setContentText();
		alert.showAndWait();
		snowstormstate = false;
    }
    /**
     * Játék deserializáló függvény
     */
    synchronized public void loadGame()
	{
		try {
            FileInputStream istream = new FileInputStream("gamestate.txt");
            ObjectInputStream p = new ObjectInputStream(istream);
            
            main.setGW((GameWorld)p.readObject());
            p.close();
            istream.close();
            
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    /**
     * Játék alapbeállításért felelős dialógus ablakok
     * Bekéri a játékosok számát, a játékosok nevét és elmenti ezeket
     */
    public void GameSettings()
    {
    	ArrayList<TextField> namestexts = new ArrayList<TextField>();
    	Dialog dialog = new Dialog();
    	dialog.setTitle("Game Setup");
    	dialog.setHeaderText("Please add game specs!");
    	ImageView iv = new ImageView(this.getClass().getResource("eszkimo.jpg").toString());
    	iv.setFitHeight(125);
    	iv.setFitWidth(100);
    	dialog.setGraphic(iv);
    	
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));
    	
    	TextField numplayer = new TextField();
    	ChoiceBox<String> cb= new ChoiceBox<>();
    	cb.getItems().addAll("Eskimo", "Researcher");
    	if(start)
    	{
    		Button importb = new Button("Import");
    		//grid.add(importb, 0, 2);
    		start=false;
    		importb.setOnAction(e ->{
    			loadGame();
    			dialog.close();
    		});
    	}
    	if(charchoice == 1)
    		{
    			cb.setDisable(true);
    			if(iseskimo) cb.getSelectionModel().select(1);
    			else cb.getSelectionModel().select(0);    			
    		}
    	else
    	{
    		cb.getSelectionModel().select(0);
    	}
    	grid.add(new Label("Character type:"), 0, 0);
    	grid.add(cb, 1, 0);
    	grid.add(new Label("Number of players:"), 0, 1);
    	grid.add(numplayer, 1, 1);
    	
    	if(toolow)
    	{
    		if(iseskimo)
    		{
    			grid.add(new Label("Not enough players to start, please add minimum 3 players. "+eskimonum+" Eskimo(s) already and "+researchernum+" Researcher(s) already"), 0, 2);
    		}
    		else
    		{
    			grid.add(new Label("Not enough players to start, please add minimum 3 players. "+researchernum+" Researchers already."), 0, 2);
    		}        	
    	}
    	
    	dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    	dialog.getDialogPane().setContent(grid);
    	Optional<ButtonType> result = dialog.showAndWait();
    	if(result.get() == ButtonType.OK && result.isPresent() && numplayer.getText().isEmpty() == false)
    			{
    				if(cb.getValue().equals("Eskimo")) 
    				{
    					eskimonum += Integer.parseInt(numplayer.getText());
    					System.out.println(eskimonum);
    					iseskimo = true;
    				}
    				if (cb.getValue().equals("Researcher")) 
    				{
    					iseskimo = false;
    					researchernum += Integer.parseInt(numplayer.getText());
    					System.out.println(researchernum);
    				}
    				System.out.println("N of players: "+(researchernum+eskimonum));
    				
    				Dialog dialog2 = new Dialog();
    		    	dialog2.setTitle("Player Settings");
    		    	dialog2.setHeaderText("Please add player names!");
    		    	ImageView iv2 = new ImageView(this.getClass().getResource("sarkkutatok.jpg").toString());
    		    	iv2.setFitHeight(125);
    		    	iv2.setFitWidth(150);
    		    	dialog2.setGraphic(iv2);
    		    	GridPane grid2 = new GridPane();
    		    	grid2.setHgap(10);
    		    	grid2.setVgap(10);
    		    	grid2.setPadding(new Insets(20, 150, 10, 10));
    		    	
    		    	for(int i = 0; i<Integer.parseInt(numplayer.getText());i++)
    		    	{
    		    		TextField player = new TextField();
    		    		namestexts.add(player);
    		    		grid2.add(new Label("Player "+ (i+1)),0,i);
    		    		grid2.add(player, 1, i);
    		    	}
    		    	
    		    	
    		    	dialog2.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    		    	dialog2.getDialogPane().setContent(grid2);
    		    	dialog2.showAndWait();
    		    	if(iseskimo)
    		    	{
    		    		for(int i = 0;i<namestexts.size();i++)
    		    		{
    		    			eskimoname.add(namestexts.get(i).getText());    		    			
    		    		}
    		    		System.out.println("Eskimos added...");
    		    	}
    		    	if(!iseskimo)
    		    	{
    		    		for(int i = 0;i<namestexts.size();i++)
    		    		{
    		    			researchername.add(namestexts.get(i).getText());
    		    		}
    		    		System.out.println("Researchers added...");
    		    	}
    			}
    	
    	charchoice = 1;
    	
    }
    /**
     * Karakterek nevét bekérő dialógusablak
     * @param n hány játékosunk, van ez alapján hoz létre megfelelő számű szövegdobozt
     */
    public void NameSettings(int n)
    {
    	ArrayList<String> namelist = new ArrayList<String>();
    	for(int i = 0; i<n;i++)
    	{
    		TextInputDialog dialog = new TextInputDialog("player" +i+1);
    		dialog.setTitle("Player" + i++ + "Name");
    		dialog.setHeaderText("Enter player"+ i++ +" name!");
    		dialog.setContentText("Name:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			if(iseskimo)
        		{
        			eskimoname.add(dialog.getEditor().getText());
        		}
        		if(!iseskimo)
        		{
        			researchername.add(dialog.getEditor().getText());
        		}
    		}
    	}
    }
    /**
     * Surprise state getter
     * @return surperisestate - a Surprise osztályban generált random szöveget kéri le
     */
	public String getSurprisestate() {
		return surprisestate;
	}
	/**
	 * Surprise state setter
	 * @param surprisestate - a Surprise osztályban generált random szöveget adja át
	 */
	public void setSurprisestate(String surprisestate) {
		this.surprisestate = surprisestate;
	}
	/**
	 * SnowStorm state getter
	 * @return snowstormstate visszaadja a bool értéket a hóvihar függvényében
	 */
	public boolean isSnowstormstate() {
		return snowstormstate;
	}
	/**
	 * SnowStorm state setter
	 * @param snowstormstate true ha hóvihar van
	 */
	synchronized public void setSnowstormstate(boolean snowstormstate) {
		this.snowstormstate = snowstormstate;
	}

}