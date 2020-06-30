package objects;

import characters.Character;
/**
 * Ez az oszt�ly felel a t�r�keny lap�t j�t�kban val� megval�s�t�s�r�l. 
 * Egy t�r�keny lap�t hasonl�an m�k�dik, mint egy sima lap�t, 
 * ugyan�gy 2 egys�g h� lap�tolhat� el vele egy egys�gnyi energia felhaszn�l�s�val, 
 * viszont h�rom haszn�lat ut�n elt�rik �s �gy m�r nem haszn�lhat�.
 */
public class BreakableShovel extends Shovel{
	/**
	 * A h�tral�v� lap�tol�sok sz�m�t nyilv�ntart� v�ltoz�.
	 */
	private int usable;
	/**
	 * Az oszt�ly konstruktora, ami egy p�ld�ny l�trehoz�sakor be�ll�tja a h�tral�v� haszn�latok sz�m�t h�romra.
	 */
	public BreakableShovel(){
		usable = 3;
	}
	/**
	 * A h�tral�v� haszn�latok sz�m�t(usable  v�ltoz�) lek�rdez� f�ggv�ny.
	 * @return - a h�tral�v� haszn�latok sz�m�val t�r vissza.
	 */
	public int getUsable() {
		return usable;
	}
	/**
	 * A h�tral�v� haszn�latok sz�m�t(usable  v�ltoz�) be�ll�t� f�ggv�ny.
	 * @param u - Az �j �rt�k, amire be�ll�tjuk  a v�ltoz�t.
	 */
	public void setUseable(int u) {
		usable = u;
	}
	/**
	 * Amennyiben a usable attrib�tum �rt�ke t�bb, mint nulla, 
	 * akkor a f�ggv�ny megh�vja az �soszt�ly use f�ggv�ny�t. 
	 * Ezut�n a usable �rt�k�t eggyel cs�kkentj�k.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	@Override
	public void use(Character c) {
		if(usable > 0) {
			super.use(c);
			usable--;
		} 
	}
		
}
