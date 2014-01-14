package pl.eiti.bpelag.analyzer.impl;

import java.util.HashMap;
import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;

import pl.eiti.bpelag.analyzer.IAnalysisResult;

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
