# About

RustyEclipse is an eclipse plugin for the [Rust programming language](http://www.rust-lang.org/).
It uses an external tool named [racer](https://github.com/phildawes/racer) for smart features like auto-complete and jump to declaration.

## Features

 * Syntax highlighting
 * Compile and run projects 
 * Show errors in source code
 * Autocomplete support via racer
 * Jump to declaration support via racer


# Installing


Current builds from the build server are available via the following update site:

	http://peeeq.de/hudson/job/Rustyeclipse/2/artifact/rustyeclipse_updatesite/target/site/


# Building

To update the version use:

	mvn -o tycho-versions:set-version -DnewVersion=0.0.2


To build the project use:

	mvn clean package
	
