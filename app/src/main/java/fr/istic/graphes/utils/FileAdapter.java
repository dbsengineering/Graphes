/********************************************************************
 * 						Classe FileAdapter   						*
 * 			            Permet de lister les fichiers			    *
 *                      .gra et de créer une liste view             *
 *                      répertoriant les chemin des fichiers.       *
 * 					                    							*
 *																	*
 *		School : .......... Istic									*
 *		Formation : ....... Master 1 MIAGE							*
 *		Lecture : ......... MOBILE									*
 *		Group : ........... 1a										*
 *		Authors : ......... Cavron Jérémy, Ez Ziraiy Nada			*
 *		DateStart : ....... 11/11/2017								*
 *		DateModify : ...... 11/11/2017								*
 *******************************************************************/
package fr.istic.graphes.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.istic.graphes.R;

/**
 *
 */
public class FileAdapter extends BaseAdapter {

    //--- Déclaration des propriétées ---
    private ArrayList<String> lstFile;
    private Context context;
    private LayoutInflater inflater;

    /**
     * Constructeur de la classe.
     */
    public FileAdapter(Context context, ArrayList<String> lstFile){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.lstFile = lstFile;
    }

    /**
     * retourne le nombre d'éléments de la listview
     */
    @Override
    public int getCount() {
        return this.lstFile.size();
    }

    /**
     * Fonction qui retourne le context
     * @return
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Fonction qui retourne l'item de la listview à un index précis
     */
    @Override
    public Object getItem(int index) {
        return lstFile.get(index);
    }

    /**
     * retourne l'index de l'élément actuel
     */
    @Override
    public long getItemId(int index) {
        return index;
    }

    /**
     * Structure contenant les éléments d'une ligne
     */
    private class ViewHolder {
        ImageView imgFile;
        TextView txtFile;
    }

    /**
     * Procédure qui affiche dans la liste les .fichiers
     */
    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder holder ;

        if (convertView == null) {
            holder = new ViewHolder() ;
            convertView = inflater.inflate(R.layout.file, null);
            holder.imgFile = (ImageView) convertView.findViewById(R.id.imgFile);
            holder.txtFile = (TextView) convertView.findViewById(R.id.txtFile);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.imgFile.setId(index);
        holder.imgFile.setClickable(true);
        holder.txtFile.setText(lstFile.get(index));

        return convertView;
    }

}
