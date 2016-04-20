package product.presisco.yourdrivers.ContentFrames;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.Network.Task.GetTopics;
import product.presisco.yourdrivers.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArticleListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArticleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArticleListAdapter mArticleListAdapter;
    RecyclerView mArticleList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Topic> mTopics = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ArticleListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticleListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleListFragment newInstance(String param1, String param2) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArticleList = (RecyclerView) view.findViewById(R.id.mainList);
        mArticleList.setLayoutManager(new LinearLayoutManager(getContext()));
        mArticleListAdapter = new ArticleListAdapter();
        mArticleListAdapter.setDataSrc(mTopics);
        mArticleList.setAdapter(mArticleListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mainSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.material_light_blue_500, R.color.material_red_500);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefresh());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class OnRefresh implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {

        }
    }

    private class OnFetchComplete implements GetTopics.OnLoadCompleteListener {
        @Override
        public void onLoadComplete(List<Topic> src) {

        }
    }

    private class OnDifferentStates implements ArticleListAdapter.OnDifferentStateListener {
        @Override
        public void onItemClicked(int pos) {

        }

        @Override
        public void onFooterShowed() {

        }
    }
}
