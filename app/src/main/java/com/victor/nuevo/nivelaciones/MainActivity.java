package com.victor.nuevo.nivelaciones;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		
		String trazadosTitle = getString(R.string.trazado);
		String trazadosSubTitle = getString(R.string.sub_trazado);
		String replanteosTitle = getString(R.string.replanteos);
		String replanteosSubTitle = getString(R.string.sub_replanteos);
		String nivelacionSimpleTitle = getString(R.string.nivelacion_simple);
		String nivelacionSimpleSubTitle = getString(R.string.sub_nivelacion_simple);
		String nivelacionPrecisionTitle = getString(R.string.nivelacion_precision);
		String nivelacionPrecisionSubTitle = getString(R.string.sub_nivelacion_precision);
		
		PojoGridItems items[] = new PojoGridItems[]{
			new PojoGridItems(R.drawable.ic_trazado, trazadosTitle, trazadosSubTitle),
			new PojoGridItems(R.drawable.ic_replanteo, replanteosTitle, replanteosSubTitle),
			new PojoGridItems(R.drawable.ic_niv_simple, nivelacionSimpleTitle, nivelacionSimpleSubTitle),
			new PojoGridItems(R.drawable.ic_niv_comp, nivelacionPrecisionTitle, nivelacionPrecisionSubTitle)
		};
		

		ImageAdapter adapter = new ImageAdapter(this, items);
		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						Intent iTrazado = new Intent(MainActivity.this, TrazadoClass.class);
						startActivity(iTrazado);
					break;
					case 1:
						
					break;
					case 2:
						
					break;
					case 3:
						
					break;
				}
			}
		});
	}
	
	
	public class ImageAdapter extends BaseAdapter{
		private Activity activity;
		private PojoGridItems items[];
		
		public ImageAdapter(Activity activity, PojoGridItems items[]){
			this.activity = activity;
			this.items = items;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			
			if(convertView == null){
				LayoutInflater inflater = activity.getLayoutInflater();
				convertView = inflater.inflate(R.layout.view_grid_menu, null);
				
				holder = new ViewHolder();
				
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.txtTitle = (TextView) convertView.findViewById(R.id.textView1);
				holder.txtSubTitle = (TextView) convertView.findViewById(R.id.textView2);
				
				convertView.setTag(holder);
			} else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.imageView.setImageResource(items[position].getImage());
			holder.txtTitle.setText(items[position].getTitle());
			holder.txtSubTitle.setText(items[position].getSubTitle());
			
			return convertView;
		}
		
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView txtTitle;
		TextView txtSubTitle;
	}
}
