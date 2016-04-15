package org.semtix.gui.tabs.antrag.nachreichungen.tabelle;

import org.semtix.db.dao.Unterlagen;
import org.semtix.gui.tabs.antrag.AntragControl;
import org.semtix.shared.textbausteine.DialogUnterlagen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Action öffnet Dialog zum Ändern von bestehenden fehlenden Unterlagen
 */
@SuppressWarnings("serial")
public class ActionUnterlagenAendern
extends AbstractAction {

	private AntragControl antragControl;
	private UnterlagenTable table;
	
	
	/**
	 * Erstellt neue Action
	 * @param antragControl AntragControl
	 * @param table Tabelle mit Unterlagenübersicht
	 */
	public ActionUnterlagenAendern(AntragControl antragControl, UnterlagenTable table){

		this.antragControl = antragControl;
		this.table = table;

		
		putValue(Action.NAME, "Eintrag ändern");
		
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		List<Unterlagen> listeUnterlagen = new ArrayList<Unterlagen>();
		listeUnterlagen.add(antragControl.getTableModelUnterlagen().getUnterlagen(table.getSelectedRow()));
		new DialogUnterlagen(antragControl, listeUnterlagen);
		
	}

}
