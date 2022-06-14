import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class JaegerTest {
    @Test
    public void callAnythingAndTest(){
        int a = 12;
        int b = 15;
        Assertions.assertEquals(27, a+b);
    }
}
