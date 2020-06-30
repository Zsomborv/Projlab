package objects;

import characters.Character;
/**
 * Ez az osztály felel a törékeny lapát játékban való megvalósításáról. 
 * Egy törékeny lapát hasonlóan mûködik, mint egy sima lapát, 
 * ugyanúgy 2 egység hó lapátolható el vele egy egységnyi energia felhasználásával, 
 * viszont három használat után eltörik és így már nem használható.
 */
public class BreakableShovel extends Shovel{
	/**
	 * A hátralévõ lapátolások számát nyilvántartó változó.
	 */
	private int usable;
	/**
	 * Az osztály konstruktora, ami egy példány létrehozásakor beállítja a hátralévõ használatok számát háromra.
	 */
	public BreakableShovel(){
		usable = 3;
	}
	/**
	 * A hátralévõ használatok számát(usable  változó) lekérdezõ függvény.
	 * @return - a hátralévõ használatok számával tér vissza.
	 */
	public int getUsable() {
		return usable;
	}
	/**
	 * A hátralévõ használatok számát(usable  változó) beállító függvény.
	 * @param u - Az új érték, amire beállítjuk  a változót.
	 */
	public void setUseable(int u) {
		usable = u;
	}
	/**
	 * Amennyiben a usable attribútum értéke több, mint nulla, 
	 * akkor a függvény meghívja az õsosztály use függvényét. 
	 * Ezután a usable értékét eggyel csökkentjük.
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	@Override
	public void use(Character c) {
		if(usable > 0) {
			super.use(c);
			usable--;
		} 
	}
		
}
