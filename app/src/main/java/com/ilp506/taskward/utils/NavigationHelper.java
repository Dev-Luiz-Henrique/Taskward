package com.ilp506.taskward.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.exceptions.codes.NavigationErrorCode;
import com.ilp506.taskward.exceptions.custom.NavigationHelperException;

/**
 * A helper class for managing navigation-related functionality in the app.
 * Handles setting up navigation, bottom navigation, and toolbar updates.
 */
public class NavigationHelper {
    private static final String TAG = NavigationHelper.class.getSimpleName();

    private final NavController navController;
    private final MutableLiveData<Integer> pointsLiveData = new MutableLiveData<>();

    /**
     * Constructor for NavigationHelper.
     *
     * @param context The activity context, which must contain a NavHostFragment.
     * @throws NavigationHelperException If the NavHostFragment is not found.
     */
    public NavigationHelper(@NonNull Context context) {
        if (!(context instanceof AppCompatActivity)) {
            throw NavigationHelperException.fromError(
                    NavigationErrorCode.UNEXPECTED_ERROR,
                    "Context must be an instance of AppCompatActivity."
            );
        }

        AppCompatActivity activity = (AppCompatActivity) context;
        try {
            this.navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        } catch (IllegalStateException e) {
            throw NavigationHelperException.fromError(
                    NavigationErrorCode.UNEXPECTED_ERROR,
                    "NavHostFragment not found in the activity. Ensure it exists in the layout.",
                    e
            );
        }
    }

    /**
     * Observes changes to the user's points.
     *
     * @return LiveData observing points changes.
     */
    public LiveData<Integer> getPointsLiveData() {
        return pointsLiveData;
    }

    /**
     * Updates the points value and notifies observers.
     *
     * @param points New points value.
     */
    public void updatePoints(int points) {
        pointsLiveData.setValue(points);
    }

    /**
     * Configures the BottomNavigationView for use with the NavController.
     *
     * @param bottomNavigationView The BottomNavigationView instance.
     */
    public void setupBottomNavigationView(@NonNull BottomNavigationView bottomNavigationView) {
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Customize item selection listener for additional behavior
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tasksFragment)
                return navController.popBackStack(R.id.tasksFragment, false);
            else {
                navController.navigate(item.getItemId());
                return true;
            }
        });

        bottomNavigationView.setItemIconTintList(null);
        configureMenuItem(bottomNavigationView, R.id.tasksFragment,
                R.drawable.ic_menu_task, R.color.tasks_item_color);
        configureMenuItem(bottomNavigationView, R.id.rewardsFragment,
                R.drawable.ic_menu_reward, R.color.rewards_item_color);
        configureMenuItem(bottomNavigationView, R.id.profileFragment,
                R.drawable.ic_menu_profile, R.color.profile_item_color);
    }

    /**
     * Configures the Toolbar to update its title dynamically.
     *
     * @param toolbar The Toolbar instance.
     */
    public void setupToolbar(@NonNull Toolbar toolbar) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getLabel() != null) {
                String label = destination.getLabel().toString();
                TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
                toolbarTitle.setText(label);
            }
        });
    }

    /**
     * Configures a menu item in the BottomNavigationView with a custom icon and tint color.
     *
     * @param navView   The BottomNavigationView instance.
     * @param itemId    ID of the menu item to configure.
     * @param iconResId Drawable resource for the icon.
     * @param tintResId Color resource for the tint.
     */
    private void configureMenuItem(@NonNull BottomNavigationView navView, int itemId, int iconResId, int tintResId) {
        MenuItem menuItem = navView.getMenu().findItem(itemId);
        if (menuItem == null) {
            throw NavigationHelperException.fromError(
                    NavigationErrorCode.INVALID_DESTINATION,
                    "Menu item not found for ID: " + itemId
            );
        }

        Drawable icon = ContextCompat.getDrawable(navView.getContext(), iconResId);
        if (icon == null) {
            throw NavigationHelperException.fromError(
                    NavigationErrorCode.UNEXPECTED_ERROR,
                    "Drawable resource not found for menu item ID: " + itemId
            );
        }

        icon.setTintList(ContextCompat.getColorStateList(navView.getContext(), tintResId));
        menuItem.setIcon(icon);
    }

    /**
     * Navigates to a specific destination if it's not already the current destination.
     *
     * @param destinationId ID of the destination to navigate to.
     * @throws NavigationHelperException If the current destination is null or the NavController is invalid.
     */
    public void navigateTo(int destinationId) {
        if (navController.getCurrentDestination() == null) {
            throw NavigationHelperException.fromError(
                    NavigationErrorCode.UNEXPECTED_ERROR,
                    "Current destination is null. Navigation cannot proceed."
            );
        }

        if (navController.getCurrentDestination().getId() != destinationId)
            navController.navigate(destinationId);
    }
}
