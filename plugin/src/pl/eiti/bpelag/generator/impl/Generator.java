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
		// TODO iterate on process activities, when assign found search for it
		// in analysis result if found put copy elements int assign block and
		// remove from analysis result

	}

	public Map<Assign, Assign> getGeneratedBlocks() {
		return generatedBlocks;
	}
}
