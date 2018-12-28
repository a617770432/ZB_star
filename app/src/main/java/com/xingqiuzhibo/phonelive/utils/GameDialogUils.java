package com.xingqiuzhibo.phonelive.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xingqiuzhibo.phonelive.R;

/**
 * Created by debug on 2018/12/12.
 */

public class GameDialogUils {
    /**
     * 龙虎游戏结果
     *
     * @param context
     * @param title
     * @param content
     * @return
     */
    public static Dialog showGameResultDialog(Context context, String title, String content, boolean isAnchor) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_game_lh_result);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView titleView = (TextView) dialog.findViewById(R.id.tv_tit);
        titleView.setText(title);
        TextView contentTextView = (TextView) dialog.findViewById(R.id.tv_coin_num);
        contentTextView.setText(content);
        if (isAnchor) {
            contentTextView.setTextColor(context.getResources().getColor(R.color.long_));
        }
        dialog.show();
        return dialog;
    }

    public static Dialog showSelectGameResultDialog(Context context, String title, String content, final OnCliCkConfirmListener confirmListener) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_game_lh_select_result);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView titleView = (TextView) dialog.findViewById(R.id.tv_tit);
        titleView.setText(title);
        TextView contentTextView = (TextView) dialog.findViewById(R.id.tv_which_win);
        contentTextView.setText(content);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (confirmListener != null) {
                    confirmListener.onConfirm();
                }
            }
        });
        dialog.show();
        return dialog;
    }

    public interface OnCliCkConfirmListener {
        void onConfirm();
    }

}
