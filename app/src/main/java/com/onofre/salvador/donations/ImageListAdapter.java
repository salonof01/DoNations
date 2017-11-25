package com.onofre.salvador.donations;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.onofre.salvador.donations.R.id.conditionText;
import static com.onofre.salvador.donations.R.id.donorName;
import static com.onofre.salvador.donations.R.id.pickUpAddress;

/**
 * Created by Onofre on 10/15/2017.
 */

public class ImageListAdapter extends ArrayAdapter<Donation> {
    private Activity context;
    private int resource;
    private List<Donation> listImage;

    public ImageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Donation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView ddescription = (TextView) v.findViewById(conditionText);
        TextView dName=(TextView) v.findViewById(donorName);
        TextView dAddress=(TextView)v.findViewById(pickUpAddress);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);

        dName.setText(listImage.get(position).getName());
        dAddress.setText(listImage.get(position).getAddress());
        ddescription.setText(listImage.get(position).getDescription());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);

        return v;

    }
}