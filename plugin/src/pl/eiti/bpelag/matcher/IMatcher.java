package pl.eiti.bpelag.matcher;

import java.util.List;

import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Variable;

public interface IMatcher {
	/**
	 * Finds all variables from given list that match (with name and type) to
	 * any input variables of invokes from given list.
	 * 
	 * @param settedVariables
	 *            list of variables that can be used as invoke call parameters
	 * @param followingInvokes
	 *            list of invoke activities following currently processed assign
	 *            activity
	 */
	public List<Copy> createCopyForMatchedVariables(List<Variable> settedVariables, List<Invoke> followingInvokes);

}
