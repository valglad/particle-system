# particle-system

A simple simulation of particles (represented by green circles on black background :P) of different masses moving about with various speeds and colliding with each other (or the walls of the window if enabled) elastically. Initial positions, masses and velocities of particles are selected randomly from ranges specified in the source code. Inter-particle forces can be switched on and the strength of the force varied from the graphical interface but the other parameters of this force can only be changed at the source code at present.

External sources of force can be introduced. "Gravity" is a uniform force acting downwards and can be switched on via the GUI but any other uniform forces would need to be added at source code. A radially acting force field with respect to any selected point in the application window can be set fully in GUI: it's of the form `A*(r-l)^(p)*(r-l)/abs(r-l)` (when r=l, this is undefined if p < 0, A if p=0 and 0 otherwise); here `A` is a constant specifying the strength of the force (negative value means repulsion from the origin point), `r` is the distance from the origin point and `l` is another constant corresponding to the distance at which the force switches sign (l=0 by default). `l` was introduced to allow modelling things like Hooke's elastic force (p=1, l is the natural length) and forces that are attractive at short ranges and repulsive otherwise. `A`, `p` and `l` can be specified when the force source is created.

A particle system can be saved in its current state and loaded at a later time.

"Merge" is a purely graphic feauture that produces lava-lamp-style patterns. It's fun to watch but can lag and flicker, especially at high speeds.

### Installation

This project is _very raw_. But if you'd like to install it anyway, clone the repository to some folder, go to that folder and run `./compile.sh` if you are a Linux user. Not sure how java files are compiled in other OS, but you can google it (so can I, at some point, to complete this section).
