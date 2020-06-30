package objects;
import java.io.Serializable;

import characters.Character;
/**
 * Ez az osztály a játékban található különbözõ használható tárgyak interfésze.
 */
public interface Item extends Serializable {
	/**
	 * Egy tárgy használatát megvalósító függvény. Minden tárgynál felülírásra kerül.
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	public void use(Character c);
}
