package com.maymeng.jinkeyulv.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by  leijiaxq
 * Date       2017/3/2 14:31
 * Describe
 */
public class FragmentFactory {
    public static final int FRAGMENT_ONE = 1;
    public static final int FRAGMENT_TWO = 2;
    public static final int FRAGMENT_THREE = 3;

    public static final int FRAGMENT_FOUR = 4;
    public static final int FRAGMENT_FIVE = 5;

    private static Map<Integer, RxLazyFragment> fragments = new HashMap<>();


    /*public static RxLazyFragment getFragment(int position) {
        RxLazyFragment fragment = null;
        if (fragments.containsKey(position)) {
            RxLazyFragment baseFragment = fragments.get(position);
            return baseFragment;
        }
        switch (position) {
            case FRAGMENT_ONE:
                fragment = new ZhihuFragment();       //知乎
                break;
            case FRAGMENT_TWO:
                fragment = new MoviesFragment();  //豆瓣影视
                break;
            case FRAGMENT_THREE:
                fragment = new BooksFragment();  //豆瓣文学
                break;
            default:
                break;
        }
        fragments.put(position, fragment);
        return fragment;
    }*/


}
