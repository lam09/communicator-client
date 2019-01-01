package SupplierAndConsumer;

import java.util.function.Consumer;


public class TestConsumer {

    /*default Consumer < T > andThen(Consumer<<?super T>after) {
        Objects.requireNonNull(after);
        return (T t) -> {
                accept(t);after.accept(t);
    };
    }*/
    public static void main(String[] args) {
        Consumer< String > consumer1 = (arg) -> {
                System.out.println(arg + "OK");
        };
        consumer1.accept("TestConsumerAccept - ");
        Consumer < String > consumer2 = (x) -> {
                System.out.println(x + "OK!!!");
        };
        consumer1.andThen(consumer2).accept("TestConsumerAfterThen - ");
    }
}
