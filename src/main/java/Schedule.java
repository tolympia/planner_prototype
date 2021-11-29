import java.util.*;
import java.time.*;


public abstract class Schedule {
  protected String[] ALL_BLOCKS = {"A", "B", "C", "D", "E", "F", "G"};
  protected int NUM_BLOCKS_PER_DAY = 5;
  protected int[] DAY_TYPES = {1, 2, 3, 4, 5, 6, 7};  // e.g., Day 1 in Planner.

  public ArrayList<Block> blocks;
  public ArrayList<String> blockNames;
  public int dayType = -1;
  protected LocalDate date;

  public LocalDate firstDayOfSchool = GAPlanner2122.usFirstDay;
  public LocalDate lastDayOfSchool = GAPlanner2122.usLastDay;

  public void setup(String[] blocks, int numBlocks, int[] dayTypes) {
    this.ALL_BLOCKS = blocks;
    this.NUM_BLOCKS_PER_DAY = numBlocks;
    this.DAY_TYPES = dayTypes;
    this.dayType = getDayType();
    this.blockNames = blocksForDayType();
  }

  public Schedule(LocalDate date) {
    this.date = date;
  }

  // Dates where we don't have school.
  public static ArrayList<LocalDate> holidays = new ArrayList<LocalDate>() {
    {
      add(LocalDate.of(2021, Month.SEPTEMBER, 16));
      add(LocalDate.of(2021, Month.OCTOBER, 11));
      add(LocalDate.of(2021, Month.NOVEMBER, 24));
      add(LocalDate.of(2021, Month.NOVEMBER, 25));
      add(LocalDate.of(2021, Month.NOVEMBER, 26));
      add(LocalDate.of(2022, Month.JANUARY, 17));
      add(LocalDate.of(2022, Month.FEBRUARY, 17));
      add(LocalDate.of(2022, Month.FEBRUARY, 18));
      add(LocalDate.of(2022, Month.FEBRUARY, 21));
      add(LocalDate.of(2022, Month.APRIL, 15));
      add(LocalDate.of(2022, Month.MAY, 30));

    }
  };

  static {
    // Winter break is December 20 to January 2.
    LocalDate firstDayOfWinterBreak = LocalDate.of(2021, Month.DECEMBER, 20);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfWinterBreak.plusDays(i));
    }

    // Spring break is March 11 to March 27.
    LocalDate firstDayOfSpringBreak = LocalDate.of(2022, Month.MARCH, 12);
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
    return this.blocks.contains(blockName);
  }

}
