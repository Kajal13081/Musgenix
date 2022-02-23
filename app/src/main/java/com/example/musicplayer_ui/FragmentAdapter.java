package com.example.musicplayer_ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    // constructor for fragment adapter object
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
// return different fragments on user's action
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                   return AlbumsFragment.newInstance();
            case 2:
                return ArtistsFragment.newInstance();
            case 3:
                return PlaylistsFragment.newInstance();

        }
        return RecyclerViewFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
