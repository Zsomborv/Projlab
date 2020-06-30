package objects;

import characters.Character;
/**
 * A játékban szereplõ lapátot megvalósító osztály. 
 * Ennek a segítségével egy játék egy energia felhasználásával két egység havat lapátolhat el.
 */
public class Shovel implements Item{
	/**
	 * A függvény lekérdezi a paraméterként kapott karakter jégtáblájának snow és ice attribútumát 
	 * (int getSnow() és boolean getIce() függvények hívása). 
	 * Ezután amennyiben a hó legalább két egységnyi vastag, úgy kettõvel csökkentjük a jégtábla 
	 * snow attribútumának értékét (void setSnow() függvény segítségével), ha csak egy egységnyi vastag, 
	 * akkor eggyel csökkentjük a változó értékét. Amennyiben nincs hó a jégtáblán (snow értéke 0) 
	 * és a tábla be van fagyva (ice értéke true), 
	 * akkor feltörjük a jeget(ice értékét false-ra állítjuk a void setIce(boolean ice) függvény segítségével).
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	public void use(Character c) {
		
		int snow = c.getMyTable().getSnow();
		boolean ice = c.getMyTable().isFrozen();
		
		if(snow >1) {
			c.getMyTable().setSnow(snow-2);
		} else if(snow==1) {
			c.getMyTable().setSnow(snow-1);
		} else if(snow == 0 && ice) {
			c.getMyTable().setIce(false);
		}	
	}
}
