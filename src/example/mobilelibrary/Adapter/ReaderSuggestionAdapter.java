package example.mobilelibrary.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.iMobileLib.NjutcmMobileLibrary.R;

public class ReaderSuggestionAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String,String>>list;
    private class Itemholder{
    	public TextView bookTitle;
    	public TextView bookAuthor;
    	public TextView bookPublic;
    	public TextView bookDate;
    	public TextView bookState;
    }
	public ReaderSuggestionAdapter(Context context,ArrayList<HashMap<String,String>>list){
		this.context=context;
		this.list=list;
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

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		Itemholder holder=null;
		if(view==null){
			holder=new Itemholder();
			LayoutInflater inflater=LayoutInflater.from(context);
			view=inflater.inflate(R.drawable.readersuggestion, null);
			holder.bookTitle=(TextView)view.findViewById(R.id.readerSuggestionBookTitle);
			holder.bookAuthor=(TextView)view.findViewById(R.id.readerSuggestionBookAuthor);
			holder.bookPublic=(TextView)view.findViewById(R.id.publicationInfo);
			holder.bookDate=(TextView)view.findViewById(R.id.purchaseDate);
			holder.bookState=(TextView)view.findViewById(R.id.purchaseStatus);
			view.setTag(holder);
		}
		else
			holder=(Itemholder)view.getTag();
		holder.bookTitle.setText(list.get(position).get("title"));
		holder.bookAuthor.setText(list.get(position).get("author"));
		holder.bookPublic.setText(list.get(position).get("publicationInfo"));
		holder.bookDate.setText(list.get(position).get("purchaseDate"));
		holder.bookState.setText(list.get(position).get("purchaseStatus"));
		if(list.get(position).get("purchaseStatus").trim().equals("ÒÑ¶©¹º")){
			holder.bookState.setTextColor(Color.BLUE);
		}
		else if(list.get(position).get("purchaseStatus").trim().equals("´ý´¦Àí")){
			holder.bookState.setTextColor(Color.RED);
		}
		else
			holder.bookState.setTextColor(Color.GREEN);
		return view;
	}

}
