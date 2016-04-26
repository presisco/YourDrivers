package product.presisco.yourdrivers.ContentFrames;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.DataModel.Topic;
import product.presisco.yourdrivers.Network.Constants;
import product.presisco.yourdrivers.Network.Task.GetTopics;
import product.presisco.yourdrivers.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopicListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopicListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicListFragment extends Fragment {
    public static final String TITLE_TEXT = "Topics";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = TopicListFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TID = "tid";
    TopicListAdapter mTopicListAdapter;
    RecyclerView mArticleList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Topic> mTopics = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private boolean isFirstLaunch = true;
    private boolean isRefresh = true;
    private String tid = "0";

    public TopicListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopicListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicListFragment newInstance(String param1, String param2) {
        TopicListFragment fragment = new TopicListFragment();
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
        if (savedInstanceState != null) {
            isFirstLaunch = false;
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
        mTopicListAdapter = new TopicListAdapter(new OnTopicClickedListener(),
                new OnAppendListener());
        mTopicListAdapter.setDataSrc(mTopics);
        mArticleList.setAdapter(mTopicListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mainSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.material_light_blue_500, R.color.material_red_500);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefresh());

        view.findViewById(R.id.gotoTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArticleList.scrollToPosition(0);
            }
        });

        refreshContent(new String[]{GetTopics.MODE_REFRESH, "0"});

        if (isFirstLaunch) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
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
            Log.d(TAG, "parent doesnt implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void refreshContent(String... params) {
        mSwipeRefreshLayout.setRefreshing(true);
        new GetTopics().setOnLoadTopicsCompleteListener(new OnFetchTopicsComplete()).execute(params);
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
            isRefresh = true;
            refreshContent(new String[]{GetTopics.MODE_REFRESH, "0"});
        }
    }

    private class OnFetchTopicsComplete implements GetTopics.OnLoadTopicsCompleteListener {
        @Override
        public void onLoadComplete(List<Topic> src) {
            if (mTopics.size() == 0) {
                mTopics = src;
            } else if (isRefresh) {
                mTopics = src;
            } else {
                mTopics.addAll(mTopics.size(), src);
            }
            mTopicListAdapter.setDataSrc(mTopics);
            mTopicListAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class OnTopicClickedListener implements TopicListAdapter.OnContentItemClickedListener {
        @Override
        public void onContentItemClicked(int pos) {
            Log.d(TAG, "selected topic:" + mTopics.get(pos).id + "/" + mTopics.get(pos).title + "/" + mTopics.get(pos).link);
            String addr = Constants.MOBILE_WEB_HOST + mTopics.get(pos).link;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(addr));
            startActivity(intent);
        }
    }

    private class OnAppendListener implements TopicListAdapter.OnFooterShowedListener {
        @Override
        public void onFooterShowed() {
            isRefresh = false;
            refreshContent(new String[]{GetTopics.MODE_GET_MORE, tid, mTopics.get(mTopics.size() - 1).id});
        }
    }
}
