/********************************************************************
 * 						    Classe Graph     						*
 * 			            Classe qui englobe	    				    *
 * 			      les noeuds et les arcs du Graphe                  *
 * 			      Classe Parcelable.                                *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ez Ziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 09/11/2017								*
 *******************************************************************/
package fr.istic.graphes.components.graphes;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Graph implements Parcelable, Serializable {

    //--- Déclaration des propriétées ---
    private static final long serialVersionUID = -29238982928391L;
    private int mData, numNode, numArc; // idParcelable, id d'un noeuds, id Arc
    private float divScreenW, divScreenH; // withScreen, HeightScreen
    private Set<Node> lstNodes; // Liste de noeuds
    private Set<Arc> lstArcs; // Liste d'arcs


    /**
     * Constructeur de la classe
     */
    public Graph(){

    }

    /**
     * Constructeur 1 de la classe.
     * @param in
     */
    public Graph(Parcel in) {
        mData = in.readInt();
    }

    /**
     * Constructeur 2 de la classe.
     * @param height : hauteur de l'écran
     * @param width : largeur de l'écran
     */
    public Graph(float height, float width){
        //initialisation des listes de noeuds et d'arcs
        lstNodes = new HashSet<Node>();
        lstArcs = new HashSet<Arc>();

        this.numNode = 1;
        this.numArc = 0;
        this.divScreenH = (height/3)/2;//Calcul pour diviser l'écran en hauteur

        //Boucle pour créer 9 noeuds en forme de matrice dans le graphe
        for (int i = 0; i < 3; i++) {
            this.divScreenW = (width/3)/2;
            for (int j = 0; j < 3; j++){
                //Création d'un noeud
                Node node = new Node(this.divScreenW, this.divScreenH, String.valueOf(this.numNode), this.numNode);
                this.lstNodes.add(node);//Ajout d'un noeud à la liste des noeuds
                this.divScreenW += width/3;
                this.numNode++;
            }
            this.divScreenH += height/4;
        }
    }

    /**
     * Procédure qui ajoute un noeud au graphe et à la liste des noeuds
     * @param x : position du noeud en X
     * @param y : position du noeud en y
     * @param nameNode : nom du noeud
     * @param id : id du noeud
     */
    public void addNode(float x, float y, String nameNode, int id){
        Node node = new Node(x, y, nameNode, id);
        lstNodes.add(node);
    }

    /**
     * Procédure qui ajoute un arc au graphe et à la liste des arcs.
     * @param nodeStart : noeud de départ
     * @param nodeEnd : noeud d'arrivé.
     */
    public void addArc(Node nodeStart, Node nodeEnd, String nameArc, int id){
        try {
            if (nodeStart.equals(nodeEnd)) {
                ArcLoop arcLoop = new ArcLoop(nodeStart, nodeEnd, nameArc, id);
                lstArcs.add(arcLoop);
            } else {
                Arc arc = new Arc(nodeStart, nodeEnd, nameArc, id);
                lstArcs.add(arc);
            }
        }catch(Exception e){

        }
    }

    /**
     * Procédure qui supprime toute la liste de noeuds du graphe.
     */
    public void clearNodes(){
        this.lstNodes.clear();
    }

    /**
     * Procédure qui supprime toute la liste d'arcs du graphe.
     */
    public void clearArcs(){
        this.lstArcs.clear();
    }

    /**
     * Procédure qui supprime un noeud
     * @param node
     */
    public void deleteNode(Node node){
        this.lstNodes.remove(node);
    }

    /**
     * Procédure qui supprime un arc
     * @param arc
     */
    public void deleteArc(Arc arc){
        this.lstArcs.remove(arc);
    }

    /**
     * Procédure qui modifie les coordonnées des noeuds quand on tourne l'écran.
     * @param pf
     */
    public void redoPosition(PointF pf){
        /*this.divScreenH = (pf.x/3)/2;//Calcul pour diviser l'écran en hauteur

        for (Node node : this.lstNodes) {
            this.divScreenW = (pf.y/3)/2;
            for (int j = 0; j < 3; j++){
                //Création d'un noeud
                Node node = new Node(this.divScreenW, this.divScreenH, String.valueOf(this.numNode), this.numNode);
                this.lstNodes.add(node);//Ajout d'un noeud à la liste des noeuds
                this.divScreenW += pf.y/3;
                this.numNode++;
            }
            this.divScreenH += pf.x/4;
        }*/
    }

    //--- Getters ---
    /**
     * Fonction qui retourne la liste des noeuds du graphe
     * @return ArrayList<Node> : liste des noeuds du graphe.
     */
    public Set<Node> getLstNodes(){
        return (!this.lstNodes.isEmpty())?this.lstNodes:null;
    }

    /**
     * Fonction qui retourne la liste des arcs du graphe
     * @return ArrayList<Arc> : liste des arcs du graphe.
     */
    public Set<Arc> getLstArcs(){
        return (!this.lstArcs.isEmpty())?this.lstArcs:null;
    }

    /**
     * Fonction qui retourne le dernier numéro d'un noeud.
     * @return
     */
    public int getNumNode(){
        return this.numNode;
    }

    /**
     * Fonction qui retourne le dernier numéro d'un arc.
     * @return
     */
    public int getNumArc(){
        return this.numArc;
    }

    //--- Setters ---
    /**
     * Procédure qui modifie le numéro du noeud.
     * @param numNode
     */
    public void setNumNode(int numNode){
        this.numNode = numNode;
    }

    /**
     * Procédure qui modifie le numéro de l'arc
     * @param numArc
     */
    public void setNumArc(int numArc){
        this.numArc = numArc;
    }

    //------------------
    //--- Parcelable ---

    /**
     * Procédure de création d'un Parcelable
     */
    public static final Parcelable.Creator<Graph> CREATOR
            = new Parcelable.Creator<Graph>() {
        public Graph createFromParcel(Parcel in) {
            return new Graph(in);
        }

        public Graph[] newArray(int size) {
            return new Graph[size];
        }
    };

    /**
     * Procédure qui décrit le parcelable
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Procédure qui écrit le parcelable
     * @param out
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }





}
