package com.danilodequeiroz.contactsdefy.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.danilodequeiroz.contactsdefy.R;
import com.danilodequeiroz.contactsdefy.model.DefyContact;
import com.danilodequeiroz.contactsdefy.viewPatterns.ViewHolderContactItem;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoCallback;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Danilo on 15/05/2015.
 */
public class ContacListAdapter extends ArrayAdapter<DefyContact> {

    List<DefyContact> contacts;
    private Context mContext;

    public ContacListAdapter(Context context, int resource, List<DefyContact> objects) {
        super(context, resource, objects);
        mContext = context;
        contacts = objects;
    }


    @Override
    public DefyContact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public void remove(DefyContact object) {
        contacts.remove(object);
    }

    @Override
    public long getItemId(int position) {
        return contacts.get(position).getmId();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderContactItem viewHolder;
        DefyContact objectItem = contacts.get(position);
        if(convertView==null){

            // inflate the layout
//            LayoutInflater inflater1 = ((Activity) mContext).getLayoutInflater();
            LayoutInflater tinflater1 = LayoutInflater.from(mContext);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_contact, parent, false);
            // well set up the ViewHolder
            viewHolder = new ViewHolderContactItem();
            viewHolder.textViewMain = (TextView) convertView.findViewById(R.id.list_item_contact_name);
            viewHolder.textViewSub = (TextView) convertView.findViewById(R.id.list_item_contact_sub);
            viewHolder.view = (ImageView) convertView.findViewById(R.id.list_item_img_contact_letter);



            viewHolder.view = (ImageView) convertView.findViewById(R.id.list_item_img_contact_letter);

            // store the holder with the view.
            convertView.setTag(viewHolder);
        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderContactItem) convertView.getTag();
        }
        // object item based on the position

        // assign values if the object is not null
        if(objectItem != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            viewHolder.textViewMain.setText(objectItem.getName()+" "+objectItem.getmSurname());
            String mLetter = String.valueOf(objectItem.getName().charAt(0));
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRect(mLetter, color);
            viewHolder.view.setImageDrawable(drawable);
            String subtitle = "";
            if(!objectItem.getCompanyName().isEmpty()){
                subtitle = "Empresa: " + objectItem.getCompanyName()+"\n";
            }
            List<String> list = new ArrayList<String>();

            if( !objectItem.getPhone().isEmpty() ){
                list.add("Telefone: "+objectItem.getPhone());
            }
            if( !objectItem.getMobilePhone().isEmpty() ){
                list.add("Celular: "+objectItem.getMobilePhone());
            }
            if( !objectItem.getWorkPhone().isEmpty() ){
                list.add("Comercial: "+objectItem.getWorkPhone());
            }
            subtitle = subtitle+join(list, "\n");;
            viewHolder.textViewSub.setText(subtitle);
        }
        return convertView;
    }
    ViewGroup viewGroup_;
//    @NonNull
//    @Override
//    public View getUndoView(int i, View convertView, @NonNull ViewGroup viewGroup) {
//        View view = convertView;
//        viewGroup_ = viewGroup;
//        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.undo_row, viewGroup, false);
//        }
//        return view;
//    }

    public static String join(List<String> strings, String del)
    {
        StringBuffer sb = new StringBuffer();
        int len = strings.size();
        boolean appended = false;
        for (int i = 0; i < len; i++)
        {
            if (appended)
            {
                sb.append(del);
            }
            sb.append(""+strings.get(i));

                    appended = true;
        }
        return sb.toString();
    }

//    @NonNull
//    @Override
//    public View getUndoClickView(@NonNull View view) {
//        return view.findViewById(R.id.undo_row_undobutton);
//    }
//
//


}
