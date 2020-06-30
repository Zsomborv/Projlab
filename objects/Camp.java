package objects;

import characters.Character;
/**
 * Ez az oszt�ly val�s�tja meg a s�trat a j�t�kban. A s�tor egy ki�shat� t�rgy, mely hasonl�an viselkedik, 
 * mint az Iglu, viszont csak egy karakter f�r el benne, nem v�d a medv�t�l �s egy k�r ut�n megsemmis�l.
 */
public class Camp extends Iglu implements Item{
	/**
	 * A s�tor meg�p�l�s�rt felel�s f�ggv�ny. Be�ll�tja a s�tor mez�j�t a karakter aktu�lis mez�j�re,
	 * majd megh�vja az �soszt�ly build f�ggv�ny�t. Be�ll�todik a s�tor �lete kett�re, majd elt�vol�tjuk a karaktert�l a s�trat.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	public void use(Character c) {
		this.setMyTable(c.getMyTable());
		super.build();
		this.setLifeTime(2);
		c.removeItem(this);
	}

}
