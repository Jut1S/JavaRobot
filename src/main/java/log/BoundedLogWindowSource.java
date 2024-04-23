package log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * BoundedLogWindowSource представляет собой ограниченную структуру данных для хранения журнальных записей
 * с ограниченным максимальным размером. При достижении максимального размера более старые записи
 * удаляются для размещения новых.
 * <p>
 * Этот класс предоставляет потокобезопасные операции для добавления журнальных записей,
 * регистрации и удаления слушателей изменений журнала и итерации по записям журнала.
 */
public class BoundedLogWindowSource {
    private final int maxSize;
    private final BlockingQueue<LogEntry> queue;
    private final List<LogChangeListener> listeners;


    /**
     * Конструирует BoundedLogWindowSource с указанным максимальным размером.
     *
     * @param maxSize Максимальный размер журнала.
     */
    public BoundedLogWindowSource(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedBlockingQueue<>(maxSize);
        this.listeners = new ArrayList<>();
    }


    /**
     * Регистрирует LogChangeListener для получения уведомлений о изменениях в журнале.
     *
     * @param listener LogChangeListener для регистрации.
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    /**
     * Отменяет регистрацию LogChangeListener.
     *
     * @param listener LogChangeListener для отмены регистрации.
     */
    public void unregisterListener(LogChangeListener listener) {
    synchronized (listeners) {
        listeners.remove(listener);
        }
    }


    /**
     * Добавляет запись журнала с указанным уровнем журнала и сообщением в журнал.
     * Если достигнут максимальный размер, старая запись удаляется.
     *
     * @param logLevel   Уровень журнала записи.
     * @param strMessage Сообщение записи.
     */
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (queue.size() >= maxSize) {
            queue.poll(); // Remove oldest entry
        }
        queue.offer(entry); // Add new entry
        notifyListeners();
    }


    /**
     * Уведомляет всех зарегистрированных слушателей изменений журнала о том, что журнал изменился.
     */
    private void notifyListeners() {
        synchronized (listeners) {
            for (LogChangeListener listener : listeners) {
                listener.onLogChanged();
            }
        }
    }


    /**
     * Возвращает размер журнала.
     *
     * @return Размер журнала.
     */
    public int size() {
        return queue.size();
    }


    /**
     * Возвращает итерируемый диапазон записей журнала, начиная с указанного индекса
     * и содержащий указанное количество записей.
     *
     * @param startFrom Индекс, с которого начинается диапазон.
     * @param count     Количество записей для включения в диапазон.
     * @return Итерируемый диапазон записей журнала.
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        return queue.stream().skip(startFrom).limit(count)::iterator;
    }

    /**
     * Возвращает итерируемый объект, содержащий все записи журнала.
     *
     * @return Итерируемый объект, содержащий все записи журнала.
     */
    public Iterable<LogEntry> all() {
        return queue;
    }
}
