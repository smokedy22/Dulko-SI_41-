// 3.3 Реализация Factory Method
public class FactoryAfter {
    private static int failedChecks = 0;

    public static void main(String[] args) {
        WagonCreator coveredCreator = new CoveredWagonCreator();
        WagonCreator platformCreator = new PlatformCreator();
        WagonCreator tankCreator = new TankWagonCreator();
        
        // 4-й тип добавляем без правки остальных классов
        WagonCreator hopperCreator = new HopperWagonCreator();

        Wagon wagon1 = coveredCreator.createWagon(68);
        Wagon wagon2 = platformCreator.createWagon(71);
        Wagon wagon3 = tankCreator.createWagon(66);
        Wagon wagon4 = hopperCreator.createWagon(70);

        check("описание крытого", wagon1.description().contains("Крытый") && wagon1.description().contains("68"));
        check("описание платформы", wagon2.description().contains("Платформа") && wagon2.description().contains("71"));
        check("описание цистерны", wagon3.description().contains("Цистерна") && wagon3.description().contains("66"));
        check("тип объекта соответствует создателю", wagon1 instanceof CoveredWagon && wagon4 instanceof HopperWagon);
        check("четвертый тип работает", wagon4.description().contains("Зерновоз") && wagon4.description().contains("70"));

        if (failedChecks > 0) throw new AssertionError("не пройдено: " + failedChecks);
        System.out.println("Все проверки пройдены.");
    }

    static void check(String name, boolean passed) {
        System.out.println((passed ? "[OK]   " : "[FAIL] ") + name);
        if (!passed) failedChecks++;
    }
}

// --- Участники паттерна ---
interface Wagon {
    String description();
    double capacityTons();
}

interface WagonCreator {
    Wagon createWagon(double capacityTons);
}

class CoveredWagon implements Wagon {
    private double cap;
    public CoveredWagon(double cap) { this.cap = cap; }
    public String description() { return "Крытый вагон: грузоподъёмность " + cap + " т, тарные и штучные грузы"; }
    public double capacityTons() { return cap; }
}

class Platform implements Wagon {
    private double cap;
    public Platform(double cap) { this.cap = cap; }
    public String description() { return "Платформа: грузоподъёмность " + cap + " т, длинномерные и тяжеловесные грузы"; }
    public double capacityTons() { return cap; }
}

class TankWagon implements Wagon {
    private double cap;
    public TankWagon(double cap) { this.cap = cap; }
    public String description() { return "Цистерна: грузоподъёмность " + cap + " т, наливные грузы"; }
    public double capacityTons() { return cap; }
}

// Новый 4-й тип
class HopperWagon implements Wagon {
    private double cap;
    public HopperWagon(double cap) { this.cap = cap; }
    public String description() { return "Зерновоз (Хоппер): грузоподъёмность " + cap + " т, сыпучие грузы"; }
    public double capacityTons() { return cap; }
}

class CoveredWagonCreator implements WagonCreator {
    public Wagon createWagon(double cap) { return new CoveredWagon(cap); }
}
class PlatformCreator implements WagonCreator {
    public Wagon createWagon(double cap) { return new Platform(cap); }
}
class TankWagonCreator implements WagonCreator {
    public Wagon createWagon(double cap) { return new TankWagon(cap); }
}
class HopperWagonCreator implements WagonCreator {
    public Wagon createWagon(double cap) { return new HopperWagon(cap); }
}
