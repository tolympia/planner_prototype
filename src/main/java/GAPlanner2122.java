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

  // TODO: Map dates to special schedules.
<<<<<<< HEAD
  // Keys are LocalDates, values are Day.
  private static TreeMap<LocalDate, Schedule> adjustedSchedules = new TreeMap();
=======
<<<<<<< Updated upstream
  // Keys are LocalDates, values are ArrayLists representing the block times for the day.
  private static TreeMap<LocalDate, ArrayList<LocalTime>> adjustedSchedules = new TreeMap();
=======
  // Keys are LocalDates, values are Schedules.
  private static TreeMap<LocalDate, Schedule> adjustedSchedules = new TreeMap();
>>>>>>> Stashed changes
>>>>>>> ms

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

  public static ArrayList<LocalDateTime> dateTimeForBlock(LocalDate date, String blockChar, boolean upperclassmen) {
    TreeMap schoolDays = getSchoolDaysMap();
    ArrayList<String> blockCharsToday = blocksForDayType((int) schoolDays.get(date.toString()));

    // What blockNum is the given block?
    int blockNum = blockCharsToday.indexOf(blockChar);
    // If -1, there is no block that day.
    if (blockNum == -1) {
      return null;
    }

    DayOfWeek day = date.getDayOfWeek();

    Schedule schedule;

    boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;
    if (flexDay) {
<<<<<<< HEAD
      schedule = new USFlexDay();
=======
<<<<<<< Updated upstream
      blockTimes = flexBaseTimes;
>>>>>>> ms
    } else if (day == DayOfWeek.FRIDAY) {
      // TODO: Update to USFriday
      schedule = new USFlexDay();
    } else {  // Wed schedule
      schedule = new USWednesday();
    }

    if (upperclassmen) {
<<<<<<< HEAD
      schedule.adjustForUpperclassmen();
=======
      if (blockNum == 3) {  // Lunch block.
        if (day == DayOfWeek.FRIDAY) {
          startTime = LocalTime.of(12, 5);  // Hacky.
        } else {
          startTime = LocalTime.of(12, 30);  // Hacky.
        }
      }
=======
      schedule = new USFlexDay(upperclassmen);
    } else if (day == DayOfWeek.FRIDAY) {
      // TODO: Update to USFriday
      schedule = new USFlexDay(upperclassmen);
    } else {  // Wed schedule
      schedule = new USWednesday(upperclassmen);
>>>>>>> Stashed changes
>>>>>>> ms
    }

    return schedule.getLocalDateTimes(date, blockNum);
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
    ArrayList<LocalDateTime> blockInfo = dateTimeForBlock(desiredDateObj, desiredBlock, year == 2);
    System.out.println("Start time for the block:" + blockInfo.get(0));
  }

  public static void main(String[] args) {
    // demo();
  }
}
