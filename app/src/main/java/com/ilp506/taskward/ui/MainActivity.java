package com.ilp506.taskward.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.OperationResponse;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private UserController userController;
    private int userId;

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

        userController = new UserController(this);
        testCreateUser();       testGetUserById();
        testUpdateUser();       testGetUserById();
        testUpdateUserPoints(); testGetUserById();
        testDeleteUser();       testGetUserById();
    }

    private void testCreateUser() {
        User user = new User();
        user.setName("John Doe");
        user.setPhoto("path/to/photo");

        OperationResponse<User> response = userController.createUser(user);

        if (response.isSuccessful()) {
            Logger.d(TAG, "testCreateUser: " + response.getMessage());
            userId = response.getData().getId();
        } else {
            Logger.e(TAG, "testCreateUser: " + response.getMessage());
        }
    }

    private void testGetUserById() {
        OperationResponse<User> response = userController.getUserById(userId);

        if (response.isSuccessful()) {
            Logger.d(TAG, "testGetUserById: " + response.getMessage() + "\nUser: " + response.getData().toString());
        } else {
            Logger.e(TAG, "testGetUserById: " + response.getMessage());
        }
    }

    private void testUpdateUser() {
        User user = new User();
        user.setId(userId);
        user.setName("John Updated");
        user.setPhoto("path/to/updated/photo");
        user.setPoints(200);

        OperationResponse<User> response = userController.updateUser(user);

        if (response.isSuccessful()) {
            Logger.d(TAG, "testUpdateUser: " + response.getMessage());
        } else {
            Logger.e(TAG, "testUpdateUser: " + response.getMessage());
        }
    }

    private void testDeleteUser() {
        OperationResponse<Void> response = userController.deleteUser(userId);

        if (response.isSuccessful()) {
            Logger.d(TAG, "testDeleteUser: " + response.getMessage());
        } else {
            Logger.e(TAG, "testDeleteUser: " + response.getMessage());
        }
    }

    private void testUpdateUserPoints() {
        int newPoints = 300;

        OperationResponse<User> response = userController.updateUserPoints(userId, newPoints);

        if (response.isSuccessful()) {
            Logger.d(TAG, "testUpdateUserPoints: " + response.getMessage());
        } else {
            Logger.e(TAG, "testUpdateUserPoints: " + response.getMessage());
        }
    }
}