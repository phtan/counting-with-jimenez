### Abstract

Suppose I want to count the number of steps I have taken.
I have an accelerometer so I can have data on changes in acceleration over time.
Given such data, how might I count the number of steps I have taken while
the data was being collected?
We attempt to solve such a problem. We implement an algorithm by Jimenez et al. (2009).

### Input

The expected input is a CSV (Comma-separated values) file.
Semantically speaking, the file contains readings from an accelerometer,
in the x-axis, y-axis and z-axis.

The following is an example of a CSV file with 5 sets of readings:

```
-2.35,0.39,9.12
-1.86,0.29,8.63
-1.77,0.49,8.73
-1.67,0.88,9.02
-1.37,1.08,9.02
```

### Output

The expected output of the program is an estimate of the number of steps
(as in walking, running, and so on) that have been taken while readings
were collected from the accelerometer.

### Usage

I provide instructions to compile and run the program, on a *nix system.

I assume you have the executables *java* and *javac*.
Let `path/to/csv` represent the location of the CSV-file (ie. input).

1. navigate to the directory that has CountStep.java

1. run the following command, including the full-stop (or period): javac -d . CountStep.java

2. run: java ZerothPackage.CountStep `path/to/csv`

### Licence

All may obtain a copy of this program, under the MIT licence. A copy of the licence
has been included with this program.

### Reference(s)

- A. R. Jiménez et al. The year 2009. 'A Comparison of Pedestrian Dead-Reckoning Algorithms using a Low-Cost MEMS IMU'. Related to 6th IEEE International Symposium on Intelligent Signal Processing. 
(A. R. Jiménez et al. 'A Comparison of Pedestrian Dead-Reckoning Algorithms using a Low-Cost MEMS IMU'. Related to 6th IEEE International Symposium on Intelligent Signal Processing. The year 2009.)