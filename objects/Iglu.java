package objects;

import game.IceTable;

import java.io.Serializable;

import characters.Character;

/**Iglu osztály
 * Felelõs
 * * saját életének tárolásáért
 * * saját jégtáblájának tárolásáért
 * * adott jégtáblán megépülésért
 * * önmaga lebontásért
 * * getter/setterek definiálásáért
 * 
 * @author BCSV
 */
public class Iglu implements Serializable{
	
	/**Hátralévõ idõ tárolása
	 */
	private int lifeTime;
	/**Saját jégtábla eltárolása
	 */
	private IceTable myTable;
	
	/**Paraméter nélküli konstruktor
	 * beállítja a hátralévõ idõt 4-re
	 */
	public Iglu() {
		lifeTime = 4;
	}
	
	/**Megépülés
	 * Saját táblának átadja magát 
	 */
	public void build(){
		myTable.addIglu(this);
	}
	
	/**Az iglu bontása paraméterben kapott értékkel
	 * Hátralévõ idõ csökkentése
	 * Ellenõrzini, hogy nem fogyott-e el, ha igen, megsemmisülés
	 * @param dmg - A bontás mértéke, egész számként
	 */
	public void destroy(int dmg) {
		lifeTime -= dmg;
		if(lifeTime < 1) {
			myTable.removeIglu();
		}
	}
	/**Getter függvény a hátralévõ idõre
	 * @return - egész számként a hátralévõ idõ
	 */
	public int getLifeTime() {
		return lifeTime;
	}
	
	/**Setter függvény a hátralévõ idõre
	 * @param lt - Int objektum, amit beállítunk saját életnek/idõnek
	 */
	public void setLifeTime(int lt) {
		lifeTime = lt;
	}
	
	/**Getter függvény a saját jégtáblára
	 * @return - IceTable objektumként a saját jégtábla
	 */
	public IceTable getMyTable() {
		return myTable;
	}
	
	/**Setter függvény a saját jégtáblára
	 * @param it - IceTable objektum, amit beállítunk saját jégtáblaként
	 */
	public void setMyTable(IceTable it) {
		myTable = it;
	}

}
