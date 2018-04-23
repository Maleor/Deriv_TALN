package derivationsTALN_package;


import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import RequeterRezo.Terme;

public class Application {

		
        public Regles r;
        public RequeterRezo systeme;
        
        public Application() throws IOException{
                r = new Regles();
                r.readFile();
                systeme = new RequeterRezo("12h", 10000);
        }
        
		public void run() throws ArrayIndexOutOfBoundsException, MalformedURLException, IOException, InterruptedException{ 
        	
        		FileWriter fichier = new FileWriter("resultat.txt");
        		fichier.write ("Mon premier fichier, priere de m'aider!");
            	fichier.close();
        	
            	System.out.println("RÃ©cupÃ©ration du mot dans jeuxdemots...\n");
            	
        		Mot m = requestWord("nager");
        
                if(m!=null){
                        String lem = getLemFromWord(m);
                        System.out.println("Lemme de "+m.getNom()+" : "+lem);                      
                        String classGramm = getClaGrammFromWord(m, 0);
                        
                        /*if(classGramm.equals("Nom:")) classGramm = "Nom";
                        if(classGramm.equals("Adj:")) classGramm = "Adj";
                        if(classGramm.equals("Ver:Inf")) classGramm = "VerInf";
                        if(classGramm.equals("Ver:")) classGramm = "Verbe";*/
                        if(classGramm.equals("Nom:Mas+SG")) classGramm = "Nom:";
                      
                        System.out.println("Classe grammaticale de "+m.getNom()+" : "+classGramm);
                        genereDerivations(m.getNom(), classGramm);
                } else {
                        System.out.println("Word does not exist");
                }
        }
        
        
        public Mot requestWord(String word){
                try {
                        return systeme.requete(word, true);
                } catch(InterruptedException e) {
        			e.printStackTrace();
        		} catch(MalformedURLException e) {
        			e.printStackTrace();
        		} catch(IOException e) {
        			e.printStackTrace();
        		}
                System.out.println("failed");
                return null;
        }
        
        
        public String getLemFromWord(Mot m){
                System.out.println("RÃ©cupÃ©ration du lemme de "+m.getNom());
                 HashMap<String, ArrayList<Terme>> req = m.getRelations_sortantes();
                 ArrayList<Terme> termes = req.get("r_lemma");
                 
                 if(termes!=null) return termes.get(0).getTerme();
                 else return null;
        }
        
        public String getClaGrammFromWord(Mot m, int index){
             HashMap<String, ArrayList<Terme>> req = m.getRelations_sortantes();
             ArrayList<Terme> termes = req.get("r_pos");
             
             if(termes!=null) return termes.get(index).getTerme();
             else return null;
        }
        
        public int getNumberOfClaGrammFromWord(Mot m){
        	
             HashMap<String, ArrayList<Terme>> req = m.getRelations_sortantes();
             ArrayList<Terme> termes = req.get("r_pos"); 
             
             if(termes != null) return termes.size();
             return 0;
        }
        
        public boolean hasClassGramm(Mot mot, String classGramm){
        	boolean res = false;
        	int tmpVar = getNumberOfClaGrammFromWord(mot);
    		for(int index = 0 ; index < tmpVar ; index++) {
    			if(getClaGrammFromWord(mot, index).equals(classGramm)) {
    				res = true;
    				break;
    			}
    		}
        	return res;
        }
        
        public void genereDerivations(String mot, String classGramm) throws IOException, MalformedURLException, InterruptedException, ArrayIndexOutOfBoundsException {
        	ArrayList<String> existants = new ArrayList<>(); //contient les mots qui existent
        	ArrayList<String> nonExistants = new ArrayList<>(); //contient les mots qui n'existent pas
        	ArrayList<String> errClaGramm = new ArrayList<>(); //contient les mots qui existent mais qui ont été renvoyé avec la mauvaise classe grammaticale
        	switch(classGramm) {
        		case "Nom:":
        			for(int jndex = 0 ; jndex < mot.length() ; jndex++) {
        				String finMot = mot.substring(jndex, mot.length());
        				for(int index = 0 ; index < r.reglesNom.size() ; index++) {
        					if(finMot.equals(r.reglesNom.get(index).get(0))) {
        						String baseMot = mot.substring(0, mot.length() - r.reglesNom.get(index).get(0).length());
        						Mot m = requestWord(baseMot + r.reglesNom.get(index).get(2));
        						
        						if((m!=null)&&(hasClassGramm(m, r.reglesNom.get(index).get(1)))) existants.add(r.reglesNom.get(index).get(1) + " : " +baseMot + r.reglesNom.get(index).get(2));        		            
        		                else nonExistants.add(r.reglesNom.get(index).get(1) + " : " +baseMot + r.reglesNom.get(index).get(2));
        		                
        		                if((m!=null)&&!(hasClassGramm(m, r.reglesNom.get(index).get(1)))) errClaGramm.add(r.reglesNom.get(index).get(1) + " : " +baseMot + r.reglesNom.get(index).get(2));
        					}
        				}
        			}
        			break;
        			
        		case "Adj:":
        			for(int jndex = 0 ; jndex < mot.length() ; jndex++) {
        				String finMot = mot.substring(jndex, mot.length());
        				for(int index = 0 ; index < r.reglesAdj.size() ; index++) {
        					if(finMot.equals(r.reglesAdj.get(index).get(0))) {
        						String baseMot = mot.substring(0, mot.length() - r.reglesAdj.get(index).get(0).length());
        						
        						Mot m = requestWord(baseMot + r.reglesAdj.get(index).get(2));
 
        						if((m!=null)&&(hasClassGramm(m, r.reglesAdj.get(index).get(1)))) existants.add(r.reglesAdj.get(index).get(1) + " : " +baseMot + r.reglesAdj.get(index).get(2));
        		                else nonExistants.add(r.reglesAdj.get(index).get(1) + " : " +baseMot + r.reglesAdj.get(index).get(2));
        						
        						if((m!=null)&&!(hasClassGramm(m, r.reglesAdj.get(index).get(1)))) errClaGramm.add(r.reglesAdj.get(index).get(1) + " : " +baseMot + r.reglesAdj.get(index).get(2));
        					}
        				}
        			}
        			break;
        			
        		case "Adv:":
        			for(int jndex = 0 ; jndex < mot.length() ; jndex++) {
        				String finMot = mot.substring(jndex, mot.length());
        				for(int index = 0 ; index < r.reglesAdv.size() ; index++) {
        					if(finMot.equals(r.reglesAdv.get(index).get(0))) {
        						String baseMot = mot.substring(0, mot.length() - r.reglesAdv.get(index).get(0).length());
        						
        						Mot m = requestWord(baseMot + r.reglesAdv.get(index).get(2));
 
        						if((m!=null)&&(hasClassGramm(m, r.reglesAdv.get(index).get(1)))) existants.add(r.reglesAdv.get(index).get(1) + " : " +baseMot + r.reglesAdv.get(index).get(2));
        		                else nonExistants.add(r.reglesAdv.get(index).get(1) + " : " +baseMot + r.reglesAdv.get(index).get(2));
        						
        						if((m!=null)&&!(hasClassGramm(m, r.reglesAdv.get(index).get(1)))) errClaGramm.add(r.reglesAdv.get(index).get(1) + " : " +baseMot + r.reglesAdv.get(index).get(2));
        					}
        				}
        			}
        			break;
        			
        		case "Ver:Inf":
        			for(int jndex = 0 ; jndex < mot.length() ; jndex++) {
        				
        				String finMot = mot.substring(jndex, mot.length());
        				
        				for(int index = 0 ; index < r.reglesVerbe.size() ; index++) {

        					String exception = r.reglesVerbe.get(index).get(3); //on prend l'exception qui est Ã  la fin de la regle
        					int tailleException = exception.length(); //on prend la taille de l'exception (nombre de lettres)
        					
        					if(!(mot.substring(mot.length()-tailleException, mot.length()).equals(exception))) { //on verifie que le mot ne se termine pas par l'exception
        						if(finMot.equals(r.reglesVerbe.get(index).get(0))) {
            						
            						String baseMot = mot.substring(0, mot.length() - r.reglesVerbe.get(index).get(0).length());
            						Mot m = requestWord(baseMot + r.reglesVerbe.get(index).get(2));
            		                
            						if((m!=null)&&(hasClassGramm(m, r.reglesVerbe.get(index).get(1)))) existants.add(r.reglesVerbe.get(index).get(1) + " : " +baseMot + r.reglesVerbe.get(index).get(2));
            		                else nonExistants.add(r.reglesVerbe.get(index).get(1) + " : " +baseMot + r.reglesVerbe.get(index).get(2));
            						
            						if((m!=null)&&!(hasClassGramm(m, r.reglesVerbe.get(index).get(1)))) errClaGramm.add(r.reglesVerbe.get(index).get(1) + " : " +baseMot + r.reglesVerbe.get(index).get(2));
            					}	
        					}	
        				}
        			}
        			break;
        	}
        	System.out.println("\nIL(S) EXISTE(NT) : \n");
        	for(int kndex = 0 ; kndex < existants.size() ; kndex ++) System.out.println(existants.get(kndex));
        	System.out.println("\nIL(S) N'EXISTE(NT) PAS : \n");
        	for(int kndex = 0 ; kndex < nonExistants.size() ; kndex ++) System.out.println(nonExistants.get(kndex));
        	System.out.println("\nIL(S) EXISTE(NT) MAIS PAS COMME PREVU : \n");
        	for(int kndex = 0 ; kndex < errClaGramm.size() ; kndex ++) System.out.println(errClaGramm.get(kndex));
        }
        
}
