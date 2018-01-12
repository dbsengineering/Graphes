/********************************************************************
 * 						Classe MainActivity 						*
 * 			            Départ du programme	    				    *
 * 			      Cette classe permet d'interagir                   *
 * 			      avec le graphe.                                   *
 * 			 Initialisation de la vue et des composants.            *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy               			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 01/01/2018								*
 *******************************************************************/
package fr.istic.graphes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import fr.istic.graphes.components.graphes.Arc;
import fr.istic.graphes.components.graphes.Graph;
import fr.istic.graphes.components.graphes.Node;
import fr.istic.graphes.utils.FileAdapter;
import fr.istic.graphes.utils.SerializableManager;
import fr.istic.graphes.view.DrawableGraph;
import static fr.istic.graphes.R.id.graphG;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    //--- Déclaration des propriétées ---
    private DrawableGraph dGraph; //Graphe Graphique (Viewer)
    private float[] startXY = new float[2]; // Coordonnées d'un start
    private float[] stopXY = new float[2]; //Coordonnées d'un touch stop
    private boolean blockToArc, blockToMove; //Block les menus pour effectuer des actions
    private AlertDialog dlMenuNode, dlMenuArc, dlMenuGame; // Fenêtre pour le menu
    private Dialog dlCoul; // Fenêtre pour le menu couleur
    private Dialog dlFile; // Fenêtre pour récupérer les fichiers graphe (.gra)
    private Context context; // Context permet de copier le context de la classe
    private Node node, nodeStart, nodeEnd; // noeuds
    private Arc arc; //Arc
    private Graph graph; //Graph non graphique
    private String coulTemp; // Couleur temporaire. permet de sauvegarder une couleur
    private boolean boolCoulT; // Permet de bloquer pour changer temporairement une couleur
    private boolean arcCreate; // Permet de savoir si un arc a été créer
    private PointF pStart, pEnd; // Noeud de départ , noeud d'arrivé (pour l'arc)
    private ArrayList<String> lstFile; // Liste des fichiers.

    /**
     * Procédure principale de création de la vue
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.context = this; // Context de la vue

        //Affectation des objets à la vue
        dGraph = (DrawableGraph) findViewById(graphG);

        if (savedInstanceState != null) {

            this.graph = (Graph) savedInstanceState.getSerializable("Graph");

            for(Node node : graph.getLstNodes()){
                node.reinit();
            }
            blockToMove = true;
            blockToArc = false;
            dGraph.setGraph(graph);
            dGraph.invalidate();
        }else {
            this.graph = dGraph.getGraph();// Initialisation du graph (non graphique)
            //Initialisation de la variable qui bloque la création d'arc
            blockToArc = false;
            //Initialisation de la variable qui bloque la modification de position arcs et noeuds
            blockToMove = false;

            //Création du dossier de sauvegarde des graphes
            createFolder();
        }

        //Initialisation de la variable qui permet de savoir si un arc est créé
        arcCreate = false;

        boolCoulT = true;//Initialisation du blocage couleur temporaire à true
        coulTemp = "bleu";//Initialisation de la couleur temporaire

        this.pStart = new PointF(); // Initialisation du point de départ pour l'arc temporaire
        this.pEnd = new PointF(); // Initialisation du point d'arrivé pour l'arc temporaire

        //Initialisation d'un touch Event et longClick sur la vue
        dGraph.setOnTouchListener(touchListener);
        dGraph.setOnLongClickListener(longClickListener);
    }

    /**
     * Procédure qui enregistre l'état actuel.
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("Graph", graph);
    }

    /**
     * Procédure qui permet de récupérer les coordonnées du touché.
     */
    private DrawableGraph.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // sauvegarde les coordonnées X,Y
            float eventX = event.getX();
            float eventY = event.getY();

            //choix event
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startXY[0] = eventX;//Récupération coordonnée X au touché
                    startXY[1] = eventY;// Récupération coordonnée Y au touché
                    try {
                        node = touchNode(eventX, eventY);
                    } catch (Exception e) {
                        Log.v("Main_touch_dwon", "node " + e.toString());
                    }
                    try {
                        arc = touchArc(eventX, eventY);
                    } catch (Exception e) {
                        Log.v("Main_touch", "Arc " + e.toString());
                    }

                    //Move node et arc . Récupération du départ de l'arc pour le node
                    if (blockToArc) {
                        try {
                            nodeStart = touchNode(startXY[0], startXY[1]);

                            if (nodeStart != null) {
                                initPathArc(nodeStart.getpMiddle().x, nodeStart.getpMiddle().y);
                            }
                        } catch (Exception e) {
                            Log.v("Main_touch_action_down", "probleme node " + e.toString());
                        }
                        try {
                            arc = touchArc(event.getX(), event.getY());
                        } catch (Exception i) {
                            Log.d("Main_touch_action_down", "problème arc " + i.toString());
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopXY[0] = eventX;
                    stopXY[1] = eventY;
                    if (!blockToArc && blockToMove) {
                        //Modification de la position d'un noeud ou d'un arc
                        //Move Node
                        try {
                            if (node != null) {
                                //Permet de changer la couleur pour le déplacement du noeud
                                if (boolCoulT) {
                                    coulTemp = node.getColorInt();
                                    node.setColor(0, "colorMove");
                                    boolCoulT = false;
                                }
                                moveNode(node, stopXY[0], stopXY[1]);
                            }
                        } catch (Exception e) {
                            Log.v("Exception au touch move", e.toString());
                        }
                        //Move Arc
                        try {
                            if (arc != null) {
                                moveArc(arc, startXY[0], startXY[1], stopXY[0], stopXY[1]);
                                dGraph.invalidate();
                            }
                        } catch (Exception i) {
                            Log.d("Main_action_move", "probleme arc");
                        }
                    } else {
                        if (blockToArc) {
                            if (nodeStart != null) {
                                //Dessine les arcs (MOVE)
                                arcTemp(stopXY[0], stopXY[1]);
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    stopXY[0] = eventX;
                    stopXY[1] = eventY;
                    if (!blockToArc && blockToMove) {
                        //Modification de la position d'un noeud
                        try {
                            if (node != null) {
                                //Permet de remettre la couleur d'origine du noeud
                                if (!boolCoulT) {
                                    node.setColor(0, coulTemp);
                                    boolCoulT = true;
                                }
                            }
                            if (arc != null) {

                            }
                            dGraph.invalidate();
                        } catch (Exception e) {
                            Log.v("Exception up touch node", e.toString());
                        }
                    }
                    if (blockToArc) {
                        nodeEnd = touchNode(stopXY[0], stopXY[1]);
                        if (nodeEnd != null) {
                            //Ajout d'un arc à la liste des arcs
                            addArc(nodeStart, nodeEnd);
                        } else {
                            dGraph.draw(pStart = null, pEnd = null);
                            dGraph.invalidate();
                        }
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * Procédure qui permet d'ajouter un noeuds sur un Long click et si seulement un noeud
     * n'exite pas sur la position voulue.
     */
    private DrawableGraph.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            float x = startXY[0];
            float y = startXY[1];
            //Bloquer l'ajout de noeuds si le mode "ajout d'arcs" est activé
            if (!blockToArc) {
                if (!blockToMove) {
                    //Affectation des coordonnées

                    //Test si on touche un noeud ou un arc
                    try {
                        node = touchNode(x, y);
                        arc = touchArc(x, y);
                        if (node == null && arc == null) {
                            //Si on ne touche pas un noeud ou pas un arc, alors on créer un noeud
                            addNode(x, y);
                        } else {
                            //Si arc n'est pas null alors on affiche le menu des arcs
                            if (arc != null) {
                                //Afficher menu de modification des arcs
                                //showDialMenuA(context);
                            } else {
                                //Afficher menu de modification des noeuds
                                showDialMenuN(context);
                            }
                        }
                    } catch (Exception e) {
                        //Si on a une erreur car le système n'a pas trouvé de noeuds
                        //alors on crée un noeud
                        addNode(x, y);
                    }
                }
            }else{
                if(arc != null){
                    showDialMenuA(context);
                }
            }
            return true;
        }
    };

    /**
     * Fonction qui créer le menu principal
     *
     * @param menu : le menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //--------------------------------
    //--- méthodes pour les noeuds ---
    //--------------------------------

    /**
     * Procédure qui permet d'afficher le menu des couleurs du noeud.
     *
     * @param v
     */
    public void clicMenuCoulN(View v) {
        dlMenuNode.dismiss();
        dlMenuNode.dismiss();
        dlCoul = new Dialog(context);
        dlCoul.setTitle(getResources().getString(R.string.color));
        dlCoul.setContentView(R.layout.changecouln);
        dlCoul.show();
    }

    /**
     * Procédure qui permet de supprimer un noeud.
     *
     * @param v
     */
    public void clicSuppN(View v) {
        dlMenuNode.dismiss();
        Iterator<Arc> itArc = graph.getLstArcs().iterator();
        try {
            while(itArc.hasNext()){
                Arc arc1 = itArc.next();
                if(arc1.getNodeStart().equals(node) || arc1.getNodeEnd().equals(node)){
                    itArc.remove();
                }
            }
        } catch (Exception e) {
            Log.d("Main delNode", "Probleme de suppression node");
        }
        graph.deleteNode(node);
        dGraph.invalidate();
    }

    /**
     * Fonction qui retourne le noeud qui a été touché.
     *
     * @param x : coordonnée x. float
     * @param y : coordonnée y. float
     * @return Node
     */
    private Node touchNode(float x, float y) {
        try {
            for (Node n : this.graph.getLstNodes()) {
                if (n.getRectFOut().contains(x, y)) {
                    return n;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Modifier la position d'un noeud.
     *
     * @param n : Node en cours
     * @param x : position x
     * @param y : position y
     */
    private void moveNode(Node n, float x, float y) {
        n.setCoord(x, y);
        dGraph.invalidate();
    }

    /**
     * Fonction qui affiche le menu d'un noeud.
     *
     * @param context
     * @return boolean
     */
    private boolean showDialMenuN(Context context) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.optsnode, null);
        AlertDialog.Builder dlgBuild = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                        }
                        return false;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        dlgBuild.setView(mView);
        dlMenuNode = dlgBuild.create();
        dlMenuNode.setTitle(getResources().getString(R.string.optsNode));
        dlMenuNode.show();
        return false;
    }

    /**
     * Procédure qui permet d'afficher la fenêtre d'ajout de noeud.
     *
     * @param x : position x
     * @param y : position y
     */
    private void addNode(final float x, final float y) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.msgboxaddnode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this.context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText txtName = (EditText) mView.findViewById(R.id.editTxtNode);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.createNode), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (graph.getNumNode() != 0) {
                            graph.setNumNode(graph.getNumNode() + 1);
                            graph.addNode(x, y, txtName.getText().toString(), graph.getNumNode() + 1);

                        } else {
                            graph.setNumNode(1);
                            graph.addNode(x, y, txtName.getText().toString(), 1);
                        }
                        //Validation du noeud
                        dGraph.invalidate();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();

                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        // Afficher directement le clavier
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setTitle(getResources().getString(R.string.createANode));

        // Montrer le dialogBox
        alertDialog.show();
    }

    /**
     * Procédure qui affiche un AlertDialogue pour modifier la taille du noeud.
     *
     * @param v : View
     */
    public void clicSizeN(View v) {
        dlMenuNode.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.sizenode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final NumberPicker np = (NumberPicker) mView.findViewById(R.id.np);//Récupération du NumberPicker
        int taille = node.getSize();
        np.setMinValue(node.getSize());
        np.setMaxValue(200);
        np.setValue(node.getSize());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.modifiy),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                node.setSize(np.getValue());
                                dGraph.invalidate();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        //Titre de l'AlertDialog
        alertDialog.setTitle(getResources().getString(R.string.modifiySize));

        // Montrer le dialogBox
        alertDialog.show();
    }

    /**
     * Procédure qui permet d'afficher un AlertDialog sur le click du menu Font.
     *
     * @param v : View
     */
    public void clicFontN(View v) {
        dlMenuNode.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.msgboxaddnode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText txtName = (EditText) mView.findViewById(R.id.editTxtNode);
        txtName.setText(node.getNameNode());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.modifiyTxt),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                node.setNameNode(txtName.getText().toString());
                                dGraph.invalidate();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        // Afficher directement le clavier
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setTitle(getResources().getString(R.string.modifiyTxt));

        // Montrer le dialogBox
        alertDialog.show();
    }

    //--------------------------------
    //--------------------------------


    //--------------------------------
    //---- méthodes pour les arcs ----
    //--------------------------------

    /**
     * Procédure qui permet de dessiner l'arc temporaire.
     *
     * @param endX : point x d'arrivé.
     * @param endY : point y d'arrivé.
     */
    private void arcTemp(float endX, float endY) {
        this.pEnd.x = endX;
        this.pEnd.y = endY;
        dGraph.draw(pStart, pEnd);
    }

    /**
     * Procédure qui permet d'initialiser le point de départ de l'arc temporaire.
     *
     * @param x : point x de départ.
     * @param y : point y de départ.
     */
    private void initPathArc(float x, float y) {
        this.pStart = new PointF();
        this.pEnd = new PointF();
        this.pStart.x = x;
        this.pStart.y = y;
    }

    /**
     * Fonction qui retourne l'arc qui a été touché.
     *
     * @param x : coordonnée x. float
     * @param y : coordonnée y. float
     * @return Arc
     */
    private Arc touchArc(float x, float y) {
        try {
            for (Arc arc : this.graph.getLstArcs()) {
                if (arc.getRectTxt().contains(x, y)) {
                    return arc;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Procédure qui permet de bouger un arc
     *
     * @param arc    : Arc
     * @param xStart : position x de départ. float
     * @param yStart : position y de départ. float
     * @param xStop  : position x d'arrivé. float
     * @param yStop  : position y d'arrivé. float
     */
    private void moveArc(Arc arc, float xStart, float yStart, float xStop, float yStop) {
        //arc.setPath(xStart, yStart, xStop, yStop);
        arc.setPath(xStart, yStart, xStop, yStop);
    }

    /**
     * Procédure qui permet d'afficher le menu des couleurs du noeud.
     *
     * @param v
     */
    public void clicMenuCoulA(View v) {
        dlMenuArc.dismiss();
        dlMenuArc.dismiss();
        dlCoul = new Dialog(context);
        dlCoul.setTitle(getResources().getString(R.string.color));
        dlCoul.setContentView(R.layout.changecoula);
        dlCoul.show();
    }

    /**
     * Procédure qui permet de supprimer un arc.
     *
     * @param v : View
     */
    public void clicSuppA(View v) {
        dlMenuArc.dismiss();
        graph.deleteArc(arc);
        dGraph.invalidate();
    }

    /**
     * Procédure qui permet de changer l'épaisseur du trait de l'arc.
     *
     * @param v : View
     */
    public void clicSizeA(View v) {
        dlMenuArc.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.sizenode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final NumberPicker np = (NumberPicker) mView.findViewById(R.id.np);//Récupération du NumberPicker

        np.setMinValue(8);
        np.setMaxValue(20);
        np.setValue(arc.getThickness());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.modifiy),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                arc.setThickness(np.getValue());

                                dGraph.invalidate();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        //Titre de l'AlertDialog
        alertDialog.setTitle(getResources().getString(R.string.modifiySizeA));

        // Montrer le dialogBox
        alertDialog.show();

    }

    /**
     * Procédure qui permet de changer la taille de l'étiquette de l'arc.
     *
     * @param v : View
     */
    public void clicSizeALabel(View v) {
        dlMenuArc.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.sizenode, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final NumberPicker np = (NumberPicker) mView.findViewById(R.id.np);//Récupération du NumberPicker

        np.setMinValue(10);
        np.setMaxValue(20);
        np.setValue(arc.getSizeLabel());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.modifiy),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                arc.setSizeLabel(np.getValue());
                                dGraph.invalidate();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        //Titre de l'AlertDialog
        alertDialog.setTitle(getResources().getString(R.string.modifiySizeL));

        // Montrer le dialogBox
        alertDialog.show();

    }

    /**
     * Procédure qui modifie le nom de l'arc.
     *
     * @param v : View
     */
    public void clicFontA(View v) {
        dlMenuArc.dismiss();
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.msgboxaddarc, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText txtName = (EditText) mView.findViewById(R.id.editTxtArc);
        txtName.setText(arc.getNameArc());
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.modifiyTxt),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                arc.setNameArc(txtName.getText().toString());
                                dGraph.invalidate();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        // Afficher directement le clavier
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setTitle(getResources().getString(R.string.modifiyTxtA));

        // Montrer le dialogBox
        alertDialog.show();
    }

    /**
     * Procédure qui permet d'afficher la fenêtre d'ajout d'arcs.
     *
     * @param nodeStart : noeud de départ. Node
     * @param nodeEnd   : noeud d'arrivé. Node
     */
    private void addArc(final Node nodeStart, final Node nodeEnd) {
        arcCreate = true;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.msgboxaddarc, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this.context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText txtName = (EditText) mView.findViewById(R.id.editTxtArc);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.createArc), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if (graph.getNumArc() != 0) {
                            graph.setNumArc(graph.getNumArc() + 1);

                            graph.addArc(nodeStart, nodeEnd, txtName.getText().toString(), graph.getNumArc() + 1);

                        } else {
                            graph.setNumArc(1);
                            graph.addArc(nodeStart, nodeEnd, txtName.getText().toString(), 1);
                        }
                        //On remet à zéro l'arc temporaire et on valide
                        dGraph.draw(pStart = null, pEnd = null);
                        dGraph.invalidate();
                        arcCreate = false;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                //On remet à zéro l'arc temporaire et on valide
                                dGraph.draw(pStart = null, pEnd = null);
                                dGraph.invalidate();
                                arcCreate = false;
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        // Afficher directement le clavier
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setTitle(getResources().getString(R.string.createAArc));

        // Montrer le dialogBox
        alertDialog.show();
    }

    /**
     * Procédure qui efface les arcs.
     */
    private void clearArcs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.titleClearA));
        builder.setMessage(getResources().getString(R.string.clearAllA));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                graph.clearArcs();
                dGraph.invalidate();
            }
        });
        //Cancel Button
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Fonction qui affiche le menu d'un arc.
     *
     * @param context
     * @return boolean
     */
    public boolean showDialMenuA(Context context) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.optsarc, null);
        AlertDialog.Builder dlgBuild = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                        }
                        return false;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        dlgBuild.setView(mView);
        dlMenuArc = dlgBuild.create();
        dlMenuArc.setTitle(getResources().getString(R.string.optsArc));
        dlMenuArc.show();
        return false;
    }

    //--------------------------------
    //--------------------------------

    /**
     * Procédure qui permet de charger le jeu de dame.
     *
     * @param v : View
     */
    public void clicDame(View v) {
        dlMenuGame.dismiss();
        try {
            InputStream inStream = getResources().openRawResource(R.raw.dame);
            graph.clearNodes();
            graph.clearArcs();
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(inStream);
                graph = (Graph) ois.readObject();
            } catch (Exception ex) {
                Log.e("Main_clicDame", "Problème ObjectInputStream : " + ex);
            }
            for (Node node : graph.getLstNodes()) {
                node.reinit();
            }
            blockToMove = true;
            blockToArc = false;
            dGraph.setGraph(graph);
            dGraph.invalidate();
        } catch (Exception e) {
            Log.e("Main_clicDame", "Fichier Dame inexistant : " + e);
        }
    }

    /**
     * Procédure qui est appellée au clic sur les boutons de couleurs
     * pour changer la couleur d'un noeud ou d'un arc.
     *
     * @param v : View
     */
    public void clicCoul(View v) {
        String nOra = v.getTag().toString(); // récupère le tag soit d'un noeud [0] ou d'un arc [1]
        switch (v.getId()) {
            case R.id.rouge:
                if (nOra.equals("0")) {
                    node.setColor(0, "rouge");
                } else {
                    arc.setColor(0, "rougeArc");
                }
                break;
            case R.id.vert:
                if (nOra.equals("0")) {
                    node.setColor(0, "vert");
                } else {
                    arc.setColor(0, "vertArc");
                }
                break;
            case R.id.jaune:
                if (nOra.equals("0")) {
                    node.setColor(0, "jaune");
                } else {
                    arc.setColor(0, "jauneArc");
                }
                break;
            case R.id.orange:
                if (nOra.equals("0")) {
                    node.setColor(0, "orange");
                } else {
                    arc.setColor(0, "orangeArc");
                }
                break;
            case R.id.rose:
                if (nOra.equals("0")) {
                    node.setColor(0, "rose");
                } else {
                    arc.setColor(0, "roseArc");
                }
                break;
            case R.id.cyan:
                if (nOra.equals("0")) {
                    node.setColor(0, "cyan");
                } else {
                    arc.setColor(0, "cyanArc");
                }
                break;
            case R.id.noir:
                if (nOra.equals("0")) {
                    node.setColor(0, "noir");
                } else {
                    arc.setColor(0, "noirArc");
                }
                break;
            default:
                if (nOra.equals("0")) {
                    node.setColor(0, "bleu");
                } else {
                    arc.setColor(0, "bleuArc");
                }
        }
        dlCoul.dismiss();
        dGraph.invalidate();
    }

    /**
     * Fonction qui permet de donner une action à effectuée sur les items
     * du menu principal.
     *
     * @param item : item du menu principal
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Selection de l'item
        switch (item.getItemId()) {
            case R.id.reinit:
                reinit();
                return true;
            case R.id.supNode:
                clearAll();
                Toast.makeText(this, getResources().getString(R.string.clearAllMess),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.supArc:
                clearArcs();
                Toast.makeText(this, getResources().getString(R.string.clearAllArcsMess),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.save:
                saveFrame();
                return true;
            case R.id.normalMode:
                blockToArc = false;
                blockToMove = false;
                item.setChecked(true);
                Toast.makeText(this, getResources().getString(R.string.normModeMess),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.addArc:
                item.setChecked(true);
                blockToArc = true;
                blockToMove = false;
                Toast.makeText(this, getResources().getString(R.string.addArcMess),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.moveAN:
                item.setChecked(true);
                blockToMove = true;
                blockToArc = false;
                Toast.makeText(this, getResources().getString(R.string.changPosMess),
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.open:
                open();
                return true;
            case R.id.send:
                sendMail();
                return true;
            case R.id.special:
                showDialMenuSpecial(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Fonction qui affiche le menu special.
     *
     * @param context
     * @return boolean
     */
    public boolean showDialMenuSpecial(Context context) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.specialmode, null);
        AlertDialog.Builder dlgBuild = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                        }
                        return false;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        dlgBuild.setView(mView);
        dlMenuGame = dlgBuild.create();
        dlMenuGame.setTitle(getResources().getString(R.string.modespe));
        dlMenuGame.show();
        return false;
    }

    /**
     * Procédure qui permet d'afficher un AlertDialog sur le click du menu Font.
     */
    private void saveFrame() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.msgboxsavefile, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText txtName = (EditText) mView.findViewById(R.id.saveFile);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                try {
                                    save(txtName.getText().toString());
                                    Toast.makeText(context, getResources().getString(R.string.saveMess),
                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        // Assigner le builderBox au dialogBox.
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        // Afficher directement le clavier
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        alertDialog.setTitle(getResources().getString(R.string.save));

        // Montrer le dialogBox
        alertDialog.show();
    }

    /**
     * @param nameFile
     * @throws IOException
     */
    private void save(String nameFile) throws IOException {
        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + "/DCIM/Graphs" + File.separator;
            path += nameFile + ".gra";
            SerializableManager.saveFile(graph, path);
        }catch(Exception e){
            Log.e("Main_save", "Problème sauvegarde : " + e);
        }
    }


    /**
     * Procédure qui permet d'ouvrir un fichier
     */
    private void open() {
        lstFile = new ArrayList<String>();
        final FileAdapter adapter;
        String path = Environment.getExternalStorageDirectory() + File.separator + "DCIM/Graphs";
        File directory = new File(path);
        File[] files = directory.listFiles();

        // lister les fichiers
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile() && files[i].getPath().endsWith(".gra")) {
                lstFile.add(path + File.separator + files[i].getName());
            }
        }
        adapter = new FileAdapter(context, lstFile);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setIcon(R.drawable.ic_file);
        builderSingle.setTitle(getResources().getString(R.string.titleFile));

        builderSingle.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameFile = (String) adapter.getItem(which);
                try {
                    openFile(nameFile);
                } catch (Exception e) {
                    Toast.makeText(context, getResources().getString(R.string.problemFile),
                            Toast.LENGTH_SHORT).show();
                    Log.d("Main_open", "openFile" + e);
                }
            }
        });
        builderSingle.show();
    }

    /**
     * Récupération des données du fichier et initialisation du graphe
     *
     * @param path
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void openFile(String path) throws IOException, ClassNotFoundException {
        try {
            File data = new File(path);
            if (data.exists()) {
                graph.clearNodes();
                graph.clearArcs();
                graph = SerializableManager.openGraph(path);

                for(Node node : graph.getLstNodes()){
                    node.reinit();
                }
                dGraph.setGraph(graph);
                dGraph.invalidate();
            }
        } catch (Exception e) {
            Log.e("Main_open", "Problème ouverture : " + e);
        }
    }

    /**
     * Procédure qui créé le dossier Graphs dans DCIM du téléphone pour y mettre les fichiers graphs sauvegardés.
     */
    private void createFolder() {
        final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Graphs");
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * Procédure qui permet d'afficher la fenêtre d'effacement totale
     */
    private void clearAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.titleClearN));
        builder.setMessage(getResources().getString(R.string.clearAllQ));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blockToArc = false;
                blockToMove = false;
                nodeStart = null;
                nodeEnd = null;
                graph.clearNodes();
                graph.clearArcs();
                dGraph.invalidate();
            }
        });
        //Cancel Button
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Procédure de réinitialisation. Remet tout à zéro.
     */
    private void reinit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.reinit));
        builder.setMessage(getResources().getString(R.string.resetAllQ));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        //Cancel Button
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Procédure qui appelle une application messagerie email pour envoyer un email avec l'image en pièce jointe
     */
    private void sendMail() {
        File file, f;
        try {
            //Enregistrement de l'image
            dGraph.setDrawingCacheEnabled(true);
            Bitmap bitmap = dGraph.getDrawingCache();

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Graphs");
            if (!file.exists()) {
                file.mkdirs();
            }
            f = new File(file.getAbsolutePath() + File.separator + "graph" + ".png");

            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, fos);
            fos.close();

            //Envoi image dans email
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse("file://" + file.getAbsolutePath() + File.separator + "graph.png");

            sharingIntent.setType("image/png");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
