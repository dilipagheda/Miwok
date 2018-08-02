package com.example.android.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MiwokFragmentAdaptor extends FragmentPagerAdapter {

    private String title[] = new String[]{"Numbers","Colors","Family","Phrases"};
    public MiwokFragmentAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch(position){
            case 0:
                f = new NumbersFragment();
                break;
            case 1:
                f = new ColorsFragment();
                break;
            case 2:
                f = new FamilyFragment();
                break;
            case 3:
                f = new PhrasesFragment();
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
