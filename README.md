# First Java Ever
Backend REST Service Written in Java for Levels Beyond

The Backend task is to build a simple RESTful, JSON API to power a note-taking application.
## Setting the Stage
The API is implemented in Java 9.0.1 but uses only Java 8. The Gson library is included for json handling assistance. I tried
to minimize added packages as it is my first Java experience. I would definitely like to have some 
improvement on the URI/URL handling and there is definitely a library or framework that handles that.
Additionally looking for some guidance and feedback on file organization and frameworks that are common
at Levels Beyond.

I coded this example in JetBrains IntelliJ IDEA and, for me, that is the best way to run it as the 
compiling in java is new to me. This folder contains the "production" code:
```
/out/production/javaSimpleServer
and the main file is Test.class
```

## Notes
The notes API lives at the route /api/notes . The API server is running on localhost, you can
expect to access the 'notes' API at http://localhost/api/notes. If you run into trouble check for
conflicting apps/services that may be using port 80.

## The Note Model
```json
{
    "id" : 1,
    "body" : "Ask Larry about the TPS reports."
}
```
## Create a New Note
When I POST note JSON to the notes route, a new note will be created.
```
POST /api/notes
BODY a note


Returns: a saved note...
```

Example
```
curl -i -H "Content-Type: application/json" -X POST -d '{"body" : "Pick up milk!"}' http://localhost/api/notes
```
Returns:
```json
{
  "id" : 2,
  "body" : "Pick up milk!"
}
```
## Update an Existing Note
When I PUT note JSON to the notes route, an existing note will be updated.
```
PUT /api/notes
BODY a note


Returns: an updated note...
```

Example
```
curl -i -H "Content-Type: application/json" -X PUT -d '{"id": 3, "body" : "Pick up milk! NOW!"}' http://localhost/api/notes
```
Returns:
```json
{
  "id" : 3,
  "body" : "Pick up milk! NOW!"
}
```
## Delete an Existing Note
When I DELETE a note JSON to the notes route, an existing note with the corresponding ID
 at the end of the URL will be deleted.
```
DELETE /api/notes/3

Returns: the deleted note...
```

Example
```
curl -i -H "Content-Type: application/json" -X DELETE http://localhost/api/notes/3
```
Returns:
```json
{
  "id" : 3,
  "body" : "Pick up milk! NOW!"
}
```

## Get an Existing Note
I can get a note using an API call:
```
GET /api/notes/{id}
Returns: the requested note..
```
Example:
```
curl -i -H "Content-Type: application/json" -X GET http://localhost/api/notes/1
```
Returns:
```json
{
  "id" : 1,
  "body" : "Ask Larry about the TPS reports."
}
```

## Get All of my Notes
I can get all notes using an API call:
```
GET /api/notes
Returns: A list of my notes
```

Example:
```
curl -i -H "Content-Type: application/json" -X GET http://localhost/api/notes
```
Returns:
```json
[
    {
        "id" : 2,
        "body" : "Pick up milk!"
    },
    {
        "id" : 1,
        "body" : "Ask Larry about the TPS reports."
    }
]
```

## Get Filtered Notes
Also, I'd like to be able to pass in an optional query parameter that will allow me to search notes by their
bodies.

Example:
```
curl -i -H "Content-Type: application/json" -X GET http://localhost/api/notes?query=milk
```
Returns a list of every note with the word 'milk' in it.
