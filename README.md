# balatro-clj

A bad recreation of the amazing [Balatro](https://www.playbalatro.com/) video game in Clojure.


## Usage

Run the project directly, via `:main-opts` (`-m balatro-clj.main`):

    $ clojure -M:run-m
    Hello, World!

Run the project's tests (they'll fail until you edit them):

    $ clojure -T:build test

Run the project's CI pipeline and build an uberjar (this will fail until you edit the tests to pass):

    $ clojure -T:build ci

This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the uberjar in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`.

If you don't want the `pom.xml` file in your project, you can remove it. The `ci` task will
still generate a minimal `pom.xml` as part of the `uber` task, unless you remove `version`
from `build.clj`.

Run that uberjar:

    $ java -jar target/net.clojars.balatro-clj/balatro-clj-0.1.0-SNAPSHOT.jar

