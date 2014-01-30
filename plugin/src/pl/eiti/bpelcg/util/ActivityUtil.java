package pl.eiti.bpelcg.util;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Empty;
import org.eclipse.bpel.model.Exit;
import org.eclipse.bpel.model.ExtensionActivity;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.Reply;
import org.eclipse.bpel.model.Rethrow;
import org.eclipse.bpel.model.Throw;
import org.eclipse.bpel.model.Validate;
import org.eclipse.bpel.model.Wait;

/**
 * Helper element used in BPELcg plugin project.
 */
public class ActivityUtil {
	/*
	 * Comes from
	 * https://github.com/BPELtools/BPEL-dataflow-analyzer/blob/master
	 * /src/de.uni
	 * -stuttgart.iaas.bpel-d.algorithm/src/de/uni_stuttgart/iaas/bpel_d
	 * /algorithm/analysis/Activity.java
	 */
	private static final Class<?>[] BASIC_ACTIVITIES = { Assign.class, Empty.class, Exit.class,
			ExtensionActivity.class, Invoke.class, Receive.class, Reply.class, Rethrow.class, Throw.class,
			Validate.class, Wait.class };

	/**
	 * Checks if the activity given as parameter is basic (an opposite is
	 * structured activity).
	 * 
	 * @param act
	 *            activity to check.
	 * @return bool information if it is basic activity.
	 */
	public static Boolean isBasicActivity(Activity act) {
		Boolean isBasic = Boolean.FALSE;
		for (Class<?> classType : BASIC_ACTIVITIES) {
			if (classType.isInstance(act)) {
				isBasic = Boolean.TRUE;
			}
		}
		return isBasic;
	}

	/**
	 * Checks if the activity given as parameter can change value of any
	 * variable in the process.
	 * 
	 * @param act
	 *            activity.
	 * @return bool information if activity can change variable value.
	 */
	public static Boolean isVarChangeActivity(Activity act) {
		Boolean isVarChange = Boolean.FALSE;
		if (act instanceof Receive || act instanceof Invoke) {
			isVarChange = Boolean.TRUE;
		}
		return isVarChange;
	}

	/**
	 * Checks if activity given as parameter is Flow instruction.
	 * 
	 * @param act
	 *            activity to check.
	 * @return bool information if activity is Flow instance.
	 */
	public static Boolean isFlowActivity(Activity act) {
		Boolean isFlow = Boolean.FALSE;
		if (act instanceof Flow) {
			isFlow = Boolean.TRUE;
		}
		return isFlow;
	}

}
