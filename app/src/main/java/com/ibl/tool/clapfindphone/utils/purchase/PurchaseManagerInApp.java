package com.ibl.tool.clapfindphone.utils.purchase;

public class PurchaseManagerInApp {
    private static PurchaseManagerInApp instance;
    public static PurchaseManagerInApp getInstance() {
        if (instance == null) {
            instance = new PurchaseManagerInApp();
        }
        return instance;
    }
    public boolean isPurchased() {
        return false;
    }
}
