/**
 * 
 */
package xls2history;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert xls file obtained from Trac to MarkDown formatted text file that contains
 * list history of changes. It is grouped by components and sorted by versions within them.
 * Trac components can be grouped under common name
 * 
 * General workflow is:
 * 
 * Read xls file 
 * 
 * Check whether header is correct
 * 
 * Copy everything into ArrayList<Entry> content
 * 
 * Look into Map and identify unique outputcomponent names (those that merge traccomonent names)
 * 
 * Go through list of records and copy them to separate arrays according to mapping between trac
 * and output components. Records are grouped in separate arrays according to output components
 * (HashMap<String, ArrayList<Entry>> a = new HashMap<String, ArrayList<Entry>>();)
 * 
 * Go through those arrays and create output file. Every outputcomponent array is sorted before
 * 
 * @author baniuk
 *
 */
public class XlsReader {

    final Logger logger = LoggerFactory.getLogger(XlsReader.class);
    public Entry header;
    public ArrayList<Entry> content;
    private Workbook workbook;
    String outputfilename = "default.txt";

    public XlsReader() {
        header = new Entry();
        content = new ArrayList<Entry>();
    }

    /**
	 * @throws IOException 
	 * 
	 */
    public XlsReader(String filename) throws IOException {
        this();
        FileInputStream inputStream = new FileInputStream(new File(filename));
        workbook = new HSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
        Row head = iterator.next();
        header = fillEntry(head); // fill header
        while (iterator.hasNext()) { // iterate over rest of document
            Row r = iterator.next();
            Entry e = fillEntry(r);
            content.add(e); // store content in list
        }
        checkHeader(); // check if header is correct
        workbook.close();
        inputStream.close();

    }

    /**
	 * Overrides default output file name
	 * @param name new name of output file
	 */
    public void setOutputFileName(String name) {
        outputfilename = name;
    }

    /**
	 * Compare first row read from xls file to given pattern.
	 * 
	 * The pattern should no change and it is dependent to tickets fields defined inTrac
	 */
    private void checkHeader() {
        Entry pattern = Entry.getRequiredHeader();
        if (!pattern.equals(header))
            throw new IllegalArgumentException("Loaded xls contains header different from assumed");
    }

    /**
	 * Sort list according to one filed of Entry 
	 * @see xls2history.EntryComparator.compare(Entry, Entry)
	 */
    public void sort(int compareTo) {
        Collections.sort(content, new EntryComparator(compareTo));
    }

    /**
	 * Assign numeric code for every unique value from input map
	 * @param map
	 * @return Map where keys are unique across values of input map. By default input map contains pairs
	 * <traccomponent, outputcomponent>, where number of output components is usually smaller (they
	 * join many trac components into one common name displayed in resulting history under common name) 
	 */
    Map<String, Integer> getValCodes(Map<String, String> map) {
        HashMap<String, Integer> codes = new HashMap<String, Integer>();
        Integer id = 0;
        for (String val : map.values()) {
            if (!codes.containsKey(val))
                codes.put(val, id++);
        }
        return codes;

    }

    /**
	 * Main method that saves history under outputfilename
	 * @param map Map of trac components assigned to output components. The keys are trac components,
	 * the values are output components that trac components should be assigned to. This map joins
	 * different trac components to one name.
	 */
    public void generateHistory(Map<String, String> map) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputfilename));
            // if(writer.)
            // throw new IllegalArgumentException("File "+outputfilename+" could not be opened");
            Map<String, Integer> codes = getValCodes(map); // get numeric codes for output component
                                                           // names
            HashMap<String, ArrayList<Entry>> a = new HashMap<String, ArrayList<Entry>>();
            // initialize lists, codes contain unique names of output components
            for (String key : codes.keySet()) {
                a.put(key, new ArrayList<Entry>());
            }

            // iterate over map and split it to separate lists component name
            // the connection between component names and output names are in input map
            for (Entry e : content) {
                // get component name
                String cn = e.component;
                // check output component name for this component
                String ocn = map.get(cn);
                if (ocn == null) // not present in map - ignore
                    continue;
                // copy this entry to correct array grouping tickets for one output component (can
                // group many trac components)
                ArrayList<Entry> tmp = a.get(ocn);
                tmp.add(e);
            }
            // now go through every key in a (it is a List)
            for (Map.Entry<String, ArrayList<Entry>> e : a.entrySet()) {
                String outComp = e.getKey();
                ArrayList<Entry> records = e.getValue();
                Collections.sort(records, new EntryComparator(Entry.FIXEDIN)); // sort to fixedin
                                                                               // (within grouped
                                                                               // components)
                logger.info(outComp);
                logger.info(records.toString());
                addHeader(writer, outComp);
                addList(writer, records);
            }
        } catch (Exception io) {

        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
	 * Add records in output file them onto subsections depended on version
	 * @param writer
	 * @param records
	 * @throws IOException 
	 */
    private void addList(BufferedWriter writer, List<Entry> records) throws IOException {
        String prev = "";
        for (Entry e : records) {
            // print only if there is fixed in value
            if (e.fixedin.isEmpty())
                continue;
            if (!e.fixedin.equals(prev)) {// different than prev - add subsection
                writer.newLine();
                writer.write("## " + e.fixedin);
                writer.newLine();
                writer.newLine();
                prev = e.fixedin;
            }
            // plot record
            String s = "#" + e.id + " [*" + e.type + "*] " + e.summary;
            writer.write("* " + s);
            writer.newLine();
        }

    }

    /**
	 * Add header (output component name) to output file
	 * @throws IOException 
	 */
    private void addHeader(BufferedWriter writer, String h) throws IOException {
        String out = "# " + h;
        writer.newLine();
        writer.write(out);
        writer.newLine();
        writer.newLine();

    }

    /**
	 * Copy content of one row to class Entry
	 * 
	 * @param r row to copy
	 * @return Instance of Entry class
	 */
    Entry fillEntry(Row r) {
        Iterator<Cell> cellIter = r.cellIterator();
        ArrayList<String> l = new ArrayList<String>();
        while (cellIter.hasNext()) { // cell value must be taken by appropriate method
            Cell cell = cellIter.next();
            switch (cell.getCellType()) { // according to cell type call correct method
                case Cell.CELL_TYPE_STRING:
                    l.add(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC: // convert to string as Entry class keeps only strings
                    Double d = new Double(cell.getNumericCellValue());
                    l.add(String.format("%.0f", d));
                    break;
                case Cell.CELL_TYPE_BLANK:
                    l.add(cell.getStringCellValue());
                    break;
                default: // other cases not supported
                    throw new IllegalArgumentException(
                            "Cell contains illegal value (not string nor numeric: "
                                    + cell.getCellType());
            }

        }
        Entry e = new Entry(l); // create instance
        return e;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "XlsReader [content=" + content + "]";
    }

}

/**
 * Keep decoded row from xls
 * 
 * Define what data are stored and in what order they are in xls file
 * 
 * This class should be modified if xls format changes.
 * 
 * @author baniuk
 */
class Entry {
    // order of fields in xls
    final static int ID = 0;
    final static int SUMMARY = 1;
    final static int TYPE = 2;
    final static int COMPONENT = 3;
    final static int FIXEDIN = 4;
    // data
    public String id;
    public String summary;
    public String type;
    public String component;
    public String fixedin;

    /**
	 * Return header that is required. Assures that data are in correct order
	 * @return Header as Entry reference
	 * @see xls2history.XlsReader.checkHeader()
	 */
    static public Entry getRequiredHeader() {
        return new Entry("id", "Summary", "Type", "Component", "Fixed in"); // header of xls file
    }

    /**
	 * Default constructor for empty object
	 */
    public Entry() {
        id = "";
        type = "";
        summary = "";
        component = "";
        fixedin = "";
    }

    /**
	 * Convert from List to Entry. 
	 * @param input List<String> with data in correct order. Order is checked in checkHeader()
	 */
    public Entry(List<String> input) {
        id = input.get(Entry.ID);
        type = input.get(Entry.TYPE);
        summary = input.get(Entry.SUMMARY);
        component = input.get(Entry.COMPONENT);
        fixedin = input.get(Entry.FIXEDIN);

    }

    /**
	 * Constructor from fields
	 * @param id
	 * @param type
	 * @param summary
	 * @param component
	 * @param fixedin
	 */
    public Entry(String id, String summary, String type, String component, String fixedin) {
        super();
        this.id = id;
        this.type = type;
        this.summary = summary;
        this.component = component;
        this.fixedin = fixedin;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "Entry [id=" + id + ", type=" + type + ", summary=" + summary + ", component="
                + component + ", fixedin=" + fixedin + "]\n";
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((component == null) ? 0 : component.hashCode());
        result = prime * result + ((fixedin == null) ? 0 : fixedin.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((summary == null) ? 0 : summary.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Entry))
            return false;
        Entry other = (Entry) obj;
        if (component == null) {
            if (other.component != null)
                return false;
        } else if (!component.equals(other.component))
            return false;
        if (fixedin == null) {
            if (other.fixedin != null)
                return false;
        } else if (!fixedin.equals(other.fixedin))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (summary == null) {
            if (other.summary != null)
                return false;
        } else if (!summary.equals(other.summary))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}