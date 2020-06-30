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
	 * Javafx start f�ggv�nyt h�vja a launch(args) paranccsal
	 * @param args
	 */
    public static void main(String[] args) {
    	
    	launch(args);
    }
    /**
     * J�t�k ind�t�sa, amit a JavaFX launch f�ggv�nye h�v meg.
	 * Be�ll�t�sra ker�l a stage v�ltoz� a kapott param�terre.
	 * Megh�v�sra ker�l a grafikus fel�let j�t�kinicializ�l� f�ggv�nye, ahol bek�r�sre ker�l a j�t�kosok fajt�ja �s neve
	 * L�trehozunk egy �j GameWorld objektumot a view eszkim� �s sarkkutat� sz�mai alapj�n, ami ut�n be�ll�tjuk ezeknek a neveit.
	 * Be�ll�tjuk a view gameworld t�rol�j�t az el�bb l�trehozott gameworld p�ld�nyra
	 * Megjelen�tj�k az els�dleges stage-et
	 * Megh�vjuk az el�bb l�trehozott gameworld j�t�k inicializ�l� f�ggv�ny�t
	 * majd elind�tunk egy �j sz�lat, hogy a grafikus fel�let �s a program ne akadjon �ssze, ne ker�lj�nk deadlock-ba
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
	 * A stage v�ltoz� getter f�ggv�nye
	 * @return stage v�ltoz�
	 */
    public Stage getStage()
    {
    	return stage;
    }
	/**
	 * A gameworld v�ltoz� setter f�ggv�nye
	 * @param gw - az �j �rt�k amire be�ll�tjuk
	 */
    public void setGW(GameWorld gw)
    {
    	gameworld = gw;
    	added = true;
    	System.out.println(added);
    }
	/**
	 * Az added v�ltoz� getter f�ggv�nye
	 * @return added v�ltoz� �rt�ke
	 */
    public boolean getAdded()
    {
    	return added;
    }
    
}