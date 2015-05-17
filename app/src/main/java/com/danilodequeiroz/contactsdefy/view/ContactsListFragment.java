package com.danilodequeiroz.contactsdefy.view;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.danilodequeiroz.contactsdefy.AddContactFragment;
import com.danilodequeiroz.contactsdefy.R;
import com.danilodequeiroz.contactsdefy.adapter.ContacListAdapter;
import com.danilodequeiroz.contactsdefy.db.DatabaseAdapter;
import com.danilodequeiroz.contactsdefy.model.DefyContact;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsListFragment extends Fragment {

    public DynamicListView mDynamicListView;
    public Context mContext;
    private static final int INITIAL_DELAY_MILLIS = 300;
    private DatabaseAdapter mDBAdapter;

    public ContactsListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = inflater.getContext();
            View view = inflater.inflate(R.layout.fragment_contacts, container, false);
            mDynamicListView = (DynamicListView) view.findViewById(R.id.dynamic_lv_contacts);
            return view;
    }


    public static Fragment newInstance() {
        ContactsListFragment mFrgment = new ContactsListFragment();
        return mFrgment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDBAdapter = new DatabaseAdapter(getActivity().getApplicationContext());

        List<DefyContact> objects = mDBAdapter.getAllClientes();

        final ContacListAdapter myAdapter = new ContacListAdapter(getActivity(),R.layout.list_item_contact,objects);
//        SwipeDismissAdapter sw = new SwipeDismissAdapter(myAdapter, new OnDismissCallback() {
//            @Override
//            public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions) {
//                for (int position : reverseSortedPositions) {
//                    myAdapter.remove(myAdapter.getItem(position));
//                }
//            }
//        });
//        mDynamicListView.ena
//        SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(myAdapter, getActivity(),new MyOnDismissCallback(myAdapter));
        AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(myAdapter);
        animAdapter.setAbsListView(mDynamicListView);
        assert animAdapter.getViewAnimator() != null;
        animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
//        mDynamicListView.enableSwipeToDismiss(
//                new OnDismissCallback() {
//                    @Override
//                    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
//                        for (int position : reverseSortedPositions) {
//                            myAdapter.remove(myAdapter.getItem(position));
//                        }
//                    }
//                }
//        );


        View emptyView = View.inflate(getActivity().getApplicationContext(), R.layout.custom_empty_view, null);


        mDynamicListView.setEmptyView(emptyView);
        mDynamicListView.setAdapter(animAdapter);
        mDynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddContactFragment newFragment = AddContactFragment.newInstance(myAdapter.getItem(position).getmId());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.replace(R.id.main_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        mDynamicListView.enableSwipeToDismiss(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    mDBAdapter.deleteContact((int) myAdapter.getItem(reverseSortedPositions[0]).getmId());
                    myAdapter.remove(myAdapter.getItem(reverseSortedPositions[0]));
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
//        mDynamicListView.enableSwipeToDismiss();
//        mDynamicListView.enableSimpleSwipeUndo();
    }

    private class MyOnDismissCallback implements OnDismissCallback {

        private final ContacListAdapter mAdapter;

        @Nullable
        private Toast mToast;

        MyOnDismissCallback(final ContacListAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                mAdapter.remove(mAdapter.getItem(position));
            }

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(
                    getActivity().getApplicationContext(),
                    getString(R.string.removed_positions, Arrays.toString(reverseSortedPositions)),
                    Toast.LENGTH_LONG
            );
            mToast.show();
        }
    }

    public interface OnContactSelectedListener {
        public void onContactItemSelected(long id);
    }

    private OnContactSelectedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof OnContactSelectedListener ) {
//            listener = (OnContactSelectedListener ) activity;
//        } else {
//            throw new ClassCastException(activity.toString()
//                    + " must implemenet MyListFragment.OnItemSelectedListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("TAG", "Deatachei");
        listener = null;
    }


}
