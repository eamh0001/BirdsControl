package com.eamh.birdcontrol.fragments.birds;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eamh.birdcontrol.R;
import com.eamh.birdcontrol.data.models.Bird;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBirdsFragmentInteractionListener}
 * interface.
 */
public class BirdsFragment extends Fragment {

    private static final String BUNDLE_KEY_BIRDS = "BUNDLE_KEY_BIRDS";

    private OnBirdsFragmentInteractionListener birdsFragmentInteractionListener;
    private List<Bird> birds;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BirdsFragment() {
    }

    @SuppressWarnings("unused")
    public static BirdsFragment newInstance(ArrayList<Bird> birds) {
        BirdsFragment fragment = new BirdsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_KEY_BIRDS, birds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            birds = arguments.getParcelableArrayList(BUNDLE_KEY_BIRDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birds, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        initRecyclerView(recyclerView);
        FloatingActionButton fabNewBird = view.findViewById(R.id.fab);
        fabNewBird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birdsFragmentInteractionListener.onBirdsFragmentListClicked(null);
            }
        });

        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView) {

//        recyclerView.setAdapter(new BirdsRecyclerViewAdapter(DummyBird.ITEMS, birdsFragmentInteractionListener));
        recyclerView.setAdapter(new BirdsRecyclerViewAdapter(birds, birdsFragmentInteractionListener));

         /*As T09.07 of NanoDegree:
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                // Here is where you'll implement swipe to delete
                //Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                long birdId = (long) viewHolder.itemView.getTag();
                birdsFragmentInteractionListener.onBirdsFragmentDeleteItem(birdId);
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBirdsFragmentInteractionListener) {
            birdsFragmentInteractionListener = (OnBirdsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBreedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        birdsFragmentInteractionListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBirdsFragmentInteractionListener {
        void onBirdsFragmentListClicked(Bird bird);

        void onBirdsFragmentDeleteItem(long birdId);
    }
}
