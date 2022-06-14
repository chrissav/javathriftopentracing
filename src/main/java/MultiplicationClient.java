import datadog.opentracing.DDTracer;
import datadog.trace.api.DDTags;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.thrift.SpanProtocol;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class MultiplicationClient {

    static Tracer tracer = new DDTracer.DDTracerBuilder().build();

    public static void main(String [] args) {


        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
            TProtocol spanProtocol = new SpanProtocol(protocol, tracer);
            MultiplicationService.Client client = new MultiplicationService.Client(spanProtocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(MultiplicationService.Client client) throws TException
    {

        Span span = tracer.buildSpan("servlet.request").start();
        try(Scope scope = tracer.scopeManager().activate(span)){
            span.setTag(DDTags.SERVICE_NAME, "basicservice");
            span.setTag(DDTags.RESOURCE_NAME, "GET /callme");
            span.setTag(DDTags.SPAN_TYPE, "web");
            try {
                System.out.println("Tracing randomly");
                int product = client.multiply(3,5);
                System.out.println("3*5=" + product);
                Thread.sleep(20);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            span.finish();
        }

    }
}
