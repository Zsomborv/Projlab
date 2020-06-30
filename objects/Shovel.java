package objects;

import characters.Character;
/**
 * A j�t�kban szerepl� lap�tot megval�s�t� oszt�ly. 
 * Ennek a seg�ts�g�vel egy j�t�k egy energia felhaszn�l�s�val k�t egys�g havat lap�tolhat el.
 */
public class Shovel implements Item{
	/**
	 * A f�ggv�ny lek�rdezi a param�terk�nt kapott karakter j�gt�bl�j�nak snow �s ice attrib�tum�t 
	 * (int getSnow() �s boolean getIce() f�ggv�nyek h�v�sa). 
	 * Ezut�n amennyiben a h� legal�bb k�t egys�gnyi vastag, �gy kett�vel cs�kkentj�k a j�gt�bla 
	 * snow attrib�tum�nak �rt�k�t (void setSnow() f�ggv�ny seg�ts�g�vel), ha csak egy egys�gnyi vastag, 
	 * akkor eggyel cs�kkentj�k a v�ltoz� �rt�k�t. Amennyiben nincs h� a j�gt�bl�n (snow �rt�ke 0) 
	 * �s a t�bla be van fagyva (ice �rt�ke true), 
	 * akkor felt�rj�k a jeget(ice �rt�k�t false-ra �ll�tjuk a void setIce(boolean ice) f�ggv�ny seg�ts�g�vel).
	 * @param c - A karakter amely haszn�lja az adott t�rgyat.
	 */
	public void use(Character c) {
		
		int snow = c.getMyTable().getSnow();
		boolean ice = c.getMyTable().isFrozen();
		
		if(snow >1) {
			c.getMyTable().setSnow(snow-2);
		} else if(snow==1) {
			c.getMyTable().setSnow(snow-1);
		} else if(snow == 0 && ice) {
			c.getMyTable().setIce(false);
		}	
	}
}
