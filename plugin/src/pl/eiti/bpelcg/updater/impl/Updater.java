package pl.eiti.bpelcg.updater.impl;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Query;
import org.eclipse.bpel.model.To;
import org.eclipse.bpel.model.impl.FromImpl;
import org.eclipse.bpel.model.impl.ToImpl;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelcg.analyzer.IAnalysisResult;
import pl.eiti.bpelcg.analyzer.impl.AnalysisResult;
import pl.eiti.bpelcg.updater.IUpdater;

/**
 * Generator updates all assign activities of given process from analysis result
 * map - if copy elements generated exists for given assign activity.
 */
public class Updater implements IUpdater {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(org.eclipse.bpel.model.Process BPELprocess, IAnalysisResult analysis) {
		TreeIterator<EObject> procIterator = BPELprocess.eAllContents();

		while (procIterator.hasNext()) {
			EObject procElement = procIterator.next();
			if (procElement instanceof Assign && ((AnalysisResult) analysis).containsKey(procElement)) {
				for (Object generatedCopy : analysis.get((Assign) procElement)) {
					if (generatedCopy instanceof Copy) {
						if (!exists((Assign) procElement, (Copy) generatedCopy)) {
							addCopyElement((Assign) procElement, (Copy) generatedCopy);
						}
					}
				}
			}
		}

	}

	/**
	 * Check generated copy element given as parameter existing in assign
	 * element given as parameter.
	 * 
	 * @param existingAssign
	 *            currently checking existing assign element
	 * @param generatedCopy
	 *            currently checking generated copy element to selected assign
	 *            element
	 * @return boolean information if exists
	 */
	private boolean exists(Assign existingAssign, Copy generatedCopy) {
		Boolean result = Boolean.FALSE;

		for (Copy existingCopy : existingAssign.getCopy()) {
			From existingFrom = existingCopy.getFrom();
			To existingTo = existingCopy.getTo();

			From generatedFrom = generatedCopy.getFrom();
			To generatedTo = generatedCopy.getTo();

			Boolean sameFrom = Boolean.FALSE;
			Boolean sameTo = Boolean.FALSE;

			if (existingFrom.getVariable().equals(generatedFrom.getVariable())
					&& areQueriesEqual(existingFrom.getQuery(), generatedFrom.getQuery())) {
				if ((existingFrom instanceof FromImpl) && (generatedFrom instanceof FromImpl)) {
					sameFrom = Boolean.TRUE;
				}
				if ((existingTo instanceof ToImpl) && (generatedTo instanceof ToImpl)) {
					sameTo = Boolean.TRUE;
				}
				if (sameFrom && sameTo) {
					result = Boolean.TRUE;
				}
			}
		}

		return result;
	}

	/**
	 * Simple check equality of BPEL queries given as parameters.
	 * 
	 * @param existingQuery
	 *            query in existing copy element
	 * @param generatedQuery
	 *            query in generated copy element
	 * @return boolean information if queries are equal
	 */
	private Boolean areQueriesEqual(Query existingQuery, Query generatedQuery) {
		Boolean result = Boolean.FALSE;

		if (null != existingQuery && null != generatedQuery && null != existingQuery.getQueryLanguage()
				&& null != existingQuery.getValue() && null != generatedQuery.getQueryLanguage()
				&& null != generatedQuery.getValue()
				&& existingQuery.getQueryLanguage().equals(generatedQuery.getQueryLanguage())
				&& existingQuery.getValue().equals(generatedQuery.getValue())) {
			result = Boolean.TRUE;
		}

		return result;
	}

	/**
	 * Adds all copy elements to copy EList of processed assign activities.
	 * 
	 * @param assignActivity
	 *            actual processed activity
	 * @param copyElements
	 *            list of copy elements as result of process analysis
	 */
	private void addCopyElement(Assign assignActivity, Copy copyElement) {
		assignActivity.getCopy().add(copyElement);
	}

	@Override
	public From createNewFrom() {
		From newFrom = BPELFactory.eINSTANCE.createFrom();

		return newFrom;
	}

	@Override
	public To createNewTo() {
		To newTo = BPELFactory.eINSTANCE.createTo();

		return newTo;
	}

	@Override
	public Query createNewQuery() {
		Query newQuery = BPELFactory.eINSTANCE.createQuery();

		return newQuery;
	}
}
