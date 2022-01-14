package com.example.gorent.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.models.Property;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.ui.entities.property.PropertyAdapter;
import com.example.gorent.ui.entities.property.PropertyDetailsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    @BindView(R.id.latest_properties_recycler)
    RecyclerView latestPropertiesRecyclerView;

    private RepositoryManager repositoryManager;

    private List<Disposable> disposables;

    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        repositoryManager = RepositoryManager.getInstance(getContext().getApplicationContext());
        disposables = new ArrayList<>();
        this.initViews();
        this.getProperties();
        return root;
    }

    private void initViews() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        latestPropertiesRecyclerView.setLayoutManager(layoutManager);
        PropertyAdapter propertyAdapter = new PropertyAdapter(new ArrayList<>(), (PropertyDetailsListener) getActivity());
        latestPropertiesRecyclerView.setAdapter(propertyAdapter);
    }

    @Override
    public void onPause() {
        this.disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    private void getProperties() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Getting latest properties...");
        disposables.add(repositoryManager.getPropertyRepository().getLatestProperties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(properties -> {
                    progressDialog.hide();
                    latestPropertiesRecyclerView.setAdapter(new PropertyAdapter(properties, (PropertyDetailsListener) getActivity()));
                }, (error) -> {
                    progressDialog.hide();
                }));
    }
}