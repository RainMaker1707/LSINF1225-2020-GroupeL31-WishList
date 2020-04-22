package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.Wish;

public class CurrentWish {
    private static Wish wish = null;

    public static void setInstance( Wish wsh) {
        wish = wsh;
    }

    public static Wish getInstance(){
        return wish;
    }
}
