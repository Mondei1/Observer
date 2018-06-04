# Config models
I know this looks weird and which plugin uses this kind of config management?
But in my case I use [Jackson JSON](https://github.com/FasterXML/jackson) a fast JSON parser.

And this parser needs "Models". These "Models" describes how the config structure is built.
Unfortunately, I have to make three models for one config, because the structure is this:
```json
{
    "client": {
        // Some client stuff here.
    },
    "backend": {
        // Some backend stuff here.
    }
}
```
And I have to make one model for the final config thats contains the other two. The other one for the clients block
and the other for the backend block.