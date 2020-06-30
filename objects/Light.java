package objects;

import characters.Character;
/**
 * Ez az osztály valósítja meg a játékban lévõ jelzõfényt. 
 * A pisztolyba szerelve a patronnal együtt, majd elsütve megnyerhetõ a játék.
 */
public class Light implements Item{
	/**
	 * Jelez a GameWorld-nek, hogy vége a játéknak az endGame(boolean state) függvény meghívásával.
	 * Majd beállítja a karakter energiáját 0-ra, így a fõciklus belsõ ciklusa ki tud lépni.
	 */
	public void use(Character c) {
		c.getMyTable().getGameworld().endGame(true);
		c.setEnergy(0);
	}

}
