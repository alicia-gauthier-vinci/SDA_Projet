import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
  private Map<Integer, Artist> artistes;
  private Map<Integer, List<Mention>> adjList;

  public Graph(String artistsFile, String mentionsFile) {
    artistes = new HashMap<>();
    adjList = new HashMap<>();
    lireArtistes(artistsFile);
    lireMentions(mentionsFile);
  }

  private void lireArtistes(String fileName) {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String nom = parts[1];
        String categorie = parts[2];
        artistes.put(id, new Artist(id, nom, categorie));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void lireMentions(String fileName) {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        int sourceId = Integer.parseInt(parts[0]);
        int targetId = Integer.parseInt(parts[1]);
        int weight = Integer.parseInt(parts[2]);
        double invertedWeight = 1.0 / weight; // Inverser le poids
        Mention mention = new Mention(sourceId, targetId, invertedWeight);
        adjList.computeIfAbsent(sourceId, k -> new ArrayList<>()).add(mention);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void trouverCheminLePlusCourt(String source, String destination) {
    int sourceId = trouverIdParNom(source);
    int destinationId = trouverIdParNom(destination);

    Map<Integer, Integer> predecessors = new HashMap<>();
    Map<Integer, Double> distances = new HashMap<>();
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    for (int id : artistes.keySet()) {
      distances.put(id, Double.MAX_VALUE);
    }
    distances.put(sourceId, 0.0);

    queue.add(sourceId);
    visited.add(sourceId);

    boolean pathFound = false;

    while (!queue.isEmpty()) {
      int currentId = queue.poll();

      if (currentId == destinationId) {
        pathFound = true;
        break;
      }

      for (Mention neighbor : adjList.getOrDefault(currentId, new ArrayList<>())) {
        int neighborId = neighbor.getTargetId();
        if (!visited.contains(neighborId)) {
          visited.add(neighborId);
          predecessors.put(neighborId, currentId);
          distances.put(neighborId, distances.get(currentId) + 1); // Increment the distance by 1 for each step
          queue.add(neighborId);
        }
      }
    }

    if (pathFound) {
      afficherCheminEtCout(sourceId, destinationId, predecessors, distances);
    } else {
      throw new RuntimeException("Aucun chemin entre " + source + " et " + destination);
    }
  }

  private void afficherCheminEtCout(int sourceId, int destinationId, Map<Integer, Integer> predecessors, Map<Integer, Double> distances) {
    List<Integer> chemin = new ArrayList<>();
    for (Integer at = destinationId; at != null; at = predecessors.get(at)) {
      chemin.add(at);
    }
    Collections.reverse(chemin);

    System.out.println("Longueur du chemin : " + (chemin.size() - 1));
    System.out.println("Coût total du chemin : " + distances.get(destinationId));
    System.out.println("Chemin :");
    for (int id : chemin) {
      System.out.println(artistes.get(id));
    }
  }

  public void trouverCheminMaxMentions(String source, String destination) {
    int sourceId = trouverIdParNom(source);
    int destinationId = trouverIdParNom(destination);

    Map<Integer, Double> distances = new HashMap<>();
    Map<Integer, Integer> predecessors = new HashMap<>();
    PriorityQueue<Mention> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Mention::getWeight));

    for (int id : artistes.keySet()) {
      distances.put(id, Double.MAX_VALUE);
    }
    distances.put(sourceId, 0.0);
    priorityQueue.add(new Mention(sourceId, sourceId, 0));

    while (!priorityQueue.isEmpty()) {
      Mention currentMention = priorityQueue.poll();
      int currentId = currentMention.getTargetId();

      if (currentId == destinationId) break;

      for (Mention neighbor : adjList.getOrDefault(currentId, new ArrayList<>())) {
        int neighborId = neighbor.getTargetId();
        double newDist = distances.get(currentId) + neighbor.getWeight();

        if (newDist < distances.get(neighborId)) {
          distances.put(neighborId, newDist);
          predecessors.put(neighborId, currentId);
          priorityQueue.add(new Mention(currentId, neighborId, newDist));
        }
      }
    }

    afficherCheminEtMentions(sourceId, destinationId, distances, predecessors);
  }

  private void afficherCheminEtMentions(int sourceId, int destinationId, Map<Integer, Double> distances, Map<Integer, Integer> predecessors) {
    List<Integer> chemin = new ArrayList<>();
    for (Integer at = destinationId; at != null; at = predecessors.get(at)) {
      chemin.add(at);
    }
    Collections.reverse(chemin);

    System.out.println("Longueur du chemin : " + (chemin.size() - 1));
    System.out.println("Coût total du chemin : " + distances.get(destinationId));
    System.out.println("Chemin :");
    for (int id : chemin) {
      System.out.println(artistes.get(id));
    }
  }

  private int trouverIdParNom(String nom) {
    for (Artist artiste : artistes.values()) {
      if (artiste.getNom().equals(nom)) {
        return artiste.getId();
      }
    }
    throw new IllegalArgumentException("Artiste non trouvé : " + nom);
  }
}