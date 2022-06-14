import datadog.opentracing.DDTracer;
import io.opentracing.Tracer;
import io.opentracing.thrift.SpanProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class MultiplicationServer {

    public static MultiplicationHandler handler;

    public static MultiplicationService.Processor processor;

    static Tracer tracer = new DDTracer.DDTracerBuilder().build();


    public static void main(String [] args) {


        try {
            handler = new MultiplicationHandler();
            processor = new MultiplicationService.Processor(handler);
            TProcessor spanProcessor = new SpanProcessor(processor, tracer);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor, spanProcessor);
                }
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(MultiplicationService.Processor processor, TProcessor spanProcessor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(spanProcessor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
