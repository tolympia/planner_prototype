import java.util.*;
import java.time.*;

public abstract class Schedule {
  private final static String[] ALL_BLOCKS = {"A", "B", "C", "D", "E", "F", "G"};
  private final static int NUM_BLOCKS_PER_DAY = 5;
  private final static int[] DAY_TYPES = {1, 2, 3, 4, 5, 6, 7};  // e.g., Day 1 in Planner.
  public static Map<String, Integer> schoolDays;

  public ArrayList<Block> blocks;
  public int dayType = -1;

  public static LocalDate firstDayOfSchool = LocalDate.of(2021, Month.SEPTEMBER, 13);
  public static LocalDate lastDayOfSchool = LocalDate.of(2022, Month.JANUARY, 12);

  public Schedule(LocalDate date) {
    this.dayType = schoolDays.get(date.toString());
  }

  // Dates where we don't have school.
  public static ArrayList<LocalDate> holidays = new ArrayList<LocalDate>() {
    {
      add(LocalDate.of(2021, Month.SEPTEMBER, 16));
      add(LocalDate.of(2021, Month.OCTOBER, 11));
      add(LocalDate.of(2021, Month.NOVEMBER, 24));
      add(LocalDate.of(2021, Month.NOVEMBER, 25));
      add(LocalDate.of(2021, Month.NOVEMBER, 26));
    }
  };

  static {
    // Winter break is December 20 to January 2.
    LocalDate firstDayOfBreak = LocalDate.of(2021, Month.DECEMBER, 20);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfBreak.plusDays(i));
    }

    schoolDays = getSchoolDaysMap();
  }

  // Return start LocalDateTime for a block based on the date and blockNum.
  public LocalDateTime getStart(LocalDate date, int blockNum) {
    Block blk = blocks.get(blockNum);
    return LocalDateTime.of(date, blk.startTime);
  }

  // Return end LocalDateTime for a block based on the date and blockNum.
  public LocalDateTime getEnd(LocalDate date, int blockNum) {
    Block blk = blocks.get(blockNum);
    return LocalDateTime.of(date, blk.endTime);
  }

  public LocalDateTime getStart(LocalDate date, String blockName) {
    Block blk = getBlockNumFromName(blockName);
    return LocalDateTime.of(date, blk.startTime);
  }

  public LocalDateTime getEnd(LocalDate date, String blockName) {
    Block blk = getBlockNumFromName(blockName);
    return LocalDateTime.of(date, blk.endTime);
  }

  // ** Helper functions **

  public Block getBlockNumFromName(String targetBlock) {
    // e.g. in this schedule, which index does block A have?
    ArrayList<String> blockNames = this.blocksForDayType();
    int thisBlock = blockNames.indexOf(targetBlock);
    return this.blocks.get(thisBlock);
  }

  // Returns the block names for a given day type.
  public ArrayList<String> blocksForDayType() {
    ArrayList<String> blocksToday = new ArrayList<String>(NUM_BLOCKS_PER_DAY);

    int currentBlock = 0;
    for (int i = 1; i < DAY_TYPES.length + 1; i++) {
      blocksToday.clear();
      for (int blockNum = 0; blockNum < NUM_BLOCKS_PER_DAY; blockNum++) {
        blocksToday.add(ALL_BLOCKS[currentBlock % ALL_BLOCKS.length]);
        currentBlock = currentBlock + 1 % ALL_BLOCKS.length;
      }
      if (i == this.dayType) {
        return blocksToday;
      }
    }
    return blocksToday;
  }

  private static boolean isThereSchool(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
      return false;
    }
    if (holidays.contains(date)) {
      return false;
    }
    return true;
  }

  public static TreeMap getSchoolDaysMap() {
    // Returns String dates, int dayType map.
    TreeMap schoolDays = new TreeMap();
    int dayTypeIdx = 0;
    int dayCounter = 0;
    // TODO: This could be a lot cleaner.
    while (true) {
      LocalDate thisDate = firstDayOfSchool.plusDays(dayCounter);
      if (thisDate.isAfter(lastDayOfSchool)) {
        break;
      }
      if (isThereSchool(thisDate)) {
        schoolDays.put(thisDate.toString(), DAY_TYPES[dayTypeIdx]);
        dayTypeIdx++;
        dayTypeIdx = dayTypeIdx % DAY_TYPES.length;
      }
      dayCounter++;
    }
    return schoolDays;
  }

}
