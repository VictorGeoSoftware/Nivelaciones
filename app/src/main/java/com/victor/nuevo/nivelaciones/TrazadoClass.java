package com.victor.nuevo.nivelaciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import com.victor.nuevo.nivelaciones.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.FeatureInfo;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TrazadoClass extends ActionBarActivity implements ActionBar.TabListener
{
	//----- ENVIRONMENT VARIABLES
		//----- Variables para cálculos 
	public static double cotaInicial = 0;
    public static ArrayList<String> puntosDefinicionPlanta = new ArrayList<String>();

	
	
	 //----- PUBLIC ELEMENTS DECLARATION
	public static TextView txtCotaInicial;

    public static TextView txtFicheroSeleccionado;
	
	SectionsPagerAdapter mSectionPageAdapter;
	public static ViewPager mViewPager;
	public static ActionBar actionBar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.trazado_activity);
		
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
		mSectionPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(mSectionPageAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		for(int i = 0; i < mSectionPageAdapter.getCount(); i++){
			actionBar.addTab(actionBar.newTab()
							.setText(mSectionPageAdapter.getPageTitle(i))
							.setTabListener(this));
		}


	}


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return ArrastreCotaFragment.newInstance("");
				case 1:
					return ReplanteoCotaFragment.newInstance("");
				case 2:
					return PuntoInicialFragment.newInstance("");
				default:
					return null;
			}
			
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.arrastre_cota).toUpperCase(l);
			case 1:
				return getString(R.string.replanteo_niveles).toUpperCase(l);
			case 2:
				return getString(R.string.regresar_punto_inicial).toUpperCase(l);
			}
			return null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_trazado, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.cota_inicial:
				EstablecerCotaDialogFragment cotaDialogFragment =  new EstablecerCotaDialogFragment();
				cotaDialogFragment.show(getSupportFragmentManager(), "CotaInicialDialogFragment");	
			break;
			case R.id.planta:
                DefinicionPlantaDialoFragment definicionDialogFragment = new DefinicionPlantaDialoFragment();
                definicionDialogFragment.show(getSupportFragmentManager(), "Definicion");
			break;
            case R.id.planta_detalles:
                DetallePlantaDialogFragment detalleDefinicionDialogFragment = new DetallePlantaDialogFragment();
                detalleDefinicionDialogFragment.show(getSupportFragmentManager(), "Detalle");
            break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * 
	 * 
	 * DIALOGFRAGMENTS
	 * 
	 *
	 */
    public static class DetallePlantaDialogFragment extends DialogFragment{
        public DetallePlantaDialogFragment(){}

        View rootView;
        TextView txtNumeroRegistros;
        TextView txtPrimerPk;
        TextView txtUltimoPk;
        ListView lstRegistros;
        LinearLayout llCancelar;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.view_detalle_fichero_trazado, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            txtNumeroRegistros = (TextView) rootView.findViewById(R.id.textView7);
            txtPrimerPk = (TextView) rootView.findViewById(R.id.textView8);
            txtUltimoPk = (TextView) rootView.findViewById(R.id.textView9);
            lstRegistros = (ListView) rootView.findViewById(R.id.listView);
            llCancelar = (LinearLayout) rootView.findViewById(R.id.linear_layout_cancelar);

            return rootView;
        }


        @Override
        public void onActivityCreated(Bundle arg0) {
            super.onActivityCreated(arg0);

            if(puntosDefinicionPlanta.size()>0){
                txtNumeroRegistros.setText(puntosDefinicionPlanta.size() + "");
                txtPrimerPk.setText(getPk(puntosDefinicionPlanta.get(0)) + "");
                txtUltimoPk.setText(getPk(puntosDefinicionPlanta.get(puntosDefinicionPlanta.size()-1)) + "");

                ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.detalle_planta_row_text, puntosDefinicionPlanta);
                lstRegistros.setAdapter(adapter);
            }




            llCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        private String getPk(String fila){
            String valores[] = fila.split("\t");
            return valores[0];
        }
    }

    public static class DefinicionPlantaDialoFragment extends DialogFragment {
        public DefinicionPlantaDialoFragment() {
        }

        //----- Elements
        ListView lstFoldersFiles;
        Button btnAceptar;
        Button btnBack;
        TextView txtCurrentRoot;


        //----- Variables
        private ArrayList<String> paths = null; // In this variable the different paths are saved
        private String root = "/";
        private File navigatorFile;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(layout.view_definicion_planta_fragment, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            lstFoldersFiles = (ListView) view.findViewById(R.id.listView1);
            btnAceptar = (Button) view.findViewById(R.id.button2);
            btnBack = (Button) view.findViewById(R.id.button3);
            txtCurrentRoot = (TextView) view.findViewById(R.id.textView2);

            getDir(root);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0) {
            // TODO Auto-generated method stub
            super.onActivityCreated(arg0);

            lstFoldersFiles.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    navigatorFile = new File(paths.get(arg2));

                    if (navigatorFile.isDirectory()) {
                        if (navigatorFile.canRead()) {
                            getDir(paths.get(arg2));
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.accion_no_permitida), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String typeFile = MimeTypeMap.getFileExtensionFromUrl(navigatorFile.getAbsolutePath());

                        if (typeFile.contentEquals("txt")) {
                            new LeerPlantaTask(navigatorFile.getAbsolutePath()).execute();
                        }
                    }
                }
            });

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!navigatorFile.getAbsolutePath().toString().contentEquals(root)) {
                        navigatorFile = new File(navigatorFile.getParent());
                        getDir(navigatorFile.getPath());
                    }
                }
            });
        }

        private class LeerPlantaTask extends AsyncTask<Void, Void, String>{

            String pathSelectedFile;
            ProgressDialog dialog;
            File file;

            public LeerPlantaTask(String pathSelectedFile){
                this.pathSelectedFile = pathSelectedFile;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage(getString(R.string.leyendo_datos));
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                file = new File(pathSelectedFile);
                ArrayList<String> lines = new ArrayList<String>();

                try{
                    FileInputStream fIn = new FileInputStream(file);
                    InputStreamReader readerFile = new InputStreamReader(fIn);
                    BufferedReader br = new BufferedReader(readerFile);
                    String line = br.readLine();
                    lines.add(line);

                    while (line != null){
                        line  = br.readLine();
                        lines.add(line);
                    }

                    for(int i = 1; i < lines.size(); i++){
                        String values[] = lines.get(i).split("\t");

                        for(int j = 0; j < values.length; j++){
                            boolean valorCorrecto = false;

                            if(j == 0){
                                valorCorrecto = checkValues(values[j].replace("+", ""), i + 1, j + 1);
                            }else{
                                valorCorrecto = checkValues(values[j], i + 1, j + 1);
                            }

                            if(valorCorrecto){
                                if(j == values.length-1){
                                    puntosDefinicionPlanta.add(lines.get(i));
                                }
                            }else{
                                break;
                            }
                        }
                    }

                    br.close();
                    readerFile.close();
                }catch (Exception e){
                    e.getStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                txtFicheroSeleccionado.setText(getString(R.string.fichero_trazado) + ": " + file.getName());
            }

            private boolean checkValues(String value, int linea, int columna){
                try{
                    double vauleToDouble = Double.parseDouble(value);
                    return true;
                }catch(Exception e){
                    Toast.makeText(getActivity(),
                            getString(R.string.error_linea) + " " + linea + ", " + getString(R.string.columna) + " " + columna,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        private void getDir(String dirPath)
        {
            txtCurrentRoot.setText(getString(R.string.ruta_actual) + ": " + dirPath);

            paths = new ArrayList<String>();
            navigatorFile = new File(dirPath);
            File files[] = navigatorFile.listFiles();
            NavigatorModel[] items = new NavigatorModel[files.length];

            for(int i = 0; i < files.length; i++)
            {
                File file = files[i];
                paths.add(file.getPath());

                items[i] = new NavigatorModel(file.isDirectory(), file.getAbsolutePath());
            }

            NavigatorAdapter adapter = new NavigatorAdapter(getActivity(), items);
            lstFoldersFiles.setAdapter(adapter);
        }


        private class NavigatorAdapter extends ArrayAdapter<NavigatorModel>
        {
            Activity contextActivity;
            NavigatorModel[] fileList;

            public NavigatorAdapter(Activity context, NavigatorModel[] fileList)
            {
                super(context, R.layout.adapter_view_file_navigator, fileList);
                this.contextActivity = context;
                this.fileList = fileList;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater = contextActivity.getLayoutInflater();
                View item = inflater.inflate(R.layout.adapter_view_file_navigator, null);

                TextView lblTittle = (TextView) item.findViewById(R.id.textView1);
                TextView lblSubTitle = (TextView) item.findViewById(R.id.textView2);
                ImageView imageView = (ImageView)item.findViewById(R.id.imageView1);

                boolean isDirectory = fileList[position].isDirectory();
                String filePath = fileList[position].getName();
                File file = new File(filePath);

                String nameItem = file.getName();
                String typeItem = MimeTypeMap.getFileExtensionFromUrl(filePath);

                lblTittle.setText(nameItem.toUpperCase());
                if(isDirectory)
                {
                    imageView.setImageResource(R.drawable.ic_carpeta);
                    lblSubTitle.setText(getString(R.string.carpeta));
                }
                else
                {
                    if(typeItem.contentEquals("txt"))
                    {
                        imageView.setImageResource(R.drawable.ic_archivo);
                        lblSubTitle.setText(getString(R.string.archivo_texto));
                    }
                    else
                    {
                        imageView.setImageResource(R.drawable.ic_desconocido);
                        lblSubTitle.setText(getString(R.string.archivo_desconocido));
                    }
                }

                return item;
            }
        }
    }

	
	public static class EstablecerCotaDialogFragment extends DialogFragment{
		public EstablecerCotaDialogFragment(){}
		
		View cotaView;
		RadioGroup rgCota;
		Button btnCotaCancelar;
		Button btnCotaAceptar;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			cotaView = inflater.inflate(R.layout.view_dialog_cota_inicial, container);
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			rgCota = (RadioGroup) cotaView.findViewById(R.id.radioGroup1);
			btnCotaCancelar = (Button) cotaView.findViewById(R.id.button1);
			btnCotaAceptar = (Button) cotaView.findViewById(R.id.button2);
			
			return cotaView;
		}
		
	
		@Override
		public void onActivityCreated(Bundle arg0) {
			super.onActivityCreated(arg0);
			
			btnCotaAceptar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (rgCota.getCheckedRadioButtonId()) {
						case R.id.radio0:
							CotaInicialDialogFragment cotaDialogFragment =  new CotaInicialDialogFragment();
							cotaDialogFragment.show(getActivity().getSupportFragmentManager(), "CotaInicialDialogFragment");							
						break;
						
						case R.id.radio1:
                            FileNavigatorDialogFragment navegadorDialogFragment =  new FileNavigatorDialogFragment();
							navegadorDialogFragment.show(getActivity().getSupportFragmentManager(), "AbrirArchivo");
						break;
						
						case R.id.radio2:
							DataBaseDialogFragment baseDatosDialogFragment =  new DataBaseDialogFragment();
							baseDatosDialogFragment.show(getActivity().getSupportFragmentManager(), "BaseDAtos");
						break;
					}
					
					dismiss();
				}
			});
			
			
			
			btnCotaCancelar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}
	
	public static class CotaInicialDialogFragment extends DialogFragment{
		
		public CotaInicialDialogFragment(){}
		
		View dialogView;
		EditText edtCota;
		Button btnAceptar;
		TextView txtDesdeArchivo;
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			dialogView = inflater.inflate(R.layout.view_dialog_establecer_cota, container);
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			
			edtCota = (EditText) dialogView.findViewById(R.id.editText1);
			btnAceptar = (Button) dialogView.findViewById(R.id.button1);
			txtDesdeArchivo = (TextView) dialogView.findViewById(R.id.textView4);
			
			return dialogView;			
		}
		
		@Override
		public void onActivityCreated(Bundle arg0) {
			super.onActivityCreated(arg0);
			
			btnAceptar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String strCota = edtCota.getText().toString();
					
					if(!strCota.contentEquals("")){
						cotaInicial = Double.parseDouble(strCota);
						txtCotaInicial.setText(getString(R.string.cota_inicial) + ": " + strCota + "m");
						dismiss();
					}
				}
			});
		}
	}

	public static class DataBaseDialogFragment extends DialogFragment
    {
        public DataBaseDialogFragment(){}

        //----- ELEMENTS
        EditText edtPointData;
        ListView lstFoundedPoints;

        
        //----- VARIABLES
        //----- Table to read is defined
        Uri camposUri;
        //----- ContentResolver is initialized for access to data base
        ContentResolver cr;
        FoundPointsAdapter adapter;
        ArrayList<FoundedPointsModel> arrayFoundPoints= new ArrayList<FoundedPointsModel>();
        String hemisphere = "N";


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.view_dialog_data_base, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            edtPointData = (EditText) view.findViewById(R.id.editText1);
            lstFoundedPoints = (ListView) view.findViewById(R.id.listView1);
            
            arrayFoundPoints.clear();

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0)
        {
            super.onActivityCreated(arg0);
            
            try
            {
                camposUri = Uri.parse("content://com.victor.basededatos/topografia");
                cr = getActivity().getContentResolver();

                String[] campos = new String[] {"proyecto", "nombrePunto", "x", "y", "z"};
                Cursor c = cr.query(camposUri, campos, null, null, null);

                int i = 0;

                if(c.moveToFirst())
                {
                    do
                    {
                        String project = c.getString(0);
                        String pointName = c.getString(1);
                        String x = c.getString(2);
                        String y = c.getString(3);
                        String z = c.getString(4);

                        arrayFoundPoints.add(new FoundedPointsModel(project, pointName, x, y, z));
                        i = i + 1;
                    }
                    while(c.moveToNext());

                    adapter = new FoundPointsAdapter(getActivity(), arrayFoundPoints);
                    lstFoundedPoints.setAdapter(adapter);

                    edtPointData.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                            adapter.getFilter().filter(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

				lstFoundedPoints.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		                String cotaInicialSeleccionada = arrayFoundPoints.get(position).getZ();
		                
		                switch (mViewPager.getCurrentItem()) {
							case 0:
								cotaInicial = Double.parseDouble(cotaInicialSeleccionada);
								txtCotaInicial.setText(getString(R.string.cota_inicial) + ": " + cotaInicialSeleccionada + "m");	
							break;
							case 1:
								
							break;
							case 2:
								
							break;
						}
		                
		                dismiss();
					}
				});
            }
            catch (Exception e)
            {
                FoundedPointsModel nodbObject[] = new FoundedPointsModel[1];
                nodbObject[0] = new FoundedPointsModel(getString(R.string.atencion), getString(R.string.comprar), getString(R.string.no_base_datos), "", "");
                arrayFoundPoints.add(nodbObject[0]);
                adapter = new FoundPointsAdapter(getActivity(), arrayFoundPoints);
                lstFoundedPoints.setAdapter(adapter);

                edtPointData.setEnabled(false);

                lstFoundedPoints.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String ruta = "http://www.geosoftware.es";
                        i.setData(Uri.parse(ruta));
                        startActivity(i);
                        dismiss();
					}
				});
            }
        }
        
        private class FoundPointsAdapter extends ArrayAdapter<FoundedPointsModel>
        {
            Activity contextActivity;

            private ArrayList<FoundedPointsModel> originalFileList;
            private ArrayList<FoundedPointsModel> fileList;
            private DataPointsFilter filter;

            public FoundPointsAdapter(Activity context, ArrayList<FoundedPointsModel> fileList)
            {
                super(context, R.layout.adapter_founded_points_list, fileList);
                this.contextActivity = context;

                this.fileList = new ArrayList<FoundedPointsModel>();
                this.fileList.addAll(fileList);
                this.originalFileList = new ArrayList<FoundedPointsModel>();
                this.originalFileList.addAll(fileList);
            }

            @Override
            public Filter getFilter()
            {
                if(filter == null)
                {
                    filter = new DataPointsFilter();
                }

                return filter;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                ViewHolder holder = null;

                if(convertView == null)
                {
                    LayoutInflater inflater = contextActivity.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.adapter_founded_points_list, null);

                    holder = new ViewHolder();

                    holder.lblProject = (TextView) convertView.findViewById(R.id.textView1);
                    holder.lblPointName = (TextView) convertView.findViewById(R.id.textView2);
                    holder.lblX = (TextView) convertView.findViewById(R.id.textView3);
                    holder.lblY = (TextView) convertView.findViewById(R.id.textView4);
                    holder.lblZ = (TextView) convertView.findViewById(R.id.textView5);

                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }

                FoundedPointsModel point = fileList.get(position);
                holder.lblProject.setText(getString(R.string.proyecto) + ": " + point.getPointName());
                holder.lblPointName.setText(getString(R.string.punto) + ": " + point.getProject());
                holder.lblX.setText(getString(R.string.x) + ": " + point.getX() + " m");
                holder.lblY.setText(getString(R.string.y) + ": " + point.getY() + " m");
                holder.lblZ.setText(getString(R.string.z) + ": " + point.getZ() + " m");

                return convertView;
            }

            private class DataPointsFilter extends Filter
            {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence)
                {
                    charSequence = charSequence.toString().toLowerCase();
                    FilterResults result = new FilterResults();

                    if(charSequence != null && charSequence.toString().length() > 0)
                    {
                        ArrayList<FoundedPointsModel> filteredItems = new ArrayList<FoundedPointsModel>();

                        for(int i = 0, l = originalFileList.size(); i < l; i++)
                        {
                            FoundedPointsModel point = originalFileList.get(i);

                            if(point.getProject().toLowerCase().contains(charSequence) ||
                                    point.getPointName().toLowerCase().contains(charSequence))
                            {
                                filteredItems.add(point);
                            }
                        }

                        result.count = filteredItems.size();
                        result.values = filteredItems;
                    }
                    else
                    {
                        synchronized (this)
                        {
                            result.values = originalFileList;
                            result.count = originalFileList.size();
                        }
                    }

                    return result;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults)
                {
                    fileList = (ArrayList<FoundedPointsModel>) filterResults.values;
                    notifyDataSetChanged();
                    clear();
                    Log.i("publishResults","Starting to publish: " + fileList);
                    for(int i = 0, l = fileList.size(); i < l; i++) {
                        add(fileList.get(i));
                    }

                    notifyDataSetInvalidated();
                }
            }
        }

        private class ViewHolder
        {
            TextView lblPointName;
            TextView lblProject;
            TextView lblX;
            TextView lblY;
            TextView lblZ;
        }
    }
	
    public static class FileNavigatorDialogFragment extends DialogFragment
    {
        public FileNavigatorDialogFragment(){}

        //----- Elements
        ListView lstFoldersFiles;
        ListView lstFoundedPoints;
        Button btnCancel;
        Button btnBack;
        TextView txtCurrentRoot;


        //----- Variables
        private ArrayList<String> paths = null; // In this variable the different paths are saved 
        private String root="/";
        private File navigatorFile;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.view_navigator_fragment, container);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            lstFoldersFiles = (ListView) view.findViewById(R.id.listView1);
            lstFoundedPoints = (ListView) view.findViewById(R.id.listView2);
            btnCancel = (Button) view.findViewById(R.id.button2);
            btnBack = (Button) view.findViewById(R.id.button3);
            txtCurrentRoot = (TextView) view.findViewById(R.id.textView2);

            getDir(root);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle arg0)
        {
            // TODO Auto-generated method stub
            super.onActivityCreated(arg0);

            lstFoldersFiles.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                {
                    navigatorFile = new File(paths.get(arg2));

                    if(navigatorFile.isDirectory())
                    {
                        if(navigatorFile.canRead())
                        {
                            getDir(paths.get(arg2));
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getString(R.string.accion_no_permitida), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        String typeFile = MimeTypeMap.getFileExtensionFromUrl(navigatorFile.getAbsolutePath());

                        if(typeFile.contentEquals("txt"))
                        {
                            ArrayList<String> readData = readFile(navigatorFile.getAbsolutePath());
                            ArrayList<String> foundedPointsArray = new ArrayList<String>();
                            int documentLenght = 4;

                            for(int i = 0; i < readData.size()-1; i++)
                            {
                            	String lineElements[] = readData.get(i).split(" ");
                            	
                            	if(lineElements.length == documentLenght){
                        			try{
                    					Double.parseDouble(lineElements[1]);
                        				Double.parseDouble(lineElements[2]);
                        				Double.parseDouble(lineElements[3]);
                        				
                        				foundedPointsArray.add(lineElements[0] + " " + lineElements[1] + " " +
                        										lineElements[2] + " " + lineElements[3]);
                        			}catch(Exception e){
                        				Toast.makeText(getActivity(), getString(R.string.valor_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
                        			}                         			
                            		
                            		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_text, foundedPointsArray);
                            		lstFoundedPoints.setAdapter(adapter);
                            	} else{
                            		Toast.makeText(getActivity(), getString(R.string.formato_incorrecto_linea) + " " + (i+1), Toast.LENGTH_SHORT).show();
                            	}
                            }
                        }
                    }
                }
            });
            
            lstFoundedPoints.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String selectedItem = lstFoundedPoints.getItemAtPosition(position).toString();
	                String valuesSelectedItem[] = selectedItem.split(" ");
	                String cotaInicialSeleccionada = valuesSelectedItem[3];
	                
	                switch (mViewPager.getCurrentItem()) {
						case 0:
							cotaInicial = Double.parseDouble(cotaInicialSeleccionada);
							txtCotaInicial.setText(getString(R.string.cota_inicial) + ": " + cotaInicialSeleccionada + "m");	
						break;
						case 1:
							
						break;
						case 2:
							
						break;
					}
	                
	                dismiss();
				}
			});

            btnCancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!navigatorFile.getAbsolutePath().toString().contentEquals(root))
                    {
                        navigatorFile = new File(navigatorFile.getParent());
                        getDir(navigatorFile.getPath());
                    }
                }
            });
        }
        

        private void getDir(String dirPath)
        {
            txtCurrentRoot.setText(getString(R.string.ruta_actual) + ": " + dirPath);

            paths = new ArrayList<String>();
            navigatorFile = new File(dirPath);
            File files[] = navigatorFile.listFiles();
            NavigatorModel[] items = new NavigatorModel[files.length];

            for(int i = 0; i < files.length; i++)
            {
                File file = files[i];
                paths.add(file.getPath());

                items[i] = new NavigatorModel(file.isDirectory(), file.getAbsolutePath());
            }

            NavigatorAdapter adapter = new NavigatorAdapter(getActivity(), items);
            lstFoldersFiles.setAdapter(adapter);
        }

        private ArrayList<String> readFile (String nombre)
        {
            File file = new File(nombre);
            ArrayList<String> lines = new ArrayList<String>();

            try
            {
                FileInputStream fIn = new FileInputStream(file);
                InputStreamReader readerFile = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(readerFile);
                String line = br.readLine();
                lines.add(line);

                while (line != null)
                {
                    line  = br.readLine();
                    lines.add(line);
                }

                br.close();
                readerFile.close();
                return lines;
            }
            catch (Exception e)
            {
                e.getStackTrace();
                return null;
            }
        }


        private class NavigatorAdapter extends ArrayAdapter<NavigatorModel>
        {
            Activity contextActivity;
            NavigatorModel[] fileList;

            public NavigatorAdapter(Activity context, NavigatorModel[] fileList)
            {
                super(context, R.layout.adapter_view_file_navigator, fileList);
                this.contextActivity = context;
                this.fileList = fileList;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                LayoutInflater inflater = contextActivity.getLayoutInflater();
                View item = inflater.inflate(R.layout.adapter_view_file_navigator, null);

                TextView lblTittle = (TextView) item.findViewById(R.id.textView1);
                TextView lblSubTitle = (TextView) item.findViewById(R.id.textView2);
                ImageView imageView = (ImageView)item.findViewById(R.id.imageView1);

                boolean isDirectory = fileList[position].isDirectory();
                String filePath = fileList[position].getName();
                File file = new File(filePath);

                String nameItem = file.getName();
                String typeItem = MimeTypeMap.getFileExtensionFromUrl(filePath);

                lblTittle.setText(nameItem.toUpperCase());
                if(isDirectory)
                {
                    imageView.setImageResource(R.drawable.ic_carpeta);
                    lblSubTitle.setText(getString(R.string.carpeta));
                }
                else
                {
                    if(typeItem.contentEquals("txt"))
                    {
                        imageView.setImageResource(R.drawable.ic_archivo);
                        lblSubTitle.setText(getString(R.string.archivo_texto));
                    }
                    else
                    {
                        imageView.setImageResource(R.drawable.ic_desconocido);
                        lblSubTitle.setText(getString(R.string.archivo_desconocido));
                    }
                }

                return item;
            }
        }
    }
    
	
	/**
	 * 
	 * FRAGMENTS
	 * 
	 */
	
	public static class ArrastreCotaFragment extends Fragment{
		private static final String RECEIVED_DATA = "";
		
		public static ArrastreCotaFragment newInstance(String receivedData){
			ArrastreCotaFragment fragment = new ArrastreCotaFragment();
			Bundle args = new Bundle();
			args.putString(RECEIVED_DATA, receivedData);
			fragment.setArguments(args);
			return fragment;
		}
		
		public ArrastreCotaFragment(){}
		
		
		//----- PUBLIC FRAGMENTS ELEMENTS DECLARATION
		View view;
		
		TextView txtLecturas;
		EditText edtLectura;
		Button btnGuardar;
		ListView lstObservaciones;
		TextView txtCotaAcumulada;
		TextView txtPlanoComparacion;
		
		
		//----- GLOBAL FRAGMENT VARIABLES
		ObservacionesListAdapter adapter;
		DecimalFormat tresDecimales = new DecimalFormat("0.000");
		ArrayList<String> observacionesArray = new ArrayList<String>();
		double cotaArrastrada = 0;
		double cotaPlanoComparacion = 0;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.trazado_fragment_arrastre_cota, container, false);
			
			txtCotaInicial = (TextView) view.findViewById(R.id.textView6);
			txtLecturas = (TextView) view.findViewById(R.id.textView1);
			edtLectura= (EditText) view.findViewById(R.id.editText1);
			btnGuardar = (Button) view.findViewById(R.id.button3);
			lstObservaciones = (ListView) view.findViewById(R.id.listView1);
			txtCotaAcumulada = (TextView) view.findViewById(R.id.textView3);
			txtPlanoComparacion = (TextView) view.findViewById(R.id.textView5);
			
			return view;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			adapter = new ObservacionesListAdapter(getActivity(), observacionesArray);

			
			btnGuardar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String strLectura = edtLectura.getText().toString();

					if(!strLectura.contentEquals("")){
						double dblLecutra = Double.parseDouble(strLectura);
						int tramos = observacionesArray.size()/2 + 1;
						String datos = tramos + " " + tresDecimales.format(dblLecutra);
						
						observacionesArray.add(datos);
						lstObservaciones.setAdapter(adapter);
						calcularDatos();
						
						edtLectura.setText("");
						lstObservaciones.setSelection(lstObservaciones.getCount());
					}
				}
			});
			
			lstObservaciones.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					if(position == lstObservaciones.getCount() - 1){
						observacionesArray.remove(position);
						lstObservaciones.setAdapter(adapter);
						
						calcularDatos();
					}
					
					return false;
				}
			});
		}
		
		@Override
		public void setMenuVisibility(boolean menuVisible) {
			super.setMenuVisibility(menuVisible);
			
			if(view != null){
				if(menuVisible){
					Log.i("ArrastreFragment", "encendido");
					lstObservaciones.setAdapter(adapter);
					refrescarLiterales();
				} else{
					Log.i("ArrastreFragment", "apagado");
				}
			}
		}

		//----- Metodos y subrrutinas
		
		public void calcularDatos(){
			double espaldas = 0;
			double frentes = 0;
			double ultimoDato = 0;
			double penultimoDato = 0;
			
			for(int i = 0; i < observacionesArray.size(); i++){
				String valores[] = observacionesArray.get(i).split(" ");
				String cota = valores[1].replace(",", ".");
				
				if(i%2 == 0){
					espaldas = espaldas + Double.parseDouble(cota);
				} else{
					frentes = frentes + Double.parseDouble(cota);
				}
				
				if(i == observacionesArray.size()-2){
					penultimoDato = espaldas - frentes;
				}
			}
			
			ultimoDato = espaldas - frentes;
			
			if(observacionesArray.size()%2 == 0){
				cotaArrastrada = cotaInicial + ultimoDato;
				cotaPlanoComparacion = cotaInicial + penultimoDato;
			} else{
				cotaArrastrada = cotaInicial + penultimoDato;
				cotaPlanoComparacion = cotaInicial + ultimoDato;
			}

			refrescarLiterales();
		}
		
		public void refrescarLiterales(){
			if(lstObservaciones.getCount()%2 == 0){
				txtLecturas.setText(getString(R.string.lectura_espalda));
			} else{
				txtLecturas.setText(getString(R.string.lectura_frente));
			}

			txtCotaInicial.setText(getString(R.string.cota_inicial) + ": " + tresDecimales.format(cotaInicial));
			txtCotaAcumulada.setText(getString(R.string.cota_acumulada_ultimo_tramo) + ": " + tresDecimales.format(cotaArrastrada) + "m");
			txtPlanoComparacion.setText(getString(R.string.plano_comparacion) + ": " + tresDecimales.format(cotaPlanoComparacion) + "m");
		}
		
		private class ObservacionesListAdapter extends ArrayAdapter<String>{
			Activity contextActivity;
			ArrayList<String> itemArrayList;
			
			public ObservacionesListAdapter(Activity contextActivity, ArrayList<String> itemArrayList) {
				super(contextActivity, R.layout.row_adapter_observaciones_espalda, itemArrayList);
				
				this.contextActivity = contextActivity;
				this.itemArrayList = itemArrayList;
			}
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = contextActivity.getLayoutInflater();
				View item;
				
				if(position%2 == 0){
					item = inflater.inflate(R.layout.row_adapter_observaciones_espalda, null);
					TextView txtLectura = (TextView) item.findViewById(R.id.textView1);
					TextView txtTramo = (TextView) item.findViewById(R.id.textView3);
					
					String datos[] = itemArrayList.get(position).split(" ");
					txtLectura.setText(datos[1] + "m");
					txtTramo.setText(getString(R.string.tramo) + " " + datos[0]); //pasar desde código
				} else{
					item = inflater.inflate(R.layout.row_adapter_observaciones_frente, null);
					TextView txtLectura = (TextView) item.findViewById(R.id.textView1);
					
					String datos[] = itemArrayList.get(position).split(" ");
					txtLectura.setText(datos[1] + "m");
				}

				return item;
			}
		}
		
	}
	
	
	/**
	 * 
	 * REPLANTEO DE COTA - FRAGMENT
	 * 
	 */
	
	public static class ReplanteoCotaFragment extends Fragment{
		private static final String RECEIVED_DATA = "";
		
		public static ReplanteoCotaFragment newInstance(String receivedData){
			ReplanteoCotaFragment fragment = new ReplanteoCotaFragment();
			Bundle args = new Bundle();
			args.putString(RECEIVED_DATA, receivedData);
			fragment.setArguments(args);
			return fragment;
		}
		
		public ReplanteoCotaFragment(){}
		
		
		//----- PUBLIC FRAGMENTS ELEMENTS DECLARATION
		View view;
        EditText edtCotaPk;
        EditText edtEspesorCapa;
        EditText edtDistanciaEje;
        EditText edtLecturaPunto;

        RadioGroup rgPeraltes;

        Button btnCalcularCota;
        Button btnLimpiarTodo;
        Button btnCalcularCotaTeorica;
        Button btnCalcularReplanteo;

        TextView txtCotaPkCalculada;
        TextView txtPlanoComparacion;
        TextView txtCotaTeoricaPunto;
        TextView txtReplanteoCalculado;


        @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.trazado_fragment_replanteo, container, false);

            edtCotaPk = (EditText) view.findViewById(R.id.editText1);
            edtEspesorCapa = (EditText) view.findViewById(R.id.editText2);
            edtDistanciaEje = (EditText) view.findViewById(R.id.editText3);
            edtLecturaPunto = (EditText) view.findViewById(R.id.editText4);

            rgPeraltes = (RadioGroup) view.findViewById(R.id.radioGroup1);

            btnCalcularCota = (Button) view.findViewById(R.id.button1);
            btnLimpiarTodo = (Button) view.findViewById(R.id.button2);
            btnCalcularCotaTeorica = (Button) view.findViewById(R.id.button3);
            btnCalcularReplanteo = (Button) view.findViewById(R.id.button4);

            txtFicheroSeleccionado = (TextView) view.findViewById(R.id.textView2);
            txtCotaPkCalculada = (TextView) view.findViewById(R.id.textView5);
            txtPlanoComparacion = (TextView) view.findViewById(R.id.textView11);
            txtCotaTeoricaPunto = (TextView) view.findViewById(R.id.textView14);
            txtReplanteoCalculado = (TextView) view.findViewById(R.id.textView15);
			
			return view;
		}

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }
	
	
	/**
	 * 
	 * VUELTA AL PUNTO INICIAL - FRAGMENT
	 * 
	 */
	
	public static class PuntoInicialFragment extends Fragment{
		private static final String RECEIVED_DATA = "";
		
		public static PuntoInicialFragment newInstance(String receivedData){
			PuntoInicialFragment fragment = new PuntoInicialFragment();
			Bundle args = new Bundle();
			args.putString(RECEIVED_DATA, receivedData);
			fragment.setArguments(args);
			return fragment;
		}
		
		public PuntoInicialFragment(){}
		
		
		//----- PUBLIC FRAGMENTS ELEMENTS DECLARATION
		View view;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.trazado_fragment_regreso, container, false);
			
			
			return view;
		}
	}
}
