package objects;

import characters.Character;
/**
 * A játékban megtalálható búvárruhát megvalósító osztály. Aki ezzel rendelkezik, az megmenekül a vízbeesésnél a megfulladástól.
 */
public class Swimsuit implements Item{
	/**
	 * Meghívja a paraméterként kapott karakter void setProtected(boolean prot) függvényét, 
	 * mellyel a karakter boolean protected változóját igazra állítja.
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	public void use(Character c) {
		c.setProtect(true);		
	}

}