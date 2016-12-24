package edu.zju.com.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import edu.zju.com.librarycontroller.R;


/**
 * Project : PocketMoney
 * Package : com.app.pocketmoney.UI.Widget
 * Created by zhangziqi on 1/26/15.
 */
public class LoadingProgress extends Dialog {

    private static ImageView surround;
    private static Context context;
    private static View contentView;
    private static LoadingProgress loadingProgress;

    private LoadingProgress(Context ctx) {
        super(ctx, R.style.LoadingProgress);
        context = ctx;
        contentView = View.inflate(context, R.layout.common_loading_progress, null);
        surround = (ImageView) contentView.findViewById(R.id.surround);
    }

    static public LoadingProgress getInstance(Context context) {
        loadingProgress = new LoadingProgress(context);
        return loadingProgress;
    }

    static public LoadingProgress getInstance() {
        return loadingProgress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(contentView);
        this.setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        Animation rotatedAnimation = AnimationUtils.loadAnimation(context, R.anim.rotation);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        rotatedAnimation.setInterpolator(linearInterpolator);
        surround.startAnimation(rotatedAnimation);
    }

    public void show(String content) {
        ((TextView) contentView.findViewById(R.id.content)).setText(content);
        show();
    }

    @Override
    public void dismiss() {
        if (!isShowing()) {
            return;
        }
        super.dismiss();
        surround.clearAnimation();
    }
}