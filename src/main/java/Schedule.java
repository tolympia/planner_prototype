import java.util.*;
import java.time.*;

public abstract class Schedule {
  public int num;
  public ArrayList<Block> blocks;

  public ArrayList<LocalDateTime> getLocalDateTimes(LocalDate date, int blockNum) {
    ArrayList<LocalDateTime> startEnd = new ArrayList<>();
    Block blk = blocks.get(blockNum);
    startEnd.add(LocalDateTime.of(date, blk.startTime));
    startEnd.add(LocalDateTime.of(date, blk.endTime));
    return startEnd;
  }

  public abstract void adjustForUpperclassmen();
}
