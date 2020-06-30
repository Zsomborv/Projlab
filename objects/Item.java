package objects;
import java.io.Serializable;

import characters.Character;
/**
 * Ez az oszt�ly a j�t�kban tal�lhat� k�l�nb�z� haszn�lhat� t�rgyak interf�sze.
 */
public interface Item extends Serializable {
	/**
	 * Egy t�rgy haszn�lat�t megval�s�t� f�ggv�ny. Minden t�rgyn�l fel�l�r�sra ker�l.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	public void use(Character c);
}
