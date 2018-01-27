package com.brightapps.all;

/**
 * Created by kyadamakanti on 12/24/2017.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends  FragmentPagerAdapter  {
    private static final String TAG = "KIT-TabPagerAdapter" ;

    int tabCount;
    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ToDoFragment todoTab= new ToDoFragment();
                return todoTab;
            case 1:
                CryptoFragment cryptoTab = new CryptoFragment();
                return cryptoTab;
            case 2:
                InfoFragment infoTab = new InfoFragment();
                return infoTab;
            case 3:
                CalorieFragment calorieTab = new CalorieFragment();
                return calorieTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}