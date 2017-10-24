/********************************************************************
 * 						Classe MainActivity							*
 * 			            Initialise la vue					        *
 * 					Départ de l'application							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ezziraiy Nada			*
 *		DateStart : ....... 19/09/2017								*
 *		DateModify : ...... 24/10/2017								*
 *******************************************************************/
package fr.istic.graphes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import fr.istic.graphes.view.GraphVue;

import static fr.istic.graphes.R.id.canvas;

public class MainActivity extends AppCompatActivity {

    private GraphVue graphVue; // Viewer du graph


    /**
     * Procédure principale du programme.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphVue = (GraphVue) findViewById(canvas);
        graphVue.setNbNode(9);
    }

    /**
     * Fonction qui créer le menu principal
     * @param menu : le menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    /**
     * Fonction qui permet de donner une action à effectuée sur les items
     * du menu principal.
     * @param item : item du menu principal
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reinit:
                reinit();
                return true;
            case R.id.save:
                Toast.makeText(this, "Sauvegarde effectuée", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.open:
                Toast.makeText(this, "Ouvrir graphe", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.send:
                Toast.makeText(this, "Envoyer mail", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     *
     * @param v
     */
    public void clearArcs(View v){
        graphVue.clearArcs();
    }

    /**
     *
     * @param v
     */
    public void clearNodes(View v){
        graphVue.clearNodes();
    }

    public void clicMenuCoul(View v){
        graphVue.clicMenuCoul();
    }

    public void clicSupp(View v){
        graphVue.clicNodeSupp();
    }

    public void clicFont(View v){
        graphVue.clicFont();
    }


    /**
     * Procédure qui est appellée au clic sur les boutons de couleurs
     * pour changer la couleur d'un noeud.
     * @param v
     */
    public void clicCoul(View v){
        switch (v.getId()) {
            case R.id.rouge:
                graphVue.clicCoul("rouge");
                break;
            case R.id.vert:
                graphVue.clicCoul("vert");
                break;
            case R.id.jaune:
                graphVue.clicCoul("jaune");
                break;
            case R.id.orange:
                graphVue.clicCoul("orange");
                break;
            case R.id.rose:
                graphVue.clicCoul("rose");
                break;
            case R.id.cyan:
                graphVue.clicCoul("cyan");
                break;
            case R.id.noir:
                graphVue.clicCoul("noir");
                break;
            default:
                graphVue.clicCoul("bleu");
        }
    }

    int doubleBackToExitPressed = 1;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        } else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Cliquer 2 fois de suite pour quitter", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }

    /**
     * Procédure de réinitialisation. Remet tout à zéro.
     */
    private void reinit(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }



}
