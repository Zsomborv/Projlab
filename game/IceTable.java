package game;

import java.io.Serializable;
import java.util.ArrayList;

import characters.PolarBear;
import characters.Stepable;
import objects.Iglu;
import objects.Item;
/**Egy j�gt�bl�n l�v� igluk, karakterek, szomsz�dok kezel�se.
 * Getter, setter f�ggv�nyek a j�t�k g�rd�l�keny halad�sa �rdek�ben
 * @author BCSV
 */
public class IceTable implements ItemOwner, Serializable{
	/**
	 * Szomsz�dok t�rol�sa
	 */
	private ArrayList<IceTable> neighbours;
	/**
    * Rajta �ll� karakterek t�rol�sa
	 */
	private ArrayList<Stepable> characters;
	/**
	 * Rajta l�v� itemek t�rol�sa
	 */
	private ArrayList<Item> item;
	/**
	 * Rajta �ll� medv�k t�rol�sa
	 */
	private ArrayList<PolarBear> polarbear;
	/**
	 * A GameWorld objektum t�rol�sa, a k�s�bbi el�r�s �rdek�ben
	 */
	private GameWorld gameworld;
	/**
	 * A befagyott �llapot igen/nem t�rol�sa
	 */
	private boolean frozen;
	/**
	 * A teherb�r�s t�rol�sa
	 */
	private int strength;
	/**
	 * A rajta l�v� h� mennyis�g�nek t�rol�sa
	 */
	private int snow;
	/**
	 * Rajta �ll� iglu t�rol�sa
	 */
	private ArrayList<Iglu> iglu;
	/*
	 * Megmutatja hogy ellenorizte-e m�r sarkkutat�
	 */
	private boolean checked = false;
	/**
	 * Konstruktor
	 * l�trehoz egy j�gt�bl�t, minden v�ltoz� kezdeti inicializ�l�sa
	 * GameWorld objektum elt�rol�sa
	 * @param gw - A l�trehoz� GameWorld objektum
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
	 * Kapott iglu t�rol�s saj�tk�nt. Ha az iglu m�rete kisebb mint 1
	 * elt�rolja a j�gt�bla az param�terk�nt kapott iglut.
	 * @param Egy iglu objektum, amit t�rolni fogunk.
	 */
	public void addIglu(Iglu iglu) {
		if(this.iglu.size()<1) {
			this.iglu.add(iglu);
		} else {
			//Valami �zenet hogy itt m�r van Iglu f�le cucc
		}
	}
	/**
	 * Getter f�ggv�ny a j�gt�bla szomsz�djaira. 
	 * @return szomsz�dos j�gt�bl�k.
	 */
	public ArrayList<IceTable> getNeighbours(){
		return neighbours;
	}
	/**
	 * A szomsz�d j�gt�bl�j�nak a regisztr�l�sa a param�ter�l kapott IceTable alapj�n.
	 * @param A leend� szomsz�dos j�gt�bla objektum.
	 */
	public void addNeighbour(IceTable it) {
		neighbours.add(it);
		it.acceptNeighbour(this);
	}
	/**
	 * Hozz�adja a param�terk�nt kapott IceTable objektumot a szomsz�dok list�j�nak v�g�hez
	 * @param it - Az IceTable objektum, ami szomsz�d lesz
	 */
	public void acceptNeighbour(IceTable it) {
		neighbours.add(it);
	}
	/**
	 *  Az adott karakter hozz�ad�sa a mez�h�z. 
	 *  Ha instabil, ellen�rzi, hogy az �j karakterrel nem l�pt�k-e �t a mez� strapa�r�s�t.
	 *  Ha �tl�pt�k a mez� strapab�r�s�t, akkor a j�t�k v�get �r.
	 *  Ha lyuk, a j�t�kos nem l�phet t�bbet.
	 *  A k�vetkez� j�t�kos k�vetkezik.
	 *  @param Egy mozogni k�pes karakter objektum.
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
	 * Getter f�ggv�ny a j�gt�bla befagy�s�ra
	 * @return visszat�r a frozen v�ltoz�val (boolean)
	 */
	public boolean isFrozen() {
		return frozen;
	}
	/**
	 * Getter f�ggv�ny az els� iglura
	 * @return A rajta �ll� iglu objektum
	 */
	public Iglu getIglu() {
		return iglu.get(0);
	}
	/**
	 * Getter f�ggv�ny az igluk list�j�ra
	 */
	public ArrayList<Iglu> IgluList(){
		return iglu;
	}
	/**
	 * Getter f�ggv�ny az igluk sz�m�ra
	 */
	public int isIglu() {
		return iglu.size();	
	}
	/**
	 * A param�terk�nt kapott ir�ny alapj�n visszaadja az adott ir�nyban l�v� szomsz�dj�t.
	 *  Az ir�ny radi�nban �rtend�, a mez�h�z k�pesti elhelyezked�s �s a szomsz�dok sz�ma alapj�n hat�rozza meg.
	 *  (Pl.: Ha 3 szomsz�dja van, akkor 2.094 radin�nonk�nt helyezkednek el, ami 120 fok.
	 *   Eszerint a 3. szomsz�d az 4.188 �s 6.28 radi�n k�z�tti ter�let.)
	 *   @param Ir�ny radi�nban (double).
	 *   @return Az adott ir�nyban l�v� IceTable objektum.
	 * 
	 */
	public IceTable getNeighbour(int dir) {
		return neighbours.get(dir);
	}
	/**
	 * Getter f�ggv�ny az rajta tal�lhat� itemre
	 * @return Visszat�r az adott Item objektummal.
	 */
	public Item getItem() {
		return this.getItems().get(0);
	}
	/**Getter f�ggv�ny a j�gt�bl�n l�v� karakterre.
	 * @param idx - a k�v�nt index� j�t�kos eg�sz sz�mk�nt
	 * @return - Az adott l�ptethet� objektum
	 */
	public Stepable getPerson(int idx){
		return characters.get(idx);
	}
	/**
	 * Getter f�ggv�ny a j�gt�bl�n l�v� karakterekre.
	 * @return a karakterek list�ja
	 */
	public ArrayList<Stepable> getCharacters() {
		return characters;
	}
	/**Megkapja a j�gt�bl�n l�v� karaktereket sz�m�t.
	 * @return - A karakterek sz�ma eg�sz sz�mk�nt
	 */
	public int getPersonNum() {
		return characters.size();
	}
	/**
	 * Getter f�ggv�ny a snow v�ltoz�ra.
	 * @return A h� mennyis�ge eg�sz sz�mk�nt
	 */
	public int getSnow() {
		return snow;
	}
	/**
	 * Getter f�ggv�ny a strenght v�ltoz�ra.
	 * @return A teherb�r�s eg�sz sz�mk�nt
	 */
	public int getStrength() {
		return strength;
	}
	/**
	 * Setter f�ggv�ny, ezzel tudjuk �ll�tani a j�gt�bla strapab�r�s�t.
	 * @param s A be�ll�tani k�v�nt teherb�r�s
	 */
	public void setStrength(int s) {
		strength = s;
	}
	/**
	 * L�ptethet� objektum kiv�tele a t�rolt objektumok list�j�b�l.
	 * @param s Mozg�k�pes karakter.
	 */
	public void removePerson(Stepable s) {
		if(s.getClass().equals(characters.PolarBear.class)) {
			polarbear = null;
		}
		characters.remove(s);
	}
	/**
	 * Setter f�ggv�ny az ice v�ltoz�ra.
	 * @param ice - A k�v�nt �rt�k (boolean)
	 */
	public void setIce(boolean ice) {
		frozen = ice;
	}
	/**
	 * Setter f�ggv�ny a snow v�ltoz�ra.
	 * @param s A k�v�nt �rt�k eg�sz sz�mk�nt
	 */
	public void setSnow(int s) {
		snow = s;
	}
	/**
	 * A j�gt�bl�r�l elt�vol�tunk egy iglut.
	 */
	public void removeIglu() {
		iglu.clear();
	}
	/**
	 * Egy �j k�r k�vetkezik, ekkor minden J�t�kos nextRoundj�t megh�vjuk.
	 * Leromboljuk a s�trakat �s cs�kkentj�k a j�gt�bl�n �ll� Iglu �lettartam�t.
	 * Ha az iglu �lettartama kisebb mint 0 vagy egyenl� vele akkor kit�r�lj�k az iglut.
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
	 * Getter f�ggv�ny a jegesmedve v�ltoz�ra.
	 * @return A PolarBear objektum
	 */
	public PolarBear getPolarbear() {
		return polarbear.get(0);
	}
	/**
	 * Getter f�ggv�ny az �sszes jegesmedv�re
	 * @return A PolarBear objektumokat t�rol� lista
	 */
	public ArrayList<PolarBear> getPolarbears(){
		return polarbear;
	}
	/**
	 * Setter f�ggv�ny a PolarBear-re
	 * @param polarbear A hozz�adand� a jegesmedve(k)
	 */
	public void setPolarbear(ArrayList<PolarBear> polarbear) {
		this.polarbear = polarbear;
	}
	/**
	 * A param�ter�l kapott Itemet hozz�adja a saj�t t�rol�j�hoz.
	 * @param i A hozz�adni k�v�nt Item objektum
	 */
	public void addItem(Item i) {
		item.add(i);
	}
	/**
	 * Elt�vol�tja az adott itemet.
	 * @param i A t�vol�tand� item objektum
	 */
	public void removeItem(Item i) {
		item.remove(0);
	}
	/**
	 * Visszat�r az adott j�gt�bl�n l�v� itemekkel.
	 * @return Az item objektumok list�ja
	 */
	public ArrayList<Item> getItems() {
		return item;
	}
	/**
	 * Getter f�ggv�ny a gameworld attrib�tumra.
	 * @return A gameworld objektum
	 */
	public GameWorld getGameworld() {
		return gameworld;
	}
	/**
	 * Setter f�ggv�ny a gameworld attrib�tumra.
	 * @param gameworld be�ll�tja a gameworld v�ltoz� �j �rt�k��l.
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
