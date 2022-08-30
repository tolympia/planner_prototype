import java.util.*;
import java.time.*;


public abstract class USSchedule extends Schedule {
  protected String[] ALL_BLOCKS = {"A", "B", "C", "D", "E", "F", "G"};
  protected int NUM_BLOCKS_PER_DAY = 5;
  protected int[] DAY_TYPES = {1, 2, 3, 4, 5, 6, 7};  // e.g., Day 1 in Planner.
//  public static LocalDate firstDayOfSchool = LocalDate.of(2022, Month.SEPTEMBER, 12);
  public static LocalDate firstDayOfSchool = LocalDate.of(2023, Month.JANUARY, 25);

//  public static LocalDate lastDayOfSchool = LocalDate.of(2023, Month.JANUARY, 11);
  public static LocalDate lastDayOfSchool = LocalDate.of(2023, Month.MAY, 24);

  public USSchedule(LocalDate date) {
    this.date = date;
    this.dayType = getDayType();
    this.blockNames = blocksForDayType();
  }

  // Dates where we don't have school.
  public static ArrayList<LocalDate> holidays = new ArrayList<LocalDate>() {
    {
      // Semester 1.
      add(LocalDate.of(2022, Month.SEPTEMBER, 26));
      add(LocalDate.of(2022, Month.OCTOBER, 5));
      add(LocalDate.of(2022, Month.OCTOBER, 10));
      add(LocalDate.of(2022, Month.NOVEMBER, 23));
      add(LocalDate.of(2022, Month.NOVEMBER, 24));
      add(LocalDate.of(2022, Month.NOVEMBER, 25));
      add(LocalDate.of(2023, Month.JANUARY, 2));

      // Semester 2.
      add(LocalDate.of(2023, Month.JANUARY, 16));
      add(LocalDate.of(2023, Month.FEBRUARY, 16));
      add(LocalDate.of(2023, Month.FEBRUARY, 17));
      add(LocalDate.of(2023, Month.FEBRUARY, 20));
      add(LocalDate.of(2023, Month.APRIL, 7));
      add(LocalDate.of(2023, Month.APRIL, 28));  // CHARTER DAY.
    }
  };

  public static TreeMap<LocalDate, String> adjustedSchedules = new TreeMap<LocalDate, String>() {
    {
      put(LocalDate.of(2022, Month.SEPTEMBER, 15), "Adjusted1");
      put(LocalDate.of(2022, Month.OCTOBER, 11), "Adjusted1");
      put(LocalDate.of(2022, Month.OCTOBER, 20), "Adjusted1");
      put(LocalDate.of(2023, Month.JANUARY, 5), "Adjusted1");
      put(LocalDate.of(2023, Month.FEBRUARY, 9), "Adjusted1");
      put(LocalDate.of(2023, Month.APRIL, 25), "Adjusted1");
      put(LocalDate.of(2023, Month.APRIL, 27), "Adjusted1");
      put(LocalDate.of(2023, Month.MAY, 11), "Adjusted1");
      put(LocalDate.of(2022, Month.SEPTEMBER, 29), "Adjusted2");
      put(LocalDate.of(2022, Month.OCTOBER, 28), "USAdjustedArtsAssembly");
      put(LocalDate.of(2023, Month.MAY, 18), "USAdjustedCommencement");
      put(LocalDate.of(2023, Month.MAY, 15), "USAdjustedHonors");
      put(LocalDate.of(2022, Month.NOVEMBER, 22), "USAdjustedIngathering");
      put(LocalDate.of(2023, Month.JANUARY, 26), "USAdjustedMLK");
      put(LocalDate.of(2023, Month.MAY, 17), "USAdjustedRehearsal");
      put(LocalDate.of(2022, Month.DECEMBER, 16), "USAdjustedWinterHoliday");
    }
  };

  static {
    // Winter break is December 19 to January 1.
    LocalDate firstDayOfWinterBreak = LocalDate.of(2022, Month.DECEMBER, 19);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfWinterBreak.plusDays(i));
    }

    // Spring break is March 13 to March 24.
    LocalDate firstDayOfSpringBreak = LocalDate.of(2023, Month.MARCH, 13);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfSpringBreak.plusDays(i));
    }
  }

  // ** Helper functions **

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

  private boolean isThereSchool(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
      return false;
    }
    if (holidays.contains(date)) {
      return false;
    }
    return true;
  }

  public int getDayType() {
    // Returns -1 if not a school day.
    if (!isThereSchool(this.date)) {
      return -1;
    }
    int dayTypeIdx = 0;
    int dayCounter = 0;
    // TODO: This could be a lot cleaner.
    while (true) {
      LocalDate thisDate = firstDayOfSchool.plusDays(dayCounter);
      if (thisDate.isAfter(lastDayOfSchool)) {
        return -1;
      }
      if (thisDate.equals(this.date)) {
        return DAY_TYPES[dayTypeIdx];
      }
      if (isThereSchool(thisDate)) {
        dayTypeIdx++;
        dayTypeIdx = dayTypeIdx % DAY_TYPES.length;
      }
      dayCounter++;
    }
  }

  public boolean isThereBlockToday(String blockName) {
    return this.blockNames.contains(blockName);
  }

}
