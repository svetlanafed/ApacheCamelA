package com.svetlanafedorova.apachecamela.routes;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class TestTimerRouter extends RouteBuilder {

    private final GetCurrentTimeBean getCurrentTimeBean;
    private final SimpleLoggingProcessingComponent logger;

    @Override
    public void configure() throws Exception {
        //queue (timer)
        //transformation
        //database (log)

        from("timer: my-timer")//(queue) - null
                //.transform().constant("My constant message")
                .transform().constant("Time now is: " + LocalDateTime.now())
                .bean(logger)
                .bean(getCurrentTimeBean)
                .process(new SimpleLoggingProcessor())
                .to("log: my-log");//(database)

        //processing - doesn't change body
        //transformation - changes body
    }
}

@Component
@Slf4j
class SimpleLoggingProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        log.error(exchange.getMessage().getBody().toString());
    }
}

@Component
class GetCurrentTimeBean {

    public String getCurrentTime() {
        return "Time now is: " + LocalDateTime.now();
    }
}

@Component
@Slf4j
class SimpleLoggingProcessingComponent {

    public void process(String messageBody) {
        log.info(messageBody);
    }
}
