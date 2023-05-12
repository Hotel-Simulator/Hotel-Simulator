package pl.agh.edu.generator.client_generator;

import pl.agh.edu.model.ClientGroup;

import java.time.LocalTime;

record Arrival(LocalTime time, ClientGroup clientGroup) implements Comparable<Arrival> {

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