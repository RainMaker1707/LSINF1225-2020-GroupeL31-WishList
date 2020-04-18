package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;

public class WishListAdapter extends BaseAdapter {
    private Context context;
    private List<WishList> wish_list;
    private LayoutInflater inflater;

    public WishListAdapter(Context context, List<WishList> wish_list){
        this.context = context;
        this.wish_list = wish_list;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.wish_list.size();
    }

    @Override
    public WishList getItem(int position) {
        return this.wish_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.wishlist_layout, null);

        WishList current = getItem(position);

        TextView title = convertView.findViewById(R.id.wishlist_title);
        ImageView picture = convertView.findViewById(R.id.wishlist_picture);
        TextView items = convertView.findViewById(R.id.wishlist_in);

        title.setText(current.getName());
        items.setText(String.format("Wish In: %d", current.getSize()));


        return convertView;
    }
}
