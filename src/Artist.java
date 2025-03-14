public class Artist {

  private int numero;
  private String nom;
  private String categorie;

  public Artist(int numero , String nom, String categorie) {
    this.numero = numero;
    this.nom = nom;
    this.categorie = categorie;
  }

  public int getNumero() {
    return numero;
  }

  public String getNom() {
    return nom;
  }

  public String getCategorie() {
    return categorie;
  }

}
