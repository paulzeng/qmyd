package com.ak.qmyd.view;

import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.StringUtil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.widget.EditText;

/**
 * @author JGB
 * @param <Editor>
 * @date 2015-6-29 ÏÂÎç3:28:52
 */
public class MyEditText extends EditText {

	private static final int ID_PASTE = android.R.id.paste;
	private static final int ID_COPY = android.R.id.copy;
	private ClipboardManager clip;
	private CharSequence paste;
	@ViewDebug.ExportedProperty(category = "text")
	private CharSequence mText;
	private Editor mEditor;
	static long LAST_CUT_OR_COPY_TIME;

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mText = "";
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTextContextMenuItem(int id) {
		clip = (ClipboardManager) getContext().getSystemService(
				Context.CLIPBOARD_SERVICE);
		paste = clip.getText();
		int min = 0;
		int max = mText.length();

		if (isFocused()) {
			final int selStart = getSelectionStart();
			final int selEnd = getSelectionEnd();

			min = Math.max(0, Math.min(selStart, selEnd));
			max = Math.max(0, Math.max(selStart, selEnd));
		}

		switch (id) {
		case ID_PASTE:
			DebugUtility.showLog("Êä³ö×Ö·û" + paste.toString());
			paste(min, max);
			return true;

		}
		return super.onTextContextMenuItem(id);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	private void paste(int min, int max) {

		ClipboardManager clipboard = (ClipboardManager) getContext()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = clipboard.getPrimaryClip();
		if (clip != null) {
			boolean didFirst = false;
			for (int i = 0; i < clip.getItemCount(); i++) {
				CharSequence paste = clip.getItemAt(i).coerceToStyledText(
						getContext());
				if (paste != null) {
					if (!didFirst) {
						Selection.setSelection((Spannable) mText, max);
						((Editable) mText).replace(min, max, paste);
						didFirst = true;
					} else {
						((Editable) mText).insert(getSelectionEnd(), "\n");
						((Editable) mText).insert(getSelectionEnd(), paste);
					}
				}
			}
			LAST_CUT_OR_COPY_TIME = 0;
		}
	}
}
