public class Mention {

  private Artist artist1;
  private Artist artist2;
  private int nbMentions;

  public Mention(Artist artist1, Artist artist2, int nbMentions) {
    this.artist1 = artist1;
    this.artist2 = artist2;
    this.nbMentions = nbMentions;
  }

  public Artist getArtist1() {
    return artist1;
  }

  public Artist getArtist2() {
    return artist2;
  }

  public int getNbMentions() {
    return nbMentions;
  }


}
