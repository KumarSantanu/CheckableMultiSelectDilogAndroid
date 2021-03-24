package com.fragmentdilog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MultiSelectedDilog multiSelectDialog;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Integer> alreadySelectedCountries = new ArrayList<>();
        alreadySelectedCountries.add(1);
        alreadySelectedCountries.add(3);
        alreadySelectedCountries.add(4);
        alreadySelectedCountries.add(7);

        ArrayList<MultilineModel> listOfCountries= new ArrayList<>();
        listOfCountries.add(new MultilineModel(1,"INDIA"));
        listOfCountries.add(new MultilineModel(2,"USA"));
        listOfCountries.add(new MultilineModel(3,"UK"));
        listOfCountries.add(new MultilineModel(4,"UAE"));
        listOfCountries.add(new MultilineModel(5,"JAPAN"));
        listOfCountries.add(new MultilineModel(6,"SINGAPORE"));
        listOfCountries.add(new MultilineModel(7,"CHINA"));
        listOfCountries.add(new MultilineModel(8,"RUSSIA"));
        listOfCountries.add(new MultilineModel(9,"BANGLADESH"));
        listOfCountries.add(new MultilineModel(10,"BELGIUM"));
        listOfCountries.add(new MultilineModel(11,"DENMARK"));
        listOfCountries.add(new MultilineModel(12,"GERMANY"));
        listOfCountries.add(new MultilineModel(13,"HONG KONG"));
        listOfCountries.add(new MultilineModel(14,"INDONESIA"));
        listOfCountries.add(new MultilineModel(15,"NETHERLAND"));
        listOfCountries.add(new MultilineModel(16,"NEW ZEALAND"));
        listOfCountries.add(new MultilineModel(17,"PORTUGAL"));
        listOfCountries.add(new MultilineModel(18,"KUWAIT"));
        listOfCountries.add(new MultilineModel(19,"QATAR"));
        listOfCountries.add(new MultilineModel(20,"SAUDI ARABIA"));
        listOfCountries.add(new MultilineModel(21,"SRI LANKA"));
        listOfCountries.add(new MultilineModel(130,"CANADA"));


        multiSelectDialog = new MultiSelectedDilog()
                .title(getResources().getString(R.string.app_name)) //setting title for dialog
                .titleSize(25)
                .positiveText("Done")
                .negativeText("Cancel")
                .setMinSelectionLimit(0)
                .setMaxSelectionLimit(listOfCountries.size())
                .preSelectIDsList(alreadySelectedCountries) //List of ids that you need to be selected
                .multiSelectList(listOfCountries) // the multi select model list with ids and name
                .onSubmit(new MultiSelectedDilog.SubmitCallbackListener() {
                    @Override
                    public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                        //will return list of selected IDS
                        for (int i = 0; i < selectedIds.size(); i++) {
                            Toast.makeText(MainActivity.this, "Selected Ids : " + selectedIds.get(i) + "\n" +
                                    "Selected Names : " + selectedNames.get(i) + "\n" +
                                    "DataString : " + dataString, Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"Dialog cancelled");

                    }
                });


    }

    public void showDilog(View view) {
        multiSelectDialog.show(getSupportFragmentManager(), "h");
    }
}