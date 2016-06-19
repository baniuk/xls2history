
# QuimP


## 16.03.17

* #19 [*defect*] Wrong composition of BOA window
* #20 [*defect*] Changes of active ROI tool
* #23 [*defect*] BOA not closing properly
* #58 [*defect*] plugin.conf and icons not included in QuimP.jar
* #60 [*feature*] Integrate DIC filtering into QuimP
* #62 [*feature*] Simple window builder
* #63 [*feature*] Build plugin interface
* #69 [*feature*] Add loading jars from disk
* #76 [*feature*] Bidirectional plugins
* #77 [*defect*] Wrong behaviour window when selecting plugin to none
* #78 [*feature*] Add About button
* #80 [*feature*] Configure plotting segmented and processed snakes
* #82 [*feature*] DIC plugin does not report progress
* #87 [*feature*] Add history of actions

## 16.05.06

* #54 [*feature*] fix reference point over time
* #85 [*enhancement*] Add DIC to icon
* #86 [*feature*] Add plugin configration saving
* #88 [*feature*] Add tick boxes for plugins
* #89 [*defect*] Rename menu to plot original
* #93 [*feature*] Add option for displaying node in QuimP view
* #98 [*enhancement*] Fix support for plugins interfaces
* #101 [*defect*] Plugin window does not disapear when BOA finishes
* #103 [*enhancement*] About window should support RMB
* #104 [*feature*] About window should display short description of filters
* #105 [*enhancement*] Add Cancel button in QWindow template
* #106 [*defect*] Make list of filters more intuitive
* #108 [*defect*] Logs in main window does not scroll and numbering is wrong on second BOA run
* #110 [*enhancement*] Rename quit buttons
* #111 [*enhancement*] Rework QuimP bar
* #114 [*defect*] Matlab procedures return errors in R2016
* #116 [*feature*] Save BOA state
* #117 [*defect*] Fail if Quit after start
* #120 [*defect*] When frame is edited and there is plugin activated on earlier frame this plugin extends to edited frame
* #137 [*defect*] QuimP plugins appear in ImageJ plugin menu

## 16.06.01

* #109 [*feature*] Discard/Apply filter effects for whole stack without segmenting again
* #123 [*defect*] Fix formatting in About
* #135 [*defect*] Unpredictable zoom
* #138 [*enhancement*] Add fixed-width fonts in About dialog
* #144 [*enhancement*] Check SnakePluginList for possible bug
* #154 [*feature*] Allow to load new format in BOA
* #156 [*feature*] Add date to QCONF
* #157 [*feature*] Original Snakes are not restored
* #158 [*feature*] Detect if user loaded correct file for Plugin config and Global config
* #159 [*feature*] Add color marker for filter list to indicate whether they have been instanced
* #160 [*defect*] Store does not work when full range segmentation
* #161 [*defect*] When global config is loaded opened plugins are not closed

# HatSnakeFilter


## 1.0.0

* #66 [*feature*] Hatfilter should have advanced ui
* #70 [*defect*] QuimP Hat fails on fresh run

## 1.0.1

* #92 [*defect*] Preview is deleted on Apply
* #107 [*defect*] Preview in HatSnakeFilter points last frame

## 1.0.2

* #143 [*defect*] Deleted cell is still in plugin
* #145 [*enhancement*] Change package to quimp.plugin
