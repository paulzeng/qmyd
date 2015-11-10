package com.ak.qmyd.receiver;

import java.util.List;

import com.ak.qmyd.activity.AlarmActivity;
import com.ak.qmyd.activity.AlarmMiniActivity;
import com.ak.qmyd.activity.TrainAlertListActivity;
import com.ak.qmyd.bean.NoteBean;
import com.ak.qmyd.db.DBManager;
import com.ak.qmyd.dialog.AlarmDialog;
import com.ak.qmyd.dialog.AlarmDialog.OnActionSheetSelected;
import com.ak.qmyd.tools.DebugUtility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author JGB
 * @date 2015-5-29 上午10:12:39
 */
public class AlarmReceiver extends BroadcastReceiver {

	private List<NoteBean> noteList;
	private NoteBean noteBean;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		noteBean = DBManager.getInstance(context).getNoteByID(
				Integer.parseInt((String) bundle.getCharSequence("id")));
		if (noteBean.getIsstart() != null && noteBean.getIsstart().equals("true")) {
			if ("alertList".equals(intent.getAction())) {
				DebugUtility.showLog("alertList的时间"+bundle.getCharSequence("time"));
				Intent i = new Intent(context, AlarmActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("id", bundle.getCharSequence("id"));
				i.putExtra("title", bundle.getCharSequence("title"));
				i.putExtra("time", bundle.getCharSequence("time"));
				i.putExtra("day", bundle.getCharSequence("day"));
				i.putExtra("way", bundle.getCharSequence("way"));
				i.putExtra("count", bundle.getCharSequence("count"));
				i.putExtra("timetype", bundle.getCharSequence("timetype"));
				i.putExtra("currentTotalTime",
						bundle.getCharSequence("currentTotalTime"));
				context.startActivity(i);
			}else if("miniAlarm".equals(intent.getAction())){
				DebugUtility.showLog("miniAlarm的时间"+bundle.getCharSequence("time"));
				Intent i = new Intent(context, AlarmMiniActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("id", bundle.getCharSequence("id"));
				i.putExtra("title", bundle.getCharSequence("title"));
				i.putExtra("time", bundle.getCharSequence("time"));
				i.putExtra("day", bundle.getCharSequence("day"));
				i.putExtra("way", bundle.getCharSequence("way"));
				i.putExtra("count", bundle.getCharSequence("count"));
				i.putExtra("timetype", bundle.getCharSequence("timetype"));
				i.putExtra("currentTotalTime",
						bundle.getCharSequence("currentTotalTime"));
				context.startActivity(i);
			}
		}
	}
}
