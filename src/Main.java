import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;

public class Main {
    private static final Schedule SCHEDULE = new Schedule();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SCHEDULE.addTask(new SingleTask("Test", "Test", LocalDateTime.now(), TaskType.PERSONAL));
        SCHEDULE.addTask(new WeeklyTask("Test", "Test", LocalDateTime.now(), TaskType.PERSONAL));
        SCHEDULE.addTask(new MonthlyTask("Test", "Test", LocalDateTime.now(), TaskType.PERSONAL));
        SCHEDULE.addTask(new YearlyTask("Test", "Test", LocalDateTime.now(), TaskType.PERSONAL));
        SCHEDULE.addTask(new DailyTask("Test", "Test", LocalDateTime.now(), TaskType.PERSONAL));
        removeTasks(scanner);


    }

    private static void addTask(Scanner scanner) throws TaskNotFoundException {
        String title = readString("Введите название задачи:", scanner);
        String description = readString("Введите описание задачи:", scanner);
        LocalDateTime taskDate = readDateTime(scanner);
        TaskType taskType = readType(scanner);
        Repeatability repeatability = readRepeatability(scanner);
        Task task = switch (repeatability) {

            case SINGLE -> new SingleTask(title, description, taskDate, taskType);
            case DAILY -> new DailyTask(title, description, taskDate, taskType);
            case WEEKLY -> new WeeklyTask(title, description, taskDate, taskType);
            case MONTHLY -> new MonthlyTask(title, description, taskDate, taskType);
            case YEARLY -> new YearlyTask(title, description, taskDate, taskType);
        };
        SCHEDULE.addTask(task);
    }

    private static TaskType readType(Scanner scanner) throws TaskNotFoundException {
        while (true) {
            try {
                System.out.println("Выберете тип задачи:");
                for (TaskType taskType : TaskType.values()) {
                    System.out.println(taskType.ordinal() + "." + localizeType(taskType));
                }
                System.out.print("Выберите тип:");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return TaskType.values()[ordinal];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Введен неверный номер типа задачи");
            }
        }
    }

    private static Repeatability readRepeatability(Scanner scanner) throws TaskNotFoundException {
        while (true) {
            try {
                System.out.println("Выберете тип повторяемости задачи:");
                for (Repeatability repeatability : Repeatability.values()) {
                    System.out.println(repeatability.ordinal() + "." + localizeRepeatability(repeatability));
                }
                System.out.println("Выберите тип:");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return Repeatability.values()[ordinal];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Введен неверный номер типа задачи");
            }
        }
    }

    private static LocalDateTime readDateTime(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        LocalTime localTime = readTime(scanner);
        return localDate.atTime(localTime);
    }

    private static String readString(String message, Scanner scanner) {
        while (true) {

            System.out.println(message);
            String readString = scanner.nextLine();
            if (readString == null || readString.isBlank()) {
                System.out.println("Введено пустое значение.");
            } else {
                return readString;
            }
        }
    }

    private static void removeTasks(Scanner scanner) {
        System.out.println("Все задачи.");
        for (Task task : SCHEDULE.getAllTask()) {
            System.out.printf("%d.%s [%s] (%s)%n",
                    task.getId(),
                    task.getTitle(),
                    localizeType(task.getTaskType()),
                    localizeRepeatability(task.getRepeatabilityType()));
        }
        while (true) {
            try {
                System.out.println("Выберете задачу для удаления:");
                String idLine = scanner.nextLine();
                int id = Integer.parseInt(idLine);
                SCHEDULE.removeTask(id);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Введен неверный id");
            } catch (TaskNotFoundException e) {
                System.out.println("Задача для удаления не найдена");
            }

        }
    }

    private static void printTaskForDate(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        Collection<Task> tasksForDate = SCHEDULE.getTaskForDate(localDate);
        System.out.println("Задачи на " + localDate.format(DATE_FORMAT));
        for (Task task : tasksForDate) {
            System.out.printf("[%s],%s:%s(%s)%n", localizeType(task.getTaskType()),
                    task.getTitle(),
                    task.getTaskDataTime().format(TIME_FORMAT),
                    task.getDescription());
        }

    }

    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите дату задачи  в формате dd.MM.yyyy:");
                String dateLine = scanner.nextLine();
                return LocalDate.parse(dateLine, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Введена дата в неверном формате");
            }
        }
    }

    private static LocalTime readTime(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите время задачи  в формате hh.mm");
                String dateLine = scanner.nextLine();
                return LocalTime.parse(dateLine, TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Введена дата в неверном формате");
            }
        }
    }

    private static String localizeType(TaskType taskType) {
        return switch (taskType) {
            case WORK -> "Рабочая задача";
            case PERSONAL -> "Персональная задача";
            default -> "еизвестная задача";
        };
    }

    private static String localizeRepeatability(Repeatability repeatability) {
        return switch (repeatability) {

            case SINGLE -> "Разовая";
            case DAILY -> "Ежедневная";
            case WEEKLY -> "Еженедельная";
            case MONTHLY -> "Ежемесячная";
            case YEARLY -> "Ежегодная";
        };
    }

}






