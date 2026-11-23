// 4.3 Реализация Observer
import java.util.ArrayList;
import java.util.List;

public class ObserverAfter {
    private static int failedChecks = 0;

    public static void main(String[] args) {
        Station station = new Station("Гомель");
        Journal journal = new Journal();
        Board board = new Board();
        Mailing mailing = new Mailing();

        station.addListener(board);
        station.addListener(journal);
        station.addListener(mailing);
        
        station.trainArrived("3022");
        check("Журнал получил 1 запись", journal.getRecordsCount() == 1);
        check("Табло и рассылка тоже сработали", board.lastMessage != null && mailing.lastMessage != null);

        station.removeListener(journal);
        station.trainArrived("6701");
        check("После отписки журнал не пишет", journal.getRecordsCount() == 1);

        // 4-й наблюдатель
        AsuListener asu = new AsuListener();
        station.addListener(asu);
        station.trainArrived("9999");
        check("4-й наблюдатель работает", asu.received);

        if (failedChecks > 0) throw new AssertionError("не пройдено: " + failedChecks);
        System.out.println("Все проверки пройдены.");
    }

    static void check(String name, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + name);
        if (!passed) failedChecks++;
    }
}

// --- Участники паттерна ---
interface ArrivalListener {
    void onTrainArrived(String stationName, String trainNumber);
}

class Station {
    private String name;
    private List<ArrivalListener> listeners = new ArrayList<>();

    public Station(String name) { this.name = name; }
    public void addListener(ArrivalListener l) { listeners.add(l); }
    public void removeListener(ArrivalListener l) { listeners.remove(l); }

    public void trainArrived(String trainNumber) {
        for (ArrivalListener l : listeners) {
            l.onTrainArrived(name, trainNumber);
        }
    }
}

class Board implements ArrivalListener {
    public String lastMessage = null;
    public void onTrainArrived(String stationName, String trainNumber) {
        lastMessage = "[табло] Поезд " + trainNumber + " прибыл на " + stationName;
        System.out.println(lastMessage);
    }
}

class Journal implements ArrivalListener {
    private List<String> records = new ArrayList<>();
    public void onTrainArrived(String stationName, String trainNumber) {
        records.add(stationName + ":" + trainNumber);
        System.out.println("[журнал] Записано событие.");
    }
    public int getRecordsCount() { return records.size(); }
}

class Mailing implements ArrivalListener {
    public String lastMessage = null;
    public void onTrainArrived(String stationName, String trainNumber) {
        lastMessage = "Рассылка: поезд " + trainNumber;
        System.out.println("[рассылка] " + lastMessage);
    }
}

// 4-й наблюдатель
class AsuListener implements ArrivalListener {
    public boolean received = false;
    public void onTrainArrived(String stationName, String trainNumber) {
        received = true;
        System.out.println("[АСУ] Данные переданы в систему.");
    }
}
