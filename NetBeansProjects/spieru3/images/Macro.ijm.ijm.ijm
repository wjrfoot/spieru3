run("Shape Smoothing", "relative_proportion_fds=15 absolute_number_fds=0 keep=[Relative_proportion of FDs]");

run("Distance Transform Watershed", "distances=[Chessboard (1,1)]output=[16 bits] normalize dynamic=2 connectivity=4");

run("glasbey inverted");

setThreshold(1, 65535, "raw");

run("Convert to Mask");