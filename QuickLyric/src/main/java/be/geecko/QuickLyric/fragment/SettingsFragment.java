package be.geecko.QuickLyric.fragment;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import be.geecko.QuickLyric.MainActivity;
import be.geecko.QuickLyric.R;
import be.geecko.QuickLyric.adapter.DrawerAdapter;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    public boolean showTransitionAnim = true;
    public boolean isActiveFragment = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        findPreference("pref_about").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("pref_about".equals(preference.getKey())) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setView(getActivity().getLayoutInflater().inflate(R.layout.about_dialog, null));
            dialog.create().show();
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.isHidden())
            return;
        final MainActivity activity = ((MainActivity) this.getActivity());
        DrawerAdapter drawerAdapter = ((DrawerAdapter) ((ListView) activity.findViewById(R.id.drawer_list)).getAdapter());

        if (drawerAdapter.getSelectedItem() != 2) {
            drawerAdapter.setSelectedItem(2);
            drawerAdapter.notifyDataSetChanged();
        }
        this.isActiveFragment = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            this.onViewCreated(getView(), null);
        else
            this.isActiveFragment = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MainActivity mainActivity = (MainActivity) this.getActivity();
        ActionBar actionBar = (mainActivity).getActionBar();
        if (mainActivity.focusOnFragment && actionBar != null) // focus is on Fragment
        {
            if (actionBar.getTitle() == null || !actionBar.getTitle().equals(this.getString(R.string.local_title)))
                actionBar.setTitle(R.string.settings_title);
        } else
            menu.clear();
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Animator anim = null;
        if (nextAnim != 0)
            anim = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
        if (anim != null) {
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationStart(Animator animator) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity.drawer instanceof DrawerLayout)
                        ((DrawerLayout) mainActivity.drawer).closeDrawer(mainActivity.drawerView);
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            if (!showTransitionAnim)
                anim.setDuration(0);
            else
                showTransitionAnim = false;
        }
        return anim;
    }
}
