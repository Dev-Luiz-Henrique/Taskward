package com.ilp506.taskward.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.exceptions.ExceptionHandler;
import com.ilp506.taskward.utils.CacheManager;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.NavigationHelper;
import com.ilp506.taskward.utils.OperationResponse;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final MutableLiveData<NavigationHelper> navigationHelperLiveData = new MutableLiveData<>();
    private NavigationHelper navigationHelper;

    private Toolbar toolbar;
    private TextView pointsTextView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ExceptionHandler.getInstance().setErrorNotifier(message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        );

        initializeUI();
        findViewById(R.id.nav_host_fragment).post(this::initializeNavigation);
    }

    /**
     * Initializes the UI components and sets up window insets for edge-to-edge display.
     */
    private void initializeUI() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.custom_toolbar);
        pointsTextView = toolbar.findViewById(R.id.toolbar_points);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    /**
     * Initializes the navigation helper and sets up components and observers.
     */
    private void initializeNavigation() {
        navigationHelper = new NavigationHelper(this);
        navigationHelperLiveData.setValue(navigationHelper);

        setupComponents();
        loadUserAndInitializePoints();
    }

    /**
     * Configures the toolbar and bottom navigation, and observes points updates.
     */
    private void setupComponents() {
        navigationHelper.setupBottomNavigationView(bottomNavigationView);
        navigationHelper.setupToolbar(toolbar);

        navigationHelper.getPointsLiveData().observe(this, points -> {
            if (points != null) pointsTextView.setText(String.valueOf(points));
        });
    }

    /**
     * Loads the user from cache and initializes their points.
     * If the user is not found, navigates to the profile creation screen.
     */
    private void loadUserAndInitializePoints() {
        User user = fetchUserFromCache();
        if (user != null)
            navigationHelper.updatePoints(user.getPoints());
        else {
            Toast.makeText(this, "User not found. Please create a profile.",
                    Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.action_global_to_createProfileFragment);
        }
    }

    /**
     * Fetches the user from the cache using the UserController and handles errors via ExceptionHandler.
     *
     * @return The user object if found, otherwise null.
     */
    @Nullable
    private User fetchUserFromCache() {
        CacheManager cacheManager = new CacheManager(this);
        UserController userController = new UserController(this);

        OperationResponse<User> response = userController.getUserById(cacheManager.getUserId());
        if (response.isSuccessful()) {
            return response.getData();
        }

        Logger.e(TAG, "Error fetching user: " + response.getMessage());
        return null;
    }

    /**
     * Returns the LiveData object for the navigation helper.
     *
     * @return The LiveData object for the navigation helper.
     */
    public LiveData<NavigationHelper> getNavigationHelperLiveData() {
        return navigationHelperLiveData;
    }

    /**
     * Returns the navigation helper instance.
     *
     * @return The navigation helper.
     */
    public NavigationHelper getNavigationHelper() {
        return navigationHelper;
    }
}
