import datadog.opentracing.DDTracer;
import datadog.trace.api.DDTags;
import datadog.trace.api.GlobalTracer;
import io.opentracing.*;

public class Minimal {

    public static void main(String[] args) throws InterruptedException {

        Tracer tracer = new DDTracer.DDTracerBuilder().build();

        Span span = tracer.buildSpan("servlet.request").start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            span.setTag(DDTags.SERVICE_NAME, "javaopenthrift");
            span.setTag(DDTags.RESOURCE_NAME, "GET /callme");
            span.setTag(DDTags.SPAN_TYPE, "web");
            try {
                System.out.println("Tracing randomly");
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            span.finish();
        }
        Thread.sleep(2000L);

    }

}
