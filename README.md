# xx

At the begining of each markdown file, you can put some metadata there, e.g., tags, in the syntax of a Clojure hash.
{:tags ["life", "NYC"]}

During server startup, scan all markdown files for metadata and keep a record.

## TODO

* create a startup hook to generate tag list
* process tag when render markdown file

## CLI

* `java -cp ~/lib/clojure-1.5.1/clojure-1.5.1.jar:target/classes/ xx.cli -h` or `java -jar standalone.jar`
