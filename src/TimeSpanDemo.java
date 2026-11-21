
import exceptions.IllegalInputTimeSpanException;
import exceptions.IllegalScaleFactorException;
import exceptions.NegativeTimeSpanException;
import exceptions.TimeSpanException;
import exceptions.TimeSpanOverflowException;

/**
 * Демонстрація роботи класу TimeSpan (з лаб. №5) та обробки виключних ситуацій.
 */
public class TimeSpanDemo {

    public static void main(String[] args) {
        // ----- звичайна робота, як у лаб. №5 -----
        TimeSpan first = new TimeSpan();
        TimeSpan second = new TimeSpan(150);
        TimeSpan third = new TimeSpan(2, 30);
        TimeSpan copy = new TimeSpan(third);

        third.add(1, 45);
        third.add(30);
        third.add(second);
        third.subtract(0, 15);
        third.subtract(20);
        third.subtract(copy);
        third.scale(2);

        System.out.println("first: " + first);
        System.out.println("second: " + second);
        System.out.println("copy: " + copy);
        System.out.println("third: " + third);
        System.out.println("Всього годин: " + third.getTotalHours());
        System.out.println("Всього хвилин: " + third.getTotalMinutes());
        System.out.println();

        // ----- виключні ситуації -----
        TimeSpan span = new TimeSpan(1, 30);

        // некоректний вхідний інтервал
        try {
            new TimeSpan(2, 75);
        } catch (IllegalInputTimeSpanException e) {
            printError(e);
        }

        try {
            span.add(-1, 10);
        } catch (IllegalInputTimeSpanException e) {
            printError(e);
        }

        try {
            new TimeSpan(-5);
        } catch (IllegalInputTimeSpanException e) {
            printError(e);
        }

        // інтервал став би від'ємним (об'єкт не змінюється)
        try {
            span.subtract(new TimeSpan(3, 0));
        } catch (NegativeTimeSpanException e) {
            printError(e);
        }
        System.out.println("span after failed subtract: " + span);

        // некоректний множник
        try {
            span.scale(0);
        } catch (IllegalScaleFactorException e) {
            printError(e);
        }

        // переповнення на дуже великих значеннях
        try {
            new TimeSpan(Integer.MAX_VALUE).scale(2);
        } catch (TimeSpanOverflowException e) {
            printError(e);
        }

        // перехоплення через базовий клас (NullTimeSpanException)
        try {
            span.add((TimeSpan) null);
        } catch (TimeSpanException e) {
            printError(e);
        }
    }

    private static void printError(RuntimeException e) {
        System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
}
