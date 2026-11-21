
import exceptions.IllegalInputTimeSpanException;
import exceptions.IllegalScaleFactorException;
import exceptions.NegativeTimeSpanException;
import exceptions.NullTimeSpanException;
import exceptions.TimeSpanOverflowException;

/**
 * Клас TimeSpan з лабораторної роботи №5 (часовий інтервал у годинах та хвилинах),
 * доповнений обробкою виключних ситуацій за допомогою власних класів виключень:
 * <ul>
 *     <li>{@link IllegalInputTimeSpanException} - некоректний вхідний інтервал;</li>
 *     <li>{@link NegativeTimeSpanException} - інтервал став би від'ємним;</li>
 *     <li>{@link IllegalScaleFactorException} - некоректний множник у scale();</li>
 *     <li>{@link NullTimeSpanException} - замість TimeSpan передано null (додаткове);</li>
 *     <li>{@link TimeSpanOverflowException} - переповнення на дуже великих значеннях (додаткове).</li>
 * </ul>
 */
public class TimeSpan {
    private int hours;
    private int minutes;

    public TimeSpan() {
        this(0, 0);
    }

    public TimeSpan(int minutes) {
        validateMinutes(minutes);
        setFromTotalMinutes(minutes);
    }

    public TimeSpan(int hours, int minutes) {
        validateHoursAndMinutes(hours, minutes);
        toTotalMinutes(hours, minutes); // перевірка на переповнення
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpan(TimeSpan span) {
        validateNotNull(span);
        this.hours = span.getHours();
        this.minutes = span.getMinutes();
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void add(int hours, int minutes) {
        validateHoursAndMinutes(hours, minutes);
        setFromTotalMinutes(safeAdd(getTotalMinutes(), toTotalMinutes(hours, minutes)));
    }

    public void add(int minutes) {
        validateMinutes(minutes);
        setFromTotalMinutes(safeAdd(getTotalMinutes(), minutes));
    }

    public void add(TimeSpan span) {
        validateNotNull(span);
        add(span.getHours(), span.getMinutes());
    }

    /**
     * Віднімає години та хвилини.
     * Якщо віднімається більше, ніж містить інтервал, викидається
     * {@link NegativeTimeSpanException}, а поточний об'єкт не змінюється.
     */
    public void subtract(int hours, int minutes) {
        validateHoursAndMinutes(hours, minutes);
        subtractTotal(toTotalMinutes(hours, minutes));
    }

    public void subtract(int minutes) {
        validateMinutes(minutes);
        subtractTotal(minutes);
    }

    public void subtract(TimeSpan span) {
        validateNotNull(span);
        subtract(span.getHours(), span.getMinutes());
    }

    public double getTotalHours() {
        return getTotalMinutes() / 60.0;
    }

    public int getTotalMinutes() {
        return hours * 60 + minutes;
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException(
                    "Scale factor must be greater than 0, got: " + factor);
        }
        try {
            setFromTotalMinutes(Math.multiplyExact(getTotalMinutes(), factor));
        } catch (ArithmeticException e) {
            throw new TimeSpanOverflowException("Time span is too large after scaling by " + factor);
        }
    }

    // ---------- допоміжні методи ----------

    private void subtractTotal(int amount) {
        if (amount > getTotalMinutes()) {
            throw new NegativeTimeSpanException(
                    "Cannot subtract " + amount + " min from " + this + ": time span would become negative");
        }
        setFromTotalMinutes(getTotalMinutes() - amount);
    }

    private static void validateHoursAndMinutes(int hours, int minutes) {
        if (hours < 0 || minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Hours and minutes cannot be negative: " + hours + "h " + minutes + "m");
        }
        if (minutes > 59) {
            throw new IllegalInputTimeSpanException("Minutes cannot exceed 59: " + minutes);
        }
    }

    private static void validateMinutes(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Minutes cannot be negative: " + minutes);
        }
    }

    private static void validateNotNull(TimeSpan span) {
        if (span == null) {
            throw new NullTimeSpanException("TimeSpan cannot be null");
        }
    }

    /** Загальна кількість хвилин з перевіркою переповнення int. */
    private static int toTotalMinutes(int hours, int minutes) {
        try {
            return Math.addExact(Math.multiplyExact(hours, 60), minutes);
        } catch (ArithmeticException e) {
            throw new TimeSpanOverflowException("Time span is too large: " + hours + "h " + minutes + "m");
        }
    }

    private static int safeAdd(int a, int b) {
        try {
            return Math.addExact(a, b);
        } catch (ArithmeticException e) {
            throw new TimeSpanOverflowException("Time span is too large after adding");
        }
    }

    private void setFromTotalMinutes(int totalMinutes) {
        if (totalMinutes < 0) {
            throw new NegativeTimeSpanException("Time span cannot be negative: " + totalMinutes + " min");
        }
        hours = totalMinutes / 60;
        minutes = totalMinutes % 60;
    }

    @Override
    public String toString() {
        return hours + " год. " + minutes + " хв.";
    }
}
