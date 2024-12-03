package com.ilp506.taskward.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;

import java.util.Objects;

public class BottomNavUtils {

    /**
     * Configures the BottomNavigationView, including linking it to the NavController
     * and setting custom icon colors.
     *
     * @param activity             The calling AppCompatActivity.
     * @param bottomNavigationView The BottomNavigationView instance to configure.
     */
    public static void setup(@NonNull AppCompatActivity activity,
                             @NonNull BottomNavigationView bottomNavigationView) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            bottomNavigationView.setItemIconTintList(null);
            setMenuItemIcon(activity, bottomNavigationView, R.id.tasksFragment,
                    R.drawable.ic_menu_task, R.color.tasks_item_color);
            setMenuItemIcon(activity, bottomNavigationView, R.id.rewardsFragment,
                    R.drawable.ic_menu_reward, R.color.rewards_item_color);
            setMenuItemIcon(activity, bottomNavigationView, R.id.profileFragment,
                    R.drawable.ic_menu_profile, R.color.profile_item_color);
        }
        else
            Logger.e("BottomNavUtils", "NavHostFragment is null. Check the XML layout.");
    }

    /**
     * Helper method to set icon and tint for a menu item.
     *
     * @param context   The Context of the calling Activity.
     * @param navView   The BottomNavigationView instance.
     * @param itemId    The menu item ID.
     * @param iconResId The resource ID of the icon.
     * @param tintResId The resource ID of the color tint.
     */
    private static void setMenuItemIcon(@NonNull Context context,
                                        @NonNull BottomNavigationView navView,
                                        int itemId, int iconResId, int tintResId) {
        Drawable icon = ContextCompat.getDrawable(context, iconResId);
        if (icon != null)
            icon.setTintList(ContextCompat.getColorStateList(context, tintResId));

        Objects.requireNonNull(navView.getMenu().findItem(itemId)).setIcon(icon);
    }
}
