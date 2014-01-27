package pl.eiti.bpelcg.analyzer.impl;

import java.util.HashMap;
import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;

import pl.eiti.bpelcg.analyzer.IAnalysisResult;

/**
 * Implementation of analysis result created by Analyzer element. Structure
 * represented by map with assign elements as a keys and list of copy
 * instructions as values mapped by keys.
 */
public class AnalysisResult extends HashMap<Assign, List<Copy>> implements IAnalysisResult<Assign, List<Copy>> {
	private static final long serialVersionUID = 437818094293014797L;

	@Override
	public List<Copy> put(Assign key, List<Copy> value) {
		List<Copy> result = null;
		if (key instanceof Assign && value instanceof List) {
			result = super.put((Assign) key, (List<Copy>) value);
		}
		return result;
	}

	@Override
	public List<Copy> get(Assign key) {
		List<Copy> result = super.get(key);
		return result;
	}

}
