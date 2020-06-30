package characters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import game.*;
import objects.Item;
/**
 * A konkrét játékosok õsosztálya. A stepable osztályból örökli pár függvényét és az ItemOwner interfacet implementálja
 * Minden nem karakterspecifikus függvény itt van implementálva.
 * Absztakt és virtuális függvénye nincs, de maga az osztály õsosztály. 
 * @author BCSV
 *
 */
public abstract class Character extends Stepable implements ItemOwner{
	private String name;
	/**
	 * A játékos energiája. Defaultként 4 van neki minden körben
	 */
	private int energy;
	/**
	 * A játékos élete/testhõje. karakterfajtánként eltérõ, 4 és 5 lehet.
	 */
	private int life;
	/**
	 * Egy játékos maximális élete. Annak ellenõrzéséhez kell, hogy esetleges életnövléshez ellenõrizhessük, hogy ne lépje túl
	 */
	private int maxlife;
	/**
	 * A játékos védettsége a fulladás ellen. Ha ez true, a játékos nem hal meg vízbeeses után
	 */
	private boolean protect = false;
	/**
	 * A karakter számított iránya egy másik táblához képest.
	 */
	private int dir;
	/**
	 * Karakter itemeinek listája, itt tárolja az itemeket, amiket már felvett az IceTable-ökbõl, felsõ korlátja nincs
	 */
	private ArrayList<Item> items;
	/**
	 * Character osztály konstruktora.
	 * Beállításra kerülnek az alapértékek:
	 * élet, energia (egy körben mindig 4) és
	 * új lista inicializálása Itemeknek
	 * @param life élet értéke
	 */
	public Character(int life) {
		this.maxlife = life;
		this.life=life;
		items = new ArrayList<Item>();
		energy = 4;
	}
	/**
	 * A játék végéhez össze kell szerelni a pisztolyt.
	 * Ennek a függvénynek a meghívásával kezdhetik meg
	 * a játék megnyeréséhez tartozó folyamatot.
	 * Ellenõrzi, hogy minden játékos egy táblán áll-e. 
	 * ha igen: Elkezdi használni a Gun-t, ha nem, nem csinál semmit. 
	 * A Gun bármelyik játékosnál lehet.
	 * Egy energiába kerül összesen
	 */
	public void buildGun() {
		if(this.getMyTable().getPersonNum() == this.getMyTable().getGameworld().getMaxPlayer()) {
			for(int j = 0; j < this.getMyTable().getGameworld().getPlayers().size(); j++) {
				for(int i = 0; i < this.getMyTable().getGameworld().getPlayers().get(j).getItems().size(); i++) {
					if(this.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).getClass().equals(objects.Gun.class)) {
						this.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).use(this);
					}
				}
			}
		}
	}
	/**
	* Egy karakter nevének lekérdezõ függvénye.
	* return - A karakter neve
	*/
	public String getCharName()
	{
		return name;
	}
	/**
	* Egy karakter nevének beállító(értékadó) függvénye.
	*/
	public void setCharName(String Name)
	{
		name=Name;
	}
	/**
	 * Egy játékos halálakor hívódó függvény.
	 * Lényegtelen, hogy a halált jegesmedve, 
	 * fulladás vagy kihûlés okozta, értesíti 
	 * a játékvilágot hogy a játék befejezodött, 
	 * vereséggel véget ért (endGame(false))
	 * @see GameWorld
	 */
	public void die() {
		this.getMyTable().getGameworld().endGame(false);
		this.setEnergy(0);
	}
	/**
	 * A játékosok lekérik a saját jégtáblájuk igluját 
	 * és csökkenthetik az iglu élettartalmát eggyel az 
	 * iglu destroy(1) meghívásával, mert addig nem 
	 * áshatnak ki Itemet alóla, amíg rajta van. 
	 * Egy energiába kerül
	 */
	public void destroyIglu() {
		this.getMyTable().getIglu().destroy(1);
	}
	/**
	 * Energia lekérdezése
	 * @return energia értéke (0-4)
	 */
	public int getEnergy() {
		return energy;
	}
	/**
	 * Élet lekérdezése
	 * @return élet értéke (0-4/5)
	 */
	public int getLife() {
		return life;
	}
	/**
	 * Védettség lekérdezése
	 * @return protect (true / false)
	 */
	public boolean isProtect() {
		return protect;
	}
	/**
	 *  A játékos testhõjének/életének növelése. 
	 *  A Food (és a Surprise) használata tudja 
	 *  kiváltani az eseményt. A játékos testhõje/élete 
	 *  csak akkor nõhet, ha az élete nem a karakteréhez 
	 *  képesti maximum. Egyébként nem csinál semmit.
	 */
	public void heal() {
		if(life < maxlife) {
			life++;
		}
	}
	/**
	 * Hóvihar esetén hívott függvény.
	 * Ha a hóvihar tisztán éri a játékost (védelem nélkül), a játékos élete csökken.
	 * Ha a játékos testhõje/élete eléri a 0-t, a játékos meghal, a játék véget ér.
	 */
	public void storm() {
		life--;
		if(life == 0) {
			this.die();
		}
	}
	/**
	 * Visszaadja a Character jégtábláját, amin áll
	 */
	public IceTable getMyTable() {
		return super.getMyTable();
	}
	/**
	 * Beállitja Character jégtábláját
	 * @param it beállítandó jégtábla
	 */
	public void setMyTable(IceTable it) {
		super.setMyTable(it);
	}
	/**
	 * A jégtábla takarításához szükséges függvény. 
	 * Ha a jégtáblán van hó, eggyel csökkenti az értékét, 
	 * ha nincs, de van jég, akkor betöri, ha nincs 
	 * se hó se jég akkor pedig nem csinál semmit. Egy energiába kerül.
	 */
	public void manageIceTable() {
		this.setEnergy(this.getEnergy()-1);
		try {
			destroyIglu();		
		}
		catch(Exception e){
			int snow = this.getMyTable().getSnow();
			boolean ice = this.getMyTable().isFrozen();
			
			if(snow >=1) {
				this.getMyTable().setSnow(snow-1);
			} 
			else if(snow == 0 && ice) {
				this.getMyTable().setIce(false);
			}
		}
	}
	/**
	 * Megkísérli felvenni az itemet a jégtábláról. 
	 * Sikerül, ha a jégtáblában van Item és a 
	 * jégtáblán nincs se hó se jég. 
	 * Ezeket az adatokat elotte el kell kérni a jégtáblától. 
	 * Egy energiába kerül.
	 */
	public void pickupItem() {
		if(!this.getMyTable().isFrozen()) {
			if(this.getMyTable().getItem() instanceof Item) {
				this.addItem(this.getMyTable().getItem());
				this.getMyTable().removeItem(this.getMyTable().getItem());
				this.setEnergy(this.getEnergy()-1);
			}		
		}
	}
	/**
	 * Védettség beállitása
	 * @param prot védettség (védett-true/védetlen-false)
	 */
	public void setProtect(boolean prot) {
		protect=prot;
	}
	/**
	 * Élet beállitása
	 * @param life élet értéke egész számként
	 */
	public void setLife(int life) {
		this.life = life;
	}
	/**
	 * Energia beállitása
	 * @param e energia értéke egész számként
	 */
	public void setEnergy(int e) {
		energy = e;
	}
	/**
	 * Max élet beállitása, Eskimo = 5, Researcher = 4
	 * @param max max élet értéke egész számként
	 */
	public void setMaxLife(int max) {
		maxlife=max;
	}
	/**
	 * Max élet gettere.
	 * @return maximum élet, Eskimo = 5, Researcher = 4
	 */
	public int getMaxLife() {
		return maxlife;
	}
	/**
	 * Használja a paraméterül kapott sorszámú Itemet. 
	 * A paraméterül kapott Itemnek a saját tárolójából 
	 * kell kikerülnie, nem használhat olyat, ami nincs nála. 
	 * Egy energiába kerül
	 * @param idx az item sorszáma egész számként
	 */
	public void useItem(int idx) {
		if(this.getItems().size()>0){
			this.getItems().get(idx).use(this);
			energy--;
		}		
	}
	/**
	 * Beállitja a viszonylagos irányt a másik jégtáblához képest.
	 * @param sdir - a paramétreül kapott irány
	 */
	public void setStepDirection(int sdir) {
		dir = sdir;
	}
	
	/**
	 * Lépési irány Gettere. A kötél használatához szükséges függvény.
	 * @return dir, double tipusú változó, radiánban értendõ
	 */
	public int getStepDirection() {
		return dir;
	}
	/**
	 * Minden kör végeztével meghívott függvény.
	 * Visszaállítja mindenki energiáját a default 4-re.
	 */
	public void nextRound() {
		this.setEnergy(4);
	}
	/**
	 * Felülirja az õs lépés függvényét, hogy energiát csökkenthessen. 
	 */
	@Override
	public void step(int dir){
		
		energy--;
		super.step(dir);
		for(int i = 0;i<this.getMyTable().getCharacters().size();i++)
		{
			if(this.getMyTable().getCharacters().get(i).getClass().equals(characters.PolarBear.class))
			{
				for(int j = 0;j<this.getMyTable().getCharacters().size();j++)
				{
					if(this.getMyTable().getCharacters().get(j).getClass().equals(characters.Researcher.class))
					{
						this.getMyTable().getPolarbear().kill();
					}
					if(this.getMyTable().getCharacters().get(j).getClass().equals(characters.Eskimo.class))
					{
						this.getMyTable().getPolarbear().kill();
					}
				}
			}
		}
	}
	/**
	 * Item hozzáfûzése a Character Item listájához
	 * @param i	amelyik Itemet hozzáadjuk
	 */
	public void addItem(Item i) {
		items.add(i);
	}
	/**
	 * Item törlése a Character Item listájából
	 * @param i	- amelyik Itemet elvesszük
	 */
	public void removeItem(Item i) {
		items.remove(i);
	}
	/**
	 * Itemek lekérdezése
	 * @return - a Character itemlistája
	 */
	public ArrayList<Item> getItems() {
		return items;
	}
}
