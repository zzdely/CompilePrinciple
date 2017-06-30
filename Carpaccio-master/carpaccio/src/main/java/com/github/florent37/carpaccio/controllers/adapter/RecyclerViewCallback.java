package com.github.florent37.carpaccio.controllers.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by florentchampigny on 31/07/15.
 */
public interface RecyclerViewCallback {

    int getItemViewType(int position);

    //return a different R.layout.
    int onCreateViewHolder(int viewType);

    Holder onCreateViewHolder(View view, int viewType);

    //do a custom action
    void onBind(Object object, Holder holder, int position);

}
