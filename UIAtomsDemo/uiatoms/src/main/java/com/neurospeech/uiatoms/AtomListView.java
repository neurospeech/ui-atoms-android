package com.neurospeech.uiatoms;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by on 05-07-2017.
 */

public class AtomListView extends RecyclerView {

    public ObservableList getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(@Nullable ObservableList selectedItems) {
        this.selectedItems = selectedItems;
        setAdapter(new ObservableAdapter());
    }

    private ObservableList selectedItems = null;

    private ObservableList items = new AtomList();

    public ObservableList getItems() {
        return items;
    }

    public void setItems(ObservableList items) {
        this.items = items;
        setAdapter(new ObservableAdapter());
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        refreshAdapter();
    }

    public Object getViewModel() {
        return viewModel;
    }

    public void setViewModel(Object viewModel) {
        this.viewModel = viewModel;
        refreshAdapter();
    }

    private void refreshAdapter() {
        Adapter a = getAdapter();
        if(a != null){
            a.notifyDataSetChanged();
        }
    }

    public Funcs.Func1 getItemHeader() {
        return itemHeader;
    }

    public void setItemHeader(Funcs.Func1 itemHeader) {
        this.itemHeader = itemHeader;
        refreshAdapter();
    }

    private Funcs.Func1 itemHeader;

    private int layoutId;

    public int getItemHeaderLayoutId() {
        return itemHeaderLayoutId;
    }

    public void setItemHeaderLayoutId(int itemHeaderLayoutId) {
        this.itemHeaderLayoutId = itemHeaderLayoutId;
        refreshAdapter();
    }

    public int getHeaderLayoutId() {
        return headerLayoutId;
    }

    public void setHeaderLayoutId(int headerLayoutId) {
        this.headerLayoutId = headerLayoutId;
        refreshAdapter();
    }

    public int getFooterLayoutId() {
        return footerLayoutId;
    }

    public void setFooterLayoutId(int footerLayoutId) {
        this.footerLayoutId = footerLayoutId;
        refreshAdapter();
    }

    private int itemHeaderLayoutId;

    private int headerLayoutId;

    private int footerLayoutId;

    public Funcs.Func1<Object, Integer> getLayoutSelector() {
        return layoutSelector;
    }

    public void setLayoutSelector(Funcs.Func1<Object, Integer> layoutSelector) {
        this.layoutSelector = layoutSelector;
    }

    private Funcs.Func1<Object,Integer> layoutSelector;

    private Object viewModel;

    public boolean isAllowMultipleSelection() {
        return allowMultipleSelection;
    }

    public void setAllowMultipleSelection(boolean allowMultipleSelection) {
        this.allowMultipleSelection = allowMultipleSelection;
    }

    private boolean allowMultipleSelection;

    public AtomListView(Context context) {
        super(context);
    }

    public AtomListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AtomListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @BindingAdapter("selectedItems")
    public static <T> void setSelectedItems(
            AtomListView view,
            @Nullable ObservableList<T> list){
        if(view.getSelectedItems() == list)
            return;
        view.setSelectedItems(list);
    }

    @BindingAdapter("items")
    public static <T> void adapterSetItems(AtomListView view, ObservableList<T> items){
        if(view.getItems() == items)
            return;
        view.setItems(items);
        if(view.getLayoutManager() == null){
            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(view.getContext(),
                            LinearLayoutManager.VERTICAL,
                            false);
            view.setLayoutManager(linearLayoutManager);
        }
    }

    @BindingAdapter("layout")
    public static void adapterSetLayout(AtomListView view,@LayoutRes int layoutId){
        view.setLayoutId(layoutId);
    }

    @BindingAdapter("itemHeader")
    public static void adapterSetItemHeader(AtomListView view, Funcs.Func1 itemHeader){
        view.setItemHeader(itemHeader);
    }


    @BindingAdapter("itemHeaderlayout")
    public static void adapterSetItemHeaderLayout(AtomListView view,@LayoutRes int layoutId){
        view.setItemHeaderLayoutId(layoutId);
    }

    @BindingAdapter("headerLayout")
    public static void adapterSetHeaderLayout(AtomListView view,@LayoutRes int layoutId){
        view.setHeaderLayoutId(layoutId);
    }

    @BindingAdapter("footerLayout")
    public static void adapterSetFooterLayout(AtomListView view,@LayoutRes int layoutId){
        view.setFooterLayoutId(layoutId);
    }

    @BindingAdapter("viewModel")
    public static void adapterSetViewModel(AtomListView view,Object viewModel){
        if(view.getViewModel() == viewModel)
            return;
        view.setViewModel(viewModel);
    }

    @BindingAdapter("allowMultipleSelection")
    public static void adapterSetViewModel(AtomListView view,boolean allowMultipleSelection){
        view.setAllowMultipleSelection(allowMultipleSelection);
    }

    class ObservableAdapter extends Adapter<BindableViewHolder> {

        private final ObservableList.OnListChangedCallback<ObservableList> selectionCallback;
        private final ObservableList.OnListChangedCallback<ObservableList> callback;

        public void notifySelectedItems(){

            notifyDataSetChanged();
        }

        public ObservableAdapter() {
            super();

            //this.setHasStableIds(true);

            this.selectionCallback = new ObservableList.OnListChangedCallback<ObservableList>(){
                @Override
                public void onChanged(ObservableList sender) {
                    notifySelectedItems();
                }

                @Override
                public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                    notifySelectedItems();
                }

                @Override
                public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                    notifySelectedItems();
                }

                @Override
                public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                    notifySelectedItems();
                }

                @Override
                public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                    notifySelectedItems();
                }
            };

            this.callback = new ObservableList.OnListChangedCallback<ObservableList>() {
                @Override
                public void onChanged(ObservableList ts) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(ObservableList ts, int i, int i1) {
                    notifyItemRangeChanged(i,i1);
                }

                @Override
                public void onItemRangeInserted(ObservableList ts, int i, int i1) {
                    notifyItemRangeInserted(i,i1);
                }

                @Override
                public void onItemRangeMoved(ObservableList ts, int i, int i1, int i2) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeRemoved(ObservableList ts, int i, int i1) {
                    //notifyItemRangeRemoved(i,i1);
                    notifyDataSetChanged();
                }
            };

            if(selectedItems!=null){
                setupSelectionCallback();
            }
        }

        ClosableAction removeSelectionCallback;

        public void setupSelectionCallback(){
            try {
                if (removeSelectionCallback != null) {
                    removeSelectionCallback.close();
                }

                if(selectedItems!=null) {
                    selectedItems.addOnListChangedCallback(selectionCallback);

                    ObservableList old = selectedItems;

                    removeSelectionCallback = new ClosableAction(() -> {
                        old.removeOnListChangedCallback(selectionCallback);
                    });
                }else{
                    removeSelectionCallback = null;
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
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
        public int getItemViewType(int position) {
            if(headerLayoutId>0){
                if(position==0)
                    return headerLayoutId;
                position--;
                if(position==items.size())
                    return footerLayoutId;
                return getItemLayout(position);
            }

            if(position < items.size())
                return getItemLayout(position);

            if(position == items.size() && footerLayoutId > 0)
                return footerLayoutId;

            return getItemLayout(position);
        }

        private int getItemLayout(int i) {
            if(layoutSelector!=null){
                return layoutSelector.call(items.get(i));
            }
            return layoutId;
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),viewType, parent, false);
            if(viewType == headerLayoutId || viewType == footerLayoutId){
                return new HeaderFooterViewHolder(binding);
            }
            return new ItemViewHolder(binding) ;
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {

            ReflectionHelper.run(holder.dataBinding,"set",viewModel);
            if(holder instanceof ItemViewHolder){

                if(headerLayoutId>0){
                    position--;
                }

                Object item = items.get(position);

                ItemViewHolder ivh = (ItemViewHolder) holder;


                if(ivh.model != item){
                    ivh.model = item;
                    ReflectionHelper.run(ivh.dataBinding,"set",item);

                    if(itemHeader != null) {

                        Object ih = itemHeader.call(item);

                        // check if we need to display header or not...
                        if (position == 0) {
                            // yes..

                            ivh.itemModel.header.set(ih);
                        }else {
                            Object prev = items.get(position-1);
                            Object prevHeader = itemHeader.call(prev);
                            if(!Atom.equals(ih,prevHeader)){
                                ivh.itemModel.header.set(ih);
                            }else{
                                 ivh.itemModel.header.set(null);
                            }
                        }
                    }
                }

                if(ivh.viewModel != viewModel){
                    ivh.viewModel = viewModel;
                    ReflectionHelper.run(ivh.dataBinding,"set",viewModel);
                }

                if(selectedItems!=null) {
                    try {
                        ivh.updateSelected = false;
                        boolean isSelected = selectedItems.contains(item);
                        ivh.itemModel.selected.set(isSelected);
                    } finally {
                        ivh.updateSelected = true;
                    }
                }
            }
            holder.dataBinding.executePendingBindings();
        }


        @Override
        public int getItemCount() {
            int total = items.size();
            if(headerLayoutId>0) {
                if(footerLayoutId>0)
                    return total + 2;
                return total + 1;
            }
            return total;
        }
    }

    public static class ItemModel {

        public final ObservableBoolean selected = new ObservableBoolean(false);

        public final ObservableField<Object> header = new ObservableField<>();

    }

    public static class BindableViewHolder extends RecyclerView.ViewHolder {


        public final ViewDataBinding dataBinding;

        public BindableViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.dataBinding = binding;

        }
    }

    public static class  HeaderFooterViewHolder extends BindableViewHolder {

        public HeaderFooterViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public class ItemViewHolder extends BindableViewHolder {



        public boolean updateSelected = true;

        public Object model;

        public Object viewModel;

        public final ItemModel itemModel;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding);

            itemModel = new ItemModel();
            ReflectionHelper.run(binding,"set",itemModel);

            itemModel.selected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if(!updateSelected)
                        return;
                    if(selectedItems == null)
                        return;
                    if(selectedItems.contains(model)){
                        if(allowMultipleSelection){
                            selectedItems.remove(model);
                        }
                    }else{
                        if(!allowMultipleSelection){
                            selectedItems.clear();
                        }
                        selectedItems.add(model);
                    }
                }
            });
        }

        public void toggleSelection(){
            itemModel.selected.set(!itemModel.selected.get());
        }
    }


}
