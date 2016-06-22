package xls2history;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Executive for XlsReader
 * Supports command line calls and gui when no cmd parameters are given
 * 
 * @see XlsReader
 */
public class Xls2History {
    public static void main(String[] args) throws IOException {
        boolean gui = false; // was started without params (gui)
        final JFileChooser fc = new JFileChooser();
        String inputfilename;
        String outputfilename;
        if (args.length > 0) {
            if (args.length != 2) {
                showHelp();
                return; // wrong syntax
            }
            inputfilename = args[0]; // process command line
            outputfilename = args[1];
        } else { // open file selector
            gui = true;
            int ret = fc.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                inputfilename = fc.getSelectedFile().getAbsolutePath();
                outputfilename = fc.getSelectedFile().getPath() + File.pathSeparator
                        + fc.getSelectedFile().getName() + "_history.md";
            } else
                return; // canceled in file selector
        }

        // run conversion
        XlsReader xls = new XlsReader(inputfilename);
        xls.setOutputFileName(outputfilename);
        // set mappings TODO Move this part to outside json
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("QuimP-Plugin", "QuimP");
        map.put("QuimP-BOA", "QuimP");
        map.put("QuimP-ANA", "QuimP");
        map.put("QuimP-DIC", "QuimP");
        map.put("QuimP-ECMM", "QuimP");
        map.put("QuimP-Q", "QuimP");
        map.put("QuimP-RandomW", "QuimP");
        map.put("HatSnakeFilter", "HatSnakeFilter");
        map.put("HedgehogSnakeFilter", "HedgehogSnakeFilter");
        map.put("MeanSnakeFilter", "MeanSnakeFilter");
        map.put("SetHeadSnakeFilter", "SetHeadSnakeFilter");
        xls.generateHistory(map); // generate output file
        if (gui == true) // say goodbye
            JOptionPane.showMessageDialog(null, "Done. File saved under " + outputfilename);
        else
            System.out.println("Done. file saved under " + outputfilename);
    }

    public static void showHelp() {
        System.out.println("Two parameters required:");
        System.out.println("Xls2History inputfile.xls outputfile.md");
    }
}
