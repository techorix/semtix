package org.semtix.shared.bearbeitungsprotokoll;


/**
 * ControlKlasse für das Bearbeitungsprotokoll, erstellt die Klassen mit den Daten (Model) und 
 * mit der View (Dialog).
 *
 */
public class ProtokollControl {
	
	private ProtokollModel protokollModel;
	private DialogProtokoll dialogProtokoll;
	
	
	/**
	 * Erstellt eine ControlKlasse für das Bearbeitungsprotokoll
	 * @param antragID ID des Antrags
	 */
	public ProtokollControl(int antragID) {
		
		// Model für die Bearbeitungsprotokoll erstellen
		protokollModel = new ProtokollModel();
		
		// View für die Bearbeitungsprotokoll erstellen
		dialogProtokoll = new DialogProtokoll();
		
		// View als Observer beim Model anmelden
		protokollModel.addObserver(dialogProtokoll);
		
		// Antrag-ID im Model setzen
		protokollModel.setAntragsID(antragID);
		
		// Dialog sichtbar machen
		dialogProtokoll.setVisible(true);
		
	}

}
