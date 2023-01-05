package MementoPattern;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/5 15:04
 * @DESCRIPTION:
 */

public class Memento {
    private String state;

    public Memento(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }
}
