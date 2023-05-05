package pl.agh.edu.generator.client_generator;

import pl.agh.edu.model.ClientGroup;

import java.time.LocalTime;

class Arrival implements Comparable<Arrival>{

    private final LocalTime time;
    private final ClientGroup clientGroup;

    Arrival(LocalTime time, ClientGroup clientGroup) {
        this.time = time;
        this.clientGroup = clientGroup;
    }

    public LocalTime getTime() {
        return time;
    }

    public ClientGroup getClientGroup() {
        return clientGroup;
    }

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