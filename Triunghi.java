
import java.awt.*;
import java.applet.Applet;
import java.util.HashSet;
import java.util.Set;

public class Triunghi extends Applet{
	Point[] puncte;//cele trei puncte
	Label[] etichetePuncte;// etichetele A,B,C
	Point centruCerc;//I, intersectia mediatoarelor
	Point punctSchimbareCerc;
	Image im;
	Graphics img;
	double k=.025;
	int moveflag=4;
	int numpoints;//numarul de puncte de control deplasat
	Button restart;
	Button mediane;
	Button inaltimi;
	Button bisectoare;
	int raza;
	boolean seSchimbaCercul;
	int stare;//poate lua valorile 0,1,2,3 (corespunzatoare fiecarui buton)

	Set<Point> locuriGeometrice;
	//lungimile laturilor triunghiului
	double latura01,latura12,latura02,s,p;

	Label etichetaCentru;

	public void init(){
		numpoints = 0;
		raza = 0;
		seSchimbaCercul = false;
		puncte=new Point[3];
		etichetaCentru=new Label("");
		etichetaCentru.setVisible(false);
		add(etichetaCentru);
		locuriGeometrice=new HashSet<Point>();

		etichetePuncte=new Label[3];
		etichetePuncte[0]=new Label("A");
		etichetePuncte[1]=new Label("B");
		etichetePuncte[2]=new Label("C");

		add(etichetePuncte[0]);
		add(etichetePuncte[1]);
		add(etichetePuncte[2]);
		 
		etichetePuncte[0].setVisible(false);
		etichetePuncte[1].setVisible(false);
		etichetePuncte[2].setVisible(false);

		im=createImage(size().width,size().height);
		img=im.getGraphics();
		restart=new Button("Restart");
		add(restart);
		mediane=new Button("Mediane");
		add(mediane);
		inaltimi=new Button("Inaltimi");
		add(inaltimi);
		bisectoare=new Button("Bisectoare");
		add(bisectoare);

		mediane.setEnabled(false);
		inaltimi.setEnabled(false);
		bisectoare.setEnabled(false);
	}

	public void update(Graphics g){paint(g);}

	public void paint(Graphics g){
		setBackground(Color.white);
		img.setColor(Color.black);
		img.clearRect(0,0,size().width,size().height);
		//deseneaza pctele de control

		for(int i=0;i<numpoints;i++){
			img.setColor(Color.black);
			if(numpoints>1 && i<(numpoints-1))
				img.drawLine(puncte[i].x,puncte[i].y,puncte[i+1].x,puncte[i+1].y);
		}
		//deseneaza triunghiul
		if(numpoints==3) {
			
			mediane.setEnabled(true);
			inaltimi.setEnabled(true);
			bisectoare.setEnabled(true);	
			img.drawLine(puncte[2].x,puncte[2].y,puncte[0].x,puncte[0].y);
		 
	   
			//gasesc raza
			latura01=Math.sqrt(Math.pow(puncte[0].x-puncte[1].x,2)+Math.pow(puncte[0].y-puncte[1].y,2));
			latura12=Math.sqrt(Math.pow(puncte[1].x-puncte[2].x,2)+Math.pow(puncte[1].y-puncte[2].y,2));
			latura02=Math.sqrt(Math.pow(puncte[0].x-puncte[2].x,2)+Math.pow(puncte[0].y-puncte[2].y,2));
			p=(latura01+latura12+latura02)/2;
			s=Math.sqrt(p*(p-latura01)*(p-latura12)*(p-latura02));
	   
			if(raza==0){
				raza=(int) Math.round((latura01*latura12*latura02)/(4*s));
				//deseneaza cercul circumscris triunghiului
				centruCerc=centru();
			}
	   
			//centrul
			img.setColor(Color.black);
			img.drawOval(centruCerc.x-raza,centruCerc.y-raza,2*raza,2*raza);
	   
			img.setColor(Color.yellow);
			img.fillOval(centruCerc.x-3,centruCerc.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(centruCerc.x-3,centruCerc.y-3,6,6);
		
		}
		//daca am apasat butonul 1 (mediane), atunci:
		if(stare==1){
			img.setColor(Color.darkGray);
			img.drawLine(puncte[0].x,puncte[0].y,(puncte[1].x+puncte[2].x)/2,(puncte[1].y+puncte[2].y)/2);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[1].x,puncte[1].y,(puncte[0].x+puncte[2].x)/2,(puncte[0].y+puncte[2].y)/2);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[2].x,puncte[2].y,(puncte[0].x+puncte[1].x)/2,(puncte[0].y+puncte[1].y)/2);
			
			//pct1
			img.setColor(new Color(255,204,0));
			img.fillOval((puncte[1].x+puncte[2].x)/2-3,(puncte[1].y+puncte[2].y)/2-3,6,6);
			img.setColor(Color.black);
			img.drawOval((puncte[1].x+puncte[2].x)/2-3,(puncte[1].y+puncte[2].y)/2-3,6,6);
			//pct2
			img.setColor(new Color(255,204,0));
			img.fillOval((puncte[0].x+puncte[2].x)/2-3,(puncte[0].y+puncte[2].y)/2-3,6,6);
			img.setColor(Color.black);
			img.drawOval((puncte[0].x+puncte[2].x)/2-3,(puncte[0].y+puncte[2].y)/2-3,6,6);
			//pct3
			img.setColor(new Color(255,204,0));
			img.fillOval((puncte[1].x+puncte[0].x)/2-3,(puncte[1].y+puncte[0].y)/2-3,6,6);
			img.setColor(Color.black);
			img.drawOval((puncte[1].x+puncte[0].x)/2-3,(puncte[1].y+puncte[0].y)/2-3,6,6);
			
			//colorez locul geometric al centrului de greutate:
			for(Point element:locuriGeometrice){
				img.setColor(Color.green);
			img.fillOval(element.x-3,element.y-3,6,6);
			}
			
			//afisam Centrul de Greutate actual
			Point CentruDeGreutate=new Point();
			CentruDeGreutate.x=(int)Math.round((puncte[0].x+puncte[1].x+puncte[2].x)/3);
			CentruDeGreutate.y=(int)Math.round((puncte[0].y+puncte[1].y+puncte[2].y)/3);
			img.setColor(Color.red);
			img.fillOval(CentruDeGreutate.x-3,CentruDeGreutate.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(CentruDeGreutate.x-3,CentruDeGreutate.y-3,6,6);
			etichetaCentru.setBounds(CentruDeGreutate.x,CentruDeGreutate.y-13,10,10);
			
		}

		//daca am apasat butonnul 2 (inaltimi), atunci:
		if(stare==2){
			//gasim ortocentrul
			Point Ortocentru=new Point();
			Ortocentru.x=(int)Math.round(puncte[0].x+puncte[1].x+puncte[2].x-2*centruCerc.x);
			Ortocentru.y=(int)Math.round(puncte[0].y+puncte[1].y+puncte[2].y-2*centruCerc.y);
			//0,1,2=vfurile triunghiului
			double[] ecuatieDreapta01 = dreapta2Puncte(puncte[0],puncte[1]);
			double[] ecuatieDreapta12 = dreapta2Puncte(puncte[1],puncte[2]);
			double[] ecuatieDreapta02 = dreapta2Puncte(puncte[0],puncte[2]);
			double[] ecuatieDreapta0H = dreapta2Puncte(puncte[0],Ortocentru);
			double[] ecuatieDreapta1H = dreapta2Puncte(puncte[1],Ortocentru);
			double[] ecuatieDreapta2H = dreapta2Puncte(puncte[2],Ortocentru);
			
			Point PiciorulInaltimii0=new Point();
			Point PiciorulInaltimii1=new Point();
			Point PiciorulInaltimii2=new Point();
			
			PiciorulInaltimii0=intersectia(ecuatieDreapta0H,ecuatieDreapta12);
			PiciorulInaltimii1=intersectia(ecuatieDreapta1H,ecuatieDreapta02);
			PiciorulInaltimii2=intersectia(ecuatieDreapta2H,ecuatieDreapta01);
			//trasam inaltimile:
			img.setColor(Color.darkGray);
			img.drawLine(PiciorulInaltimii0.x,PiciorulInaltimii0.y,Ortocentru.x,Ortocentru.y);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[0].x,puncte[0].y,Ortocentru.x,Ortocentru.y);
			
			img.setColor(Color.darkGray);
			img.drawLine(PiciorulInaltimii1.x,PiciorulInaltimii1.y,Ortocentru.x,Ortocentru.y);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[1].x,puncte[1].y,Ortocentru.x,Ortocentru.y);
			
			img.setColor(Color.darkGray);
			img.drawLine(PiciorulInaltimii2.x,PiciorulInaltimii2.y,Ortocentru.x,Ortocentru.y);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[2].x,puncte[2].y,Ortocentru.x,Ortocentru.y);
			//afisam picioarele inaltimilor pe cele trei laturi ale triunghiului:
			//pct1
			img.setColor(new Color(255,204,0));
			img.fillOval(PiciorulInaltimii0.x-3,PiciorulInaltimii0.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(PiciorulInaltimii0.x-3,PiciorulInaltimii0.y-3,6,6);
			
			//pct2
			img.setColor(new Color(255,204,0));
			img.fillOval(PiciorulInaltimii1.x-3,PiciorulInaltimii1.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(PiciorulInaltimii1.x-3,PiciorulInaltimii1.y-3,6,6);
			
			//pct3
			img.setColor(new Color(255,204,0));
			img.fillOval(PiciorulInaltimii2.x-3,PiciorulInaltimii2.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(PiciorulInaltimii2.x-3,PiciorulInaltimii2.y-3,6,6);
			
			//colorez locul geometric al ortocentrului:
			for(Point element:locuriGeometrice){
				img.setColor(Color.green);
			img.fillOval(element.x-3,element.y-3,6,6);
			}
			
			//afisam Ortocentrul actual
			img.setColor(Color.red);
			img.fillOval(Ortocentru.x-3,Ortocentru.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(Ortocentru.x-3,Ortocentru.y-3,6,6);
			etichetaCentru.setBounds(Ortocentru.x,Ortocentru.y-13,10,10);
		}
		
		//daca am apasat butonul 3 (bisectoare), atunci:
		
		if(stare==3){
			Point I=new Point();
			I.x=(int)Math.round((latura01*puncte[2].x+latura12*puncte[0].x+latura02*puncte[1].x)/(p*2));
			I.y=(int)Math.round((latura01*puncte[2].y+latura12*puncte[0].y+latura02*puncte[1].y)/(p*2));
			
			double[] ecuatieDreapta01 = dreapta2Puncte(puncte[0],puncte[1]);
			double[] ecuatieDreapta12 = dreapta2Puncte(puncte[1],puncte[2]);
			double[] ecuatieDreapta02 = dreapta2Puncte(puncte[0],puncte[2]);
			double[] ecuatieDreapta0I = dreapta2Puncte(puncte[0],I);
			double[] ecuatieDreapta1I = dreapta2Puncte(puncte[1],I);
			double[] ecuatieDreapta2I = dreapta2Puncte(puncte[2],I);
			
			Point pct0=new Point();
			Point pct1=new Point();
			Point pct2=new Point();
			
			pct0=intersectia(ecuatieDreapta0I,ecuatieDreapta12);
			pct1=intersectia(ecuatieDreapta1I,ecuatieDreapta02);
			pct2=intersectia(ecuatieDreapta2I,ecuatieDreapta01);
			
			//trasam bisectoarele:
			img.setColor(Color.darkGray);
			img.drawLine(pct0.x,pct0.y,I.x,I.y);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[0].x,puncte[0].y,I.x,I.y);
			
			img.setColor(Color.darkGray);
			img.drawLine(pct1.x,pct1.y,I.x,I.y);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[1].x,puncte[1].y,I.x,I.y);
			
			img.setColor(Color.darkGray);
			img.drawLine(pct2.x,pct2.y,I.x,I.y);
			img.setColor(Color.darkGray);
			img.drawLine(puncte[2].x,puncte[2].y,I.x,I.y);
			
			//pct0
			img.setColor(new Color(255,204,0));
			img.fillOval(pct0.x-3,pct0.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(pct0.x-3,pct0.y-3,6,6);
			
			//pct1
			img.setColor(new Color(255,204,0));
			img.fillOval(pct1.x-3,pct1.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(pct1.x-3,pct1.y-3,6,6);
			
			//pct2
			img.setColor(new Color(255,204,0));
			img.fillOval(pct2.x-3,pct2.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(pct2.x-3,pct2.y-3,6,6);
			
			//colorez locul geometric al centrului cercului inscris:
			for(Point element:locuriGeometrice){
				img.setColor(Color.green);
			img.fillOval(element.x-3,element.y-3,6,6);
			}
			
			//afisam centrul cercului inscris
			img.setColor(Color.red);
			img.fillOval(I.x-3,I.y-3,6,6);
			img.setColor(Color.black);
			img.drawOval(I.x-3,I.y-3,6,6);
			etichetaCentru.setBounds(I.x,I.y-13,10,10);
			
		}
		
		for(int i=0;i<numpoints;i++){
			img.setColor(Color.yellow);
			img.fillOval(puncte[i].x-3,puncte[i].y-3,6,6);//aici pt probleme
			img.setColor(Color.black);
			img.drawOval(puncte[i].x-3,puncte[i].y-3,6,6);
			etichetePuncte[i].setBounds(puncte[i].x,puncte[i].y-13,10,10);
		}

		g.drawImage(im,0,0,this);
	}
//metode pt determinarea centrului cercului circumscris; ecuatia dreptei

	public double[] dreapta2Puncte(Point P,Point Q)//ecuatia dreptei data de doua puncte; returneaza coeficientii din ec dreptei
	{//c=ax+by
		double a,b,c;
		a =P.y-Q.y;
		b = Q.x-P.x;
		c = a*(Q.x)+ b*(Q.y);
		double[] result={a,b,c};
		return result;
	}

	public double[] perpendicularaMijlocul(int i,double[] coeficientiEcuatie){//mediatoarea corespunzatoare dreptei formate din punctele i, i+1 ale triunghiului
		int mijlocX, mijlocY;
		mijlocX =(int)Math.round((puncte[i].x+puncte[i+1].x)/2);
		mijlocY =(int)Math.round((puncte[i].y+puncte[i+1].y)/2);
		coeficientiEcuatie[2]=coeficientiEcuatie[1]*mijlocX - coeficientiEcuatie[0]*mijlocY;
		double aux;
		aux=coeficientiEcuatie[0];
		coeficientiEcuatie[0]=coeficientiEcuatie[1];
		coeficientiEcuatie[1]=-aux;
		return coeficientiEcuatie;
	}

	public Point intersectia(double[] coeficientiEcuatie1, double[] coeficientiEcuatie2){//intersectia a doua drepte
		int x, y;
		double determinant=coeficientiEcuatie1[0]*coeficientiEcuatie2[1]-coeficientiEcuatie2[0]*coeficientiEcuatie1[1];
		x=(int)Math.round((coeficientiEcuatie1[2]*coeficientiEcuatie2[1]-coeficientiEcuatie1[1]*coeficientiEcuatie2[2])/determinant);
		y=(int)Math.round((coeficientiEcuatie1[0]*coeficientiEcuatie2[2]-coeficientiEcuatie2[0]*coeficientiEcuatie1[2])/determinant);
		return new Point(x,y);
	}

	public Point centru(){//intersectia a doua mediatoare=>centrul cercului circumscris
		double[] ecuatieDreapta1 = dreapta2Puncte(puncte[0],puncte[1]);
		double[] ecuatieDreapta2 = dreapta2Puncte(puncte[1],puncte[2]);
	   
	   return intersectia(perpendicularaMijlocul(0,ecuatieDreapta1),perpendicularaMijlocul(1,ecuatieDreapta2));
	} 

//****************************************************


	public boolean action(Event e, Object o){//cand fac click pe fiecare buton
		
		//restart
		if(e.target==restart){
			raza=0;
			mediane.setEnabled(false);
			inaltimi.setEnabled(false);
			bisectoare.setEnabled(false);
			etichetePuncte[0].setVisible(false);
			etichetePuncte[1].setVisible(false);
			etichetePuncte[2].setVisible(false);
			etichetaCentru.setVisible(false);
			locuriGeometrice.clear();numpoints=0; stare=0;repaint();
			return true;
		}
			
		//mediane
		if(e.target==mediane){	
			locuriGeometrice.clear();
			etichetaCentru.setText("G");
			etichetaCentru.setVisible(true);
			stare=1;
			repaint();
			return true;
		}
		//inaltimi
		if(e.target==inaltimi){	
			locuriGeometrice.clear();
			etichetaCentru.setText("H");
			etichetaCentru.setVisible(true);
			stare=2;
			repaint();
			return true;
		}
		//bisectoare
		if(e.target==bisectoare){	
			locuriGeometrice.clear();
			etichetaCentru.setText("I");
			etichetaCentru.setVisible(true);
			stare=3;
			repaint();
			return true;
		}
		
		return false;
	}

	//******************************************************
	public boolean mouseDown(Event evt,int x,int y){//deseneaza un punct cand numarul de puncte e mai mic decat 3
		Point point=new Point(x,y);
		if(numpoints<3)
			{	puncte[numpoints]=point;
				etichetePuncte[numpoints].setVisible(true);
				numpoints++;
				repaint();
			}
		else {
			for(int i=0;i<numpoints;i++)
				for(int j=-2;j<3;j++)
					for(int l=-2;l<3;l++)
						if(point.equals(new Point(puncte[i].x+j,puncte[i].y+l))) 
							moveflag=i;//traseaza cu o line neagra intre cele 2 puncte
			
			if(moveflag==4) {
				double d;
				d=Math.sqrt(Math.pow(point.x-centruCerc.x,2)+Math.pow(point.y-centruCerc.y,2));
				if(Math.abs(raza-d)<=3){
					punctSchimbareCerc = point;//inseamna ca am apasat pe un punct de pe cerc diferit de A,B,C=> se modif cercul
					seSchimbaCercul = true;
				}
			}
		}
	return true;
	}
	public boolean mouseDrag(Event evt,int x,int y){
		if(moveflag<numpoints) {
			double razaDouble=Math.sqrt(Math.pow(centruCerc.x-puncte[moveflag].x,2)+Math.pow(centruCerc.y-puncte[moveflag].y,2));
			double d = Math.sqrt(Math.pow(x - centruCerc.x, 2) + Math.pow(y - centruCerc.y, 2));
			
			int noulX, noulY;
			

			noulX = (int)((raza*x+(d-raza)*centruCerc.x)/d);
			noulY = (int)((raza*y+(d-raza)*centruCerc.y)/d);
			
			puncte[moveflag].move(noulX,noulY);
			double distanta=Math.sqrt(Math.pow(noulX - centruCerc.x, 2) + Math.pow(noulY - centruCerc.y, 2));
			
				switch(stare) {
					case 1:
						Point CentruDeGreutate=new Point();
						CentruDeGreutate.x=(int)Math.round((puncte[0].x+puncte[1].x+puncte[2].x)/3);
						CentruDeGreutate.y=(int)Math.round((puncte[0].y+puncte[1].y+puncte[2].y)/3);
						locuriGeometrice.add(CentruDeGreutate);
						break;
					case 2:
						Point Ortocentru=new Point();
						Ortocentru.x=(int)Math.round(puncte[0].x+puncte[1].x+puncte[2].x-2*centruCerc.x);
						Ortocentru.y=(int)Math.round(puncte[0].y+puncte[1].y+puncte[2].y-2*centruCerc.y);
						locuriGeometrice.add(Ortocentru);
						break;
					case 3:
						Point I=new Point();
						I.x=(int)Math.round((latura01*puncte[2].x+latura12*puncte[0].x+latura02*puncte[1].x)/(p*2));
						I.y=(int)Math.round((latura01*puncte[2].y+latura12*puncte[0].y+latura02*puncte[1].y)/(p*2));
						locuriGeometrice.add(I);
						break;
					default:
						break; 
				}
				
			repaint();
		} else if(seSchimbaCercul) {
			double d = Math.sqrt(Math.pow(punctSchimbareCerc.x - x, 2) + Math.pow(punctSchimbareCerc.y - y, 2));
			double nouaRaza = Math.sqrt(Math.pow(x - centruCerc.x, 2) + Math.pow(y - centruCerc.y, 2));
			int noulX, noulY;
			double raport = nouaRaza / (double) raza;

			for(int i=0;i<numpoints;i++){
				noulX = (int)Math.round(centruCerc.x+raport*(puncte[i].x-centruCerc.x));
				noulY = (int)Math.round(centruCerc.y+raport*(puncte[i].y-centruCerc.y));
				puncte[i].move(noulX,noulY);
				
			}

			centruCerc=centru();
			raza=(int)Math.round(Math.sqrt(Math.pow(centruCerc.x-puncte[0].x,2)+Math.pow(centruCerc.y-puncte[0].y,2)));
			
				switch(stare) {
				case 1:
					Point CentruDeGreutate=new Point();
					CentruDeGreutate.x=(int)Math.round((puncte[0].x+puncte[1].x+puncte[2].x)/3);
					CentruDeGreutate.y=(int)Math.round((puncte[0].y+puncte[1].y+puncte[2].y)/3);
					locuriGeometrice.add(CentruDeGreutate);
					break;
				case 2:
					Point Ortocentru=new Point();
					Ortocentru.x=(int)Math.round(puncte[0].x+puncte[1].x+puncte[2].x-2*centruCerc.x);
					Ortocentru.y=(int)Math.round(puncte[0].y+puncte[1].y+puncte[2].y-2*centruCerc.y);
					locuriGeometrice.add(Ortocentru);
					break;
				case 3:
					Point I=new Point();
					I.x=(int)Math.round((latura01*puncte[2].x+latura12*puncte[0].x+latura02*puncte[1].x)/(p*2));
					I.y=(int)Math.round((latura01*puncte[2].y+latura12*puncte[0].y+latura02*puncte[1].y)/(p*2));
					locuriGeometrice.add(I);
					break;
				default:
					break;
					}
			
			repaint();
			
		}
			
	return true;
	}

	public boolean mouseUp(Event evt,int x,int y){
		if(numpoints==3)
		{
			centruCerc=centru();
			raza=(int)Math.round(Math.sqrt(Math.pow(centruCerc.x-puncte[0].x,2)+Math.pow(centruCerc.y-puncte[0].y,2)));
			repaint();
		}
		seSchimbaCercul = false;
		moveflag=3+1;return true;
	}
}


/*

	Moveflag	
Daca moveflag e intre 0 si 2
inseamna ca am apasat fie pe A, fie pe B, fie pe C
si, in cazul asta, ce se intampla e schimbarea triunghiului.

Si daca nu apas pe un punct din asta
(adica moveflag = 4)
si trag, ce se intampla e modificarea cercului
trag de el/il maresc/il micsorez

*/