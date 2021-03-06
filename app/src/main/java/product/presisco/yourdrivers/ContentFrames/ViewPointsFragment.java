package product.presisco.yourdrivers.ContentFrames;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import product.presisco.yourdrivers.Article.ArticleActivity;
import product.presisco.yourdrivers.DataModel.Viewpoint;
import product.presisco.yourdrivers.Network.Request.ExtendedRequest;
import product.presisco.yourdrivers.Network.Request.ViewPointRequest;
import product.presisco.yourdrivers.Network.VolleyPlusRes;
import product.presisco.yourdrivers.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPointsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPointsFragment extends Fragment implements ExtendedRequest.OnLoadCompleteListener<List<Viewpoint>>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TITLE_TEXT = "View Points";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Viewpoint> mViewPoints = new ArrayList<>();
    RecyclerView mViewPointList;
    ViewpointAdapter mViewPointAdapter;

    private String mParam1;
    private String mParam2;

    public ViewPointsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     *
     * @return A new instance of fragment ViewPointsFragment.
     */
    public static ViewPointsFragment newInstance() {
        ViewPointsFragment fragment = new ViewPointsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_point_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPointList = (RecyclerView) view.findViewById(R.id.viewpointList);
        mViewPointAdapter = new ViewpointAdapter();
        mViewPointList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewPointList.setAdapter(mViewPointAdapter);
        view.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        VolleyPlusRes.getRequestQueue().add(new ViewPointRequest(this, this));
    }

    @Override
    public void onLoadComplete(List<Viewpoint> data) {
        mViewPoints.clear();
        mViewPoints.addAll(data);
        mViewPointAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private class ViewpointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            if (mViewPoints == null) {
                return 0;
            } else {
                return mViewPoints.size();
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ContentHolder contentHolder = (ContentHolder) holder;
            contentHolder.mTitle.setText(mViewPoints.get(position).title);
            VolleyPlusRes.getImageLoader().get(mViewPoints.get(position).icon,
                    ImageLoader.getImageListener(contentHolder.mIcon, R.drawable.empty_photo, R.drawable.error_image));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cardview_viewpoint, parent, false));
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        class ContentHolder extends RecyclerView.ViewHolder {
            final TextView mTitle;
            final ImageView mIcon;

            public ContentHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.textTitle);
                mIcon = (ImageView) itemView.findViewById(R.id.iconView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra(ArticleActivity.ARTICLE_ID, mViewPoints.get(getAdapterPosition()).id);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(addr));
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
