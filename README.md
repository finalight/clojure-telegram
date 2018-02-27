# telegram-clojure

Sample project to show how to connect to the telegram bot using clojure programming language.

Prerequisite

You need to export the following environment variable

```
export BOT_URL=YOUR BOT URL HERE
export TELEGRAM_API_KEY=YOUR TELEGRAM API KEY HERE
```

Using heroku, you can also set the config variable in the platform and push to heroku after that.

For those who are not familiar with heroku/heroku clojure deployment, you can refer to (here)[https://devcenter.heroku.com/articles/getting-started-with-clojure]

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    ```lein ring server```
    Or
    ```lein ring server-headless```


## License

Copyright © 2018 FIXME
