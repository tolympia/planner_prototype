// Contains logic for school days/blocks, block times, and days off.
// Does date/time wrangling.
// TODO: Extract hard coded values.

import java.time.*;
import java.util.*;


public class GAPlanner2122 {

  private static LocalDate firstDayOfSchool = LocalDate.of(2021, Month.SEPTEMBER, 13);
  private static LocalDate lastDayOfSchool = LocalDate.of(2022, Month.JANUARY, 12);
  private static String[] blocks = {"A", "B", "C", "D", "E", "F", "G"};
  private static int[] dayTypes = {1, 2, 3, 4, 5, 6, 7};  // e.g., Day 1 in Planner.

  // Dates where we don't have school.
  private static ArrayList<LocalDate> holidays = new ArrayList<LocalDate>() {
    {
      add(LocalDate.of(2021, Month.SEPTEMBER, 16));
      add(LocalDate.of(2021, Month.OCTOBER, 11));
      add(LocalDate.of(2021, Month.NOVEMBER, 24));
      add(LocalDate.of(2021, Month.NOVEMBER, 25));
      add(LocalDate.of(2021, Month.NOVEMBER, 26));
    }
  };

  // The following are lists of block start times.
  // TODO: Should be a class with start and end times encapsulated.
  private static ArrayList<LocalTime> flexBaseTimes = new ArrayList<LocalTime>() {
    {
      add(LocalTime.of(8, 10));
      add(LocalTime.of(9, 20));
      add(LocalTime.of(11, 05));
      add(LocalTime.of(12, 05));  // Lunch block.
      add(LocalTime.of(13, 40));
    }
  };

  private static ArrayList<LocalTime> friBaseTimes = new ArrayList<LocalTime>() {  // Fri schedule.
    {
      add(LocalTime.of(8, 10));
      add(LocalTime.of(9, 20));
      add(LocalTime.of(10, 30));
      add(LocalTime.of(11, 40));  // Lunch block.
      add(LocalTime.of(13, 15));
    }
  };

  private static ArrayList<LocalTime> wedBaseTimes = new ArrayList<LocalTime>() {  // Wed schedule.
    {
      add(LocalTime.of(9, 10));
      add(LocalTime.of(10, 10));
      add(LocalTime.of(11, 10));
      add(LocalTime.of(12, 10));  // Lunch block.
      add(LocalTime.of(13, 30));
    }
  };

  // TODO: Map dates to special schedules.
  // Keys are LocalDates, values are ArrayLists representing the block times for the day.
  private static TreeMap<LocalDate, ArrayList<LocalTime>> adjustedSchedules = new TreeMap();

  static {
    // Winter break is December 20 to January 2.
    LocalDate firstDayOfBreak = LocalDate.of(2021, Month.DECEMBER, 20);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfBreak.plusDays(i));
    }
  }

  public static boolean isThereClassToday(String dateString, String block, TreeMap schoolDays) {
    ArrayList<String> blocksToday = blocksForDayType((int) schoolDays.get(dateString));
    return blocksToday.contains(block);
  }

  public static int getClassDuration(LocalDate date, String block) {
    TreeMap schoolDays = getSchoolDaysMap();
    ArrayList<String> blocksToday = blocksForDayType((int) schoolDays.get(date.toString()));
    // What blockNum is the given block?
    int blockNum = blocksToday.indexOf(block);

    DayOfWeek day = date.getDayOfWeek();
    boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;

    if (day == DayOfWeek.WEDNESDAY) {
      return 50;  // Shortened schedule because of delayed start.
    } else {
      // Shorter periods on flex days block #2.
      if (blockNum == 2 && flexDay) {
        return 50;
      }
      else {
        return 60;
      }
    }
  }

  public static LocalDateTime dateTimeForBlock(LocalDate date, String block, boolean upperclassmen) {
    TreeMap schoolDays = getSchoolDaysMap();
    ArrayList<String> blocksToday = blocksForDayType((int) schoolDays.get(date.toString()));

    // What blockNum is the given block?
    int blockNum = blocksToday.indexOf(block);
    // If -1, there is no block that day.
    if (blockNum == -1) {
      return null;
    }

    DayOfWeek day = date.getDayOfWeek();
    ArrayList<LocalTime> blockTimes;

    boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;
    if (flexDay) {
      blockTimes = flexBaseTimes;
    } else if (day == DayOfWeek.FRIDAY) {
      blockTimes = friBaseTimes;
    } else {  // Wed schedule
      blockTimes = wedBaseTimes;
    }

    LocalTime startTime = blockTimes.get(blockNum);

    // Adjust for upperclassmen schedule.
    // TODO: Maybe add to static variable set.
    if (upperclassmen) {
      if (blockNum == 3) {  // Lunch block.
        if (day == DayOfWeek.FRIDAY) {
          startTime = LocalTime.of(12, 5);  // Hacky.
        } else {
          startTime = LocalTime.of(12, 30);  // Hacky.
        }
      }
    }
    return LocalDateTime.of(date, startTime);

  }

  public static ArrayList<String> blocksForDayType(int dayType) {
    int numBlocksPerDay = 5;
    ArrayList<String> blocksToday = new ArrayList<String>(numBlocksPerDay);

    int currentBlock = 0;
    for (int i = 1; i < dayTypes.length + 1; i++) {
      blocksToday.clear();
      for (int blockNum = 0; blockNum < numBlocksPerDay; blockNum++) {
        blocksToday.add(blocks[currentBlock % blocks.length]);
        currentBlock = currentBlock + 1 % blocks.length;
      }
      if (i == dayType) {
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
        schoolDays.put(thisDate.toString(), dayTypes[dayTypeIdx]);
        dayTypeIdx++;
        dayTypeIdx = dayTypeIdx % dayTypes.length;
      }
      dayCounter++;
    }
    return schoolDays;
  }

  public static void demo() {
    TreeMap schoolDays = getSchoolDaysMap();
    ArrayList<String> schoolDates = new ArrayList<String>(schoolDays.keySet());

    Scanner sc = new Scanner(System.in);
    System.out.println("Which date? yyyy-mm-dd format:");
    String desiredDate = sc.nextLine();
    System.out.println("Which block?:");
    String desiredBlock = sc.nextLine();
    System.out.println("1 for underclassmen, 2 for upper:");
    int year = sc.nextInt();
    LocalDate desiredDateObj = LocalDate.parse(desiredDate);
    System.out.println("That date is a Day " + schoolDays.get(desiredDate));
    ArrayList<String> blocksToday = blocksForDayType((int) schoolDays.get(desiredDate));
    System.out.println("Blocks that day :" + blocksToday);
    System.out.println("Start time for the block:" + dateTimeForBlock(desiredDateObj, desiredBlock, year == 2));
  }

  public static void main(String[] args) {
    // demo();
  }
}
