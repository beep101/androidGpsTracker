This app is a simple GPS path recorder for android

The user interface consists of a tree activities/interfaces

  1. Main activity is a starting point with the button linked to the recorder activity and a list of previously recorded paths. By clicking on a path analytics activity is started

  2. Analytics activity shows a map with a path drawn over. Open street maps are used as background. Elevation and speed graphs are displayed below the map

  3. Recorder activity shows current speed and elevation and gives the user ability to start and stop recording walk/run/ride etc. It calls service implemented to manage android gps api

Point and path objects are used to model recordings. Point is speed, elevation, etc for a single point in time and path is list of points related to one recording. BoxStore an object database, is used for data persistency.
