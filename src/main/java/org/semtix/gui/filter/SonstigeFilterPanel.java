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

import org.semtix.config.SemesterConf;
import org.semtix.config.UniConf;
import org.semtix.db.DBHandlerSemester;
import org.semtix.db.dao.Semester;
import org.semtix.shared.daten.enums.Status;
import org.semtix.shared.elements.SForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Das SonstigeFilterPanel ist Teil des Dialogs zum Filtern nach Anträgen (DialogAntragFilter) und enthält 
 * ComboBoxen, mit denen zusätzliche Filtermöglichkeiten eingestellt werden können.
 * Diese zusätzlichen Filtermöglichkeiten sollen die Hauptfilter ergänzen.
 */
@SuppressWarnings("serial")
public class SonstigeFilterPanel
extends JPanel {

	private final FilterControl filterControl;
	private JComboBox comboSemester, comboKulanz, comboErstsemester, comboBarauszahler, comboTeilzuschuss,
			comboRatenzahlung, comboNachreichung, comboNothilfe, comboSpaetis, comboArchiviert;


    /**
     * Erstellt ein SonstigeFilterPanel
     * @param filterController FilterControl
     */
	public SonstigeFilterPanel(FilterControl filterController) {

		this.filterControl = filterController;

		this.setLayout(new BorderLayout());
		
		JLabel lbTitel = new JLabel("Sonstige Filter");
		lbTitel.setFont(lbTitel.getFont().deriveFont(Font.BOLD));
		
		JLabel lbSemester = new JLabel("Semester");
		JLabel lbKulanz = new JLabel("Kulanz");
		JLabel lbErstsemester = new JLabel("Erstsemester");
		JLabel lbRatenzahlung = new JLabel("Ratenzahlung");
		JLabel lbNachreichung = new JLabel("Ungeprüfte Nachreichung vorhanden");
		JLabel lbNothilfe = new JLabel("Nothilfe");
		JLabel lbSpaetis = new JLabel("Verspätete");
		JLabel lbBarauszahler = new JLabel("Barauszahler_innen");
		JLabel lbArchiviert = new JLabel("Archiviert");
		JLabel lbTeilzuschuss = new JLabel("Teilzuschuss");


		comboSemester = new JComboBox();
		comboKulanz = new JComboBox(Status.values());
		comboErstsemester = new JComboBox(Status.values());
		comboRatenzahlung = new JComboBox(Status.values());
		comboNachreichung = new JComboBox(Status.values());
		comboNothilfe = new JComboBox(Status.values());
		comboSpaetis = new JComboBox(Status.values());
		comboBarauszahler = new JComboBox(Status.values());
		comboArchiviert = new JComboBox(Status.values());
		comboTeilzuschuss = new JComboBox(Status.values());

		comboKulanz.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setKulanz((Status) comboKulanz.getSelectedItem());
			}
		});


		comboErstsemester.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setErstsemester((Status) comboErstsemester.getSelectedItem());
			}
		});

		comboRatenzahlung.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setRatenzahlung((Status) comboRatenzahlung.getSelectedItem());
			}
		});

		comboNachreichung.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setNachreichung((Status) comboNachreichung.getSelectedItem());
			}
		});

		comboNothilfe.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setNothilfe((Status) comboNothilfe.getSelectedItem());
			}
		});

		comboSpaetis.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setSpaetis((Status) comboSpaetis.getSelectedItem());
			}
		});

		comboBarauszahler.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setBarauszahler((Status) comboBarauszahler.getSelectedItem());
			}
		});

		comboArchiviert.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setArchiviert((Status) comboArchiviert.getSelectedItem());
			}
		});

		comboTeilzuschuss.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				filterControl.setTeilzuschuss((Status) comboTeilzuschuss.getSelectedItem());
			}
		});

        SForm mainPanel = new SForm();

		mainPanel.add(lbTitel, 0, 0, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 5, 2, 5));
		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL), 0, 1, 1, 1, 0.0, 0.0, 2, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbSemester,               0, 2, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 8, 2, 5));
		mainPanel.add(comboSemester,            0, 3, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbKulanz,                 0, 4, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 8, 2, 5));
		mainPanel.add(comboKulanz,              0, 5, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbErstsemester,           0, 6, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 8, 2, 5));
		mainPanel.add(comboErstsemester,        0, 7, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbRatenzahlung,           0, 8, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 8, 2, 5));
		mainPanel.add(comboRatenzahlung,        0, 9, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbNachreichung,           0, 10, 1, 1, 0.0, 0.0, 0, 18, new Insets(5, 8, 2, 5));
		mainPanel.add(comboNachreichung,        0, 11, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbNothilfe,				0, 12, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(comboNothilfe, 			0, 13, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbSpaetis,				0, 14, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(comboSpaetis, 			0, 15, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbBarauszahler, 0, 16, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(comboBarauszahler, 0, 17, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbArchiviert, 0, 18, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(comboArchiviert, 0, 19, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(lbTeilzuschuss, 0, 20, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));
		mainPanel.add(comboTeilzuschuss, 0, 21, 1, 1, 0.0, 0.0, 0, 18, new Insets(2, 5, 5, 5));

		this.add(mainPanel);

		// Model für ComboBox Semester zusammestellen

		// Holt die Liste der Semester für Uni
		List<Semester> semesterListe = new DBHandlerSemester().getSemesterListe(UniConf.aktuelleUni);

		Semester selectedSemester = null;

		DefaultComboBoxModel modelSemester = new DefaultComboBoxModel();
		modelSemester.addElement("alle");
		for(Semester s : semesterListe) {
			modelSemester.addElement(s);
			if(s.getSemesterKurzform().equals(SemesterConf.getSemester().getSemesterKurzform()))
				selectedSemester = s;

		}

		comboSemester.setModel(modelSemester);

		// aktuelles Semester in Combobox selektieren
		comboSemester.setSelectedItem(selectedSemester);

		comboSemester.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				Object o = comboSemester.getSelectedItem();
				if (null == o || o instanceof String) {
					filterControl.setSemesterID(-1);
				} else {
					filterControl.setSemesterID(((Semester) o).getSemesterID());
				}
			}
		});
		
	}
	
	
	
	/**
	 * Liefert das in der Combobox selektierte Semester
	 * @return Semester
	 */
    public Object getSemester() {
        return comboSemester.getSelectedItem();
    }

	public Status getErstsemester() {
		return (Status) comboErstsemester.getSelectedItem();
	}

	public Status getKulanz() {
		return (Status) comboKulanz.getSelectedItem();
	}

	public Status getRatenzahlung() {
		return (Status) comboRatenzahlung.getSelectedItem();
	}

	public Status getNachreichung() {
		return (Status) comboNachreichung.getSelectedItem();
	}

	public Status getNothilfe() {
		return (Status) comboNothilfe.getSelectedItem();
	}

	public Status getSpaetis() {
		return (Status) comboSpaetis.getSelectedItem();
	}

	public Status getBarauszahler() {
		return (Status) comboBarauszahler.getSelectedItem();
	}

    public Status getArchiviert() {
        return (Status) comboArchiviert.getSelectedItem();
    }

	public Status getTeilzuschuss() {
		return (Status) comboTeilzuschuss.getSelectedItem();
	}

}
