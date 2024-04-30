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

Run that uberjar:

    $ java -jar target/net.clojars.balatro-clj/balatro-clj-0.1.0-SNAPSHOT.jar

