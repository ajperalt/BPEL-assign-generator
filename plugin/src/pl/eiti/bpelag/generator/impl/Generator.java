package pl.eiti.bpelag.generator.impl;

import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.analyzer.IAnalysisResult;
import pl.eiti.bpelag.generator.IGenerator;

/**
 * Generator updates all assign activities of given process from analysis result
 * map - if copy elements generated exists for given assign activity.
 */
public class Generator implements IGenerator {

	@Override
	public void generate(org.eclipse.bpel.model.Process BPELprocess, IAnalysisResult analysis) {
		TreeIterator<EObject> procIterator = BPELprocess.eAllContents();

		while (procIterator.hasNext()) {
			if (procIterator instanceof Assign) {
				addCopyElements((Assign) procIterator, analysis.get((Assign) procIterator));
			}
			procIterator.next();
		}

	}

	/**
	 * Adds all copy elements to copy EList of processed assign activities.
	 * 
	 * @param assignActivity
	 *            actual processed activity
	 * @param copyElements
	 *            list of copy elements as result of process analysis
	 */
	private void addCopyElements(Assign assignActivity, List<Copy> copyElements) {
		assignActivity.getCopy().addAll(copyElements);
	}
}
