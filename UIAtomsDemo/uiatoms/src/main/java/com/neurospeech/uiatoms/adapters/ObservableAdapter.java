package com.neurospeech.uiatoms.adapters;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neurospeech.uiatoms.AtomListView;
import com.neurospeech.uiatoms.BR;
import com.neurospeech.uiatoms.ListItem;
import com.neurospeech.uiatoms.R;
import com.neurospeech.uiatoms.ReflectionHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by on 29-06-2017.
 */

public class ObservableAdapter<T> extends RecyclerView.Adapter<ObservableAdapter.ObservableViewHolder> {

    private final ObservableList<T> items;
    private final int layoutId;
    private final ObservableList.OnListChangedCallback<ObservableList<T>> callback;
    private final Object viewModel;
    //private final int modelId;
    //private final int viewModelId;
    private final ObservableList.OnListChangedCallback<ObservableList<T>> selectionCallback;

    private final ObservableField<T> selectedItem = new ObservableField<T>();

    public boolean isAllowMultipleSelection() {
        return allowMultipleSelection;
    }

    public void setAllowMultipleSelection(boolean allowMultipleSelection) {
        this.allowMultipleSelection = allowMultipleSelection;
    }

    private boolean allowMultipleSelection;

    private ObservableList<T> selectedItems;


    public ObservableAdapter(ObservableList<T> items, @NonNull ObservableList<T> selectedItems, @LayoutRes int layoutId, Object viewModel)  {
        super();
        this.items = items;
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        //this.modelId = modelId;
        //this.viewModelId = viewModelId;
        this.selectedItems = selectedItems;

        this.selectionCallback = new ObservableList.OnListChangedCallback<ObservableList<T>>(){
            @Override
            public void onChanged(ObservableList<T> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyDataSetChanged();
            }
        };

        selectedItems.addOnListChangedCallback(selectionCallback);

        this.callback = new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> ts) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
                notifyItemRangeChanged(i,i1);
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
                notifyItemRangeInserted(i,i1);
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
                notifyItemRangeRemoved(i,i1);
            }
        };

    }

    public List<T> getItems(){
        return this.items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        items.addOnListChangedCallback(callback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        items.removeOnListChangedCallback(callback);
        super.onDetachedFromRecyclerView(recyclerView);
    }


    @Override
    public ObservableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),layoutId,parent,false);

        final ObservableViewHolder vh = new ObservableViewHolder(binding);
        vh.selected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(!vh.updateSelected)
                    return;

                // update selections...
                if(selectedItems.contains(vh.getItem())){
                    if(allowMultipleSelection){
                        selectedItems.remove(vh.getItem());
                    }
                }else {
                    if (!allowMultipleSelection) {
                        selectedItems.clear();
                    }
                    selectedItems.add((T)vh.getItem());
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ObservableViewHolder holder, int position) {
        T item = items.get(position);
        if(holder.model!=item) {
            //holder.dataBinding.setVariable(modelId, item);
            ReflectionHelper.run(holder.dataBinding,"set",item);
            holder.model = item;
        }
        if(holder.viewModel != viewModel) {
            //holder.dataBinding.getClass().getMethods()
            //holder.dataBinding.setVariable(viewModelId, viewModel);
            ReflectionHelper.run(holder.dataBinding,"set",viewModel);
            holder.viewModel = viewModel;
        }
        try{
            holder.updateSelected = false;
            holder.selected.set( selectedItems.contains(item) );
            holder.dataBinding.executePendingBindings();
        }finally {
            holder.updateSelected = true;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ObservableViewHolder
            extends
                RecyclerView.ViewHolder
            implements
                ListItem{

        public final ViewDataBinding dataBinding;

        public Object model;
        public Object viewModel;

        public final ObservableBoolean selected = new ObservableBoolean(false);

        public boolean updateSelected = true;

        public ObservableViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            //dataBinding.setVariable(BR.item,this);
            ReflectionHelper.run(dataBinding,"set",this);
            this.dataBinding = dataBinding;
        }

        @Override
        public Object getItem() {
            return model;
        }

        @Override
        public ObservableBoolean getSelected() {
            return selected;
        }

        @Override
        public void toggleSelection() {
            selected.set(!selected.get());
        }
    }


    @BindingAdapter(value = {"items","selectedItems", "allowMultipleSelection", "layout","viewModel"},requireAll = false)
    public static <T> void setAdapter(
            RecyclerView view,
            ObservableList<T> items,
            ObservableList<T> selectedItems,
            Boolean allowMultipleSelection,
            int layoutResId,
            Object viewModel) {


        if(selectedItems==null)
            selectedItems = new ObservableArrayList<T>();

        ObservableAdapter<T> adapter = new ObservableAdapter<T>(items, selectedItems, layoutResId, viewModel);
        adapter.setAllowMultipleSelection(allowMultipleSelection != null && allowMultipleSelection.booleanValue());
        view.setAdapter(adapter);
        view.setTag(R.id.observableAdapterTag,adapter);
        if(view.getLayoutManager() == null) {
            LinearLayoutManager linearLayoutManager
                    = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);

            view.setLayoutManager(linearLayoutManager);
        }
    }


    @BindingAdapter(value = {"items","allowMultipleSelection", "layout", "viewModel"},requireAll = false)
    public static <T> void setAdapter(RecyclerView view, T[] items,Boolean allowMultipleSelection,int layoutResId, Object viewModel) {

        if(items==null){
            return;
        }


        ObservableArrayList<T> list = new ObservableArrayList<T>();
        list.addAll(Arrays.asList(items));
        ObservableAdapter<T> adapter = new ObservableAdapter<T>(list, new ObservableArrayList<T>(), layoutResId, viewModel);
        adapter.setAllowMultipleSelection(allowMultipleSelection != null && allowMultipleSelection.booleanValue());
        view.setAdapter(adapter);
        view.setTag(R.id.observableAdapterTag,adapter);
        if(view.getLayoutManager() == null) {
            LinearLayoutManager linearLayoutManager
                    = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);

            view.setLayoutManager(linearLayoutManager);
        }
    }
}
