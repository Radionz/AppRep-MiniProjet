/**
 * Created by blanc on 16/05/2016.
 */
public class Event {

    private String key;
    private int timestamp;
    private int requestNb;

    public String getKey() {
        return key;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getRequestNb() {
        return requestNb;
    }

    public Event(String key, int timestamp) {
        this.key = key;
        this.timestamp = timestamp;
        this.requestNb = 0;
    }

    public void wasRequested(int timestamp) {
        this.timestamp = timestamp;
        this.requestNb++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return key != null ? key.equals(event.key) : event.key == null;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
