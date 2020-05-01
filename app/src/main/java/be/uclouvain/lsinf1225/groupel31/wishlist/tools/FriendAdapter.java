package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;


public class FriendAdapter extends BaseAdapter {
    private Context context;
    private List<User> user_l;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, List<User> users) {
        this.context = context;
        this.user_l = users;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.user_l.size();
    }

    @Override
    public User getItem(int position) {
        return this.user_l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.friends_adapter, null);
        User current = this.getItem(position);

        TextView name = convertView.findViewById(R.id.name);
        TextView nbr_wishlist = convertView.findViewById(R.id.nbr_wishlist);
        TextView mail = convertView.findViewById(R.id.mail_friend);

        name.setText(current.getPseudo());
        nbr_wishlist.setText(String.format("Has %s WishLists", current.getWishlist_list().size()));
        mail.setText(current.getEmail());
        return convertView;
    }
}
