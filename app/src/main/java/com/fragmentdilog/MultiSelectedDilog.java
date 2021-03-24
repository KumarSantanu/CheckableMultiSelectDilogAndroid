package com.fragmentdilog;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class MultiSelectedDilog extends AppCompatDialogFragment implements SearchView.OnQueryTextListener, View.OnClickListener{

    private TextView dialogTitle, dialogSubmit, dialogCancel;
    public ArrayList<MultilineModel> mainListOfAdapter = new ArrayList<>();
    public static ArrayList<Integer> selectedIdsForCallback = new ArrayList<>();
    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();
    private MultiLineAdpter mutliSelectAdapter;

    private String title;
    private float titleSize = 25;
    private String positiveText = "DONE";
    private String negativeText = "CANCEL";


    private ArrayList<Integer> tempPreviouslySelectedIdsList = new ArrayList<>();
    private ArrayList<MultilineModel> tempMainListOfAdapter = new ArrayList<>();

    private SubmitCallbackListener submitCallbackListener;

    private int minSelectionLimit = 1;
    private String minSelectionMessage = null;
    private int maxSelectionLimit = 0;
    private String maxSelectionMessage = null;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.fragment_dilog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RecyclerViewEmptySupport mrecyclerView =  dialog.findViewById(R.id.recycler_view);
        SearchView searchView =  dialog.findViewById(R.id.search_view);
        dialogTitle =  dialog.findViewById(R.id.title);
        dialogSubmit =  dialog.findViewById(R.id.done);
        dialogCancel =  dialog.findViewById(R.id.cancel);

        mrecyclerView.setEmptyView(dialog.findViewById(R.id.list_empty1));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mrecyclerView.setLayoutManager(layoutManager);

        dialogSubmit.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

        settingValues();

        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, previouslySelectedIdsList);
        mutliSelectAdapter = new MultiLineAdpter(mainListOfAdapter, getContext());
        mrecyclerView.setAdapter(mutliSelectAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();


        return dialog;
    }

    private void settingValues() {
        dialogTitle.setText(title);
        dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        dialogSubmit.setText(positiveText.toUpperCase());
        dialogCancel.setText(negativeText.toUpperCase());
    }

    public MultiSelectedDilog title(String title) {
        this.title = title;
        return this;
    }

    public MultiSelectedDilog titleSize(float titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public MultiSelectedDilog positiveText(@NonNull String message) {
        this.positiveText = message;
        return this;
    }

    public MultiSelectedDilog negativeText(@NonNull String message) {
        this.negativeText = message;
        return this;
    }

    public MultiSelectedDilog preSelectIDsList(ArrayList<Integer> list) {
        this.previouslySelectedIdsList = list;
        this.tempPreviouslySelectedIdsList = new ArrayList<>(previouslySelectedIdsList);
        return this;
    }

    public MultiSelectedDilog multiSelectList(ArrayList<MultilineModel> list) {
        this.mainListOfAdapter = list;
        this.tempMainListOfAdapter = new ArrayList<>(mainListOfAdapter);
        if(maxSelectionLimit == 0)
            maxSelectionLimit = list.size();
        return this;
    }

    public MultiSelectedDilog setMaxSelectionLimit(int limit){
        this.maxSelectionLimit = limit;
        return this;
    }

    public MultiSelectedDilog setMaxSelectionMessage(String message) {
        this.maxSelectionMessage = message;
        return this;
    }

    public MultiSelectedDilog setMinSelectionLimit(int limit){
        this.minSelectionLimit = limit;
        return this;
    }

    public MultiSelectedDilog setMinSelectionMessage(String message) {
        this.minSelectionMessage = message;
        return this;
    }

    public MultiSelectedDilog onSubmit(@NonNull SubmitCallbackListener callback) {
        this.submitCallbackListener = callback;
        return this;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.done) {
            ArrayList<Integer> callBackListOfIds = selectedIdsForCallback;

            if (callBackListOfIds.size() >= minSelectionLimit) {
                if (callBackListOfIds.size() <= maxSelectionLimit) {

                    //to remember last selected ids which were successfully done
                    tempPreviouslySelectedIdsList = new ArrayList<>(callBackListOfIds);

                    if(submitCallbackListener !=null) {
                        submitCallbackListener.onSelected(callBackListOfIds, getSelectNameList(), getSelectedDataString());
                    }
                    dismiss();
                } else {
                    String youCan = getResources().getString(R.string.you_can_only_select_upto);
                    String options = getResources().getString(R.string.options);
                    String option = getResources().getString(R.string.option);
                    String message = "";

                    if(this.maxSelectionMessage != null) {
                        message = maxSelectionMessage;
                    }
                    else {
                        if (maxSelectionLimit > 1)
                            message = youCan + " " + maxSelectionLimit + " " + options;
                        else
                            message = youCan + " " + maxSelectionLimit + " " + option;
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            } else {
                String pleaseSelect = getResources().getString(R.string.please_select_atleast);
                String options = getResources().getString(R.string.options);
                String option = getResources().getString(R.string.option);
                String message = "";

                if(this.minSelectionMessage != null) {
                    message = minSelectionMessage;
                }
                else {
                    if (minSelectionLimit > 1)
                        message = pleaseSelect + " " + minSelectionLimit + " " + options;
                    else
                        message = pleaseSelect + " " + minSelectionLimit + " " + option;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }

        if (view.getId() == R.id.cancel) {
            if(submitCallbackListener!=null){
                selectedIdsForCallback.clear();
                selectedIdsForCallback.addAll(tempPreviouslySelectedIdsList);
                submitCallbackListener.onCancel();
            }
            dismiss();
        }
    }

    private String getSelectedDataString() {
        String data = "";
        for (int i = 0; i < tempMainListOfAdapter.size(); i++) {
            if (checkForSelection(tempMainListOfAdapter.get(i).getId())) {
                data = data + ", " + tempMainListOfAdapter.get(i).getDisplayName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }

    private ArrayList<String> getSelectNameList() {
        ArrayList<String> names = new ArrayList<>();
        for(int i=0;i<tempMainListOfAdapter.size();i++){
            if(checkForSelection(tempMainListOfAdapter.get(i).getId())){
                names.add(tempMainListOfAdapter.get(i).getDisplayName());
            }
        }
        return names;
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectedDilog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectedDilog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdsForCallback = previouslySelectedIdsList;
        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, selectedIdsForCallback);
        ArrayList<MultilineModel> filteredlist = filter(mainListOfAdapter, newText);
        mutliSelectAdapter.setData(filteredlist, newText.toLowerCase(), mutliSelectAdapter);
        return false;
    }

    private ArrayList<MultilineModel> setCheckedIDS(ArrayList<MultilineModel> multiselectdata, ArrayList<Integer> listOfIdsSelected) {
        for (int i = 0; i < multiselectdata.size(); i++) {
            multiselectdata.get(i).setSelected(false);
            for (int j = 0; j < listOfIdsSelected.size(); j++) {
                if (listOfIdsSelected.get(j).equals(multiselectdata.get(i).getId())) {
                    multiselectdata.get(i).setSelected(true);
                }
            }
        }
        return multiselectdata;
    }


    public interface SubmitCallbackListener {
        void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String commonSeperatedData);
        void onCancel();
    }

    private ArrayList<MultilineModel> filter(ArrayList<MultilineModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<MultilineModel> filteredModelList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredModelList.addAll(models);
            return filteredModelList;
        }

        for (MultilineModel model : models) {
            final String name = model.getDisplayName().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }


        return filteredModelList;
    }
}
