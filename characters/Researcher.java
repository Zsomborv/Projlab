package characters;

/**Sarkkutat� oszt�ly a karaktert�l �r�k�l
 * Szomsz�d t�bla vizsg�l�s��rt felel�s
 * Minden m�st a characteren kereszt�l,
 * * �r�k�lt met�dusokkal val�s�t meg
 * 
 * @author BCSV
 */
public class Researcher extends Character{
	
	/**Param�ter n�lk�l konstruktor
	 * L�trehoz egy 4 testh�j� karaktert
	 * * az �r�k�lt konstuktor seg�ts�g�vel
	 */
	public Researcher() {
		super(4);
	}

	/**A szomsz�dos j�gt�bla teherb�r�s�nak megm�r�se
	 * Egy energi�ba ker�l
	 * a sarkkutat� lek�rdezi az adott ir�nyba l�v� t�bl�t
	 * majd t�le a saj�t erej�t
	 * @param dir - Az ir�ny radi�nban megadva (double v�ltoz�)
	 * @return - A teherb�r�s eg�sz sz�mk�nt
	 */
	public int checkStrength(int dir) {	
		this.setEnergy(this.getEnergy()-1);
		return this.getMyTable().getNeighbour(dir).getStrength();
	}
}
