package characters;

/**Sarkkutató osztály a karaktertõl örököl
 * Szomszéd tábla vizsgálásáért felelõs
 * Minden mást a characteren keresztül,
 * * örökölt metódusokkal valósít meg
 * 
 * @author BCSV
 */
public class Researcher extends Character{
	
	/**Paraméter nélkül konstruktor
	 * Létrehoz egy 4 testhõjû karaktert
	 * * az örökölt konstuktor segítségével
	 */
	public Researcher() {
		super(4);
	}

	/**A szomszédos jégtábla teherbírásának megmérése
	 * Egy energiába kerül
	 * a sarkkutató lekérdezi az adott irányba lévõ táblát
	 * majd tõle a saját erejét
	 * @param dir - Az irány radiánban megadva (double változó)
	 * @return - A teherbírás egész számként
	 */
	public int checkStrength(int dir) {	
		this.setEnergy(this.getEnergy()-1);
		return this.getMyTable().getNeighbour(dir).getStrength();
	}
}
