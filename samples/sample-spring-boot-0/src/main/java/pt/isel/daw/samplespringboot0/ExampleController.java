package pt.isel.daw.samplespringboot0;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Summary;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import biweekly.util.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/examples")
public class ExampleController {

    private static final Logger log = LoggerFactory.getLogger(ExampleController.class);

    private final List<HttpMessageConverter<?>> messageConverterList;

    public ExampleController(List<HttpMessageConverter<?>> messageConverterList) {

        this.messageConverterList = messageConverterList;
    }


    @GetMapping("/1/{id}")
    public String get1(@PathVariable("id") int id) {

        return "The request id was " + id;
    }

    @GetMapping("/2/{id}")
    public String get2(
            @PathVariable("id") int id,
            @RequestParam("name") String name) {
        return String.format("The request id was %d and name was %s", id, name);
    }

    @GetMapping("/3/{id}")
    public String get3(
            @PathVariable("id") int id,
            @RequestParam("name") Optional<String> name) {
        return String.format("The request id was %d and name was %s",
                id,
                name.orElse("absent"));
    }

    @GetMapping("/4")
    public String get4(
            @RequestParam MultiValueMap<String, String> prms) {
        return prms.entrySet().stream()
                .map(e -> e.getKey() + "->" + e.getValue())
                .collect(Collectors.joining());
    }

    private static class OutputModel5{
        public int i = 42;
        public String s = "hello";
    }

    @GetMapping("/5")
    public OutputModel5 get5() {
        return new OutputModel5();
    }

    private static class OutputModel6{
       public OutputModel5 field1;
       public OutputModel5 field2 = new OutputModel5();
       public OutputModel5[] field3 =  {new OutputModel5()};
    }

    @GetMapping("/6")
    public OutputModel6 get6() {
        return new OutputModel6();
    }

    private static class OutputModel7{
        // JsonIgnore is a Jackson annotation
        @JsonIgnore
        public OutputModel5 field1;
        public OutputModel5 field2 = new OutputModel5();
        public OutputModel5[] field3 =  {new OutputModel5()};
    }

    @GetMapping("/7")
    public OutputModel7 get7() {
        return new OutputModel7();
    }

    @GetMapping(path="/8", produces="application/xml")
    public OutputModel7 get8() {
        return new OutputModel7();
    }

    @GetMapping(path="/8.1", produces="application/json")
    public OutputModel7 get81() {
        return new OutputModel7();
    }

    @GetMapping(path="/9", produces="text/calendar")
    @RequiresAuthentication
    public ResponseEntity<ICalendar> get9() {
        ICalendar ical = new ICalendar();
        VEvent event = new VEvent();
        Summary summary = event.setSummary("Aula de DAW");
        summary.setLanguage("pt-pt");

        Date start = Date.from(Instant.parse("2018-03-06T14:30:00.00Z"));
        event.setDateStart(start);

        Duration duration = new Duration.Builder().minutes(90).build();
        event.setDuration(duration);

        Recurrence recur = new Recurrence.Builder(Frequency.WEEKLY)
                .interval(1)
                .count(13)
                .build();
        event.setRecurrenceRule(recur);
        ical.addEvent(event);

        HttpHeaders headers = new HttpHeaders();
        headers.add("My-Header", "My-Value");
        ResponseEntity<ICalendar> res =
                new ResponseEntity<>(ical, headers, HttpStatus.ACCEPTED);
        return res;
    }

    public static class InputModel10 {
        public Integer i;
    }

    @PostMapping("/10")
    public String post10(@RequestBody InputModel10 input) {
        return "OK";
    }

    @PostMapping("/11")
    public String post11(@RequestBody InputModel10 input, BindingResult br) {
        return "OK";
    }

    @GetMapping("/12/{delay}")
    public String get12(@PathVariable("delay") int delay) throws InterruptedException {
        log.info("on get12");
        Thread.sleep(delay);
        return Integer.toString(delay);
    }

    public static class ConverterListOutputModel {
        public List<String> converters;

        @JsonProperty("some-integer")
        public int someInteger;

        public ConverterListOutputModel(List<String> converters) {

            this.converters = converters;
        }
    }

    @GetMapping("/13")
    ResponseEntity<ConverterListOutputModel> get13() {
        ConverterListOutputModel output = new ConverterListOutputModel(
            messageConverterList.stream()
                .map(e -> e.getClass().getSimpleName())
                .collect(Collectors.toList()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("My-Header", "My-Value");

        ResponseEntity<ConverterListOutputModel> res = new ResponseEntity<>(
                output,
                headers,
                HttpStatus.ACCEPTED
        );


        return res;

    }

    @GetMapping("/14")
    public void get14(HttpServletRequest req, HttpServletResponse res) {
        res.addHeader("My-Header", "My-Value");
        res.setStatus(204);
    }

    @GetMapping("/15")
    public String get15(
            QueryString qs, int i) {
        return qs.toString();
    }

    @GetMapping("/16")
    public void get16(HttpServletRequest req, HttpServletResponse res) {
        res.setStatus(204);
    }

    @GetMapping("/17")
    public String get17(ClientIp clientIp) {
        return "hello " +  clientIp;
    }

    @GetMapping("/18")
    public String get18() throws Exception {
        throw new Exception("don't do this");
    }

    @GetMapping("/19")
    public JsonNode get19() {
        ObjectMapper m = new ObjectMapper();
        ObjectNode node = m.createObjectNode();
        node.put("the-field", 42);
        return node;
    }

}
