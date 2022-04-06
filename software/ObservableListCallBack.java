/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
/**
 *
 * @author Vishnu
 */
public class ObservableListCallBack implements Callback<TableColumn.CellDataFeatures<javafx.collections.ObservableList, String>, ObservableValue<String>>{
    private int index;
    public ObservableListCallBack(int i){
        index = i;
    }
    public ObservableValue<String> call(TableColumn.CellDataFeatures<javafx.collections.ObservableList, String> param) {
        
                    return new SimpleStringProperty(param.getValue().get(index).toString());
    }
}
       
   

