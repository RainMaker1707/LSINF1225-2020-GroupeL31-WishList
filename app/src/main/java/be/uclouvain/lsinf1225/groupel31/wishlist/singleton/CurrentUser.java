package be.uclouvain.lsinf1225.groupel31.wishlist.singleton;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;

public class CurrentUser {
    /*
     * Singleton for user signed in
     */

    private static User user = null;

    public static void setInstance(User new_user){
        user = new_user;
    }

    public static User getInstance(){
        return user;
    }
}
