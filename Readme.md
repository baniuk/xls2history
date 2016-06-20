# xls2history 

This tool converts *xls* files produced by [ExportImportXlsPlugin](https://trac-hacks.org/wiki/ExportImportXlsPlugin) for Trac.

## Usage

1. Export the following fields from Trac:

    * id
    * summary
    * type - *defect*, *enhancment*, *feature*
    * component - any related to project, currently Trac components can be joined and presented under one common final name. This is configured inside the source code as `Map<Trac-component, Output-component>`. The current mapping is:
  
    ```java
    HashMap<String, String> map = new HashMap<String, String>();
        map.put("QuimP-Plugin", "QuimP");
        map.put("QuimP-BOA", "QuimP");
        map.put("QuimP-ANA", "QuimP");
        map.put("QuimP-DIC", "QuimP");
        map.put("QuimP-ECMM", "QuimP");
        map.put("QuimP-Q", "QuimP");
        map.put("HatSnakeFilter", "HatSnakeFilter");
        map.put("HedgehogSnakeFilter", "HedgehogSnakeFilter");
        map.put("MeanSnakeFilter", "MeanSnakeFilter");
        map.put("SetHeadSnakeFilter", "SetHeadSnakeFilter");
    ```

    * fixedin - which version current bug has been fixed in

2. Run tool
    
    ```sh
    java -jar target/xls2history-1.0.0.jar
    ```
    
    or
    
    ```
    java -jar target/xls2history-1.0.0.jar input.xls output.md
    ```