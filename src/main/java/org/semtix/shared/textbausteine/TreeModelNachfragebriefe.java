/*
 * Semtix Semesterticketbüroverwaltungssoftware entwickelt für das
 *        Semesterticketbüro der Humboldt-Universität Berlin
 *
 * Copyright (c) 2015. Michael Mertins (MichaelMertins@gmail.com)
 * 2011-2014 Jürgen Schmelzle (j.schmelzle@web.de)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.semtix.shared.textbausteine;

import org.semtix.db.DBHandlerTextbausteine;
import org.semtix.db.dao.Textbaustein;
import org.semtix.shared.daten.enums.Textbausteinkategorie;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * TreeModel enthält Daten der Textbausteine für Nachfragebriefe. Mit diesen Werten wird 
 * die baumartige Struktur der Textbausteine in der Anwendung angezeigt und es können 
 * neue Textbausteine angelegt, bestehende geändert oder gelöscht werden. 
 */
public class TreeModelNachfragebriefe
implements TreeModel {

	private List<Textbaustein> textbausteinListe;
	
	private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();


	/**
	 * Erstellt neues Model mit Liste der Textbausteine für Nachfragebriefe.
	 */
	public TreeModelNachfragebriefe() {
		
		textbausteinListe = new ArrayList<Textbaustein>();

		updateListe();
	
	}
	
	

	/**
	 * Textbausteine aus DB holen und Liste aktualisieren
	 */
	public void updateListe() {
		
		DBHandlerTextbausteine dbHandlerTextbausteine = new DBHandlerTextbausteine();
		
		textbausteinListe.clear();
		
		textbausteinListe = dbHandlerTextbausteine.getTextbausteinListe();
		
	}
	
	
	
	/**
	 * neuen Textbaustein zu Tree hinzufügen
	 * @param parent übergeordnetes Element
	 * @param t Textbaustein, der hinzugefügt werden soll
	 */
	public void addTextbaustein(TreePath parent, Textbaustein t) {
		
		DBHandlerTextbausteine dbHandlerTextbausteine = new DBHandlerTextbausteine();
		
		dbHandlerTextbausteine.create(t);

		// Den Knoten einbauen
        int index = getChildCount(parent.getLastPathComponent());
		
		//add(parent, new DefaultMutableTreeNode(t.getLabel()));
		
		// Die Listener unterrichten
        TreeModelEvent event = new TreeModelEvent( 
                this,  // Quelle des Events 
                parent, // Pfad zum Vater des veränderten Knoten
                new int[]{ index },  // Index des veränderten Knotens
                new Object[]{ t } ); // Der neue Knoten
        
        updateListe();
        
        for(TreeModelListener listener : listeners)
            listener.treeNodesInserted(event);
		
	}
	
	
	
	/**
	 * Textbaustein in Tree aktualisieren
	 * @param parentPath Pfad des Textbausteins
	 * @param t zu aktualisierender Textbaustein
	 */
	public void updateTextbaustein(TreePath parentPath, Textbaustein t) {
		
		DBHandlerTextbausteine dbHandlerTextbausteine = new DBHandlerTextbausteine();

        //ein versuch:
        if (parentPath.getLastPathComponent() instanceof Textbausteinkategorie) {
            Textbausteinkategorie nfb = (Textbausteinkategorie) parentPath.getLastPathComponent();
            t.setParentID(nfb.ordinal() + 1);
        }
        dbHandlerTextbausteine.update(t);



		// Den Knoten einbauen
		int index = getIndexOfChild(parentPath.getLastPathComponent(), t);
        //int index = getChildCount(parent.getLastPathComponent());

		// Die Listener unterrichten
        TreeModelEvent event = new TreeModelEvent(
                this,  // Quelle des Events
                parentPath, // Pfad zum Vater des veränderten Knoten
                new int[]{ index },  // Index des veränderten Knotens
                new Object[]{ t } ); // Der neue Knoten
		
		updateListe();
		
		for(TreeModelListener listener : listeners)
            listener.treeNodesChanged(event);
		
		
	}


    /**
     * Entfernt den Liste von Textbausteinen aus dem Baum
     *
     * (Das Treemodel wird aber nicht sofort geupdatet, deshalb sollte der Diolog disposed werden)
     *
     * @param tl Liste von Textbausteinen
     */
    public void remove(List<Textbaustein> tl) {

        if (null != tl) {
            DBHandlerTextbausteine dbHandlerTextbausteine = new DBHandlerTextbausteine();

            for (Textbaustein t : tl) {
                dbHandlerTextbausteine.delete(t);
            }

            updateListe();
        }

    }



    /**
     * Diese Methode gibt die Wurzel des Baumes an.
     */
    public Object getRoot() {
    	
    	return "Briefe/Nachfragen";

    }

    
    

    /**
     * Hier wird das index'te Kind des Knotens "parent" bestimmt.
     */
    public Object getChild(Object parent, int index) {
    	
    	if(parent.equals(getRoot())){

            for (Textbausteinkategorie n : Textbausteinkategorie.values()) {

                if(n.getIndex()==index+1){
        			return n;
        		}

        	}    		
    		
    	}
    	
    	else{
    		
    		ArrayList<Textbaustein> tempListe = new ArrayList<Textbaustein>();

        	for (Textbaustein t : textbausteinListe) {

                if (((Textbausteinkategorie) parent).getIndex() == t.getParentID())
                    tempListe.add(t);
        			        		
        	}
        	
        	return tempListe.get(index);    		
    		
    	}

    	return "";
    	

    }

    
    
    /**
     * Gibt für jeden Knoten an, wieviele Kinder er hat.
     */
    public int getChildCount(Object parent) {

        if (parent instanceof Textbausteinkategorie) {

            int count = 0;
    		
    		for (Textbaustein t : textbausteinListe) {

                if (((Textbausteinkategorie) parent).getIndex() == t.getParentID())
                    count++;
        		
        	}

    		return count;
    		
    	}

    	if(parent.equals(getRoot()))
            return Textbausteinkategorie.values().length;

        return 0;
    	
    	
    }

    

    /**
     * Gibt an, ob ein Knoten ein Blatt ist. Ein Blatt kann in einem JTree nicht weiter geöffnet werden.
     */
    public boolean isLeaf(Object node) {

        return !(node instanceof Textbausteinkategorie || node.equals(getRoot()));
        
    }

    
    
 
    /**
     * Gibt an, welchen Index der Knoten "child" als Kind vom Knoten "parent" hat. Wenn man folgenden Code ausführt:
     * 'Object result = getChild( parent, getIndexOfChild( parent, child ))', dann muss "result" und "child" dasselbe Objekt sein.
     */
    public int getIndexOfChild(Object parent, Object child){

        int max = getChildCount(parent);

        for(int i = 0; i < max; i++){

            if(getChild(parent, i).equals(child)){
        		 return i;
        	 }
                 
        }
           
        
        return -1;
        
    }

	
	
	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		listeners.add(listener);
	}
	
	
	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		listeners.remove(listener);
	}

	
	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
	}

}
