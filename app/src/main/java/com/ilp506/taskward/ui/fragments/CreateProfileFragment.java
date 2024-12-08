package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.utils.OperationResponse;

/**
 * Fragment for creating profiles in the TaskWard app.
 * Handles user input, validations, and profile creation logic.
 */
public class CreateProfileFragment extends Fragment {

    private UserController userController;

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        userController = new UserController(requireContext());

        setupCreateProfileButton(view);

        return view;
    }

    /**
     * Sets up the create profile button and its click listener.
     * Validates user inputs and triggers profile creation.
     *
     * @param view The root view of the fragment.
     */
    private void setupCreateProfileButton(@NonNull View view) {
        ImageButton buttonCreateProfile = view.findViewById(R.id.buttonCreateProfile);

        buttonCreateProfile.setOnClickListener(v -> {
            EditText editProfileName = view.findViewById(R.id.editProfileName);
            EditText editProfileEmail = view.findViewById(R.id.editProfileEmail);
            // TODO Implement profile photo selection logic

            String name = editProfileName.getText().toString().trim();
            String email = editProfileEmail.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Profile name is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Profile email is required", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhoto("default_profile_picture.png");

            handleProfileCreation(user);
        });
    }

    /**
     * Handles the profile creation process, including feedback to the user.
     * @param user The user object to be created.
     */
    private void handleProfileCreation(User user) {
        OperationResponse<User> response = userController.createUser(user);
        if (response.isSuccessful()) {
            Toast.makeText(requireContext(), "Profile created successfully!", Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation to profile list or details screen
        }
        else {
            Toast.makeText(requireContext(), "Failed to create profile. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}