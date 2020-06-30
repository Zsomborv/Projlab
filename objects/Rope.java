package objects;

import characters.Character;
/**
 * Ez az oszt�ly val�s�tja meg a j�t�kban a k�telet, melynek seg�ts�g�vel egy j�t�kos kimentheti egy v�zbeesett t�rs�t.
 */
public class Rope implements Item{
	
	/**Ha egy karakter haszn�lja, a t�bl�j�ra h�zhatja egy mellette l�v� j�t�kost.
	 * Lek�ri a viszonylagos ir�nyt a m�sik t�bl�hoz k�pest, majd az alapj�n �tl�pteti a szomsz�dos l�v� j�t�kost a mi�nkre
	 * Ha a j�t�kos v�zben volt, le�ll�tja a k�rsz�ml�l�t, ami alapj�n meghalna a j�t�kos
	 * @param c - A karakter aki haszn�lja
	 */
	public void use(Character c) {		
		int dir = c.getStepDirection();
		int myindex= c.getMyTable().getNeighbour(dir).getNeighbours().indexOf(c.getMyTable());
		c.getMyTable().getNeighbour(dir).getPerson(0).step(myindex);	
		c.getMyTable().getGameworld().setInWater(false);
	}

}
