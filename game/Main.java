package game;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import gui.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
public class Main extends Application {
	Stage stage;
	private View view = new View();
	private GameWorld gameworld;
	private boolean added = false;
	private boolean pressed = false;
	protected static Button buttons[];
	/**
	 * Javafx start függvényt hívja a launch(args) paranccsal
	 * @param args
	 */
    public static void main(String[] args) {
    	
    	launch(args);
    }
    /**
     * Játék indítása, amit a JavaFX launch függvénye hív meg.
	 * Beállításra kerül a stage változó a kapott paraméterre.
	 * Meghívásra kerül a grafikus felület játékinicializáló függvénye, ahol bekérésre kerül a játékosok fajtája és neve
	 * Létrehozunk egy új GameWorld objektumot a view eszkimó és sarkkutató számai alapján, ami után beállítjuk ezeknek a neveit.
	 * Beállítjuk a view gameworld tárolóját az elõbb létrehozott gameworld példányra
	 * Megjelenítjük az elsõdleges stage-et
	 * Meghívjuk az elõbb létrehozott gameworld játék inicializáló függvényét
	 * majd elindítunk egy új szálat, hogy a grafikus felület és a program ne akadjon össze, ne kerüljünk deadlock-ba
     */
    @Override
    public void start(Stage primaryStage) 
    {
    	stage=primaryStage;
    	view.initGui(primaryStage,this);
    	gameworld = new GameWorld(view.getEskimoNum(), view.getResNum(), view);
    	gameworld.setNames(view.getEskimoNames(), view.getResearcherNames());
    	view.gameGui(gameworld);
    	primaryStage.show();
        this.gameworld.initGame();
        Thread thread = new Thread(gameworld);
        thread.start();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
               Platform.exit();
               System.exit(0);
            }
         });
    }
	/**
	 * A stage változó getter függvénye
	 * @return stage változó
	 */
    public Stage getStage()
    {
    	return stage;
    }
	/**
	 * A gameworld változó setter függvénye
	 * @param gw - az új érték amire beállítjuk
	 */
    public void setGW(GameWorld gw)
    {
    	gameworld = gw;
    	added = true;
    	System.out.println(added);
    }
	/**
	 * Az added változó getter függvénye
	 * @return added változó értéke
	 */
    public boolean getAdded()
    {
    	return added;
    }
    
}