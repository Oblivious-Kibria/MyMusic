package com.example.amazingaayan.mymusic;

/**
 * Created by Amazing Aayan on 17-Jun-16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Amazing Aayan on 12/30/2015.
 */
public class SongListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] songs;

    public SongListAdapter(Context context, String[] songs) {
        super(context, -1, songs);
        this.context = context;
        this.songs = songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.playlist_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.songTitle);
        textView.setText(songs[position]);
        return rowView;
    }
}