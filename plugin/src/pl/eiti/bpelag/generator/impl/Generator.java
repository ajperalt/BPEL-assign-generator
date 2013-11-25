package pl.eiti.bpelag.generator.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpel.model.Assign;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.generator.IGenerator;

public class Generator implements IGenerator {
	private Map<Assign, Assign> generatedBlocks = null;

	@Override
	public void generate(org.eclipse.bpel.model.Process BPELprocess, IAnalysisResult analysis) {
		generatedBlocks = new HashMap<Assign, Assign>();
		// TODO Auto-generated method stub

	}

	public Map<Assign, Assign> getGeneratedBlocks() {
		return generatedBlocks;
	}
}
