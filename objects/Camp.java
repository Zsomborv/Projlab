package objects;

import characters.Character;
/**
 * Ez az osztály valósítja meg a sátrat a játékban. A sátor egy kiásható tárgy, mely hasonlóan viselkedik, 
 * mint az Iglu, viszont csak egy karakter fér el benne, nem véd a medvétõl és egy kör után megsemmisül.
 */
public class Camp extends Iglu implements Item{
	/**
	 * A sátor megépülésért felelõs függvény. Beállítja a sátor mezõjét a karakter aktuális mezõjére,
	 * majd meghívja az õsosztály build függvényét. Beállítodik a sátor élete kettõre, majd eltávolítjuk a karaktertõl a sátrat.
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	public void use(Character c) {
		this.setMyTable(c.getMyTable());
		super.build();
		this.setLifeTime(2);
		c.removeItem(this);
	}

}
