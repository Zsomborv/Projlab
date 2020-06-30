package objects;

import characters.Character;
/**
 * Ez az osztályt valósítja meg a játékban megtalálható ételt. 
 * Felhasználásával a karakter testhõje növekszik eggyel.
 */
public class Food implements Item{
	/**
	 * Meghívja a paraméterként kapott karakter heal függvényét, ezzel annak testhõjét növeli.
	 * Ezután eltávolítjuk a karaktertõl az ételt.
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	public void use(Character c) {
		c.heal();
		c.removeItem(this);
	}

}
