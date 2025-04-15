package com.example.filmworld.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.filmworld.R;
import com.example.filmworld.activities.MainActivity;


public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText emailEditText = view.findViewById(R.id.emailEditText);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.buttonLogin);
        Button registerButton = view.findViewById(R.id.buttonRegister);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.login(email, password, view);
            } else {
                Toast.makeText(getContext(), "Error: Activity not found", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment));

        return view;
    }
    // In your LoginFragment.java

}