package library.server;

import io.grpc.*;

import java.util.Arrays;
import java.util.logging.Logger;

public class LoggingInterceptor implements ServerInterceptor {
    private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler) {

        Thread currentThread = Thread.currentThread();
        for (String s : Arrays.asList(String.format("Call handled by thread [%s] - ThreadId: %d - IsVirtual: %b",
                currentThread.getName(),
                currentThread.threadId(),
                currentThread.isVirtual()), "Method: " + serverCall.getMethodDescriptor().getFullMethodName())) {
            logger.info(s);
        }

        return serverCallHandler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<>(serverCall) {
            @Override
            public void sendMessage(RespT message) {
                Thread responseThread = Thread.currentThread();
                logger.info(String.format("Response thread [%s] - ThreadId: %d - IsVirtual: %b",
                        responseThread.getName(),
                        responseThread.threadId(),
                        responseThread.isVirtual()));
                super.sendMessage(message);
            }
        }, metadata);
    }
}