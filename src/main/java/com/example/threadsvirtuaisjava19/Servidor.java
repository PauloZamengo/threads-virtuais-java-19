package com.example.threadsvirtuaisjava19;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

public class Servidor {

    private ConfigurableApplicationContext ctx;
    private Map<String, Function<ApplicationContext, String>> rotas;
    ExecutorService executorService;

    public Servidor(ConfigurableApplicationContext ctx,
                    Map<String, Function<ApplicationContext, String>> rotas,
                    ExecutorService executorService){
        this.ctx = ctx;
        this.rotas = rotas;
        this.executorService = executorService;
    }

    public void executa(String rota, Consumer<String> canalResposta) {
        executorService.execute(() -> canalResposta.accept(rotas.get(rota).apply(ctx)));

    }
}
