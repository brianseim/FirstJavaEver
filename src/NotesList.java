import java.util.ArrayList;
import java.util.HashMap;

class NotesList {
    private HashMap<Integer, String> notes = new HashMap<>(); // Consider SparseArray??
    private int nextId;

    public NotesList(){
        this.nextId = 1;
    }

    public void AddNote(String noteText){
        notes.put(nextId, noteText);
        nextId++;
        return;
    }

    public String JsonOut(){
        String r = "[";
        for(HashMap.Entry<Integer,String> note : notes.entrySet()){
            if(r.length()>1) r += ","; // looking for some kind of array.join like C# could also strip the last comma.
            r += NoteJsonOut(note);
        }
        r += "]";
        return r;
    }

    public String NoteJsonOut(HashMap.Entry<Integer,String> note){
        return "{ \"id\": " + note.getKey() + ", \"body\": \"" + note.getValue() + "\" }";
    }

    public String NoteJsonOut(int noteId) {
        String note = new String();
        if (notes.containsKey(noteId)) {
            String noteString = notes.get(noteId);
            note = "{ \"id\": " + noteId + ", \"body\": \"" + noteString + "\" }";
        }
        return note;
    }
}