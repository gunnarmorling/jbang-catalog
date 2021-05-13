//DEPS io.vertx:vertx-web:4.0.3
//DEPS com.beust:jcommander:1.81

import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import com.beust.jcommander.*;

import java.io.File;
import java.util.concurrent.Future;

class web {

    @Parameter(names={"--port", "-p"})
    int port = 8080;
    @Parameter(names={"--root", "-r"})
    String path = ".";

    public static void main(String... args) throws Exception {
        web main = new web();

        JCommander.newBuilder()
            .addObject(main)
            .build()
            .parse(args);

        main.run();
    }

    private void run() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new StaticVerticle());
        System.out.println("Serving contents of " + new File(path).toPath().toAbsolutePath().normalize() + " on port " + port);
    }

    public class StaticVerticle extends AbstractVerticle {

        @Override
        public void start(Promise<Void> future) {
            HttpServer server = vertx.createHttpServer();

            Router router = Router.router(vertx);
            router.route("/*").handler(StaticHandler.create(path));
            server.requestHandler(router).listen(port);
        }
    }
}
