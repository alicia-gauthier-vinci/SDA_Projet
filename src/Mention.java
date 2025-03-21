public class Mention {
  private int sourceId;
  private int targetId;
  private double weight;

  public Mention(int sourceId, int targetId, double weight) {
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.weight = weight;
  }

  public int getSourceId() {
    return sourceId;
  }

  public int getTargetId() {
    return targetId;
  }

  public double getWeight() {
    return weight;
  }
}