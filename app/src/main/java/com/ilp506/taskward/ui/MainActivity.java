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
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.utils.CacheManager;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.NavigationHelper;
import com.ilp506.taskward.utils.OperationResponse;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

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

        setupNavigationHelper();
        setupObservers();
        loadUserAndInitializePoints();
    }

    /**
     * Sets up the NavigationHelper and related components.
     */
    private void setupNavigationHelper() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        pointsTextView = toolbar.findViewById(R.id.toolbar_points);

        navigationHelper = new NavigationHelper(this);
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
        } else {
            handleUserLoadingError(response.getMessage());
        }
    }

    /**
     * Handles errors during user loading.
     *
     * @param errorMessage The error message to log and display.
     */
    private void handleUserLoadingError(String errorMessage) {
        Logger.e(TAG, "Error fetching user: " + errorMessage);
        Toast.makeText(this, "Error fetching user", Toast.LENGTH_SHORT).show();
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
