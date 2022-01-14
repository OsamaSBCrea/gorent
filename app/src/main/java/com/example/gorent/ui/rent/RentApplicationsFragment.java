package com.example.gorent.ui.rent;

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
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.RentRepository;
import com.example.gorent.ui.entities.property.PropertyAdapter;
import com.example.gorent.ui.entities.property.PropertyDetailsListener;
import com.example.gorent.ui.entities.rent.RentApplicationAdapter;
import com.example.gorent.ui.entities.rent.RentApplicationDetailsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RentApplicationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RentApplicationsFragment extends Fragment {

    @BindView(R.id.rent_applications_recycler)
    RecyclerView rentApplicationsRecycler;

    private RentApplicationAdapter rentApplicationAdapter;

    private RentRepository rentRepository;

    private ProgressDialog progressDialog;

    private final List<Disposable> disposables = new ArrayList<>();

    private static final String TAG = "[RentApplicationsFragment]";

    public RentApplicationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment RentApplicationsFragment.
     */
    public static RentApplicationsFragment newInstance() {
        RentApplicationsFragment fragment = new RentApplicationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rent_applications, container, false);
        ButterKnife.bind(this, view);
        rentRepository = RepositoryManager.getInstance(getActivity().getApplicationContext()).getRentRepository();
        this.initViews();
        return view;
    }

    @Override
    public void onPause() {
        this.disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getRentApplications();
    }

    private void initViews() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rentApplicationsRecycler.setLayoutManager(layoutManager);
        rentApplicationAdapter = new RentApplicationAdapter(new ArrayList<>(), (RentApplicationDetailsListener) getActivity());
        rentApplicationsRecycler.setAdapter(rentApplicationAdapter);
    }

    private void getRentApplications() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...");
        disposables.add(rentRepository.getRentApplications(0, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rentApplicationPage -> {
                    rentApplicationAdapter.setAll(rentApplicationPage.getContent());
                    progressDialog.hide();
                }, error -> {
                    progressDialog.hide();
                    Log.e(TAG, "GET Applications: " + error.getMessage());
                }));
    }
}