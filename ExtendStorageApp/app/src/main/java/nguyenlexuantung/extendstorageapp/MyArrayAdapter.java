package nguyenlexuantung.extendstorageapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<Contact> {
    Activity context;
    ArrayList<Contact> myArray;
    int layoutID;

    public MyArrayAdapter(@NonNull Activity context, int textViewResourceId, @NonNull ArrayList<Contact> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.layoutID = textViewResourceId;
        this.myArray = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutID,null);
        if(myArray.size() > 0 && position >= 0){
            TextView txtDisplay =convertView.findViewById(R.id.tv_item);
            Contact contact = myArray.get(position);
            txtDisplay.setText(contact.toString());
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
