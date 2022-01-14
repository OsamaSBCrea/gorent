package com.example.gorent.ui.property;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gorent.R;
import com.example.gorent.data.models.Property;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.ui.entities.property.PropertyAdapter;
import com.example.gorent.ui.entities.property.PropertyDetailsListener;
import com.example.gorent.util.shared.ToastMaker;
import com.example.gorent.util.shared.keys.SharedKeys;
import com.example.gorent.util.shared.pagination.Page;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PropertyListFragment extends Fragment {

    @BindView(R.id.properties_recycler)
    RecyclerView propertiesRecyclerView;

    private RepositoryManager repositoryManager;

    private List<Disposable> disposables;
    
    private PropertyAdapter propertyAdapter;

    private ProgressDialog progressDialog;

    public static PropertyListFragment newInstance() {
        PropertyListFragment fragment = new PropertyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        this.disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            Log.d("ARGS", getArguments().toString());
            propertyAdapter.setAll(((Page<Property>) getArguments().getSerializable(SharedKeys.BUNDLE_PROPERTIES_PAGE)).getContent());
        } else {
            this.getProperties();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_property_list, container, false);
        ButterKnife.bind(this, root);
        repositoryManager = RepositoryManager.getInstance(getContext().getApplicationContext());
        disposables = new ArrayList<>();
        this.initViews();
        return root;
    }

    private void initViews() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        propertiesRecyclerView.setLayoutManager(layoutManager);
        propertyAdapter = new PropertyAdapter(new ArrayList<>(), (PropertyDetailsListener) getActivity());
        propertiesRecyclerView.setAdapter(propertyAdapter);
    }

    private void getProperties() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Getting your properties...");
        disposables.add(repositoryManager.getPropertyRepository().getProperties(0, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(properties -> {
                    propertyAdapter.setAll(properties.getContent());
                    progressDialog.hide();
                }, (error) -> {
                    progressDialog.hide();
                    Log.e("GET Properties", error.getMessage());
                    ToastMaker.showLongToast(getActivity(), R.string.alrt_get_properties_list);
                }));
    }
}