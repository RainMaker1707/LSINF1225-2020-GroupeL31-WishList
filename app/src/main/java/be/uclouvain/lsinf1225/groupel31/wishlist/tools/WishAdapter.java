package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.Wish;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;

public class WishAdapter extends BaseAdapter {
    private Context context;
    private List<Wish> wish_l;
    private LayoutInflater inflater;

    public WishAdapter(Context context, List<Wish> wish_l){
        this.context = context;
        this.wish_l = wish_l;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.wish_l.size();
    }

    @Override
    public Wish getItem(int position) {
        return this.wish_l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.wish_layout, null);

        Wish current = getItem(position);

        TextView title = convertView.findViewById(R.id.wish_title);
        ImageView picture = convertView.findViewById(R.id.wish_picture);
        TextView price = convertView.findViewById(R.id.wish_price);

        if(current.getPicture() != null){picture.setImageBitmap(current.getPicture());}

        title.setText(current.getName());
        price.setText(String.format("Price : %.2f", current.getPrice()));

        return convertView;
    }
}
