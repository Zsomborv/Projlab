package characters;

import java.io.Serializable;

import game.IceTable;

/**Stepable abszrakt oszt�ly, Serializable-t megval�s�tja
 * felel�s:
 * * a saj�t j�gt�bla t�rol�s��rt
 * * a l�ptethet� objektumok minden f�ggv�ny�nek deklar�l�s��rt
 * * getter/setter f�ggv�nyek �s l�p�s met�dus defini�l�s��rt
 * 
 * @author BCSV
 */
public abstract class Stepable implements Serializable {

	/**Saj�t IceTable objektum elt�rol�sa priv�t l�that�s�ggal
	 */
	private IceTable myTable;

	/**Getter f�ggv�ny a myTable attrib�tumra
	 * @return - IceTable objektum (myTable)
	 */
	public IceTable getMyTable() {
		return myTable;
	}

	/**Setter f�ggv�ny a myTable attrib�tumra
	 * @param myTable - IceTable objektum, amire a myTable �ll�tva lesz
	 */
	public void setMyTable(IceTable myTable) {
		this.myTable = myTable;
	}
	
	/**Egy l�p�s levez�nyl�se
	 * Adott ir�nyban tal�lhat� szomsz�d elk�r�se
	 * �tl�p�s levez�nyl�se
	 * * k�veket� t�bl�nak odaadni a Stepable objektumot
	 * * jelenlegi t�bl�r�l kiszedni a Stepable objektumot
	 * @param dir - Radi�nban az ir�ny (double)
	 */
	public void step(int dir) {
		if(myTable == null) {
			System.out.println("Nincs myTable");
		}
		myTable.getNeighbour(dir).addPerson(this);
		myTable.removePerson(this);
		this.setMyTable(myTable.getNeighbour(dir));
	}
	
	/**Absztrakt k�vetkez� k�r f�ggv�ny
	 * Defini�lni kell az �r�kl�nek
	 * Egy k�r eltelt�vel mi kell t�rt�njen az adott objektummal
	 */
	abstract public void nextRound();

	/**Absztrakt vihar f�ggv�ny
	 * Defini�lni kell az �r�kl�nek
	 * Egy vihar eset�n mi kell t�rt�njen az adott objektummal
	 */
	abstract public void storm();
}
