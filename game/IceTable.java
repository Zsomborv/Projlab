package game;

import java.io.Serializable;
import java.util.ArrayList;

import characters.PolarBear;
import characters.Stepable;
import objects.Iglu;
import objects.Item;
/**Egy jégtáblán lévõ igluk, karakterek, szomszédok kezelése.
 * Getter, setter függvények a játék gördülékeny haladása érdekében
 * @author BCSV
 */
public class IceTable implements ItemOwner, Serializable{
	/**
	 * Szomszédok tárolása
	 */
	private ArrayList<IceTable> neighbours;
	/**
    * Rajta álló karakterek tárolása
	 */
	private ArrayList<Stepable> characters;
	/**
	 * Rajta lévõ itemek tárolása
	 */
	private ArrayList<Item> item;
	/**
	 * Rajta álló medvék tárolása
	 */
	private ArrayList<PolarBear> polarbear;
	/**
	 * A GameWorld objektum tárolása, a késõbbi elérés érdekében
	 */
	private GameWorld gameworld;
	/**
	 * A befagyott állapot igen/nem tárolása
	 */
	private boolean frozen;
	/**
	 * A teherbírás tárolása
	 */
	private int strength;
	/**
	 * A rajta lévõ hó mennyiségének tárolása
	 */
	private int snow;
	/**
	 * Rajta álló iglu tárolása
	 */
	private ArrayList<Iglu> iglu;
	/*
	 * Megmutatja hogy ellenorizte-e már sarkkutató
	 */
	private boolean checked = false;
	/**
	 * Konstruktor
	 * létrehoz egy jégtáblát, minden változó kezdeti inicializálása
	 * GameWorld objektum eltárolása
	 * @param gw - A létrehozó GameWorld objektum
	 */
	public IceTable(GameWorld gw){
		neighbours = new ArrayList<IceTable>();
		characters = new ArrayList<Stepable>();
		item = new ArrayList<Item>();
		iglu = new ArrayList<Iglu>();
		polarbear = new ArrayList<PolarBear>();
		gameworld = gw;
	}
	/**
	 * Kapott iglu tárolás sajátként. Ha az iglu mérete kisebb mint 1
	 * eltárolja a jégtábla az paraméterként kapott iglut.
	 * @param Egy iglu objektum, amit tárolni fogunk.
	 */
	public void addIglu(Iglu iglu) {
		if(this.iglu.size()<1) {
			this.iglu.add(iglu);
		} else {
			//Valami üzenet hogy itt már van Iglu féle cucc
		}
	}
	/**
	 * Getter függvény a jégtábla szomszédjaira. 
	 * @return szomszédos jégtáblák.
	 */
	public ArrayList<IceTable> getNeighbours(){
		return neighbours;
	}
	/**
	 * A szomszéd jégtáblájának a regisztrálása a paraméterül kapott IceTable alapján.
	 * @param A leendõ szomszédos jégtábla objektum.
	 */
	public void addNeighbour(IceTable it) {
		neighbours.add(it);
		it.acceptNeighbour(this);
	}
	/**
	 * Hozzáadja a paraméterként kapott IceTable objektumot a szomszédok listájának végéhez
	 * @param it - Az IceTable objektum, ami szomszéd lesz
	 */
	public void acceptNeighbour(IceTable it) {
		neighbours.add(it);
	}
	/**
	 *  Az adott karakter hozzáadása a mezõhöz. 
	 *  Ha instabil, ellenõrzi, hogy az új karakterrel nem lépték-e át a mezõ strapaírását.
	 *  Ha átlépték a mezõ strapabírását, akkor a játék véget ér.
	 *  Ha lyuk, a játékos nem léphet többet.
	 *  A következõ játékos következik.
	 *  @param Egy mozogni képes karakter objektum.
	 */
	public void addPerson(Stepable character) {
		characters.add(character);
		int idx = this.getGameworld().getPlayers().indexOf(character);
		 if(strength == 0 && !character.getClass().equals(characters.PolarBear.class)) {
			 if(!this.getGameworld().getPlayers().get(idx).isProtect()) {
				 gameworld.nextPlayer();
					gameworld.setInWater(true);
			 }

		} else if(characters.size() >strength && !character.getClass().equals(characters.PolarBear.class)) {
			this.gameworld.endGame(false);
		}
		 try {
		 if(character.getClass().equals(characters.PolarBear.class)) {
			 polarbear.add((PolarBear) character);
		 }
		 } catch (Exception e) {
			 
		 }
	}
	/**
	 * Getter függvény a jégtábla befagyására
	 * @return visszatér a frozen változóval (boolean)
	 */
	public boolean isFrozen() {
		return frozen;
	}
	/**
	 * Getter függvény az elsõ iglura
	 * @return A rajta álló iglu objektum
	 */
	public Iglu getIglu() {
		return iglu.get(0);
	}
	/**
	 * Getter függvény az igluk listájára
	 */
	public ArrayList<Iglu> IgluList(){
		return iglu;
	}
	/**
	 * Getter függvény az igluk számára
	 */
	public int isIglu() {
		return iglu.size();	
	}
	/**
	 * A paraméterként kapott irány alapján visszaadja az adott irányban lévõ szomszédját.
	 *  Az irány radiánban értendõ, a mezõhöz képesti elhelyezkedés és a szomszédok száma alapján határozza meg.
	 *  (Pl.: Ha 3 szomszédja van, akkor 2.094 radinánonként helyezkednek el, ami 120 fok.
	 *   Eszerint a 3. szomszéd az 4.188 és 6.28 radián közötti terület.)
	 *   @param Irány radiánban (double).
	 *   @return Az adott irányban lévõ IceTable objektum.
	 * 
	 */
	public IceTable getNeighbour(int dir) {
		return neighbours.get(dir);
	}
	/**
	 * Getter függvény az rajta található itemre
	 * @return Visszatér az adott Item objektummal.
	 */
	public Item getItem() {
		return this.getItems().get(0);
	}
	/**Getter függvény a jégtáblán lévõ karakterre.
	 * @param idx - a kívánt indexû játékos egész számként
	 * @return - Az adott léptethetõ objektum
	 */
	public Stepable getPerson(int idx){
		return characters.get(idx);
	}
	/**
	 * Getter függvény a jégtáblán lévõ karakterekre.
	 * @return a karakterek listája
	 */
	public ArrayList<Stepable> getCharacters() {
		return characters;
	}
	/**Megkapja a jégtáblán lévõ karaktereket számát.
	 * @return - A karakterek száma egész számként
	 */
	public int getPersonNum() {
		return characters.size();
	}
	/**
	 * Getter függvény a snow változóra.
	 * @return A hó mennyisége egész számként
	 */
	public int getSnow() {
		return snow;
	}
	/**
	 * Getter függvény a strenght változóra.
	 * @return A teherbírás egész számként
	 */
	public int getStrength() {
		return strength;
	}
	/**
	 * Setter függvény, ezzel tudjuk állítani a jégtábla strapabírását.
	 * @param s A beállítani kívánt teherbírás
	 */
	public void setStrength(int s) {
		strength = s;
	}
	/**
	 * Léptethetõ objektum kivétele a tárolt objektumok listájából.
	 * @param s Mozgóképes karakter.
	 */
	public void removePerson(Stepable s) {
		if(s.getClass().equals(characters.PolarBear.class)) {
			polarbear = null;
		}
		characters.remove(s);
	}
	/**
	 * Setter függvény az ice változóra.
	 * @param ice - A kívánt érték (boolean)
	 */
	public void setIce(boolean ice) {
		frozen = ice;
	}
	/**
	 * Setter függvény a snow változóra.
	 * @param s A kívánt érték egész számként
	 */
	public void setSnow(int s) {
		snow = s;
	}
	/**
	 * A jégtábláról eltávolítunk egy iglut.
	 */
	public void removeIglu() {
		iglu.clear();
	}
	/**
	 * Egy új kör következik, ekkor minden Játékos nextRoundját meghívjuk.
	 * Leromboljuk a sátrakat és csökkentjük a jégtáblán álló Iglu élettartamát.
	 * Ha az iglu élettartama kisebb mint 0 vagy egyenlõ vele akkor kitöröljük az iglut.
	 */
	public void nextRound() {
		if(this.iglu.size()>0) {
			try {
			this.getIglu().destroy(1);
			if(this.getIglu().getLifeTime()<=0) {
				this.removeIglu();
			}
			} catch (Exception e) {
				
			}
		}
		
		if(characters.size()>0) {
			for(int i=0; i<characters.size() ; i++) {
				characters.get(i).nextRound();
			}
		}
	}
	/**
	 * Getter függvény a jegesmedve változóra.
	 * @return A PolarBear objektum
	 */
	public PolarBear getPolarbear() {
		return polarbear.get(0);
	}
	/**
	 * Getter függvény az összes jegesmedvére
	 * @return A PolarBear objektumokat tároló lista
	 */
	public ArrayList<PolarBear> getPolarbears(){
		return polarbear;
	}
	/**
	 * Setter függvény a PolarBear-re
	 * @param polarbear A hozzáadandó a jegesmedve(k)
	 */
	public void setPolarbear(ArrayList<PolarBear> polarbear) {
		this.polarbear = polarbear;
	}
	/**
	 * A paraméterül kapott Itemet hozzáadja a saját tárolójához.
	 * @param i A hozzáadni kívánt Item objektum
	 */
	public void addItem(Item i) {
		item.add(i);
	}
	/**
	 * Eltávolítja az adott itemet.
	 * @param i A távolítandó item objektum
	 */
	public void removeItem(Item i) {
		item.remove(0);
	}
	/**
	 * Visszatér az adott jégtáblán lévõ itemekkel.
	 * @return Az item objektumok listája
	 */
	public ArrayList<Item> getItems() {
		return item;
	}
	/**
	 * Getter függvény a gameworld attribútumra.
	 * @return A gameworld objektum
	 */
	public GameWorld getGameworld() {
		return gameworld;
	}
	/**
	 * Setter függvény a gameworld attribútumra.
	 * @param gameworld beállítja a gameworld változó új értékéül.
	 */
	public void setGameworld(GameWorld gameworld) {
		this.gameworld = gameworld;
	}
	/**
	 * Visszaadja a, hogy ellenorizte-e mar Researcher
	 * @return
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * Beallitja, hogy ellenoriztek-e
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
