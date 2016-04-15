package org.semtix.gui.tabs.antrag.nachreichungen.pruefen;

import java.util.HashMap;
import java.util.Map;

/**
 * Konstante zur Art der ungeprüften Nachreichung. Es kann angegeben werden, ob die NAchreichung in 
 * Papierform, als Email oder als ausgedruckte Email vorliegt.
 */
public enum NachreichungArt {
	
	
	PAPER (1, "Papier"),
    EMAIL (2, "Email"),
	PRINTED_EMAIL (3, "ausgedruckte Email");

    private static Map<Integer, NachreichungArt> idToNachreichungArtMapping;
    private int nachreichungArtID;
	private String nachreichungArtText;
	
	
	private NachreichungArt(int nachreichungArtID, String nachreichungArtText) {
		
		this.nachreichungArtID = nachreichungArtID;
		this.nachreichungArtText = nachreichungArtText;

	}
	
	
	
	/**
	 * Liefert NachreichungArt anhand der nachreichungArtID
	 * @param i Index der Nachreichungsart
	 * @return Nachreichungsart
	 */
	public static NachreichungArt getNachreichungArt(int i) {
		if(idToNachreichungArtMapping == null) {
			initMapping();
		}
		return idToNachreichungArtMapping.get(i);
	}
	
	
	// schreibt nachreichungArtID und NachreichungArt in eine Map, damit auch über
	// die ID auf die NachreichungArt zugegriffen werden kann
	private static void initMapping() {
		idToNachreichungArtMapping = new HashMap<Integer, NachreichungArt>();
		for (NachreichungArt n : values()) {
			idToNachreichungArtMapping.put(n.nachreichungArtID, n);
		}
	}
	
	
	public int getNachreichungArtID() {
		return nachreichungArtID;
	}
	
	
	public String getNachreichungArtText() {
		return nachreichungArtText;
	}


}
