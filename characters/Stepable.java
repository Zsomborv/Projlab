package characters;

import java.io.Serializable;

import game.IceTable;

/**Stepable abszrakt osztály, Serializable-t megvalósítja
 * felelõs:
 * * a saját jégtábla tárolásáért
 * * a léptethetõ objektumok minden függvényének deklarálásáért
 * * getter/setter függvények és lépés metódus definiálásáért
 * 
 * @author BCSV
 */
public abstract class Stepable implements Serializable {

	/**Saját IceTable objektum eltárolása privát láthatósággal
	 */
	private IceTable myTable;

	/**Getter függvény a myTable attribútumra
	 * @return - IceTable objektum (myTable)
	 */
	public IceTable getMyTable() {
		return myTable;
	}

	/**Setter függvény a myTable attribútumra
	 * @param myTable - IceTable objektum, amire a myTable állítva lesz
	 */
	public void setMyTable(IceTable myTable) {
		this.myTable = myTable;
	}
	
	/**Egy lépés levezénylése
	 * Adott irányban található szomszéd elkérése
	 * átlépés levezénylése
	 * * köveketõ táblának odaadni a Stepable objektumot
	 * * jelenlegi tábláról kiszedni a Stepable objektumot
	 * @param dir - Radiánban az irány (double)
	 */
	public void step(int dir) {
		if(myTable == null) {
			System.out.println("Nincs myTable");
		}
		myTable.getNeighbour(dir).addPerson(this);
		myTable.removePerson(this);
		this.setMyTable(myTable.getNeighbour(dir));
	}
	
	/**Absztrakt következõ kör függvény
	 * Definiálni kell az öröklõnek
	 * Egy kör elteltével mi kell történjen az adott objektummal
	 */
	abstract public void nextRound();

	/**Absztrakt vihar függvény
	 * Definiálni kell az öröklõnek
	 * Egy vihar esetén mi kell történjen az adott objektummal
	 */
	abstract public void storm();
}
