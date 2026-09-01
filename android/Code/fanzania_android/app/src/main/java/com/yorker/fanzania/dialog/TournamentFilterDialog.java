package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.TeamFilterDialogBinding;
import com.yorker.fanzania.databinding.TournamentFilterDialogBinding;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.TournamentFilterListAdapter;
import com.yorker.fanzania.views.screens.matchcontest.fragments.home.model.TournamentModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TournamentFilterDialog extends AlertDialog.Builder implements TournamentFilterListAdapter.ITeamList {
    private AlertDialog alertDialog;
    private List<String> teamName;
    private TournamentFilterListAdapter tAdapter;

    public TournamentFilterDialog(Context context, ITeamFilterDialog iTeamNameDialog, List<TournamentModel> list, List<String> teamname) {
        super(context);
        this.teamName = teamname;

        LayoutInflater li = LayoutInflater.from(context);
        TournamentFilterDialogBinding binding = DataBindingUtil.inflate(li, R.layout.tournament_filter_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        for (TournamentModel obj : list) {
            if (teamName.contains(obj.getTournamentName()))
                list.get(list.indexOf(obj)).setChecked(true);
        }

        binding.rvList.setLayoutManager(new GridLayoutManager(context, 2));
        tAdapter = new TournamentFilterListAdapter(list, this);
        binding.rvList.setAdapter(tAdapter);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            iTeamNameDialog.returnTName(teamName);
            alertDialog.dismiss();
        });

        binding.btnReset.setOnClickListener(view -> {
            teamName = new LinkedList<>();
            for (TournamentModel obj : list) {
                obj.setChecked(false);
            }
            iTeamNameDialog.returnTName(teamName);
            alertDialog.dismiss();
        });

        binding.btnNo.setOnClickListener(view -> {
                    teamName = new LinkedList<>();
                    iTeamNameDialog.clearTeamName();
                    alertDialog.dismiss();
                }
        );
    }

    @Override
    public void OnSelectTeam(String teamShortName) {
        teamName.add(teamShortName);
    }

    @Override
    public void OnRemoveTeam(String teamShortName) {
        if (teamName.size() > 0)
            teamName.remove(teamName.indexOf(teamShortName));
    }

    public interface ITeamFilterDialog {

        void returnTName(List<String> teamName);

        void clearTeamName();
    }


}
