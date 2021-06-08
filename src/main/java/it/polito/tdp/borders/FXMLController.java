
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	String annoStringa = txtAnno.getText();
    	int anno = -1;
    	try {
    		anno = Integer.parseInt(annoStringa);
    	}catch(NumberFormatException nfe) {
    		System.out.println("Devi inserire un numero!");
    	} catch(NullPointerException npe) {
    		System.out.println("Devi inserire un numero!");
    	}
    	if(anno<1816 && anno>2006) {
    		txtResult.setText("Devi inserire un anno compreso tra 1816 e 2006");
    		return;
    	}
    	this.model.getConfini(anno);
    	for(Border b: this.model.getConfini(anno)) {
    		txtResult.appendText(b.toString()+"\n");
    	}
    	for(Country c: this.model.numeroStatiConfinanti().keySet()) {
    		txtResult.appendText(c.toString()+" "+ this.model.numeroStatiConfinanti().get(c));
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
