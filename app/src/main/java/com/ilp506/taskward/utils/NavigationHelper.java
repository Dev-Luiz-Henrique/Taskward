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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.exceptions.NavigationHelperException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A helper class for managing navigation-related functionality in the app.
 * It handles setting up the navigation controller, bottom navigation, and toolbar customization.
 * This class also updates the navigation bar and toolbar dynamically based on the active fragment.
 * Implements the LifecycleObserver interface to manage NavController lifecycle events.
 */
public class NavigationHelper implements LifecycleObserver {
    private static final String TAG = NavigationHelper.class.getSimpleName();

    private final AppCompatActivity activity;
    private final NavController navController;

    private final MutableLiveData<Integer> pointsLiveData = new MutableLiveData<>();

    /**
     * Constructs a NavigationHelper for managing navigation tasks in the specified activity.
     * It attempts to retrieve the NavController from the NavHostFragment and initializes navigation.
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
     * Returns a LiveData object that observes changes to the user's points.
     * This LiveData is used to update the UI when the user's points value changes.
     *
     * @return LiveData object for observing points changes.
     */
    public LiveData<Integer> getPointsLiveData() {
        return pointsLiveData;
    }

    /**
     * Updates the points value stored in LiveData.
     * This method will notify observers about the new points value.
     *
     * @param points The new points value to be updated.
     */
    public void updatePoints(int points) {
        pointsLiveData.setValue(points);
    }

    /**
     * Configures the BottomNavigationView to work with the NavController.
     * It also customizes the item icons and their respective tint colors.
     *
     * @param bottomNavigationView The BottomNavigationView instance to configure.
     * @throws NavigationHelperException If there is an error during configuration.
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
     * Configures the Toolbar dynamically based on the active fragment's navigation destination.
     * It updates the toolbar's title according to the current fragment.
     *
     * @param toolbar The Toolbar to be updated with the current fragment title.
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
     * Helper method to configure the icon and color tint for a specific menu item in the BottomNavigationView.
     *
     * @param navView   The BottomNavigationView instance.
     * @param itemId    The ID of the menu item to configure.
     * @param iconResId The resource ID of the icon for the menu item.
     * @param tintResId The resource ID of the color tint for the icon.
     * @throws NavigationHelperException If there is an error during the menu item configuration.
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
