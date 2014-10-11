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

Requirements:

 * Java 8 Runtime (Java 7 and older are not supported)
 * Eclipse Luna or later (a minimal download is the Platform Runtime Binary, which can be found [here](http://download.eclipse.org/eclipse/downloads/))
 * Rust
 * Cargo
 * [racer](https://github.com/phildawes/racer)
 * Rust source code 

Current builds from the build server are available via the following update site:

	http://peeeq.de/hudson/job/Rustyeclipse/2/artifact/rustyeclipse_updatesite/target/site/

In eclipse, go to Help->Install new software and add the above url as an update site. 
Then you should be able to install the plugin.

After installing the plugin, you have to configure the external tools.
Go to Window->Preferences and then select the Rust options.
You have to provide the commands to execute `rustc`, `cargo`, and `racer`.
Additionally you have to provide the path to the rust sources, so racer can find definitions from the standard library.



# Building

To update the version use:

	mvn -o tycho-versions:set-version -DnewVersion=0.0.2


To build the project use:

	mvn clean package
	
