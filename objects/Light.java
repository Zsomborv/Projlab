package objects;

import characters.Character;
/**
 * Ez az oszt�ly val�s�tja meg a j�t�kban l�v� jelz�f�nyt. 
 * A pisztolyba szerelve a patronnal egy�tt, majd els�tve megnyerhet� a j�t�k.
 */
public class Light implements Item{
	/**
	 * Jelez a GameWorld-nek, hogy v�ge a j�t�knak az endGame(boolean state) f�ggv�ny megh�v�s�val.
	 * Majd be�ll�tja a karakter energi�j�t 0-ra, �gy a f�ciklus bels� ciklusa ki tud l�pni.
	 */
	public void use(Character c) {
		c.getMyTable().getGameworld().endGame(true);
		c.setEnergy(0);
	}

}
