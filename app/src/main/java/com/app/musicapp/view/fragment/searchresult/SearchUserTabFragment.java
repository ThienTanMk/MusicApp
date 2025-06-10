package com.app.musicapp.view.fragment.searchresult;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.musicapp.R;
import com.app.musicapp.adapter.search.SearchUserListAdapter;
import com.app.musicapp.model.response.ProfileWithCountFollowResponse;
import com.app.musicapp.view.fragment.profile.UserProfileFragment;

import java.util.*;

public class SearchUserTabFragment extends Fragment {
    private static final String ARG_USERS = "users";
    private List<ProfileWithCountFollowResponse> userResults;

    public static SearchUserTabFragment newInstance(List<ProfileWithCountFollowResponse> users) {
        SearchUserTabFragment fragment = new SearchUserTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USERS, new ArrayList<>(users));
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userResults = (List<ProfileWithCountFollowResponse>) getArguments().getSerializable(ARG_USERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_user_tab, container, false);

        ListView listView = view.findViewById(R.id.list_view_user_results);
        SearchUserListAdapter userAdapter = new SearchUserListAdapter(getContext(), userResults, profile -> {
            UserProfileFragment fragment = UserProfileFragment.newInstance(profile.getUserId(), "search");
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        listView.setAdapter(userAdapter);

        return view;
    }
}