# xls2history 

This tool converts *xls* files produced by [ExportImportXlsPlugin](https://trac-hacks.org/wiki/ExportImportXlsPlugin) for Trac.

## Usage

1. Export the following fields from Trac:

* id
* summary
* type - *defect*, *enhancment*, *feature*
* component - any related to project, currently Trc components can be joined and presented under one common final name. This is configured inside the source code. The current mapping is:
  
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

* fixedin

2. Run tool 