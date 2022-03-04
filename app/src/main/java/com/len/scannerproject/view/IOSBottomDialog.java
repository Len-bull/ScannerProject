package com.len.scannerproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.len.scannerproject.R;
import com.tdqc.util.DeviceUtils;
import com.tdqc.util.ResourceUtils;
import com.tdqc.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿IOS底部弹出窗。
 * Created by weicheng on 2017/3/27.
 */
public class IOSBottomDialog {

    private static final String TAG = "IOSBottomDialog";
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private View diviver_title;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<DialogItem> sheetItemList;

    public IOSBottomDialog(Context context) {
        this(context,false);
    }

    public IOSBottomDialog(@NonNull Context context, boolean isFullScreen) {
        this.context = context;
        if(context == null){
            return;
        }
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ios_bottom, null);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(DeviceUtils.getScreenWidth());
        // 获取自定义Dialog布局中的控件
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        diviver_title = view.findViewById(R.id.diviver_title);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
//                    L.e(TAG,e);
                }
            }
        });
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        if(isFullScreen){ /*全屏，隐藏状态栏和虚拟按键*/
            dialog.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    public IOSBottomDialog setTitle(String title) {
        if(context == null && !StringUtils.isEmpty(title)){
            return this;
        }
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        diviver_title.setVisibility(View.VISIBLE);
        return this;
    }

    public IOSBottomDialog setCancelable(boolean cancel) {
        if(context == null){
            return this;
        }
        dialog.setCancelable(cancel);
        return this;
    }

    public IOSBottomDialog setCanceledOnTouchOutside(boolean cancel) {
        if(context == null){
            return this;
        }
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 添加条目
     * @param strItem 条目名称
     * @param color 条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return
     */
    public IOSBottomDialog addSheetItem(@NonNull String strItem, @Nullable TextColor color, @Nullable OnDialogItemClickListener listener) {
        if(context == null && !StringUtils.isEmpty(strItem)){
            return this;
        }
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<>();
        }
        sheetItemList.add(new DialogItem(strItem, color, listener));
        return this;
    }

    /** 设置条目布局 */
    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }
        int size = sheetItemList.size();
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sLayout_content.getLayoutParams();
            params.height = DeviceUtils.getScreenHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }
        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i - 1;
            DialogItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            TextColor color = sheetItem.color;
            final OnDialogItemClickListener listener = sheetItem.itemClickListener;
            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            textView.setGravity(Gravity.CENTER);
            View diviver = null;
            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_bottom);
                } else {
                    textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_single);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_middle);
                        diviver = getDiviver();
                    } else {
                        textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_bottom);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_top);
                        diviver = getDiviver();
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_middle);
                        diviver = getDiviver();
                    } else {
                        textView.setBackgroundResource(R.drawable.selector_ios_dialog_item_bottom);
                    }
                }
            }
            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(TextColor.Blue.name));
            } else {
                textView.setTextColor(Color.parseColor(color.name));
            }
            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (60 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
            // 点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
//                        L.e(TAG,e);
                    }
                }
            });
            lLayout_content.addView(textView);
            if(diviver != null){
                lLayout_content.addView(diviver);
            }
        }
    }

    /**显示*/
    public void show() {
        if(context == null){
            return ;
        }
        try {
            setSheetItems();
            dialog.show();
        } catch (Exception e) {
//            L.e(TAG,e);
        }
    }

    /**获取分割线View*/
    private View getDiviver(){
        View diviver = new View(context);
        diviver.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
        diviver.setBackgroundColor(ResourceUtils.getColor(R.color.ios_dialog_gray));
        return diviver;
    }

    public interface OnDialogItemClickListener {
        void onClick(int which);
    }

    public class DialogItem {
        String name;
        OnDialogItemClickListener itemClickListener;
        TextColor color;
        public DialogItem(String name, TextColor color, OnDialogItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public enum TextColor {
        Blue("#037BFF"), Red("#FD4A2E");
        public String name;
        TextColor(String name) {
            this.name = name;
        }
    }

}
