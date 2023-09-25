package pl.agh.edu.utils;

public record EnumPair<F extends Enum<F>, S extends Enum<S>>(F first, S second) {
}
