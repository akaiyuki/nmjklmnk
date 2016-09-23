package com.fasionparade.fasionparadeApp.Functions.Core;

/**
 * Created by CodeSyaona on 9/20/16.
 */
public class MSingleton {

    public static int upgradeAccount;

    public static int getUpgradeAccount() {
        return upgradeAccount;
    }

    public static void setUpgradeAccount(int upgradeAccount) {
        MSingleton.upgradeAccount = upgradeAccount;
    }

}
