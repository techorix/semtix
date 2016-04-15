/*
 *
 *  Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *         Semesterticketbüro der Humboldt-Universität Berlin
 *
 *  Copyright (c) 2015-2016 Michael Mertins (MichaelMertins@gmail.com)
 *  2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.semtix.gui.filter;

import org.semtix.config.UserConf;
import org.semtix.db.DBHandlerUser;
import org.semtix.db.dao.SemtixUser;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;


/**
 * Panel mit Anfangsbuchstaben und Checkboxen zur Filterauswahl, welches im FilterDialog angezeigt wird.
 */
@SuppressWarnings("serial")
public class BuchstabenPanel
extends JPanel {
	
	private final String[] buchstaben = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private final FilterControl filterControl;
	private SForm buchstabenPanel;
	private JCheckBox[] cbArray;


    /**
     * Erstellt BuchstabenPanel
     * @param filterController FilterControl
     */
	public BuchstabenPanel(FilterControl filterController) {

		this.filterControl = filterController;

		if (null == UserConf.CURRENT_USER || null == UserConf.CURRENT_USER.getKuerzel() || null == UserConf.CURRENT_USER.getBuchstaben() || null == UserConf.CURRENT_USER.getLoginName() || null == UserConf.CURRENT_USER.getKuerzel())
			JOptionPane.showMessageDialog(null, "Aktueller Benutzer muss noch ausreichend konfiguriert werden, bevor dieses Feature benutzt werden kann.");

		else {
			this.setLayout(new BorderLayout());

			//setBackground(new Color(230,230,230));

			SForm mainPanel = new SForm();
			mainPanel.setBackground(new Color(230, 230, 230));

			buchstabenPanel = new SForm();
			buchstabenPanel.setBackground(new Color(230, 230, 230));

			// Liste der User (Büromitarbeiter_innen) aus Datenbank auslesen
			List<SemtixUser> userListe = new DBHandlerUser().getUserListe();


			// Model für ComboBox (Buchstabenverteilung) zusammenstellen
			DefaultComboBoxModel modelBuchstabenverteilung = new DefaultComboBoxModel();
			// ComboBox-Eintrag für alle Buchstaben-Checkboxen aktiviert
			modelBuchstabenverteilung.addElement("alle Buchstaben");
			// ComboBox-Eintrag für keine Buchstaben-Checkboxen aktiviert
			modelBuchstabenverteilung.addElement("keine Buchstaben");
			// weitere ComboBox-Einträge für Buchstabenverteilung der User
			for (SemtixUser u : userListe) {
				if (null != u.getBuchstaben() && !u.getBuchstaben().isEmpty() && !u.getBuchstaben().equals("")) {
					String letterDispersion = u.getBuchstaben() + " (Kürzel: " + u.getKuerzel() + ")";
					modelBuchstabenverteilung.addElement(letterDispersion);
					if (u.getLoginName().equals(UserConf.CURRENT_USER.getLoginName())) {
						modelBuchstabenverteilung.setSelectedItem(letterDispersion);
					}
				}
			}

			JComboBox comboBuchstabenverteilung = new JComboBox();

			// Der Combobox wird das Model (Buchstabenverteilung) zugewiesen
			comboBuchstabenverteilung.setModel(modelBuchstabenverteilung);

			// ActionListener auf ComboBox (selektiert bestimmte Checkboxen im BuchstabenPanel, je nach Buchstabenverteilung)
			comboBuchstabenverteilung.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					JComboBox cb = (JComboBox) e.getSource();

					if (cb.getSelectedItem().equals("alle Buchstaben"))
						// alle Checkboxen selektieren
						selectAllCheckboxes(true);
					else if (cb.getSelectedItem().equals("keine Buchstaben"))
						// alle Checkboxen deselektieren
						selectAllCheckboxes(false);
					else {
						// bestimmte Checkboxen selektieren
						String letterdispersion = (String) cb.getSelectedItem();
						letterdispersion = letterdispersion.substring(0, letterdispersion.indexOf('('));
						selectCustomCheckboxes(letterdispersion);
					}
				}
			});


			cbArray = new JCheckBox[buchstaben.length];

			int count = 0;
			int x = 0;
			int y = 0;


			for (String s : buchstaben) {

				JCheckBox cb = new JCheckBox(s);


				if (UserConf.CURRENT_USER.getBuchstaben().toUpperCase().contains(s))
					cb.setSelected(true);

				cbArray[count] = cb;
				buchstabenPanel.add(cb, x, y, 1, 1, 0.0, 0.0, 0, 17, new Insets(5, 8, 5, 8));

				count++;
				x++;

				if (count % 13 == 0) {
					y++;
					x = 0;
				}
			}

			filterControl.setBuchstaben(getSelectedLetters());


			for (JCheckBox cb : cbArray) {
				cb.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent itemEvent) {
						filterControl.setBuchstaben(getSelectedLetters());
					}
				});
			}

			JLabel lbTitel = new JLabel("Filtern nach Anfangsbuchstaben (Nachnamen)");
			lbTitel.setFont(lbTitel.getFont().deriveFont(Font.BOLD));

			mainPanel.add(lbTitel, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 2, 5));
			mainPanel.add(new JSeparator(JSeparator.HORIZONTAL), 0, 1, 1, 1, 0.0, 0.0, 2, 18, new Insets(2, 5, 5, 5));
			mainPanel.add(comboBuchstabenverteilung, 0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 5, 5));
			mainPanel.add(buchstabenPanel, 0, 3, 1, 1, 1.0, 1.0, 0, 18, new Insets(5, 5, 5, 5));

			this.add(mainPanel);
		}

	}
	
	
	
	/**
	 * Selektion aller Checkboxen ändern
	 * @param state alle Checkboxen selektieren ja/nein
	 */
	public void selectAllCheckboxes(boolean state) {
		
		for(JCheckBox cb : cbArray)
			cb.setSelected(state);
		
	}
	
	
	
	/**
	 * Selektion bestimmter Checkboxen ändern
	 * @param letters Buchstaben deren Checkboxen selektiert werden sollen
	 */
	public void selectCustomCheckboxes(String letters) {
		
		// alle Checkboxen durchiterieren
		for(JCheckBox cb : cbArray) {
            // wenn Buchstabe in letters selektieren
            if (letters.contains(cb.getText()))
                cb.setSelected(true);
                // wenn nicht, dann deselektieren
            else
                cb.setSelected(false);
        }
	}
		
	
	
	/**
	 * Liefert String mit den selektierten Buchstaben
	 * @return selektierte Buchstaben
	 */
	private String getSelectedLetters() {

		// Stringbuilder enthält Anfangsbuchstaben der selektierten Checkboxen
		StringBuilder result = new StringBuilder();
		
		// Zähler für selektierte Checkboxen (Anfangsbuchstaben)
		int count = 0;

		for (JCheckBox cb : cbArray) {
			if(cb.isSelected()) {
				result.append(cb.getText());
				count++;
			}
		}
		  
		// keine Checkbox ist selektiert
		if(count == 0)
			return "keine";
		// alle Checkboxen sind selektiert
		else if(count == cbArray.length)
			return "alle";
		// einige der 26 Checkboxen (Anfangsbuchstaben) sind selektiert
		else
			return result.toString();

	}
	


}
