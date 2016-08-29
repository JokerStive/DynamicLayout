package com.lilun.passionlife.cloudplatform.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilun.passionlife.R;
import com.lilun.passionlife.cloudplatform.base.BaseFunctionActivity;
import com.lilun.passionlife.cloudplatform.bean.InformationReview;
import com.lilun.passionlife.cloudplatform.bean.OrganizationInformation;
import com.lilun.passionlife.cloudplatform.common.User;
import com.lilun.passionlife.cloudplatform.net.retrofit.ApiFactory;
import com.lilun.passionlife.cloudplatform.net.rxjava.PgSubscriber;
import com.lilun.passionlife.cloudplatform.ui.App;
import com.lilun.passionlife.cloudplatform.utils.StringUtils;
import com.lilun.passionlife.cloudplatform.utils.UIUtils;
import com.orhanobut.logger.Logger;
import com.pacific.adapter.RecyclerAdapter;
import com.pacific.adapter.RecyclerAdapterHelper;

import java.util.List;

import butterknife.Bind;

public class MessageDetailActivity extends BaseFunctionActivity implements View.OnLayoutChangeListener {


    @Bind(R.id.iv_message_writer)
    TextView ivMessageWriter;

    @Bind(R.id.iv_message_time)
    TextView ivMessageTime;

    @Bind(R.id.iv_message_title)
    TextView ivMessageTitle;

    @Bind(R.id.iv_message_content)
    TextView ivMessageContent;

    @Bind(R.id.review_list)
    RecyclerView reviewList;

    @Bind(R.id.im_message_review)
    ImageView imMessageReview;

    @Bind(R.id.message_review)
    LinearLayout rlMessageReview;


    @Bind(R.id.iv_message_review)
    TextView ivMessageReview;


    @Bind(R.id.im_message_share)
    ImageView imMessageShare;
//
//    @Bind(R.id.et_write_review)
//    EditText et_writeReview;

    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    @Bind(R.id.ll_info_pic)
    LinearLayout llInfoPic;


    private OrganizationInformation info;
    private RecyclerAdapter<InformationReview> adapter;
    private PopupWindow pop_review;
    private View view;
    private int screenHeight;
    private int keyHeight;
    private Object infoId;
    private List<InformationReview> reviews;


    @Override
    public View setContent() {
        view = inflater.inflate(R.layout.activity_message_detail, null);
        return view;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get("message") != null) {
            info = (OrganizationInformation) bundle.get("message");
            infoId = info.getId();
            showMessageDetail(info);
        }
        reviewList.setLayoutManager(new LinearLayoutManager(App.app, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.addOnLayoutChangeListener(this);
    }

    private void showMessageDetail(OrganizationInformation info) {
        ivMessageWriter.setText(StringUtils.filteEmpty(info.getUpdatorId()));
        ivMessageTime.setText(StringUtils.filteEmpty(StringUtils.IOS2UTC(info.getCreatedAt())));
        ivMessageTitle.setText(StringUtils.filteEmpty(info.getTitle()));
        ivMessageContent.setText(StringUtils.filteEmpty(info.getContext()));
        showReviews(info.getReviews());
        setIsReview(info.isReplyable());
    }

    /**
     * 展示评论
     */
    private void showReviews(List<InformationReview> reviews) {
        this.reviews = reviews;
        if (reviews != null) {
            if (adapter == null) {
                adapter = new RecyclerAdapter<InformationReview>(App.app, reviews, R.layout.item_message_review) {
                    @Override
                    protected void convert(RecyclerAdapterHelper helper, InformationReview review) {
                        helper.setText(R.id.reviewer_name, StringUtils.filteEmpty(review.getUpdatorId()))
                                .setText(R.id.reviewer_time, StringUtils.IOS2UTC(review.getCreatedAt()))
                                .setText(R.id.reviewer_content, review.getContext());

                    }
                };
                Logger.d("review adapter count = " + adapter.getItemCount());
            }
            reviewList.setAdapter(adapter);
        }
    }


    /**
     * 设置是否可以评论
     */
    private void setIsReview(boolean replyable) {
        imMessageReview.setEnabled(replyable);
        ivMessageReview.setTextColor(replyable ? Color.BLACK : Color.GRAY);
        rlMessageReview.setOnClickListener(v -> {
            if (replyable) {
                relativeLayout.setVisibility(View.GONE);
                showReviewDialog();
            }
        });
    }


    private void showReviewDialog() {
        if (pop_review == null) {
            View view = LayoutInflater.from(App.app).inflate(R.layout.dialog_message_review, null);
            EditText etWritReview = (EditText) view.findViewById(R.id.et_write_review);
            etWritReview.requestFocus();
            TextView tvIssueReview = (TextView) view.findViewById(R.id.tv_issue_review);
            tvIssueReview.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(etWritReview.getText().toString())) {
                    //发布评论
                    Logger.d("then post a review");
                    isssueReview(etWritReview.getText().toString());
                }
            });
            pop_review = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop_review.setOutsideTouchable(false);
            pop_review.setTouchInterceptor((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return false;
                }
                return true;
            });
            pop_review.setOnDismissListener(() -> {
                UIUtils.setBackgroundAlpha(MessageDetailActivity.this, 1.0f);
                Logger.d("change bg alpha 1.0f");
                relativeLayout.setVisibility(View.VISIBLE);
            });
            pop_review.setBackgroundDrawable(new BitmapDrawable());
        }
        UIUtils.toggleKeyBoard();
        UIUtils.setBackgroundAlpha(MessageDetailActivity.this, 0.5f);
        Logger.d("change bg alpha 0.5f");
        pop_review.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 发布评论
     */
    private void isssueReview(String content) {
        addSubscription(ApiFactory.postReview(newReview(content)), new PgSubscriber<InformationReview>(this) {
            @Override
            public void on_Next(InformationReview review) {
                reviews.add(review);
                assert adapter!=null;
                adapter.add(review);
                UIUtils.toggleKeyBoard();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                UIUtils.toggleKeyBoard();
            }
        });
    }

    private InformationReview newReview(String content) {
        InformationReview review = new InformationReview();
        review.setUpdatorId(User.getUsername());
        review.setCreatorId(User.getUsername());
        review.setTitle("");
        review.setContext(content);
        review.setInformationId((int)infoId);
        return review;
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //分别表示，变化前和变化后，四个点的坐标
        if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            if (pop_review != null && pop_review.isShowing()) {
                pop_review.dismiss();
            }
        }
    }
}
