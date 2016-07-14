package com.drivingassisstantHouse.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.drivingassisstantHouse.library.tools.ToolResource;


/**
 * 自定义进度对话框
 * @author
 * @version 1.0
 */
public class ProgressDialog extends Dialog {
	
	/**消息TextView*/
	private TextView tvMsg ; 
	/**
	 * 构造方法
	 * @param  context 上下文
	 * @param  strMessage 需要设置的进度消息
	 * **/
	public ProgressDialog(Context context, String strMessage) {
		this(context, ToolResource.getStyleId("CustomProgressDialog"),strMessage);
	}

	public ProgressDialog(Context context, int theme, String strMessage) {
		super(context, theme);
		this.setContentView(ToolResource.getLayoutId("view_progress_dialog"));
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
	    tvMsg = (TextView) this.findViewById(ToolResource.getIdId("tv_msg"));
	    setMessage(strMessage);
	}

	/**
	 * 设置进度条消息
	 * @param strMessage 消息文本
	 */
	public void setMessage(String strMessage){
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}
}