package com.example.android.americanfootballscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int scoreTeamA = 0;
    int scoreTeamB = 0;

    //increase the score for Team A by 6 points
    public void touchdownForTeamA(View v){
        scoreTeamA += 6;
        displayForTeamA(scoreTeamA);
    }

    //increase the score for Team A by 1 point
    public void extraPointForTeamA(View v){
        scoreTeamA ++;
        displayForTeamA(scoreTeamA);
    }

    //increase the score for Team A by 3 points
    public void fieldGoalForTeamA(View v){
        scoreTeamA += 3;
        displayForTeamA(scoreTeamA);
    }

    //increase the score for Team A by 2 points
    public void safetyForTeamA(View v){
        scoreTeamA += 2;
        displayForTeamA(scoreTeamA);
    }

    //increase the score for Team B by 6 points
    public void touchdownForTeamB(View v){
        scoreTeamB += 6;
        displayForTeamB(scoreTeamB);
    }

    //increase the score for Team B by 1 point
    public void extraPointForTeamB(View v){
        scoreTeamB ++;
        displayForTeamB(scoreTeamB);
    }

    //increase the score for Team B by 3 points
    public void fieldGoalForTeamB(View v){
        scoreTeamB += 3;
        displayForTeamB(scoreTeamB);
    }

    //increase the score for Team B by 2 points
    public void safetyForTeamB(View v){
        scoreTeamB += 2;
        displayForTeamB(scoreTeamB);
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    public void resetScores(View v){
        scoreTeamA = 0;
        scoreTeamB = 0;
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }
}
