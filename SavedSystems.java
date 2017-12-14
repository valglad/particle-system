package particleSystem;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class SavedSystems{
    static String saveDir = "../saved_systems/";

    protected static void save(PSystem system, String name){
        try{
            FileOutputStream outFile = new FileOutputStream(saveDir + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(outFile);
            out.writeObject(system);
            out.close();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static PSystem load(String name){
        String filename = saveDir + name + ".ser";
        try{
            FileInputStream inFile = new FileInputStream(saveDir + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(inFile);
            PSystem system = (PSystem) in.readObject();
            in.close();
            inFile.close();
            return system;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            e.printStackTrace();
            return null;
        }
    }
}
