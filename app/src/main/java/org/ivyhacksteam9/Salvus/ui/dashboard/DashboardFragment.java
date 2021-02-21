package org.ivyhacksteam9.Salvus.ui.dashboard;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.ivyhacksteam9.Salvus.R;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private TextView timerTextView; //
    private long startTime = 0;
    protected Handler timerHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        timerTextView = root.findViewById(R.id.timer);

        //Add listener to the button
        Button b = root.findViewById(R.id.button);
        b.setOnClickListener(v -> {
            Button b1 = (Button) v;
            if (b1.getText().equals(requireContext().getResources().getString(R.string.timerEnd))) {
                timerHandler.removeCallbacks(timerRunnable);
                b1.setText(getContext().getResources().getString(R.string.timerStart));
            } else {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                b1.setText(getContext().getResources().getString(R.string.timerEnd));
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = getView().findViewById(R.id.button);

        b.setText(getContext().getResources().getString(R.string.timerStart));
    }

    //Prepare timer
    Runnable timerRunnable = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            if(minutes%5==0){
                reminder(getView());
            }
            timerHandler.postDelayed(this, 500);
        }
    };

    //prepare notification
    protected void reminder(View view){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "notify9527")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(getContext().getResources().getString(R.string.reminderNotifyTitle))
                .setContentText(getContext().getResources().getString(R.string.reminderNotifyText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

}