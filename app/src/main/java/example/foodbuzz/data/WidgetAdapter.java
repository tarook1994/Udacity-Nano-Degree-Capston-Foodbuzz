package example.foodbuzz.data;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import example.foodbuzz.R;

/**
 * Created by DELL on 4/3/2017.
 */

public class WidgetAdapter extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            DBHandlerFav dbHelper;
            private Cursor mCursor;
            SQLiteDatabase db;

            @Override
            public void onCreate() {
                dbHelper = new DBHandlerFav(getApplicationContext());
                db = dbHelper.getWritableDatabase();


            }

            @Override
            public void onDataSetChanged() {

                mCursor = db.query("fav",null,null,null,null,null,null);
                dbHelper = new DBHandlerFav(getApplicationContext());
                db = dbHelper.getWritableDatabase();

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                Log.d("Count",mCursor.getCount()+"");
                return mCursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_item);
                int indexTitle = mCursor.getColumnIndexOrThrow("name");
                int indexDesc = mCursor.getColumnIndexOrThrow("describtion");
                int indexIcon = mCursor.getColumnIndexOrThrow("icon");
                int indexPrice = mCursor.getColumnIndexOrThrow("price");
                int indexRating = mCursor.getColumnIndexOrThrow("rating");


                if (mCursor.moveToPosition(position)) {
                    Log.d("here","here"+mCursor.getString(indexTitle));

                    views.setTextViewText(R.id.titleWidget,mCursor.getString(indexTitle));
                    views.setTextViewText(R.id.descriptionWidget,mCursor.getString(indexDesc));
                    try {
                        Bitmap b = Picasso.with(getApplicationContext()).load(mCursor.getString(indexIcon)).get();
                        views.setImageViewBitmap(R.id.RestImageViewWidget, b);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra("icon",mCursor.getString(indexIcon));
                fillInIntent.putExtra("price",mCursor.getString(indexPrice));
                fillInIntent.putExtra("rating",mCursor.getString(indexRating));
                fillInIntent.putExtra("name",mCursor.getString(indexTitle));
                fillInIntent.putExtra("desc",mCursor.getString(indexDesc));
                fillInIntent.putExtra("position",position+"");

                views.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_item);

            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };

    }
}