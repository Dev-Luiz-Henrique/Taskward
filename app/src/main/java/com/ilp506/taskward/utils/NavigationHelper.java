package com.ilp506.taskward.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleObserver;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.exceptions.NavigationHelperException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for managing navigation-related functionality, including
 * NavController setup, BottomNavigationView configuration, and toolbar updates.
 */
public class NavigationHelper implements LifecycleObserver {
    private static final String TAG = NavigationHelper.class.getSimpleName();

    private final AppCompatActivity activity;
    private final NavController navController;

    /**
     * Initializes the NavigationHelper with the NavController derived from the NavHostFragment.
     *
     * @param activity The calling AppCompatActivity containing the NavHostFragment.
     * @throws NavigationHelperException If the NavHostFragment is not found.
     */
    public NavigationHelper(@NonNull AppCompatActivity activity) {
        this.activity = activity;

        try {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            NavHostFragment navHostFragment = (NavHostFragment)
                    fragmentManager.findFragmentById(R.id.nav_host_fragment);

            if (navHostFragment != null)
                this.navController = navHostFragment.getNavController();
            else {
                Logger.e(TAG, "NavHostFragment not found in the layout.");
                throw new NavigationHelperException("NavHostFragment not found. " +
                        "Ensure the XML layout includes a <fragment> with ID R.id.nav_host_fragment.");
            }
        } catch (Exception e) {
            Logger.e(TAG, "Error initializing NavigationHelper: " + e.getMessage(), e);
            throw new NavigationHelperException("Failed to initialize NavigationHelper.", e);
        }
    }

    /**
     * Configures the BottomNavigationView, linking it to the NavController and
     * customizing item icons and colors.
     *
     * @param bottomNavigationView The BottomNavigationView instance to configure.
     * @throws NavigationHelperException If configuration fails.
     */
    public void setupBottomNavigationView(@NonNull BottomNavigationView bottomNavigationView) {
        try {
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            bottomNavigationView.setItemIconTintList(null);

            configureMenuItem(bottomNavigationView, R.id.tasksFragment,
                    R.drawable.ic_menu_task, R.color.tasks_item_color);
            configureMenuItem(bottomNavigationView, R.id.rewardsFragment,
                    R.drawable.ic_menu_reward, R.color.rewards_item_color);
            configureMenuItem(bottomNavigationView, R.id.profileFragment,
                    R.drawable.ic_menu_profile, R.color.profile_item_color);
        } catch (Exception e) {
            Logger.e(TAG, "Error setting up BottomNavigationView: " + e.getMessage(), e);
            throw new NavigationHelperException("Failed to configure BottomNavigationView.", e);
        }
    }

    /**
     * Configures the Toolbar by updating the title dynamically based on the current fragment in the navigation.
     *
     * @param toolbar The Toolbar to be updated with the current title.
     * @throws NavigationHelperException If the Toolbar cannot be configured.
     */
    public void setupToolbar(@NonNull Toolbar toolbar) {
        try {
            TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
            if (toolbarTitle == null) {
                Logger.w(TAG, "Toolbar title TextView not found.");
                throw new NavigationHelperException("Toolbar title TextView not found in the layout.");
            }

            Map<Integer, Integer> destinationToTitleMap = new HashMap<>();
            destinationToTitleMap.put(R.id.tasksFragment, R.string.tasks_title);
            destinationToTitleMap.put(R.id.rewardsFragment, R.string.rewards_title);
            destinationToTitleMap.put(R.id.profileFragment, R.string.profile_title);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                Integer titleResId = destinationToTitleMap.getOrDefault(destination.getId(), R.string.app_name);
                toolbarTitle.setText(activity.getString(titleResId));
            });
        } catch (Exception e) {
            Logger.e(TAG, "Error setting up Toolbar with NavController: " + e.getMessage(), e);
            throw new NavigationHelperException("Failed to configure Toolbar with NavController.", e);
        }
    }

    /**
     * Helper method to configure icon and tint for a menu item in the BottomNavigationView.
     *
     * @param navView   The BottomNavigationView instance.
     * @param itemId    The menu item ID.
     * @param iconResId The resource ID of the icon.
     * @param tintResId The resource ID of the color tint.
     * @throws NavigationHelperException If menu item configuration fails.
     */
    private void configureMenuItem(@NonNull BottomNavigationView navView,
                                   int itemId, int iconResId, int tintResId) {
        try {
            Context context = activity.getApplicationContext();
            Drawable icon = ContextCompat.getDrawable(context, iconResId);
            if (icon == null) {
                Logger.w(TAG, "Drawable resource not found for item: " + itemId);
                throw new NavigationHelperException("Drawable resource not found for menu item.");
            }

            icon.setTintList(ContextCompat.getColorStateList(context, tintResId));
            Objects.requireNonNull(navView.getMenu().findItem(itemId))
                    .setIcon(icon);
        } catch (Exception e) {
            Logger.e(TAG, "Error configuring menu item: " + e.getMessage(), e);
            throw new NavigationHelperException("Failed to configure menu item.", e);
        }
    }
}
