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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.Article.ArticleActivity;
import product.presisco.yourdrivers.DataModel.Category;
import product.presisco.yourdrivers.DataModel.Headline;
import product.presisco.yourdrivers.Network.Task.ExtendedRequest;
import product.presisco.yourdrivers.Network.Task.HeadlineRequest;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeadlinesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeadlinesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeadlinesFragment extends Fragment {
    public static final String TITLE_TEXT = "Topics";
    public static final String CATEGORY = "category";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = HeadlinesFragment.class.getSimpleName();
    HeadlinesListAdapter mHeadlinesListAdapter;
    RecyclerView mArticleList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Headline> mHeadlines = new ArrayList<>();
    private boolean isFirstLaunch = true;
    private Category currentCategory;
    private int currentPosition = 0;

    public HeadlinesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Browse category.
     * @return A new instance of fragment HeadlinesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeadlinesFragment newInstance(Category category) {
        HeadlinesFragment fragment = new HeadlinesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (savedInstanceState != null) {
            isFirstLaunch = false;
        }
        if (getArguments() != null) {
            currentCategory = (Category) getArguments().getSerializable(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        mArticleList = (RecyclerView) view.findViewById(R.id.mainList);
        mArticleList.setLayoutManager(new LinearLayoutManager(getContext()));
        mHeadlinesListAdapter = new HeadlinesListAdapter(new OnTopicClickedListener(),
                new OnAppendListener());
        mHeadlinesListAdapter.setDataSrc(mHeadlines);
        mArticleList.setAdapter(mHeadlinesListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mainSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.material_light_blue_500, R.color.material_red_500);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefresh());

        view.findViewById(R.id.gotoTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArticleList.scrollToPosition(0);
            }
        });

        requestContent(OnLoadCompleteListener.MODE_NEW);

        if (isFirstLaunch) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    private void requestContent(String mode) {
        mSwipeRefreshLayout.setRefreshing(true);
        HeadlineRequest request = null;
        String max_id = "";

        switch (mode) {
            case OnLoadCompleteListener.MODE_MORE:
                max_id = mHeadlines.get(mHeadlines.size() - 1).id;
                break;
            case OnLoadCompleteListener.MODE_NEW:
                max_id = "99999999";
                break;
        }

        VolleyPlusRes.getRequestQueue().add(
                new HeadlineRequest(currentCategory
                        , max_id
                        , new OnLoadCompleteListener(mode)
                        , new OnLoadFailedListener()
                ));
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
            requestContent(OnLoadCompleteListener.MODE_NEW);
        }
    }

    private class OnLoadCompleteListener implements ExtendedRequest.OnLoadCompleteListener<List<Headline>> {
        public static final String MODE_NEW = "new";
        public static final String MODE_MORE = "more";
        private String mode = "";

        public OnLoadCompleteListener(String _mode) {
            mode = _mode;
        }

        @Override
        public void onLoadComplete(List<Headline> src) {
            switch (mode) {
                case MODE_NEW:
                    mHeadlines.clear();
                case MODE_MORE:
                    mHeadlines.addAll(src);
                    mHeadlinesListAdapter.setDataSrc(mHeadlines);
                    mHeadlinesListAdapter.notifyDataSetChanged();
                default:
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
            }
        }
    }

    private class OnTopicClickedListener implements HeadlinesListAdapter.OnContentItemClickedListener {
        @Override
        public void onContentItemClicked(int pos) {
            Log.d(TAG, "selected topic:" + mHeadlines.get(pos).id + "/" + mHeadlines.get(pos).title + "/" + mHeadlines.get(pos).link);
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_addr));
            Intent intent = new Intent(getContext(), ArticleActivity.class);
            intent.putExtra(ArticleActivity.ARTICLE_ID, mHeadlines.get(pos).id);
            startActivity(intent);
        }
    }

    private class OnAppendListener implements HeadlinesListAdapter.OnFooterShowedListener {
        @Override
        public void onFooterShowed() {
            requestContent(OnLoadCompleteListener.MODE_MORE);
        }
    }

    private class OnLoadFailedListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
