public class Mention {
  private int targetId;
  private int weight;

  public Mention(int targetId, int weight) {
    this.targetId = targetId;
    this.weight = weight;
  }

  public int getTargetId() {
    return targetId;
  }

  public int getWeight() {
    return weight;
  }
}