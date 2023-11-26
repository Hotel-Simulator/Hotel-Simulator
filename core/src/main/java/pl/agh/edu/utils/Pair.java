package pl.agh.edu.utils;

import pl.agh.edu.serialization.KryoConfig;

public record Pair<F, S>(F first, S second) {
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    public static void kryoRegister() {
        KryoConfig.kryo.register(Pair.class);
    }
}
