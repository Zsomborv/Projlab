package objects;

import characters.Character;
/**
 * A j�t�kban megtal�lhat� b�v�rruh�t megval�s�t� oszt�ly. Aki ezzel rendelkezik, az megmenek�l a v�zbees�sn�l a megfullad�st�l.
 */
public class Swimsuit implements Item{
	/**
	 * Megh�vja a param�terk�nt kapott karakter void setProtected(boolean prot) f�ggv�ny�t, 
	 * mellyel a karakter boolean protected v�ltoz�j�t igazra �ll�tja.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	public void use(Character c) {
		c.setProtect(true);		
	}

}