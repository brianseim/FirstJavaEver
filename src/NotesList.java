import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class NotesList {
    private HashMap<Integer, Note> notes = new HashMap<>(); // Consider SparseArray??
    private int nextId;

    NotesList(){
        this.nextId = 1;
    }

    public Note add(String noteText){
        Note newNote = new Note(nextId, noteText);
        notes.put(nextId, newNote);
        nextId++;
        return newNote;
    }

    public Note update(Note note) throws Exception{
        if(notes.containsKey(note.id)) {
            notes.put(note.id, note);
            return note;
        }
        throw new Exception("Exception Thrown - note not found");
    }

    public Note delete(int noteId) throws Exception{
        Note deleteNote = this.get(noteId);
        // not found exception may be encountered above
        notes.remove(noteId);
        return deleteNote;
    }

    public Note get(int noteId) throws Exception {
        if (notes.containsKey(noteId)) {
            return notes.get(noteId);
        }
        throw new Exception("Exception Thrown - note not found");
    }

    public Note[] get(){
        return notes.values().toArray(new Note[0]);
    }

    public Note[] getFiltered(String txt){
        // https://stackoverflow.com/questions/11160382/java-map-filter-with-values-properties
        // https://www.mkyong.com/java8/java-8-filter-a-map-examples/
        Map<Integer, Note> filtered = notes.entrySet().stream()
                .filter(map -> map.getValue().body.contains(txt))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return filtered.values().toArray(new Note[0]);
    }

//    public String JsonOut(){
//        String r = "[";
//        for(HashMap.Entry<Integer,Note> note : notes.entrySet()){
//            if(r.length()>1) r += ","; // looking for some kind of array.join like C# could also strip the last comma.
//            r += NoteJsonOut(note);
//        }
//        r += "]";
//        return r;
//    }
//
//    public String NoteJsonOut(HashMap.Entry<Integer,String> note){
//        return "{ \"id\": " + note.getKey() + ", \"body\": \"" + note.getValue() + "\" }";
//    }
//
//    public String NoteJsonOut(int noteId) {
//        String note = new String();
//        if (notes.containsKey(noteId)) {
//            String noteString = notes.get(noteId);
//            note = "{ \"id\": " + noteId + ", \"body\": \"" + noteString + "\" }";
//        }
//        return note;
//    }
}