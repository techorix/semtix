package org.semtix.gui.filter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.semtix.gui.MainControl;

/**
 * Action zum Starten eines FilterDialogs, mit dem nach Anträgen gefiltert werden kann. Diese Action 
 * kann sowohl auf einen Button in der ToolBar als auch in einen Menüpunkt gelegt werden.
 */
@SuppressWarnings("serial")
public class ActionFilterAntraege
extends AbstractAction {
	
	private MainControl mainControl;

	/**
	 * Erstellt eine neue Action
	 * @param text Text der Angezeigt wird
	 * @param mc MainControl
	 */
	public ActionFilterAntraege(String text, MainControl mc) {
		
		this.mainControl = mc;

		putValue(Action.NAME, text);
		putValue(Action.SHORT_DESCRIPTION, "Auswahl filtern");

    }

	/**
	 * Action nach Klick auslösen
	 */
    public void actionPerformed(ActionEvent event) {

    	// Filterdialog öffnen (PagingControl = null)
    	new FilterControl(null, mainControl);

    }


}
