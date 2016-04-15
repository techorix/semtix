/*

  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
         Semesterticketbüro der Humboldt-Universität Berlin

  Copyright (c) 2015 Michael Mertins (MichaelMertins@gmail.com)
  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.semtix.gui.admin.options;

import org.apache.log4j.Logger;
import org.semtix.config.Settings;
import org.semtix.db.DBHandlerConf;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel in den AdminTools für Einstellungen für das Person-Formular.
 *
 */
@SuppressWarnings("serial")
public class OptionPanelPerson
extends JPanel {

	private Logger logger = Logger.getLogger(OptionPanelPerson.class);


    public OptionPanelPerson() {

		final JCheckBox cbBIC, cbBarauszahler, cbSaveOverride;

		this.setLayout(new BorderLayout());
		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel lbBankverbindung = new JLabel("Bankverbindung");
		
		lbBankverbindung.setFont(lbBankverbindung.getFont().deriveFont(Font.BOLD));

		cbBIC = new JCheckBox("BIC (Bank Identifier Code) darf leer bleiben");

        cbBarauszahler = new JCheckBox("KHB-Studenten dürfen keine Barauszahler sein");

		cbSaveOverride = new JCheckBox("Personen ohne Nachfrage speichern");

		cbSaveOverride.setToolTipText("Erlaubt es, (nur für die laufende Sitzung) Personen, ohne Rückfragen zu speichern");

        final DBHandlerConf dbHandlerConf = new DBHandlerConf();

        try {
            cbBIC.setSelected(Boolean.parseBoolean(dbHandlerConf.read("emptyBICAllowed")));
            cbBarauszahler.setSelected(Boolean.parseBoolean(dbHandlerConf.read("keineBarauszahlerKHB")));
			cbSaveOverride.setSelected(Settings.SAVE_PERSON_ANYWAY);
		} catch (Exception e) {
            logger.info("[info] Fehler beim lesen von emptyBICAllowed aus conf-Table");
        }

        JButton buttonSave = new JButton("Speichern");
        buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                dbHandlerConf.update("emptyBICAllowed", cbBIC.isSelected() + "");
                dbHandlerConf.update("keineBarauszahlerKHB", cbBarauszahler.isSelected() + "");
				Settings.SAVE_PERSON_ANYWAY = cbSaveOverride.isSelected();
			}
        });
		
		
		
		SForm mainPanel = new SForm();


		mainPanel.add(lbBankverbindung, 0, 0, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 10, 0));
		mainPanel.add(cbBIC, 0, 1, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		mainPanel.add(cbBarauszahler, 0, 2, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));
		mainPanel.add(cbSaveOverride, 0, 3, 1, 1, 0.0, 0.0, 0, 17, new Insets(0, 0, 0, 0));


        mainPanel.add(buttonSave, 0, 20, 1, 1, 1.0, 1.0, 0, GridBagConstraints.LAST_LINE_START, new Insets(0, 0, 0, 0));

		
		add(mainPanel, BorderLayout.CENTER);
		
	}


}
