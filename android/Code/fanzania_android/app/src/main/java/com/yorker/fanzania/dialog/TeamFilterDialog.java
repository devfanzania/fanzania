package com.yorker.fanzania.dialog;

import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;

import com.yorker.fanzania.R;
import com.yorker.fanzania.databinding.TeamFilterDialogBinding;
import com.yorker.fanzania.views.screens.tournament.playerlist.adapter.TeamFilterListAdapter;
import com.yorker.fanzania.views.screens.tournament.playerlist.model.TeamFilterModel;

import java.util.LinkedList;
import java.util.Objects;

public class TeamFilterDialog extends AlertDialog.Builder implements TeamFilterListAdapter.ITeamList {
    private AlertDialog alertDialog;
    private LinkedList<String> teamName;
    private TeamFilterListAdapter tAdapter;

    public TeamFilterDialog(Context context, ITeamFilterDialog iTeamNameDialog, LinkedList<TeamFilterModel> list, LinkedList<String> teamname) {
        super(context);
        this.teamName = teamname;

        LayoutInflater li = LayoutInflater.from(context);
        TeamFilterDialogBinding binding = DataBindingUtil.inflate(li, R.layout.team_filter_dialog, null, false);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(binding.getRoot());

        for (TeamFilterModel obj : list) {
            if (teamName.contains(obj.getTeamShortName()))
                list.get(list.indexOf(obj)).setChecked(true);
        }

        binding.rvList.setLayoutManager(new GridLayoutManager(context, 2));
        tAdapter = new TeamFilterListAdapter(list, this, teamName);
        binding.rvList.setAdapter(tAdapter);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        binding.btnYes.setOnClickListener(view -> {
            iTeamNameDialog.returnteamname(teamName);
            alertDialog.dismiss();
        });

        binding.btnReset.setOnClickListener(view -> {
            teamName = new LinkedList<>();
            for (TeamFilterModel obj : list) {
                obj.setChecked(false);
            }
            iTeamNameDialog.returnteamname(teamName);
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

        void returnteamname(LinkedList<String> teamName);

        void clearTeamName();
    }


}
