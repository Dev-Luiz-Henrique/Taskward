package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
 * This fragment allows users to create a new profile by providing their name and email.
 * It interacts with the UserController to handle the profile creation process and updates the UI accordingly.
 */
public class CreateProfileFragment extends Fragment {
    private UserController userController;

    private EditText editProfileName, editProfileEmail;
    private ImageButton buttonCreateProfile;

    public CreateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userController = new UserController(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        initializeUI(view);
        setupComponents();
        return view;
    }

    /**
     * Initializes the UI components of the fragment.
     *
     * @param view The View of the fragment.
     */
    private void initializeUI(@NonNull View view) {
        editProfileName = view.findViewById(R.id.editProfileName);
        editProfileEmail = view.findViewById(R.id.editProfileEmail);
        buttonCreateProfile = view.findViewById(R.id.buttonCreateProfile);
    }

    /**
     * Sets up the components of the fragment.
     */
    private void setupComponents() {
        buttonCreateProfile.setOnClickListener(v -> {
            if (validateProfileInputs()) {
                User user = buildProfileFromInputs();
                handleProfileCreation(user);
            }
        });
    }

    /**
     * Validates the profile inputs before creating a new profile.
     *
     * @return true if inputs are valid, false otherwise.
     */
    private boolean validateProfileInputs() {
        String name = editProfileName.getText().toString().trim();
        String email = editProfileEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(requireContext(), "Profile name is required", Toast.LENGTH_SHORT).show();
            editProfileName.setError("Profile name is required");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), "Profile email is required", Toast.LENGTH_SHORT).show();
            editProfileEmail.setError("Profile email is required");
            return false;
        }
        return true;
    }

    /**
     * Builds a User object from the user inputs.
     *
     * @return The User object.
     */
    @NonNull
    private User buildProfileFromInputs() {
        String name = editProfileName.getText().toString().trim();
        String email = editProfileEmail.getText().toString().trim();
        String photo = "default_profile_picture.png"; // TODO Implement profile photo selection logic

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoto(photo);
        return user;
    }

    /**
     * Handles the profile creation process, including feedback to the user.
     *
     * @param user The user object to be created.
     */
    private void handleProfileCreation(User user) {
        OperationResponse<User> response = userController.createUser(user);
        if (response.isSuccessful()) {
            Toast.makeText(requireContext(), "Profile created successfully!", Toast.LENGTH_SHORT).show();

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_createProfileFragment_to_profileFragment);
        }
        else
            Toast.makeText(requireContext(), "Failed to create profile. Try again.", Toast.LENGTH_SHORT).show();
    }
}