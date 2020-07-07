package com.example.potholedetectiondemo.TAB;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.potholedetectiondemo.BlankFragment;
import com.example.potholedetectiondemo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class TabFragment extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.tabViewPager);
//        viewPager.setOffscreenPageLimit(1);
        tabLayout = view.findViewById(R.id.tabLayout);

        adapter = new TabAdapter(this);
//        adapter.addFragment(new HistoryMyBookingsFragment(), "HISTORY");
//        adapter.addFragment(new UpcomingMyBookingsFragment(), "UPCOMING");
        viewPager.setAdapter(adapter);
        String[] tabTitle = {"CAMERA", "FEED", "MAPS" ,"PROFILE"};
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitle[position])
        ).attach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    class TabAdapter extends FragmentStateAdapter {

        TabAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position==0){
                return new CameraFragment();
                //return new BlankFragment();
            }
            else if (position == 1) {
                return new FeedFragment();
                //return new BlankFragment();
            } else if(position == 3) {

                return new ProfileFragment();
                //return new BlankFragment();
            }
            else {
                return new MapFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

}