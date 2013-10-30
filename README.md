![trenders.org: an online stock market sim for students &amp; teachers](https://raw.github.com/chriskuehl/trenders.org/master/dev/images/trenders%20logos%20and%20images/trenders-logo-big.png)

trenders.org
============

trenders.org is an online stock market sim for students &amp; teachers. It grants students a virtual $100,000 to
invest in the US stock market (NASDAQ or NYSE).

Real-world data is used to run the simulation, which means that students have a real opportunity to test their
investing skills.

trenders.org is intended to be used by schools, but can also be used by individuals in the "DEMO" classroom.

The website can be viewed at [trenders.org](http://trenders.org/).

### Technologies
trenders.org is a web application written using the [Grails](http://grails.org/) web framework. For more about the
technologies used, see [the about page](http://trenders.org/about).

### Building trenders
Download and install the version of Grails used (currently **2.3.1**). It should build without any
additional configuration. Use `grails run-app` for testing on your machine and `grails war` to create a WAR file to
deploy to a Java application server.

When running in the `production` environment, the JDBC URL, username, and password are read from the following
environment variabes:

* `JDBC_URL`
* `JDBC_USERNAME`
* `JDBC_PASSWORD`

(right now only MySQL is supported but it should be very simple to support other databases, potentially by moving
the driver to an environment varible)

### Contributing
I originally started trenders as a closed-source project, but have open-sourced it in hopes that if anybody has an
interest in the site, they will be able to help maintain it.

Because of the heavy use of "trenders.org" branding, creating a fork can be problematic. Personally, I would strongly
encourage you not to create a competing fork of trenders and instead to submit a pull request or open an issue with
your changes.

You are free to create a competing fork (the code is released under the MIT license), but I believe it would be
beneficial for you to instead work with us to improve the site. We'd love to include your changes!

I'll be happy to deploy updates to the main trenders.org site after reviewing them.

### License
trenders.org is copyright &copy; 2013 Chris Kuehl, with original code and images released under an MIT license. See
`LICENSE` for details.
