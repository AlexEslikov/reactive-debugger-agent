import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.debugger.streams.reactive.agent.ReactiveDebuggerAgent;
import com.intellij.debugger.streams.reactive.agent.StreamInfoStorage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author Aleksandr Eslikov
 */
public class ReactorTest {

    @Test
    public void doTest() {
        ReactiveDebuggerAgent.INSTANCE.init();
        ReactiveDebuggerAgent.INSTANCE.processExistingClasses();

        enableLogging();

        runStream();

        Object[] result = getResult();

        checkResult(result);
    }



    private void runStream() {
        Flux.just(1, 2, 3, 4, 5)
                .map(x -> x * x)
                .filter(x -> x % 2 == 0)
                .take(5)
                .subscribe();
    }

    private void enableLogging() {
        StreamInfoStorage.INSTANCE.enableLogForStream(
                List.of(
                        "ReactorTest.java:33",
                        "ReactorTest.java:34",
                        "ReactorTest.java:35",
                        "ReactorTest.java:36",
                        "ReactorTest.java:37"));
    }

    private Object[] getResult() {
        Object[] result = StreamInfoStorage.INSTANCE.getResult("ReactorTest.java:33");
        Assertions.assertNotNull(result);
        return (Object[]) result[0];
    }

    private void checkResult(@NotNull Object[] result) {
        Assertions.assertEquals(4, result.length);
        Assertions.assertDoesNotThrow(() -> {
            String json = new ObjectMapper().writeValueAsString(result);
            Assertions.assertEquals("[[[[1,3,7,9,13],[1,2,3,4,5]],[[2,4,8,10,14],[1,4,9,16,25]]],[[[2,4,8,10,14],[1,4,9,16,25]],[[5,11],[4,16]]],[[[5,11],[4,16]],[[6,12],[4,16]]],[[[6,12],[4,16]],[[],[]]]]",
                    json);
        });
    }
}
