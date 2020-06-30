package characters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import game.*;
import objects.Item;
/**
 * A konkr�t j�t�kosok �soszt�lya. A stepable oszt�lyb�l �r�kli p�r f�ggv�ny�t �s az ItemOwner interfacet implement�lja
 * Minden nem karakterspecifikus f�ggv�ny itt van implement�lva.
 * Absztakt �s virtu�lis f�ggv�nye nincs, de maga az oszt�ly �soszt�ly. 
 * @author BCSV
 *
 */
public abstract class Character extends Stepable implements ItemOwner{
	private String name;
	/**
	 * A j�t�kos energi�ja. Defaultk�nt 4 van neki minden k�rben
	 */
	private int energy;
	/**
	 * A j�t�kos �lete/testh�je. karakterfajt�nk�nt elt�r�, 4 �s 5 lehet.
	 */
	private int life;
	/**
	 * Egy j�t�kos maxim�lis �lete. Annak ellen�rz�s�hez kell, hogy esetleges �letn�vl�shez ellen�rizhess�k, hogy ne l�pje t�l
	 */
	private int maxlife;
	/**
	 * A j�t�kos v�detts�ge a fullad�s ellen. Ha ez true, a j�t�kos nem hal meg v�zbeeses ut�n
	 */
	private boolean protect = false;
	/**
	 * A karakter sz�m�tott ir�nya egy m�sik t�bl�hoz k�pest.
	 */
	private int dir;
	/**
	 * Karakter itemeinek list�ja, itt t�rolja az itemeket, amiket m�r felvett az IceTable-�kb�l, fels� korl�tja nincs
	 */
	private ArrayList<Item> items;
	/**
	 * Character oszt�ly konstruktora.
	 * Be�ll�t�sra ker�lnek az alap�rt�kek:
	 * �let, energia (egy k�rben mindig 4) �s
	 * �j lista inicializ�l�sa Itemeknek
	 * @param life �let �rt�ke
	 */
	public Character(int life) {
		this.maxlife = life;
		this.life=life;
		items = new ArrayList<Item>();
		energy = 4;
	}
	/**
	 * A j�t�k v�g�hez �ssze kell szerelni a pisztolyt.
	 * Ennek a f�ggv�nynek a megh�v�s�val kezdhetik meg
	 * a j�t�k megnyer�s�hez tartoz� folyamatot.
	 * Ellen�rzi, hogy minden j�t�kos egy t�bl�n �ll-e. 
	 * ha igen: Elkezdi haszn�lni a Gun-t, ha nem, nem csin�l semmit. 
	 * A Gun b�rmelyik j�t�kosn�l lehet.
	 * Egy energi�ba ker�l �sszesen
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
	* Egy karakter nev�nek lek�rdez� f�ggv�nye.
	* return - A karakter neve
	*/
	public String getCharName()
	{
		return name;
	}
	/**
	* Egy karakter nev�nek be�ll�t�(�rt�kad�) f�ggv�nye.
	*/
	public void setCharName(String Name)
	{
		name=Name;
	}
	/**
	 * Egy j�t�kos hal�lakor h�v�d� f�ggv�ny.
	 * L�nyegtelen, hogy a hal�lt jegesmedve, 
	 * fullad�s vagy kih�l�s okozta, �rtes�ti 
	 * a j�t�kvil�got hogy a j�t�k befejezod�tt, 
	 * veres�ggel v�get �rt (endGame(false))
	 * @see GameWorld
	 */
	public void die() {
		this.getMyTable().getGameworld().endGame(false);
		this.setEnergy(0);
	}
	/**
	 * A j�t�kosok lek�rik a saj�t j�gt�bl�juk igluj�t 
	 * �s cs�kkenthetik az iglu �lettartalm�t eggyel az 
	 * iglu destroy(1) megh�v�s�val, mert addig nem 
	 * �shatnak ki Itemet al�la, am�g rajta van. 
	 * Egy energi�ba ker�l
	 */
	public void destroyIglu() {
		this.getMyTable().getIglu().destroy(1);
	}
	/**
	 * Energia lek�rdez�se
	 * @return energia �rt�ke (0-4)
	 */
	public int getEnergy() {
		return energy;
	}
	/**
	 * �let lek�rdez�se
	 * @return �let �rt�ke (0-4/5)
	 */
	public int getLife() {
		return life;
	}
	/**
	 * V�detts�g lek�rdez�se
	 * @return protect (true / false)
	 */
	public boolean isProtect() {
		return protect;
	}
	/**
	 *  A j�t�kos testh�j�nek/�let�nek n�vel�se. 
	 *  A Food (�s a Surprise) haszn�lata tudja 
	 *  kiv�ltani az esem�nyt. A j�t�kos testh�je/�lete 
	 *  csak akkor n�het, ha az �lete nem a karakter�hez 
	 *  k�pesti maximum. Egy�bk�nt nem csin�l semmit.
	 */
	public void heal() {
		if(life < maxlife) {
			life++;
		}
	}
	/**
	 * H�vihar eset�n h�vott f�ggv�ny.
	 * Ha a h�vihar tiszt�n �ri a j�t�kost (v�delem n�lk�l), a j�t�kos �lete cs�kken.
	 * Ha a j�t�kos testh�je/�lete el�ri a 0-t, a j�t�kos meghal, a j�t�k v�get �r.
	 */
	public void storm() {
		life--;
		if(life == 0) {
			this.die();
		}
	}
	/**
	 * Visszaadja a Character j�gt�bl�j�t, amin �ll
	 */
	public IceTable getMyTable() {
		return super.getMyTable();
	}
	/**
	 * Be�llitja Character j�gt�bl�j�t
	 * @param it be�ll�tand� j�gt�bla
	 */
	public void setMyTable(IceTable it) {
		super.setMyTable(it);
	}
	/**
	 * A j�gt�bla takar�t�s�hoz sz�ks�ges f�ggv�ny. 
	 * Ha a j�gt�bl�n van h�, eggyel cs�kkenti az �rt�k�t, 
	 * ha nincs, de van j�g, akkor bet�ri, ha nincs 
	 * se h� se j�g akkor pedig nem csin�l semmit. Egy energi�ba ker�l.
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
	 * Megk�s�rli felvenni az itemet a j�gt�bl�r�l. 
	 * Siker�l, ha a j�gt�bl�ban van Item �s a 
	 * j�gt�bl�n nincs se h� se j�g. 
	 * Ezeket az adatokat elotte el kell k�rni a j�gt�bl�t�l. 
	 * Egy energi�ba ker�l.
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
	 * V�detts�g be�llit�sa
	 * @param prot v�detts�g (v�dett-true/v�detlen-false)
	 */
	public void setProtect(boolean prot) {
		protect=prot;
	}
	/**
	 * �let be�llit�sa
	 * @param life �let �rt�ke eg�sz sz�mk�nt
	 */
	public void setLife(int life) {
		this.life = life;
	}
	/**
	 * Energia be�llit�sa
	 * @param e energia �rt�ke eg�sz sz�mk�nt
	 */
	public void setEnergy(int e) {
		energy = e;
	}
	/**
	 * Max �let be�llit�sa, Eskimo = 5, Researcher = 4
	 * @param max max �let �rt�ke eg�sz sz�mk�nt
	 */
	public void setMaxLife(int max) {
		maxlife=max;
	}
	/**
	 * Max �let gettere.
	 * @return maximum �let, Eskimo = 5, Researcher = 4
	 */
	public int getMaxLife() {
		return maxlife;
	}
	/**
	 * Haszn�lja a param�ter�l kapott sorsz�m� Itemet. 
	 * A param�ter�l kapott Itemnek a saj�t t�rol�j�b�l 
	 * kell kiker�lnie, nem haszn�lhat olyat, ami nincs n�la. 
	 * Egy energi�ba ker�l
	 * @param idx az item sorsz�ma eg�sz sz�mk�nt
	 */
	public void useItem(int idx) {
		if(this.getItems().size()>0){
			this.getItems().get(idx).use(this);
			energy--;
		}		
	}
	/**
	 * Be�llitja a viszonylagos ir�nyt a m�sik j�gt�bl�hoz k�pest.
	 * @param sdir - a param�tre�l kapott ir�ny
	 */
	public void setStepDirection(int sdir) {
		dir = sdir;
	}
	
	/**
	 * L�p�si ir�ny Gettere. A k�t�l haszn�lat�hoz sz�ks�ges f�ggv�ny.
	 * @return dir, double tipus� v�ltoz�, radi�nban �rtend�
	 */
	public int getStepDirection() {
		return dir;
	}
	/**
	 * Minden k�r v�gezt�vel megh�vott f�ggv�ny.
	 * Vissza�ll�tja mindenki energi�j�t a default 4-re.
	 */
	public void nextRound() {
		this.setEnergy(4);
	}
	/**
	 * Fel�lirja az �s l�p�s f�ggv�ny�t, hogy energi�t cs�kkenthessen. 
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
	 * Item hozz�f�z�se a Character Item list�j�hoz
	 * @param i	amelyik Itemet hozz�adjuk
	 */
	public void addItem(Item i) {
		items.add(i);
	}
	/**
	 * Item t�rl�se a Character Item list�j�b�l
	 * @param i	- amelyik Itemet elvessz�k
	 */
	public void removeItem(Item i) {
		items.remove(i);
	}
	/**
	 * Itemek lek�rdez�se
	 * @return - a Character itemlist�ja
	 */
	public ArrayList<Item> getItems() {
		return items;
	}
}
