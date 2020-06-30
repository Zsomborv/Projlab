package game;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

import characters.Eskimo;
import characters.PolarBear;
import characters.Researcher;
import characters.Stepable;
import gui.View;
import javafx.application.Platform;
import objects.*;
/**
 * A jatekot megvalosito osztaly, mondhatni jatekmezo. Letrehozza es birtokolja a mezoket, a jatekosokat, 
 * kiosztja a targyakat, felugyeli a jatekot.
 * @author BCSV
 *
 */
public class GameWorld implements Serializable, Runnable {
	
	/**
	 * A GameWorld nézet változója
	 */
	private transient View view;
    /**
	 * Parancsot tároló String
	 */
	private String buttonCommand;
	/**
	 * Várakozás állapotának tárolása
	 */
	private boolean waitForComm=false;
	
	/**
	 * A jatekmezoket/jegtablakat tartalmazo lista, minden cselekmeny rajtuk tortenik
	 */
	private ArrayList<IceTable> tables;
	/**
	 * Csak az eszkimokat tartalmazo lista. 
	 * A specialis kepesseg miatt van ra szukseg
	 */
	private ArrayList<Eskimo> eskimos;
	/**
	 * Csak a kutatokat tartalmazo lista. 
	 * A specialis kepesseg miatt van ra szukseg
	 */
	private ArrayList<Researcher> researchers;
	/**
	 * Csak Jegesmedveket nyilvantarto lista. 
	 */
	private ArrayList<PolarBear> polarbears;
	/**
	 * Minden jatszhato karakter listaja. Az inicializalas miatt eloszor az eszkimok, majd a kutatok kerulnek bele. Minden altalanos kepesseg, 
	 * igy a lepes es az aktualis jatekos kivalasztasa is ez alapjan van
	 */
	private ArrayList<characters.Character> players;
	/**
	 * Az itemeket tartalmazo lista. A random szetszoras miatt van ra szukseg, mindenbol annyit tarolunk amennyi letrejohet
	 */
	private ArrayList<Item> items;
	/**
	 * Az osszes jatekos szama
	 */
	private int playerNum;
	/**
	 * Vizben levo jatekos jelzese, kezdetben false, senki sincs vizben
	 */
	private boolean inWater = false;
	/**
	 * A vizbeeses ota eltelt ido
	 */
	private int watercounter = -1;
	/**
	 * szomszedossagi matrix, ez alapjan vannak kiosztva a szomszedok egy algoritmus alapjan
	 */
	private int[][] neighbours;
	/**
	 * A jelenleg jatszo jatekos. 
	 * A jatekoslista soron kovetkezo eleme
	 */
	private characters.Character actualplayer;
	/**
	 * A jatek menetet figyelo valtozo
	 * Erteke addig true, amig vege nincs a jateknak
	 */
	private boolean ongoing = true;
	/**
	 * A jatek vegeredmenyet jelzo valtozo. Erteke true, ha a jatekot megnyertek
	 */
	private boolean win;
	/**
	 * Az aktualis jatekos indexe a jatekosok listajaban
	 */
	private int actNum = 0;
	
	/**
	 * Konstruktor. Beállítja a nézetet. 
	 * Letrehozza az osszes listat, a jatekosokat
	 * Parameterei a karakterek szama, amik osszege nem lehet kevesebb 3-nal
	 * A jatekoslistaba eloszor az eszkimok, majd a kutatok kerulnek, 
	 * igy sok jatekos utan is eloszor mindig az eszkimok fognak jonni.
	 * @param view - a nézet, amiben meg fog jelenni a játéktér
	 * @param eskimoPlayerNum - eszkimok szama
	 * @param resPlayerNum - kutatok szama
	 */
	public GameWorld(int eskimoPlayerNum, int resPlayerNum, View view) {
		this.view=view;
		playerNum = resPlayerNum + eskimoPlayerNum;
		tables = new ArrayList<IceTable>();
		players = new ArrayList<characters.Character>();
		eskimos = new ArrayList<Eskimo>();
		researchers = new ArrayList<Researcher>();
		items = new ArrayList<Item>();
		polarbears = new ArrayList<PolarBear>();
		for(int i=0; i< eskimoPlayerNum; i++) {
			Eskimo e = new Eskimo();
			eskimos.add(e);
			players.add(e);
		}
		for(int i=0; i< resPlayerNum; i++) {
			Researcher res = new Researcher();
			researchers.add(res);
			players.add(res);
		}

	}
	/**
	 * A karakterek nevének beállító függvénye. 
	 * @return eskimonames - Eszkimók nevét tartalmazó lista
	 * @return researchernames - Sarkkutatók nevét tartalmazó lista
	 */
	public void setNames(ArrayList<String> eskimonames,ArrayList<String> researchernames)
	{
		for(int i=0;i<eskimos.size();i++)
		{
			eskimos.get(i).setCharName(eskimonames.get(i));
		}
		for(int i=0;i<researchers.size();i++)
		{
			researchers.get(i).setCharName(researchernames.get(i));
		}
	}
	/**
	 * A view változó setter fuggvenye. 
	 * @param view - az a view változó amivel egyenlővé tesszük
	 */
	public void setView(View view)
	{
		this.view = view;
	}
	/**
	 * A karakterlista getter fuggvenye. 
	 * @return - Visszaadja a karakterlistat
	 */
	synchronized public ArrayList<characters.Character> getPlayers() {
		return players;
	}
	/**
	 * Az aktualis jatekos beallitasaert felelos fuggveny. A parameterul kapott karakter lesz az aktualis jatekos,
	 * utana mindenfelekeppen az elozo aktualis jatekos utan kovetkezo karakter fog jonni, hacsak nem lett a szama is beallitva.
	 * Utobbi esetben csak akkor szabad meghivni a fuggvenyt, ha tudjuk, hogy a parameterul kapott karakter szerepel a jatekoslistaban
	 * @param actualplayer - a parameterul kapott karakter
	 */
	synchronized public void setActualplayer(characters.Character actualplayer) {
		this.actualplayer = actualplayer;
	}
	/**
	 * Az aktualis karakter gettere. Visszaadja az aktualis jatekost.
	 * @return - az aktualist karaktert adja vissza
	 */
	synchronized public characters.Character getActualplayer() {
		return actualplayer;
	}
	
	/**
	 * Az aktualis jatek fuggvenye. Csak akkor hivando, ha minden inicializalva lett.
	 * Az elso jatekos a jatekoslista 0.indexu eleme. onnan megy sorban. 
	 * Amig az ongoing true, addig megy a jatek. Itt feltetelezzuk, hogy kutato is lehet elso jatekos.
	 * A belso ciklus egy jatekosra vonatkozik, addig megy, amig el nem fogy az energiaja. 
	 * 
	 * A grafikus felulettel ellatott programban a gomboktol kapott parancsokat futtatja.
	 * 6 utasitas kozul valaszthatunk. Mindegyik egy tenyleges mukodest valosit meg,
	 * mint a step, targy felvetele, use, a specialis kepessegek, a fegyverepites vagy holapatolas
	 * 
	 * - A hovihar a palya harmadan hatasos, random sorsolva a mezoket. Minden karakter utan a kovetkezo jatekost
	 * valaszto fuggveny hivodik meg
	 * 
	 * - A step utasitassal egyutt egy jegtabla indexet kell megadni
	 * - A use utan meg kell adni a hasznalando targy indexet
	 * - A buildgun meghivasa soran befejezhetjuk a jatekot, ha minden feltetel megvan hozza (Minden alkatresz es mindenki egy mezon all)
	 * - A build parancs csak akkor lep eletbe, ha az aktualis jatekos eszkimo. Ezzel megprobal iglut epiteni a mezore, amin all
	 * - A check parancs csak akkor fut le, ha az aktualis jatekos kutato. Meg kell adni neki egy jegtabla indexet, csakugy, mint a step-nel
	 * - A manage utasitassal takarithatunk havat, vagy torhetunk jeget ha mar nincs a mezonkon ho. Akkor is lefut, ha mar nincs ho es a jeg is be van torve
	 * - A pickup utesitassal megprobaljuk felvenni a jegben talalhato Itemet. Akadalyt jelent, ha a jeg be van fagyva, van rajta Iglu vagy nincs benne item
	 */
	public void play() {
		actualplayer = players.get(0);
		while(this.getOngoing()) {
			
			int idx = 0;
			
			if(actualplayer.getClass().equals(characters.Eskimo.class)) {
				actualplayer = (Eskimo)actualplayer;
				for(Eskimo e: eskimos) {
					if(e.equals(actualplayer)) {
						idx = eskimos.indexOf(actualplayer);
					}
				}
			} else if(actualplayer.getClass().equals(characters.Researcher.class)) {
				actualplayer = (Researcher)actualplayer;
				for(Researcher r: researchers) {
					if(r.equals(actualplayer)) {
						idx = researchers.indexOf(actualplayer);
					}
				}
			}
			while(actualplayer.getEnergy() >0) {
							
				Platform.runLater(()->view.update(view.getMain().getStage()));
				String command = command();
				String[] commands = command.split(" ");
				
				if(commands[0].equals("step")) {
					actualplayer.step(Integer.parseInt(commands[1]));
					
				}
				if(commands[0].equals("use")) {
			
					actualplayer.useItem(Integer.parseInt(commands[1]));
				}
				if(commands[0].equals("buildgun")) {
					actualplayer.buildGun();
					
				}
				if(commands[0].equals("build") && actualplayer.getClass().equals(characters.Eskimo.class)) {
					eskimos.get(idx).buildIglu();
					
					
				}
				if(commands[0].equals("check")&& actualplayer.getClass().equals(characters.Researcher.class)) {
					int s = researchers.get(idx).checkStrength(Integer.parseInt(commands[1]));
					
				}
				if(commands[0].equals("snowstorm")) {
					Random random = new Random();
					ArrayList<Integer> stormtables = new ArrayList<Integer>();

					for(int i=0; i<tables.size()/3; i++) {
						int randomNumber = random.nextInt(tables.size());
						
						boolean bad=true;
						while(bad) {
							int counter = 0;
							for(int st : stormtables) {
								if(st == randomNumber) {
									counter++;
								}
							}
							if(counter == 0) {
								bad = false;
							} else
								randomNumber = random.nextInt(tables.size());
						}
						stormtables.add(randomNumber);
						}
					this.snowStorm(stormtables);
				}
				if(commands[0].equals("manage")) {
					actualplayer.manageIceTable();
				}
				if(commands[0].equals("end")) {
					endGame(false);
					break;	
				}
				if(commands[0].equals("pickup")) {
					actualplayer.pickupItem();
				}
			}
			nextPlayer();
		}
		System.out.println("Vege");
	}
	/**
	 * Az utasitasokat beolvaso fuggveny
	 * A megnyomott billentyu parancsat olvassa be.
	 * @return - visszaadja az inputot String formajaban
	 */
	public String command() {
		
		if(new Random().nextInt(50) % 11 ==0)
		{
			return "snowstorm";
		}
		String command = null;
		while(!this.isWaitForComm()) {
			command=this.getButtonCommand();
		}
		waitForComm=false;
		return command;
	}
	
	
	
	/**
	 * A kovetkezo jatekost kezelo fuggveny. 
	 * Alapesetben csak megnoveli az indexszamot, es kijeloli a kovetkezo jatekost
	 * Ha a jatekos indexe elerte a maximumot, az indexet nullazzuk, ez azt jelenti, hogy a jatekosok korbeertek,
	 * ilyenkor meg kell hivni a nextRound fuggvenyt, ami a koroket kezeli
	 * 
	 * Ha az inWater true, vagyis van valaki van a vizben, a korszamlalot csokkentjuk
	 * Ha eleri a 0-t az azt jelenti, hogy egy kor alatt senki sem hozta ki, vagyis a jatek veget er vereseggel
	 * 
	 */
	public void nextPlayer() {
		actNum++;
		if(inWater) {
			watercounter--;
			if(watercounter==0) {
				this.endGame(false);
			}
			
		}
		if(actNum == playerNum) {
			actNum = 0;
			nextRound();
		}
		actualplayer = players.get(actNum);
		if(actualplayer.getMyTable().getStrength() == 0) {
			actualplayer.setEnergy(0);
		}
	}
	/**
	 * Az aktualis jatekos indexenek Getter
	 * @return - visszaadja az aktulis jatekos indexet
	 */
	public int getactNum() {
		return actNum;
	}
	/**
	 * Beallitja az aktualis jatekos indexet. Az alapjatekban nem hasznaljuk
	 * @param act - a paramterul kapott index
	 */
	public void setactNum(int act) {
		actNum=act;
	}
	/**
	 * A jegmezoket tartalmazo lista Gettere
	 * @return - visszaadja a jegmezoket tartalmazo listat
	 */
	synchronized public ArrayList<IceTable> getTables() {
		return tables;
	}
	/**
	 * Setter fuggveny a jegtablakat tartalmazo listara
	 * Kulsoleg letrehozott jatek eseten van ra szukseg
	 * @param its - a parameterul kapott lista amit beallitunk sajatunknak
	 */
	synchronized public void setTables(ArrayList<IceTable> its) {
		tables = its;
	}
	/**
	 * Az paramterul kapott index alapjan visszaad egy mezot a jegmezoket tartalmazo listabol
	 * @param idx - paramterul kapott index
	 * @return - visszaadja az index helyen levo jegmezot
	 */
	synchronized public IceTable getTable(int idx) {
		return tables.get(idx);
	}
	/**
	 * Hozzaadja a parameterul kapott jegmezot a listahoz
	 * @param it - paramterul kapott jegmezo
	 */
	synchronized public void addTable(IceTable it) {
		tables.add(it);
	}
	/**
	 * Hovihar altal hivott fuggveny. Ha van a mezon iglu, elpusztitja, de nem sebzi meg a benne levoket.
	 * Ha nincs, minden jegmezon allo karaktert megsebez se minden jegmezon allo jegesemedvet leptet egyet
	 * @param it - paramterul kapott jegmezo
	 */
	public void damage(IceTable it) {
		if(it.isIglu()>0)
			it.getIglu().destroy(4);	
		else if(it.getCharacters().size()>0) {
			for(int i=0; i< it.getCharacters().size(); i++) {
				it.getCharacters().get(i).storm();
			}
		}					
	}
	
	/**
	 * Az uj kort kezelo fuggveny. 
	 * Minden jegmezore meghivja az altaluk definialt nextRoundot
	 */
	public void nextRound() {
		for(int i=0; i< tables.size(); i++) {
			tables.get(i).nextRound();
		}
		for(int i=0; i< tables.size(); i++) {
			try
			{
				tables.get(i).getPolarbear().setStepped(false);
			}
			catch(Exception e) {}
		}
		
		
	}
	/**
	 * A jatek veget kezelo fuggveny. 
	 * paramterul kapja hogy a jatekot megnyertek-e vagy sem.
	 * Barmikor hivodik, az aktualis karakter energiajat 0-ra allitja hogy megszakitsa a jatekciklust,
	 * majd az ongoingot false-ra allitja hogy a fociklus is megszakadjon
	 * @param state true - gyozelem, false - vereseg
	 */
	public void endGame(boolean state) {
		
		if(state) {
			win = true;
		}else if(!state) {
			win = false;
		}
		this.actualplayer.setEnergy(0);
		view.setGameover(true);
		//ongoing = false;
		//Platform.runLater(()->view.update(view.getMain().getStage()));
		
	}
	synchronized public void setOngoing(boolean og) {
		ongoing = og;
	}
	/**
	 * A jatek mentete jelzo valtozo Gettere
	 * @return - visszaadja, hogy megy e meg a jatek
	 */
	synchronized public boolean getOngoing() {
		return ongoing;
	}
	/**
	 * A jatekot inicializalo fuggveny
	 * Letrehozza a szomszedossagi matrixot a jatekosok szamai altal meghatarozott szorzoval
	 * Algoritmus: 
	 * Sorsolunk egy random szomszedszamot ket elore meghatarozott ertek alapjan
	 * Ha az oszlopan meg nincs meg egy minimum sorsolunk neki egy random szomszedot, ami nem lehet se onmaga, sem egy mar korabban sorsolt index.
	 * A kisorsolt sor-oszlop parost tukrozve az oszlop-sor index alapjan is jelezzuk a matrixban, hogy szomszedosak, hiszen ha 
	 * az egyiknek szomszedja a masik, akkor a masiknak is szomszedja az egyik.
	 * 
	 * Ezutan letrehozzuk a tablakat, majd a matrix alapjan hozzaadjuk a szomszedokat a tablakhoz.
	 * A szomszedossagi matrixban a foatlotol lefele minden erteknel hozzaadjuk a sorhoz tartozo indexu tablat a tablakat
	 * tartalmazo listabol szomszedkent, majd visszafele is.
	 * Mindegyiken beallitjuk a jeget fagyottra es adunk nekik random havat 0 es 2 kozott, a strapabirtasukat pedig 0 es maxjatekos szam kozott
	 * Keresunk egy stabil mezot, es minden jatekos rateszunk arra a mezore.
	 * Meghatarozzuk a jatekosok szama alapjan a jegesmedvek szamat es keresunk egy mezot ahol nem allnak kezdetben karakterek
	 * Ezutan meghivjuk az itemeket szetszoro fuggvenyt.
	 *  
	 * 
	 */
	/*public void initGame() {
		neighbours = new int[playerNum*5][playerNum*5];
		int tableNum= playerNum * 5;
		int min=3, max=6; //egyelore
		int counter;
		Random rand = new Random(); 
		for(int i=0; i< tableNum; i++) {
			
			int neighbourNum = rand.nextInt(max-min+1)+min;
			counter=0;
			for(int j=0; j< tableNum; j++) {
				if(this.neighbours[i][j] == 1) {
					counter++;
				}
			}
			while(counter<neighbourNum) {
				int nbIdx = rand.nextInt(tableNum);
				if(this.neighbours[i][nbIdx] == 0 && nbIdx !=i) {
					this.neighbours[i][nbIdx] = 1;
					this.neighbours[nbIdx][i] = 1;
				}
				counter++;
			}
		}

		//TABLAK LETREHOZASA
		for(int i=0; i< tableNum; i++) {
			tables.add(new IceTable(this));
			tables.get(i).setStrength(rand.nextInt(playerNum+1));
		}
		for(int i=0; i< tableNum; i++) {	
			for(int j=i+1; j< tableNum; j++) {
				if(this.neighbours[i][j]==1) {
					tables.get(i).addNeighbour(tables.get(j));
				}
			}
		}
		
		for(int i=0; i< tables.size(); i++) {
			tables.get(i).setSnow(rand.nextInt(3));
			tables.get(i).setIce(true);
			
		}
		int idx = rand.nextInt(tables.size());
		while(tables.get(idx).getStrength()<playerNum) {
			idx = rand.nextInt(tables.size());
		}
		for(Eskimo e: eskimos) {
			tables.get(idx).addPerson(e);
			e.setMyTable(tables.get(idx));
		}
		for(Researcher r: researchers) {
			tables.get(idx).addPerson(r);
			r.setMyTable(tables.get(idx));
		}
		for(int i=0; i< (int)2*players.size()/3; i++) {
			PolarBear pb = new PolarBear();
			idx = rand.nextInt(tables.size());
			while(tables.get(idx).getCharacters().size()>0) {
				idx = rand.nextInt(tables.size());
			}
			this.polarbears.add(pb);
			tables.get(idx).addPerson(pb);
			pb.setMyTable(tables.get(idx));
		}
		itemInit(min, max);
	}*/
	/**
	 * A szomszédsági mátrix lekérdező függvénye.
	 * @return - mátrixot adja vissza
	 * 
	 */
	public int[][] getNeighboursMatrix()
	{
		return neighbours;
	}
	/**
	 * A grafikus felülettel ellátott jatekot inicializalo fuggveny
	 * Letrehozza a szomszedossagi matrixot a megadott ertekkel
	 * Algoritmus: 
	 * Sorsolunk egy random egesz szamot 0 es 1 kozott, aminek a segitsegevel kivalasztjuk, hogy melyik palyat toltjuk be.
	 * Ezutan megnyitjuk a kisorsolt palyat es betoltjuk a megadott fajlbol
	 * 
	 * Ezutan letrehozzuk a tablakat, majd a matrix alapjan hozzaadjuk a szomszedokat a tablakhoz.
	 * A szomszedossagi matrixban a foatlotol lefele minden erteknel hozzaadjuk a sorhoz tartozo indexu tablat a tablakat
	 * tartalmazo listabol szomszedkent, majd visszafele is.
	 * Mindegyiken beallitjuk a jeget fagyottra es adunk nekik random havat 0 es 2 kozott, a strapabirtasukat pedig 0 es maxjatekos szam kozott
	 * Keresunk egy stabil mezot, es minden jatekos rateszunk arra a mezore.
	 * Meghatarozzuk a jatekosok szama alapjan a jegesmedvek szamat es keresunk egy mezot ahol nem allnak kezdetben karakterek
	 * Ezutan meghivjuk az itemeket szetszoro fuggvenyt.
	 */
	public void initGame() {
		neighbours = new int[22][22];
		String[] datasplit;
		int min=3, max=6;
		int tableNum= 22;
		Random rand = new Random(); 
		try {
			String filename = new String();
			int r = rand.nextInt(2);
			if(r==0)
			{
				filename = "matrix01";
			}
			if(r==1)
			{
				filename = "matrix02";
			}
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String data = br.readLine();
		      int i=0;
		      while (data != null) {
		    	  
		        
		        datasplit = data.split("\\t");
		        for(int j = 0; j<datasplit.length;j++)
		        {
		        	neighbours[i][j] = Integer.parseInt(datasplit[j]);
		        }
		        data = br.readLine();
		        i++;
		      }
		      br.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
			catch(IOException io)
		{
				 System.out.println("An error occurred.");
			      io.printStackTrace();
		}
		
		//TABLAK LETREHOZASA
		for(int i=0; i< tableNum; i++) {
			tables.add(new IceTable(this));
			tables.get(i).setStrength(rand.nextInt(playerNum+1));
		}
		for(int i=0; i< tableNum; i++) {	
			for(int j=i+1; j< tableNum; j++) {
				if(this.neighbours[i][j]==1) {
					tables.get(i).addNeighbour(tables.get(j));
				}
			}
		}
		
		for(int i=0; i< tables.size(); i++) {
			tables.get(i).setSnow(rand.nextInt(3));
			tables.get(i).setIce(true);
			
		}
		int idx = rand.nextInt(tables.size());
		while(tables.get(idx).getStrength()<playerNum) {
			idx = rand.nextInt(tables.size());
		}
		for(Eskimo e: eskimos) {
			tables.get(idx).addPerson(e);
			e.setMyTable(tables.get(idx));
		}
		for(Researcher r: researchers) {
			tables.get(idx).addPerson(r);
			r.setMyTable(tables.get(idx));
		}
		for(int i=0; i< (int)2*players.size()/3; i++) {
			PolarBear pb = new PolarBear();
			idx = rand.nextInt(tables.size());
			while(tables.get(idx).getCharacters().size()>0) {
				idx = rand.nextInt(tables.size());
			}
			this.polarbears.add(pb);
			tables.get(idx).addPerson(pb);
			pb.setMyTable(tables.get(idx));
		}
		itemInit(min, max);
	}
	
	/**
	 * Az itemeket letrehozo es szetszoro fuggveny.
	 * Minden itemet egy, a fejlesztok altal meghatarozott szam alapjan letrehoz, majd beteszi az itemeket tartalmazo listaba.
	 * Valasztunk egy random mezot, amin nincs meg item es nem lyuk, es hozzaadjuk a listaban a sorban kovetkezot. 
	 * @param min
	 * @param max
	 */
	public void itemInit(int min, int max) {
		//FOOD
		for(int i = 0; i<playerNum; i++) { //3
			items.add(new Food());
		}
		//ROPE
		items.add(new Rope());
		//SWIMSUIT
		items.add(new Swimsuit());	
		//CAMP
		for(int i = 0; i<playerNum/2; i++) { //2
			items.add(new Camp());
		}
		//SURPRISE
		for(int i = 0; i<playerNum/2; i++) { //2
			items.add(new Surprise());
		}
		//SHOVEL
		for(int i = 0; i<playerNum/2; i++) { //2
			items.add(new Shovel());
		}
		//BSHOVEL
		for(int i = 0; i<playerNum/2; i++) { //2
			items.add(new BreakableShovel());
		}
		//GUN
		items.add(new Gun());
		items.add(new Stencil());
		items.add(new Light());
		
		Random rand = new Random(); 
		for(int i = 0; i<items.size(); i++) {
			int randNum = rand.nextInt(tables.size());
			//NEM BEAKADASBIZTOS
			while(tables.get(randNum).getItems().size() != 0 && tables.get(randNum).getStrength() != 0) {
				randNum = rand.nextInt(tables.size());
			}	
			tables.get(randNum).addItem(items.get(i));
		}
	}
	/**
	 * A nézet változó getter függvénye.
	 * @return - visszatér az aktuális nézettel.
	 */
	public View getView()
	{
		return view;
	}
	
	/**
	 * A jatek eredmenyet tarolo valtozo Gettere. Visszaadja hogy a jatek win vagy lose lett-e
	 * @return - visszaadja a jatek vegeredmenyet
	 */
	synchronized public boolean getWin() {
		return win;
	}
	/**
	 * Lehetoseg van a jatek mentesere
	 * Minden osztaly szerializalhato, egy gamestate.txt-be szerializaljuk ki a gameworldot, ami tartalmaz mindent
	 */
	synchronized public void saveGame() {
		try {
            FileOutputStream ostream = new FileOutputStream("gamestate.txt");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            
            p.writeObject(this);
            p.flush();
            ostream.close();
            
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
		
	/**
	 * A hovihart kelto fuggveny
	 * Elore kisorsolt jegmezok mindegyikre meghivja a damage-t, 
	 * befagyasztja oket es tesz rajuk havat 0 es 2 ertek kozott 
	 * @param stormtables - a hovihar alatal sujtott jegmezok
	 */
	public void snowStorm(ArrayList<Integer> stormtables) {
		view.setSnowstormstate(true);
		Random random = new Random();
		for(int i = 0; i<stormtables.size(); i++) {
			tables.get(stormtables.get(i)).setIce(true);
			tables.get(stormtables.get(i)).setSnow(tables.get(stormtables.get(i)).getSnow()+random.nextInt(3));
			damage(tables.get(stormtables.get(i)));
		}
	}
	
	/**
	 * A max jatekosok szamara Getter fuggveny
	 * @return - Visszaadja a maximalis jatekosszamot
	 */
	synchronized public int getMaxPlayer() {
		return playerNum;
	}
	/**
	 * A vizeesestol szamitott korszamlalo Gettere
	 * @return - visszaadja, hogy meg hany jatekos van a vizbeesett jatekosig
	 */
	synchronized public int getWatercounter() {
		return watercounter;
	}
	/**
	 * Ha a kapott paramtere igaz, azt azt jelenti hogy valaki a vizbe esett. ha mas meg nincs a vizben, 
	 * akkor aktivalja a visszaszamlalot
	 * Ha a kapott paramertek hamis, az azt jelenti hogy a vizbeesett embert kihuztak
	 * @param b - kapott paramter alapjan donti el hogy valaki ki vagy be ment a vizbe
	 */
	synchronized public void setInWater(boolean b) {
		inWater = b;
		if(inWater && watercounter == -1) {
			watercounter=playerNum;
		}
		if(!inWater) {
			watercounter = -1;
		}
	}
	/**
	 * A vizbenlevoseg Gettere
	 * @return - visszaadja, hogy valaki van-e a vizben
	 */
	synchronized public boolean getInWater() {
		return inWater;
	}
	/**
	 * Az eszkimokat tartalmazo lista Gettere
	 * @return - visszaadja az eszkimokat tartalmazo listat
	 */
	synchronized public ArrayList<Eskimo> getEskimos() {
		return eskimos;
	}
	/**
	 * Az eszkimokat tartalmazo lista Settere
	 * @param eskimos - beallitja a paramterul kapott listat a sajat eszkimokat tartalmazo listanak
	 */
	synchronized public void setEskimos(ArrayList<Eskimo> eskimos) {
		this.eskimos = eskimos;
	}
	/**
	 * A kutatokat tartalmazo lista Gettere
	 * @return - visszaadja a kutatokat tartalmazo listat
	 */
	synchronized public ArrayList<Researcher> getResearchers() {
		return researchers;
	}
	/**
	 * A kutatokat tartalmazo lista Settere
	 * @param researchers - beallitja a paramterul kapott listat a sajat kuatatokat tartalmazo listanak
	 */
	synchronized public void setResearchers(ArrayList<Researcher> researchers) {
		this.researchers = researchers;
	}
	/**
	 * A jegesmedveket tartalmazo lista Gettere
	 * @return - visszaadja a jegesmedveket tartalmazo listat
	 */
	synchronized public ArrayList<PolarBear> getPolarbears() {
		return polarbears;
	}
	/**
	 * A jegesmedveket tartalmazo lista Settere
	 * @param polarbears - beallitja a paramterul kapott listat a sajat jegesmedveket tartalmazo listanak
	 */
	synchronized public void setPolarbears(ArrayList<PolarBear> polarbears) {
		this.polarbears = polarbears;
	}
	/**
	 * A grafikus felület mellett több szálra van szükségünk, hogy ne kerüljünk deadloc-ba.
	 * Így indítunk egy új szálat.
	 */
	@Override
	public void run() {
		this.play();
		
	}
	/**
	 * A várakozás állapotát lekérdező függvény
	 * @return - isWaitForComm értéke
	 */
	synchronized public boolean isWaitForComm() {
		return waitForComm;
	}
	/**
	 * A várakozás állapotát setter függvény
	 * @param waitForComm - új érték amire beállítjuk
	 */
	synchronized public void setWaitForComm(boolean waitForComm) {
		this.waitForComm = waitForComm;
	}
	/**
	 * A parancs lekérdező függvénye
	 * @return - a gomb szövege
	 */
	synchronized public String getButtonCommand() {
		return buttonCommand;
	}
	/**
	 * Az uj parancs beállítása
	 * @param buttonCommand - új érték amire beállítjuk
	 */
	synchronized public void setButtonCommand(String buttonCommand) {
		this.buttonCommand = buttonCommand;
	}
}
