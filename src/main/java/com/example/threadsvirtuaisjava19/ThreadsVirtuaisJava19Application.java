package com.example.threadsvirtuaisjava19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@SpringBootApplication
@EnableFeignClients
public class ThreadsVirtuaisJava19Application {

	public static void main(String[] args) throws InterruptedException {

		ConfigurableApplicationContext ctx = SpringApplication
				.run(ThreadsVirtuaisJava19Application.class, args);

		Map<String, Function<ApplicationContext, String>> rotas = new HashMap<>();
		rotas.put("/significado", contexto -> {
			DicionarioRemoto dicionarioRemoto = ctx.getBean(DicionarioRemoto.class);
			return dicionarioRemoto.explica("java");
		});

		rotas.put("/chucknorris", contexto -> {
			ChuckNorrisFacts chuckNorrisFacts = ctx.getBean(ChuckNorrisFacts.class);
			return chuckNorrisFacts.frase();
		});

		rotas.put("memoria", contexto -> (2 * 2) + "");

//		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
//		ExecutorService executor = Executors.newFixedThreadPool(4);
		ExecutorService executor = Executors.newWorkStealingPool();

		Servidor servidor = new Servidor(ctx, rotas, executor);
		List<String> listaRotas = List.of("/significado","/chucknorris","/memoria");

		for (int i=0; i<10; i++){
			for (String rota : listaRotas) {
				servidor.executa(rota, resposta -> {
					System.out.println(Thread.currentThread().getName() + ";Rota:" + rota + "=>" + resposta);
				});
			}
		}

		executor.shutdown();
		System.out.println("Esperando acabar tudo...");
		while (!executor.isTerminated()){
		}

		System.out.println("Finalizado!");
	}

}
