import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {

  private Map<Integer, Artist> artistes;
  private Map<String, Artist> artistesParNom;
  private Map<Integer, List<Mention>> listAdj;

  public Graph(String artistesTxt, String mentionsTxt) {
    artistes = new HashMap<>();
    artistesParNom = new HashMap<>();
    listAdj = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(artistesTxt))) {
      String line;
      while ((line = br.readLine()) != null) {
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

    try (BufferedReader br = new BufferedReader(new FileReader(mentionsTxt))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        int artisteSrc = Integer.parseInt(parts[0]);
        int artisteMentionne = Integer.parseInt(parts[1]);
        int poids = Integer.parseInt(parts[2]);
        double invertedWeight = 1.0 / poids;
        Mention mention = new Mention(artisteSrc, artisteMentionne, invertedWeight);
        listAdj.computeIfAbsent(artisteSrc, k -> new ArrayList<>()).add(mention);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void trouverCheminLePlusCourt(String source, String destination) {
    Queue<Integer> queue = new ArrayDeque<>();
    Map<Integer, Mention> artistesVisites = new HashMap<>();
    Map<Integer, Double> cout = new HashMap<>();

    Artist artisteSource = artistesParNom.get(source);
    Artist artisteDestination = artistesParNom.get(destination);

    queue.add(artisteSource.getId());
    artistesVisites.put(artisteSource.getId(), null);
    cout.put(artisteSource.getId(), 0.0);

    boolean trouve = false;

    while (!queue.isEmpty()) {
      int current = queue.poll();
      if (current == artisteDestination.getId()) {
        trouve = true;
        break;
      }
      List<Mention> listeMentions = listAdj.get(current);
      if (listeMentions != null) {
        for (Mention mention : listeMentions) {
          int voisin = mention.getArtisteMentionne();
          double poids = mention.getPoids();
          if (!artistesVisites.containsKey(voisin)) {
            queue.add(voisin);
            artistesVisites.put(voisin, mention);
            cout.put(voisin, cout.get(current) + poids);
          }
        }
      }
    }
    if (trouve) {
      afficherChemin(artisteSource.getId(), artisteDestination.getId(), artistesVisites, cout);
    } else {
      throw new RuntimeException("Aucun chemin entre " + source + " et " + destination);
    }
  }

  public void trouverCheminMaxMentions(String source, String destination) {
    Map<Integer, Double> distances = new HashMap<>();
    Queue<Artist> artistePoidsPlusFaible = new PriorityQueue<>(
        Comparator.comparingDouble(artiste -> distances.get(artiste.getId())));
    Map<Integer, Mention> artistesAtteints = new HashMap<>();

    Artist artisteSource = artistesParNom.get(source);
    Artist artisteDestination = artistesParNom.get(destination);

    for (Integer id : artistes.keySet()) {
      distances.put(id, Double.MAX_VALUE);
    }

    distances.put(artisteSource.getId(), 0.0);
    artistePoidsPlusFaible.add(artisteSource);

    boolean trouve = false;

    while (!artistePoidsPlusFaible.isEmpty()) {
      Artist currentArtiste = artistePoidsPlusFaible.poll();
      int current = currentArtiste.getId();

      if (current == artisteDestination.getId()) {
        trouve = true;
        break;
      }

      List<Mention> listeMentions = listAdj.get(current);
      if (listeMentions != null) {
        for (Mention mention : listeMentions) {
          int voisin = mention.getArtisteMentionne();
          double nvDist = distances.get(current) + mention.getPoids();

          if (nvDist < distances.get(voisin)) {
            distances.put(voisin, nvDist);
            artistesAtteints.put(voisin, mention);
            artistePoidsPlusFaible.add(artistes.get(voisin));
          }
        }
      }
    }
    if (trouve) {
      afficherChemin(artisteSource.getId(), artisteDestination.getId(), artistesAtteints,
          distances);
    } else {
      throw new RuntimeException("Aucun chemin entre " + source + " et " + destination);
    }
  }

  public void afficherChemin(int artisteSourceId, int artisteDestinationId,
      Map<Integer, Mention> artistesVisites, Map<Integer, Double> distance) {

    double coutTotal = distance.get(artisteDestinationId);
    ArrayList<Integer> chemin = new ArrayList<>();
    int current = artisteDestinationId;
    while (artistesVisites.get(current) != null) {
      Mention mention = artistesVisites.get(current);
      chemin.add(current);
      current = mention.getArtisteSource();
    }
    chemin.add(artisteSourceId);
    Collections.reverse(chemin);

    System.out.println("Longueur du chemin : " + (chemin.size() - 1));
    System.out.println("Cout total du chemin: " + coutTotal);
    System.out.println("Chemin : ");
    for (Integer id : chemin) {
      System.out.println(artistes.get(id).getNom() + "," + artistes.get(id).getCategorie());
    }
  }
}
