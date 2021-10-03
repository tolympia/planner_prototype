import java.util.*;
import java.time.*;


public class Block {
  public LocalTime startTime;
  public LocalTime endTime;
  public int durationMinutes;

  public Block(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public Block(LocalTime startTime, int durationMinutes) {
    this.startTime = startTime;
    this.endTime = startTime.plusMinutes(durationMinutes);
  }

}
