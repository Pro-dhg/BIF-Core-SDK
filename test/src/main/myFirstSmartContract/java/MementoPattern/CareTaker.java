package MementoPattern;
import java.util.ArrayList;
import java.util.List;
/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/5 15:04
 * @DESCRIPTION:
 */

public class CareTaker {
    private List<Memento> mementoList = new ArrayList<Memento>();

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }
}
