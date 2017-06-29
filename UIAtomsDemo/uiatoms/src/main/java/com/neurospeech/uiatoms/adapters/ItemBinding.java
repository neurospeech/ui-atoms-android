package com.neurospeech.uiatoms.adapters;


import android.support.annotation.LayoutRes;

/**
 * ItemBinding Created by  on 6/15/2017.
 * BR class is generated during compile time hence - ItemBinding class had to be created
 * Here variableId is the model Id and layout Id is the item layout
 * ItemBinding has to be used for model in question for e.g. in RealEstateModel
 */

public class ItemBinding<T> {

    private int variableId;

    public Object getViewModel() {
        return viewModel;
    }

    public int getViewModelId() {
        return viewModelId;
    }

    private Object viewModel;

    /**
     *
     * @return the model in question
     */
    public int getVariableId() {
        return variableId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    private int layoutId;

    private int viewModelId;

    /**
     *
     * @param variableId - Model
     * @param layoutId - Item Layout
     * @param <T> - Generic type
     * @return
     *
     * Now since we do not give itemBinding in recycler view, we do not have the
     * name of model whose items have to be displayed in the recycler view. Hence in
     * RecyclerBindingAdapter - model is bound to BR.model and viewModel is bound to
     * BR.viewModel. But since we want viewModel to be dynamic, it will be resolved
     * runtime and hence we pass the viewModelId as well.
     * Going forward, as a convention for all the models we should specify "model" as
     * variable name and "viewModel" for the fragment/activity etc name in the xml.
     */
    public static <T> ItemBinding<T> of(int variableId,@LayoutRes int layoutId, int viewModelId, Object viewModel) {
        ItemBinding<T> binding = new ItemBinding<T>();
        binding.variableId = variableId;
        binding.layoutId = layoutId;
        binding.viewModelId = viewModelId;
        binding.viewModel = viewModel;
        return  binding;
    }

    /**
     *
     * @param variableId - Model
     * @param layoutId - Item Layout
     * @param <T> - Generic type
     * @return
     */
    public static <T> ItemBinding<T> of(int variableId,@LayoutRes int layoutId) {
        return  of(variableId,layoutId,0,null);
    }
}
