import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph {

  //pour récupérer un artiste selon son id
  private Map<Integer, Artist> artistes;
  //pour récupérer un artiste selon son nom
  private Map<String, Artist> artistesParNom;
  //pour récupérer les mentions d'un artiste
  private Map<Integer, List<Mention>> listAdj;
  //file pour garder les sommets visités
  Queue<Integer> queue = new ArrayDeque<>();
  Set<Integer> artistesVisites = new HashSet<>();

  public Graph(String artistesTxt, String mentionsTxt) {
    artistes = new HashMap<>();
    artistesParNom = new HashMap<>();
    listAdj = new HashMap<>();

    try(BufferedReader br = new BufferedReader(new FileReader(artistesTxt))){
      String line;
      while ((line = br.readLine()) != null){
        String[] artiste = line.split(",");
        int id = Integer.parseInt(artiste[0]);
        String nom = artiste[1];
        String categorie = artiste[2];
        artistes.put(id, new Artist(id, nom, categorie));
        artistesParNom.put(nom, new Artist(id, nom, categorie));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try(BufferedReader br = new BufferedReader(new FileReader(mentionsTxt))){
      String line;
      while ((line = br.readLine()) != null){
        String[] parts = line.split(",");
        int artisteSrc = Integer.parseInt(parts[0]);
        int artisteMentionne = Integer.parseInt(parts[1]);
        int poids = Integer.parseInt(parts[2]);
        Mention mention = new Mention(artisteSrc, artisteMentionne, poids);
        listAdj.computeIfAbsent(artisteSrc, k -> new ArrayList<>()).add(mention);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void trouverCheminLePlusCourt(String source, String destination) {
    Artist artisteSource = artistesParNom.get(source);
    Artist artisteDestination = artistesParNom.get(destination);
    queue.clear();
    artistesVisites.clear();

    queue.add(artisteSource.getId());
    artistesVisites.add(artisteSource.getId());

    boolean trouve = false;
    //retiens les parents de chaque sommet
    int []precedents = new int[artistes.size()];
    int []couts = new int[artistes.size()];

    while(!queue.isEmpty()){
      int current = queue.poll();
      if(current == artisteDestination.getId()){
        trouve = true;
        break;
      }
      //liste des voisins
      List<Mention> listeMentions = listAdj.get(current);
      if(listeMentions != null){
        for(Mention mention : listeMentions){
          int voisin = mention.getArtisteMentionne();
          int poids = mention.getPoids();
          if (!artistesVisites.contains(voisin)) {
            queue.add(voisin);
            artistesVisites.add(voisin);
            precedents[voisin] = current;
            couts[voisin] = couts[current] + poids;
          }
        }
      }
    }

    if(trouve){
      int coutTotal = couts[artisteDestination.getId()];
      System.out.println("Longueur du chemin : ");
      System.out.println("Cout total du chemin: " + coutTotal);
      //récupère le chemin en faisant le trajet inverse depuis la destination
      ArrayList<Integer> chemin = new ArrayList<>();
      int current = artisteDestination.getId();
      while (current != -1) {
        chemin.add(current);
        current = precedents[current];
      }
      Collections.reverse(chemin);
      
      for (Integer id : chemin) {
        System.out.println(artistes.get(id).getNom() + "," + artistes.get(id).getCategorie());
      }
    }
  }

  public void trouverCheminMaxMentions(String artist1, String artist2){

  }
}
