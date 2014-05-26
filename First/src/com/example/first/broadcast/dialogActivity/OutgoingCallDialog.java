package com.example.first.broadcast.dialogActivity;

import com.example.first.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class OutgoingCallDialog extends Activity {
	private String callNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callNumber=this.getIntent().getStringExtra("callNumber");
		dialog();
	}

	protected void dialog() {

		AlertDialog.Builder builder = new Builder(OutgoingCallDialog.this);
		
		builder.setMessage("确认拨打电话吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
               Uri.parse("tel:" + callNumber));
                 startActivity(phoneIntent);
				dialog.dismiss();
				OutgoingCallDialog.this.finish();

			}

		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OutgoingCallDialog.this.finish();
			}

		});
		Dialog dialog=builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();

	}

}
