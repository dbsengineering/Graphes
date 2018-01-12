/********************************************************************
 * 						Classe SerializableManager					*
 * 			            Permet de sérialiser des objects.           *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy               			*
 *		DateStart : ....... 19/12/2017								*
 *		DateModify : ...... 12/01/2018								*
 *******************************************************************/
package fr.istic.graphes.utils;

import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import fr.istic.graphes.components.graphes.Graph;

/**
 * Created by cavronjeremy on 11/11/2017.
 */

public class SerializableManager {

    /**
     * Procédure qui permet d'enregistrer un graphe en fichier.
     * @param graph
     * @param path
     */
    public static void saveFile(Graph graph, String path){
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(graph);
            Log.d("SerializableManager_s", "Enregistrement ok.");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Fonction qui retourne le graphe récupéré dans un fichier.
     * @param path
     * @return graph : Graph
     */
    public static Graph openGraph(String path){
        Graph graph = null;
        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {
            fin = new FileInputStream(path);
            ois = new ObjectInputStream(fin);
            graph = (Graph) ois.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return graph;
    }
}
