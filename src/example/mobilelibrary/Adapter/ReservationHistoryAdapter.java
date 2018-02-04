package example.mobilelibrary.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.iMobileLib.NjutcmMobileLibrary.MenuActivity;
import cn.iMobileLib.NjutcmMobileLibrary.R;
import cn.iMobileLib.NjutcmMobileLibrary.WebServiceHelper;

public class ReservationHistoryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private String title = null;
	private String marc_no = null;
	private String indexNo = null;
	ProgressDialog pDialog = null;
	private String result = null;

	private class Itemholder {
		public TextView bookTitle;
		public TextView bookAuthor;
		public TextView bookStorePlace;
		public TextView bookReservationDate;
		public TextView bookReturnDate;
		public TextView bookState;
		public Button cancelButton;
	}

	public ReservationHistoryAdapter(Context context,
			ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	};
	Handler handler2 = new Handler() {
	};

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		Itemholder holder = null;

		if (view == null) {
			holder = new Itemholder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.drawable.reservationhistory, null);
			holder.bookTitle = (TextView) view
					.findViewById(R.id.reservationHistoryBookTitle);
			holder.bookAuthor = (TextView) view
					.findViewById(R.id.reservationHistoryBookAuthor);
			holder.bookStorePlace = (TextView) view
					.findViewById(R.id.reservationHistoryStorePlace);
			holder.bookReservationDate = (TextView) view
					.findViewById(R.id.reservationDate);
			holder.bookReturnDate = (TextView) view
					.findViewById(R.id.reservationHistoryReturnDate);
			holder.bookState = (TextView) view
					.findViewById(R.id.reservationHistoryStatus);
			holder.cancelButton = (Button) view.findViewById(R.id.cancelButton);
			view.setTag(holder);
		} else
			holder = (Itemholder) view.getTag();
		title = list.get(position).get("title");
		marc_no = list.get(position).get("marc_no");
		indexNo = list.get(position).get("bookNumber");
		System.out.println("title=" + title);
		System.out.println("marc_no=" + marc_no);
		System.out.println("indexNo=" + indexNo);
		holder.bookTitle.setText(list.get(position).get("title"));
		holder.bookAuthor.setText(list.get(position).get("author"));
		holder.bookStorePlace.setText(list.get(position).get("storePlace"));
		holder.bookReservationDate.setText(list.get(position).get(
				"reservationDate"));
		holder.bookReturnDate.setText(list.get(position).get("returnDate"));
		holder.bookState.setText(list.get(position).get("status"));
		if (list.get(position).get("status").trim().equals("申请中")) {
			holder.bookState.setTextColor(Color.RED);
		}
		holder.cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (title != null) {
					pDialog = new ProgressDialog(context);
					pDialog.setMessage("预约取消中,请稍后...");
					pDialog.show();
					new Thread() {
						public void run() {
							try {
								result = WebServiceHelper.CancelReservation(
										marc_no, indexNo,
										MenuActivity.passWord,
										MenuActivity.userCode);
								handler.sendEmptyMessage(0);
								if(result!=null){
								handler2.post(new Runnable() {

									@Override
									public void run() {
										AlertDialog.Builder builder = new Builder(
												context);
										builder.setMessage(result);
										builder.setTitle("提示");
										builder.setPositiveButton(
												"确认",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												});
										builder.create().show();

									}
								});
							}else {
								handler2.post(new Runnable() {

									@Override
									public void run() {
										AlertDialog.Builder builder = new Builder(
												context);
										builder.setMessage("请求出错啦,请检查网络后重试...");
										builder.setTitle("提示");
										builder.setPositiveButton(
												"确认",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												});
										builder.create().show();

									}
								});
							}
							} catch (Exception e) {
								e.printStackTrace();
							}

						};
					}.start();
				}else {
					handler2.post(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(context, "取消预约出错啦...", Toast.LENGTH_SHORT).show();
							
						}
					});
				}
			}
		});
		return view;
	}

}
