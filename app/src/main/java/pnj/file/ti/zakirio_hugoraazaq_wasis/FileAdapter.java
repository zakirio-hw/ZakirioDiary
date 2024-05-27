package pnj.file.ti.zakirio_hugoraazaq_wasis;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {

    ArrayList<String> fileArray;
    Context context;

    public FileAdapter(ArrayList<String> fileArray, Context context) {
        this.fileArray = fileArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fileArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            TextView textView = new TextView(context);
            textView.setTextColor(Color.rgb(10, 10, 10));
            textView.setTextSize(20);
            textView.setText(fileArray.get(position));
            convertView = textView;
        }

        return convertView;
    }
}
