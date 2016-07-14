package com.drivingassisstantHouse.library.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drivingassisstantHouse.library.R;
import com.drivingassisstantHouse.library.tools.ToolUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vlonjat Gashi (vlonjatg)
 *         修改自ProgressAvtivity开源项目。可作为页面的根布局，也可以作为页面的子布局进行使用。\n
 *         可用于程序程序加载中，获取数据失败，获取数据为空 三种状态的占位。获取数据失败提供了按钮进行重试
 * @modify by Sunji
 */
public class ProgressView extends RelativeLayout {

    private static final String TAG_LOADING = "ProgressView.TAG_LOADING";
    private static final String TAG_EMPTY = "ProgressView.TAG_EMPTY";
    private static final String TAG_ERROR = "ProgressView.TAG_ERROR";

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    LayoutInflater inflater;
    View view;
    LayoutParams layoutParams;
    Drawable currentBackground;

    List<View> contentViews = new ArrayList<>();

    RelativeLayout loadingStateRelativeLayout;
    ProgressBar loadingStateProgressBar;
    TextView loadingStateTview;

    RelativeLayout emptyStateRelativeLayout;
    ImageView emptyStateImageView;
    TextView emptyStateTitleTextView;
    TextView emptyStateContentTextView;

    RelativeLayout errorStateRelativeLayout;
    ImageView errorStateImageView;
    TextView errorStateTitleTextView;
    TextView errorStateContentTextView;
    Button errorStateButton;

    int loadingStateProgressBarWidth;
    int loadingStateProgressBarHeight;
    int loadingStateBackgroundColor;
    int loadingStateTitleTextSize;
    int loadingStateTitleTextColor;

    int emptyStateImageWidth;
    int emptyStateImageHeight;
    int emptyStateTitleTextSize;
    int emptyStateContentTextSize;
    int emptyStateTitleTextColor;
    int emptyStateContentTextColor;
    int emptyStateBackgroundColor;

    int errorStateImageWidth;
    int errorStateImageHeight;
    int errorStateTitleTextSize;
    int errorStateContentTextSize;
    int errorStateTitleTextColor;
    int errorStateContentTextColor;
    int errorStateButtonTextColor;
    int errorStateBackgroundColor;

    private String state = CONTENT;

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);

        //Loading state attrs
        loadingStateProgressBarWidth =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_loadingProgressBarWidth, ToolUnit.dipTopx(30));

        loadingStateProgressBarHeight =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_loadingProgressBarHeight, ToolUnit.dipTopx(30));

        loadingStateBackgroundColor =
                typedArray.getColor(R.styleable.ProgressView_loadingBackgroundColor, Color.TRANSPARENT);

        loadingStateTitleTextColor = typedArray.getColor(R.styleable.ProgressView_loadingTitleTextColor, Color.BLACK);
        loadingStateTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.ProgressView_loadingTitleTextSize, 15);
        //Empty state attrs
        emptyStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_emptyImageWidth, 308);

        emptyStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_emptyImageHeight, 308);

        emptyStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_emptyTitleTextSize, 15);

        emptyStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_emptyContentTextSize, 14);

        emptyStateTitleTextColor =
                typedArray.getColor(R.styleable.ProgressView_emptyTitleTextColor, Color.BLACK);

        emptyStateContentTextColor =
                typedArray.getColor(R.styleable.ProgressView_emptyContentTextColor, Color.BLACK);

        emptyStateBackgroundColor =
                typedArray.getColor(R.styleable.ProgressView_emptyBackgroundColor, Color.TRANSPARENT);

        //Error state attrs
        errorStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_errorImageWidth, 308);

        errorStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_errorImageHeight, 308);

        errorStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_errorTitleTextSize, 15);

        errorStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.ProgressView_errorContentTextSize, 14);

        errorStateTitleTextColor =
                typedArray.getColor(R.styleable.ProgressView_errorTitleTextColor, Color.BLACK);

        errorStateContentTextColor =
                typedArray.getColor(R.styleable.ProgressView_errorContentTextColor, Color.BLACK);

        errorStateButtonTextColor =
                typedArray.getColor(R.styleable.ProgressView_errorButtonTextColor, Color.BLACK);

        errorStateBackgroundColor =
                typedArray.getColor(R.styleable.ProgressView_errorBackgroundColor, Color.TRANSPARENT);

        typedArray.recycle();

        currentBackground = this.getBackground();
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {

            contentViews.add(child);
        }
    }

    /**
     * Hide all other states and show content
     */
    public void showContent() {
        switchState(CONTENT, null, null, null, null, null, Collections.<Integer>emptyList());
    }

//    /**
//     * Hide all other states and show content
//     *
//     * @param skipId Id of view not to show
//     */
//    public void showContent(Integer skipId) {
//        ArrayList<Integer> skipIds = new ArrayList<>();
//        skipIds.add(skipId);
//        showContent(skipIds);
//    }

    /**
     * Hide all other states and show content
     *
     * @param skipIds Id of view not to show
     */
    public void showContent(Integer... skipIds) {
        ArrayList<Integer> integers = new ArrayList<>();
        if (null != skipIds && skipIds.length > 0) {
            for (Integer id : skipIds) {
                integers.add(id);
            }
        }
        showContent(integers);
    }

    /**
     * Hide all other states and show content
     *
     * @param skipIds Ids of views not to show
     */
    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, null, null, null, null, null, skipIds);
    }

    /**
     * Hide content and show the progress bar
     */
    public void showLoading() {
        switchState(LOADING, null, getContext().getString(R.string.loading_waiting), null, null, null, Collections.<Integer>emptyList());
    }

    public void showLoading(String text) {
        String tempText = text == null ? getContext().getString(R.string.loading_waiting) : text;
        switchState(LOADING, null, tempText, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide content and show the progress bar
     *
     * @param skipIds Ids of views to not hide
     */
    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, null, getContext().getString(R.string.loading_waiting), null, null, null, skipIds);
    }

    /**
     * Hide content and show the progress bar
     *
     * @param skipId Id of view to not hide
     */
    public void showLoading(Integer skipId) {
        ArrayList<Integer> skipIds = new ArrayList<>();
        skipIds.add(skipId);
        showLoading(skipIds);
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable Drawable to show
     * @param emptyTextTitle     Title of the empty view to show
     * @param emptyTextContent   Content of the empty view to show
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent) {
        switchState(EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable Drawable to show
     * @param emptyTextTitle     Title of the empty view to show
     * @param emptyTextContent   Content of the empty view to show
     * @param skipIds            Ids of views to not hide
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent, List<Integer> skipIds) {
        switchState(EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, null, skipIds);
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable Drawable to show
     * @param errorTextTitle     Title of the error view to show
     * @param errorTextContent   Content of the error view to show
     * @param errorButtonText    Text on the error view button to show
     * @param onClickListener    Listener of the error view button
     */
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, String errorButtonText, OnClickListener onClickListener) {
        switchState(ERROR, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, onClickListener, Collections.<Integer>emptyList());
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable Drawable to show
     * @param errorTextTitle     Title of the error view to show
     * @param errorTextContent   Content of the error view to show
     * @param errorButtonText    Text on the error view button to show
     * @param onClickListener    Listener of the error view button
     * @param skipIds            Ids of views to not hide
     */
    public void showError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        switchState(ERROR, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, onClickListener, skipIds);
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public String getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state.equals(CONTENT);
    }

    /**
     * Check if loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state.equals(LOADING);
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state.equals(EMPTY);
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state.equals(ERROR);
    }

    private void switchState(String state, Drawable drawable, String errorText, String errorTextContent,
                             String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        this.state = state;

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                hideLoadingView();
                hideEmptyView();
                hideErrorView();

                setContentVisibility(true, skipIds);
                break;
            case LOADING:
                hideEmptyView();
                hideErrorView();

                setLoadingView();
                loadingStateTview.setText(errorText);
                setContentVisibility(false, skipIds);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();

                setEmptyView();
                emptyStateImageView.setImageDrawable(drawable);
                emptyStateTitleTextView.setText(errorText);
                emptyStateContentTextView.setText(errorTextContent);
                setContentVisibility(false, skipIds);
                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();

                setErrorView();
                errorStateImageView.setImageDrawable(drawable);
                errorStateTitleTextView.setText(errorText);
                errorStateContentTextView.setText(errorTextContent);
                errorStateButton.setText(errorButtonText);
                errorStateButton.setOnClickListener(onClickListener);
                setContentVisibility(false, skipIds);
                break;
        }
    }

    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_loading_view, this,false);
            loadingStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadingStateRelativeLayout);
            loadingStateRelativeLayout.setTag(TAG_LOADING);

            loadingStateProgressBar = (ProgressBar) view.findViewById(R.id.loadingStateProgressBar);
            loadingStateTview = (TextView) view.findViewById(R.id.loadingstateTitleview);
            loadingStateTview.setTextColor(loadingStateTitleTextColor);
            loadingStateTview.setTextSize(loadingStateTitleTextSize);
            ViewGroup.LayoutParams params = loadingStateProgressBar.getLayoutParams();
            params.width = loadingStateProgressBarWidth;
            params.height = loadingStateProgressBarHeight;
            loadingStateProgressBar.setLayoutParams(params);

            //Set background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(loadingStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_empty_view, null);
            emptyStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyStateRelativeLayout);
            emptyStateRelativeLayout.setTag(TAG_EMPTY);

            emptyStateImageView = (ImageView) view.findViewById(R.id.emptyStateImageView);
            emptyStateTitleTextView = (TextView) view.findViewById(R.id.emptyStateTitleTextView);
            emptyStateContentTextView = (TextView) view.findViewById(R.id.emptyStateContentTextView);

            //Set empty state image width and height
            emptyStateImageView.getLayoutParams().width = emptyStateImageWidth;
            emptyStateImageView.getLayoutParams().height = emptyStateImageHeight;
            emptyStateImageView.requestLayout();

            emptyStateTitleTextView.setTextSize(emptyStateTitleTextSize);
            emptyStateContentTextView.setTextSize(emptyStateContentTextSize);
            emptyStateTitleTextView.setTextColor(emptyStateTitleTextColor);
            emptyStateContentTextView.setTextColor(emptyStateContentTextColor);

            //Set background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(emptyStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(emptyStateRelativeLayout, layoutParams);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setErrorView() {
        if (errorStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_error_view, null);
            errorStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.errorStateRelativeLayout);
            errorStateRelativeLayout.setTag(TAG_ERROR);

            errorStateImageView = (ImageView) view.findViewById(R.id.errorStateImageView);
            errorStateTitleTextView = (TextView) view.findViewById(R.id.errorStateTitleTextView);
            errorStateContentTextView = (TextView) view.findViewById(R.id.errorStateContentTextView);
            errorStateButton = (Button) view.findViewById(R.id.errorStateButton);

            //Set error state image width and height
            errorStateImageView.getLayoutParams().width = errorStateImageWidth;
            errorStateImageView.getLayoutParams().height = errorStateImageHeight;
            errorStateImageView.requestLayout();

            errorStateTitleTextView.setTextSize(errorStateTitleTextSize);
            errorStateContentTextView.setTextSize(errorStateContentTextSize);
            errorStateTitleTextView.setTextColor(errorStateTitleTextColor);
            errorStateContentTextView.setTextColor(errorStateContentTextColor);
            errorStateButton.setTextColor(errorStateButtonTextColor);

            //Set background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(errorStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : contentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }

    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }

    private void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }
}