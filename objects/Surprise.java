package objects;

import java.util.Random;

import characters.Character;
/**
 * A j�t�kban l�v� meglepet�s t�rgyat megval�s�t� oszt�ly. Ennek a megtal�l�s�val a j�t�kos k�l�nb�z� �k�pess�get� kap.
 */
public class Surprise implements Item{
	private int randnum;
	private int getRandNum()
	{
		return randnum;
	}
	/**
	 * A f�ggv�ny h�v�s�n�l a param�terk�nt kapott karakterre fejt ki egy k�l�nleges hat�st.
	 * Minden h�v�sn�l sorsol egy v�letlenszer� hat�st, amivel a j�t�kos seg�tve vagy valamilyen szinten h�tr�ltatva lesz.
	 * Ez a hat�s csak a karakterten fejt�dik ki, pl.: �letcs�kkent�s, �letn�vel�s stb.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	public void use(Character c) {
		// TODO
		Random random = new Random();
		int rn = random.nextInt(4);
		switch(rn) {
			case 0: c.setEnergy(c.getEnergy()+3);
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("Felvetted a Dun�t, ami megemelte az �tlagodat. Plusz 3 energi�t kapsz a k�rben!");
				randnum = rn;
				break;
			}
				
			case 1: c.setLife(c.getMaxLife());
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("M�lt�nyoss�gidat elhaszn�lva, �jra max �leted lesz!");
				randnum = rn;
				break;
			}
			case 2: c.heal();
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("J� csapatt�rsakat kapt�l projlabon. Kapsz +1 �letet!");
				randnum = rn;
				break;
			}
			case 3: c.setEnergy(0);
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("Felvetted id�n a Grafik�t �s az Adatb-t egyszerre. A k�rben nincs t�bb energi�d! :(");
				randnum = rn;
				break;
			}
		}
		c.removeItem(this);
	}

}
