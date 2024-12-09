package com.ilp506.taskward.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.utils.CacheManager;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.NavigationHelper;
import com.ilp506.taskward.utils.OperationResponse;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final MutableLiveData<NavigationHelper> navigationHelperLiveData = new MutableLiveData<>();
    private NavigationHelper navigationHelper;
    private TextView pointsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.nav_host_fragment).post(() -> {
            navigationHelper = new NavigationHelper(this);
            navigationHelperLiveData.setValue(navigationHelper);

            setupComponents();
            setupObservers();
            loadUserAndInitializePoints();
        });
    }

    /**
     * Sets up the components and UI elements of the activity.
     */
    private void setupComponents() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        pointsTextView = toolbar.findViewById(R.id.toolbar_points);

        navigationHelper.setupBottomNavigationView(bottomNavigationView);
        navigationHelper.setupToolbar(toolbar);
    }

    /**
     * Sets up observers for LiveData provided by NavigationHelper.
     */
    private void setupObservers() {
        navigationHelper.getPointsLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer points) {
                if (points != null) pointsTextView.setText(String.valueOf(points));
            }
        });
    }

    /**
     * Loads the user from the cache and initializes the points display.
     */
    private void loadUserAndInitializePoints() {
        CacheManager cacheManager = new CacheManager(this);
        UserController userController = new UserController(this);
        OperationResponse<User> response = userController.getUserById(cacheManager.getUserId());

        if (response.isSuccessful()) {
            User user = response.getData();
            navigationHelper.updatePoints(user.getPoints());
        }
        else {
            Logger.e(TAG, "Error fetching user: " + response.getMessage());
            Toast.makeText(this, "User not found. Please create a profile.",
                    Toast.LENGTH_SHORT).show();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.action_global_to_createProfileFragment);
        }
    }


    /**
     * Exposes the NavigationHelper as LiveData for fragments.
     *
     * @return LiveData containing the NavigationHelper instance.
     */
    public LiveData<NavigationHelper> getNavigationHelperLiveData() {
        return navigationHelperLiveData;
    }

    /**
     * Returns the NavigationHelper instance.
     *
     * @return The NavigationHelper instance.
     */
    public NavigationHelper getNavigationHelper() {
        return navigationHelper;
    }
}
