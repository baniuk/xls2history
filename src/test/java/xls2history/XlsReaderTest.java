/**
 * 
 */
package xls2history;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author baniuk
 *
 */
public class XlsReaderTest {
    final Logger logger = LoggerFactory.getLogger(XlsReaderTest.class);
    private Workbook workbook;
    private HashMap<String, String> map;
    private XlsReader obj;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        FileInputStream inputStream =
                new FileInputStream(new File("src/test/resources/tickets.xls"));
        workbook = new HSSFWorkbook(inputStream);
        inputStream.close();
        map = new HashMap<String, String>();
        map.put("QuimP-Plugin", "QuimP");
        map.put("QuimP-BOA", "QuimP");
        map.put("HatSnakeFilter", "HatSnakeFilter");
        obj = new XlsReader("src/test/resources/tickets.xls");
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        workbook.close();
    }

    @Test
    public void fillEntryTest() {
        XlsReader t = new XlsReader();
        Iterator<Row> iterator = workbook.getSheetAt(0).iterator();
        Row r = iterator.next();
        logger.debug(t.fillEntry(r).toString());
    }

    @Test
    public void XlsReaderTest() throws IOException {
        XlsReader r = new XlsReader("src/test/resources/tickets.xls");
    }

    @Test
    public void sortTest() throws IOException {
        XlsReader r = new XlsReader("src/test/resources/tickets.xls");
        // logger.debug(r.toString());
        r.sort(Entry.COMPONENT);
        // logger.debug(r.toString());

    }

    @Test
    public void getValCodesTest() {
        Map<String, Integer> ret = obj.getValCodes(map);
        logger.debug(ret.toString());
    }

    @Test
    public void generateHistory() throws IOException {
        logger.trace("GenerateHistory");
        XlsReader r = new XlsReader("src/test/resources/tickets.xls");
        r.generateHistory(map);
    }

}
