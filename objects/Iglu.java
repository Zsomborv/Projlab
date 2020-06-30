package objects;

import game.IceTable;

import java.io.Serializable;

import characters.Character;

/**Iglu oszt�ly
 * Felel�s
 * * saj�t �let�nek t�rol�s��rt
 * * saj�t j�gt�bl�j�nak t�rol�s��rt
 * * adott j�gt�bl�n meg�p�l�s�rt
 * * �nmaga lebont�s�rt
 * * getter/setterek defini�l�s��rt
 * 
 * @author BCSV
 */
public class Iglu implements Serializable{
	
	/**H�tral�v� id� t�rol�sa
	 */
	private int lifeTime;
	/**Saj�t j�gt�bla elt�rol�sa
	 */
	private IceTable myTable;
	
	/**Param�ter n�lk�li konstruktor
	 * be�ll�tja a h�tral�v� id�t 4-re
	 */
	public Iglu() {
		lifeTime = 4;
	}
	
	/**Meg�p�l�s
	 * Saj�t t�bl�nak �tadja mag�t 
	 */
	public void build(){
		myTable.addIglu(this);
	}
	
	/**Az iglu bont�sa param�terben kapott �rt�kkel
	 * H�tral�v� id� cs�kkent�se
	 * Ellen�rzini, hogy nem fogyott-e el, ha igen, megsemmis�l�s
	 * @param dmg - A bont�s m�rt�ke, eg�sz sz�mk�nt
	 */
	public void destroy(int dmg) {
		lifeTime -= dmg;
		if(lifeTime < 1) {
			myTable.removeIglu();
		}
	}
	/**Getter f�ggv�ny a h�tral�v� id�re
	 * @return - eg�sz sz�mk�nt a h�tral�v� id�
	 */
	public int getLifeTime() {
		return lifeTime;
	}
	
	/**Setter f�ggv�ny a h�tral�v� id�re
	 * @param lt - Int objektum, amit be�ll�tunk saj�t �letnek/id�nek
	 */
	public void setLifeTime(int lt) {
		lifeTime = lt;
	}
	
	/**Getter f�ggv�ny a saj�t j�gt�bl�ra
	 * @return - IceTable objektumk�nt a saj�t j�gt�bla
	 */
	public IceTable getMyTable() {
		return myTable;
	}
	
	/**Setter f�ggv�ny a saj�t j�gt�bl�ra
	 * @param it - IceTable objektum, amit be�ll�tunk saj�t j�gt�blak�nt
	 */
	public void setMyTable(IceTable it) {
		myTable = it;
	}

}
