# invesdwin-context-r
Integrate R functionality with these modules for the [invesdwin-context](https://github.com/subes/invesdwin-context) module system.

## Maven

Releases and snapshots are deployed to this maven repository:
```
http://invesdwin.de/artifactory/invesdwin-oss-remote
```

Dependency declaration:
```xml
<dependency>
	<groupId>de.invesdwin</groupId>
	<artifactId>invesdwin-context-r-runtime-rcaller</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

## License Discussion

Please note that [Renjin](http://www.renjin.org/) (as a fast R-Engine for the JVM) and some [R](https://www.r-project.org/) packages that are used here are licensed under the GPL. Thus we have decided to publish some of our R modules (documented in their respective pom.xmls) here as GPL as well. Even though the rest of the invesdwin platform is still licensed under the LGPL (if not otherwise noted), you should be aware that by using these GPL'd R modules in your main application you will have to follow the restrictions that are imposed by the GPL. If you do not want to make your whole application available under the terms of the GPL, you could go for a compromise by writing standalone applications for your R-scripts (which are licensed under the GPL then) and integrating them by calling them as separate processes via the command line from your main application. This at least will provide reusable (and hopefully high quality) command line applications for using R that the rest of the community can use and this will also allow you to leverage R functionality in a legally tolerated way (even though we are no lawyers and this is not any sort of legal advice, it still seems to be [what the FSF came to terms with](https://www.gnu.org/licenses/gpl-faq.html#GPLPlugins)). As long as there is no intimate relationship between the main application and the command line application this border case seems to be accepted. If you have any further advice on this topic, we would be glad to hear more about it. 

## Runtime Integration Modules

We have a few options available for integrating R:
- **invesdwin-context-r-runtime-jri**: You could integrate R directly via [JRI](https://rforge.net/JRI/) which seems to be licensed under the LGPL (even though this itself dynamically links to R libs and is thus legally questionable, even if R itself has an exception for allowing dynamic linking in some of its headers files that are licensed via LGPL, most R packages that you might want to use don't have that). The technological problem with JRI is that it only supports single threaded access to the embedded R session. Thus scalability is an issue when running multiple scripts in parallel without forking more processes. Though for single threaded usage this could provide a good performance since the communication overhead is minimal in comparison to other methods. This module provides the following configuration options as system properties:
```properties
# specify where the libjri.so resides on your computer (which you might normally add to java.library.path manually)
de.invesdwin.context.r.runtime.jri.JriProperties.JRI_LIBRARY_PATH=/usr/lib/R/site-library/rJava/jri/
```
- **invesdwin-context-r-runtime-rserve**: You could use [Rserve](https://github.com/s-u/REngine) (which seems to be licensed under the LGPL too) to talk to a separate R process via a socket to run your scripts (which might use GPL packages) there, separated from your main application. This module can also be used to run your R scripts on remote hosts. We use a pool of [Rsession](https://github.com/yannrichet/rsession) instances here to do the actual work.
- **invesdwin-context-r-runtime-renjin**: The idea of [using Renjin only behind the javax.script API](https://groups.google.com/forum/#!msg/renjin-dev/yoS1dTeJLm8/bVtVu_tGLck) seems to be legally questionable since it still dynamically links to Renjin in the same JVM, just with an API layer in between. As long as Renjin does not have a classpath exception in its GPL version, it is advisable to separate the process as described above via a separate command line tool for your scripts. Or maybe it suffices that the Renjin integration is kept optional in your application and you allow the user to choose which module he uses, then you could still distribute Renjin with the other modules (or provide a separate download for this) and allow the user to run it in the same JVM if he decides so ([this was another idea of the Renjin developers](https://groups.google.com/forum/#!msg/renjin-dev/yoS1dTeJLm8/bVtVu_tGLck)). Technologically Renjin can provide a faster runtime for your R scripts as long as all required packages work correctly. Since Renjin is still a work in progress and might have missing functionality, you should better test it thoroughly.
- **invesdwin-context-r-runtime-rcaller**: Another alternative would be to call the original R executable directly from your main application via a command line interface and let it run your R script. Though keep in mind that you might still have to make at least your R scripts open source if they use R packages that are licensed under the GPL. Though by using the R executable directly you would lose the performance benefit that other solutions bring to the table. On the other hand, this solution requires no additional setup on the systems it runs on, since a simple installation of R suffices as long as the `Rscript` executable is available in the `$PATH`. We use a pool of [RCaller](https://github.com/jbytecode/rcaller) instances here to do the actual work.

You are free to choose which integration method you prefer by selecting the appropriate runtime module as a dependency for your application. The `invesdwin-context-r-runtime-contract` module defines interfaces for integrating your R scripts in a way that works with all of the above runtime modules. So you have the benefit of being able to write your R scripts once and easily test against different runtimes in order to: 
- verify that Renjin produces the same results as R itself
- to measure the performance impact of the different runtime solutions
- to gain flexibility in various deployment scenarios

For an example R script integration, have a look at the `invesdwin-context-r-optimalf` module or the test cases in `invesdwin-context-r-runtime-contract` which are executed in each individual runtime module test suite.

## Recommended Editors

For working with R we recommend using [StatET](http://www.walware.de/goto/statet) if you are mainly using Eclipse. The included editor suffices if you only run the scripts using `invesdwin-context-r`. So no complicated R setup with eclipse is needed, just install the plugin from the marketplace and run your scripts with `invesdwin-context-r-runtime-rcaller` (add this module as a `test` scope dependency) during development to get R console output as you are used to from an interactive R shell (you also need to add a dependecy to the type `test-jar` for the log level to get activated, or alternatively change the log level of `de.invesdwin.context.r.runtime.contract.IScriptTaskRunnerR` to `DEBUG` on your own). The actual deployment distribution can choose a different runtime then as a hard dependency (again see the `invesdwin-context-r-optimalf` module as an example for this). For experimenting with R it might be interesting to use [RStudio](https://www.rstudio.com/) as a standalone development environment. It supports a nice variable viewer and has a nice integration of the R documentation, which helps a lot during R learning and development. It also comes with a comfortable debugger for R scripts.
