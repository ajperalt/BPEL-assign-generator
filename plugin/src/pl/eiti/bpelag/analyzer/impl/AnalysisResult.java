package pl.eiti.bpelag.analyzer.impl;

import java.util.HashMap;
import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;

import pl.eiti.bpelag.analyzer.IAnalysisResult;

public class AnalysisResult extends HashMap<Assign, List<Copy>> implements IAnalysisResult {
	private static final long serialVersionUID = 437818094293014797L;

}
