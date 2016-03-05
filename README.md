# salt-clj

A Clojure client for Salt REST API.

## Usage

The library functions correspond to the Salt API endpoints (e.g. `minions`). Functions that use HTTP POST end with `!`-mark (kind of clojure convention for destructive operations). Currently only a subset of endpoints is supported (basically running jobs & collecting the results).

See
* [test/salt_clj/examples.clj](test/salt_clj/examples.clj) file or docs of the functions,
* the [doc](doc) directory for some attempts to document this thing.

## Todo
* Explore and document non-happy path scenarios
* API logout doesn't work although returning 200

## License
See the `LICENSE` file.

