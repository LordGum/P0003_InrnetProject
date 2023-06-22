package com.example.p0003_internet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private Button button;
    private ProgressBar progressBar;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.loadJoke();
        clickOnButton();

        viewModel.getIsError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if(isError) {
                    Toast.makeText(MainActivity.this,
                            "No internet",
                            Toast.LENGTH_SHORT
                            ).show();
                }
            }
        });
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading) {
                    textView.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        viewModel.getIsPunchlineVisible().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPunchlineVisible) {
                if(isPunchlineVisible) {
                    button.setText(R.string.punchline);
                    viewModel.loadJoke();
                    textView2.setVisibility(View.INVISIBLE);
                } else {
                    textView2.setVisibility(View.VISIBLE);
                    button.setText(R.string.next_joke);
                }
            }
        });
        viewModel.getJoke().observe(this, new Observer<Joke>() {
            @Override
            public void onChanged(Joke joke) {
                textView.setText(joke.getSetup());
                textView2.setText(joke.getPunchline());
            }
        });
    }

    private void init() {
        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
    }

    private void clickOnButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.changeIs();
            }
        });
    }
}