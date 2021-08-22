package com.jina.cleanenergy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CityActivity extends AppCompatActivity {
    private static final int REQUEST_QUIZ = 0;
    private static int actionPoint = 0;

    private boolean cityCleaned = false;
    private boolean generatorCleaned = false;
    private boolean forestCleaned = false;

    private int co2 = 100;
    private int electricty = 100;
    private int temperature = 100;

    TextView actionPointTextView;
    ImageView coalPlant;
    ImageView city;
    ImageView forest;
    View smog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        init();

    }

    private void init() {
        View center = findViewById(R.id.center);
        coalPlant = findViewById(R.id.coalPlant);
        Glide.with(this)
                .load(R.drawable.coalplant_anim)
                .into(coalPlant);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, QuizActivity.class);
                startActivityForResult(intent, REQUEST_QUIZ);
            }
        });

        actionPointTextView = findViewById(R.id.actionPointText);

        city = findViewById(R.id.city);

        Glide.with(this)
                .load(R.drawable.city_anim)
                .into(city);


        forest = findViewById(R.id.forest);

        Glide.with(this)
                .load(R.drawable.brokenforest)
                .into(forest);

        city.setOnClickListener(v -> {
            if (cityCleaned) {
                return;
            }
            PopupMenu popup= new PopupMenu(CityActivity.this, center);

            getMenuInflater().inflate(R.menu.menu_quiz, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.information:
                        showMsgBox(R.string.informationCity, R.string.informationCity2);
                        break;

                    case R.id.action:
                        if (actionPoint >= 3) {
                            actionPoint -=3;
                            Glide.with(this)
                                    .load(R.drawable.city_clean_anim)
                                    .into(city);

                            cityCleaned = true;
                            refreshStatus();
                        } else {
                            showMsgBox(R.string.need3ap, R.string.need3ap);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            });

            popup.show();
        });

        coalPlant.setOnClickListener(v -> {
            if (generatorCleaned) {
                return;
            }
            PopupMenu popup= new PopupMenu(CityActivity.this, v);

            getMenuInflater().inflate(R.menu.menu_quiz, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.information:
                        showMsgBox(R.string.informationGenerator, R.string.informationGenerator2);
                        break;

                    case R.id.action:
                        if (actionPoint >= 3) {
                            actionPoint -=3;
                            generatorCleaned = true;
                            coalPlant.setVisibility(View.GONE);
                            findViewById(R.id.solarPlant).setVisibility(View.VISIBLE);
                            refreshStatus();
                        } else {
                            showMsgBox(R.string.need3ap, R.string.need3ap);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            });

            popup.show();
        });

        forest.setOnClickListener(v -> {
            if (forestCleaned) {
                return;
            }
            PopupMenu popup= new PopupMenu(CityActivity.this, center);

            getMenuInflater().inflate(R.menu.menu_quiz, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.information:
                        showMsgBox(R.string.informationForest, R.string.informationForest2);
                        break;

                    case R.id.action:
                        if (actionPoint >= 3) {
                            actionPoint -=3;
                            Glide.with(this)
                                    .load(R.drawable.forest_recovered)
                                    .into(forest);

                            forestCleaned = true;
                            refreshStatus();
                        } else {
                            showMsgBox(R.string.need3ap, R.string.need3ap);
                        }
                        break;

                    default:
                        break;
                }
                return false;
            });

            popup.show();
        });

        smog = findViewById(R.id.smog);
        smog.setBackgroundColor(0x88000000);

        showMsgBox(R.string.needhelp, R.string.needhelp2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_QUIZ:
                if (resultCode == RESULT_OK) {
                    actionPoint++;
                    actionPointTextView.setText(String.valueOf(actionPoint));
                }
                break;
        }
    }

    private void refreshStatus() {
        actionPointTextView.setText(String.valueOf(actionPoint));
        co2 = 100;
        temperature = 100;
        if (cityCleaned) {
            co2-= 30;
            temperature-= 20;
        }

        if (forestCleaned) {
            co2-= 20;
            temperature-= 10;
        }

        if (generatorCleaned) {
            co2 -= 30;
            temperature-= 30;
        }

        ProgressBar progressBarCo2 = findViewById(R.id.co2Progress);
        ProgressBar progressBarTemp = findViewById(R.id.temperatureProgress);

        progressBarCo2.setProgress(co2);
        progressBarTemp.setProgress(temperature);

        if (co2 == 20) {
            progressBarCo2.setProgressDrawable(getDrawable(R.drawable.progress_blue));
        } else if (co2 < 100) {
            progressBarCo2.setProgressDrawable(getDrawable(R.drawable.progress_yellow));
        } else {
            progressBarCo2.setProgressDrawable(getDrawable(R.drawable.progress_red));
        }

        if (temperature == 40) {
            progressBarTemp.setProgressDrawable(getDrawable(R.drawable.progress_blue));
        } else if (temperature < 100) {
            progressBarTemp.setProgressDrawable(getDrawable(R.drawable.progress_yellow));
        } else {
            progressBarTemp.setProgressDrawable(getDrawable(R.drawable.progress_red));
        }


        switch (co2) {
            case 100:
                smog.setBackgroundColor(0x88000000);
                break;

            case 20:
                smog.setBackgroundColor(0x00000000);
                break;

            default:
                smog.setBackgroundColor(0x44000000);
                break;
        }

        if (cityCleaned && generatorCleaned && forestCleaned) {
            showMsgBox(R.string.congratulation, R.string.congratulation2);
        }
    }

    private void showMsgBox(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }

    private void showMsgBox(int title, int msg) {
        showMsgBox(getString(title), getString(msg));
    }

}
