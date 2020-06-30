package objects;

import characters.Character;
/**
 * Ez az oszt�ly val�s�tja meg a pisztolyt a j�t�kban. 
 * A pisztoly a j�t�k megnyer�s�hez sz�ks�ges a patron �s a jelz�f�ny mellett.
 */
public class Gun implements Item{
	/**
	 * A f�ggv�ny megh�v�s�n�l a param�terk�nt kapott karakter j�gt�bl�j�n kereszt�l lek�rdezi az aktu�lis j�t�kteret(GameWorld),
	 * majd ezen j�t�kt�r minden j�t�kos�n v�gigiter�lva leellen�rzi, hogy valakin�l megtal�lhat�-e a fegyver k�vetkez� sz�ks�ges
	 * �p�t�eleme a patron(Stencil). Amennyiben igen. akkor megh�vja annak a void use(Character c) f�ggv�ny�t.
	 * @param c - A karakter amely haszn�lja az adott t�rgyat. 
	 */
	public void use(Character c) {
		for(int j = 0; j < c.getMyTable().getGameworld().getPlayers().size(); j++) {
			for(int i = 0; i < c.getMyTable().getGameworld().getPlayers().get(j).getItems().size(); i++) {
				if(c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).getClass().equals(objects.Stencil.class)) {
					c.getMyTable().getGameworld().getPlayers().get(j).getItems().get(i).use(c);
				}
			}
		}
	}
}