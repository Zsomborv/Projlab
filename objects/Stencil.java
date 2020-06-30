package objects;

import characters.Character;
/**
 * A j�t�kban megtal�lhat� patron megval�s�t�s��rt felel�s oszt�ly. 
 * A pisztolyba t�ltve a jelz�f�nnyel egy�tt megnyerhet� a j�t�k.
 */
public class Stencil implements Item{
	/**
	 * A f�ggv�ny megh�v�s�n�l hasonl�an m�k�dik, mint a Gun use f�ggv�nye.
	 * A param�terk�nt kapott karakter j�gt�bl�j�n kereszt�l lek�rdezi az aktu�lis j�t�kteret(GameWorld),
	 * majd ezen j�t�kt�r minden j�t�kos�n v�gigiter�lva lellen�rzi, hogy valakin�l megtal�lhat�-e a fegyver k�vetkez� sz�ks�ges
	 * �p�t�eleme a patron(Stencil). Amennyiben igen. akkor megh�vja annak a void use(Character c) f�ggv�ny�t.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat. 
	 */
	public void use(Character c) {
		for(int j = 0; j < c.getMyTable().getGameworld().getPlayers().size(); j++) {
			for(int i = 0; i < c.getMyTable().getGameworld().getPlayers().get(j).getItems().size(); i++) {
				if(c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).getClass().equals(objects.Light.class)) {
					c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).use(c);
				}
			}
		}
	}

}