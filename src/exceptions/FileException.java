package exceptions;


public class FileException extends Exception {
    
      public FileException(){
          super();
      }

      public FileException(String message){
         super(message);
      }
}
