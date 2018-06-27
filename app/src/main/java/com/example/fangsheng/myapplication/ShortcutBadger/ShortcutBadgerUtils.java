package com.example.fangsheng.myapplication.ShortcutBadger;

import android.content.Context;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by fangsheng on 2017/10/24.
 */

public class ShortcutBadgerUtils {

    public static void ShortcutBadgerTest(Context context, int badgeCount){
        ShortcutBadger.applyCount(context, badgeCount);
    }
}
