package library.server;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

import java.io.IOException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryServer {
    static Logger logger = Logger.getLogger(LibraryServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        Executor virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
        int port = 5000;
        Server server = ServerBuilder.forPort(port)
                .executor(virtualThreadExecutor)
                .addService(ServerInterceptors.intercept(new LibraryServiceImpl(), new LoggingInterceptor()))
                .build();

        logger.log(Level.INFO, () -> "gRPC server started on port " + port);
        server.start();
        server.awaitTermination();
    }
}
