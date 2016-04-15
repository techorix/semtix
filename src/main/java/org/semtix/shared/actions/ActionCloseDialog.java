package org.semtix.shared.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;


/**
 * Action zum Schließen/Beenden von Dialogen. Dem Konstruktor dieser Action kann der
 * zu schließende Dialog sowie ein Text für eine Buttonbeschriftung übergeben werden. 
 * Durch Klick auf den Button wird dann der Dialog geschlossen.
 * 
 * <p>Beispiel eines Button mit Beschriftung "Beenden":</p>
 * <pre>
 * {@code
 *   JButton closeButton = new JButton(new ActionCloseDialog(dialog, "Beenden"))
 * }
 * </pre>
 */
@SuppressWarnings("serial")
public class ActionCloseDialog
extends AbstractAction {

	private JDialog dialog;
	
	/**
	 * Erstellt eine Action zum Schließen/Beenden von Dialogen.
	 * @param dialog der zu schließende Dialog
	 * @param text Beschriftung für Menü oder Button
	 */
	public ActionCloseDialog(JDialog dialog, String text) {
		
		this.dialog = dialog;
		
		putValue(Action.NAME, text);
		putValue(Action.SHORT_DESCRIPTION, "Dialog schließen");

    }
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.dispose();		
	}

}
