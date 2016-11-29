import loganalazyer.LogAnalyzer;
import loganalazyer.LogSource;
import loganalazyer.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class LogAnalyzerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Logger loggerMock;

    @Mock
    private LogSource logSourceStub;

    private int expectedErrorLines;
    private int expectedInfoLines;
    private List<String> inputLines;

    public LogAnalyzerTest(List<String> lines, int expectedErrorLines, int expectedInfoLines) {
        this.expectedErrorLines = expectedErrorLines;
        this.expectedInfoLines = expectedInfoLines;
        this.inputLines = lines;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //Valid error cases
                {Arrays.asList("Error: ",
                        "Error: Major fayul",
                        "Error: mayday.. mayday",
                        "someError: dsa",
                        "some 34567890- 67890-Error: crash inevitable",
                        "some people like to log some weird stuff Error: ",
                        "Error:Error: ",
                        "dummy"), 7 ,0},
                //Invalid error cases
                {Arrays.asList("Error:",
                        "Error :Major fayul",
                        "E rror: mayday.. mayday",
                        "someError:dsa",
                        "some 34567890- 67890-Err0r: crash inevitable",
                        "some people like to log some weird stuffError:Error:Error:",
                        "Error: dummy"), 1 ,0},
                //Valid info cases
                {Arrays.asList("Info: ",
                        "Info: Major fayul",
                        "Info: mayday.. mayday",
                        "someInfo: dsa",
                        "some 34567890- 67890-Info: crash inevitable",
                        "some people like to log some weird stuff Info: ",
                        "Info:Info: ",
                        "dummy"), 0 ,7},
                //Invalid error cases
                {Arrays.asList("Info:",
                        "Info :Major fayul",
                        "I nfo: mayday.. mayday",
                        "someInfo:dsa",
                        "some 34567890- 67890-1nfo: crash inevitable",
                        "some people like to log some weird stuffInfo:Info:Info:",
                        "Info: dummy"), 0 ,1},
                //When error is logged, Info should be ignored
                {Arrays.asList("some text Info: some other text Error: ",
                        " text Error:  some text Info: some other",
                        "dummy"), 2 ,0},
        });
    }

    @Test
    public void analyzeCanLogError() throws Exception {
        when(logSourceStub.getLines())
                .thenReturn(inputLines);

        LogAnalyzer analyzer = new LogAnalyzer(logSourceStub,loggerMock);
        analyzer.analyze();

        verify(loggerMock,times(expectedErrorLines)).logError(matches(".*Error:\\s.*"));
        verify(loggerMock,times(expectedInfoLines)).logInfo(matches(".*Info:\\s.*"));
    }


}