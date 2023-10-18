package pl.agh.edu.management.client.report;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ClientGroupReportHandler {
    private static final LinkedList<ClientGroupReportData> data = new LinkedList<>();
    private static int partialClientGroupNumber = 0;
    private static final Time time = Time.getInstance();
    private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

    private ClientGroupReportHandler() {
    }

    public static void collectData(EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups) {
        partialClientGroupNumber += numberOfClientGroups.values().stream().mapToInt(Integer::intValue).sum();

        if (isLastDayOfMonth()) {
            YearMonth yearMonth = time.getYearMonth();
            int clientGroupNumber = partialClientGroupNumber;
            partialClientGroupNumber = 0;

            timeCommandExecutor.addCommand(
                    new TimeCommand(
                            () -> data.addFirst(generateClientGroupReportData(yearMonth, clientGroupNumber)),
                            time.getTime().truncatedTo(ChronoUnit.DAYS).plusDays(1)
                    )
			);
        }
    }

    public static List<ClientGroupReportData> getData() {
        return data;
    }

    private static boolean isLastDayOfMonth() {
        return time.getTime().plusDays(1).getDayOfMonth() == 1;
    }

    private static ClientGroupReportData generateClientGroupReportData(YearMonth yearMonth, int currentClientNumber) {
        Optional<Integer> optionalLastYearClientGroupNumber = getLastYearClientGroupNumber();
        String yearOnYearChangePercentage = "-";
        String yearOnYearChange = "-";

        if (optionalLastYearClientGroupNumber.isPresent()) {
            int lastYearClientGroupNumber = optionalLastYearClientGroupNumber.get();
            yearOnYearChangePercentage = getYearOnYearChangePercentage(lastYearClientGroupNumber, currentClientNumber);
            yearOnYearChange = getYearOnYearChange(lastYearClientGroupNumber, currentClientNumber);
        }
        return new ClientGroupReportData(
                yearMonth,
                currentClientNumber,
                yearOnYearChangePercentage,
                yearOnYearChange
        );
    }


    private static Optional<Integer> getLastYearClientGroupNumber() {
        if (data.size() < 12) {
            return Optional.empty();
        }
        return Optional.ofNullable(data.get(11).clientGroupNumber());
    }

    private static String getYearOnYearChangePercentage(int previousValue, int currentValue) {
        int percentage = BigDecimal.valueOf((double) currentValue / (double) previousValue)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_EVEN)
                .intValue();

        return percentage < 0 ? percentage + "%" : "+" + percentage + "%";
    }

    private static String getYearOnYearChange(int previousValue, int currentValue) {
        int result = currentValue - previousValue;
        return result < 0 ? String.valueOf(result) : "+" + result;
    }
}
