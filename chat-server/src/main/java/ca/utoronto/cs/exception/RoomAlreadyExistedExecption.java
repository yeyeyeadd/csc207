package ca.utoronto.cs.exception;

public class RoomAlreadyExistedExecption extends RuntimeException{
    public RoomAlreadyExistedExecption(String roomname){
        super(roomname);
    }
}
