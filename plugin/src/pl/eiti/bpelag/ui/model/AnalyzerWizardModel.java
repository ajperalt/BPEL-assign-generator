package pl.eiti.bpelag.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Assign;

public class AnalyzerWizardModel {
	private Map<String, Assign> assignMap = null;

	public AnalyzerWizardModel() {
		assignMap = new HashMap<String, Assign>();
	}

	public Map<String, Assign> getAssignMap() {
		return assignMap;
	}

	public List<String> getAssignNameList() {
		return new ArrayList<>(assignMap.keySet());
	}

}
