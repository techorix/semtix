package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import org.semtix.gui.tabs.antrag.AntragControl;

import javax.swing.*;
import java.awt.event.ActionEvent;
//import persontab.antrag.AntragControl;

/**
 * Action öffnet Dialog zum Löschen von bestehenden fehlenden Unterlagen
 */
@SuppressWarnings("serial")
public class ActionUnterlagenLoeschen
extends AbstractAction {

	private AntragControl antragControl;
	private UnterlagenTable table;
	
	
	/**
	 * Erstellt neue Action
	 * @param antragControl AntragControl
	 * @param table Tabelle mit Unterlagenübersicht
	 */
	public ActionUnterlagenLoeschen(AntragControl antragControl, UnterlagenTable table){

		this.antragControl = antragControl;
		this.table = table;
		
		putValue(Action.NAME, "Eintrag löschen");
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Optionen zur Auswahl festlegen (0, 1, 2 - von rechts nach links ausgerichtet!)
		Object[] options = {"Abbrechen", "Löschen"};
		
		String text = "Soll der Eintrag wirklich gelöscht werden?";
		
		// OptionPane mit Rückgabewert anzeigen
		int selected = JOptionPane.showOptionDialog(null, text, "Hinweis", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		// Reaktion je nach Auswahl der Option
		if(selected == 1) {
			antragControl.deleteUnterlagen(table.getSelectedRow());
        }

    }

}
