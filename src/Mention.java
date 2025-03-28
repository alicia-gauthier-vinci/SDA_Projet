public class Mention {

  private int artisteSource;
  private int artisteMentionne;
  private double poids;

  public Mention(int artisteSource, int artisteMentionne, double poids) {
    this.artisteSource = artisteSource;
    this.artisteMentionne = artisteMentionne;
    this.poids = poids;
  }

  public int getArtisteSource() {
    return artisteSource;
  }

  public int getArtisteMentionne() {
    return artisteMentionne;
  }

  public double getPoids() {
    return poids;
  }
}
