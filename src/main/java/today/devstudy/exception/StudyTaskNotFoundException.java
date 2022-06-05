package today.devstudy.exception;

public class StudyTaskNotFoundException extends RuntimeException{
    public StudyTaskNotFoundException(){
        super();
    }
    public StudyTaskNotFoundException(String msg){
        super(msg);
    }
}
