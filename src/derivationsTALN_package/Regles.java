package derivationsTALN_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Regles {

	public ArrayList<ArrayList<String>> reglesVerbe;
	public ArrayList<ArrayList<String>> reglesNom;
	public ArrayList<ArrayList<String>> reglesAdj;
	public ArrayList<ArrayList<String>> reglesAdv;
	
	Regles(){
		reglesVerbe = new ArrayList<>();
		reglesNom = new ArrayList<>();
		reglesAdj = new ArrayList<>();
		reglesAdv = new ArrayList<>();
	}
	
	public void readFile() throws IOException {
		
		File fichier = new File("./src/regles");
		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fichier));
		String ligne = "";
		String [] l;
		while (ligne != null) {
			ligne = bufferedReader.readLine();
			if(ligne !=null) {
				l = ligne.split(";");
				if(l[0].equals("Ver:Inf")) reglesVerbe.add(new ArrayList<String>(Arrays.asList(l[1],l[2],l[3],l[4])));
				else if(l[0].equals("Nom:")) reglesNom.add(new ArrayList<String>(Arrays.asList(l[1],l[2],l[3],l[4])));
				else if(l[0].equals("Adv:")) reglesAdv.add(new ArrayList<String>(Arrays.asList(l[1],l[2],l[3],l[4])));
				else if(l[0].equals("Adj:")) reglesAdj.add(new ArrayList<String>(Arrays.asList(l[1],l[2],l[3],l[4])));
			}
		}
	}
	
	public void analyseRegle(String mot) {
		System.out.println("Mot en argument :" + mot);
	}
	
	public String toString(){
		String ret = "Règles : \n";
		
		for(int i = 0; i<reglesVerbe.size(); i++)
			ret+="verbe"+":"+reglesVerbe.get(i).get(0)+";"+reglesVerbe.get(i).get(1)+":"+reglesVerbe.get(i).get(2)+"\n";

		for(int i = 0; i<reglesNom.size(); i++)
			ret+="nom"+":"+reglesNom.get(i).get(0)+";"+reglesNom.get(i).get(1)+":"+reglesNom.get(i).get(2)+"\n";
		
		for(int i = 0; i<reglesAdj.size(); i++)
			ret+="adj"+":"+reglesAdj.get(i).get(0)+";"+reglesAdj.get(i).get(1)+":"+reglesAdj.get(i).get(2)+"\n";
		
		for(int i = 0; i<reglesAdv.size(); i++)
			ret+="adv"+":"+reglesAdv.get(i).get(0)+";"+reglesAdv.get(i).get(1)+":"+reglesAdv.get(i).get(2)+"\n";

		return ret;
	}
}
