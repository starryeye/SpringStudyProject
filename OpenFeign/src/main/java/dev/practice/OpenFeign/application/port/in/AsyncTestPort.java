package dev.practice.OpenFeign.application.port.in;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface AsyncTestPort {

    void asyncMethod();

    Future<String> asyncMethodWithFuture();

    CompletableFuture<String> asyncMethodWithCompletableFuture();
}
