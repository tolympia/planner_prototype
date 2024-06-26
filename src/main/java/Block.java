import java.util.*;
import java.time.*;


// Represents one class in a schedule.
public class Block {
  public LocalTime startTime;
  public LocalTime endTime;
  public int durationMinutes;

  // Constructor where start and end time is specified.
  public Block(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.durationMinutes = (int) Duration.between(startTime, endTime).toMinutes();
  }

  // Constructor where end time is inferred from duration.
  public Block(LocalTime startTime, int durationMinutes) {
    this(startTime, startTime.plusMinutes(durationMinutes));
  }

  public String toString() {
    return "start: " + this.startTime + ", end: " + this.endTime + " (" + this.durationMinutes + ")";
  }
}
