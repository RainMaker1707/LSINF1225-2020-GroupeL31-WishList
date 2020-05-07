package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;

public class UserAdapter extends BaseAdapter {

    private List<List<String>> attributesList;
    private LayoutInflater inflater;

    public UserAdapter(Context context, User user){
        this.attributesList = makeListFromUser(user);
        this.inflater = LayoutInflater.from(context);
    }

    private List<List<String>> makeListFromUser(User user) {
        List<List<String>> attributes = new ArrayList<>();

        //pseudo
        List <String> toAdd = new ArrayList<>();
        toAdd.add("Name");
        toAdd.add(user.getPseudo());
        attributes.add(toAdd);

        //mail
        toAdd = new ArrayList<>();
        toAdd.add("Email");
        toAdd.add(user.getEmail());
        attributes.add(toAdd);

        //Address
        toAdd = new ArrayList<>();
        toAdd.add("Address");
        toAdd.add(user.getAddress());
        attributes.add(toAdd);

        //color
        toAdd = new ArrayList<>();
        toAdd.add("Color");
        toAdd.add(user.getFavColor());
        attributes.add(toAdd);

        //sport
        toAdd = new ArrayList<>();
        toAdd.add("Sport");
        toAdd.add(user.getSport());
        attributes.add(toAdd);

        //meal
        toAdd = new ArrayList<>();
        toAdd.add("Favorites Meal");
        toAdd.add(user.getMeal());
        attributes.add(toAdd);

        //hobby
        toAdd = new ArrayList<>();
        toAdd.add("Hobby");
        toAdd.add(user.getHobby());
        attributes.add(toAdd);

        return attributes;
    }


    @Override
    public int getCount() {
        return this.attributesList.size();
    }

    @Override
    public List<String> getItem(int position) {
        return this.attributesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.profile_layout, null);

        List<String> current = getItem(position);

        TextView title = convertView.findViewById(R.id.preference_name);
        TextView preference = convertView.findViewById(R.id.preference_user);

        title.setText(current.get(0));
        title.setTextSize(20);
        preference.setText(current.get(1));
        preference.setTextSize(20);
        return convertView;
    }
}
