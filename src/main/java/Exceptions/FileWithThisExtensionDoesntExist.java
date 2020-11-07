package Exceptions;

public class FileWithThisExtensionDoesntExist extends Exception{
    public FileWithThisExtensionDoesntExist(String message){
        super(message);
    }
}
