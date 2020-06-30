package objects;

import characters.Character;
/**
 * Ez az osztály valósítja meg a játékban a kötelet, melynek segítségével egy játékos kimentheti egy vízbeesett társát.
 */
public class Rope implements Item{
	
	/**Ha egy karakter használja, a táblájára húzhatja egy mellette lévõ játékost.
	 * Lekéri a viszonylagos irányt a másik táblához képest, majd az alapján átlépteti a szomszédos lévõ játékost a miénkre
	 * Ha a játékos vízben volt, leállítja a körszámlálót, ami alapján meghalna a játékos
	 * @param c - A karakter aki használja
	 */
	public void use(Character c) {		
		int dir = c.getStepDirection();
		int myindex= c.getMyTable().getNeighbour(dir).getNeighbours().indexOf(c.getMyTable());
		c.getMyTable().getNeighbour(dir).getPerson(0).step(myindex);	
		c.getMyTable().getGameworld().setInWater(false);
	}

}
