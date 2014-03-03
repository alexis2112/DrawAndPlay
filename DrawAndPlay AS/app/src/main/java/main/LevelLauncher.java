package main;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.drawandplay.R;

import DAPGameItems.DAPSol;
import DAPGameItems.Effaceur;
import DAPGameItems.Escargot;
import DAPGameItems.Fauteuil;
import DAPGameItems.GameItem;
import DAPGameItems.GrosseGoutte;
import DAPGameItems.Ink;
import DAPGameItems.MurVertical;
import DAPGameItems.Pics;
import DAPGameItems.Pieuvre;
import DAPGameItems.PlateformeFixe;
import DAPGameItems.PlateformeMobileHorizontale;
import DAPGameItems.PlateformeMobileVerticale;
import DAPGameItems.Sortie;

import drawingEnvironment.DAPGLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;


import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import DAPGameItems.ClapetBas;
import DAPGameItems.ClapetHaut;

public class LevelLauncher extends Activity {
	
	private DAPGLSurfaceView mGLV;
	private ArrayList<GameItem> level = new ArrayList<GameItem>(0);
    private Ink ink;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getBooleanExtra("EXIT", false)){
			finish();
		
			
		}
	
		
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(
	                WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String levelDatas ="PlateformeFixe;0.0;0.0;0.0;2.0/PlateformeFixe;3.0;0.0;0.0;2.5/PlateformeFixe;10.0;0.0;0.0;3.0/PlateformeFixe;-1.5;3.0;0.0;3.0/" +
                "PlateformeFixe;3.0;3.0;0.0;8.0/PlateformeFixe;3.5;5.5;0.0;2.5/PlateformeFixe;10.0;5.0;0.0;3.0/PlateformeFixe;-1.0;8.0;0.0;3.5/" +
                "PlateformeFixe;2.0;7.0;0.0;7.0/PlateformeFixe;11.5;7.0;0.0;2.5/PlateformeFixe;5.0;11.0;0.0;8.0/PlateformeFixe;5.8;9.0;0.0;2.2/" +
                "PlateformeFixe;8.8;9.0;0.0;1.0/PlateformeFixe;10.0;8.0;0.0;1.0/PlateformeFixe;11.0;9.0;0.0;2.0/"+
                "MurVertical;4.7;9.0;2.2;0.0/MurVertical;13.0;9.0;2.0;0.0/MurVertical;9.7;8.0;1.0;0.0/MurVertical;11.0;8.0;1.0;0.0/" +
                "MurVertical;10.7;0.8;2.0;0.0/MurVertical;12.5;0.0;5.0;0.0/"+
                "Pics;-1.0;-2.0;0.0;16.0/Pics;1.0;6.0;0.0;10.0/Pics;-1.0;2.5;0.0;6.0/"+
                "ClapetBas;5.0;8.5;0.0;0.0/"+"ClapetHaut;8.0;8.5;0.0;0.0/"+
                "GrosseGoutte;6.0;7.5;0.0;0.0/"+
                "Escargot;8.0;13.0;0.0;0.0/Escargot;9.0;12.5;0.0;0.0/Escargot;5.0;2.5;0.0;0.0/"+
                "Effaceur;7.0;13.3;0.0;0.0/"+
                "Pieuvre;-2.0;1.0;0.0;0.0;0.0;3.0/"+
                "PlateformeMobileVerticale;-1.0;8.3;0.0;1.0;7.0;12.0/PlateformeMobileVerticale;5.0;11.0;0.0;2.0;11.0;13.0/PlateformeMobileHorizontale;0.0;11.0;0.0;1.0;0.0;3.0/"+
                "Sortie;11.0;11.3;0.0;0.0/"+
                "Fauteuil;12.0;0.5;0.0;0.0/Fauteuil;10.1;8.5;0.0;0.0/Ink;0.0;2.0;1.0;0.0/";
        String description="description";
	        this.writeLevelFile("firstLevelFile",levelDatas);
        try {
            this.convertFileFromPxToGame("firstLevelFile","secondLevelFile",description);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.readLevelFile("secondLevelFile");
	        

		    mGLV = new DAPGLSurfaceView(this,level,ink);
		    this.setContentView(mGLV);
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onStop(){
		super.onStop();
		mGLV.onStop();
		
	}
	
	//methode qui écrit un ficher texte
	public void writeLevelFile(String fileName,String levelDatas,String description ){
		
		String datas = levelDatas + "/"+description+"//////";
		FileOutputStream fos;
		try {
			
			fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			
			fos.write(datas.getBytes());
		
			fos.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
    public void writeLevelFile(String fileName,String levelDatas ){


        FileOutputStream fos;
        try {

            fos = openFileOutput(fileName, Context.MODE_PRIVATE);

            fos.write(levelDatas.getBytes());

            fos.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }


    }
    //methode qui lit un fichier text et cree un niveau à partir de ça
	public void readLevelFile(String fileName){
		
			FileInputStream fis;
			String sep =";";
			String end="/";
			String data="";
			int i;
			
			
			
			try {
				
				fis = openFileInput(fileName);
				
				i=fis.read();
				while(i!=-1){
					
				
				
					while((char)i!=sep.charAt(0)){
						data=data+(char)i;
					   i=fis.read();}
				
				
						addItemToLevel(data,fis);
						data="";
						i=fis.read();

                    if((char)i==end.charAt(0)){

                        readEndFile(fis);

                        i=fis.read();
                    }}



				fis.close();
				
				
				
				
				
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		
			
			
		
				
			
		}
	//methode qui lit une suite de données correspondant à un item et l'ajoute au niveau
	public void addItemToLevel(String itemName,FileInputStream fis){
		String data;
		int i;
		String sep =";";
		String end ="/";
		float x0;
		float y0;
		float width;
		float length;
		float xmin = 0;
		float xmax=0;
		try {
		
		
			
				i=fis.read();
				
			
			data="";
			while((char)i!=sep.charAt(0)){
				data=data+(char)i;
				i=fis.read();
				
			}
			
			x0=Float.valueOf(data);
			
			
			i=fis.read();
			data="";
			while((char)i!=sep.charAt(0)){
				data=data+(char)i;
				i=fis.read();
				
			}
			y0=Float.valueOf(data);
			
			
			i=fis.read();
			data="";
			while((char)i!=sep.charAt(0)){
				data=data+(char)i;
				i=fis.read();
				
			}
			width=Float.valueOf(data);

            i=fis.read();
            data="";
			
			if(itemName.equals("PlateformeMobileHorizontale")||itemName.equals("PlateformeMobileVerticale")||itemName.equals("Pieuvre")){




				while((char)i!=sep.charAt(0)){
					data=data+(char)i;
					i=fis.read();}

				length=Float.valueOf(data);

				i=fis.read();
				data="";
				while((char)i!=sep.charAt(0)){
					data=data+(char)i;
					i=fis.read();}
				xmin=Float.valueOf(data);

				
				i=fis.read();
				data="";
				while((char)i!=end.charAt(0)){
					data=data+(char)i;
					i=fis.read();}
				xmax=Float.valueOf(data);

				
			}else{


				while((char)i!=end.charAt(0)){
					data=data+(char)i;
					i=fis.read();}
				length=Float.valueOf(data);
			}
			
			
			
			
			if(itemName.equals("PlateformeFixe")){
			    PlateformeFixe plateforme = new PlateformeFixe(x0,y0,length,this);
			    level.add(plateforme);}
			
			if(itemName.equals("ClapetBas")){
			    ClapetBas clapetBas = new ClapetBas(x0,y0,this);
			    level.add(clapetBas);}
			
			if(itemName.equals("ClapetHaut")){
				 ClapetHaut clapetHaut = new ClapetHaut(x0,y0,this);
				 level.add(clapetHaut);}
			
			if(itemName.equals("DAPSol")){
				DAPSol sol = new DAPSol(x0,y0,width,length,this);
				level.add(sol);}
			
			if(itemName.equals("Effaceur")){
				Effaceur effaceur = new Effaceur(x0,y0,this);
				level.add(effaceur);}
			
			if(itemName.equals("Fauteuil")){
				Fauteuil fauteuil = new Fauteuil(x0,y0,this);
			
				level.add(fauteuil);
			
				}
			
			if(itemName.equals("MurVertical")){
				MurVertical mv = new MurVertical(x0,y0,width,this);
				level.add(mv);}
			
			if(itemName.equals("Pics")){
				Pics pics = new Pics(x0,y0,length,this);
				level.add(pics);}
			
			if(itemName.equals("Sortie")){
				Sortie sortie = new Sortie(x0,y0,this);
				level.add(sortie);}
			
			if(itemName.equals("Escargot")){
				Escargot escargot = new Escargot(x0,y0,this);
				level.add(escargot);}
			
			if(itemName.equals("GrosseGoutte")){
				GrosseGoutte gg = new GrosseGoutte(x0,y0,this);
				level.add(gg);}
			
			if(itemName.equals("Pieuvre")){
				Pieuvre pieuvre = new Pieuvre(x0,y0,xmin,xmax,this);
				level.add(pieuvre);}
			
			if(itemName.equals("PlateformeMobileHorizontale")){
				
				PlateformeMobileHorizontale pmh = new PlateformeMobileHorizontale(x0,y0,length,xmin,xmax,this);
				level.add(pmh);
				}
			
			if(itemName.equals("PlateformeMobileVerticale")){
				PlateformeMobileVerticale pmv = new PlateformeMobileVerticale(x0,y0,length,xmax,xmin,this);
				level.add(pmv);}
		
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
    //methode qui lit la fin du fichier, description, note, record
	public void readEndFile(FileInputStream fis) throws IOException {
        String data="";
        String description;
        String record;
        String note;
        int i=fis.read();
        String end ="/";

        //description
        if((char)i==end.charAt(0)){description="";}
        else{
            while((char)i!=end.charAt(0)){
            data=data+(char)i;
                i=fis.read();

        }description=data;
        }

        data="";
        fis.read();
        i=fis.read();

        if((char)i==end.charAt(0)){record="";}
        else{
            while((char)i!=end.charAt(0)){
                data=data+(char)i;
                i=fis.read();

            }record=data;
        }


        data="";
        fis.read();
        i=fis.read();

        if((char)i==end.charAt(0)){note="";}
        else{
            while((char)i!=end.charAt(0)){
                data=data+(char)i;
                i=fis.read();

            }note=data;
        }
        data="";
        fis.read();


    }

    //on corvertit les fichiers de données de pixels à l'échelle
    public void convertFileFromPxToGame(String fileName1,String fileName2,String description) throws IOException {
        int i;
        String sep=";";
        String end="/";
        String data="";
        String itemName="";

        FileInputStream fis = openFileInput(fileName1);

        FileOutputStream fos = openFileOutput(fileName2, Context.MODE_PRIVATE);

        float scale=this.detectScale(fileName1);
        Log.i("debug","scale:"+scale);
        i=fis.read();
        while(i!=-1){
            //on lit le nom
            while((char)i!=sep.charAt(0)){
                itemName=itemName+(char)i;
                i=fis.read();
            }

            if(!itemName.equals("Ink")){
            fos.write(itemName.getBytes());
            fos.write(sep.getBytes());}

            i=fis.read();
           //lescture de x0 et mise à l'échelle
           while((char)i!=sep.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }

            float x0Px=Float.valueOf(data);
            float x0=x0Px/scale;
            if(!itemName.equals("Ink")){
            String x0Str =Float.toString(x0);
            fos.write(x0Str.getBytes());
            fos.write(sep.getBytes());}
            data="";
            i=fis.read();

            //lescture de y0 et mise à l'échelle
            while((char)i!=sep.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }
            float y0Px=Float.valueOf(data);
            float y0=y0Px/scale;
            if(!itemName.equals("Ink")){
           String y0Str =Float.toString(y0);
           fos.write(y0Str.getBytes());
           fos.write(sep.getBytes());}
            data="";
            i=fis.read();

            //lescture de width et mise à l'échelle
            while((char)i!=sep.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }
            if(!itemName.equals("Ink")){
            float widthPx=Float.valueOf(data);
            float width=widthPx/scale;
            String widthStr =Float.toString(width);
                if(itemName.equals("PlateformeFixe")){
                   width=0.3f/scale;
                     widthStr =Float.toString(width);
                    fos.write(widthStr.getBytes());
                }else{
            fos.write(widthStr.getBytes());}

            fos.write(sep.getBytes());}
            data="";
            i=fis.read();
if(itemName.equals("PlateformeMobileHorizontale")||itemName.equals("PlateformeMobileVerticale")||itemName.equals("Pieuvre")){
    Log.i("debug",itemName);
    Log.i("debug",""+(char)i);
    //on lit length et mise à l'échelle
    while((char)i!=sep.charAt(0)){
        data=data+(char)i;
        i=fis.read();
    }
    Log.i("debug","length"+data);
        float lengthPx=Float.valueOf(data);
        float length=lengthPx/scale;
        String lengthStr =Float.toString(length);
        fos.write(lengthStr.getBytes());
        fos.write(sep.getBytes());
        data="";
        i=fis.read();
//on lit xmin et mise à l'échelle
    while((char)i!=sep.charAt(0)){
        data=data+(char)i;
        i=fis.read();
    }
    Log.i("debug","xminPx"+data);
    float xminPx=Float.valueOf(data);
    float xmin=xminPx/scale;
    String xminStr =Float.toString(xmin);
    fos.write(xminStr.getBytes());
    fos.write(sep.getBytes());
    data="";
    i=fis.read();

    //on lit xmax et mise à l'échelle
    while((char)i!=end.charAt(0)){
        data=data+(char)i;
        i=fis.read();
    }
    Log.i("debug","xmaxPx"+data);
    float xmaxPx=Float.valueOf(data);
    float xmax=xmaxPx/scale;
    String xmaxStr =Float.toString(xmax);
    fos.write(xmaxStr.getBytes());
    fos.write(end.getBytes());






}else{
            //lescture de length et mise à l'échelle
    Log.i("debug",itemName);
            while((char)i!=end.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }
            if(!itemName.equals("Ink")){
            float lengthPx=Float.valueOf(data);
            float length=lengthPx/scale;
            String lengthStr =Float.toString(length);
            fos.write(lengthStr.getBytes());

            fos.write(end.getBytes());}}

            if(itemName.equals("Ink")){
                this.ink=new Ink(x0,y0,this);}

            itemName="";
            data="";
            i=fis.read();
            }
        data="/"+description+"//////";
        fos.write(data.getBytes());
        fis.close();
        fos.close();
        this.deleteFile(fileName1);




    }
//on détecte l'échelle
    public float detectScale(String fileName) throws IOException {

        FileInputStream fis = openFileInput(fileName);

        int i;
        String sep=";";
        String end="/";
        String data="";
        String itemName="";
        float width;
        i=fis.read();

        while(i!=-1){
            while((char)i!=sep.charAt(0)){
                itemName=itemName+(char)i;
                i=fis.read();
                }


            i=fis.read();

            //on lit x0
            while((char)i!=sep.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }

            data="";
            i=fis.read();

//on lit y0
            while((char)i!=sep.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }
            i=fis.read();

            data="";
//on lit width et on enregistre la valeur
            while((char)i!=sep.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }

            i=fis.read();
            width=Float.valueOf(data);

            data="";
//on lit length
            while((char)i!=end.charAt(0)){
                data=data+(char)i;
                i=fis.read();
            }

            data="";
            i=fis.read();
            if(itemName.equals("Ink")){
                Log.i("debug","i'm ink");
                fis.close();
                return(width);
            }
            itemName="";
        }

        fis.close();
        return 1.0f;

    }

	

}
