public class Mention {

  private int artisteSource;
  private int artisteMentionne;
  private int poids;

  public Mention(int artisteSource, int artisteMentionne, int poids) {
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

  public int getPoids() {
    return poids;
  }
}
