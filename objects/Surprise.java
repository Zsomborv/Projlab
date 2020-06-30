package objects;

import java.util.Random;

import characters.Character;
/**
 * A játékban lévõ meglepetés tárgyat megvalósító osztály. Ennek a megtalálásával a játékos különbözõ “képességet” kap.
 */
public class Surprise implements Item{
	private int randnum;
	private int getRandNum()
	{
		return randnum;
	}
	/**
	 * A függvény hívásánál a paraméterként kapott karakterre fejt ki egy különleges hatást.
	 * Minden hívásnál sorsol egy véletlenszerû hatást, amivel a játékos segítve vagy valamilyen szinten hátráltatva lesz.
	 * Ez a hatás csak a karakterten fejtõdik ki, pl.: életcsökkentés, életnövelés stb.
	 * @param c - A karakter amely használja az adott tárgyat.
	 */
	public void use(Character c) {
		// TODO
		Random random = new Random();
		int rn = random.nextInt(4);
		switch(rn) {
			case 0: c.setEnergy(c.getEnergy()+3);
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("Felvetted a Dunát, ami megemelte az átlagodat. Plusz 3 energiát kapsz a körben!");
				randnum = rn;
				break;
			}
				
			case 1: c.setLife(c.getMaxLife());
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("Méltányosságidat elhasználva, újra max életed lesz!");
				randnum = rn;
				break;
			}
			case 2: c.heal();
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("Jó csapattársakat kaptál projlabon. Kapsz +1 életet!");
				randnum = rn;
				break;
			}
			case 3: c.setEnergy(0);
			{
				c.getMyTable().getGameworld().getView().setSurprisestate("Felvetted idén a Grafikát és az Adatb-t egyszerre. A körben nincs több energiád! :(");
				randnum = rn;
				break;
			}
		}
		c.removeItem(this);
	}

}
