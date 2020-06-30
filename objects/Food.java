package objects;

import characters.Character;
/**
 * Ez az oszt�lyt val�s�tja meg a j�t�kban megtal�lhat� �telt. 
 * Felhaszn�l�s�val a karakter testh�je n�vekszik eggyel.
 */
public class Food implements Item{
	/**
	 * Megh�vja a param�terk�nt kapott karakter heal f�ggv�ny�t, ezzel annak testh�j�t n�veli.
	 * Ezut�n elt�vol�tjuk a karaktert�l az �telt.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	public void use(Character c) {
		c.heal();
		c.removeItem(this);
	}

}
