package fr.istic.graphes.component;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import fr.istic.graphes.R;

/**
 * Created by cavronjeremy on 24/10/2017.
 */

public class MenuNode {

    private AlertDialog dialogMenu;
    private AlertDialog dlMenuNode;
    private boolean block;


    public boolean showDialMenu(Context context){
        block = true;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.optsnode, null);
        AlertDialog.Builder dlgBuild = new AlertDialog.Builder(context)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            block = false;

                            dialog.cancel();

                            return block;
                        }
                        return false;
                    }
                });
        dlgBuild.setView(mView);
        dlMenuNode = dlgBuild.create();

        //dlMenuNode = new Dialog(context);
        dlMenuNode.setTitle("Options Noeud");
        //dlMenuNode.setContentView(R.layout.optsnode_couleur);
        dlMenuNode.show();
        return block;
    }

    public void stopDialMenu(){
        this.dlMenuNode.dismiss();
    }


}
