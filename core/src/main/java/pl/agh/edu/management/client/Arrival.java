package pl.agh.edu.management.client;

import java.time.LocalTime;

import pl.agh.edu.model.client.ClientGroup;

public record Arrival(LocalTime time, ClientGroup clientGroup) implements Comparable<Arrival> {

    @Override
    public int compareTo(Arrival o) {
        return this.time.compareTo(o.time);
    }

    @Override
    public String toString() {
        return "Arrival{" +
                "time=" + time +
                ", clientGroup=" + clientGroup +
                '}';
    }
}