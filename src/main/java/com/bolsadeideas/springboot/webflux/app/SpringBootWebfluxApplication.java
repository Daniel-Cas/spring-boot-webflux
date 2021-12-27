package com.bolsadeideas.springboot.webflux.app;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    @Autowired
    private ProductoDao productoDao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger((SpringBootWebfluxApplication.class));

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection("productos").subscribe();

        Flux.just(
                new Producto("Pantalla",123.21),
                new Producto("Chasis",123.66),
                new Producto("Mouse",98.5),
                new Producto("Teclado",44.3),
                new Producto("Gráfica",794.2)
        )
                .flatMap( producto -> productoDao.save(producto))
                .subscribe( producto -> log.info("Insert: "+ producto.getId() + " " + producto.getNombre()));
    }
}
