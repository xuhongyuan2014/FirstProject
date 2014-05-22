package com.example.first.broadcast;

import com.example.first.FullscreenActivity;
import com.example.first.R;
import com.example.first.broadcast.dialogActivity.OutgoingCallDialog;
import com.example.first.service.WatchService;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class OutgoingCallReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String callNumber=this.getResultData();
this.setResultData(null);
Intent intentDialog = new Intent(context, OutgoingCallDialog.class);
intentDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
intentDialog.putExtra("calNumber", callNumber);
context.startActivity(intentDialog);
	}

	
	/*protected void dialog(Context context) {

		AlertDialog.Builder builder = new Builder(context);

		builder.setMessage("ȷ�ϲ���绰��");

		builder.setTitle("��ʾ");

		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				//OutgoingCallDialog.this.finish();

			}

		});

		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//OutgoingCallDialog.this.finish();
			}

		});
		builder.create().show();

	}*/
}
