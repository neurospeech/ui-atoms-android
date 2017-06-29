package com.neurospeech.uiatoms.adapters;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neurospeech.uiatoms.BR;

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
    private final int modelId;
    private final int viewModelId;

    public ObservableAdapter(ObservableList<T> items, ItemBinding<T> binding){
        this(items,binding.getLayoutId(),binding.getVariableId(),binding.getViewModelId(),binding.getViewModel());
    }

    public ObservableAdapter(ObservableList<T> items, @LayoutRes int layoutId, int modelId, int viewModelId, Object viewModel)  {
        super();
        this.items = items;
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.modelId = modelId;
        this.viewModelId = viewModelId;
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
        return new ObservableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ObservableViewHolder holder, int position) {
        T item = items.get(position);
        if(holder.model!=item) {
            holder.dataBinding.setVariable(modelId, item);
            holder.model = item;
        }
        if(viewModelId>0){
            if(holder.viewModel != viewModel) {
                holder.dataBinding.setVariable(viewModelId, viewModel);
                holder.viewModel = viewModel;
            }
        }
        holder.dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public static class ObservableViewHolder extends RecyclerView.ViewHolder{

        public final ViewDataBinding dataBinding;

        public Object model;
        public Object viewModel;

        public ObservableViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }
    }




    @BindingAdapter(value = {"items","itemBinding", "layout","viewModel"},requireAll = false)
    public static <T> void setAdapter(RecyclerView view, ObservableList<T> items,ItemBinding<T> itemBinding, int layoutResId, Object viewModel) {

        if(itemBinding==null){
            itemBinding = ItemBinding.of(BR.model, layoutResId, viewModel == null ? 0 : BR.viewModel, viewModel );
        }

        view.setAdapter(new ObservableAdapter<T>(items,itemBinding));
        if(view.getLayoutManager() == null) {
            LinearLayoutManager linearLayoutManager
                    = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);

            view.setLayoutManager(linearLayoutManager);
        }
    }


    @BindingAdapter(value = {"items","itemBinding", "layout", "viewModel"},requireAll = false)
    public static <T> void setAdapter(RecyclerView view, T[] items,ItemBinding<T> itemBinding, int layoutResId, Object viewModel) {

        if(items==null){
            return;
        }

        if(itemBinding==null){
            itemBinding = ItemBinding.of(BR.model, layoutResId, viewModel == null ? 0 : BR.viewModel, viewModel );
        }


        ObservableArrayList<T> list = new ObservableArrayList<T>();
        list.addAll(Arrays.asList(items));
        view.setAdapter(new ObservableAdapter<T>(list,itemBinding));
        if(view.getLayoutManager() == null) {
            LinearLayoutManager linearLayoutManager
                    = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);

            view.setLayoutManager(linearLayoutManager);
        }
    }
}
