package metier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Exemple extends JFrame
{
	JPanel panel ;
		
	private static double[][] utilitePlateau = new double[3][4];
	private static double[][] utiliteReel= new double[3][4];
	private static String[][] politiquePlateau = new String[3][4] ;
	private static int x = 0, y = 0 ; // position Agent
	
	private static String[] actions = {"h","b","d","g"};
	
	
	// récompense obtenue en partant de l’état s, et en effectuant l’action a || clé = "x""y""action" ==> valeur = recompense
	private static HashMap<String, Double> tableauRecompense = new HashMap<String, Double>();
	

	public Exemple() 
	{
		this.panel = new JPanel(new GridLayout(3, 4));
		panel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		
		for(int i = 0 ; i < 3 ; i++)
			for (int j = 0 ; j < 4 ; j++)
				utilitePlateau[i][j] = -0.04;
		
		for(int i = 0 ; i < 3 ; i++)
			for (int j = 0 ; j < 4 ; j++)
				politiquePlateau[i][j] = "h";
		
		
		utilitePlateau[0][3] = +999;
		utilitePlateau[1][3] = -1;
		utilitePlateau[1][1] = -999;
		
		
		for(int i = 0; i < utilitePlateau.length; i++)
			utiliteReel[i] = utilitePlateau[i].clone();
		
		
		print();
		
		setContentPane(panel);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void print()
	{
		this.panel = new JPanel(new GridLayout(3, 4));
		panel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		for(int i = 0 ; i < 3 ; i++)
		{
			for (int j = 0 ; j < 4 ; j++)
			{
				String s = "U : "+utilitePlateau[i][j]+/*" \nU agent : "+utiliteAgentPlateau[i][j]+*/"\nP : "+politiquePlateau[i][j] ;
			
				for(String key : tableauRecompense.keySet())
				{
					String[] k = key.split(" ");
				
					if ( Double.parseDouble(k[0]) == i && Double.parseDouble(k[1]) == j)
						s += "\nU de l'agent: "+k[2]+" = "+tableauRecompense.get(key);
				}
				
				if ( i == x && j == y )
					s+= "\n======> VOUS ETES ICI <======" ;
				
			
				
				JTextArea l = new JTextArea(s) ;
				
				l.setPreferredSize(new Dimension(200,200));
				
				l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				panel.add(l);
			}
		}
		setContentPane(panel);
		panel.repaint();
		revalidate();
		repaint();
		
	}
	
	public void deplace(String d)
	{
		
		// Probabilité de 0.3 de ne pas aller dans la bonne direction
		double p = Math.random();
		
		if ( p <= 0.3 )
		{
			int idaction = (int)( Math.random()*( 3 - 0 + 1 ) ); // chiffre entre 0 et 3
			d = actions[idaction];
			System.out.println(d);
		}
		
		if ( d.equals("b") )
		{
			if ( x < 2 && !(x+1==1&&y==1) )
				if ( (x+1==0 || x+1==1) && y==3 )
				{
					x=0 ; y=0 ;
				}
				else
				{
					x++ ;
				}
		}
		else if ( d.equals("h") )
		{
			if ( x > 0 && !(x-1==1&&y==1) )
				if ( (x-1==0 || x-1==1) && y==3 )
				{
					x=0 ; y=0 ;
				}
				else
				{
					x-- ;
				}
		}
		else if ( d.equals("g") )
		{
			if ( y > 0 && !(x==1&&y-1==1) )
				if ( (x==0 || x==1) && y-1==3 )
				{
					x=0 ; y=0 ;
				}
				else
				{
					y-- ;
				}
		}
		else if ( d.equals("d") )
		{
			if ( y < 3 && !(x==1&&y+1==1) )
				if ( (x==0 || x==1) && y+1==3 )
				{
					x=0 ; y=0 ;
				}
				else
				{
					y++ ;
				}
		}
	}
	

	public void apprentissage() 
	{
		double p = Math.random();
		String politique ;
		if ( p > 0.6 )
		{
			int idaction = (int)( Math.random()*( 3 - 0 + 1 ) ); // chiffre entre 0 et 3
			politique = actions[idaction];
			System.out.println("Exploration "+politique);
		}
		else
		{
			//politique = argmaxPolitique();
			politique = politiquePlateau[x][y];
			System.out.println("Continuation "+politique);
		}
		
		double recompense ;
		if ( directionInterdite(politique) )
		{
			recompense = -1 ;
		}
		else
			recompense = utilitePlateau[x][y] + getUtiliteDirection(politique) ;
		
		System.out.println("   Recompense = "+recompense);
		tableauRecompense.put(x+" "+y+" "+politique, recompense);
		System.out.println("   "+tableauRecompense);

		deplace(politique);

		majEtats();
	}

	private boolean directionInterdite(String d)
	{
		if ( d.equals("b") )
		{
			if ( x < 2 && !(x+1==1&&y==1) )
				return false ;
		}
		else if ( d.equals("h") )
		{
			if ( x > 0 && !(x-1==1&&y==1) )
				return false ;
		}
		else if ( d.equals("g") )
		{
			if ( y > 0 && !(x==1&&y-1==1) )
				return false ;
		}
		else if ( d.equals("d") )
		{
			if ( y < 3 && !(x==1&&y+1==1) )
				return false ;
		}
		
		return true ;
	}

	private void majEtats() 
	{
		ArrayList<String> actionsAMaj = new ArrayList<>();
		for(String key : tableauRecompense.keySet())
		{
			String[] s = key.split(" ");
		
			if ( Double.parseDouble(s[0]) == x && Double.parseDouble(s[1]) == y)
				actionsAMaj.add(key) ;
		
		}
		
		String politique = "";
		double recompense = -9999 ;
		for ( String key : actionsAMaj )
		{
			if ( recompense < tableauRecompense.get(key) )
			{
				recompense = tableauRecompense.get(key) ;
				politique = key.split(" ")[2];
			}
		}
		
		if (! "".equals(politique))
		{
			System.out.println("   Mise a jour etat "+x+","+y);
			politiquePlateau[x][y] = politique ;
			double gamma = 0.1 ;
			double alpha = 0.1 ;
			
			utilitePlateau[x][y] = utiliteReel[x][y]+(alpha*(recompense+(gamma*(getUtiliteDirection(politique)-utiliteReel[x][y]))));
			//utilitePlateau[x][y] = utiliteReel[x][y]+recompense;
		}
	}

	
	private String argmaxPolitique() 
	{
		// pour toutes les directions
		double utilite = -1000 ;
		String bestDir = "";
		for ( int i = 0 ; i < 4 ; i ++)
		{
			if ( utilite < getUtiliteDirection(actions[i]) )
			{
				utilite = getUtiliteDirection(actions[i]) ;
				bestDir = actions[i] ;
			}
		}
		
		return bestDir ;
	}

	private double getUtiliteDirection(String d) 
	{
		if ( d.equals("b"))
		{	
			if ( x < 2 && !(x+1==1&&y==1) )
				return utilitePlateau[x+1][y] ;
		}
		else if ( d.equals("h") )
		{
			if ( x > 0 && !(x-1==1&&y==1) )
				return utilitePlateau[x-1][y] ;
		}
		else if ( d.equals("g") )
		{
			if ( y > 0 && !(x==1&&y-1==1) )
				return utilitePlateau[x][y-1] ;
		}
		else if ( d.equals("d") )
		{
			if ( y < 3 && !(x==1&&y+1==1) )
				return utilitePlateau[x][y+1] ;
		}

		return 0;
	}

	
	public static void main(String[] args) throws InterruptedException
	{
		Exemple e = new Exemple();
		
		while ( true )
		{
			Thread.sleep(10);
			//e.deplace("d");
		
			e.apprentissage();
			e.print();
			e.repaint();
		}
	}
}
