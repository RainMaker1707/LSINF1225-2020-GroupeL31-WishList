package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;

public class CurrentWishList {
    private static WishList wish_lst = null;

    public static WishList getInstance(){return wish_lst;}

    public static void setInstance(WishList wishLst){wish_lst = wishLst;}
}

