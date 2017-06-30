package com.github.florent37.carpaccio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.florent37.carpaccio.controllers.adapter.CarpaccioRecyclerViewAdapter;
import com.github.florent37.carpaccio.controllers.adapter.OnHeaderClickListener;
import com.github.florent37.carpaccio.controllers.adapter.OnItemClickListener;
import com.github.florent37.carpaccio.controllers.adapter.OnItemSwipedListener;
import com.github.florent37.carpaccio.controllers.adapter.RecyclerViewCallback;
import com.github.florent37.carpaccio.mapping.MappingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 21/07/15.
 */
public class Carpaccio extends FrameLayout {

    private static final String TAG = "Carpaccio";
    protected CarpaccioManager carpaccioManager;
    public static boolean IN_EDIT_MODE = false;
    protected boolean displayErrors = false;
    protected boolean onlyForPreview = false; //is a normal FrameLayout on device, only effective on Preview

    protected void handleAttributes(Context context, AttributeSet attrs) {
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.Carpaccio);

        {
            String register = styledAttrs.getString(R.styleable.Carpaccio_register);

            onlyForPreview = styledAttrs.getBoolean(R.styleable.Carpaccio_onlyForPreview, false);
            displayErrors = styledAttrs.getBoolean(R.styleable.Carpaccio_displayErrors, false);

            if(carpaccioManager != null)
                carpaccioManager.displayErrors = this.displayErrors;

            if ((onlyForPreview && IN_EDIT_MODE) || !IN_EDIT_MODE) {
                if (register != null && carpaccioManager != null)
                    carpaccioManager.registerControllers(context,register);
            }
        }

        styledAttrs.recycle();
    }

    public Carpaccio(Context context) {
        super(context);
        carpaccioManager = new CarpaccioManager(new MappingManager());
    }

    public Carpaccio(Context context, AttributeSet attrs) {
        super(context, attrs);
        carpaccioManager = new CarpaccioManager(new MappingManager());
        handleAttributes(context, attrs);
    }

    public Carpaccio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        carpaccioManager = new CarpaccioManager(new MappingManager());
        handleAttributes(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        IN_EDIT_MODE = isInEditMode();

        if (onlyForPreview) {
            if (IN_EDIT_MODE) {
                execute();
            } else {
                CarpaccioLogger.d(TAG, "This Carpaccio is only effective on preview");
            }
        } else {
            execute();
        }
    }

    public void execute() {
        if (carpaccioManager != null) {
            carpaccioManager.findCarpaccioControlledViews(this);
            carpaccioManager.executeActionsOnViews();
        }
    }

    public CarpaccioManager getCarpaccioManager() {
        return carpaccioManager;
    }

    public void mapObject(String name, Object object) {
        if (carpaccioManager != null)
            carpaccioManager.mapObject(name, object);
    }

    public Object getMappedObject(String name) {
        if (carpaccioManager != null)
            return carpaccioManager.getMappedObject(name);
        else
            return null;
    }

    //region mapList

    public <T> T registerAdapter(String mappedName, Object adapter) {
        if (carpaccioManager != null)
            return (T) carpaccioManager.registerAdapter(mappedName, adapter);
        return null;
    }

    public CarpaccioRecyclerViewAdapter getAdapter(String mappedName) {
        if (carpaccioManager != null)
            return carpaccioManager.getAdapter(mappedName);
        return null;
    }

    public Carpaccio onItemClick(String mappedName, OnItemClickListener onItemClickListener) {
        CarpaccioRecyclerViewAdapter adapter = getAdapter(mappedName);
        if (adapter != null) {
            adapter.setOnItemClickListener(onItemClickListener);
        }
        return this;
    }

    public Carpaccio onHeaderClick(String listMappedName, OnHeaderClickListener onHeaderClickListener) {
        CarpaccioRecyclerViewAdapter adapter = getAdapter(listMappedName);
        if (adapter != null) {
            adapter.setOnHeaderClickListener(onHeaderClickListener);
        }
        return this;
    }

    public Carpaccio onItemSwiped(String mappedName, OnItemSwipedListener onItemSwipedListener) {
        CarpaccioRecyclerViewAdapter adapter = getAdapter(mappedName);
        if (adapter != null) {
            adapter.setOnItemSwipedListener(onItemSwipedListener);
        }
        return this;
    }

    public Carpaccio setRecyclerViewCallback(String mappedName, RecyclerViewCallback recyclerViewCallback) {
        CarpaccioRecyclerViewAdapter adapter = getAdapter(mappedName);
        if (adapter != null) {
            adapter.setRecyclerViewCallback(recyclerViewCallback);
        }
        return this;
    }

    public Carpaccio mapList(String name, List list) {
        if (carpaccioManager != null)
            carpaccioManager.mapList(name, list);
        return this;
    }

    public void appendList(String name, List list) {
        if (carpaccioManager != null)
            carpaccioManager.appendList(name, list);
    }

    public List getMappedList(String mapName) {
        if (carpaccioManager != null)
            return carpaccioManager.getMappedList(mapName);
        return null;
    }

    public void addChildViews(View view) {
        if (carpaccioManager != null)
            carpaccioManager.addChildViews(view);
    }

    public Object bindView(View view, String mapName, int position) {
        if (carpaccioManager != null)
            return carpaccioManager.bindView(view, mapName, position);
        return null;
    }

    public Object bindView(View view, String mapName) {
        if (carpaccioManager != null)
            return carpaccioManager.bindView(view, mapName);
        return null;
    }

    public void addCarpaccioView(View view) {
        if (carpaccioManager != null) {
            //carpaccioManager.addView(view); //TODO
            List<View> childrens = new ArrayList<>();
            carpaccioManager.findCarpaccioControlledViews(view, childrens);
            carpaccioManager.executeActionsOnViews(childrens, null);
        }
    }

    //endregion
}
