// Contains logic for school days/blocks, block times, and days off.
// Does date/time wrangling.
// TODO: Extract hard coded values.

import java.time.*;
import java.util.*;
import java.util.ArrayList;

public class GAPlanner {

  private static LocalDate firstDayOfSchool = LocalDate.of(2021, Month.JANUARY, 20);
  private static LocalDate lastDayOfSchool = LocalDate.of(2021, Month.MAY, 26);
  private static String[] blocks = {"A", "B", "C", "D", "E", "F", "G"};
  private static int[] dayTypes = {1, 2, 3, 4, 5, 6, 7};  // e.g., Day 1 in Planner.

  // Dates where we don't have school.
  private static ArrayList<LocalDate> holidays = new ArrayList<LocalDate>() {
    {
      add(LocalDate.of(2020, Month.SEPTEMBER, 7));
      add(LocalDate.of(2020, Month.SEPTEMBER, 28));
      add(LocalDate.of(2020, Month.OCTOBER, 12));
      add(LocalDate.of(2020, Month.NOVEMBER, 25));
      add(LocalDate.of(2020, Month.NOVEMBER, 26));
      add(LocalDate.of(2020, Month.NOVEMBER, 27));
      // add(LocalDate.of(2020, Month.NOVEMBER, 30));
      add(LocalDate.of(2021, Month.FEBRUARY, 11));
      add(LocalDate.of(2021, Month.FEBRUARY, 12));
      add(LocalDate.of(2021, Month.FEBRUARY, 15));
      add(LocalDate.of(2021, Month.MARCH, 15));
      add(LocalDate.of(2021, Month.MARCH, 16));
      add(LocalDate.of(2021, Month.MARCH, 17));
      add(LocalDate.of(2021, Month.MARCH, 18));
      add(LocalDate.of(2021, Month.MARCH, 19));
      add(LocalDate.of(2021, Month.MARCH, 22));
      add(LocalDate.of(2021, Month.MARCH, 23));
      add(LocalDate.of(2021, Month.MARCH, 24));
      add(LocalDate.of(2021, Month.MARCH, 25));
      add(LocalDate.of(2021, Month.MARCH, 26));
      add(LocalDate.of(2021, Month.APRIL, 2));
      add(LocalDate.of(2021, Month.APRIL, 23));
    }
  };

  public static boolean isThereClassToday(String dateString, String block, TreeMap schoolDays) {
    ArrayList<String> blocksToday = blocksForDayType((int) schoolDays.get(dateString));
    return blocksToday.contains(block);
  }

  public static int getClassDuration(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    if (day == DayOfWeek.WEDNESDAY) {
      return 40;  // Shortened schedule because of Covid.
    } else {
      return 50;
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
    LocalTime startTime = timeForBlockStart(date, blockNum, upperclassmen);
    return LocalDateTime.of(date, startTime);
  }

  private static LocalTime timeForBlockStart(LocalDate date, int blockNum, boolean upperclassmen) {
    int upperclassmenDelay = 10;  // Minutes.
    DayOfWeek day = date.getDayOfWeek();

    ArrayList<LocalTime> blockTimes;
    boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;
    if (flexDay) {
      blockTimes = new ArrayList<LocalTime>() {
        {
          add(LocalTime.of(8, 15));
          add(LocalTime.of(9, 20));
          add(LocalTime.of(11, 00));
          add(LocalTime.of(12, 05));  // Lunch block.
          add(LocalTime.of(13, 45));
        }
      };
    }
    else {
      blockTimes = new ArrayList<LocalTime>() {  // Wed/Fri schedule.
        {
          add(LocalTime.of(8, 15));
          add(LocalTime.of(9, 20));
          add(LocalTime.of(10, 25));
          add(LocalTime.of(11, 30));  // Lunch block.
          add(LocalTime.of(13, 10));
        }
      };
    };

    LocalTime classTime = blockTimes.get(blockNum);
    // Adjust for upperclassmen schedule.
    if (upperclassmen) {
      if (blockNum == 3) {  // Lunch block.
        if (flexDay) {
          classTime = LocalTime.of(12, 50);  // Hacky.
        } else {  // Not a flex day.
          classTime = LocalTime.of(12, 15);  // Hacky.
        }
      } else {
        classTime = classTime.plusMinutes(upperclassmenDelay);
      }
    }

    LocalTime advisory;
    if (day == DayOfWeek.WEDNESDAY) {  // Late start days.
      advisory = LocalTime.of(8, 40);
      classTime = classTime.plusMinutes(50 - (blockNum * 10));
    } else {
      advisory = LocalTime.of(7, 50);
    }
    return classTime;
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
