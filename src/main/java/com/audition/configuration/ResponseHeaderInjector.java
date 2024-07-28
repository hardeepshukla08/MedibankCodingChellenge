package com.audition.configuration;
import io.opentelemetry.api.trace.Span;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ResponseHeaderInjector implements HandlerInterceptor {


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {

        Span currentSpan = Span.current();

        // Get traceId and spanId from the current span
        String traceId = currentSpan.getSpanContext().getTraceId();
        String spanId = currentSpan.getSpanContext().getSpanId();

        // Add traceId and spanId to response headers
        response.addHeader("X-Trace-Id", traceId);
        response.addHeader("X-Span-Id", spanId);
    }

    /*private final Tracer tracer;

    public ResponseHeaderInjector(TracerProvider tracerProvider) {
        this.tracer = tracerProvider.get("com.audition.configuration");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Span currentSpan = Span.current();
        if (currentSpan != null) {
            SpanContext spanContext = currentSpan.getSpanContext();
            response.setHeader("trace-id", spanContext.getTraceId());
            response.setHeader("span-id", spanContext.getSpanId());
        }
        return true;
    }*/

}
