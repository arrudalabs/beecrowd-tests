package io.arrudalabs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Beecrowd1021Test {

    @ParameterizedTest
    @MethodSource("testArgs")
    void test(double quantiaParaTeste,
              Map<Double, Integer> decomposicaoEsperada) {
        // Given
        var quantia = new ValorMotario(quantiaParaTeste);
        var notasEMoedas = new double[]{100.00, 50.00, 20.00, 10.00, 5.00, 2.00, 1.00, 0.50, 0.25, 0.10, 0.05, 0.01};
        // When
        var notasEmEspecie = quantia.decompor(notasEMoedas);
        // Then
        for (var notaOuMoeda : notasEMoedas) {
            assertEquals(decomposicaoEsperada.get(notaOuMoeda), notasEmEspecie.get(notaOuMoeda));
        }
    }

    public static Stream<Arguments> testArgs() {
        return Stream.of(
                Arguments.arguments(
                        576.73,
                        new HashMap<Double, Integer>() {
                            {
                                put(100.00, 5);
                                put(50.00, 1);
                                put(20.00, 1);
                                put(10.00, 0);
                                put(5.00, 1);
                                put(2.00, 0);
                                put(1.00, 1);
                                put(0.50, 1);
                                put(0.25, 0);
                                put(0.10, 2);
                                put(0.05, 0);
                                put(0.01, 3);
                            }
                        }
                ),
                Arguments.arguments(
                        4.00,
                        new HashMap<Double, Integer>() {
                            {
                                put(100.00, 0);
                                put(50.00, 0);
                                put(20.00, 0);
                                put(10.00, 0);
                                put(5.00, 0);
                                put(2.00, 2);
                                put(1.00, 0);
                                put(0.50, 0);
                                put(0.25, 0);
                                put(0.10, 0);
                                put(0.05, 0);
                                put(0.01, 0);
                            }
                        }
                ),
                Arguments.arguments(
                        91.01,
                        new HashMap<Double, Integer>() {
                            {
                                put(100.00, 0);
                                put(50.00, 1);
                                put(20.00, 2);
                                put(10.00, 0);
                                put(5.00, 0);
                                put(2.00, 0);
                                put(1.00, 1);
                                put(0.50, 0);
                                put(0.25, 0);
                                put(0.10, 0);
                                put(0.05, 0);
                                put(0.01, 1);
                            }
                        }
                )
        );
    }

}

record ValorMotario(double quantia) {

    Map<Double, Integer> decompor(double... notas) {

        var especies = new LinkedHashMap<Double, Integer>();
        var quantiaCalculavel = new AtomicReference<Double>(quantia);

//        for (var nota : notas) {
//            Map<Double, Integer> especiesPorNota = this.decomporPorSubQuantia(quantiaCalculavel.get(), nota);
//            especies.putAll(especiesPorNota);
//            quantiaCalculavel
//                    .getAndUpdate(valorAtual -> {
//                        return valorAtual - especiesPorNota.entrySet()
//                                .stream()
//                                .map(entry ->
//                                        entry.getKey() * entry.getValue())
//                                .reduce(new Double(0.00), (valor1, valor2) -> {
//                                    return valor1 + valor2;
//                                });
//                    });
//        }

        Arrays.stream(notas)
                .forEach(nota -> {
                    Map<Double, Integer> especiesPorNota = this.decomporPorSubQuantia(quantiaCalculavel.get(), nota);
                    especies.putAll(especiesPorNota);
                    quantiaCalculavel
                            .getAndUpdate(valorAtual -> {
                                return valorAtual - especiesPorNota.entrySet()
                                        .stream()
                                        .map(entry ->
                                                entry.getKey() * entry.getValue())
                                        .reduce(new Double(0.00), (valor1, valor2) -> {
                                            return valor1 + valor2;
                                        });
                            });
                });

        return especies;
    }

    private Map<Double, Integer> decomporPorSubQuantia(double quantia, double subQuantia) {
        int qtdeNotas = (int) (quantia / subQuantia);
        return Map.of(subQuantia, qtdeNotas);
    }

}


