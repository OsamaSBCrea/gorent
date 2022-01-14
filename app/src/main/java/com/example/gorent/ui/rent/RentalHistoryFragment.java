package com.example.gorent.ui.rent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gorent.R;
import com.example.gorent.data.enums.UserRole;
import com.example.gorent.data.models.RentApplication;
import com.example.gorent.data.models.RentHistory;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.RentRepository;
import com.example.gorent.ui.entities.rent.history.RentHistoryAdapter;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.keys.SharedKeys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RentalHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RentalHistoryFragment extends Fragment {

    @BindView(R.id.rent_history_recycler)
    RecyclerView rentHistoryRecycler;

    @BindView(R.id.no_history_text)
    TextView noHistoryText;

    private RentHistoryAdapter rentHistoryAdapter;

    private ProgressDialog progressDialog;

    private RentRepository rentRepository;

    private SharedPreferencesManager sharedPreferencesManager;

    private final List<Disposable> disposables = new ArrayList<>();

    private RentApplication rentApplication;

    public static final String TAG = "[RentalHistoryFragment]";

    public RentalHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RentalHistoryFragment.
     */
    public static RentalHistoryFragment newInstance(RentApplication rentApplication) {
        RentalHistoryFragment fragment = new RentalHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(SharedKeys.BUNDLE_RENT_APPLICATION, rentApplication);
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
        View view = inflater.inflate(R.layout.fragment_rental_history, container, false);
        ButterKnife.bind(this, view);
        rentRepository = RepositoryManager.getInstance(getContext().getApplicationContext()).getRentRepository();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity().getApplicationContext());
        if (getArguments() != null) {
            rentApplication = (RentApplication) getArguments().getSerializable(SharedKeys.BUNDLE_RENT_APPLICATION);
        }
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getRentHistory();
    }

    @Override
    public void onPause() {
        this.disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    private void initViews() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rentHistoryRecycler.setLayoutManager(layoutManager);
        rentHistoryAdapter = new RentHistoryAdapter(new ArrayList<>(), sharedPreferencesManager.getUser().getRole() == UserRole.RENTING_AGENCY);
        rentHistoryRecycler.setAdapter(rentHistoryAdapter);
    }

    private void getRentHistory() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Getting your history...");
        Observable<List<RentHistory>> historyObservable;
        if (rentApplication == null) {
            historyObservable = rentRepository.getRentHistory();
        } else {
            historyObservable = rentRepository.getTenantHistory(rentApplication.getTenantId());
        }
        disposables.add(historyObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rentHistoryList -> {
                    if (rentApplication != null) {
                        rentHistoryList.removeIf(rentHistory -> rentHistory.getRentId().equals(rentApplication.getRentId()));
                    }
                    if (rentHistoryList.isEmpty()) {
                        noHistoryText.setVisibility(View.VISIBLE);
                    } else {
                        noHistoryText.setVisibility(View.GONE);
                    }
                    rentHistoryAdapter.setAll(rentHistoryList);
                    progressDialog.hide();
                }, error -> {
                    progressDialog.hide();
                    Log.e(TAG, error.getMessage());
                }));
    }
}