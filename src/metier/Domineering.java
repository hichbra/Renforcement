package metier;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Domineering 
{    
    //----------- RENFORCEMENT 
    private static HashMap<String, Double> utilitePlateau = new HashMap<String, Double>();
	private static HashMap<String, String> politiquePlateau = new HashMap<String, String>();
	
	//																							   Politique
	//																							 /------------\
	// récompense obtenue en partant de l’état s, et en effectuant l’action a || clé = "plateau""ligne""colonne" ==> valeur = recompense
	private static HashMap<String, Double> tableauRecompense = new HashMap<String, Double>();
	
	
	private static double epsilon = 0.6 ;
	
	
    static int eval(String[] board, String player)
    {
    	int realCur = 0, realOpp = 0 ;
    	int safeCur = 0, safeOpp = 0 ;
    	
		ArrayList<String> moves = new ArrayList<String>() ;
		
		//-------- Calcul de RealCur / SafeCur
	    int ligne = 0;
		int colonne = 0;
		for (String s : board)
		{
			colonne = 0 ;
			for (char c : s.toCharArray()) 
			{
				moves.add(ligne+""+colonne);
				colonne++ ;
			}
			
			ligne++ ;
		}
		
		//System.out.println(moves);
		for ( int i = 0 ; i < moves.size() ; i++ )
		{
			ligne = Character.getNumericValue(moves.get(i).toCharArray()[0]);
			colonne = Character.getNumericValue(moves.get(i).toCharArray()[1]);

			if (player.equals("h")) 
	      	{
				if ( colonne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne].toCharArray()[colonne+1] == '-' )
				{
					realCur++ ;
					
					// si l'adversaire ne peut pas jouer ce move, il est sécurisé
					if ( ligne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne+1].toCharArray()[colonne] != '-')
						safeCur++ ;
							
					
					moves.remove(ligne+""+colonne);
					moves.remove(ligne+""+(colonne+1));
					i = -1 ;
				}
	      	}
			else if (player.equals("v"))
			{ 
				if ( ligne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne+1].toCharArray()[colonne] == '-')
				{
					realCur++ ;
					
					if ( colonne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne].toCharArray()[colonne+1] != '-' )
						safeCur++ ;

						
					moves.remove(ligne+""+colonne);
					moves.remove((ligne+1)+""+colonne);
					i = -1 ;
				}
			}
		}

		//-------- Calcul de RealOpp / SafeOpp

		ligne = 0;
		colonne = 0;
		for (String s : board)
		{
			colonne = 0 ;
			for (char c : s.toCharArray()) 
			{
				moves.add(ligne+""+colonne);
				colonne++ ;
			}
			
			ligne++ ;
		}
		
		for ( int i = 0 ; i < moves.size() ; i++ )
		{
			ligne = Character.getNumericValue(moves.get(i).toCharArray()[0]);
			colonne = Character.getNumericValue(moves.get(i).toCharArray()[1]);

			if (player.equals("h")) 
	      	{
				if ( ligne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne+1].toCharArray()[colonne] == '-')
				{
					realOpp++ ;
					
					if ( colonne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne].toCharArray()[colonne+1] != '-' )
						safeOpp++ ;
					
					moves.remove(ligne+""+colonne);
					moves.remove((ligne+1)+""+colonne);
					i = -1 ;
				}
	      	}
			else if (player.equals("v"))
			{ 
				if ( colonne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne].toCharArray()[colonne+1] == '-' )
				{
					realOpp++ ;
					
					if ( ligne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne+1].toCharArray()[colonne] != '-')
						safeOpp++ ;
					
					moves.remove(ligne+""+colonne);
					moves.remove(ligne+""+(colonne+1));
					i = -1 ;
				}
				
			}
		}


        int score=0;
		if (moveImpossible(board, player))
        {
            int moi=0, lui=0 ;
            for ( String s : board )
            {
               for (char c : s.toCharArray()) 
			   {
                   if ( c != '-' )
                   {
                       if ( player.equals("v") && c == 'v' )
                           moi++;
                       else
                           lui++;
                   }
               }
            }
            
            if ( moi > lui )
                return 99 ;
            else
                return -99;
        }
        else
	       score = (realOpp - realCur) + (safeOpp - safeCur) ;

        return -score ;
	}
     
    /*
    static int level(String[] board, String player)
    {
    	int realCur = 0, realOpp = 0 ;
    	int safeCur = 0, safeOpp = 0 ;
    	
		ArrayList<String> moves = new ArrayList<String>() ;
		
		//-------- Calcul de RealCur / SafeCur
	    int ligne = 0;
		int colonne = 0;
		for (String s : board)
		{
			colonne = 0 ;
			for (char c : s.toCharArray()) 
			{
				moves.add(ligne+""+colonne);
				colonne++ ;
			}
			
			ligne++ ;
		}
		
		for ( int i = 0 ; i < moves.size() ; i++ )
		{
			ligne = Character.getNumericValue(moves.get(i).toCharArray()[0]);
			colonne = Character.getNumericValue(moves.get(i).toCharArray()[1]);

			if (player.equals("h")) 
	      	{
				if ( colonne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne].toCharArray()[colonne+1] == '-' )
				{
					realCur++ ;
					
					// si l'adversaire ne peut pas jouer ce move, il est sécurisé
					if ( ligne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne+1].toCharArray()[colonne] != '-')
						safeCur++ ;
							
					
					moves.remove(ligne+""+colonne);
					moves.remove(ligne+""+(colonne+1));
					i = -1 ;
				}
	      	}
			else if (player.equals("v"))
			{ 
				if ( ligne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne+1].toCharArray()[colonne] == '-')
				{
					realCur++ ;
					
					if ( colonne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne].toCharArray()[colonne+1] != '-' )
						safeCur++ ;

						
					moves.remove(ligne+""+colonne);
					moves.remove((ligne+1)+""+colonne);
					i = -1 ;
				}
			}
		}

		//-------- Calcul de RealOpp / SafeOpp

		ligne = 0;
		colonne = 0;
		for (String s : board)
		{
			colonne = 0 ;
			for (char c : s.toCharArray()) 
			{
				moves.add(ligne+""+colonne);
				colonne++ ;
			}
			
			ligne++ ;
		}
		
		for ( int i = 0 ; i < moves.size() ; i++ )
		{
			ligne = Character.getNumericValue(moves.get(i).toCharArray()[0]);
			colonne = Character.getNumericValue(moves.get(i).toCharArray()[1]);

			if (player.equals("h")) 
	      	{
				if ( ligne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne+1].toCharArray()[colonne] == '-')
				{
					realOpp++ ;
					
					if ( colonne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne].toCharArray()[colonne+1] != '-' )
						safeOpp++ ;
					
					moves.remove(ligne+""+colonne);
					moves.remove((ligne+1)+""+colonne);
					i = -1 ;
				}
	      	}
			else if (player.equals("v"))
			{ 
				if ( colonne < 7 && board[ligne].toCharArray()[colonne] == '-' && board[ligne].toCharArray()[colonne+1] == '-' )
				{
					realOpp++ ;
					
					if ( ligne >= 7 || board[ligne].toCharArray()[colonne] != '-' || board[ligne+1].toCharArray()[colonne] != '-')
						safeOpp++ ;
					
					moves.remove(ligne+""+colonne);
					moves.remove(ligne+""+(colonne+1));
					i = -1 ;
				}
				
			}
		}

		
		int score = 0 ;
		// Level 1 
		if ( realCur > realOpp && safeCur >= safeOpp)
			score += 1 ;
		else if ( safeOpp >= realCur )
			score -= 1000 ;
		
		// Level 2
		if ( realCur > (7*realOpp) - (2*safeCur))
			score += 10 ;
		else if (realOpp >= (7*realCur) - (2*safeOpp))
			score -= 900 ;
		
		
			
		return score ;
    }*/
    
    // Le jeu est t-il fini
    static boolean moveImpossible(String[] board, String player)
    {
        int numLigne = 0;
		int numColonne = 0;
        
        for (String s : board)
		{
            numColonne = 0 ;
            for (char c : s.toCharArray()) 
			{
                if (player.equals("h") && numColonne < 7) 
                {
                    if ( c == '-' && board[numLigne].toCharArray()[numColonne + 1] == '-' )
                    {
                        return false ; 
                    }
                }
                else if (player.equals("v") && numLigne < 7)
                {
                    if ( c == '-' && board[numLigne + 1].toCharArray()[numColonne] == '-' )
                    {
                        return false ; 
                    }
                }
                
                numColonne++ ;
            }
            
            numLigne++ ;
        }
        
        return true;
    }    
        
    static String[] play(String[] board, String player, int numLigne, int numColonne)
    {
    	String[] newGame = new String[8] ;
         
    	for (int ligne = 0 ; ligne < 8 ; ligne++)
    	{
    		if ( ligne == numLigne )
            {
                if( player.equals("h") )
                {
             	   newGame[ligne] = board[ligne].substring(0,numColonne)+"hh"+board[ligne].substring(numColonne+2);
                }
                else
                {
                   newGame[ligne] = board[ligne].substring(0,numColonne)+"v"+board[ligne].substring(numColonne+1);
                   ligne++;
                   newGame[ligne] = board[ligne].substring(0,numColonne)+"v"+board[ligne].substring(numColonne+1);
                }

            }
            else
            {
                newGame[ligne] = board[ligne] ;
            }   
    	 }
         
         return newGame ;
    }
    
    static Object[] negamax(String[] board, String player, int depth, int alpha, int beta)
	{
		if (player.equals("v"))
            player = "h";
        else
            player = "v";
		
        Object[] retour = new Object[2] ;

		if ( depth == 0 )
		{
			retour[0] = eval(board, player);
			return retour ;
		}
		
		String bestMove = "" ;
		int numLigne, numColonne ;
		
		numLigne=0;
		for (String s : board)
		{
			numColonne = 0;
			for (char c : s.toCharArray())
			{
				if (c == '-') 
				{
					// si je suis joueur h et que je ne suis pas a la derniere colonne
					if (player.equals("h") && numColonne < 7) 
					{
						// si la case à droite est vide
						if (board[numLigne].toCharArray()[numColonne + 1] == '-')
						{
							String[] newGame = play(board, player, numLigne, numColonne);
							
							int e = -(int)negamax(newGame, player, depth-1, -beta, -alpha)[0] ;

							if ( e > alpha )
							{
								alpha = e ;
								bestMove = numLigne+""+numColonne;
								
								if ( alpha >= beta )
								{
									retour[0] = beta;
									retour[1] = bestMove;
									return retour ;
								}
							}
						}
					}
					// si je suis joueur v et que je ne suis pas sur la derniere ligne
					else if (player.equals("v") && numLigne < 7) 
					{
						// si la case en bas est vide
						if (board[numLigne + 1].toCharArray()[numColonne] == '-') 
						{
							String[] newGame = play(board, player, numLigne, numColonne);
							
							int e = -(int)negamax(newGame, player, depth-1, -beta, -alpha)[0] ;
							
							if ( e > alpha )
							{
								alpha = e ;
								bestMove = numLigne+""+numColonne;
								
								if ( alpha >= beta )
								{
									retour[0] = beta;
									retour[1] = bestMove;
									return retour ;
									
								}
							}
						}
					}
				}
				
				numColonne++;
			}
			numLigne++;
		}
		

		retour[0] = alpha;
		retour[1] = bestMove;
		return retour ;
	}
    
    
    
	public static void main(String[] args) throws IOException
    {
		File f = new File("C:/Users/Hichbra/Desktop/LogRenforcement.csv");
		FileWriter fw = new FileWriter (f);
		fw.write("Partie ; NbCoupsJoue ; Vainqueur ; Epsilon ; Connaissance\n");

		String gagnant = "" ;

		int nbPartie = 0 ;

		while (nbPartie < 15000)
		{
			String board[] = new String[8];
			for ( int i = 0 ; i < 8 ; i++ )
				board[i] = "--------";
						
			String player = "v" ;		
			while (! moveImpossible(board, player))
			{
				String coup ;
				int lig, col ;
				
				// Algo Alpha-Beta
				if ( player.equals("h")) 
				{		
					String p ;
					if (player.equals("v"))
			            p = "h";
			        else
			            p = "v";
					
					Object[] result = negamax(board, p, 2, -999, 999);
					
					coup = (String)result[1];

					lig = Character.getNumericValue(coup.toCharArray()[0]);
					col = Character.getNumericValue(coup.toCharArray()[1]);
				}
				// Algo Renforcement
				else
				{
					coup = apprentissage(board, player);	

					lig = Character.getNumericValue(coup.toCharArray()[0]);
					col = Character.getNumericValue(coup.toCharArray()[2]);
				}

				board = play(board, player, lig, col);
				/*
				System.out.println("\nEtat du Jeu : Tour "+player+"\n");
				System.out.println("coup="+coup);
	
				for ( String s : board )
					System.out.println(s);*/
				
				if (player.equals("v"))
		            player = "h";
		        else
		            player = "v";
				

				if ( moveImpossible(board, player)) 
				{
					if (player.equals("v"))
						gagnant = "h";
			        else
			        	gagnant = "v";
				}
				
	
			}
		
			//System.out.println("\nEtat du Jeu : Tour "+player+"\n");
			
			int nbCoup = 0 ;

			for ( String s : board )
				for ( char c : s.toCharArray() )
					if ( c != '-' )
						nbCoup++ ;
			
			System.out.println("The end "+gagnant);
			
			
			if ( gagnant.equals("v"))
			{
				System.out.println("victoire: "+epsilon);
				if ( epsilon >= 0.1 )
					epsilon -= 0.005 ;
			}
			
			fw.write(nbPartie+" ; "+nbCoup+" ; "+gagnant+" ; "+epsilon+" ; "+tableauRecompense.size()+"\n");
			
			nbPartie++ ;
			
			System.out.println("Partie n°"+nbPartie);
			//System.out.println("Taille connaisance "+tableauRecompense.size());

		}
		
		fw.close();
		System.out.println("NB TOUR TOTAL "+nbPartie);

        //System.out.println((int)jeu[1]+" "+(int)jeu[2]);
       // System.out.println(bestLigne+" "+bestColonne);

        /*System.out.println(bestLigne);
        System.out.println(bestColonne);*/
	}


	private static String apprentissage(String[] b, String player)
	{
		String board ="";
		for (String s : b)
			board+= s ;
		
		double p = Math.random();
		String politique ;
		
		// Politique aleatoire
		if ( p < epsilon )
		{
			int ligne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7
			int colonne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7

			politique = ligne+" "+colonne;
			
		//	System.out.println("Exploration "+politique);
		}
		else
		{
			politique = politiquePlateau.get(board);
			
			if (politique == null)
			{
				int ligne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7
				int colonne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7

				politique = ligne+" "+colonne;
			}
			
		//	System.out.println("Continuation "+politique);
		}
		
		double recompense ;
		while ( !moveAutorise(b, politique, player) )
		{
			recompense = -1 ;
			tableauRecompense.put(board+" "+politique, recompense);
			majEtats(b, board, player, false);

			p = Math.random();
			
			// Politique aleatoire
			if ( p > 0.8 )
			{
				int ligne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7
				int colonne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7

				politique = ligne+" "+colonne;
				
			//	System.out.println("Exploration "+politique);
			}
			else
			{
				politique = politiquePlateau.get(board);
				
				if (politique == null)
				{
					int ligne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7
					int colonne = (int)( Math.random()*( 7 - 0 + 1 ) ); // chiffre entre 0 et 7

					politique = ligne+" "+colonne;
				}
				
			//	System.out.println("Continuation "+politique);
			}
		}


		recompense = getUtilitePlateau(b, player) + getUtiliteDirection(b, player, politique) ;
		
		//System.out.println("   Recompense = "+recompense);
		tableauRecompense.put(board+" "+politique, recompense);
		//System.out.println("   "+tableauRecompense);

		
		majEtats(b, board, player, true);

		return politique ;

	}



	private static double getUtiliteDirection(String[] b, String player, String politique) 
	{
		int lig = Character.getNumericValue(politique.toCharArray()[0]);
		int col = Character.getNumericValue(politique.toCharArray()[2]);
		
		b = play(b, player, lig, col);
		
		return getUtilitePlateau(b, player) ;
	}


	private static double getUtilitePlateau(String[] b, String player) 
	{
		String adv ;
		if (player.equals("v"))
			adv = "h";
        else
        	adv = "v";
	
		
		if ( moveImpossible(b, player)) 
			return -1 ;
		else if ( moveImpossible(b, adv))
			return 999 ;
		else 
			return eval(b, player);//return -0.01 ;
	}


	private static boolean moveAutorise(String[] board, String coup, String player)
	{
		int lig = Character.getNumericValue(coup.toCharArray()[0]);
		int col = Character.getNumericValue(coup.toCharArray()[2]);

				
		if ( board[lig].toCharArray()[col] == '-' )
		{
			if ( player.equals("v") )
			{
				if ( lig < 7 && board[lig+1].toCharArray()[col] == '-' )
					return true ;
				else
					return false ;
			}
			else
			{
				if ( col < 7 && board[lig].toCharArray()[col+1] == '-' )
					return true ;
				else
					return false ;
			}
		}
		else
			return false ;
	}
	

	private static void majEtats(String[] b, String board, String player, boolean moveAutorise) 
	{
		String politique = "";
		
		ArrayList<String> actionsAMaj = new ArrayList<>();
		for(String key : tableauRecompense.keySet())
		{
			String[] s = key.split(" ");
			
			politique = s[1]+" "+s[2] ;
					
			if ( s[0].equals(board) && moveAutorise(b, politique, player))
				actionsAMaj.add(key) ;
		}
		
		politique = "";
		double recompense = -9999 ;
		for ( String key : actionsAMaj )
		{
			if ( recompense < tableauRecompense.get(key) )
			{
				recompense = tableauRecompense.get(key) ;
				politique = key.split(" ")[1]+" "+key.split(" ")[2];
			}
		}
		
		if (! "".equals(politique))
		{
			//System.out.println("   Mise a jour etat "+board+" moveAutorise? "+moveAutorise+" politique "+politique);
			politiquePlateau.put(board, politique) ;
			double gamma = 0.1 ;
			double alpha = 0.1 ;
			
			if ( moveAutorise )
				utilitePlateau.put(board, getUtilitePlateau(b, player)+(alpha*(recompense+(gamma*(getUtiliteDirection(b, player, politique)-getUtilitePlateau(b, player))))));
			else
				utilitePlateau.put(board, getUtilitePlateau(b, player)+(alpha*(recompense+(gamma*(-1-getUtilitePlateau(b, player))))));

			
			//utilitePlateau[x][y] = utiliteReel[x][y]+recompense;
		}
	}		
}