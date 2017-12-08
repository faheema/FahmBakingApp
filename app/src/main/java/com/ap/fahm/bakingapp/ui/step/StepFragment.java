/*
 * Copyright 2017.  FaheemApps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ap.fahm.bakingapp.ui.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ap.fahm.bakingapp.R;
import com.ap.fahm.bakingapp.model.pojo.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements StepAdapter.StepClickListener {

    @BindView(R.id.rv_step)
    RecyclerView rvStep;

    private StepAdapter stepAdapter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int currentStepIndex;
    private boolean isTablet;
    private StepFragmentListener fragmentListener;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(ArrayList<Step> steps, boolean isTablet) {
        StepFragment stepFragment = new StepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", steps);
        bundle.putBoolean("isTablet", isTablet);
        stepFragment.setArguments(bundle);

        return stepFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (StepFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList("steps");
            isTablet = getArguments().getBoolean("isTablet");
        }
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvStep.setLayoutManager(layoutManager);
        stepAdapter = new StepAdapter(steps, this);
        rvStep.setAdapter(stepAdapter);
    }

    @Override
    public void onStepItemClick(Step step) {
        currentStepIndex = steps.indexOf(step);
        if (isTablet) {
            fragmentListener.onStepClicked(step);
        } else {
            Intent intent = new Intent(getActivity(), StepDetailActivity.class);
            intent.putParcelableArrayListExtra("steps", steps);
            intent.putExtra("currentStepIndex", currentStepIndex);
            startActivity(intent);
        }
    }

    public interface StepFragmentListener {
        void onStepClicked(Step step);
    }
}
