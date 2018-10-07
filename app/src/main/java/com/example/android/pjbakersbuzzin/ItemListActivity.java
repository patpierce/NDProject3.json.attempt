package com.example.android.pjbakersbuzzin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.pjbakersbuzzin.utilities.JsonUtils;
import com.example.android.pjbakersbuzzin.utilities.NetworkUtils;
import com.bumptech.glide.Glide;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    private static final String TAG = ItemListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter mAdapter;
    private RecyclerView mRecipesListRecView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

//    private static String[][] mRecipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecipesListRecView = findViewById(R.id.item_list);
        assert mRecipesListRecView != null;
        mAdapter = new SimpleItemRecyclerViewAdapter(this, null, mTwoPane);
        mRecipesListRecView.setAdapter(mAdapter);

        // "progress bar" circle
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        Log.d(TAG, "onCreate: about to run load data, mTwoPane = " + mTwoPane);
        loadRecipesData();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        //        private final List<DummyContent.DummyItem> mValues;
        private String[][] mRecipeData;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipeId = (String) view.getTag();
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
//                    ItemDetailFragment fragment = new ItemDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, recipeId);

                    context.startActivity(intent);
//                }
            }
        };

        public SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                             String[][] recipes,
                                             boolean twoPane) {
            mRecipeData = recipes;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        public void setRecipeData(String[][] recipes) {
            mRecipeData = recipes;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipes_list_item_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mRecipeData[position][1]);
            String numServings = " Serves: " + mRecipeData[position][4];
            holder.mServingsView.setText(numServings);
            String imageUrl = mRecipeData[position][5];
            Context context = holder.mRecipeImageView.getContext();
            Glide.with(context).load(imageUrl)
                    .placeholder(R.drawable.ic_kitchen).into(holder.mRecipeImageView);
            holder.itemView.setTag(mRecipeData[position][0]);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (null == mRecipeData) return 0;
            return mRecipeData.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            //            final TextView mIdView;
            final TextView mContentView;
            final TextView mServingsView;
            final ImageView mRecipeImageView;

            ViewHolder(View view) {
                super(view);
//                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.tv_content);
                mServingsView = (TextView) view.findViewById(R.id.tv_num_servings);
                mRecipeImageView = (ImageView) view.findViewById(R.id.iv_card_image);
            }
        }
    }
    private void loadRecipesData() {
        showRecipesDataView();
        Log.d(TAG, "loadRecipesData: ");

        new FetchRecipesTask().execute();
    }

    // Make the View for the recipe data visible and hide the error message.
    private void showRecipesDataView() {
        /* hide the error message text view */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* show the movie data is recycler view */
        mRecipesListRecView.setVisibility(View.VISIBLE);
    }

    // Make the View for the error message visible and hide the recipe data.
    private void showErrorMessage() {
        /* hide  the movie data is recycler view */
        mRecipesListRecView.setVisibility(View.INVISIBLE);
        /* show the error message text view */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private class FetchRecipesTask extends AsyncTask<String, Void, String[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... params) {

            Log.d("doInBackground: ", "sortMethod ");
            // IF THE SORT METHOD IS FAVORITES, GET INFO FROM CONTENT PROVIDER
//            if ("favorites".equals(sortMethod)) {
//
//                int cPos;
//                Cursor cursor = getFavoriteRecipes();
//                if (cursor.getCount() > 0) { // If cursor has atleast one row
//                    String[][] dbRecipesData = new String[cursor.getCount()][6];
////                    Log.d(TAG, "doInBackground: cursor.getCount() " + cursor.getCount());
//
//                    cursor.moveToFirst();
//                    do {
//                        cPos = cursor.getPosition();
////                        Log.d(TAG, "doInBackground: cPos " + cPos);
////                        Log.d(TAG, "doInBackground: dbRecipeData[cPos][0] " + dbRecipesData[cPos][0]);
////                        Log.d(TAG, "doInBackground: dbRecipeData[cPos][1] " + dbRecipesData[cPos][1]);
////                        Log.d(TAG, "doInBackground: dbRecipeData[cPos][2] " + dbRecipesData[cPos][2]);
////                        Log.d(TAG, "doInBackground: dbRecipeData[cPos][3] " + dbRecipesData[cPos][3]);
////                        Log.d(TAG, "doInBackground: dbRecipeData[cPos][4] " + dbRecipesData[cPos][4]);
////                        Log.d(TAG, "doInBackground: dbRecipeData[cPos][5] " + dbRecipesData[cPos][5]);
//                        dbRecipesData[cPos][0] = cursor.getString(cursor.getColumnIndex("recipeId"));
//                        dbRecipesData[cPos][1] = cursor.getString(cursor.getColumnIndex("title"));
//                        dbRecipesData[cPos][2] = cursor.getString(cursor.getColumnIndex("posterUrl"));
//                        dbRecipesData[cPos][3] = cursor.getString(cursor.getColumnIndex("synopsis"));
//                        dbRecipesData[cPos][4] = cursor.getString(cursor.getColumnIndex("rating"));
//                        dbRecipesData[cPos][5] = cursor.getString(cursor.getColumnIndex("releaseDate"));
//                        cursor.moveToNext();
//                    } while (!cursor.isAfterLast());
//
//                    return dbRecipesData;
//
//                } else {
//                    String[][] phRecipesData = new String[1][6]; // Dynamic string array
//
//                    Log.e("FetchRecipes: Content Provider", "Cursor has no data");
////                    phRecipesData[0][0] = null;
////                    phRecipesData[0][1] = getString(R.string.no_favorites_indb_message);
////                    phRecipesData[0][2] = null;
////                    phRecipesData[0][3] = getString(R.string.no_favs_placeholder_message);
////                    phRecipesData[0][4] = null;
////                    phRecipesData[0][5] = null;
////                    return phRecipesData;
//                    return null;
//
//                }
//            } else {
//                URL RecipesRequestUrl = NetworkUtils.buildMainUrl(sortMethod, apiKey);
            try {
                String jsonRecipeResponse = NetworkUtils
                        .getResponseFromHttpUrl();
//                Log.d("doInBackground from nw", "jsonRecipeResponse" + jsonRecipeResponse);
                String[][] JsonRecipesData = JsonUtils
                        .getRecipeStringsFromJson(ItemListActivity.this, jsonRecipeResponse);
                Log.d("doBkgnd from json", "JsonRecipesData 0 1 name " + JsonRecipesData[0][1]);
                Log.d("doBkgnd from json", "JsonRecipesData 0 4 servings " + JsonRecipesData[0][4]);
//                Log.d("doBkgnd from json", "JsonRecipesData 0 5 image " + JsonRecipesData[0][5]);
//                Log.d("doBkgnd from json", "JsonRecipesData 1 1 name " + JsonRecipesData[1][1]);
//                Log.d("doBkgnd from json", "JsonRecipesData 1 5 image " + JsonRecipesData[1][5]);
//                Log.d("doBkgnd from json", "JsonRecipesData 2 1 name " + JsonRecipesData[2][1]);
//                Log.d("doBkgnd from json", "JsonRecipesData 2 2 ingredients " + JsonRecipesData[2][2]);
//                Log.d("doBkgnd from json", "JsonRecipesData 2 3 steps " + JsonRecipesData[2][3]);
//                Log.d("doBkgnd from json", "JsonRecipesData 2 4 servings " + JsonRecipesData[2][4]);
//                Log.d("doBkgnd from json", "JsonRecipesData 2 5 image " + JsonRecipesData[2][5]);

                return JsonRecipesData;

            } catch (Exception e) {
                Log.e("FetchRecipes jsonfailed", "No results from RecipesRequestUrl");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] JsonRecipesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (JsonRecipesData != null) {
                showRecipesDataView();
                mAdapter.setRecipeData(JsonRecipesData);
            } else {
                showErrorMessage();
            }
//            if (savedRecyclerLayoutState!=null) {
//                layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
//            }
        }
    }
}